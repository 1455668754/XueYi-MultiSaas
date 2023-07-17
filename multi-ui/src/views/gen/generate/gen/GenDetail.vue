<template>
  <PageWrapper :title="getTitle" @back="goBack">
    <template #extra>
      <a-button type="primary" @click="submit" v-auth="GenAuth.EDIT" danger> 保存</a-button>
    </template>

    <template #footer>
      <Tabs v-model:activeKey="currentKey">
        <TabPane v-for="item in genList" :key="item.key" :tab="item.name" forceRender="true" />
      </Tabs>
    </template>

    <card v-show="genList[0].key === currentKey">
      <BasicForm @submit="handleCheck" ref="basicRef" />
    </card>

    <card v-show="genList[1].key === currentKey">
      <GenerateForm @submit="handleCheck" ref="generateRef" />
    </card>

    <card v-show="genList[2].key === currentKey">
      <FieldForm @submit="handleCheck" ref="fieldRef" />
    </card>
  </PageWrapper>
</template>

<script lang="ts">
  import { defineComponent, reactive, ref, toRefs } from 'vue';
  import { useRoute } from 'vue-router';
  import { Card, Tabs } from 'ant-design-vue';
  import BasicForm from './BasicForm.vue';
  import FieldForm from './FieldForm.vue';
  import GenerateForm from './GenerateForm.vue';
  import { genList } from './gen.detail.data';
  import { editGenApi, getSubGenApi } from '/@/api/gen/generate/gen';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useTabs } from '/@/hooks/web/useTabs';
  import { PageWrapper } from '/@/components/Page';
  import { GenTableIM } from '/@/model/gen';
  import { GenIndexGo } from '/@/enums/gen';
  import { GenAuth } from '/@/auth/gen';
  import { useUserStore } from '/@/store/modules/user';

  export default defineComponent({
    name: 'GenDetail',
    components: {
      Card,
      PageWrapper,
      BasicForm,
      FieldForm,
      GenerateForm,
      Tabs,
      TabPane: Tabs.TabPane,
    },
    setup() {
      const route = useRoute();
      const { createMessage } = useMessage();

      const state = reactive<{
        basicRef: { initialize: Function; submit: Function };
        generateRef: { initialize: Function; submit: Function };
        fieldRef: { initialize: Function; submit: Function };
      }>({
        basicRef: {
          initialize: () => {},
          submit: () => {},
        },
        generateRef: {
          initialize: () => {},
          submit: () => {},
        },
        fieldRef: {
          initialize: () => {},
          submit: () => {},
        },
      });

      const genState = reactive<{
        info: Nullable<GenTableIM>;
      }>({
        info: null,
      });
      const currentKey = ref(genList[0].key);
      const submitCheck = ref(true);
      const getTitle = ref('生成配置');

      const { setTitle, close } = useTabs();

      /** 初始加载 */
      async function create() {
        const id = route.params.id as string;
        genState.info = await getSubGenApi(id);
        getTitle.value = genState.info?.comment;
        setTitle('生成配置：' + getTitle.value);
        state.basicRef.initialize(genState.info);
        state.generateRef.initialize(genState.info);
        state.fieldRef.initialize(genState.info);
      }

      create();

      /** 返回代码生成页 */
      function goBack() {
        close();
        useUserStore().getRoutePath(GenIndexGo);
      }

      /** 保存按钮 */
      async function submit() {
        try {
          const values1 = await state.basicRef.submit();
          const values2 = await state.generateRef.submit(values1);
          const values3 = await state.fieldRef.submit(values2);
          if (submitCheck.value) {
            if (values3 !== null) {
              await editGenApi(values3).then(() => {
                createMessage.success('修改成功！');
                goBack();
              });
            }
          }
        } catch {
          createMessage.success('修改失败，请检查！');
        } finally {
          submitCheck.value = true;
        }
      }

      /** 校验错误页跳转 */
      function handleCheck(current) {
        submitCheck.value = false;
        currentKey.value = current;
      }

      return {
        GenAuth,
        goBack,
        getTitle,
        genList,
        genState,
        submitCheck,
        currentKey,
        submit,
        handleCheck,
        ...toRefs(state),
      };
    },
  });
</script>
