import { dicDictList } from '@/api/sys/dict.api';
import { listModuleApi } from '@/api/system/authority/module.api';
import { getMenuRouteListApi } from '@/api/system/authority/menu.api';
import { listDictTypeApi } from '@/api/system/dict/dictType.api';
import { FormSchema } from '@/components/Form';
import {
  DicCodeGenEnum,
  GenModeEnum,
  GenStatusEnum,
  IsTickEnum,
  SourceModeEnum,
  TemplateTypeEnum,
} from '@/enums/gen/generate';
import { COMMON_MENU, MenuTypeEnum } from '@/enums/system/authority';
import { BasicColumn } from '@/components/Table';
import { GenTableColumnIM, GenTableColumnLM, GenTableLM } from '@/model/gen/generate';
import { h } from 'vue';
import { Input, Select, Switch } from 'ant-design-vue';
import { DicCodeEnum, DicStatusEnum, DicYesNoEnum } from '@/enums';
import { hasTreeNode } from '@/utils/core/treeUtil';
import { SelectValue } from 'ant-design-vue/es/select';

type CheckedType = boolean | string | number;

/** 字典查询 */
export const dictMap = await dicDictList([
  DicCodeGenEnum.GEN_TEMPLATE_TYPE,
  DicCodeGenEnum.GEN_JAVA_TYPE,
  DicCodeGenEnum.GEN_QUERY_TYPE,
  DicCodeGenEnum.GEN_DISPLAY_TYPE,
  DicCodeGenEnum.GEN_GENERATION_MODE,
  DicCodeEnum.SYS_YES_NO,
  DicCodeGenEnum.GEN_SOURCE_MODE,
]);

/** 字典 */
export const dict: any = {
  DicTemplateTypeOption: dictMap[DicCodeGenEnum.GEN_TEMPLATE_TYPE],
  DicJavaTypeOption: dictMap[DicCodeGenEnum.GEN_JAVA_TYPE],
  DicQueryTypeOption: dictMap[DicCodeGenEnum.GEN_QUERY_TYPE],
  DicDisplayTypeOption: dictMap[DicCodeGenEnum.GEN_DISPLAY_TYPE],
  DicGenerationMode: dictMap[DicCodeGenEnum.GEN_GENERATION_MODE],
  DicYesNoOptions: dictMap[DicCodeEnum.SYS_YES_NO],
  DicSourceMode: dictMap[DicCodeGenEnum.GEN_SOURCE_MODE],
  DicDictTypeOption: await listDictTypeApi({ status: DicStatusEnum.NORMAL }).then((res) => {
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
        options: dict.DicJavaTypeOption,
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
        onChange(checked: CheckedType) {
          data.isHide = checked as boolean;
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
        onChange(checked: CheckedType) {
          data.isInsert = checked as boolean;
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
        onChange(checked: CheckedType) {
          data.isEdit = checked as boolean;
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
        onChange(checked: CheckedType) {
          data.isView = checked as boolean;
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
        onChange(checked: CheckedType) {
          data.isImport = checked as boolean;
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
        onChange(checked: CheckedType) {
          data.isExport = checked as boolean;
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
        onChange(checked: CheckedType) {
          data.isUnique = checked as boolean;
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
        onChange(checked: CheckedType) {
          data.isRequired = checked as boolean;
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
        onChange(checked: CheckedType) {
          data.isList = checked as boolean;
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
        onChange(checked: CheckedType) {
          data.isQuery = checked as boolean;
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
        options: dict.DicQueryTypeOption,
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
        options: dict.DicDisplayTypeOption,
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
        options: dict.DicDictTypeOption,
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
    label: '生成后端包路径',
    field: 'rdPackageName',
    component: 'Input',
    required: true,
    helpMessage: ['生成在哪个java包下，例如 com.xueyi.system'],
    colProps: { span: 12 },
  },
  {
    label: '生成前端包路径',
    field: 'fePackageName',
    component: 'Input',
    required: true,
    helpMessage: ({ values }) => {
      const fePackageName = (values?.fePackageName || '').replace('.', '/');
      return [
        '生成在对应包哪个目录层级下，例如 system/organize',
        `以当前路径${values?.fePackageName}为例，最终生成路径为：`,
        `api：src/api/${fePackageName}/${values?.businessName}.api.ts`,
        `auth：src/api/${fePackageName}/${values?.businessName}.auth.ts`,
        `enums：src/api/${fePackageName}/${values?.businessName}.enum.ts`,
        `model：src/api/${fePackageName}/${values?.businessName}.model.ts`,
        `viewIndex：src/api/${fePackageName}/${values?.businessName}/index.ts`,
        `viewData：src/api/${fePackageName}/${values?.businessName}/data.ts`,
        `viewModal：src/api/${fePackageName}/${values?.businessName}/Modal.ts`,
        `viewDetail：src/api/${fePackageName}/${values?.businessName}/Detail.ts`,
      ];
    },
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
    label: '生成权限标识',
    field: 'authorityName',
    component: 'Input',
    required: true,
    helpMessage: ({ values }) => {
      return [
        '权限标识，例如 system:organize，最终权限标识为：FE/RD:权限标识:功能名:操作名',
        `以当前权限标识${values?.authorityName}为例，生成列表查询权限，权限标识为：`,
        `前端：FE:${values?.authorityName}:list`,
        `后端：RD:${values?.authorityName}:list`,
      ];
    },
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
    label: '生成路径',
    field: 'genType',
    component: 'RadioGroup',
    defaultValue: GenModeEnum.DEFAULT,
    componentProps: {
      options: dict.DicGenerationMode,
    },
    helpMessage: ['默认路径生成到当前项目目录下，也可以自定义生成路径'],
    colProps: { span: 12 },
  },
  {
    label: '后端生成路径',
    field: 'genPath',
    component: 'Input',
    show: ({ values }) => values.genType == GenModeEnum.CUSTOM,
    colProps: { span: 12 },
  },
  {
    label: '前端生成路径',
    field: 'uiPath',
    component: 'Input',
    show: ({ values }) => values.genType == GenModeEnum.CUSTOM,
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
      options: dict.DicYesNoOptions,
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
      options: dict.DicSourceMode,
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '依赖缩写模式',
    field: 'dependMode',
    component: 'RadioButtonGroup',
    defaultValue: DicYesNoEnum.NO,
    componentProps: {
      options: dict.DicYesNoOptions,
    },
    helpMessage: ['否：默认引用到具体文件或字段', '是：默认引用到相应文件夹或类'],
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
        api: listModuleApi,
        params: { status: DicStatusEnum.NORMAL },
        showSearch: true,
        optionFilterProp: 'label',
        resultField: 'items',
        labelField: 'name',
        valueField: 'id',
        onChange: (e: SelectValue) => {
          (async () => {
            const treeData =
              e === undefined
                ? []
                : await getMenuRouteListApi({
                    moduleId: e as string,
                    menuTypeLimit: MenuTypeEnum.DIR,
                    defaultNode: true,
                  });
            if (formModel.parentMenuId !== undefined && formModel.parentMenuId !== COMMON_MENU) {
              if (!hasTreeNode(treeData, 'id', 'children', formModel.parentMenuId)) {
                formModel.parentMenuId = undefined;
              }
            }
            if (formActionType !== undefined) {
              const { updateSchema } = formActionType;
              updateSchema({
                field: 'parentMenuId',
                componentProps: { treeData },
              });
            }
          })();
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
      options: dict.DicYesNoOptions,
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
      options: dict.DicYesNoOptions,
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
      options: dict.DicYesNoOptions,
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
      options: dict.DicYesNoOptions,
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
      options: dict.DicYesNoOptions,
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
      options: dict.DicYesNoOptions,
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
      options: dict.DicYesNoOptions,
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
      options: dict.DicYesNoOptions,
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '缓存',
    field: 'apiCache',
    component: 'RadioButtonGroup',
    defaultValue: GenStatusEnum.FALSE,
    componentProps: {
      options: dict.DicYesNoOptions,
    },
    required: true,
    colProps: { span: 12 },
  },
];
