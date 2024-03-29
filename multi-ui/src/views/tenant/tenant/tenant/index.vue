<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          :preIcon="IconEnum.ADD"
          v-auth="TenantAuth.ADD"
          @click="handleCreate"
          type="primary"
        >
          新增
        </a-button>
        <a-button
          :preIcon="IconEnum.DELETE"
          v-auth="TenantAuth.DEL"
          @click="handleDelete"
          type="primary"
          color="error"
        >
          删除
        </a-button>
      </template>
      <template #logo="{ text }">
        <Image :height="30" :width="30" :src="text" />
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: IconEnum.VIEW,
              tooltip: '查看',
              auth: TenantAuth.SINGLE,
              onClick: handleView.bind(null, record),
            },
            {
              icon: IconEnum.EDIT,
              tooltip: '编辑',
              auth: TenantAuth.EDIT,
              onClick: handleEdit.bind(null, record),
            },
            {
              icon: IconEnum.AUTH,
              tooltip: '菜单权限',
              auth: TenantAuth.AUTH,
              ifShow: () => record.isLessor === TenantTypeEnum.NORMAL,
              onClick: handleAuth.bind(null, record),
            },
            {
              icon: IconEnum.DELETE,
              tooltip: '删除',
              auth: TenantAuth.DEL,
              color: 'error',
              onClick: handleDelete.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>
    <TenantModal @register="registerModal" @success="handleSuccess" />
    <TenantInitModal @register="initRegisterModal" @success="handleSuccess" />
    <TenantAuthGroupModal @register="authGroupRegisterModal" @success="handleSuccess" />
  </div>
</template>

<script setup lang="ts">
  import { reactive } from 'vue';
  import { delTenantApi, listTenantApi } from '@/api/tenant/tenant/tenant.api';
  import { useModal } from '@/components/Modal';
  import { useMessage } from '@/hooks/web/useMessage';
  import { useGo } from '@/hooks/web/usePage';
  import { BasicTable, TableAction, useTable } from '@/components/Table';
  import { TenantAuth } from '@/auth/tenant/tenant';
  import { columns, searchFormSchema } from './tenant.data';
  import { TenantDetailGo } from '@/enums/tenant/tenant';
  import TenantModal from './TenantModal.vue';
  import TenantInitModal from './TenantInitModal.vue';
  import { IconEnum, TenantTypeEnum } from '@/enums';
  import { Image } from 'ant-design-vue';
  import TenantAuthGroupModal from './TenantAuthGroupModal.vue';

  const go = useGo();
  const { createMessage, createConfirm } = useMessage();
  const [registerModal, { openModal: basicOpenModal }] = useModal();
  const [initRegisterModal, { openModal: initOpenModal }] = useModal();
  const [authGroupRegisterModal, { openModal: authGroupOpenModal }] = useModal();
  const state = reactive<{
    ids: string[];
    idNames: string;
  }>({
    ids: [],
    idNames: '',
  });
  const [registerTable, { reload }] = useTable({
    title: '租户列表',
    api: listTenantApi,
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
            return item.name;
          })
          .join(',');
      },
    },
  });

  /** 查看按钮 */
  function handleView(record: Recordable) {
    go(TenantDetailGo + record.id);
  }

  /** 新增按钮 */
  function handleCreate() {
    initOpenModal(true, {});
  }

  /** 修改按钮 */
  function handleEdit(record: Recordable) {
    basicOpenModal(true, {
      record,
    });
  }

  /** 权限组分配按钮 */
  function handleAuth(record: Recordable) {
    authGroupOpenModal(true, {
      record,
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
          delTenantApi(delIds).then(() => {
            createMessage.success('删除' + delNames + '成功！');
            reload();
          }),
      });
    }
  }

  function handleSuccess() {
    reload();
  }
</script>
