<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          :preIcon="IconEnum.ADD"
          v-auth="AuthGroupAuth.ADD"
          @click="handleCreate"
          v-if="isLessor"
          type="primary"
        >
          新增
        </a-button>
        <a-button
          :preIcon="IconEnum.DELETE"
          v-auth="AuthGroupAuth.DELETE"
          @click="handleDelete"
          v-if="isLessor"
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
              auth: AuthGroupAuth.SINGLE,
              ifShow: () => isLessor,
              onClick: handleView.bind(null, record),
            },
            {
              icon: IconEnum.EDIT,
              tooltip: '编辑',
              auth: AuthGroupAuth.EDIT,
              ifShow: () => isLessor,
              onClick: handleEdit.bind(null, record),
            },
            {
              icon: IconEnum.DELETE,
              tooltip: '删除',
              auth: AuthGroupAuth.DELETE,
              color: 'error',
              ifShow: () => isLessor,
              onClick: handleDelete.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>
    <AuthGroupModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>

<script setup lang="ts">
  import { computed, reactive } from 'vue';
  import { delAuthGroupApi, listAuthGroupApi } from '@/api/system/authority/authGroup.api';
  import { useModal } from '@/components/Modal';
  import AuthGroupModal from './AuthGroupModal.vue';
  import { useMessage } from '@/hooks/web/useMessage';
  import { IconEnum } from '@/enums';
  import { BasicTable, TableAction, useTable } from '@/components/Table';
  import { AuthGroupAuth } from '@/auth/system/authority';
  import { columns, searchFormSchema } from './authGroup.data';
  import { AuthGroupDetailGo } from '@/enums/system/authority';
  import { useUserStore } from '@/store/modules/user';

  const { createMessage, createConfirm } = useMessage();
  const [registerModal, { openModal }] = useModal();
  const useUser = useUserStore();
  const state = reactive<{
    ids: string[];
    idNames: string;
  }>({
    ids: [],
    idNames: '',
  });

  const [registerTable, { reload }] = useTable({
    title: '权限组列表',
    api: listAuthGroupApi,
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

  const isLessor = computed(() => useUser.isLessor);

  /** 查看按钮 */
  function handleView(record: Recordable) {
    useUserStore().getRoutePath(AuthGroupDetailGo, record?.id);
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
    const delIds = record?.id || state.ids;
    const delNames = record?.name || state.idNames;
    if (!record?.id && state.ids.length === 0) {
      createMessage.warning('请选择要操作的数据！');
    } else {
      createConfirm({
        iconType: 'warning',
        title: '提示',
        content: '是否确定要删除' + delNames + '?',
        onOk: () =>
          delAuthGroupApi(delIds).then(() => {
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
