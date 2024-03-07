<template>
  <BasicModal
    v-bind="$attrs"
    :width="700"
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
  import { computed, ref, unref } from 'vue';
  import { useMessage } from '@/hooks/web/useMessage';
  import { addDictTypeApi, editDictTypeApi, getDictTypeApi } from '@/api/system/dict/dictType.api';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicForm, useForm } from '@/components/Form';
  import { typeFormSchema } from './dict.data';
  import { DictTypeIM } from '@/model/system/dict';

  const emit = defineEmits(['success', 'register']);

  const { createMessage } = useMessage();
  const isUpdate = ref(true);

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 100,
    schemas: typeFormSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;

    if (unref(isUpdate)) {
      const dictType = await getDictTypeApi(data.record.id);
      dictType.tenantId = dictType?.enterpriseInfo?.id;
      setFieldsValue({ ...dictType });
    }
  });

  /** 标题初始化 */
  const getTitle = computed(() => (!unref(isUpdate) ? '新增字典类型' : '编辑字典类型'));

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const values: DictTypeIM = await validate();
      setModalProps({ confirmLoading: true });
      unref(isUpdate)
        ? await editDictTypeApi(values).then(() => {
            closeModal();
            createMessage.success('编辑字典类型成功！');
          })
        : await addDictTypeApi(values).then(() => {
            closeModal();
            createMessage.success('新增字典类型成功！');
          });
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
