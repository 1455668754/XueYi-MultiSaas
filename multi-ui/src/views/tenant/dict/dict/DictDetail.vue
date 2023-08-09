<template>
  <PageWrapper :title="getTitle" @back="goBack">
    <Description @register="register" class="mt-4" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { onMounted, ref } from 'vue';
  import { getDictTypeApi } from '@/api/tenant/dict/dictType.api';
  import { Description, useDescription } from '/@/components/Description';
  import { useRoute } from 'vue-router';
  import { DictTypeIndexGo } from '@/enums/tenant/dict';
  import { useTabs } from '/@/hooks/web/useTabs';
  import { PageWrapper } from '/@/components/Page';
  import { useUserStore } from '/@/store/modules/user';
  import { DescItemSizeEnum } from '@/enums/basic';
  import { typeDetailSchema } from '@/views/tenant/dict/dict/dict.data';

  const route = useRoute();
  const { setTitle } = useTabs();
  const getTitle = ref('字典类型详情');

  const [register, { setDescProps }] = useDescription({
    title: '字典类型详情',
    schema: typeDetailSchema,
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
</script>
