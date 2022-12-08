import { dicDictList } from '/@/api/sys/dict';
import { optionModuleApi } from '/@/api/system/authority/module';
import { getMenuRouteListApi } from '/@/api/system/authority/menu';
import { optionDictTypeApi } from '/@/api/tenant/dict/dictType';
import { FormSchema } from '/@/components/Form';
import {
  GenerationModeEnum,
  GenStatusEnum,
  IsTickEnum,
  SourceModeEnum,
  TemplateTypeEnum,
} from '/@/enums/gen';
import { MenuTypeEnum } from '/@/enums/system';
import { BasicColumn } from '/@/components/Table';
import { GenTableColumnIM, GenTableColumnLM, GenTableLM } from '/@/model/gen';
import { h } from 'vue';
import { Input, Select, Switch } from 'ant-design-vue';

/** 字典查询 */
export const dictMap = await dicDictList([
  'gen_template_type',
  'gen_java_type',
  'gen_query_type',
  'gen_display_type',
  'gen_generation_mode',
  'sys_yes_no',
  'gen_source_mode',
]);

/** 字典 */
export const dict: any = {
  templateTypeOption: dictMap['gen_template_type'],
  javaTypeOption: dictMap['gen_java_type'],
  queryTypeOption: dictMap['gen_query_type'],
  displayTypeOption: dictMap['gen_display_type'],
  generationMode: dictMap['gen_generation_mode'],
  genStatus: dictMap['sys_yes_no'],
  sourceMode: dictMap['gen_source_mode'],
  dictTypeOption: await optionDictTypeApi().then((res) => {
    return res.items.map((item) => {
      return { label: item.name, value: item.code };
    });
  }),
};

/** options 构造方法 */
export function getOptions(list: GenTableLM | GenTableColumnLM) {
  return list.map((item) => {
    return { value: item.id, label: item.name + ':' + item.comment };
  });
}

/** 关联表类型校验 */
export function isMergeTpl(tplType: TemplateTypeEnum) {
  return tplType === TemplateTypeEnum.MERGE;
}

/** 单表类型校验 */
export function isBaseTpl(tplType: TemplateTypeEnum) {
  return tplType === TemplateTypeEnum.BASE;
}

/** 树型表类型校验 */
export function isTreeTpl(tplType: TemplateTypeEnum) {
  return tplType === TemplateTypeEnum.TREE;
}

/** 分页数据 */
export const genList = [
  {
    key: 'basic',
    name: '基础配置',
    component: 'BasicForm',
    ref: 'basicRef',
  },
  {
    key: 'generate',
    name: '生成配置',
    component: 'GenerateForm',
    ref: 'generateRef',
  },
  {
    key: 'field',
    name: '字段配置',
    component: 'FieldForm',
    ref: 'fieldRef',
  },
];

/** 基础配置数据 */
export const basicFormSchema: FormSchema[] = [
  {
    label: '表名称',
    field: 'name',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '表描述',
    field: 'comment',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '实体类名称',
    field: 'className',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '作者',
    field: 'functionAuthor',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
    colProps: { span: 24 },
  },
];

/** 字段配置数据 */
export const fieldColumns: BasicColumn[] = [
  {
    title: '字段列名',
    dataIndex: 'name',
    fixed: 'left',
    width: 180,
  },
  {
    title: '字段描述',
    dataIndex: 'comment',
    customRender: ({ record }) => {
      const data = record as GenTableColumnIM;
      return h(Input, {
        value: data.comment,
        onChange(e) {
          data.comment = e.target.value as string;
        },
      });
    },
    fixed: 'left',
    width: 180,
  },
  {
    title: '物理类型',
    dataIndex: 'type',
    width: 180,
  },
  {
    title: 'Java类型',
    dataIndex: 'javaType',
    customRender: ({ record }) => {
      const data = record as GenTableColumnIM;
      return h(Select, {
        value: data.javaType,
        style: { width: '100px' },
        showSearch: true,
        optionFilterProp: 'label',
        options: dict.javaTypeOption,
        onChange(e) {
          data.javaType = e as string;
        },
      });
    },
    width: 180,
  },
  {
    title: 'Java属性名',
    dataIndex: 'javaField',
    customRender: ({ record }) => {
      const data = record as GenTableColumnIM;
      return h(Input, {
        value: data.javaField,
        onChange(e) {
          data.javaField = e.target.value as string;
        },
      });
    },
    width: 180,
  },
  {
    title: '隐藏',
    dataIndex: 'isHide',
    customRender: ({ record }) => {
      const data = record as GenTableColumnIM;
      return h(Switch, {
        checked: data.isHide,
        checkedChildren: IsTickEnum.YES,
        unCheckedChildren: IsTickEnum.NO,
        onChange(checked: boolean) {
          data.isHide = checked;
          if (checked) {
            data.isInsert = !checked;
            data.isEdit = !checked;
            data.isView = !checked;
            data.isImport = !checked;
            data.isExport = !checked;
            data.isRequired = !checked;
            data.isList = !checked;
            data.isQuery = !checked;
          }
        },
      });
    },
    width: 60,
  },
  {
    title: '新增',
    dataIndex: 'isInsert',
    customRender: ({ record }) => {
      const data = record as GenTableColumnIM;
      return h(Switch, {
        checked: data.isInsert,
        checkedChildren: IsTickEnum.YES,
        unCheckedChildren: IsTickEnum.NO,
        onChange(checked: boolean) {
          data.isInsert = checked;
        },
      });
    },
    width: 60,
  },
  {
    title: '编辑',
    dataIndex: 'isEdit',
    customRender: ({ record }) => {
      const data = record as GenTableColumnIM;
      return h(Switch, {
        checked: data.isEdit,
        checkedChildren: IsTickEnum.YES,
        unCheckedChildren: IsTickEnum.NO,
        onChange(checked: boolean) {
          data.isEdit = checked;
        },
      });
    },
    width: 60,
  },
  {
    title: '查看',
    dataIndex: 'isView',
    customRender: ({ record }) => {
      const data = record as GenTableColumnIM;
      return h(Switch, {
        checked: data.isView,
        checkedChildren: IsTickEnum.YES,
        unCheckedChildren: IsTickEnum.NO,
        onChange(checked: boolean) {
          data.isView = checked;
        },
      });
    },
    width: 60,
  },
  {
    title: '导入',
    dataIndex: 'isImport',
    customRender: ({ record }) => {
      const data = record as GenTableColumnIM;
      return h(Switch, {
        checked: data.isImport,
        checkedChildren: IsTickEnum.YES,
        unCheckedChildren: IsTickEnum.NO,
        onChange(checked: boolean) {
          data.isImport = checked;
        },
      });
    },
    width: 60,
  },
  {
    title: '导出',
    dataIndex: 'isExport',
    customRender: ({ record }) => {
      const data = record as GenTableColumnIM;
      return h(Switch, {
        checked: data.isExport,
        checkedChildren: IsTickEnum.YES,
        unCheckedChildren: IsTickEnum.NO,
        onChange(checked: boolean) {
          data.isExport = checked;
        },
      });
    },
    width: 60,
  },
  {
    title: '唯一',
    dataIndex: 'isUnique',
    customRender: ({ record }) => {
      const data = record as GenTableColumnIM;
      return h(Switch, {
        checked: data.isUnique,
        checkedChildren: IsTickEnum.YES,
        unCheckedChildren: IsTickEnum.NO,
        onChange(checked: boolean) {
          data.isUnique = checked;
        },
      });
    },
    width: 60,
  },
  {
    title: '必填',
    dataIndex: 'isRequired',
    customRender: ({ record }) => {
      const data = record as GenTableColumnIM;
      return h(Switch, {
        checked: data.isRequired,
        checkedChildren: IsTickEnum.YES,
        unCheckedChildren: IsTickEnum.NO,
        onChange(checked: boolean) {
          data.isRequired = checked;
        },
      });
    },
    width: 60,
  },
  {
    title: '列表',
    dataIndex: 'isList',
    customRender: ({ record }) => {
      const data = record as GenTableColumnIM;
      return h(Switch, {
        checked: data.isList,
        checkedChildren: IsTickEnum.YES,
        unCheckedChildren: IsTickEnum.NO,
        onChange(checked: boolean) {
          data.isList = checked;
        },
      });
    },
    width: 60,
  },
  {
    title: '查询',
    dataIndex: 'isQuery',
    customRender: ({ record }) => {
      const data = record as GenTableColumnIM;
      return h(Switch, {
        checked: data.isQuery,
        checkedChildren: IsTickEnum.YES,
        unCheckedChildren: IsTickEnum.NO,
        onChange(checked: boolean) {
          data.isQuery = checked;
        },
      });
    },
    width: 60,
  },
  {
    title: '查询方式',
    dataIndex: 'queryType',
    customRender: ({ record }) => {
      const data = record as GenTableColumnIM;
      return h(Select, {
        value: data.queryType,
        style: { width: '140px' },
        showSearch: true,
        optionFilterProp: 'label',
        options: dict.queryTypeOption,
        onChange(e) {
          data.queryType = e as string;
        },
      });
    },
    width: 180,
  },
  {
    title: '显示类型',
    dataIndex: 'htmlType',
    customRender: ({ record }) => {
      const data = record as GenTableColumnIM;
      return h(Select, {
        value: data.htmlType,
        style: { width: '160px' },
        showSearch: true,
        optionFilterProp: 'label',
        options: dict.displayTypeOption,
        onChange(e) {
          data.htmlType = e as string;
        },
      });
    },
    width: 180,
  },
  {
    title: '字段类型',
    dataIndex: 'dictType',
    customRender: ({ record }) => {
      const data = record as GenTableColumnIM;
      return h(Select, {
        value: data.dictType,
        style: { width: '160px' },
        showSearch: true,
        optionFilterProp: 'label',
        options: dict.dictTypeOption,
        onChange(e) {
          data.dictType = e as string;
        },
      });
    },
    width: 220,
  },
];

/** 生成基础配置数据 */
export const generateFormSchema: FormSchema[] = [
  {
    label: '生成模板',
    field: 'tplCategory',
    component: 'Select',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '生成包路径',
    field: 'packageName',
    component: 'Input',
    required: true,
    helpMessage: ['生成在哪个java包下，例如 com.xueyi.system'],
    colProps: { span: 12 },
  },
  {
    label: '生成模块名',
    field: 'moduleName',
    component: 'Input',
    required: true,
    helpMessage: ['可理解为子系统名，例如 system'],
    colProps: { span: 12 },
  },
  {
    label: '生成权限名',
    field: 'authorityName',
    component: 'Input',
    required: true,
    helpMessage: [
      '权限标识，例如 organize',
      '最终生成的功能权限标识为： 生成权限名:生成业务名:功能名，如：organize:module:add',
    ],
    colProps: { span: 12 },
  },
  {
    label: '生成业务名',
    field: 'businessName',
    component: 'Input',
    required: true,
    helpMessage: [
      '可理解为功能英文名，例如 module',
      '最终业务生成的前端为： 生成模块名.生成权限名.生成业务名，如：system/organize/module',
    ],
    colProps: { span: 12 },
  },
  {
    label: '生成功能名',
    field: 'functionName',
    component: 'Input',
    required: true,
    helpMessage: ['用作类描述，例如 用户'],
    colProps: { span: 12 },
  },
  {
    label: '生成代码方式',
    field: 'genType',
    component: 'RadioGroup',
    defaultValue: GenerationModeEnum.ZIP,
    componentProps: {
      options: dict.generationMode,
      resultField: 'items',
      labelField: 'name',
      valueField: 'id',
    },
    helpMessage: ['默认为zip压缩包下载，也可以自定义生成路径'],
    colProps: { span: 12 },
  },
  {
    label: '后端生成路径',
    field: 'genPath',
    component: 'Input',
    ifShow: ({ values }) => values.genType == GenerationModeEnum.CUSTOM,
    colProps: { span: 12 },
  },
  {
    label: '前端生成路径',
    field: 'uiPath',
    component: 'Input',
    ifShow: ({ values }) => values.genType == GenerationModeEnum.CUSTOM,
    colProps: { span: 12 },
  },
];

/** 生成参数配置数据 */
export const generateBasicSchema: FormSchema[] = [
  {
    label: '多租户模式',
    field: 'isTenant',
    component: 'RadioButtonGroup',
    defaultValue: GenStatusEnum.FALSE,
    componentProps: {
      options: dict.genStatus,
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '源策略模式',
    field: 'sourceMode',
    component: 'RadioButtonGroup',
    defaultValue: SourceModeEnum.ISOLATE,
    componentProps: {
      options: dict.sourceMode,
    },
    required: true,
    colProps: { span: 12 },
  },
];

/** 生成主表配置数据 */
export const generateBaseSchema: FormSchema[] = [
  {
    label: '归属模块',
    field: 'parentModuleId',
    component: 'ApiSelect',
    componentProps: ({ formModel, formActionType }) => {
      return {
        api: optionModuleApi,
        showSearch: true,
        optionFilterProp: 'label',
        resultField: 'items',
        labelField: 'name',
        valueField: 'id',
        onChange: async (e: any) => {
          const treeData = e === undefined ? [] : await getMenuRouteListApi(e, MenuTypeEnum.DIR);
          formModel.parentMenuId = undefined;
          const { updateSchema } = formActionType;
          updateSchema({
            field: 'parentMenuId',
            componentProps: { treeData },
          });
        },
      };
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '上级菜单',
    field: 'parentMenuId',
    component: 'TreeSelect',
    componentProps: {
      showSearch: true,
      treeNodeFilterProp: 'title',
      fieldNames: {
        label: 'title',
        key: 'id',
        value: 'id',
      },
      getPopupContainer: () => document.body,
    },
    required: true,
    colProps: { span: 12 },
  },
];

/** 生成树表配置数据 */
export const generateTreeSchema: FormSchema[] = [
  {
    label: '树编码字段',
    field: 'treeCode',
    component: 'Select',
    componentProps: {
      showSearch: true,
      optionFilterProp: 'label',
    },
    helpMessage: ['树显示的编码字段名， 默认设置为Id主键'],
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '树父编码字段',
    field: 'parentId',
    component: 'Select',
    componentProps: {
      showSearch: true,
      optionFilterProp: 'label',
    },
    helpMessage: ['树的父编码字段名， 默认为parentId'],
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '树名称字段',
    field: 'treeName',
    component: 'Select',
    componentProps: {
      showSearch: true,
      optionFilterProp: 'label',
    },
    helpMessage: ['树显示的编码字段名， 默认为name'],
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '祖籍列表字段',
    field: 'ancestors',
    component: 'Select',
    dynamicDisabled: true,
    componentProps: {
      optionFilterProp: 'label',
    },
    helpMessage: ['树的祖籍列表字段名， 默认为ancestors'],
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '层级字段',
    field: 'level',
    component: 'Select',
    dynamicDisabled: true,
    componentProps: {
      optionFilterProp: 'label',
    },
    helpMessage: ['树的层级字段名， 默认为level'],
    required: true,
    colProps: { span: 12 },
  },
];

/** 生成接口配置数据 */
export const generateApiSchema: FormSchema[] = [
  {
    label: '列表查询',
    field: 'apiList',
    component: 'RadioButtonGroup',
    defaultValue: GenStatusEnum.FALSE,
    componentProps: {
      options: dict.genStatus,
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '详情查询',
    field: 'apiGetInfo',
    component: 'RadioButtonGroup',
    defaultValue: GenStatusEnum.FALSE,
    componentProps: {
      options: dict.genStatus,
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '新增',
    field: 'apiAdd',
    component: 'RadioButtonGroup',
    defaultValue: GenStatusEnum.FALSE,
    componentProps: {
      options: dict.genStatus,
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '修改',
    field: 'apiEdit',
    component: 'RadioButtonGroup',
    defaultValue: GenStatusEnum.FALSE,
    componentProps: {
      options: dict.genStatus,
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '修改状态',
    field: 'apiEditStatus',
    component: 'RadioButtonGroup',
    defaultValue: GenStatusEnum.FALSE,
    componentProps: {
      options: dict.genStatus,
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '批量删除',
    field: 'apiBatchRemove',
    component: 'RadioButtonGroup',
    defaultValue: GenStatusEnum.FALSE,
    componentProps: {
      options: dict.genStatus,
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '导入',
    field: 'apiImport',
    component: 'RadioButtonGroup',
    defaultValue: GenStatusEnum.FALSE,
    componentProps: {
      options: dict.genStatus,
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '导出',
    field: 'apiExport',
    component: 'RadioButtonGroup',
    defaultValue: GenStatusEnum.FALSE,
    componentProps: {
      options: dict.genStatus,
    },
    required: true,
    colProps: { span: 12 },
  },
];
