<template>
  <div class="m-4 mr-0 overflow-hidden bg-white">
    <BasicTree
      title="组织列表"
      toolbar
      search
      :clickRowToExpand="false"
      :treeData="treeData"
      :fieldNames="{ key: 'id', title: 'label' }"
      @select="handleSelect"
    />
  </div>
</template>

<script lang="ts">
  import { defineComponent, onMounted, ref } from 'vue';
  import { BasicTree, TreeItem } from '/@/components/Tree';
  import { organizeOptionApi } from '/@/api/system/organize/organize';
  import { OrganizeTypeEnum } from '/@/enums/system';

  export default defineComponent({
    name: 'OrganizeTree',
    components: { BasicTree },

    emits: ['select'],
    setup(_, { emit }) {
      const treeData = ref<TreeItem[]>([]);

      async function fetch() {
        treeData.value = (await organizeOptionApi()) as unknown as TreeItem[];
      }

      function handleSelect(keys, e) {
        if (e.node.type === OrganizeTypeEnum.DEPT) {
          emit('select', keys[0], '');
        } else if (e.node.type === OrganizeTypeEnum.POST) {
          emit('select', '', keys[0]);
        }
      }

      onMounted(() => {
        fetch();
      });

      return { treeData, handleSelect };
    },
  });
</script>
