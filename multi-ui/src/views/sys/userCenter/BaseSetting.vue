<template>
  <CollapseContainer title="基本设置" :canExpan="false">
    <a-row :gutter="24">
      <a-col :span="14">
        <BasicForm @register="register" />
      </a-col>
      <a-col :span="10">
        <div class="change-avatar">
          <div class="mb-2">头像</div>
          <CropperAvatar
            :uploadApi="editAvatarApi"
            :value="avatar"
            btnText="更换头像"
            :btnProps="{ preIcon: 'ant-design:cloud-upload-outlined' }"
            @change="updateAvatar"
            width="150"
          />
        </div>
      </a-col>
    </a-row>
    <Button type="primary" @click="handleSubmit"> 更新基本信息</Button>
  </CollapseContainer>
</template>

<script setup lang="ts">
  import { Button, Col, Row } from 'ant-design-vue';
  import { computed, onMounted } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form';
  import { CollapseContainer } from '/@/components/Container';
  import { CropperAvatar } from '/@/components/Cropper';
  import { useMessage } from '/@/hooks/web/useMessage';
  import headerImg from '/@/assets/images/header.jpg';
  import { baseSettingSchemas } from './data';
  import { useUserStore } from '/@/store/modules/user';
  import { editAvatarApi, editUserProfileApi, getUserProfileApi } from '@/api/sys/user.api';

  const ARow = Row;
  const ACol = Col;

  const { createMessage } = useMessage();
  const userStore = useUserStore();

  const [register, { setFieldsValue, validate }] = useForm({
    labelWidth: 120,
    schemas: baseSettingSchemas,
    showActionButtonGroup: false,
  });

  onMounted(async () => {
    const data = await getUserProfileApi();
    setFieldsValue(data);
  });

  const avatar = computed(() => {
    const { avatar } = userStore.getUserInfo;
    return avatar || headerImg;
  });

  /** 更换头像 */
  function updateAvatar({ data }) {
    const userinfo = userStore.getUserInfo;
    userinfo.avatar = data;
    userStore.setUserInfo(userinfo);
  }

  /** 提交按钮 */
  async function handleSubmit() {
    try {
      const values = await validate();
      await editUserProfileApi(values).then(() => {
        const userinfo = userStore.getUserInfo;
        userinfo.nickName = values.nickName;
        userinfo.sex = values.sex;
        userinfo.profile = values.profile;
        userStore.setUserInfo(userinfo);
        createMessage.success('更新成功！');
      });
    } finally {
      /* empty */
    }
  }
</script>

<style lang="less" scoped>
  .change-avatar {
    img {
      display: block;
      margin-bottom: 15px;
      border-radius: 50%;
    }
  }
</style>
