<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <TypeIndex class="w-2/5 xl:w-2/5 h-full" />
    <DataIndex class="w-3/5 xl:w-3/5 h-full" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import PageWrapper from '@/components/Page/src/PageWrapper.vue';
  import DataIndex from './DataIndex.vue';
  import TypeIndex from './TypeIndex.vue';
</script>
