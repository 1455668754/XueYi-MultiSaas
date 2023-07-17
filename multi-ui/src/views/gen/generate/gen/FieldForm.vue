<template>
  <BasicTable @register="registerTable" />
</template>

<script lang="ts">
  import { fieldColumns, genList } from './gen.detail.data';
  import { defineComponent } from 'vue';
  import { BasicTable, useTable } from '/@/components/Table';
  import { GenTableIM } from '@/model/gen';

  export default defineComponent({
    name: 'FieldFrom',
    components: { BasicTable },
    emits: ['submit'],
    setup(_, { emit }) {
      const [registerTable, { setTableData, getDataSource }] = useTable({
        title: '字段配置',
        columns: fieldColumns,
        bordered: true,
        pagination: false,
        showIndexColumn: false,
      });

      /** 数据初始化 */
      function initialize(info: GenTableIM) {
        setTableData(info.subList as Recordable[]);
      }

      /** 保存校验 */
      async function submit(info: GenTableIM) {
        try {
          info.subList = getDataSource();
          return info;
        } catch {
          emit('submit', genList[2].key);
        }
      }

      return { registerTable, initialize, submit };
    },
  });
</script>
