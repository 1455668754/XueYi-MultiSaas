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
  import { computed, ref, unref } from 'vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicTree, TreeItem } from '/@/components/Tree';
  import { BasicForm, useForm } from '/@/components/Form';
  import { editAuthScopeApi, getAuthRoleApi } from '@/api/system/authority/role.api';
  import { authScopeEnterpriseApi } from '@/api/system/authority/auth.api';
  import { RoleIM } from '@/model/system';
  import { authFormSchema } from './role.data';

  const emit = defineEmits(['success', 'register']);

  const { createMessage } = useMessage();
  const authTree = ref<TreeItem[]>([]);
  const authKeys = ref<string[]>([]);
  const authHalfKeys = ref<string[]>([]);

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 100,
    schemas: authFormSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    authReset();
    resetFields();
    setModalProps({ confirmLoading: false });
    const record = data.record as RoleIM;
    record.authIds = await getAuthRoleApi(record.id);
    if (unref(authTree).length === 0) {
      authTree.value = (await authScopeEnterpriseApi()) as any as TreeItem[];
    }
    setFieldsValue({
      ...record,
    });
  });

  /** 标题初始化 */
  const getTitle = computed(() => '功能权限分配');

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      await editAuthScopeApi(values.id, authKeys.value.concat(authHalfKeys.value)).then(() => {
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
    authKeys.value = [];
    authHalfKeys.value = [];
  }

  /** 获取权限Id */
  function authCheck(checkedKeys: string[], e) {
    authKeys.value = checkedKeys;
    authHalfKeys.value = e.halfCheckedKeys as string[];
  }
</script>
