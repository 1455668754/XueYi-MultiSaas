<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
  import { computed, ref, unref } from 'vue';
  import { formSchema } from './dept.data';
  import { useMessage } from '@/hooks/web/useMessage';
  import { addDeptApi, editDeptApi, getDeptApi, listDeptApi } from '@/api/system/organize/dept.api';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicForm, useForm } from '@/components/Form';

  const emit = defineEmits(['success', 'register']);

  const { createMessage } = useMessage();
  const isUpdate = ref(true);

  const [registerForm, { resetFields, setFieldsValue, updateSchema, validate }] = useForm({
    labelWidth: 100,
    schemas: formSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;

    if (unref(isUpdate)) {
      const dept = await getDeptApi(data.record.id);
      setFieldsValue({ ...dept });
    }
    const treeData = await listDeptApi({
      id: unref(isUpdate) ? data.record.id : undefined,
      defaultNode: true,
      exNodes: unref(isUpdate),
    });
    updateSchema({ field: 'parentId', componentProps: { treeData } });
  });

  /** 标题初始化 */
  const getTitle = computed(() => (!unref(isUpdate) ? '新增部门' : '编辑部门'));

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      unref(isUpdate)
        ? await editDeptApi(values).then(() => {
            closeModal();
            createMessage.success('编辑部门成功！');
          })
        : await addDeptApi(values).then(() => {
            closeModal();
            createMessage.success('新增部门成功！');
          });
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
