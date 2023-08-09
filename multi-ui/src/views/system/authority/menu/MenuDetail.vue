<template>
  <PageWrapper :title="getTitle" @back="goBack">
    <Description @register="register" class="mt-4" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { onMounted, ref } from 'vue';
  import { getMenuApi } from '@/api/system/authority/menu.api';
  import { Description, useDescription } from '/@/components/Description';
  import { detailSchema } from './menu.data';
  import { useRoute } from 'vue-router';
  import { MenuIndexGo } from '@/enums/system/authority';
  import { useTabs } from '/@/hooks/web/useTabs';
  import { PageWrapper } from '/@/components/Page';
  import { useUserStore } from '/@/store/modules/user';
  import { DescItemSizeEnum } from '@/enums/basic';

  const route = useRoute();
  const { setTitle } = useTabs();
  const getTitle = ref('菜单详情');

  const [register, { setDescProps }] = useDescription({
    title: '菜单详情',
    schema: detailSchema,
    column: DescItemSizeEnum.DEFAULT,
  });

  onMounted(async () => {
    const id = route.params.id as string;
    const data = await getMenuApi(id);
    setDescProps({ data: data });
    getTitle.value = '菜单详情:' + data?.title;
    setTitle(getTitle.value);
  });

  /** 返回菜单主页 */
  function goBack() {
    useUserStore().getRoutePath(MenuIndexGo);
  }
</script>
