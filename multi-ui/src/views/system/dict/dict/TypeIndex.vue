<template>
  <div class="h-full">
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
              auth: DictTypeAuth.DEL,
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

<script setup lang="ts">
  import { onMounted, reactive } from 'vue';
  import { delDictTypeApi, listDictTypeApi, refreshDictApi } from '@/api/system/dict/dictType.api';
  import { useModal } from '@/components/Modal';
  import { useMessage } from '@/hooks/web/useMessage';
  import { IconEnum } from '@/enums';
  import { BasicTable, TableAction, useTable } from '@/components/Table';
  import { DictTypeAuth } from '@/auth/system/dict';
  import DictTypeModal from './DictTypeModal.vue';
  import { typeColumns, typeSearchFormSchema } from './dict.data';
  import { listTenantApi } from '@/api/tenant/tenant/tenant.api';
  import { TenantIM } from '@/model/tenant/tenant';

  const emit = defineEmits(['dict-change', 'dict-del']);
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
    title: '字典类型列表',
    api: listDictTypeApi,
    striped: false,
    useSearchForm: true,
    rowKey: 'id',
    bordered: true,
    clickToRowSelect: true,
    showIndexColumn: true,
    columns: typeColumns,
    isCanResizeParent: true,
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

  /** 新增按钮 */
  function handleCreate() {
    openModal(true, {
      isUpdate: false,
    });
  }

  /** 字典管理按钮 */
  function handleDict(record: Recordable) {
    emit('dict-change', record);
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
          delDictTypeApi(delIds).then(() => {
            createMessage.success('删除' + delNames + '成功！');
            emit('dict-del', delIds);
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
