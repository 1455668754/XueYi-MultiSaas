<template>
  <PageWrapper :title="getTitle" @back="goBack">
    <Description @register="register" class="mt-4" />
  </PageWrapper>
</template>

<script lang="ts">
  import { defineComponent, onMounted, ref } from 'vue';
  import { getJobApi } from '@/api/system/system/job.api';
  import { Description, useDescription } from '/@/components/Description';
  import { detailSchema } from './job.data';
  import { useRoute } from 'vue-router';
  import { JobIndexGo } from '@/enums/system';
  import { useTabs } from '/@/hooks/web/useTabs';
  import { PageWrapper } from '/@/components/Page';
  import { useUserStore } from '/@/store/modules/user';
  import { DescItemSizeEnum } from '@/enums';

  export default defineComponent({
    components: { Description, PageWrapper },
    setup() {
      const route = useRoute();
      const { setTitle } = useTabs();
      const getTitle = ref('调度任务详情');

      const [register, { setDescProps }] = useDescription({
        title: '调度任务详情',
        schema: detailSchema,
        column: DescItemSizeEnum.DEFAULT,
      });

      onMounted(async () => {
        const id = route.params.id as string;
        const data = await getJobApi(id);
        setDescProps({ data: data });
        getTitle.value = '调度任务详情:' + data?.name;
        setTitle(getTitle.value);
      });

      /** 返回调度任务主页 */
      function goBack() {
        useUserStore().getRoutePath(JobIndexGo);
      }

      return { register, getTitle, goBack };
    },
  });
</script>
