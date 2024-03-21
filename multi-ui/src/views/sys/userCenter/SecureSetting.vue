<template>
  <div>
    <CollapseContainer title="安全设置" :canExpand="false">
      <List>
        <template v-for="item in secureSettingList" :key="item.key">
          <ListItem>
            <ListItemMeta>
              <template #title>
                {{ item.title }}
                <div class="extra" v-if="item.extra" @click="handleEdit(item)">
                  {{ item.extra }}
                </div>
              </template>
              <template #description>
                <div>
                  {{ getDescription(item) }}
                </div>
              </template>
            </ListItemMeta>
          </ListItem>
        </template>
      </List>
    </CollapseContainer>
    <SecureSettingModal @register="setRegisterModal" />
  </div>
</template>

<script setup lang="ts">
  import { List } from 'ant-design-vue';
  import { computed } from 'vue';
  import { CollapseContainer } from '@/components/Container';
  import { ListItemIM, SecureEnum, secureSettingList } from './data';
  import { useUserStore } from '@/store/modules/user';
  import { useModal } from '@/components/Modal';
  import SecureSettingModal from './SecureSettingModal.vue';
  import { isEmpty } from '@/utils/core/ObjectUtil';

  const ListItem = List.Item;
  const ListItemMeta = List.Item.Meta;

  const userStore = useUserStore();

  const [setRegisterModal, { openModal: setOpenModal }] = useModal();

  const userName = computed(() => {
    const { userName } = userStore.getUserInfo;
    return userName;
  });

  const phone = computed(() => {
    const { phone } = userStore.getUserInfo;
    return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2');
  });

  const email = computed(() => {
    const { email } = userStore.getUserInfo;
    return email;
  });

  /** 显示 */
  function getDescription(record: ListItemIM) {
    switch (record.code) {
      case SecureEnum.USER_NAME:
        return '当前账户账号：' + userName.value;
      case SecureEnum.PASSWORD:
        return '当前密码强度：强';
      case SecureEnum.PHONE:
        return isEmpty(phone.value) ? '未绑定' : '已绑定手机：' + phone.value;
      case SecureEnum.EMAIL:
        return isEmpty(email.value) ? '未绑定' : '已绑定邮箱：' + email.value;
      default:
        return '';
    }
  }

  /** 修改按钮 */
  function handleEdit(record: ListItemIM) {
    setOpenModal(true, {
      record,
    });
  }
</script>

<style lang="less" scoped>
  .extra {
    float: right;
    margin-top: 10px;
    margin-right: 30px;
    font-weight: normal;
    color: #1890ff;
    cursor: pointer;
  }
</style>
