<template>
  <BasicModal
    v-bind="$attrs"
    :destroyOnClose="true"
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
  import { roleFormSchema } from './post.data';
  import { editAuthPostScopeApi, getAuthPostApi } from '@/api/system/organize/post.api';

  const emit = defineEmits(['success', 'register']);

  const { createMessage } = useMessage();

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 70,
    schemas: roleFormSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    const record = await getAuthPostApi(data.record.id);
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
      await editAuthPostScopeApi(values.id, values.roleIds).then(() => {
        closeModal();
        createMessage.success('角色分配成功！');
      });
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
