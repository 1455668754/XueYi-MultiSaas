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

<script lang="ts">
  import { computed, defineComponent, ref, unref } from 'vue';
  import { formSchema } from './job.data';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { getJobApi, addJobApi, editJobApi } from '/@/api/system/system/job';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';

  export default defineComponent({
    name: 'JobModal',
    components: { BasicModal, BasicForm },
    emits: ['success', 'register'],
    setup(_, { emit }) {
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

      return { registerModal, registerForm, getTitle, handleSubmit };
    },
  });
</script>
