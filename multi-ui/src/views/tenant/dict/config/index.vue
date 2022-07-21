<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          :preIcon="IconEnum.ADD"
          v-auth="ConfigAuth.ADD"
          @click="handleCreate"
          type="primary"
        >
          新增
        </a-button>
        <a-button
          :preIcon="IconEnum.DELETE"
          v-auth="ConfigAuth.DELETE"
          @click="handleDelete"
          type="primary"
          color="error"
        >
          删除
        </a-button>
        <a-button
          :preIcon="IconEnum.RESET"
          v-auth="ConfigAuth.EDIT"
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
              auth: ConfigAuth.SINGLE,
              onClick: handleView.bind(null, record),
            },
            {
              icon: IconEnum.EDIT,
              tooltip: '编辑',
              auth: ConfigAuth.EDIT,
              onClick: handleEdit.bind(null, record),
            },
            {
              icon: IconEnum.DELETE,
              tooltip: '删除',
              auth: ConfigAuth.DELETE,
              color: 'error',
              onClick: handleDelete.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>
    <ConfigModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts">
  import { defineComponent, reactive } from 'vue';
  import { listConfigApi, delConfigApi, refreshConfigApi } from '/@/api/tenant/dict/config';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { IconEnum } from '/@/enums/appEnum';
  import { BasicTable, TableAction, useTable } from '/@/components/Table';
  import { ConfigAuth } from '/@/auth/tenant';
  import { columns, searchFormSchema } from './config.data';
  import { ConfigDetailGo } from '/@/enums/tenant';
  import ConfigModal from './ConfigModal.vue';
  import { useUserStore } from '/@/store/modules/user';

  export default defineComponent({
    name: 'ConfigManagement',
    components: { BasicTable, ConfigModal, TableAction },
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
        title: '参数列表',
        api: listConfigApi,
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
        useUserStore().getRoutePath(ConfigDetailGo, record.id);
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
              delConfigApi(delIds).then(() => {
                createMessage.success('删除' + delNames + '成功！');
                reload();
              }),
          });
        }
      }

      /** 刷新缓存按钮 */
      function handleRefresh() {
        refreshConfigApi().then(() => createMessage.success('参数缓存刷新成功！'));
      }

      function handleSuccess() {
        reload();
      }

      return {
        IconEnum,
        ConfigAuth,
        registerTable,
        registerModal,
        handleView,
        handleCreate,
        handleEdit,
        handleDelete,
        handleRefresh,
        handleSuccess,
      };
    },
  });
</script>
