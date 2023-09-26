<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
  import { computed } from 'vue';
  import { formSchema } from './tenant.data';
  import { useMessage } from '@/hooks/web/useMessage';
  import { editTenantApi, getTenantApi } from '@/api/tenant/tenant/tenant.api';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicForm, useForm } from '@/components/Form';

  const emit = defineEmits(['success', 'register']);

  const { createMessage } = useMessage();

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 100,
    schemas: formSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    const tenant = await getTenantApi(data.record.id);
    setFieldsValue({
      ...tenant,
    });
  });

  /** 标题初始化 */
  const getTitle = computed(() => '编辑租户');

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      await editTenantApi(values).then(() => {
        closeModal();
        createMessage.success('编辑租户成功！');
      });
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
