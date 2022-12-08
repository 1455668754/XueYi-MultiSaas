<template>
  <CollapseContainer title="基础配置">
    <BasicForm @register="register" />
  </CollapseContainer>

  <CollapseContainer title="参数配置">
    <BasicForm @register="basicRegister" />
  </CollapseContainer>

  <CollapseContainer title="主表配置" v-show="!isMergeTpl(tplType)">
    <BasicForm @register="baseRegister" />
  </CollapseContainer>

  <CollapseContainer title="树表配置" v-show="isTreeTpl(tplType)">
    <BasicForm @register="treeRegister" />
  </CollapseContainer>

  <CollapseContainer title="接口配置" v-show="!isMergeTpl(tplType)">
    <BasicForm @register="apiRegister" />
  </CollapseContainer>
</template>

<script lang="ts">
  import { CollapseContainer } from '/@/components/Container';
  import { BasicForm, useForm } from '/@/components/Form';
  import { TemplateTypeEnum } from '/@/enums/gen';
  import { GenTableIM, OptionIM } from '/@/model/gen';
  import { defineComponent, reactive, ref } from 'vue';
  import {
    dict,
    generateApiSchema,
    generateBaseSchema,
    generateBasicSchema,
    generateFormSchema,
    generateTreeSchema,
    genList,
    getOptions,
    isMergeTpl,
    isTreeTpl,
  } from './gen.detail.data';
  import { getMenuRouteListApi } from '/@/api/system/authority/menu';
  import { MenuTypeEnum } from '/@/enums/system';
  import { sourceAssign } from '/@/utils/xueyi';

  export default defineComponent({
    name: 'GenerateFrom',
    components: { BasicForm, CollapseContainer },
    emits: ['submit'],
    setup(_, { emit }) {
      const state = reactive<{
        info: Nullable<GenTableIM>;
      }>({
        info: null,
      });

      const tplType = ref<TemplateTypeEnum>(TemplateTypeEnum.BASE);

      const [register, { setFieldsValue, validate, updateSchema }] = useForm({
        labelWidth: 160,
        schemas: generateFormSchema,
        showActionButtonGroup: false,
      });

      const [basicRegister, { setFieldsValue: basicSetFieldsValue, validate: basicValidate }] =
        useForm({
          labelWidth: 160,
          schemas: generateBasicSchema,
          showActionButtonGroup: false,
        });

      const [
        baseRegister,
        {
          setFieldsValue: baseSetFieldsValue,
          validate: baseValidate,
          updateSchema: baseUpdateSchema,
        },
      ] = useForm({
        labelWidth: 160,
        schemas: generateBaseSchema,
        showActionButtonGroup: false,
      });

      const [
        treeRegister,
        {
          setFieldsValue: treeSetFieldsValue,
          validate: treeValidate,
          updateSchema: treeUpdateSchema,
        },
      ] = useForm({
        labelWidth: 160,
        schemas: generateTreeSchema,
        showActionButtonGroup: false,
      });

      const [apiRegister, { setFieldsValue: apiSetFieldsValue, validate: apiValidate }] = useForm({
        labelWidth: 160,
        schemas: generateApiSchema,
        showActionButtonGroup: false,
      });

      /** 数据初始化 */
      function initialize(info: GenTableIM) {
        state.info = info;
        tplType.value = state.info.tplCategory as TemplateTypeEnum;
        initBasic();
        const dataList = state.info.subList === undefined ? [] : getOptions(state.info.subList);
        const option = JSON.parse(state.info?.options) as OptionIM;
        initBase(option);
        initTree(dataList);
        basicSetFieldsValue({ ...option });
        treeSetFieldsValue({ ...option });
        baseSetFieldsValue({ ...option });
        apiSetFieldsValue({ ...option });
      }

      /** 基础配置初始化 */
      function initBasic() {
        setFieldsValue({ ...state.info });
        updateSchema({
          field: 'tplCategory',
          componentProps: () => {
            return {
              options: dict.templateTypeOption,
              onChange: (e: any) => {
                tplType.value = e;
              },
            };
          },
        });
      }

      /** 单表配置初始化 */
      async function initBase(options: OptionIM) {
        const parentMenuIdOptions =
          options?.parentModuleId === undefined
            ? []
            : await getMenuRouteListApi(options?.parentModuleId, MenuTypeEnum.DIR);
        baseUpdateSchema([
          { field: 'parentMenuId', componentProps: { treeData: parentMenuIdOptions } },
        ]);
      }

      /** 树表配置初始化 */
      function initTree(subList: any[]) {
        treeUpdateSchema({ field: 'treeCode', componentProps: { options: subList } });
        treeUpdateSchema({ field: 'parentId', componentProps: { options: subList } });
        treeUpdateSchema({ field: 'treeName', componentProps: { options: subList } });
        treeUpdateSchema({ field: 'ancestors', componentProps: { options: subList } });
        treeUpdateSchema({ field: 'level', componentProps: { options: subList } });
      }

      /** 保存校验 */
      async function submit() {
        try {
          sourceAssign(state.info, await validate());
          let options = {};
          sourceAssign(options, await basicValidate());
          if (isTreeTpl(tplType.value)) {
            sourceAssign(options, await treeValidate());
          }
          if (!isMergeTpl(tplType.value)) {
            sourceAssign(options, await baseValidate());
          }
          sourceAssign(options, await apiValidate());
          sourceAssign(state.info, { options: JSON.stringify(options) });
        } catch {
          emit('submit', genList[1].key);
        }
      }

      return {
        register,
        basicRegister,
        baseRegister,
        treeRegister,
        apiRegister,
        initialize,
        submit,
        tplType,
        TemplateTypeEnum,
        isMergeTpl,
        isTreeTpl,
      };
    },
  });
</script>
