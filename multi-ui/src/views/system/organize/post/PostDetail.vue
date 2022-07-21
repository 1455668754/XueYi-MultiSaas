<template>
  <PageWrapper :title="getTitle" @back="goBack">
    <Description @register="register" class="mt-4" />
  </PageWrapper>
</template>

<script lang="ts">
  import { defineComponent, onMounted, ref } from 'vue';
  import { getPostApi } from '/@/api/system/organize/post';
  import { Description, useDescription } from '/@/components/Description';
  import { detailSchema } from './post.data';
  import { useRoute } from 'vue-router';
  import { PostIndexGo } from '/@/enums/system';
  import { useTabs } from '/@/hooks/web/useTabs';
  import { PageWrapper } from '/@/components/Page';
  import { useUserStore } from '/@/store/modules/user';
  import { DescItemSizeEnum } from '/@/enums/appEnum';

  export default defineComponent({
    components: { Description, PageWrapper },
    setup() {
      const route = useRoute();
      const { setTitle } = useTabs();
      const getTitle = ref('岗位详情');

      const [register, { setDescProps }] = useDescription({
        title: '岗位详情',
        schema: detailSchema,
        column: DescItemSizeEnum.DEFAULT,
      });

      onMounted(async () => {
        const id = route.params.id as string | number;
        const data = await getPostApi(id);
        setDescProps({ data: data });
        getTitle.value = '岗位详情:' + data?.name;
        setTitle(getTitle.value);
      });

      /** 返回岗位主页 */
      function goBack() {
        useUserStore().getRoutePath(PostIndexGo);
      }

      return { register, getTitle, goBack };
    },
  });
</script>
