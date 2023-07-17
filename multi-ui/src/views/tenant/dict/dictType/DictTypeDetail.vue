<template>
  <PageWrapper :title="getTitle" @back="goBack">
    <Description @register="register" class="mt-4" />
  </PageWrapper>
</template>

<script lang="ts">
  import { defineComponent, onMounted, ref } from 'vue';
  import { getDictTypeApi } from '/@/api/tenant/dict/dictType';
  import { Description, useDescription } from '/@/components/Description';
  import { detailSchema } from './dictType.data';
  import { useRoute } from 'vue-router';
  import { DictTypeIndexGo } from '/@/enums/tenant';
  import { useTabs } from '/@/hooks/web/useTabs';
  import { PageWrapper } from '/@/components/Page';
  import { useUserStore } from '/@/store/modules/user';
  import { DescItemSizeEnum } from '/@/enums/appEnum';

  export default defineComponent({
    components: { Description, PageWrapper },
    setup() {
      const route = useRoute();
      const { setTitle } = useTabs();
      const getTitle = ref('字典类型详情');

      const [register, { setDescProps }] = useDescription({
        title: '字典类型详情',
        schema: detailSchema,
        column: DescItemSizeEnum.DEFAULT,
      });

      onMounted(async () => {
        const id = route.params.id as string;
        const data = await getDictTypeApi(id);
        setDescProps({ data: data });
        getTitle.value = '字典类型详情:' + data?.name;
        setTitle(getTitle.value);
      });

      /** 返回字典类型主页 */
      function goBack() {
        useUserStore().getRoutePath(DictTypeIndexGo);
      }

      return { register, getTitle, goBack };
    },
  });
</script>
