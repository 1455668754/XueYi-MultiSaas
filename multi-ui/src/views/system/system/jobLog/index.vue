<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          :preIcon="IconEnum.DELETE"
          v-auth="JobAuth.DELETE"
          @click="handleClean"
          type="primary"
          color="error"
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
              auth: JobAuth.SINGLE,
              onClick: handleView.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>
    <JobLogModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import { useModal } from '@/components/Modal';
  import { IconEnum } from '@/enums';
  import { BasicTable, TableAction, useTable } from '@/components/Table';
  import { JobAuth } from '@/auth/system/system';
  import { columns, searchFormSchema } from './jobLog.data';
  import JobLogModal from './JobLogModal.vue';
  import { cleanJobLogApi, listJobLogApi } from '@/api/system/system/jobLog.api';
  import { useRoute } from 'vue-router';
  import { useMessage } from '@/hooks/web/useMessage';
  import { isEmpty } from 'lodash-es';

  const route = useRoute();
  const jobId = ref(route.params.id as string);

  const { createMessage, createConfirm } = useMessage();
  const [registerModal, { openModal }] = useModal();
  const [registerTable, { reload, getForm }] = useTable({
    title: '调度日志列表',
    api: listJobLogApi,
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
      width: 120,
      title: '操作',
      dataIndex: 'action',
      slots: { customRender: 'action' },
    },
    beforeFetch(info) {
      if (isEmpty(info.jobId)) {
        info.jobId = jobId.value;
        jobId.value = '';
        getForm().setFieldsValue({ jobId: info.jobId });
      }
      return info;
    },
  });

  /** 查看按钮 */
  function handleView(record: Recordable) {
    openModal(true, {
      record,
      isUpdate: true,
    });
  }

  /** 清空按钮 */
  function handleClean() {
    createConfirm({
      iconType: 'warning',
      title: '提示',
      content: '是否确定要清空所有调度日志?',
      onOk: () => {
        cleanJobLogApi();
        createMessage.success('清空成功！');
        reload();
      },
    });
  }

  function handleSuccess() {
    reload();
  }
</script>
