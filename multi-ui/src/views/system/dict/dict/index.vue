<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <TypeIndex
      ref="typeRef"
      class="w-3/5 xl:w-3/5 h-full"
      @dict-change="onDictChange"
      @dict-del="onDictDel"
    />
    <DataIndex v-show="hasDict" ref="dataRef" class="w-2/5 xl:w-2/5 h-full" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import PageWrapper from '@/components/Page/src/PageWrapper.vue';
  import DataIndex from './DataIndex.vue';
  import TypeIndex from './TypeIndex.vue';
  import { computed, ref } from 'vue';
  import { DictTypeIM } from '@/model/system/dict';
  import { isArray, isNotEmpty } from '@/utils/core/ObjectUtil';
  import { indexOf } from 'lodash-es';

  const typeRef = ref();
  const dataRef = ref<{
    onChangeDictInfo: Function;
  }>({
    onChangeDictInfo: () => {},
  });
  const dictTypeId = ref();
  const hasDict = computed(() => isNotEmpty(dictTypeId.value));

  /** 字典管理 */
  function onDictChange(dict: DictTypeIM) {
    dictTypeId.value = dict?.id;
    dataRef.value.onChangeDictInfo(dict);
  }

  function onDictDel(dictIds: string | string[]) {
    if (hasDict.value) {
      if (isArray(dictIds)) {
        if (indexOf(dictIds, dictTypeId.value) !== -1) {
          dictTypeId.value = undefined;
        }
      } else if (dictTypeId.value === dictIds) {
        dictTypeId.value = undefined;
      }
    }
  }
</script>
