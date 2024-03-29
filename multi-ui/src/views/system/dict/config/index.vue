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
          v-auth="ConfigAuth.DEL"
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
              auth: ConfigAuth.DEL,
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

<script setup lang="ts">
  import { onMounted, reactive } from 'vue';
  import { delConfigApi, listConfigApi, refreshConfigApi } from '@/api/system/dict/config.api';
  import { useModal } from '@/components/Modal';
  import { useMessage } from '@/hooks/web/useMessage';
  import { IconEnum } from '@/enums';
  import { BasicTable, TableAction, useTable } from '@/components/Table';
  import { ConfigAuth } from '@/auth/system/dict';
  import { columns, searchFormSchema } from './config.data';
  import { ConfigDetailGo } from '@/enums/system/dict';
  import ConfigModal from './ConfigModal.vue';
  import { useUserStore } from '@/store/modules/user';
  import { listTenantApi } from '@/api/tenant/tenant/tenant.api';
  import { TenantIM } from '@/model/tenant/tenant';

  const { createMessage, createConfirm } = useMessage();
  const [registerModal, { openModal }] = useModal();
  const state = reactive<{
    ids: string[];
    idNames: string;
  }>({
    ids: [],
    idNames: '',
  });
  const [registerTable, { reload, getForm }] = useTable({
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
        state.ids = selectedRowKeys as string[];
        state.idNames = selectRows
          .map((item) => {
            return item.name;
          })
          .join(',');
      },
    },
  });

  onMounted(() => {
    initTenantList();
  });

  async function initTenantList() {
    const tenantList = await listTenantApi();
    tenantList.items.push({
      id: '0',
      nick: '通用',
    } as TenantIM);
    getForm().updateSchema({
      field: 'tenantId',
      componentProps: {
        options: tenantList.items,
      },
    });
  }

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
</script>
