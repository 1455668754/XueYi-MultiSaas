<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
  import { computed, ref, unref } from 'vue';
  import { formSchema } from './module.data';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { addModuleApi, editModuleApi, getModuleApi } from '@/api/system/authority/module.api';
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
      const module = await getModuleApi(data.record.id);
      module.tenantId = module?.enterpriseInfo?.id;
      setFieldsValue({
        ...module,
      });
    }
  });

  /** 标题初始化 */
  const getTitle = computed(() => (!unref(isUpdate) ? '新增模块' : '编辑模块'));

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      unref(isUpdate)
        ? await editModuleApi(values).then(() => {
            closeModal();
            createMessage.success('编辑模块成功！');
          })
        : await addModuleApi(values).then(() => {
            closeModal();
            createMessage.success('新增模块成功！');
          });
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
