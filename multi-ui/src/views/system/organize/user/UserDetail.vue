<template>
  <PageWrapper :title="getTitle" @back="goBack">
    <Description @register="register" class="mt-4" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { onMounted, ref } from 'vue';
  import { getUserApi } from '@/api/system/organize/user.api';
  import { Description, useDescription } from '/@/components/Description';
  import { detailSchema } from './user.data';
  import { useRoute } from 'vue-router';
  import { UserIndexGo } from '@/enums/system/organize';
  import { useTabs } from '/@/hooks/web/useTabs';
  import { PageWrapper } from '/@/components/Page';
  import { useUserStore } from '/@/store/modules/user';
  import { DescItemSizeEnum } from '@/enums/basic';

  const route = useRoute();
  const { setTitle } = useTabs();
  const getTitle = ref('用户详情');

  const [register, { setDescProps }] = useDescription({
    title: '用户详情',
    schema: detailSchema,
    column: DescItemSizeEnum.DEFAULT,
  });

  onMounted(async () => {
    const id = route.params.id as string;
    const data = await getUserApi(id);
    setDescProps({ data: data });
    getTitle.value = '用户详情:' + data?.nickName;
    setTitle(getTitle.value);
  });

  /** 返回用户主页 */
  function goBack() {
    useUserStore().getRoutePath(UserIndexGo);
  }
</script>
