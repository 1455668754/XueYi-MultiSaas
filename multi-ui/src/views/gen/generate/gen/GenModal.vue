<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    destroyOnClose
    :width="700"
    :title="getTitle"
    @ok="handleSubmit"
  >
    <BasicTable @register="registerTable">
      <template #toolbar></template>
    </BasicTable>
  </BasicModal>
</template>

<script lang="ts">
  import { defineComponent, reactive } from 'vue';
  import { modelColumns, searchModelFormSchema } from './gen.data';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { importDBGenApi, listDBGenApi } from '/@/api/gen/generate/gen';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { BasicTable, useTable } from '/@/components/Table';

  export default defineComponent({
    name: 'GenModal',
    components: { BasicTable, BasicModal },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const { createMessage } = useMessage();

      const state = reactive<{
        dbNames: (string | number)[];
      }>({
        dbNames: [],
      });

      const [registerModal, { setModalProps, closeModal }] = useModalInner(() => {
        setModalProps({ confirmLoading: false });
      });

      const [registerTable, { reload }] = useTable({
        title: '数据表列表',
        api: listDBGenApi,
        columns: modelColumns,
        formConfig: {
          labelWidth: 70,
          schemas: searchModelFormSchema,
        },
        rowKey: 'name',
        striped: true,
        useSearchForm: true,
        showTableSetting: true,
        bordered: true,
        showIndexColumn: true,
        rowSelection: {
          onChange: (selectedRowKeys) => {
            state.dbNames = selectedRowKeys;
          },
        },
      });

      const getTitle = '导入数据表';

      async function handleSubmit() {
        try {
          setModalProps({ confirmLoading: true, destroyOnClose: true });
          await importDBGenApi(state.dbNames).then(() => {
            closeModal();
            createMessage.success('导入成功！');
          });
          emit('success');
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      return { registerModal, registerTable, reload, getTitle, handleSubmit };
    },
  });
</script>
