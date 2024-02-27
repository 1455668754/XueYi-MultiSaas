<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm">
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
  </BasicModal>
</template>

<script setup lang="ts">
  import { computed, reactive, ref, unref } from 'vue';
  import { useMessage } from '@/hooks/web/useMessage';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicTree, TreeItem } from '@/components/Tree';
  import { BasicForm, useForm } from '@/components/Form';
  import { editAuthScopeApi, getAuthRoleApi } from '@/api/system/authority/role.api';
  import { authScopeEnterpriseApi } from '@/api/system/authority/auth.api';
  import { authFormSchema } from './role.data';
  import { concat, difference, intersection } from 'lodash-es';
  import { getTreeNodes } from '@/utils/core/treeUtil';
  import { RoleIM } from '@/model/system/authority';

  const emit = defineEmits(['success', 'register']);

  const { createMessage } = useMessage();
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
    schemas: authFormSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    authReset();
    resetFields();
    setModalProps({ confirmLoading: false });
    const role = await getAuthRoleApi(data.record.id);

    if (unref(authTree).length === 0) {
      authTree.value = (await authScopeEnterpriseApi()) as any as TreeItem[];
      state.treeLastLeaf = getTreeNodes(authTree.value, 'id', 'children');
      state.moduleLeafs = authTree.value.map((item) => item.id);
    }

    // 初始化权限树回显
    role.authIds = concat(
      intersection(role.moduleIds, state.treeLastLeaf),
      intersection(role.menuIds, state.treeLastLeaf),
    );
    state.authKeys = concat(role.moduleIds, role.menuIds);
    setFieldsValue({
      ...role,
    });
  });

  /** 标题初始化 */
  const getTitle = computed(() => '功能权限分配');

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const values: RoleIM = await validate();
      setModalProps({ confirmLoading: true });
      const { moduleIds, menuIds } = getAuthNodes();
      values.authIds = undefined;
      values.moduleIds = moduleIds;
      values.menuIds = menuIds;
      await editAuthScopeApi(values).then(() => {
        closeModal();
        createMessage.success('角色功能权限修改成功！');
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
