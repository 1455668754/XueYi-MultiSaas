<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          :preIcon="IconEnum.ADD"
          v-auth="RoleAuth.ADD"
          @click="handleCreate"
          type="primary"
        >
          新增
        </a-button>
        <a-button
          :preIcon="IconEnum.DELETE"
          v-auth="RoleAuth.DELETE"
          @click="handleDelete"
          type="primary"
          color="error"
        >
          删除
        </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: IconEnum.VIEW,
              tooltip: '查看',
              auth: RoleAuth.SINGLE,
              onClick: handleView.bind(null, record),
            },
            {
              icon: IconEnum.EDIT,
              tooltip: '编辑',
              auth: RoleAuth.EDIT,
              onClick: handleEdit.bind(null, record),
            },
            {
              icon: IconEnum.AUTH,
              tooltip: '功能权限',
              auth: RoleAuth.AUTH,
              onClick: handleAuthEdit.bind(null, record),
            },
            {
              icon: IconEnum.DATA,
              tooltip: '数据权限',
              auth: RoleAuth.AUTH,
              onClick: handleDataEdit.bind(null, record),
            },
            {
              icon: IconEnum.DELETE,
              tooltip: '删除',
              auth: RoleAuth.DELETE,
              color: 'error',
              onClick: handleDelete.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>
    <RoleInitModal @register="initRegisterModal" @success="handleSuccess" />
    <RoleModal @register="registerModal" @success="handleSuccess" />
    <RoleAuthModal @register="authRegisterModal" @success="handleSuccess" />
    <RoleDataModal @register="organizeRegisterModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts">
  import { defineComponent, reactive } from 'vue';
  import { delRoleApi, listRoleApi } from '/@/api/system/authority/role';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { IconEnum } from '/@/enums/appEnum';
  import { BasicTable, TableAction, useTable } from '/@/components/Table';
  import { RoleAuth } from '/@/auth/system';
  import { columns, searchFormSchema } from './role.data';
  import { RoleDetailGo } from '/@/enums/system';
  import RoleModal from './RoleModal.vue';
  import { useUserStore } from '/@/store/modules/user';
  import RoleInitModal from './RoleInitModal.vue';
  import RoleAuthModal from './RoleAuthModal.vue';
  import RoleDataModal from './RoleDataModal.vue';

  export default defineComponent({
    name: 'RoleManagement',
    components: {
      BasicTable,
      RoleModal,
      RoleInitModal,
      RoleAuthModal,
      RoleDataModal,
      TableAction,
    },
    setup() {
      const { createMessage, createConfirm } = useMessage();
      const [initRegisterModal, { openModal: initOpenModal }] = useModal();
      const [registerModal, { openModal: basicOpenModal }] = useModal();
      const [authRegisterModal, { openModal: authOpenModal }] = useModal();
      const [organizeRegisterModal, { openModal: organizeOpenModal }] = useModal();
      const state = reactive<{
        ids: string[];
        idNames: string;
      }>({
        ids: [],
        idNames: '',
      });
      const [registerTable, { reload }] = useTable({
        title: '角色列表',
        api: listRoleApi,
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
        useUserStore().getRoutePath(RoleDetailGo, record.id);
      }

      /** 新增按钮 */
      function handleCreate() {
        initOpenModal(true, {});
      }

      /** 修改按钮 */
      function handleEdit(record: Recordable) {
        basicOpenModal(true, { record });
      }

      /** 功能权限按钮 */
      function handleAuthEdit(record: Recordable) {
        authOpenModal(true, { record });
      }

      /** 数据权限按钮 */
      function handleDataEdit(record: Recordable) {
        organizeOpenModal(true, { record });
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
              delRoleApi(delIds).then(() => {
                createMessage.success('删除' + delNames + '成功！');
                reload();
              }),
          });
        }
      }

      function handleSuccess() {
        reload();
      }

      return {
        IconEnum,
        RoleAuth,
        registerTable,
        registerModal,
        initRegisterModal,
        authRegisterModal,
        organizeRegisterModal,
        handleView,
        handleCreate,
        handleEdit,
        handleAuthEdit,
        handleDataEdit,
        handleDelete,
        handleSuccess,
      };
    },
  });
</script>
