<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="密码重置" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
  import { resPwdFormSchema } from './user.data';
  import { useMessage } from '@/hooks/web/useMessage';
  import { resetUserPwdApi } from '@/api/system/organize/user.api';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicForm, useForm } from '@/components/Form';

  const { createMessage } = useMessage();

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 100,
    schemas: resPwdFormSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    setFieldsValue({ ...data.record });
  });

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const { id, password } = await validate();
      setModalProps({ confirmLoading: true });
      await resetUserPwdApi(id, password).then(() => {
        closeModal();
        createMessage.success('密码重置成功！');
      });
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
