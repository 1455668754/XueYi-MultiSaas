<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          :preIcon="IconEnum.ADD"
          v-auth="MenuAuth.ADD"
          @click="handleCreate"
          type="primary"
        >
          新增
        </a-button>
        <a-button
          :preIcon="IconEnum.DELETE"
          v-auth="MenuAuth.DELETE"
          @click="handleDelete"
          type="primary"
          color="error"
        >
          删除
        </a-button>
        <a-button type="primary" @click="expandAll">展开全部</a-button>
        <a-button type="primary" @click="collapseAll">折叠全部</a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: IconEnum.VIEW,
              tooltip: '查看',
              auth: MenuAuth.SINGLE,
              ifShow: handleMenu.bind(null, record),
              onClick: handleView.bind(null, record),
            },
            {
              icon: IconEnum.EDIT,
              tooltip: '编辑',
              auth: MenuAuth.EDIT,
              ifShow: handleMenu.bind(null, record),
              onClick: handleEdit.bind(null, record),
            },
            {
              icon: IconEnum.DELETE,
              tooltip: '删除',
              auth: MenuAuth.DELETE,
              color: 'error',
              ifShow: handleMenu.bind(null, record),
              onClick: handleDelete.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>
    <MenuModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts">
  import { defineComponent, reactive } from 'vue';
  import { delMenuApi, listMenuApi } from '/@/api/system/authority/menu';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { BasicTable, TableAction, useTable } from '/@/components/Table';
  import { MenuAuth } from '/@/auth/system';
  import { columns, searchFormSchema } from './menu.data';
  import { COMMON_MENU, MenuDetailGo } from '/@/enums/system';
  import MenuModal from './MenuModal.vue';
  import { useUserStore } from '/@/store/modules/user';
  import { IconEnum } from '/@/enums/appEnum';

  export default defineComponent({
    name: 'MenuManagement',
    components: { BasicTable, MenuModal, TableAction },
    setup() {
      const { createMessage, createConfirm } = useMessage();
      const [registerModal, { openModal }] = useModal();
      const state = reactive<{
        ids: string[];
        idNames: string;
      }>({
        ids: [],
        idNames: '',
      });
      const [registerTable, { reload, expandAll, collapseAll }] = useTable({
        title: '菜单列表',
        api: listMenuApi,
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
        isTreeTable: true,
        pagination: false,
        canResize: false,
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
                return item.title;
              })
              .join(',');
          },
        },
      });

      /** 查看按钮 */
      function handleView(record: Recordable) {
        useUserStore().getRoutePath(MenuDetailGo, record.id);
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
        const delNames = record.title || state.idNames;
        if (!record.id && state.ids.length === 0) {
          createMessage.warning('请选择要操作的数据！');
        } else {
          createConfirm({
            iconType: 'warning',
            title: '提示',
            content: '是否确定要删除' + delNames + '?',
            onOk: () =>
              delMenuApi(delIds).then(() => {
                createMessage.success('删除' + delNames + '成功！');
                reload();
              }),
          });
        }
      }

      /** 菜单操作权限检验 */
      function handleMenu(record: Recordable): boolean {
        return record.id !== COMMON_MENU && useUserStore().useCommon(record.isCommon);
      }

      function handleSuccess() {
        reload();
      }

      return {
        IconEnum,
        MenuAuth,
        handleMenu,
        registerTable,
        registerModal,
        handleView,
        handleCreate,
        handleEdit,
        handleDelete,
        handleSuccess,
        expandAll,
        collapseAll,
      };
    },
  });
</script>
