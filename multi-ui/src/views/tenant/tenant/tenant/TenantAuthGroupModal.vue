<template>
  <BasicModal
    :destroyOnClose="true"
    v-bind="$attrs"
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
  import { computed } from 'vue';
  import { useMessage } from '@/hooks/web/useMessage';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicForm, useForm } from '@/components/Form';
  import { editAuthGroupTenantApi, getAuthGroupTenantApi } from '@/api/tenant/tenant/tenant.api';
  import { authGroupFormSchema } from './tenant.data';

  const emit = defineEmits(['success', 'register']);

  const { createMessage } = useMessage();

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 70,
    schemas: authGroupFormSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    const tenantInfo = await getAuthGroupTenantApi(data.record.id);
    setFieldsValue({
      ...tenantInfo,
    });
  });

  /** 标题初始化 */
  const getTitle = computed(() => '权限组分配');

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      await editAuthGroupTenantApi(values.id, values.authGroupIds).then(() => {
        closeModal();
        createMessage.success('权限组分配成功！');
      });
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
