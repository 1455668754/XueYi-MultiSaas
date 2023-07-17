<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          :preIcon="IconEnum.DELETE"
          v-auth="OnlineAuth.DELETE"
          @click="handleDelete"
          type="primary"
          color="error"
        >
          强退
        </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: IconEnum.DELETE,
              tooltip: '强退',
              auth: OnlineAuth.FORCE_LOGOUT,
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
  import { delOnlineApi, listOnlineApi } from '@/api/system/monitor/online.api';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { IconEnum } from '@/enums/basic';
  import { BasicTable, TableAction, useTable } from '/@/components/Table';
  import { OnlineAuth } from '/@/auth/system';
  import { columns, searchFormSchema } from './online.data';

  const { createMessage, createConfirm } = useMessage();
  const state = reactive<{
    ids: string[];
    idNames: string;
  }>({
    ids: [],
    idNames: '',
  });
  const [registerTable, { reload }] = useTable({
    title: '在线用户列表',
    api: listOnlineApi,
    striped: false,
    useSearchForm: true,
    rowKey: 'tokenId',
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
        state.ids = selectedRowKeys;
        state.idNames = selectRows
          .map((item) => {
            return item.userNick;
          })
          .join(',');
      },
    },
  });

  /** 删除按钮 */
  function handleDelete(record: Recordable) {
    const delIds = record.tokenId || state.ids;
    const delNames = record.userNick || state.idNames;
    if (!record.tokenId && state.ids.length === 0) {
      createMessage.warning('请选择要操作的数据！');
    } else {
      createConfirm({
        iconType: 'warning',
        title: '提示',
        content: '是否确定要删除' + delNames + '?',
        onOk: () =>
          delOnlineApi(delIds).then(() => {
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
