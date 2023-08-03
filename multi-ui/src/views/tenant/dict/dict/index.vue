<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <TypeIndex ref="typeRef" class="w-3/5 xl:w-3/5 h-full" @dict-change="onDictChange" />
    <DataIndex ref="dataRef" class="w-2/5 xl:w-2/5 h-full" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import PageWrapper from '@/components/Page/src/PageWrapper.vue';
  import DataIndex from './DataIndex.vue';
  import TypeIndex from './TypeIndex.vue';
  import { ref } from 'vue';
  import { DictTypeIM } from '@/model/tenant';

  const typeRef = ref();
  const dataRef = ref<{
    onChangeDictInfo: Function;
  }>({
    onChangeDictInfo: () => {},
  });

  /** 字典管理 */
  function onDictChange(dict: DictTypeIM) {
    dataRef.value.onChangeDictInfo(dict);
  }
</script>
