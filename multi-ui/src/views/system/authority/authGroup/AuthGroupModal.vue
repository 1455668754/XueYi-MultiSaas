<template>
  <BasicModal
    v-bind="$attrs"
    :width="1000"
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmit"
  >
    <PageWrapper dense fixedHeight contentClass="flex">
      <BasicForm @register="registerForm" class="w-2/4 xl:w-2/4" />
      <BasicForm @register="authRegisterForm" class="w-2/4 xl:w-2/4">
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
    </PageWrapper>
  </BasicModal>
</template>

<script setup lang="ts">
  import { computed, reactive, ref, unref } from 'vue';
  import { authFormSchema, formSchema } from './authGroup.data';
  import { useMessage } from '@/hooks/web/useMessage';
  import {
    addAuthGroupApi,
    editAuthGroupApi,
    getAuthGroupApi,
  } from '@/api/system/authority/authGroup.api';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicForm, useForm } from '@/components/Form';
  import { BasicTree, TreeItem } from '@/components/Tree';
  import PageWrapper from '@/components/Page/src/PageWrapper.vue';
  import { getTreeNodes } from '@/utils/core/treeUtil';
  import { concat, difference, intersection } from 'lodash-es';
  import { authScopeCommonApi } from '@/api/system/authority/auth.api';
  import { AuthGroupIM } from '@/model/system/authority';

  const emit = defineEmits(['success', 'register']);
  const { createMessage } = useMessage();
  const isUpdate = ref(true);
  const authTree = ref<TreeItem[]>([]);
  const state = reactive<{
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

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 100,
    schemas: formSchema,
    showActionButtonGroup: false,
  });

  const [authRegisterForm, { resetFields: authResetFields, setFieldsValue: authSetFieldsValue }] =
    useForm({
      labelWidth: 100,
      schemas: authFormSchema,
      showActionButtonGroup: false,
    });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    authReset();
    resetFields();
    authResetFields();
    setModalProps({ confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;
    authReset();

    if (unref(authTree).length === 0) {
      authTree.value = (await authScopeCommonApi()) as any as TreeItem[];
      state.treeLastLeaf = getTreeNodes(authTree.value, 'id', 'children');
      state.moduleLeafs = authTree.value.map((item) => item.id);
    }

    if (unref(isUpdate)) {
      const authGroup = await getAuthGroupApi(data.record.id);
      setFieldsValue({ ...authGroup });
      // 初始化权限树回显
      authGroup.authIds = concat(
        intersection(authGroup.moduleIds, state.treeLastLeaf),
        intersection(authGroup.menuIds, state.treeLastLeaf),
      );
      if (authGroup?.authIds) {
        state.authKeys = concat(authGroup.moduleIds, authGroup.menuIds);
        authSetFieldsValue({ authIds: authGroup?.authIds });
      }
    }
  });

  /** 标题初始化 */
  const getTitle = computed(() => (!unref(isUpdate) ? '新增权限组' : '编辑权限组'));

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const values: AuthGroupIM = await validate();
      setModalProps({ confirmLoading: true });
      const { moduleIds, menuIds } = getAuthNodes();
      values.moduleIds = moduleIds;
      values.menuIds = menuIds;
      unref(isUpdate)
        ? await editAuthGroupApi(values).then(() => {
            closeModal();
            createMessage.success('编辑权限组成功！');
          })
        : await addAuthGroupApi(values).then(() => {
            closeModal();
            createMessage.success('新增权限组成功！');
          });
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  /** 权限Id重置 */
  function authReset() {
    state.authKeys = [];
    state.authHalfKeys = [];
  }

  /** 获取权限Id */
  function authCheck(checkedKeys: string[], e: any) {
    state.authKeys = checkedKeys;
    state.authHalfKeys = e.halfCheckedKeys as string[];
  }

  /** 拆解权限Id */
  function getAuthNodes() {
    const authIds = concat(state.authKeys, state.authHalfKeys);
    return {
      moduleIds: intersection(authIds, state.moduleLeafs),
      menuIds: difference(authIds, state.moduleLeafs),
    };
  }
</script>
