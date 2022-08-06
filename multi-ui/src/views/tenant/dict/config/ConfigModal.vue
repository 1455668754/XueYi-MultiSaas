<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script lang="ts">
  import { computed, defineComponent, ref, unref } from 'vue';
  import { formSchema } from './config.data';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { getConfigApi, addConfigApi, editConfigApi } from '/@/api/tenant/dict/config';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';

  export default defineComponent({
    name: 'ConfigModal',
    components: { BasicModal, BasicForm },
    emits: ['success', 'register'],
    setup(_, { emit }) {
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

      return { registerModal, registerForm, getTitle, handleSubmit };
    },
  });
</script>
