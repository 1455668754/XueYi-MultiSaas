<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          :preIcon="IconEnum.DELETE"
          v-auth="LoginLogAuth.DEL"
          @click="handleDelete"
          type="primary"
          color="error"
        >
          删除
        </a-button>
        <a-button
          :preIcon="IconEnum.DELETE"
          v-auth="LoginLogAuth.DEL"
          @click="handleClean"
          type="primary"
          color="warning"
        >
          清空
        </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: IconEnum.DELETE,
              tooltip: '删除',
              auth: LoginLogAuth.DEL,
              color: 'error',
              onClick: handleDelete.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>
  </div>
</template>

<script setup lang="ts">
  import { reactive } from 'vue';
  import {
    cleanLoginLogApi,
    delLoginLogApi,
    listLoginLogApi,
  } from '@/api/system/monitor/loginLog.api';
  import { useMessage } from '@/hooks/web/useMessage';
  import { IconEnum } from '@/enums';
  import { BasicTable, TableAction, useTable } from '@/components/Table';
  import { LoginLogAuth } from '@/auth/system/monitor';
  import { columns, searchFormSchema } from './loginLog.data';

  const { createMessage, createConfirm } = useMessage();
  const state = reactive<{
    ids: string[];
    idNames: string;
  }>({
    ids: [],
    idNames: '',
  });
  const [registerTable, { reload }] = useTable({
    title: '访问日志列表',
    api: listLoginLogApi,
    striped: false,
    useSearchForm: true,
    rowKey: 'id',
    bordered: true,
    showIndexColumn: true,
    columns,
    formConfig: {
      labelWidth: 120,
      schemas: searchFormSchema,
      fieldMapToTime: [['accessTime', ['accessTimeStart', 'accessTimeEnd'], 'YYYY-MM-DD']],
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
            return item.userName;
          })
          .join(',');
      },
    },
  });

  /** 清空按钮 */
  function handleClean() {
    createConfirm({
      iconType: 'warning',
      title: '提示',
      content: '是否确定要清空访问日志?',
      onOk: () => {
        cleanLoginLogApi();
        createMessage.success('清空成功！');
        reload();
      },
    });
  }

  /** 删除按钮 */
  function handleDelete(record: Recordable) {
    const delIds = record.id || state.ids;
    const delNames = record.userName || state.idNames;
    if (!record.id && state.ids.length === 0) {
      createMessage.warning('请选择要操作的数据！');
    } else {
      createConfirm({
        iconType: 'warning',
        title: '提示',
        content: '是否确定要删除' + delNames + '?',
        onOk: () =>
          delLoginLogApi(delIds).then(() => {
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
