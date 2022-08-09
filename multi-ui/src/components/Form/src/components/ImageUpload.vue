<template>
  <div class="clearfix">
    <a-upload
      v-model:file-list="fileList"
      v-model:value="state"
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

<script lang="ts">
  import { PlusOutlined } from '@ant-design/icons-vue';
  import { defineComponent, PropType, ref, unref, watch } from 'vue';
  import { message, Modal, Upload, UploadProps } from 'ant-design-vue';
  import { UploadFile } from 'ant-design-vue/lib/upload/interface';
  import { useRuleFormItem } from '/@/hooks/component/useFormItem';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { buildShortUUID } from '/@/utils/uuid';

  export type ImageUploadType = 'text' | 'picture' | 'picture-card';
  export default defineComponent({
    name: 'ImageUpload',
    components: {
      PlusOutlined,
      AUpload: Upload,
      AModal: Modal,
    },
    props: {
      value: [Array, String],
      api: {
        type: Function as PropType<(file: UploadFile) => Promise<any>>,
        default: null,
      },
      listType: {
        type: String as PropType<ImageUploadType>,
        default: 'picture-card',
      },
      // 文件类型
      fileType: {
        type: Array,
        default: () => ['image/png', 'image/jpeg'],
      },
      multiple: { type: Boolean, default: false },
      // 最大数量的文件
      maxCount: { type: Number, default: 1 },
      // 最小数量的文件
      minCount: { type: Number, default: 0 },
      // 文件最大多少MB
      maxSize: { type: Number, default: 2 },
    },
    emits: ['update:value'],
    setup(props, { emit }) {
      const { t } = useI18n();
      const previewVisible = ref(false);
      const previewImage = ref('');
      const emitData = ref<string | string[] | undefined>(undefined);
      const fileList = ref<UploadFile[]>([]);
      const isFirstLoad = ref(true);

      // Embedded in the form, just use the hook binding to perform form verification
      const [state] = useRuleFormItem(props);

      watch(
        () => props.value,
        (count) => {
          console.error(count);
          if (isFirstLoad.value) {
            fileList.value = [];
            if (count !== undefined)
              if (count && Array.isArray(count)) {
                const uuid = buildShortUUID();
                for (let i = 0; i < count.length; i++) {
                  fileList.value.push({
                    uid: uuid,
                    name: uuid,
                    status: 'done',
                    url: count[i] as string,
                  });
                }
              } else {
                const uuid = buildShortUUID();
                fileList.value.push({
                  uid: uuid,
                  name: uuid,
                  status: 'done',
                  url: count as string,
                });
              }
            isFirstLoad.value = false;
          }
        },
      );

      watch(
        () => fileList.value,
        () => {
          emitData.value =
            props.maxCount === 1
              ? fileList.value.length > 0
                ? (fileList.value[0].url as string)
                : ''
              : fileList.value.length > 0
              ? (fileList.value.map((item) => {
                  return item.url;
                }) as string[])
              : [];
          emitChange();
        },
        { deep: true },
      );

      function emitChange() {
        emit('update:value', unref(emitData));
      }

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
          return false;
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
          for (let i = fileList.value.length - 1; i >= 0; i--) {
            if (fileList.value[i].uid === file.uid) {
              fileList.value.splice(i, 1);
              break;
            }
          }
        }
        if (isFirstLoad.value) isFirstLoad.value = false;
        return isPNG && isLt2M;
      };

      /** 自定义上传 */
      const handleCustomRequest = async (option: any) => {
        const { file } = option;
        const uid = file.uid;
        await props
          .api(option)
          .then((res) => {
            file.status = 'done';
            file.url = res.data.url;
            for (let i = fileList.value.length - 1; i >= 0; i--)
              if (fileList.value[i].uid === uid) {
                fileList.value[i] = file;
                break;
              }
          })
          .catch(() => {
            for (let i = fileList.value.length - 1; i >= 0; i--)
              if (fileList.value[i].uid === uid) {
                fileList.value.splice(i, 1);
                break;
              }
          });
      };

      return {
        t,
        state,
        previewVisible,
        previewImage,
        fileList,
        handleCancel,
        handlePreview,
        handleCustomRequest,
        handleBeforeUpload,
      };
    },
  });

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
