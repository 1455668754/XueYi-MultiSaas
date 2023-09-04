<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <div class="py-8 bg-white flex flex-col justify-center items-center">
      <BasicForm @register="registerForm" />
    </div>
  </BasicModal>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';
  import { ListItemIM, SecureEnum, secureFormSchema, secureSettingList } from './data';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useUserStore } from '/@/store/modules/user';
  import {
    resetUserEmailApi,
    resetUserNameApi,
    resetUserPhoneApi,
    resetUserPwdApi,
  } from '@/api/sys/user.api';

  /** 标题初始化 */
  const getTitle = ref('');
  const userStore = useUserStore();

  const { createMessage } = useMessage();
  const [registerForm, { validate, setFieldsValue, resetFields }] = useForm({
    size: 'large',
    labelWidth: 100,
    showActionButtonGroup: false,
    schemas: secureFormSchema,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    const item = secureSettingList.find((item) => item.code === data.record.code) as ListItemIM;
    getTitle.value = item?.extra + item?.title;
    setModalProps({ confirmLoading: false });
    setFieldsValue({ code: data.record.code });
  });

  async function handleSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      const { code } = values;
      switch (code) {
        case SecureEnum.USER_NAME:
          const { userName } = values;
          await resetUserNameApi(userName).then(() => {
            const userInfo = userStore.getUserInfo;
            userInfo.userName = userName;
            userStore.setUserInfo(userInfo);
            closeModal();
            createMessage.success('账号修改成功！');
          });
          break;
        case SecureEnum.PASSWORD:
          const { passwordOld, passwordNew } = values;
          await resetUserPwdApi(passwordOld, passwordNew).then(async () => {
            closeModal();
            createMessage.success('密码修改成功！');
            await useUserStore().logout(true);
          });
          break;
        case SecureEnum.PHONE:
          const { phone } = values;
          await resetUserPhoneApi(phone).then(() => {
            const userInfo = userStore.getUserInfo;
            userInfo.phone = phone;
            userStore.setUserInfo(userInfo);
            closeModal();
            createMessage.success('密保手机修改成功！');
          });
          break;
        case SecureEnum.EMAIL:
          const { email } = values;
          await resetUserEmailApi(email).then(() => {
            const userInfo = userStore.getUserInfo;
            userInfo.email = email;
            userStore.setUserInfo(userInfo);
            closeModal();
            createMessage.success('备用邮箱修改成功！');
          });
          break;
        default:
          break;
      }
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
