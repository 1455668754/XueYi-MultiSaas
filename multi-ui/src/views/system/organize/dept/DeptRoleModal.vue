<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm">
      <template #role="{ model, field }">
        <Transfer
          v-model:target-keys="model[field]"
          :data-source="roleList"
          :titles="['待选', '已选']"
          show-search
          :filter-option="filterOption"
          :render="(item) => item.name"
          :rowKey="(item) => item.id"
        />
      </template>
    </BasicForm>
  </BasicModal>
</template>

<script lang="ts">
  import { computed, defineComponent, ref, unref } from 'vue';
  import { Transfer } from 'ant-design-vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';
  import { optionRoleApi } from '@/api/system/authority/role.api';
  import { DeptIM, RoleIM, RoleLM } from '@/model/system';
  import { roleFormSchema } from './dept.data';
  import { editAuthDeptScopeApi, getAuthDeptApi } from '@/api/system/organize/dept.api';

  export default defineComponent({
    name: 'UserRoleModal',
    components: { BasicModal, BasicForm, Transfer },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const { createMessage } = useMessage();
      const roleList = ref<RoleLM>([]);

      const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
        labelWidth: 70,
        schemas: roleFormSchema,
        showActionButtonGroup: false,
      });

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        resetFields();
        roleList.value = [];
        setModalProps({ confirmLoading: false });
        const record = data.record as DeptIM;
        record.roleIds = await getAuthDeptApi(record.id);
        if (unref(roleList).length === 0) {
          roleList.value = await optionRoleApi().then((item) => item.items);
        }
        setFieldsValue({
          ...record,
        });
      });

      /** 标题初始化 */
      const getTitle = computed(() => '角色分配');

      /** 提交按钮 */
      async function handleSubmit() {
        try {
          const values = await validate();
          setModalProps({ confirmLoading: true });
          await editAuthDeptScopeApi(values.id, values.roleIds).then(() => {
            closeModal();
            createMessage.success('角色分配成功！');
          });
          emit('success');
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      const filterOption = (inputValue: string, option: RoleIM) => {
        return option.name.indexOf(inputValue) > -1;
      };

      return {
        registerModal,
        registerForm,
        getTitle,
        roleList,
        handleSubmit,
        filterOption,
      };
    },
  });
</script>
