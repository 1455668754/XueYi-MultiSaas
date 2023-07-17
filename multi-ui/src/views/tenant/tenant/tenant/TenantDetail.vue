<template>
  <PageWrapper :title="getTitle" @back="goBack">
    <Description @register="register" class="mt-4" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { onMounted, ref } from 'vue';
  import { getTenantApi } from '@/api/tenant/tenant/tenant.api';
  import { Description, useDescription } from '/@/components/Description';
  import { detailSchema } from './tenant.data';
  import { useRoute } from 'vue-router';
  import { TenantIndexGo } from '@/enums/tenant';
  import { useTabs } from '/@/hooks/web/useTabs';
  import { PageWrapper } from '/@/components/Page';
  import { useUserStore } from '/@/store/modules/user';
  import { DescItemSizeEnum } from '@/enums/basic';

  const route = useRoute();
  const { setTitle } = useTabs();
  const getTitle = ref('租户详情');

  const [register, { setDescProps }] = useDescription({
    title: '租户详情',
    schema: detailSchema,
    column: DescItemSizeEnum.DEFAULT,
  });

  onMounted(async () => {
    const id = route.params.id as string;
    const data = await getTenantApi(id);
    setDescProps({ data: data });
    getTitle.value = '租户详情:' + data?.name;
    setTitle(getTitle.value);
  });

  /** 返回租户主页 */
  function goBack() {
    useUserStore().getRoutePath(TenantIndexGo);
  }
</script>
