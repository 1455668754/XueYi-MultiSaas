<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm">
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
  </BasicModal>
</template>

<script lang="ts">
  import { computed, defineComponent, ref, unref } from 'vue';
  import { authFormSchema } from './tenant.data';
  import { useMessage } from '/@/hooks/web/useMessage';
  import {
    authScopeTenantApi,
    editAuthTenantApi,
    getAuthTenantApi,
  } from '/@/api/tenant/tenant/tenant';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicTree, TreeItem } from '/@/components/Tree';
  import { BasicForm, useForm } from '/@/components/Form';
  import { TenantIM } from '/@/model/tenant';

  export default defineComponent({
    name: 'TenantAuthModal',
    components: { BasicModal, BasicForm, BasicTree },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const { createMessage } = useMessage();
      const authTree = ref<TreeItem[]>([]);
      const authKeys = ref<(string | number)[]>([]);
      const authHalfKeys = ref<(string | number)[]>([]);

      const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
        labelWidth: 100,
        schemas: authFormSchema,
        showActionButtonGroup: false,
      });

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        authReset();
        resetFields();
        setModalProps({ confirmLoading: false });
        const record = data.record as TenantIM;
        record.authIds = await getAuthTenantApi(record.id);
        if (unref(authTree).length === 0) {
          authTree.value = (await authScopeTenantApi()) as any as TreeItem[];
        }
        setFieldsValue({
          ...record,
        });
      });

      /** 标题初始化 */
      const getTitle = computed(() => '租户权限分配');

      /** 提交按钮 */
      async function handleSubmit() {
        try {
          const values = (await validate()) as TenantIM;
          setModalProps({ confirmLoading: true });
          if (authKeys.value.length === 0 && values.authIds.length !== 0) {
            closeModal();
            createMessage.success('角色功能权限修改成功！');
          } else {
            values.authIds = authKeys.value.concat(authHalfKeys.value);
            await editAuthTenantApi(values).then(() => {
              closeModal();
              createMessage.success('租户权限分配成功！');
            });
          }
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
      function authCheck(checkedKeys, e) {
        authKeys.value = checkedKeys as (string | number)[];
        authHalfKeys.value = e.halfCheckedKeys as (string | number)[];
      }

      return {
        registerModal,
        registerForm,
        getTitle,
        authTree,
        handleSubmit,
        authCheck,
      };
    },
  });
</script>
