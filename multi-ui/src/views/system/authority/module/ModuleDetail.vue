<template>
  <PageWrapper :title="getTitle" @back="goBack">
    <Description @register="register" class="mt-4" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { onMounted, ref } from 'vue';
  import { getModuleApi } from '@/api/system/authority/module.api';
  import { Description, useDescription } from '/@/components/Description';
  import { detailSchema } from './module.data';
  import { useRoute } from 'vue-router';
  import { ModuleIndexGo } from '@/enums/system';
  import { useTabs } from '/@/hooks/web/useTabs';
  import { PageWrapper } from '/@/components/Page';
  import { useUserStore } from '/@/store/modules/user';
  import { DescItemSizeEnum } from '@/enums/basic';

  const route = useRoute();
  const { setTitle } = useTabs();
  const getTitle = ref('模块详情');

  const [register, { setDescProps }] = useDescription({
    title: '模块详情',
    schema: detailSchema,
    column: DescItemSizeEnum.DEFAULT,
  });

  onMounted(async () => {
    const id = route.params.id as string;
    const data = await getModuleApi(id);
    setDescProps({ data: data });
    getTitle.value = '模块详情:' + data?.name;
    setTitle(getTitle.value);
  });

  /** 返回模块主页 */
  function goBack() {
    useUserStore().getRoutePath(ModuleIndexGo);
  }
</script>
