<template>
  <div class="m-4 mr-0 overflow-hidden bg-white scroll-wrap">
    <ScrollContainer>
      <BasicTree
        title="部门列表"
        toolbar
        search
        :clickRowToExpand="false"
        :treeData="treeData"
        :fieldNames="{ key: 'id', title: 'name' }"
        @select="handleSelect"
      />
    </ScrollContainer>
  </div>
</template>

<script lang="ts">
  import { defineComponent, onMounted, ref } from 'vue';
  import { BasicTree, TreeItem } from '/@/components/Tree';
  import { optionDeptApi } from '/@/api/system/organize/dept';
  import { ScrollContainer } from '/@/components/Container';

  export default defineComponent({
    name: 'DeptTree',
    components: { ScrollContainer, BasicTree },
    emits: ['select'],
    setup(_, { emit }) {
      const treeData = ref<TreeItem[]>([]);

      async function fetch() {
        treeData.value = (await optionDeptApi()) as unknown as TreeItem[];
      }

      function handleSelect(keys) {
        emit('select', keys[0]);
      }

      onMounted(() => {
        fetch();
      });

      return { treeData, handleSelect };
    },
  });
</script>
