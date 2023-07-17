<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <DeptTree class="w-1/4 xl:w-1/5" @select="handleSelect" />
    <BasicTable @register="registerTable" class="w-3/4 xl:w-4/5" :searchInfo="searchInfo">
      <template #toolbar>
        <a-button
          :preIcon="IconEnum.ADD"
          v-auth="PostAuth.ADD"
          @click="handleCreate"
          type="primary"
        >
          新增
        </a-button>
        <a-button
          :preIcon="IconEnum.DELETE"
          v-auth="PostAuth.DELETE"
          @click="handleDelete"
          type="primary"
          color="error"
        >
          删除
        </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: IconEnum.VIEW,
              tooltip: '查看',
              auth: PostAuth.SINGLE,
              onClick: handleView.bind(null, record),
            },
            {
              icon: IconEnum.EDIT,
              tooltip: '编辑',
              auth: PostAuth.EDIT,
              onClick: handleEdit.bind(null, record),
            },
            {
              icon: IconEnum.AUTH,
              tooltip: '分配角色',
              auth: PostAuth.AUTH,
              onClick: handleRole.bind(null, record),
            },
            {
              icon: IconEnum.DELETE,
              tooltip: '删除',
              auth: PostAuth.DELETE,
              color: 'error',
              onClick: handleDelete.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>
    <PostModal @register="registerModal" @success="handleSuccess" />
    <PostRoleModal @register="registerRoleModal" @success="handleSuccess" />
  </PageWrapper>
</template>

<script lang="ts">
  import { defineComponent, reactive } from 'vue';
  import { delPostApi, listPostApi } from '@/api/system/organize/post.api';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { IconEnum } from '@/enums';
  import { BasicTable, TableAction, useTable } from '/@/components/Table';
  import { PostAuth } from '/@/auth/system';
  import { columns, searchFormSchema } from './post.data';
  import { PostDetailGo } from '@/enums/system';
  import PostModal from './PostModal.vue';
  import PostRoleModal from './PostRoleModal.vue';
  import { useUserStore } from '/@/store/modules/user';
  import PageWrapper from '/@/components/Page/src/PageWrapper.vue';
  import DeptTree from './DeptTree.vue';

  export default defineComponent({
    name: 'PostManagement',
    components: {
      DeptTree,
      PageWrapper,
      BasicTable,
      PostModal,
      PostRoleModal,
      TableAction,
    },
    setup() {
      const searchInfo = reactive<Recordable>({});
      const { createMessage, createConfirm } = useMessage();
      const [registerModal, { openModal }] = useModal();
      const [registerRoleModal, { openModal: openRoleModal }] = useModal();
      const state = reactive<{
        ids: string[];
        idNames: string;
      }>({
        ids: [],
        idNames: '',
      });
      const [registerTable, { reload }] = useTable({
        title: '岗位列表',
        api: listPostApi,
        striped: false,
        useSearchForm: true,
        rowKey: 'id',
        bordered: true,
        showIndexColumn: true,
        columns,
        formConfig: {
          labelWidth: 120,
          schemas: searchFormSchema,
        },
        showTableSetting: true,
        tableSetting: {
          fullScreen: true,
        },
        actionColumn: {
          width: 220,
          title: '操作',
          dataIndex: 'action',
          slots: { customRender: 'action' },
        },
        rowSelection: {
          onChange: (selectedRowKeys, selectRows) => {
            state.ids = selectedRowKeys;
            state.idNames = selectRows
              .map((item) => {
                return item.name;
              })
              .join(',');
          },
        },
      });

      /** 查看按钮 */
      function handleView(record: Recordable) {
        useUserStore().getRoutePath(PostDetailGo, record.id);
      }

      /** 新增按钮 */
      function handleCreate() {
        openModal(true, {
          isUpdate: false,
        });
      }

      /** 修改按钮 */
      function handleEdit(record: Recordable) {
        openModal(true, {
          record,
          isUpdate: true,
        });
      }

      /** 角色分配按钮 */
      function handleRole(record: Recordable) {
        openRoleModal(true, { record });
      }

      /** 删除按钮 */
      function handleDelete(record: Recordable) {
        const delIds = record.id || state.ids;
        const delNames = record.name || state.idNames;
        if (!record.id && state.ids.length === 0) {
          createMessage.warning('请选择要操作的数据！');
        } else {
          createConfirm({
            iconType: 'warning',
            title: '提示',
            content: '是否确定要删除' + delNames + '?',
            onOk: () =>
              delPostApi(delIds).then(() => {
                createMessage.success('删除' + delNames + '成功！');
                reload();
              }),
          });
        }
      }

      /** 部门树选择 */
      function handleSelect(deptId = '') {
        searchInfo.deptId = deptId;
        reload();
      }

      function handleSuccess() {
        reload();
      }

      return {
        IconEnum,
        PostAuth,
        searchInfo,
        registerTable,
        registerModal,
        registerRoleModal,
        handleView,
        handleCreate,
        handleEdit,
        handleRole,
        handleDelete,
        handleSuccess,
        handleSelect,
      };
    },
  });
</script>
