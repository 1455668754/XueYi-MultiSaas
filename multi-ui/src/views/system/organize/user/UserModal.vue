<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
  import { computed, ref, unref } from 'vue';
  import { formSchema } from './user.data';
  import { useMessage } from '@/hooks/web/useMessage';
  import { addUserApi, editUserApi, getUserApi } from '@/api/system/organize/user.api';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicForm, useForm } from '@/components/Form';
  import { UserIM } from '@/model/system/organize';

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
      const user = await getUserApi(data.record.id);
      setFieldsValue({ ...user });
    }
  });

  /** 标题初始化 */
  const getTitle = computed(() => (!unref(isUpdate) ? '新增用户' : '编辑用户'));

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const values: UserIM = await validate();
      setModalProps({ confirmLoading: true });
      unref(isUpdate)
        ? await editUserApi(values).then(() => {
            closeModal();
            createMessage.success('编辑用户成功！');
          })
        : await addUserApi(values).then(() => {
            closeModal();
            createMessage.success('新增用户成功！');
          });
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
