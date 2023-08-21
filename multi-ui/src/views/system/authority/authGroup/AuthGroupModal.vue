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
  import { computed, ref, unref } from 'vue';
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
  import { authScopeTenantApi } from '@/api/tenant/tenant/tenant.api';
  import { getTreeNodes } from '@/utils/core/treeUtil';
  import { concat, difference, intersection } from 'lodash-es';

  const emit = defineEmits(['success', 'register']);
  const { createMessage } = useMessage();
  const isUpdate = ref(true);
  const authTree = ref<TreeItem[]>([]);
  const authKeys = ref<string[]>([]);
  const authHalfKeys = ref<string[]>([]);

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
      authTree.value = (await authScopeTenantApi()) as any as TreeItem[];
    }

    if (unref(isUpdate)) {
      const authGroup = await getAuthGroupApi(data.record.id);
      setFieldsValue({ ...authGroup });
      if (authGroup?.authIds) {
        authSetFieldsValue({ authIds: authGroup?.authIds });
      }
    }
  });

  /** 标题初始化 */
  const getTitle = computed(() => (!unref(isUpdate) ? '新增权限组' : '编辑权限组'));

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const values = await validate();
      const { authIds, moduleIds, menuIds } = getAuthNodes();

      values.authIds = authIds;
      values.moduleIds = moduleIds;
      values.menuIds = menuIds;
      setModalProps({ confirmLoading: true });
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
    authKeys.value = [];
    authHalfKeys.value = [];
  }

  /** 获取权限Id */
  function authCheck(checkedKeys: string[], e: any) {
    authKeys.value = checkedKeys;
    authHalfKeys.value = e.halfCheckedKeys as string[];
  }

  /** 拆解权限Id */
  function getAuthNodes() {
    const allModuleIds = authTree.value.map((item) => item.id);
    const authIds = getTreeNodes(authTree.value, authKeys.value, 'id', 'children');
    const moduleIds = concat(
      intersection(allModuleIds, authKeys.value),
      intersection(allModuleIds, authHalfKeys.value),
    );
    const menuIds = difference(authIds, moduleIds);
    return {
      authIds: authIds,
      moduleIds: moduleIds,
      menuIds: menuIds,
    };
  }
</script>
