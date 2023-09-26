<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="getTitle"
    :defaultFullscreen="true"
    :showCancelBtn="false"
    :showOkBtn="current === 2"
    @ok="handleSubmit"
  >
    <div class="step-form-form">
      <a-steps :current="current">
        <a-step v-for="item in roleInitList" :title="item.title" :key="item.key" />
      </a-steps>

      <div class="mt-5">
        <BasicForm @register="roleRegister" v-show="current === roleInitList[0].current" />
        <BasicForm @register="authRegister" v-show="current === roleInitList[1].current">
          <template #auth="{ model, field }">
            <BasicTree
              v-model:value="model[field]"
              :treeData="authTree"
              :fieldNames="{ title: 'label', key: 'id' }"
              checkable
              toolbar
              @check="authCheck"
              title="功能权限"
            />
          </template>
        </BasicForm>
        <BasicForm @register="organizeRegister" v-show="current === roleInitList[2].current">
          <template #organize="{ model, field }">
            <BasicTree
              v-model:value="model[field]"
              :treeData="organizeTree"
              :fieldNames="{ title: 'label', key: 'id' }"
              checkable
              toolbar
              @check="dataScopeCheck"
              title="数据权限"
            />
          </template>
        </BasicForm>
      </div>
    </div>
    <template #centerFooter>
      <a-button type="default" @click="handleStepPrev" v-show="current > 0"> 上一步</a-button>
    </template>
    <template #appendFooter>
      <a-button type="primary" @click="handleStepNext" v-show="current < 2"> 下一步</a-button>
    </template>
  </BasicModal>
</template>

<script setup lang="ts">
  import { computed, reactive, ref, unref } from 'vue';
  import {
    authFormSchema,
    getOrgDeptIds,
    organizeFormSchema,
    roleFormSchema,
    roleInitList,
  } from './role.data';
  import { useMessage } from '@/hooks/web/useMessage';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicForm, useForm } from '@/components/Form';
  import { BasicTree, TreeItem } from '@/components/Tree';
  import { addRoleApi } from '@/api/system/authority/role.api';
  import { authScopeEnterpriseApi } from '@/api/system/authority/auth.api';
  import { organizeScopeApi } from '@/api/system/organize/organize.api';
  import { sourceAssign } from '@/utils/xueyi';
  import { getTreeNodes } from '@/utils/core/treeUtil';
  import { concat, difference, intersection } from 'lodash-es';
  import { DataScopeEnum } from '@/enums/system/authority';

  const emit = defineEmits(['success', 'register']);

  const { createMessage } = useMessage();
  const current = ref(0);
  const authTree = ref<TreeItem[]>([]);
  const organizeTree = ref<TreeItem[]>([]);

  const auth = reactive<{
    treeLastLeaf: string[];
    moduleLeafs: string[];
    authKeys: string[];
    authHalfKeys: string[];
  }>({
    treeLastLeaf: [],
    moduleLeafs: [],
    authKeys: [],
    authHalfKeys: [],
  });

  const dataScope = reactive<{
    treeLastLeaf: string[];
    deptLeafs: string[];
    authKeys: string[];
  }>({
    treeLastLeaf: [],
    deptLeafs: [],
    authKeys: [],
  });

  const [roleRegister, { resetFields: roleResetFields, validate: roleValidate }] = useForm({
    labelWidth: 100,
    schemas: roleFormSchema,
    showActionButtonGroup: false,
  });

  const [
    authRegister,
    { setFieldsValue: authSetFieldsValue, resetFields: authResetFields, validate: authValidate },
  ] = useForm({
    labelWidth: 100,
    schemas: authFormSchema,
    showActionButtonGroup: false,
  });

  const [
    organizeRegister,
    {
      setFieldsValue: organizeSetFieldsValue,
      resetFields: organizeResetFields,
      validate: organizeValidate,
    },
  ] = useForm({
    labelWidth: 100,
    schemas: organizeFormSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async () => {
    current.value = 0;
    authReset();
    Promise.all([roleResetFields(), authResetFields(), organizeResetFields()]);
    setModalProps({ confirmLoading: false });
    if (unref(authTree).length === 0) {
      authTree.value = (await authScopeEnterpriseApi()) as unknown as TreeItem[];
      auth.treeLastLeaf = getTreeNodes(authTree.value, 'id', 'children');
      auth.moduleLeafs = authTree.value.map((item) => item.id);
    }
    if (unref(organizeTree).length === 0) {
      const orgTree = await organizeScopeApi();
      dataScope.treeLastLeaf = getOrgDeptIds(orgTree);
      organizeTree.value = orgTree as unknown as TreeItem[];
      dataScope.deptLeafs = organizeTree.value.map((item) => item.id);
    }
  });

  /** 标题初始化 */
  const getTitle = computed(() => '新增角色');

  /** 上一步按钮 */
  function handleStepPrev() {
    current.value--;
  }

  /** 下一步按钮 */
  async function handleStepNext() {
    switch (current.value) {
      case 0:
        await roleValidate();
        await setInitName();
        break;
      case 1:
        await authValidate();
        break;
      case 2:
        await organizeValidate();
        break;
    }
    current.value++;
  }

  /** 重置其他页面值 */
  async function setInitName() {
    const { code, name } = await roleValidate();
    await authSetFieldsValue({ code: code, name: name });
    await organizeSetFieldsValue({ code: code, name: name });
  }

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const [role, auth, organize] = await Promise.all([
        roleValidate(),
        authValidate(),
        organizeValidate(),
      ]);
      setModalProps({ confirmLoading: true });
      const data = sourceAssign({}, auth, organize, role);
      const { moduleIds, menuIds } = getAuthNodes();
      data.authIds = undefined;
      data.organizeIds = undefined;
      data.moduleIds = moduleIds;
      data.menuIds = menuIds;
      if (data.dataScope === DataScopeEnum.CUSTOM) {
        const { orgDeptIds, orgPostIds } = getDataScopeNodes();
        data.orgDeptIds = orgDeptIds;
        data.orgPostIds = orgPostIds;
      }
      await addRoleApi(data).then(() => {
        closeModal();
        createMessage.success('新增角色成功！');
      });
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  /** 权限Id重置 */
  function authReset() {
    auth.authKeys = [];
    auth.authHalfKeys = [];
    dataScope.authKeys = [];
  }

  /** 获取权限Id */
  function authCheck(checkedKeys: string[], e: any) {
    auth.authKeys = checkedKeys;
    auth.authHalfKeys = e.halfCheckedKeys as string[];
  }

  /** 获取组织Id */
  function dataScopeCheck(checkedKeys: string[]) {
    dataScope.authKeys = checkedKeys;
  }

  /** 拆解功能权限Id */
  function getAuthNodes() {
    const authIds = concat(auth.authKeys, auth.authHalfKeys);
    return {
      moduleIds: intersection(authIds, auth.moduleLeafs),
      menuIds: difference(authIds, auth.moduleLeafs),
    };
  }

  /** 拆解数据权限Id */
  function getDataScopeNodes() {
    const organizeIds = dataScope.authKeys;
    return {
      orgDeptIds: intersection(organizeIds, dataScope.deptLeafs),
      orgPostIds: difference(organizeIds, dataScope.deptLeafs),
    };
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
