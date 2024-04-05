<template>
  <BasicDrawer
    v-bind="$attrs"
    :title="getTitle"
    @register="registerDrawer"
    width="40%"
    showFooter
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
    <DataIndex v-if="isUpdate" ref="dataRef" />
  </BasicDrawer>
</template>

<script setup lang="ts">
  import { computed, ref, unref } from 'vue';
  import { BasicDrawer, useDrawerInner } from '@/components/Drawer';
  import { useMessage } from '@/hooks/web/useMessage';
  import { addDictTypeApi, editDictTypeApi, getDictTypeApi } from '@/api/system/dict/dictType.api';
  import { BasicForm, useForm } from '@/components/Form';
  import { typeFormSchema } from './dict.data';
  import { DictTypeIM } from '@/model/system/dict';
  import DataIndex from './DataIndex.vue';

  const emit = defineEmits(['success', 'register']);

  const { createMessage } = useMessage();
  const isUpdate = ref(true);
  const dataRef = ref<{
    onChangeDictInfo: Function;
  }>({
    onChangeDictInfo: () => {},
  });

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 100,
    schemas: typeFormSchema,
    showActionButtonGroup: false,
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    await resetFields();
    setDrawerProps({ loading: true, confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;

    if (unref(isUpdate)) {
      const dictType = await getDictTypeApi(data.record.id);
      dictType.tenantId = dictType?.enterpriseInfo?.id;
      dataRef.value.onChangeDictInfo(dictType);
      await setFieldsValue({ ...dictType });
    }
    setDrawerProps({ loading: false });
  });

  /** 标题初始化 */
  const getTitle = computed(() => (!unref(isUpdate) ? '新增字典类型' : '编辑字典类型'));

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const values: DictTypeIM = await validate();
      setDrawerProps({ confirmLoading: true });
      unref(isUpdate)
        ? await editDictTypeApi(values).then(() => {
            closeDrawer();
            createMessage.success('编辑字典类型成功！');
          })
        : await addDictTypeApi(values).then(() => {
            closeDrawer();
            createMessage.success('新增字典类型成功！');
          });
      emit('success');
    } finally {
      setDrawerProps({ confirmLoading: false });
    }
  }
</script>
