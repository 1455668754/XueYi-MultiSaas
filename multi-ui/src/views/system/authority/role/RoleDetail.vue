<template>
  <PageWrapper :title="getTitle" @back="goBack">
    <Description @register="register" class="mt-4" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { onMounted, ref } from 'vue';
  import { getRoleApi } from '@/api/system/authority/role.api';
  import { Description, useDescription } from '@/components/Description';
  import { detailSchema } from './role.data';
  import { useRoute } from 'vue-router';
  import { RoleIndexGo } from '@/enums/system/authority';
  import { useTabs } from '@/hooks/web/useTabs';
  import { PageWrapper } from '@/components/Page';
  import { useUserStore } from '@/store/modules/user';
  import { DescItemSizeEnum } from '@/enums';

  const route = useRoute();
  const { setTitle } = useTabs();
  const getTitle = ref('角色详情');

  const [register, { setDescProps }] = useDescription({
    title: '角色详情',
    schema: detailSchema,
    column: DescItemSizeEnum.DEFAULT,
  });

  onMounted(async () => {
    const id = route.params.id as string;
    const data = await getRoleApi(id);
    setDescProps({ data: data });
    getTitle.value = '角色详情:' + data?.name;
    setTitle(getTitle.value);
  });

  /** 返回角色主页 */
  function goBack() {
    useUserStore().getRoutePath(RoleIndexGo);
  }
</script>
