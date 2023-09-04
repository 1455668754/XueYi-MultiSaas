<template>
  <BasicModal
    v-bind="$attrs"
    :width="800"
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
  import { computed, ref, unref } from 'vue';
  import { formSchema, initialize } from './menu.data';
  import { useMessage } from '/@/hooks/web/useMessage';
  import {
    addMenuApi,
    editMenuApi,
    getMenuApi,
    getMenuRouteListApi,
  } from '@/api/system/authority/menu.api';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';
  import { COMMON_MODULE, MenuTypeEnum } from '@/enums/system/authority';

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
      const menu = await getMenuApi(data.record.id);
      menu.tenantId = menu?.enterpriseInfo?.id;
      setMenuTree(menu.id, menu.moduleId);
      setFieldsValue({
        ...menu,
      });
    } else {
      setMenuTree(undefined, COMMON_MODULE);
    }
  });

  /** 标题初始化 */
  const getTitle = computed(() => (!unref(isUpdate) ? '新增菜单' : '编辑菜单'));

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      unref(isUpdate)
        ? await editMenuApi(initialize(values)).then(() => {
            closeModal();
            createMessage.success('编辑菜单成功！');
          })
        : await addMenuApi(initialize(values)).then(() => {
            closeModal();
            createMessage.success('新增菜单成功！');
          });
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  /** 生成菜单树 */
  async function setMenuTree(id: string | undefined, moduleId: string) {
    const treeData = await getMenuRouteListApi({
      id: id,
      moduleId: moduleId,
      menuTypeLimit: MenuTypeEnum.DIR,
      exNodes: id !== undefined,
      defaultNode: true,
    });
    updateSchema({
      field: 'parentId',
      componentProps: { treeData },
    });
  }
</script>
