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
          @check="dataScopeCheck"
          title="数据权限"
        />
      </template>
    </BasicForm>
  </BasicModal>
</template>

<script setup lang="ts">
  import { computed, reactive, ref, unref } from 'vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicTree, TreeItem } from '/@/components/Tree';
  import { BasicForm, useForm } from '/@/components/Form';
  import { getOrgDeptIds, organizeFormSchema } from './role.data';
  import { editDataScopeApi, getOrganizeRoleApi } from '@/api/system/authority/role.api';
  import { RoleIM } from '@/model/system/authority';
  import { DataScopeEnum } from '@/enums/system/authority';
  import { organizeScopeApi } from '@/api/system/organize/organize.api';
  import { concat, difference, intersection, isEqual } from 'lodash-es';

  const emit = defineEmits(['success', 'register']);

  const { createMessage } = useMessage();
  const organizeTree = ref<TreeItem[]>([]);
  const dataScope = reactive<{
    treeLastLeaf: string[];
    deptLeafs: string[];
    authKeys: string[];
  }>({
    treeLastLeaf: [],
    deptLeafs: [],
    authKeys: [],
  });

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 100,
    schemas: organizeFormSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    authReset();
    resetFields();
    setModalProps({ confirmLoading: false });
    const role = await getOrganizeRoleApi(data.record.id);

    if (unref(organizeTree).length === 0) {
      const orgTree = await organizeScopeApi();
      dataScope.treeLastLeaf = getOrgDeptIds(orgTree);
      organizeTree.value = orgTree as unknown as TreeItem[];
      dataScope.deptLeafs = organizeTree.value.map((item) => item.id);
    }

    if (isEqual(role.dataScope, DataScopeEnum.CUSTOM)) {
      // 初始化权限树回显
      role.organizeIds = concat(role.orgDeptIds, role.orgPostIds);
      dataScope.authKeys = role.organizeIds;
    }
    setFieldsValue({
      ...role,
    });
  });

  /** 标题初始化 */
  const getTitle = computed(() => '数据权限分配');

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const values = (await validate()) as RoleIM;
      setModalProps({ confirmLoading: true });
      values.organizeIds = undefined;
      if (values.dataScope === DataScopeEnum.CUSTOM) {
        const { orgDeptIds, orgPostIds } = getDataScopeNodes();
        values.orgDeptIds = orgDeptIds;
        values.orgPostIds = orgPostIds;
      }
      await editDataScopeApi(values).then(() => {
        closeModal();
        createMessage.success('角色数据权限修改成功！');
      });
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  /** 权限Id重置 */
  function authReset() {
    dataScope.authKeys = [];
  }

  /** 获取组织Id */
  function dataScopeCheck(checkedKeys: string[]) {
    dataScope.authKeys = checkedKeys;
  }

  /** 拆解数据权限Id */
  function getDataScopeNodes() {
    const organizeIds = dataScope.authKeys;
    return {
      orgDeptIds: intersection(organizeIds, dataScope.deptLeafs),
      orgPostIds: difference(organizeIds, dataScope.deptLeafs),
    };
  }
</script>
