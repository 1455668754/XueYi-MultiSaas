<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script lang="ts">
  import { computed, defineComponent, ref, unref } from 'vue';
  import { formSchema } from './module.data';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { getModuleApi, addModuleApi, editModuleApi } from '/@/api/system/authority/module';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';

  export default defineComponent({
    name: 'ModuleModal',
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
          const module = await getModuleApi(data.record.id);
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

      return { registerModal, registerForm, getTitle, handleSubmit };
    },
  });
</script>
