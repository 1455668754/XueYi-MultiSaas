<template>
  <Card title="模块" v-bind="$attrs">
    <CardGrid
      v-for="(item, index) in route"
      :key="index"
      class="!md:w-1/3 !w-full"
      @click="handleReset(item)"
    >
      <span class="flex">
        <a-image :src="item.logo" :preview="false" :width="40" :height="40" />
        <span class="text-lg ml-4">{{ item.name }}</span>
      </span>
      <div class="flex mt-2 h-10 text-secondary">{{ item.remark }}</div>
      <div class="flex justify-between text-secondary">
        <Tag v-if="isEqual(item.id, moduleId)" color="green">当前使用</Tag>
        <Tag v-else>可使用</Tag>
      </div>
    </CardGrid>
  </Card>
</template>

<script setup lang="ts">
  import { onMounted, ref } from 'vue';
  import { Card, Image, Tag } from 'ant-design-vue';
  import { useMessage } from '@/hooks/web/useMessage';
  import { usePermission } from '@/hooks/web/usePermission';
  import { FrameTypeEnum, MODULE_CACHE } from '@/enums/system/authority';
  import { ModuleIM, ModuleLM } from '@/model/system/authority';
  import { getModuleList } from '@/api/sys/menu.api';
  import { isEqual } from 'lodash-es';
  import { usePermissionStore } from '@/store/modules/permission';

  const CardGrid = Card.Grid;
  const AImage = Image;

  const moduleId = ref(usePermissionStore().getModuleId);
  const route = ref<ModuleLM>([]);
  const { createMessage, createConfirm } = useMessage();
  const { refreshMenu } = usePermission();

  onMounted(async () => (route.value = await getModuleList()));

  function handleReset(data: ModuleIM) {
    if (data?.type === FrameTypeEnum.EXTERNAL_LINKS) {
      if (data?.path) {
        window.open(data?.path);
      } else {
        createMessage.success('未指定跳转地址，跳转失败！');
      }
    } else {
      if (isEqual(data.id, usePermissionStore().getModuleId)) {
        createMessage.success('当前正处于[' + data.name + ']，无需切换！');
      } else {
        createConfirm({
          iconType: 'warning',
          title: '提示',
          content: '确定要跳转到模块：【' + data.name + '】， 并重新加载其资源吗？',
          onOk: () => {
            usePermissionStore().setModuleId(data.id);
            sessionStorage.setItem(MODULE_CACHE, data.id.toString());
            moduleId.value = data.id as string;
            createMessage.success('已成功切换至' + data.name + '！');
            refreshMenu();
          },
        });
      }
    }
  }
</script>
