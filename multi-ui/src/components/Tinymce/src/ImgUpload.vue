<template>
  <div :class="[prefixCls, { fullscreen }]">
    <Upload
      name="file"
      multiple
      @change="handleChange"
      :customRequest="customRequest"
      :showUploadList="false"
      accept=".jpg,.jpeg,.gif,.png,.webp"
    >
      <a-button type="primary" v-bind="{ ...getButtonProps }">
        {{ t('component.upload.imgUpload') }}
      </a-button>
    </Upload>
  </div>
</template>

<script lang="ts" setup>
  import { computed } from 'vue';
  import { Upload } from 'ant-design-vue';
  import { useDesign } from '@/hooks/web/useDesign';
  import { useI18n } from '@/hooks/web/useI18n';
  import { fileUploadApi } from '@/api/sys/upload.api';
  import { AxiosProgressEvent } from 'axios';
  import { UploadRequestOption } from 'ant-design-vue/lib/vc-upload/interface';

  defineOptions({ name: 'TinymceImageUpload' });

  const props = defineProps({
    fullscreen: {
      type: Boolean,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  });

  const emit = defineEmits(['uploading', 'done', 'error']);

  let uploading = false;

  const { t } = useI18n();
  const { prefixCls } = useDesign('tinymce-img-upload');

  const getButtonProps = computed(() => {
    const { disabled } = props;
    return {
      disabled,
    };
  });

  function handleChange(info: Record<string, any>) {
    const file = info.file;
    const status = file?.status;
    const url = file?.response?.url;
    const name = file?.name;

    if (status === 'uploading') {
      if (!uploading) {
        emit('uploading', name);
        uploading = true;
      }
    } else if (status === 'done') {
      emit('done', name, url);
      uploading = false;
    } else if (status === 'error') {
      emit('error');
      uploading = false;
    }
  }

  async function customRequest(option: UploadRequestOption) {
    const { file, onSuccess, onError } = option;
    const newFile = file as unknown as any;
    try {
      await fileUploadApi(
        {
          file: newFile,
        },
        function onUploadProgress(progressEvent: AxiosProgressEvent) {
          newFile.percent = ((progressEvent.loaded / (progressEvent?.total || 1)) * 100) | 0;
        },
      ).then((res) => {
        onSuccess?.(res?.data);
      });
    } catch (e) {
      onError?.(e);
    }
  }
</script>

<style lang="less" scoped>
  @prefix-cls: ~'@{namespace}-tinymce-img-upload';

  .@{prefix-cls} {
    position: absolute;
    z-index: 20;
    top: 4px;
    right: 10px;

    &.fullscreen {
      position: fixed;
      z-index: 10000;
    }
  }
</style>
