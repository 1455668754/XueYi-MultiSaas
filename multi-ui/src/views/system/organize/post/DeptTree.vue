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

<script setup lang="ts">
  import { onMounted, ref } from 'vue';
  import { BasicTree, TreeItem } from '@/components/Tree';
  import { listDeptApi } from '@/api/system/organize/dept.api';
  import { ScrollContainer } from '@/components/Container';
  import { DicStatusEnum } from '@/enums';

  const emit = defineEmits(['select']);

  const treeData = ref<TreeItem[]>([]);

  async function fetch() {
    treeData.value = (await listDeptApi({ status: DicStatusEnum.NORMAL })) as unknown as TreeItem[];
  }

  function handleSelect(keys) {
    emit('select', keys[0]);
  }

  onMounted(() => {
    fetch();
  });
</script>
