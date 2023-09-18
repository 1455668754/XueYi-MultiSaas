<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button :preIcon="IconEnum.ADD" v-auth="JobAuth.ADD" @click="handleCreate" type="primary">
          新增
        </a-button>
        <a-button
          :preIcon="IconEnum.DELETE"
          v-auth="JobAuth.DELETE"
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
              icon: IconEnum.VIEW,
              tooltip: '查看',
              auth: JobAuth.SINGLE,
              onClick: handleView.bind(null, record),
            },
            {
              icon: IconEnum.EDIT,
              tooltip: '编辑',
              auth: JobAuth.EDIT,
              onClick: handleEdit.bind(null, record),
            },
            {
              icon: 'ant-design:caret-right-filled',
              tooltip: '执行一次',
              auth: JobAuth.EDIT,
              onClick: handleRun.bind(null, record),
            },
            {
              icon: IconEnum.LOG,
              tooltip: '调度日志',
              auth: JobAuth.EDIT,
              onClick: handleLog.bind(null, record),
            },
            {
              icon: IconEnum.DELETE,
              tooltip: '删除',
              auth: JobAuth.DELETE,
              color: 'error',
              onClick: handleDelete.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>
    <JobModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>

<script setup lang="ts">
  import { reactive } from 'vue';
  import { delJobApi, listJobApi, runJobApi } from '@/api/system/system/job.api';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { IconEnum } from '@/enums/basic';
  import { BasicTable, TableAction, useTable } from '/@/components/Table';
  import { JobAuth } from '/@/auth/system/system';
  import { columns, searchFormSchema } from './job.data';
  import { JobDetailGo, JobLogIndexGo } from '@/enums/system/system';
  import JobModal from './JobModal.vue';
  import { useUserStore } from '/@/store/modules/user';

  const { createMessage, createConfirm } = useMessage();
  const [registerModal, { openModal }] = useModal();
  const state = reactive<{
    ids: string[];
    idNames: string;
  }>({
    ids: [],
    idNames: '',
  });
  const [registerTable, { reload }] = useTable({
    title: '调度任务列表',
    api: listJobApi,
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

  /** 查看按钮 */
  function handleView(record: Recordable) {
    useUserStore().getRoutePath(JobDetailGo, record.id);
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

  /** 执行一次按钮 */
  function handleRun(record: Recordable) {
    runJobApi(record.id).then(() => {
      createMessage.success('执行' + record.name + '成功！');
    });
  }

  /** 调度日志查看按钮 */
  function handleLog(record: Recordable) {
    useUserStore().getRoutePath(JobLogIndexGo, record.id);
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
          delJobApi(delIds).then(() => {
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
