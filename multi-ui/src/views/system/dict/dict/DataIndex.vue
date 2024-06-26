<template>
  <div>
    <BasicTable ref="tableRef" @register="registerTable">
      <template #toolbar>
        <a-button
          :preIcon="IconEnum.ADD"
          v-auth="DictTypeAuth.DICT"
          v-if="state.hasDict"
          @click="handleCreate"
          type="primary"
        >
          新增
        </a-button>
        <a-button
          :preIcon="IconEnum.DELETE"
          v-auth="DictTypeAuth.DICT"
          v-if="state.hasDict"
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
              icon: IconEnum.EDIT,
              tooltip: '编辑',
              auth: DictTypeAuth.DICT,
              onClick: handleEdit.bind(null, record),
            },
            {
              icon: IconEnum.DELETE,
              tooltip: '删除',
              auth: DictTypeAuth.DICT,
              color: 'error',
              onClick: handleDelete.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>
    <DictDataModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>

<script setup lang="ts">
  import { reactive } from 'vue';
  import { delDictDataApi, listDictDataApi } from '@/api/system/dict/dictData.api';
  import { useModal } from '@/components/Modal';
  import { useMessage } from '@/hooks/web/useMessage';
  import { COMMON_TENANT_ID, IconEnum } from '@/enums';
  import { BasicTable, TableAction, useTable } from '@/components/Table';
  import { DictTypeAuth } from '@/auth/system/dict';
  import DictDataModal from './DictDataModal.vue';
  import { dataColumns, dataSearchFormSchema } from './dict.data';
  import { DictTypeIM } from '@/model/system/dict';

  defineExpose({
    onChangeDictInfo,
  });

  const { createMessage, createConfirm } = useMessage();
  const [registerModal, { openModal }] = useModal();
  const state = reactive<{
    ids: string[];
    idNames: string;
    dictTypeInfo?: DictTypeIM;
    hasDict: boolean;
  }>({
    ids: [],
    idNames: '',
    hasDict: false,
  });
  const [registerTable, { reload }] = useTable({
    title: '字典明细',
    api: listDictDataApi,
    striped: false,
    useSearchForm: false,
    rowKey: 'id',
    bordered: true,
    showIndexColumn: true,
    columns: dataColumns,
    immediate: false,
    formConfig: {
      labelWidth: 120,
      schemas: dataSearchFormSchema,
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
    beforeFetch: (info) => {
      info.code = state.dictTypeInfo?.code || 'null';
      info.tenantId = state.dictTypeInfo?.enterpriseInfo?.id || COMMON_TENANT_ID;
      return info;
    },
    rowSelection: {
      onChange: (selectedRowKeys, selectRows) => {
        state.ids = selectedRowKeys as string[];
        state.idNames = selectRows
          .map((item) => {
            return item.label;
          })
          .join(',');
      },
    },
  });

  /** 新增按钮 */
  function handleCreate() {
    openModal(true, {
      dictTypeInfo: state.dictTypeInfo,
      isUpdate: false,
    });
  }

  /** 修改按钮 */
  function handleEdit(record: Recordable) {
    openModal(true, {
      dictTypeInfo: state.dictTypeInfo,
      record,
      isUpdate: true,
    });
  }

  /** 删除按钮 */
  function handleDelete(record: Recordable) {
    const delIds = record.id || state.ids;
    const delNames = record.label || state.idNames;
    if (!record.id && state.ids.length === 0) {
      createMessage.warning('请选择要操作的数据！');
    } else {
      createConfirm({
        iconType: 'warning',
        title: '提示',
        content: '是否确定要删除' + delNames + '?',
        onOk: () =>
          delDictDataApi(delIds).then(() => {
            createMessage.success('删除' + delNames + '成功！');
            reload();
          }),
      });
    }
  }

  function onChangeDictInfo(dict?: DictTypeIM) {
    state.hasDict = !!dict;
    if (dict) {
      dict.tenantId = dict?.enterpriseInfo?.id || COMMON_TENANT_ID;
    }
    state.dictTypeInfo = dict;
    reload();
  }

  function handleSuccess() {
    reload();
  }
</script>
