<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm">
      <template #auth="{ model, field }">
        <Transfer
          v-model:target-keys="model[field]"
          :data-source="authGroupList"
          :titles="['待选', '已选']"
          show-search
          :filter-option="filterOption"
          :render="(item) => item.name"
          :rowKey="(item) => item.id"
        />
      </template>
    </BasicForm>
  </BasicModal>
</template>

<script setup lang="ts">
  import { computed, ref, unref } from 'vue';
  import { Transfer } from 'ant-design-vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';
  import { AuthGroupLM } from '@/model/system/authority';
  import { DicStatusEnum } from '@/enums';
  import { TransferItem } from 'ant-design-vue/lib/transfer';
  import { editAuthGroupTenantApi, getAuthGroupTenantApi } from '@/api/tenant/tenant/tenant.api';
  import { listAuthGroupApi } from '@/api/system/authority/authGroup.api';
  import { authGroupFormSchema } from './tenant.data';

  const emit = defineEmits(['success', 'register']);

  const { createMessage } = useMessage();
  const authGroupList = ref<AuthGroupLM>([]);

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 70,
    schemas: authGroupFormSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    authGroupList.value = [];
    setModalProps({ confirmLoading: false });
    const tenantInfo = await getAuthGroupTenantApi(data.record.id);
    if (unref(authGroupList).length === 0) {
      authGroupList.value = await listAuthGroupApi({ status: DicStatusEnum.NORMAL }).then(
        (item) => item.items,
      );
    }
    setFieldsValue({
      ...tenantInfo,
    });
  });

  /** 标题初始化 */
  const getTitle = computed(() => '权限组分配');

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      await editAuthGroupTenantApi(values.id, values.authGroupIds).then(() => {
        closeModal();
        createMessage.success('权限组分配成功！');
      });
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  const filterOption = (inputValue: string, option: TransferItem) => {
    return option?.name.indexOf(inputValue) > -1;
  };
</script>
