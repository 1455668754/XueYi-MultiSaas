<template>
  <PageWrapper :title="getTitle" @back="goBack">
    <Description @register="register" class="mt-4" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { onMounted, ref } from 'vue';
  import { getNoticeApi } from '@/api/system/notice/notice.api';
  import { Description, useDescription } from '/@/components/Description';
  import { detailSchema } from './notice.data';
  import { useRoute } from 'vue-router';
  import { NoticeIndexGo } from '@/enums/system/notice';
  import { useTabs } from '/@/hooks/web/useTabs';
  import { PageWrapper } from '/@/components/Page';
  import { useUserStore } from '/@/store/modules/user';
  import { DescItemSizeEnum } from '@/enums/basic';

  const route = useRoute();
  const { setTitle } = useTabs();
  const getTitle = ref('通知公告详情');

  const [register, { setDescProps }] = useDescription({
    title: '通知公告详情',
    schema: detailSchema,
    column: DescItemSizeEnum.DEFAULT,
  });

  onMounted(async () => {
    const id = route.params.id as string;
    const data = await getNoticeApi(id);
    setDescProps({ data: data });
    getTitle.value = '通知公告详情:' + data?.name;
    setTitle(getTitle.value);
  });

  /** 返回通知公告主页 */
  function goBack() {
    useUserStore().getRoutePath(NoticeIndexGo);
  }
</script>
