<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
  import { computed, ref, unref } from 'vue';
  import { formSchema } from './strategy.data';
  import { useMessage } from '@/hooks/web/useMessage';
  import {
    addStrategyApi,
    editStrategyApi,
    getStrategyApi,
  } from '@/api/tenant/source/strategy.api';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicForm, useForm } from '@/components/Form';
  import { StrategyIM } from '@/model/tenant/source';

  const emit = defineEmits(['success', 'register']);

  const { createMessage } = useMessage();
  const isUpdate = ref(true);

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 100,
    schemas: formSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;

    if (unref(isUpdate)) {
      const strategy = await getStrategyApi(data.record.id);
      setFieldsValue({
        ...strategy,
      });
    }
  });

  /** 标题初始化 */
  const getTitle = computed(() => (!unref(isUpdate) ? '新增源策略' : '编辑源策略'));

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const values: StrategyIM = await validate();
      setModalProps({ confirmLoading: true });
      unref(isUpdate)
        ? await editStrategyApi(values).then(() => {
            closeModal();
            createMessage.success('编辑源策略成功！');
          })
        : await addStrategyApi(values).then(() => {
            closeModal();
            createMessage.success('新增源策略成功！');
          });
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
