<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          :preIcon="IconEnum.ADD"
          v-auth="ModuleAuth.ADD"
          @click="handleCreate"
          type="primary"
        >
          新增
        </a-button>
        <a-button
          :preIcon="IconEnum.DELETE"
          v-auth="ModuleAuth.DELETE"
          @click="handleDelete"
          type="primary"
          color="error"
        >
          删除
        </a-button>
      </template>
      <template #logo="{ text }">
        <Image :height="30" :width="30" :src="text" />
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: IconEnum.VIEW,
              tooltip: '查看',
              auth: ModuleAuth.SINGLE,
              onClick: handleView.bind(null, record),
            },
            {
              icon: IconEnum.EDIT,
              tooltip: '编辑',
              auth: ModuleAuth.EDIT,
              onClick: handleEdit.bind(null, record),
            },
            {
              icon: IconEnum.DELETE,
              tooltip: '删除',
              auth: ModuleAuth.DELETE,
              color: 'error',
              onClick: handleDelete.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>
    <ModuleModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>

<script setup lang="ts">
  import { reactive } from 'vue';
  import { delModuleApi, listModuleApi } from '@/api/system/authority/module.api';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useUserStore } from '/@/store/modules/user';
  import { IconEnum } from '@/enums/basic';
  import { BasicTable, TableAction, useTable } from '/@/components/Table';
  import { Image } from 'ant-design-vue';
  import { ModuleAuth } from '/@/auth/system/authority';
  import { columns, searchFormSchema } from './module.data';
  import { ModuleDetailGo } from '@/enums/system/authority';
  import ModuleModal from './ModuleModal.vue';

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
    title: '模块列表',
    api: listModuleApi,
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
    useUserStore().getRoutePath(ModuleDetailGo, record.id);
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
          delModuleApi(delIds).then(() => {
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
