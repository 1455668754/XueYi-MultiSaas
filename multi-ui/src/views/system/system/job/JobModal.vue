<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :width="700"
    :title="getTitle"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
  import { computed, ref, unref } from 'vue';
  import { formSchema } from './job.data';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { addJobApi, editJobApi, getJobApi } from '@/api/system/system/job.api';
  import { useModalInner } from '/@/components/Modal';
  import { useForm } from '/@/components/Form';

  const emit = defineEmits(['success', 'register']);
  const { createMessage } = useMessage();
  const isUpdate = ref(true);

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 140,
    schemas: formSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;

    if (unref(isUpdate)) {
      const job = await getJobApi(data.record.id);
      setFieldsValue({ ...job });
    }
  });

  /** 标题初始化 */
  const getTitle = computed(() => (!unref(isUpdate) ? '新增调度任务' : '编辑调度任务'));

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      unref(isUpdate)
        ? await editJobApi(values).then(() => {
            closeModal();
            createMessage.success('编辑调度任务成功！');
          })
        : await addJobApi(values).then(() => {
            closeModal();
            createMessage.success('新增调度任务成功！');
          });
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
