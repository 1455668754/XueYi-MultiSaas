<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="getTitle"
    :defaultFullscreen="true"
    :showCancelBtn="false"
    :showOkBtn="current === 3"
    @ok="handleSubmit"
  >
    <div class="step-form-form">
      <a-steps :current="current">
        <a-step v-for="item in tenantInitList" :title="item.title" :key="item.key" />
      </a-steps>

      <div class="mt-5">
        <BasicForm @register="strategyRegister" v-show="current === tenantInitList[0].current" />
        <BasicForm @register="tenantRegister" v-show="current === tenantInitList[1].current" />
        <BasicForm @register="organizeRegister" v-show="current === tenantInitList[2].current" />
        <BasicForm @register="authorityRegister" v-show="current === tenantInitList[3].current">
          <template #menu="{ model, field }">
            <BasicTree
              v-model:value="model[field]"
              :treeData="authTree"
              :fieldNames="{ title: 'label', key: 'id' }"
              checkable
              toolbar
              @check="authCheck"
              title="菜单分配"
            />
          </template>
        </BasicForm>
      </div>
    </div>
    <template #centerFooter>
      <a-button type="default" @click="handleStepPrev" v-show="current > 0"> 上一步</a-button>
    </template>
    <template #appendFooter>
      <a-button type="primary" @click="handleStepNext" v-show="current < 3"> 下一步</a-button>
    </template>
  </BasicModal>
</template>

<script setup lang="ts">
  import { computed, ref, unref } from 'vue';
  import {
    authorityFormSchema,
    organizeFormSchema,
    strategyFormSchema,
    tenantFormSchema,
    tenantInitList,
  } from './tenant.data';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { addTenantApi, authScopeTenantApi } from '@/api/tenant/tenant/tenant.api';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';
  import { BasicTree, TreeItem } from '/@/components/Tree';
  import { sourceAssign } from '/@/utils/xueyi';

  const emit = defineEmits(['success', 'register']);

  const { createMessage } = useMessage();
  const current = ref(0);
  const authTree = ref<TreeItem[]>([]);
  const authKeys = ref<string[]>([]);
  const authHalfKeys = ref<string[]>([]);

  const [strategyRegister, { resetFields: strategyResetFields, validate: strategyValidate }] =
    useForm({
      labelWidth: 100,
      schemas: strategyFormSchema,
      showActionButtonGroup: false,
    });

  const [tenantRegister, { resetFields: tenantResetFields, validate: tenantValidate }] = useForm({
    labelWidth: 100,
    schemas: tenantFormSchema,
    showActionButtonGroup: false,
  });

  const [organizeRegister, { resetFields: organizeResetFields, validate: organizeValidate }] =
    useForm({
      labelWidth: 100,
      schemas: organizeFormSchema,
      showActionButtonGroup: false,
    });

  const [authorityRegister, { resetFields: authorityResetFields, validate: authorityValidate }] =
    useForm({
      labelWidth: 100,
      schemas: authorityFormSchema,
      showActionButtonGroup: false,
    });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async () => {
    current.value = 0;
    Promise.all([
      strategyResetFields(),
      tenantResetFields(),
      organizeResetFields(),
      authorityResetFields(),
    ]);
    authReset();
    if (unref(authTree).length === 0) {
      authTree.value = (await authScopeTenantApi()) as any as TreeItem[];
    }
    setModalProps({ confirmLoading: false });
  });

  /** 标题初始化 */
  const getTitle = computed(() => '新增租户');

  /** 上一步按钮 */
  function handleStepPrev() {
    current.value--;
  }

  /** 下一步按钮 */
  async function handleStepNext() {
    switch (current.value) {
      case 0:
        await strategyValidate();
        break;
      case 1:
        await tenantValidate();
        break;
      case 2:
        await organizeValidate();
        break;
      case 3:
        await authorityValidate();
        break;
    }
    current.value++;
  }

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const [strategy, tenant, organize, authority] = await Promise.all([
        strategyValidate(),
        tenantValidate(),
        organizeValidate(),
        authorityValidate(),
      ]);
      setModalProps({ confirmLoading: true });
      const data = sourceAssign({}, strategy, tenant, organize, authority);
      data.authIds = authKeys.value.concat(authHalfKeys.value);
      await addTenantApi(data).then(() => {
        closeModal();
        createMessage.success('新增租户成功！');
      });
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  /** 权限Id重置 */
  function authReset() {
    authKeys.value = [];
    authHalfKeys.value = [];
  }

  /** 获取权限Id */
  function authCheck(checkedKeys: string[], e) {
    authKeys.value = checkedKeys;
    authHalfKeys.value = e.halfCheckedKeys as string[];
  }
</script>

<style lang="less" scoped>
  .step-form-content {
    padding: 24px;
    background-color: @component-background;
  }

  .step-form-form {
    width: 750px;
    margin: 0 auto;
  }

  .step {
    &-form {
      width: 450px;
      margin: 0 auto;
    }
  }
</style>
