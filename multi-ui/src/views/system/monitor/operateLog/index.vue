<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          :preIcon="IconEnum.DELETE"
          v-auth="OperateLogAuth.DELETE"
          @click="handleDelete"
          type="primary"
          color="error"
        >
          删除
        </a-button>
        <a-button
          :preIcon="IconEnum.DELETE"
          v-auth="OperateLogAuth.DELETE"
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
              icon: IconEnum.VIEW,
              tooltip: '查看',
              auth: OperateLogAuth.SINGLE,
              onClick: handleView.bind(null, record),
            },
            {
              icon: IconEnum.DELETE,
              tooltip: '删除',
              auth: OperateLogAuth.DELETE,
              color: 'error',
              onClick: handleDelete.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>
    <OperateLogModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts">
  import { defineComponent, reactive } from 'vue';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { IconEnum } from '/@/enums/appEnum';
  import { BasicTable, TableAction, useTable } from '/@/components/Table';
  import { OperateLogAuth } from '/@/auth/system';
  import { columns, searchFormSchema } from './operateLog.data';
  import OperateLogModal from './OperateLogModal.vue';
  import {
    cleanOperateLogApi,
    delOperateLogApi,
    listOperateLogApi,
  } from '/@/api/system/monitor/operateLog';

  export default defineComponent({
    name: 'OperateLogManagement',
    components: { BasicTable, OperateLogModal, TableAction },
    setup() {
      const { createMessage, createConfirm } = useMessage();
      const [registerModal, { openModal }] = useModal();
      const state = reactive<{
        ids: (string | number)[];
        idNames: string;
      }>({
        ids: [],
        idNames: '',
      });
      const [registerTable, { reload }] = useTable({
        title: '操作日志列表',
        api: listOperateLogApi,
        striped: false,
        useSearchForm: true,
        rowKey: 'id',
        bordered: true,
        showIndexColumn: true,
        columns,
        formConfig: {
          labelWidth: 120,
          schemas: searchFormSchema,
          fieldMapToTime: [['operateTime', ['operateTimeStart', 'operateTimeEnd'], 'YYYY-MM-DD']],
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
                return item.id;
              })
              .join(',');
          },
        },
      });

      /** 查看按钮 */
      function handleView(record: Recordable) {
        openModal(true, { record });
      }

      /** 清空按钮 */
      function handleClean() {
        createConfirm({
          iconType: 'warning',
          title: '提示',
          content: '是否确定要清空操作日志?',
          onOk: () => {
            cleanOperateLogApi();
            createMessage.success('清空成功！');
            reload();
          },
        });
      }

      /** 删除按钮 */
      function handleDelete(record: Recordable) {
        const delIds = record.id || state.ids;
        const delNames = record.id || state.idNames;
        if (!record.id && state.ids.length === 0) {
          createMessage.warning('请选择要操作的数据！');
        } else {
          createConfirm({
            iconType: 'warning',
            title: '提示',
            content: '是否确定要删除' + delNames + '?',
            onOk: () =>
              delOperateLogApi(delIds).then(() => {
                createMessage.success('删除' + delNames + '成功！');
                reload();
              }),
          });
        }
      }

      function handleSuccess() {
        reload();
      }

      return {
        IconEnum,
        OperateLogAuth,
        registerTable,
        registerModal,
        handleView,
        handleDelete,
        handleClean,
        handleSuccess,
      };
    },
  });
</script>
