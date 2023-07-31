<template>
  <div class="h-full">
    <BasicTable ref="tableRef" @register="registerTable">
      <template #toolbar>
        <a-button
          :preIcon="IconEnum.ADD"
          v-auth="DictTypeAuth.DICT"
          @click="handleCreate"
          type="primary"
        >
          新增
        </a-button>
        <a-button
          :preIcon="IconEnum.DELETE"
          v-auth="DictTypeAuth.DICT"
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
    <!--    <DictDataModal @register="registerModal" @success="handleSuccess" />-->
  </div>
</template>

<script setup lang="ts">
  import { reactive, ref } from 'vue';
  import { delDictDataApi, listDictDataApi } from '@/api/tenant/dict/dictData.api';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { IconEnum } from '@/enums/basic';
  import { BasicTable, TableAction, useTable } from '/@/components/Table';
  import { DictTypeAuth } from '/@/auth/tenant';
  // import DictDataModal from './DictDataModal.vue';
  import { useRoute } from 'vue-router';
  import { isEmpty } from 'lodash-es';
  import { dataColumns, dataSearchFormSchema } from './dict.data';

  const route = useRoute();
  const { createMessage, createConfirm } = useMessage();
  const [registerModal, { openModal }] = useModal();
  const dictCode = ref(route.params.code as string);
  const state = reactive<{
    ids: string[];
    idNames: string;
  }>({
    ids: [],
    idNames: '',
  });
  const [registerTable, { reload, getForm }] = useTable({
    title: '字典数据列表',
    api: listDictDataApi,
    striped: false,
    useSearchForm: true,
    rowKey: 'id',
    bordered: true,
    showIndexColumn: true,
    columns: dataColumns,
    isCanResizeParent: true,
    formConfig: {
      labelWidth: 120,
      schemas: dataSearchFormSchema,
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
    beforeFetch(info) {
      if (isEmpty(info.code)) {
        info.code = dictCode.value;
        getForm().setFieldsValue({ code: dictCode.value });
      } else {
        dictCode.value = info.code;
      }
      return info;
    },
    rowSelection: {
      onChange: (selectedRowKeys, selectRows) => {
        state.ids = selectedRowKeys;
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
      dictCode: dictCode,
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

  function handleSuccess() {
    reload();
  }
</script>
