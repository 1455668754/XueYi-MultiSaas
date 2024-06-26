<template>
  <BasicModal
    width="800px"
    :title="t('component.upload.preview')"
    class="upload-preview-modal"
    v-bind="$attrs"
    @register="register"
    :showOkBtn="false"
  >
    <FileList :dataSource="fileListRef" :columns="columns" :actionColumn="actionColumn" />
  </BasicModal>
</template>

<script lang="ts" setup>
  import { ref, watch } from 'vue';
  import FileList from './FileList.vue';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { previewProps } from '../props';
  import { FileBasicColumn, PreviewFileItem } from '../types/typing';
  import { downloadByUrl } from '@/utils/file/download';
  import { createPreviewActionColumn, createPreviewColumns } from './data';
  import { useI18n } from '@/hooks/web/useI18n';
  import { isArray } from '@/utils/core/ObjectUtil';
  import { BasicColumn } from '@/components/Table';

  const props = defineProps(previewProps);

  const emit = defineEmits(['list-change', 'register', 'delete']);

  let columns: BasicColumn[] | FileBasicColumn[] = createPreviewColumns();
  let actionColumn: any;

  const [register] = useModalInner();
  const { t } = useI18n();

  const fileListRef = ref<PreviewFileItem[] | Array<any>>([]);
  watch(
    () => props.previewColumns,
    () => {
      if (props.previewColumns.length) {
        columns = props.previewColumns;
        actionColumn = null;
      } else {
        columns = createPreviewColumns();
        actionColumn = createPreviewActionColumn({ handleRemove, handleDownload });
      }
    },
    { immediate: true },
  );

  watch(
    () => props.value,
    (value) => {
      if (!isArray(value)) value = [];
      if (props.beforePreviewData) {
        value = props.beforePreviewData(value) as any;
        fileListRef.value = value;
        return;
      }
      fileListRef.value = value
        .filter((item) => !!item)
        .map((item) => {
          if (typeof item !== 'string') {
            console.error('return value should be string');
            return;
          }
          return {
            url: item,
            type: item.split('.').pop() || '',
            name: item.split('/').pop() || '',
          };
        });
    },
    { immediate: true },
  );

  // 删除
  function handleRemove(record: PreviewFileItem) {
    const index = fileListRef.value.findIndex((item) => item.url === record.url);
    if (index !== -1) {
      const removed = fileListRef.value.splice(index, 1);
      emit('delete', removed[0].url);
      emit(
        'list-change',
        fileListRef.value.map((item) => item.url),
      );
    }
  }

  // 下载
  function handleDownload(record: PreviewFileItem) {
    const { url = '' } = record;
    downloadByUrl({ url });
  }
</script>

<style lang="less">
  .upload-preview-modal {
    .ant-upload-list {
      display: none;
    }

    .ant-table-wrapper .ant-spin-nested-loading {
      padding: 0;
    }
  }
</style>
