<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="getTitle"
    :width="1000"
    :showCancelBtn="false"
    :showOkBtn="false"
  >
    <Description @register="register" class="mt-4" />
  </BasicModal>
</template>

<script lang="ts">
  import { computed, defineComponent } from 'vue';
  import { Description, useDescription } from '/@/components/Description';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { DescItemSizeEnum } from '/@/enums/appEnum';
  import { getJobLogApi } from '/@/api/system/system/jobLog';
  import { detailSchema } from './jobLog.data';

  export default defineComponent({
    name: 'JobLogModal',
    components: { BasicModal, Description },
    emits: ['register'],
    setup(_, {}) {
      const [register, { setDescProps }] = useDescription({
        schema: detailSchema,
        column: DescItemSizeEnum.DEFAULT,
      });

      const [registerModal, { setModalProps }] = useModalInner(async (data) => {
        setModalProps({ confirmLoading: false });
        const jobLog = await getJobLogApi(data.record.id);
        setDescProps({ data: jobLog });
      });

      /** 标题初始化 */
      const getTitle = computed(() => '调度日志详情');

      return { registerModal, register, getTitle };
    },
  });
</script>
