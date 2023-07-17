<template>
  <PageWrapper :title="getTitle" @back="goBack">
    <Description @register="register" class="mt-4" />
  </PageWrapper>
</template>

<script lang="ts">
  import { defineComponent, onMounted, ref } from 'vue';
  import { getDeptApi } from '/@/api/system/organize/dept';
  import { Description, useDescription } from '/@/components/Description';
  import { detailSchema } from './dept.data';
  import { useRoute } from 'vue-router';
  import { DeptIndexGo } from '/@/enums/system';
  import { useTabs } from '/@/hooks/web/useTabs';
  import { PageWrapper } from '/@/components/Page';
  import { useUserStore } from '/@/store/modules/user';
  import { DescItemSizeEnum } from '/@/enums/appEnum';

  export default defineComponent({
    components: { Description, PageWrapper },
    setup() {
      const route = useRoute();
      const { setTitle } = useTabs();
      const getTitle = ref('部门详情');

      const [register, { setDescProps }] = useDescription({
        title: '部门详情',
        schema: detailSchema,
        column: DescItemSizeEnum.DEFAULT,
      });

      onMounted(async () => {
        const id = route.params.id as string;
        const data = await getDeptApi(id);
        setDescProps({ data: data });
        getTitle.value = '部门详情:' + data?.name;
        setTitle(getTitle.value);
      });

      /** 返回部门主页 */
      function goBack() {
        useUserStore().getRoutePath(DeptIndexGo);
      }

      return { register, getTitle, goBack };
    },
  });
</script>
