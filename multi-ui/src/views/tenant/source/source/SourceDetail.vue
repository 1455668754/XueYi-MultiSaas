<template>
  <PageWrapper :title="getTitle" @back="goBack">
    <Description @register="register" class="mt-4" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { onMounted, ref } from 'vue';
  import { getSourceApi } from '@/api/tenant/source/source.api';
  import { Description, useDescription } from '@/components/Description';
  import { detailSchema } from './source.data';
  import { useRoute } from 'vue-router';
  import { SourceIndexGo } from '@/enums/tenant/source';
  import { useTabs } from '@/hooks/web/useTabs';
  import { PageWrapper } from '@/components/Page';
  import { DescItemSizeEnum } from '@/enums';
  import { useUserStore } from '@/store/modules/user';

  const route = useRoute();
  const { setTitle } = useTabs();
  const getTitle = ref('数据源详情');

  const [register, { setDescProps }] = useDescription({
    title: '数据源详情',
    schema: detailSchema,
    column: DescItemSizeEnum.DEFAULT,
  });

  onMounted(async () => {
    const id = route.params.id as string;
    const data = await getSourceApi(id);
    setDescProps({ data: data });
    getTitle.value = '数据源详情:' + data?.name;
    setTitle(getTitle.value);
  });

  /** 返回数据源主页 */
  function goBack() {
    useUserStore().getRoutePath(SourceIndexGo);
  }
</script>
