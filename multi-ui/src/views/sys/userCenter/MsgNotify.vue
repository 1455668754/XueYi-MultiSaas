<template>
  <CollapseContainer title="新消息通知" :canExpand="false">
    <List>
      <template v-for="item in msgNotifyList" :key="item.key">
        <ListItem>
          <ListItemMeta>
            <template #title>
              {{ item.title }}
              <Switch
                class="extra"
                checked-children="开"
                un-checked-children="关"
                default-checked
              />
            </template>
            <template #description>
              <div>{{ item.description }}</div>
            </template>
          </ListItemMeta>
        </ListItem>
      </template>
    </List>
  </CollapseContainer>
</template>

<script setup lang="ts">
  import { List, Switch } from 'ant-design-vue';
  import { CollapseContainer } from '@/components/Container';
  import { msgNotifyList } from './data';

  const ListItem = List.Item;
  const ListItemMeta = List.Item.Meta;
</script>

<style lang="less" scoped>
  .extra {
    float: right;
    margin-top: 10px;
    margin-right: 30px;
  }
</style>
