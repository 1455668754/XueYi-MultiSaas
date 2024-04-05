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
          v-auth="DictTypeAuth.DEL"
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
              icon: IconEnum.DELETE,
              tooltip: '删除',
              auth: DictTypeAuth.DEL,
              color: 'error',
              onClick: handleDelete.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>
    <DictTypeDrawer @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>

<script setup lang="ts">
  import { onMounted, reactive } from 'vue';
  import { useMessage } from '@/hooks/web/useMessage';
  import { IconEnum } from '@/enums';
  import { BasicTable, TableAction, useTable } from '@/components/Table';
  import { DictTypeAuth } from '@/auth/system/dict';
  import { typeColumns, typeSearchFormSchema } from './dict.data';
  import { ConfigDetailGo } from '@/enums/system/dict';
  import { useUserStore } from '@/store/modules/user';
  import { listTenantApi } from '@/api/tenant/tenant/tenant.api';
  import { TenantIM } from '@/model/tenant/tenant';
  import { delDictTypeApi, listDictTypeApi, refreshDictApi } from '@/api/system/dict/dictType.api';
  import { useDrawer } from '@/components/Drawer';
  import DictTypeDrawer from './DictTypeDrawer.vue';

  const { createMessage, createConfirm } = useMessage();
  const [registerDrawer, { openDrawer }] = useDrawer();

  const state = reactive<{
    ids: string[];
    idNames: string;
  }>({
    ids: [],
    idNames: '',
  });
  const [registerTable, { reload, getForm }] = useTable({
    api: listDictTypeApi,
    striped: false,
    useSearchForm: true,
    rowKey: 'id',
    bordered: true,
    showIndexColumn: true,
    columns: typeColumns,
    formConfig: {
      labelWidth: 120,
      schemas: typeSearchFormSchema,
    },
    showTableSetting: true,
    tableSetting: {
      fullScreen: true,
    },
    actionColumn: {
      width: 120,
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
    openDrawer(true, {
      isUpdate: false,
    });
  }

  /** 修改按钮 */
  function handleEdit(record: Recordable) {
    openDrawer(true, {
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
</script>
