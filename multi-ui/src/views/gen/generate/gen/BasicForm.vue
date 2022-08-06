<template>
  <BasicForm @register="registerForm" />
</template>

<script lang="ts">
  import { defineComponent, reactive } from 'vue';
  import { basicFormSchema, genList } from './gen.detail.data';
  import { BasicForm, useForm } from '/@/components/Form';
  import { GenTableIM } from '/@/model/gen';
  import { sourceCopy } from '/@/utils/xueyi';

  export default defineComponent({
    name: 'BasicFrom',
    components: { BasicForm },
    emits: ['submit'],
    setup(_, { emit }) {
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
          return true;
        } catch {
          emit('submit', genList[0].key);
        }
      }

      return { registerForm, initialize, submit };
    },
  });
</script>
