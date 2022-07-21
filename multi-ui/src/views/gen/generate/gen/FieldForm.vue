<template>
  <BasicTable @register="registerTable" />
</template>

<script lang="ts">
  import { fieldColumns, genList } from './gen.detail.data';
  import { defineComponent, reactive } from 'vue';
  import { BasicTable, useTable } from '/@/components/Table';
  import { GenTableIM } from '/@/model/gen';

  export default defineComponent({
    name: 'FieldFrom',
    components: { BasicTable },
    emits: ['submit'],
    setup(_, { emit }) {
      const state = reactive<{
        info: Nullable<GenTableIM>;
      }>({
        info: null,
      });

      const [registerTable, { setTableData, getDataSource }] = useTable({
        title: '字段配置',
        columns: fieldColumns,
        bordered: true,
        pagination: false,
        showIndexColumn: false,
      });

      /** 数据初始化 */
      function initialize(info: GenTableIM) {
        state.info = info;
        setTableData(state.info.subList as Recordable[]);
      }

      /** 保存校验 */
      async function submit() {
        try {
          if (state.info !== null) {
            state.info.subList = getDataSource();
          }
        } catch {
          emit('submit', genList[2].key);
        }
      }

      return { registerTable, initialize, submit };
    },
  });
</script>
