<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
    <template #insertFooter>
      <a-button type="primary" danger @click="connection">连接测试</a-button>
    </template>
  </BasicModal>
</template>

<script setup lang="ts">
  import { computed, ref, unref } from 'vue';
  import { formSchema } from './source.data';
  import { useMessage } from '@/hooks/web/useMessage';
  import {
    addSourceApi,
    connectionSourceApi,
    editSourceApi,
    getSourceApi,
  } from '@/api/tenant/source/source.api';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicForm, useForm } from '@/components/Form';

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
      const source = await getSourceApi(data.record.id);
      setFieldsValue({
        ...source,
      });
    }
  });

  /** 标题初始化 */
  const getTitle = computed(() => (!unref(isUpdate) ? '新增数据源' : '编辑数据源'));

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      unref(isUpdate)
        ? await editSourceApi(values).then(() => {
            closeModal();
            createMessage.success('编辑数据源成功！');
          })
        : await addSourceApi(values).then(() => {
            closeModal();
            createMessage.success('新增数据源成功！');
          });
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  /** 连接测试按钮 */
  async function connection() {
    const values = await validate();
    await connectionSourceApi(values).then(() => createMessage.success('数据源连接成功！'));
  }
</script>
