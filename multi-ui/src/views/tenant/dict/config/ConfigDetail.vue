<template>
  <PageWrapper :title="getTitle" @back="goBack">
    <Description @register="register" class="mt-4" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { onMounted, ref } from 'vue';
  import { getConfigApi } from '@/api/tenant/dict/config.api';
  import { Description, useDescription } from '/@/components/Description';
  import { detailSchema } from './config.data';
  import { useRoute } from 'vue-router';
  import { ConfigIndexGo } from '@/enums/tenant';
  import { useTabs } from '/@/hooks/web/useTabs';
  import { PageWrapper } from '/@/components/Page';
  import { useUserStore } from '/@/store/modules/user';
  import { DescItemSizeEnum } from '@/enums/basic';

  const route = useRoute();
  const { setTitle } = useTabs();
  const getTitle = ref('参数详情');

  const [register, { setDescProps }] = useDescription({
    title: '参数详情',
    schema: detailSchema,
    column: DescItemSizeEnum.DEFAULT,
  });

  onMounted(async () => {
    const id = route.params.id as string;
    const data = await getConfigApi(id);
    setDescProps({ data: data });
    getTitle.value = '参数详情:' + data?.name;
    setTitle(getTitle.value);
  });

  /** 返回参数主页 */
  function goBack() {
    useUserStore().getRoutePath(ConfigIndexGo);
  }
</script>
