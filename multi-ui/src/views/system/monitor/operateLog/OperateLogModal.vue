<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="getTitle"
    :width="1000"
    :showCancelBtn="false"
    :showOkBtn="false"
  >
    <Description @register="register" class="mt-4"/>
  </BasicModal>
</template>

<script lang="ts">
import {computed, defineComponent} from 'vue';
import {detailSchema} from './operateLog.data';
import {Description, useDescription} from '/@/components/Description';
import {getOperateLogApi} from '@/api/system/monitor/operateLog.api';
import {BasicModal, useModalInner} from '/@/components/Modal';
import {DescItemSizeEnum} from '@/enums/basic';

export default defineComponent({
  name: 'OperateLogModal',
  components: {BasicModal, Description},
  emits: ['register'],
  setup(_, {}) {
    const [register, {setDescProps}] = useDescription({
      schema: detailSchema,
      column: DescItemSizeEnum.DEFAULT,
    });

    const [registerModal, {setModalProps}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false});
      const operateLog = await getOperateLogApi(data.record.id);
      setDescProps({data: operateLog});
    });

    /** 标题初始化 */
    const getTitle = computed(() => '操作日志详情');

    return {registerModal, register, getTitle};
  },
});
</script>
