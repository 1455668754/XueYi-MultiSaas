<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm">
      <template #organize="{ model, field }">
        <BasicTree
          v-model:value="model[field]"
          :treeData="organizeTree"
          :fieldNames="{ title: 'label', key: 'id' }"
          checkable
          toolbar
          title="数据权限"
        />
      </template>
    </BasicForm>
  </BasicModal>
</template>

<script lang="ts">
  import { computed, defineComponent, ref, unref } from 'vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicTree, TreeItem } from '/@/components/Tree';
  import { BasicForm, useForm } from '/@/components/Form';
  import { organizeFormSchema } from './role.data';
  import { editDataScopeApi, getOrganizeRoleApi } from '/@/api/system/authority/role';
  import { RoleIM } from '/@/model/system';
  import { DataScopeEnum } from '/@/enums/system';
  import { organizeScopeApi } from '/@/api/system/organize/organize';
  import { isEqual } from 'lodash-es';

  export default defineComponent({
    name: 'RoleDataModal',
    components: { BasicModal, BasicForm, BasicTree },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const { createMessage } = useMessage();
      const organizeTree = ref<TreeItem[]>([]);

      const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
        labelWidth: 100,
        schemas: organizeFormSchema,
        showActionButtonGroup: false,
      });

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        resetFields();
        setModalProps({ confirmLoading: false });
        const record = data.record as RoleIM;
        if (isEqual(record.dataScope, DataScopeEnum.CUSTOM)) {
          record.organizeIds = await getOrganizeRoleApi(record.id);
        }
        if (unref(organizeTree).length === 0) {
          organizeTree.value = (await organizeScopeApi()) as any as TreeItem[];
        }
        setFieldsValue({
          ...record,
        });
      });

      /** 标题初始化 */
      const getTitle = computed(() => '数据权限分配');

      /** 提交按钮 */
      async function handleSubmit() {
        try {
          const values = (await validate()) as RoleIM;
          setModalProps({ confirmLoading: true });
          await editDataScopeApi(values).then(() => {
            closeModal();
            createMessage.success('角色数据权限修改成功！');
          });
          emit('success');
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      return {
        registerModal,
        registerForm,
        getTitle,
        organizeTree,
        handleSubmit,
      };
    },
  });
</script>
