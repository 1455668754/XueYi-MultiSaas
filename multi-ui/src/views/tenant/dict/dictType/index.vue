<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          :preIcon="IconEnum.ADD"
          v-auth="DictTypeAuth.ADD"
          @click="handleCreate"
          type="primary"
        >
          新增
        </a-button>
        <a-button
          :preIcon="IconEnum.DELETE"
          v-auth="DictTypeAuth.DELETE"
          @click="handleDelete"
          type="primary"
          color="error"
        >
          删除
        </a-button>
        <a-button
          :preIcon="IconEnum.RESET"
          v-auth="DictTypeAuth.EDIT"
          @click="handleRefresh"
          type="primary"
          color="error"
        >
          刷新缓存
        </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: IconEnum.VIEW,
              tooltip: '查看',
              auth: DictTypeAuth.SINGLE,
              onClick: handleView.bind(null, record),
            },
            {
              icon: IconEnum.EDIT,
              tooltip: '编辑',
              auth: DictTypeAuth.EDIT,
              onClick: handleEdit.bind(null, record),
            },
            {
              icon: 'ant-design:read-outlined',
              tooltip: '字典数据',
              auth: DictTypeAuth.DICT,
              onClick: handleDict.bind(null, record),
            },
            {
              icon: IconEnum.DELETE,
              tooltip: '删除',
              auth: DictTypeAuth.DELETE,
              color: 'error',
              onClick: handleDelete.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>
    <DictTypeModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts">
  import { defineComponent, reactive } from 'vue';
  import { listDictTypeApi, delDictTypeApi, refreshDictApi } from '/@/api/tenant/dict/dictType';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { IconEnum } from '/@/enums/appEnum';
  import { BasicTable, TableAction, useTable } from '/@/components/Table';
  import { DictTypeAuth } from '/@/auth/tenant';
  import { columns, searchFormSchema } from './dictType.data';
  import { DictTypeDetailGo, DictDataIndexGo } from '/@/enums/tenant';
  import DictTypeModal from './DictTypeModal.vue';
  import { useUserStore } from '/@/store/modules/user';

  export default defineComponent({
    name: 'DictTypeManagement',
    components: { BasicTable, DictTypeModal, TableAction },
    setup() {
      const { createMessage, createConfirm } = useMessage();
      const [registerModal, { openModal }] = useModal();
      const state = reactive<{
        ids: (string | number)[];
        idNames: string;
      }>({
        ids: [],
        idNames: '',
      });
      const [registerTable, { reload }] = useTable({
        title: '字典类型列表',
        api: listDictTypeApi,
        striped: false,
        useSearchForm: true,
        rowKey: 'id',
        bordered: true,
        showIndexColumn: true,
        columns,
        formConfig: {
          labelWidth: 120,
          schemas: searchFormSchema,
        },
        showTableSetting: true,
        tableSetting: {
          fullScreen: true,
        },
        actionColumn: {
          width: 220,
          title: '操作',
          dataIndex: 'action',
          slots: { customRender: 'action' },
        },
        rowSelection: {
          onChange: (selectedRowKeys, selectRows) => {
            state.ids = selectedRowKeys;
            state.idNames = selectRows
              .map((item) => {
                return item.name;
              })
              .join(',');
          },
        },
      });

      /** 查看按钮 */
      function handleView(record: Recordable) {
        useUserStore().getRoutePath(DictTypeDetailGo, record.id);
      }

      /** 新增按钮 */
      function handleCreate() {
        openModal(true, {
          isUpdate: false,
        });
      }

      /** 修改按钮 */
      function handleEdit(record: Recordable) {
        openModal(true, {
          record,
          isUpdate: true,
        });
      }

      /** 字典管理按钮 */
      function handleDict(record: Recordable) {
        useUserStore().getRoutePath(DictDataIndexGo, record.code);
      }

      /** 删除按钮 */
      function handleDelete(record: Recordable) {
        const delIds = record.id || state.ids;
        const delNames = record.name || state.idNames;
        if (!record.id && state.ids.length === 0) {
          createMessage.warning('请选择要操作的数据！');
        } else {
          createConfirm({
            iconType: 'warning',
            title: '提示',
            content: '是否确定要删除' + delNames + '?',
            onOk: () =>
              delDictTypeApi(delIds).then(() => {
                createMessage.success('删除' + delNames + '成功！');
                reload();
              }),
          });
        }
      }

      /** 刷新缓存按钮 */
      function handleRefresh() {
        refreshDictApi().then(() => createMessage.success('字典缓存刷新成功！'));
      }

      function handleSuccess() {
        reload();
      }

      return {
        IconEnum,
        DictTypeAuth,
        registerTable,
        registerModal,
        handleView,
        handleCreate,
        handleEdit,
        handleDict,
        handleDelete,
        handleRefresh,
        handleSuccess,
      };
    },
  });
</script>
