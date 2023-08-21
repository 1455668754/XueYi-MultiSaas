<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
  import { computed, ref, unref } from 'vue';
  import { formSchema } from './config.data';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { addConfigApi, editConfigApi, getConfigApi } from '@/api/system/dict/config.api';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';

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
      const config = await getConfigApi(data.record.id);
      config.tenantId = config?.enterpriseInfo?.id;
      setFieldsValue({ ...config });
    }
  });

  /** 标题初始化 */
  const getTitle = computed(() => (!unref(isUpdate) ? '新增参数' : '编辑参数'));

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      unref(isUpdate)
        ? await editConfigApi(values).then(() => {
            closeModal();
            createMessage.success('编辑参数成功！');
          })
        : await addConfigApi(values).then(() => {
            closeModal();
            createMessage.success('新增参数成功！');
          });
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
