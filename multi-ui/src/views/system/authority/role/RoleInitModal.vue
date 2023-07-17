<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="getTitle"
    :defaultFullscreen="true"
    :showCancelBtn="false"
    :showOkBtn="current === 2"
    @ok="handleSubmit"
  >
    <div class="step-form-form">
      <a-steps :current="current">
        <a-step v-for="item in roleInitList" :title="item.title" :key="item.key" />
      </a-steps>

      <div class="mt-5">
        <BasicForm @register="roleRegister" v-show="current === roleInitList[0].current" />
        <BasicForm @register="authRegister" v-show="current === roleInitList[1].current">
          <template #auth="{ model, field }">
            <BasicTree
              v-model:value="model[field]"
              :treeData="authTree"
              :fieldNames="{ title: 'label', key: 'id' }"
              checkable
              toolbar
              @check="authCheck"
              title="功能权限"
            />
          </template>
        </BasicForm>
        <BasicForm @register="organizeRegister" v-show="current === roleInitList[2].current">
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
      </div>
    </div>
    <template #centerFooter>
      <a-button type="default" @click="handleStepPrev" v-show="current > 0"> 上一步</a-button>
    </template>
    <template #appendFooter>
      <a-button type="primary" @click="handleStepNext" v-show="current < 2"> 下一步</a-button>
    </template>
  </BasicModal>
</template>

<script lang="ts">
  import { computed, defineComponent, ref, unref } from 'vue';
  import { authFormSchema, organizeFormSchema, roleFormSchema, roleInitList } from './role.data';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';
  import { BasicTree, TreeItem } from '/@/components/Tree';
  import { Steps } from 'ant-design-vue';
  import { addRoleApi } from '/@/api/system/authority/role';
  import { authScopeEnterpriseApi } from '/@/api/system/authority/auth';
  import { organizeScopeApi } from '/@/api/system/organize/organize';
  import { sourceAssign } from '/@/utils/xueyi';

  export default defineComponent({
    name: 'RoleInitModal',
    components: {
      BasicModal,
      BasicForm,
      BasicTree,
      [Steps.name]: Steps,
      [Steps.Step.name]: Steps.Step,
    },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const { createMessage } = useMessage();
      const current = ref(0);
      const authTree = ref<TreeItem[]>([]);
      const organizeTree = ref<TreeItem[]>([]);
      const authKeys = ref<string[]>([]);
      const authHalfKeys = ref<string[]>([]);

      const [roleRegister, { resetFields: roleResetFields, validate: roleValidate }] = useForm({
        labelWidth: 100,
        schemas: roleFormSchema,
        showActionButtonGroup: false,
      });

      const [
        authRegister,
        {
          setFieldsValue: authSetFieldsValue,
          resetFields: authResetFields,
          validate: authValidate,
        },
      ] = useForm({
        labelWidth: 100,
        schemas: authFormSchema,
        showActionButtonGroup: false,
      });

      const [
        organizeRegister,
        {
          setFieldsValue: organizeSetFieldsValue,
          resetFields: organizeResetFields,
          validate: organizeValidate,
        },
      ] = useForm({
        labelWidth: 100,
        schemas: organizeFormSchema,
        showActionButtonGroup: false,
      });

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async () => {
        current.value = 0;
        Promise.all([roleResetFields(), authResetFields(), organizeResetFields()]);
        authReset();
        if (unref(authTree).length === 0) {
          authTree.value = (await authScopeEnterpriseApi()) as any as TreeItem[];
        }
        if (unref(organizeTree).length === 0) {
          organizeTree.value = (await organizeScopeApi()) as any as TreeItem[];
        }
        setModalProps({ confirmLoading: false });
      });

      /** 标题初始化 */
      const getTitle = computed(() => '新增角色');

      /** 上一步按钮 */
      function handleStepPrev() {
        current.value--;
      }

      /** 下一步按钮 */
      async function handleStepNext() {
        switch (current.value) {
          case 0:
            await roleValidate();
            await setInitName();
            break;
          case 1:
            await authValidate();
            break;
          case 2:
            await organizeValidate();
            break;
        }
        current.value++;
      }

      /** 重置其他页面值 */
      async function setInitName() {
        const { code, name } = await roleValidate();
        await authSetFieldsValue({ code: code, name: name });
        await organizeSetFieldsValue({ code: code, name: name });
      }

      /** 提交按钮 */
      async function handleSubmit() {
        try {
          const [role, auth, organize] = await Promise.all([
            roleValidate(),
            authValidate(),
            organizeValidate(),
          ]);
          setModalProps({ confirmLoading: true });
          const data = sourceAssign({}, auth, organize, role);
          data.authIds = authKeys.value.concat(authHalfKeys.value);
          await addRoleApi(data).then(() => {
            closeModal();
            createMessage.success('新增角色成功！');
          });
          emit('success');
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      /** 权限Id重置 */
      function authReset() {
        authKeys.value = [];
        authHalfKeys.value = [];
      }

      /** 获取权限Id */
      function authCheck(checkedKeys: string[], e) {
        authKeys.value = checkedKeys;
        authHalfKeys.value = e.halfCheckedKeys as string[];
      }

      return {
        roleInitList,
        current,
        registerModal,
        roleRegister,
        authRegister,
        organizeRegister,
        getTitle,
        authTree,
        organizeTree,
        authCheck,
        handleStepPrev,
        handleStepNext,
        handleSubmit,
      };
    },
  });
</script>

<style lang="less" scoped>
  .step-form-content {
    padding: 24px;
    background-color: @component-background;
  }

  .step-form-form {
    width: 750px;
    margin: 0 auto;
  }

  .step {
    &-form {
      width: 450px;
      margin: 0 auto;
    }
  }
</style>
