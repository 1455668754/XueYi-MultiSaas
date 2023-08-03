<template>
  <div class="clearfix">
    <a-upload
      v-model:file-list="fileList"
      :list-type="listType"
      :multiple="multiple"
      :max-count="maxCount"
      :customRequest="handleCustomRequest"
      :before-upload="handleBeforeUpload"
      @preview="handlePreview"
    >
      <div v-if="fileList.length < maxCount">
        <plus-outlined />
        <div style="margin-top: 8px">
          {{ t('component.upload.upload') }}
        </div>
      </div>
    </a-upload>
    <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
      <img alt="example" style="width: 100%" :src="previewImage" />
    </a-modal>
  </div>
</template>

<script setup lang="ts">
  import { PlusOutlined } from '@ant-design/icons-vue';
  import { PropType, reactive, ref, watch } from 'vue';
  import { message, Modal, Upload, UploadProps } from 'ant-design-vue';
  import { UploadFile } from 'ant-design-vue/lib/upload/interface';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { join } from 'lodash-es';
  import { buildShortUUID } from '@/utils/uuid';
  import { isArray, isNotEmpty, isUrl } from '@/utils/is';
  import { useRuleFormItem } from '@/hooks/component/useFormItem';
  import { fileUploadApi } from '@/api/sys/upload.api';

  type ImageUploadType = 'text' | 'picture' | 'picture-card';

  const emit = defineEmits(['update:value']);
  const props = defineProps({
    value: [Array, String],
    api: {
      type: Function as PropType<(file: UploadFile) => Promise<any>>,
      default: fileUploadApi,
    },
    listType: {
      type: String as PropType<ImageUploadType>,
      default: () => 'picture-card',
    },
    // 文件类型
    fileType: {
      type: Array,
      default: () => ['image/png', 'image/jpeg'],
    },
    multiple: {
      type: Boolean,
      default: () => false,
    },
    // 最大数量的文件
    maxCount: {
      type: Number,
      default: () => 1,
    },
    // 最小数量的文件
    minCount: {
      type: Number,
      default: () => 0,
    },
    // 文件最大多少MB
    maxSize: {
      type: Number,
      default: () => 2,
    },
  });

  // Embedded in the form, just use the hook binding to perform form verification
  const [state] = useRuleFormItem(props);

  const AUpload = Upload;
  const AModal = Modal;
  const { t } = useI18n();
  const previewVisible = ref(false);
  const previewImage = ref('');
  const fileList = ref<UploadFile[]>([]);

  const fileState = reactive<{
    newList: any[];
    newStr: string;
    oldStr: string;
    isLoad: boolean;
  }>({
    newList: [],
    newStr: '',
    oldStr: '',
    isLoad: false,
  });

  watch(
    () => fileList.value,
    (v) => {
      console.error(fileList.value);
      fileState.newList = v
        .filter((item) => {
          return item?.url && item.status === 'done' && isUrl(item?.url);
        })
        .map((item) => item?.url);
      fileState.newStr = join(fileState.newList);
      // 不相等代表数据变更
      if (fileState.newStr !== fileState.oldStr) {
        fileState.oldStr = fileState.newStr;
        if (!fileState.isLoad) {
          if (props.multiple) {
            state.value = fileState.newList;
          } else {
            state.value = fileState.newStr;
          }
        } else {
          fileState.isLoad = false;
        }
      }
    },
    {
      deep: true,
    },
  );

  watch(
    () => state.value,
    (v) => {
      if (!fileState.isLoad) {
        const stateStr = props.multiple ? join((v as any[]) || []) : v || '';
        if (stateStr !== fileState.oldStr) {
          fileState.isLoad = true;
          fileList.value = [];
          let list: string[] = [];
          if (props.multiple) {
            if (isNotEmpty(v)) {
              if (isArray(v)) {
                list = v as string[];
              } else {
                list.push(v as string);
              }
            }
          } else {
            if (isNotEmpty(v)) {
              list.push(v as string);
            }
          }
          fileList.value = list.map((item) => {
            const uuid = buildShortUUID();
            return {
              uid: uuid,
              name: uuid,
              status: 'done',
              url: item,
            };
          });
        } else {
          emit('update:value', v);
        }
      }
    },
    {
      immediate: true,
    },
  );

  /** 关闭查看 */
  const handleCancel = () => {
    previewVisible.value = false;
  };

  /** 查看图片 */
  // @ts-ignore
  const handlePreview = async (file: UploadProps['fileList'][number]) => {
    if (!file.url && !file.preview) {
      file.preview = (await getBase64(file.originFileObj)) as string;
    }
    previewImage.value = file.url || file.preview;
    previewVisible.value = true;
  };

  /** 上传前校验 */
  const handleBeforeUpload: UploadProps['beforeUpload'] = (file) => {
    if (fileList.value.length > props.maxCount) {
      fileList.value.splice(props.maxCount, fileList.value.length - props.maxCount);
      message.error(t('component.upload.maxNumber', [props.maxCount]));
      return Upload.LIST_IGNORE;
    }
    const isPNG = props.fileType.includes(file.type);
    if (!isPNG) {
      message.error(t('component.upload.acceptUpload', [props.fileType.toString()]));
    }
    const isLt2M = file.size / 1024 / 1024 < props.maxSize;
    if (!isLt2M) {
      message.error(t('component.upload.maxSizeMultiple', [props.maxSize]));
    }
    if (!(isPNG && isLt2M)) {
      fileList.value.pop();
    }
    return (isPNG && isLt2M) || Upload.LIST_IGNORE;
  };

  /** 自定义上传 */
  const handleCustomRequest = async (option: any) => {
    const { file } = option;
    await props
      .api(option)
      .then((res) => {
        file.url = res.data.url;
        file.status = 'done';
        fileList.value.pop();
        fileList.value.push(file);
      })
      .catch(() => {
        fileList.value.pop();
      });
  };

  function getBase64(file: File) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result);
      reader.onerror = (error) => reject(error);
    });
  }
</script>

<style scoped>
  /* you can make up upload button and sample style by using stylesheets */
  .ant-upload-select-picture-card i {
    font-size: 32px;
    color: #999;
  }

  .ant-upload-select-picture-card .ant-upload-text {
    margin-top: 8px;
    color: #666;
  }
</style>
