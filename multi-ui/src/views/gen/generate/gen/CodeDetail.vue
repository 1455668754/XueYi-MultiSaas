<template>
  <PageWrapper :title="getTitle" @back="goBack" contentFullHeight fixedHeight contentBackground>
    <template #extra>
      <a-space size="middle">
        <a-button @click="showData" type="primary">获取数据</a-button>
      </a-space>
    </template>
    <template #footer>
      <a-tabs v-model:activeKey="currentKey" @change="handleModeChange">
        <a-tab-pane v-for="(item, index) in codeList" :key="index" :tab="item.name" />
      </a-tabs>
    </template>
    <CodeEditor v-model:value="codeValue" :mode="codeMode" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { h, ref, unref } from 'vue';
  import { Modal } from 'ant-design-vue';
  import { GenCodeLM } from '@/model/gen/generate';
  import { useRoute } from 'vue-router';
  import { CodeEditor, JsonPreview, MODE } from '/@/components/CodeEditor';
  import { useTabs } from '/@/hooks/web/useTabs';
  import { getGenApi, previewGenApi } from '@/api/gen/generate/gen.api';
  import { PageWrapper } from '/@/components/Page';
  import { GenIndexGo } from '@/enums/gen/generate';
  import { useUserStore } from '/@/store/modules/user';

  const route = useRoute();
  const { setTitle } = useTabs();
  const getTitle = ref('');
  const codeList = ref<GenCodeLM>([]);
  const currentKey = ref(0);

  const codeMode = ref<MODE>(MODE.JSON);
  const codeValue = ref('');

  function handleModeChange() {
    codeMode.value = codeList.value[currentKey.value].language as MODE;
    codeValue.value = codeList.value[currentKey.value]?.template;
  }

  async function create() {
    const id = route.params.id as string;
    const data = await getGenApi(id);
    codeList.value = await previewGenApi(id);
    getTitle.value = data?.comment;
    handleModeChange();
    setTitle('生成预览：' + getTitle.value);
  }

  create();

  function showData() {
    if (unref(codeMode) === MODE.JSON) {
      Modal.info({
        title: '编辑器当前值',
        content: h(JsonPreview, { data: JSON.parse(codeValue.value) }),
      });
    } else {
      Modal.info({ title: '编辑器当前值', content: codeValue.value });
    }
  }

  /** 返回代码生成页 */
  function goBack() {
    useUserStore().getRoutePath(GenIndexGo);
  }
</script>
