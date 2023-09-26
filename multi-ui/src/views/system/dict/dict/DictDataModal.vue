<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
  import { computed, ref, unref } from 'vue';
  import { dataFormSchema } from './dict.data';
  import { useMessage } from '@/hooks/web/useMessage';
  import { addDictDataApi, editDictDataApi, getDictDataApi } from '@/api/system/dict/dictData.api';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicForm, useForm } from '@/components/Form';
  import { DictTypeIM } from '@/model/system/dict';

  const emit = defineEmits(['success', 'register']);

  const { createMessage } = useMessage();
  const isUpdate = ref(true);
  const dictTypeInfo = ref<DictTypeIM>();

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 100,
    schemas: dataFormSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;
    dictTypeInfo.value = data?.dictTypeInfo;
    if (unref(isUpdate)) {
      const dictData = await getDictDataApi(data.record.id);
      setFieldsValue({ ...dictData });
    } else {
      setFieldsValue({ code: dictTypeInfo.value?.code });
    }
  });

  /** 标题初始化 */
  const getTitle = computed(() => (!unref(isUpdate) ? '新增字典数据' : '编辑字典数据'));

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const values = await validate();
      values.tenantId = dictTypeInfo.value?.tenantId;
      values.dictTypeId = dictTypeInfo.value?.id;
      setModalProps({ confirmLoading: true });
      unref(isUpdate)
        ? await editDictDataApi(values).then(() => {
            closeModal();
            createMessage.success('编辑字典数据成功！');
          })
        : await addDictDataApi(values).then(() => {
            closeModal();
            createMessage.success('新增字典数据成功！');
          });
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
