<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <OrganizeTree class="w-1/4 xl:w-1/5" @select="handleSelect" />
    <BasicTable @register="registerTable" class="w-3/4 xl:w-4/5" :searchInfo="searchInfo">
      <template #toolbar>
        <a-button
          :preIcon="IconEnum.ADD"
          v-auth="UserAuth.ADD"
          @click="handleCreate"
          type="primary"
        >
          新增
        </a-button>
        <a-button
          :preIcon="IconEnum.DELETE"
          v-auth="UserAuth.DEL"
          @click="handleDelete"
          type="primary"
          color="error"
        >
          删除
        </a-button>
        <a-button
          :preIcon="IconEnum.EXPORT"
          v-auth="UserAuth.EXPORT"
          @click="handleExport"
          type="primary"
          color="warning"
        >
          导出
        </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: IconEnum.VIEW,
              tooltip: '查看',
              auth: UserAuth.SINGLE,
              ifShow: handleHidden.bind(null, record),
              onClick: handleView.bind(null, record),
            },
            {
              icon: IconEnum.EDIT,
              tooltip: '编辑',
              auth: UserAuth.EDIT,
              ifShow: handleHidden.bind(null, record),
              onClick: handleEdit.bind(null, record),
            },
            {
              icon: IconEnum.AUTH,
              tooltip: '分配角色',
              auth: UserAuth.AUTH,
              ifShow: handleHidden.bind(null, record),
              onClick: handleRole.bind(null, record),
            },
            {
              icon: IconEnum.PASSWORD,
              tooltip: '重置密码',
              auth: UserAuth.RES_PWD,
              ifShow: handleHidden.bind(null, record),
              onClick: handleResPwd.bind(null, record),
            },
            {
              icon: IconEnum.DELETE,
              tooltip: '删除',
              auth: UserAuth.DEL,
              color: 'error',
              ifShow: handleHidden.bind(null, record),
              onClick: handleDelete.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>
    <UserModal @register="registerModal" @success="handleSuccess" />
    <UserRoleModal @register="registerRoleModal" @success="handleSuccess" />
    <UserResPwdModal @register="registerResPwdModal" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { reactive } from 'vue';
  import { delUserApi, exportUserApi, listUserApi } from '@/api/system/organize/user.api';
  import { useModal } from '@/components/Modal';
  import { useMessage } from '@/hooks/web/useMessage';
  import { BasicTable, TableAction, useTable } from '@/components/Table';
  import { UserAuth } from '@/auth/system/organize';
  import { columns, searchFormSchema } from './user.data';
  import { UserDetailGo } from '@/enums/system/organize';
  import UserModal from './UserModal.vue';
  import UserRoleModal from './UserRoleModal.vue';
  import { useUserStore } from '@/store/modules/user';
  import PageWrapper from '@/components/Page/src/PageWrapper.vue';
  import OrganizeTree from './OrganizeTree.vue';
  import { UserPM } from '@/model/system/organize';
  import UserResPwdModal from './UserResPwdModal.vue';
  import { isEqual } from 'lodash-es';
  import { IconEnum, UserTypeEnum } from '@/enums';

  const searchInfo = reactive<Recordable>({});
  const { createMessage, createConfirm } = useMessage();
  const [registerModal, { openModal }] = useModal();
  const [registerRoleModal, { openModal: openRoleModal }] = useModal();
  const [registerResPwdModal, { openModal: openResPwdModal }] = useModal();
  const state = reactive<{
    ids: string[];
    idNames: string;
  }>({
    ids: [],
    idNames: '',
  });
  const [registerTable, { reload, getForm }] = useTable({
    title: '用户列表',
    api: listUserApi,
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
        state.ids = selectedRowKeys as string[];
        state.idNames = selectRows
          .map((item) => {
            return item.nickName;
          })
          .join(',');
      },
    },
  });

  /** 查看按钮 */
  function handleView(record: Recordable) {
    useUserStore().getRoutePath(UserDetailGo, record.id);
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

  /** 角色分配按钮 */
  function handleRole(record: Recordable) {
    openRoleModal(true, { record });
  }

  /** 密码重置按钮 */
  function handleResPwd(record: Recordable) {
    openResPwdModal(true, {
      record,
    });
  }

  /** 删除按钮 */
  function handleDelete(record: Recordable) {
    const delIds = record.id || state.ids;
    const delNames = record.nickName || state.idNames;
    if (!record.id && state.ids.length === 0) {
      createMessage.warning('请选择要操作的数据！');
    } else {
      createConfirm({
        iconType: 'warning',
        title: '提示',
        content: '是否确定要删除' + delNames + '?',
        onOk: () =>
          delUserApi(delIds).then(() => {
            createMessage.success('删除' + delNames + '成功！');
            reload();
          }),
      });
    }
  }

  /** 导出按钮 */
  function handleExport() {
    const value = getForm().getFieldsValue() as UserPM;
    exportUserApi(value);
  }

  /** 隐藏校验 */
  function handleHidden(record: Recordable) {
    return !(isEqual(record.userType, UserTypeEnum.ADMIN) && useUserStore().isNotAdmin);
  }

  /** 组织树选择 */
  function handleSelect(deptId = '', postId = '') {
    searchInfo.deptId = deptId;
    searchInfo.postId = postId;
    reload();
  }

  function handleSuccess() {
    reload();
  }
</script>
