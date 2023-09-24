<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
  import { computed } from 'vue';
  import { roleFormSchema } from './role.data';
  import { useMessage } from '@/hooks/web/useMessage';
  import { editRoleApi, getRoleApi } from '@/api/system/authority/role.api';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicForm, useForm } from '@/components/Form';

  const emit = defineEmits(['success', 'register']);

  const { createMessage } = useMessage();

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 100,
    schemas: roleFormSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    const role = await getRoleApi(data.record.id);
    setFieldsValue({
      ...role,
    });
  });

  /** 标题初始化 */
  const getTitle = computed(() => '编辑角色');

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      await editRoleApi(values).then(() => {
        closeModal();
        createMessage.success('编辑角色成功！');
      });
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
