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

<script lang="ts">
  import { computed, defineComponent, ref, unref } from 'vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicTree, TreeItem } from '/@/components/Tree';
  import { BasicForm, useForm } from '/@/components/Form';
  import { getAuthRoleApi, editAuthScopeApi } from '/@/api/system/authority/role';
  import { authScopeEnterpriseApi } from '/@/api/system/authority/auth';
  import { RoleIM } from '/@/model/system';
  import { authFormSchema } from './role.data';

  export default defineComponent({
    name: 'RoleAuthModal',
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
          if (authKeys.value.length === 0 && values.authIds.length !== 0) {
            closeModal();
            createMessage.success('角色功能权限修改成功！');
          } else {
            await editAuthScopeApi(values.id, authKeys.value.concat(authHalfKeys.value)).then(
              () => {
                closeModal();
                createMessage.success('角色功能权限修改成功！');
              },
            );
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
