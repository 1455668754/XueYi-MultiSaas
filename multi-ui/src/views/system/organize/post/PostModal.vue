<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
  import { computed, ref, unref } from 'vue';
  import { formSchema } from './post.data';
  import { useMessage } from '@/hooks/web/useMessage';
  import { addPostApi, editPostApi, getPostApi } from '@/api/system/organize/post.api';
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
      const post = await getPostApi(data.record.id);
      setFieldsValue({ ...post });
    }
  });

  /** 标题初始化 */
  const getTitle = computed(() => (!unref(isUpdate) ? '新增岗位' : '编辑岗位'));

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      unref(isUpdate)
        ? await editPostApi(values).then(() => {
            closeModal();
            createMessage.success('编辑岗位成功！');
          })
        : await addPostApi(values).then(() => {
            closeModal();
            createMessage.success('新增岗位成功！');
          });
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
