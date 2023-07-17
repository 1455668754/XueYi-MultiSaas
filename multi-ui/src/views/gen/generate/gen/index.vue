<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          :preIcon="IconEnum.UPLOAD"
          v-auth="GenAuth.IMPORT"
          type="primary"
          @click="handleImport"
        >
          导入
        </a-button>
        <a-button
          :preIcon="IconEnum.DELETE"
          v-auth="GenAuth.DELETE"
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
              icon: IconEnum.PREVIEW,
              tooltip: '预览',
              auth: GenAuth.PREVIEW,
              onClick: handlePreview.bind(null, record),
            },
            {
              icon: IconEnum.EDIT,
              tooltip: '编辑',
              auth: GenAuth.EDIT,
              onClick: handleEdit.bind(null, record),
            },
            {
              icon: IconEnum.DOWNLOAD,
              tooltip: '下载',
              auth: GenAuth.CODE,
              onClick: handleDownload.bind(null, record),
            },
            {
              icon: IconEnum.EXPORT,
              tooltip: '生成代码',
              auth: GenAuth.CODE,
              onClick: handleGenerate.bind(null, record),
            },
            {
              icon: IconEnum.DELETE,
              tooltip: '删除',
              color: 'error',
              auth: GenAuth.DELETE,
              onClick: handleDelete.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>
    <GenModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>

<script setup lang="ts">
  import { reactive } from 'vue';
  import GenModal from './GenModal.vue';
  import { columns, searchFormSchema } from './gen.data';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { GenAuth } from '/@/auth/gen';
  import {
    delForceGenApi,
    downloadGenApi,
    generateGenApi,
    listGenApi,
  } from '@/api/gen/generate/gen.api';
  import { BasicTable, TableAction, useTable } from '/@/components/Table';
  import { GenCodeDetailGo, GenGenerateDetailGo } from '@/enums/gen';
  import { useUserStore } from '/@/store/modules/user';
  import { IconEnum } from '@/enums/basic';

  const state = reactive<{
    ids: string[];
    idNames: string;
  }>({
    ids: [],
    idNames: '',
  });

  const { createMessage, createConfirm } = useMessage();
  const [registerModal, { openModal }] = useModal();
  const [registerTable, { reload }] = useTable({
    title: '代码生成列表',
    api: listGenApi,
    columns,
    formConfig: {
      labelWidth: 120,
      schemas: searchFormSchema,
    },
    striped: false,
    useSearchForm: true,
    showTableSetting: true,
    tableSetting: {
      fullScreen: true,
    },
    rowKey: 'id',
    bordered: true,
    showIndexColumn: true,
    actionColumn: {
      width: 220,
      title: '操作',
      dataIndex: 'action',
      slots: { customRender: 'action' },
      fixed: undefined,
    },
    rowSelection: {
      onChange: (selectedRowKeys, selectRows) => {
        state.ids = selectedRowKeys;
        state.idNames = selectRows
          .map((item) => {
            return item.comment;
          })
          .join(',');
      },
    },
  });

  /** 预览按钮 */
  function handlePreview(record: Recordable) {
    useUserStore().getRoutePath(GenCodeDetailGo, record.id);
  }

  /** 导入按钮 */
  function handleImport() {
    openModal(true);
  }

  /** 修改按钮 */
  function handleEdit(record: Recordable) {
    useUserStore().getRoutePath(GenGenerateDetailGo, record.id);
  }

  /** 下载按钮 */
  function handleDownload(record: Recordable) {
    downloadGenApi(record.id, 'xueyi.zip')
      .then(() => createMessage.success('下载成功！'))
      .catch(() => createMessage.warning('下载异常，请联系管理员！'));
  }

  /** 生成代码按钮 */
  function handleGenerate(record: Recordable) {
    generateGenApi(record.id)
      .then(() => createMessage.success('生成成功！'))
      .catch(() => createMessage.warning('生成异常，请联系管理员！'));
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
        title: 'Tip',
        content: '是否确定要删除' + delNames + '?',
        onOk: () =>
          delForceGenApi(delIds).then(() => {
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
