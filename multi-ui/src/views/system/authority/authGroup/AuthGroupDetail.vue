<template>
  <PageWrapper :title="getTitle" @back="goBack">
    <Description @register="register" class="mt-4" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { onMounted, ref } from 'vue';
  import { getAuthGroupApi } from '@/api/system/authority/authGroup.api';
  import { Description, useDescription } from '@/components/Description';
  import { detailSchema } from './authGroup.data';
  import { useRoute } from 'vue-router';
  import { AuthGroupIndexGo } from '@/enums/system/authority';
  import { useTabs } from '@/hooks/web/useTabs';
  import { PageWrapper } from '@/components/Page';
  import { useUserStore } from '@/store/modules/user';
  import { DescItemSizeEnum } from '@/enums/basic';

  const route = useRoute();
  const { setTitle } = useTabs();
  const getTitle = ref('权限组详情');

  const [register, { setDescProps }] = useDescription({
    title: '权限组详情',
    schema: detailSchema,
    column: DescItemSizeEnum.DEFAULT,
  });

  onMounted(async () => {
    const id = route.params.id as string;
    const data = await getAuthGroupApi(id);
    setDescProps({ data: data });
    getTitle.value = '权限组详情:' + data?.name;
    setTitle(getTitle.value);
  });

  /** 返回系统服务 | 权限模块 | 租户权限组主页 */
  function goBack() {
    useUserStore().getRoutePath(AuthGroupIndexGo);
  }
</script>
