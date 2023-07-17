<template>
  <BasicForm @register="registerForm" />
</template>

<script setup lang="ts">
  import { reactive } from 'vue';
  import { basicFormSchema, genList } from './gen.detail.data';
  import { BasicForm, useForm } from '/@/components/Form';
  import { GenTableIM } from '@/model/gen';
  import { sourceCopy } from '/@/utils/xueyi';

  const emit = defineEmits(['submit']);
  defineExpose({ initialize, submit });

  const state = reactive<{
    info: Nullable<GenTableIM>;
  }>({
    info: null,
  });

  const [registerForm, { setFieldsValue, validate }] = useForm({
    labelWidth: 100,
    schemas: basicFormSchema,
    showActionButtonGroup: false,
  });

  /** 数据初始化 */
  function initialize(info: GenTableIM) {
    state.info = info;
    setFieldsValue({ ...state.info });
  }

  /** 保存校验 */
  async function submit() {
    try {
      sourceCopy(state.info, await validate());
      return state.info;
    } catch {
      emit('submit', genList[0].key);
    }
  }
</script>
