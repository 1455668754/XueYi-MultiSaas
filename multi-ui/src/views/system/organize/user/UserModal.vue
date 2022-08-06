<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script lang="ts">
  import { computed, defineComponent, ref, unref } from 'vue';
  import { formSchema } from './user.data';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { getUserApi, addUserApi, editUserApi } from '/@/api/system/organize/user';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';

  export default defineComponent({
    name: 'UserModal',
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
          const user = await getUserApi(data.record.id);
          setFieldsValue({ ...user });
        }
      });

      /** 标题初始化 */
      const getTitle = computed(() => (!unref(isUpdate) ? '新增用户' : '编辑用户'));

      /** 提交按钮 */
      async function handleSubmit() {
        try {
          const values = await validate();
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

      return { registerModal, registerForm, getTitle, handleSubmit };
    },
  });
</script>
