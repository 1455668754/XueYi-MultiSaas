import { FormSchema } from '/@/components/Form';
import { BasicColumn } from '/@/components/Table';
import { DescItem } from '/@/components/Description';
import { dicDictList } from '@/api/sys/dict.api';
import { DicCommonPrivateEnum, DicShowHideEnum, DicSortEnum, DicStatusEnum } from '@/enums/basic';
import { ModuleIM } from '@/model/system/authority';
import { useUserStore } from '/@/store/modules/user';
import { FrameTypeEnum } from '@/enums/system/authority';
import { dictConversion } from '/@/utils/xueyi';
import { isNotEmpty } from '@/utils/is';

/** 字典查询 */
export const dictMap = await dicDictList([
  'auth_frame_type',
  'sys_show_hide',
  'sys_normal_disable',
  'sys_common_private',
]);

/** 字典表 */
export const dict: any = {
  DicAuthFrameTypeOptions: dictMap['auth_frame_type'],
  DicShowHideOptions: dictMap['sys_show_hide'],
  DicNormalDisableOptions: dictMap['sys_normal_disable'],
  DicCommonPrivateOptions: dictMap['sys_common_private'],
};

/** 表格数据 */
export const columns: BasicColumn[] = [
  {
    title: '模块名称',
    dataIndex: 'name',
    width: 220,
  },
  {
    title: '模块logo',
    dataIndex: 'logo',
    slots: { customRender: 'logo' },
    width: 120,
  },
  {
    title: '模块类型',
    dataIndex: 'type',
    width: 220,
    customRender: ({ record }) => {
      const data = record as ModuleIM;
      return dictConversion(dict.DicAuthFrameTypeOptions, data.type);
    },
  },
  {
    title: '显隐状态',
    dataIndex: 'hideModule',
    width: 220,
    customRender: ({ record }) => {
      const data = record as ModuleIM;
      return dictConversion(dict.DicShowHideOptions, data.hideModule);
    },
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 220,
    customRender: ({ record }) => {
      const data = record as ModuleIM;
      return dictConversion(dict.DicNormalDisableOptions, data.status);
    },
  },
  {
    title: '公共模块',
    dataIndex: 'isCommon',
    width: 220,
    customRender: ({ record }) => {
      const data = record as ModuleIM;
      return dictConversion(dict.DicCommonPrivateOptions, data.isCommon);
    },
  },
];

/** 查询数据 */
export const searchFormSchema: FormSchema[] = [
  {
    label: '模块名称',
    field: 'name',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '模块类型',
    field: 'type',
    component: 'Select',
    componentProps: {
      options: dict.DicAuthFrameTypeOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    colProps: { span: 6 },
  },
  {
    label: '显隐状态',
    field: 'hideModule',
    component: 'Select',
    componentProps: {
      options: dict.DicShowHideOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    colProps: { span: 6 },
  },
  {
    label: '状态',
    field: 'status',
    component: 'Select',
    componentProps: {
      options: dict.DicNormalDisableOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    colProps: { span: 6 },
  },
  {
    label: '公共模块',
    field: 'isCommon',
    component: 'Select',
    componentProps: {
      options: dict.DicCommonPrivateOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    colProps: { span: 6 },
  },
];

/** 表单数据 */
export const formSchema: FormSchema[] = [
  {
    label: '模块Id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '模块名称',
    field: 'name',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '公共模块',
    field: 'isCommon',
    component: 'RadioButtonGroup',
    defaultValue: DicCommonPrivateEnum.PRIVATE,
    componentProps: {
      options: dict.DicCommonPrivateOptions,
    },
    helpMessage: ['是否可以被其他租户使用'],
    dynamicDisabled: ({ values }) => isNotEmpty(values.id),
    required: () => useUserStore().isLessor,
    ifShow: () => useUserStore().isLessor,
    colProps: { span: 12 },
  },
  {
    label: 'logo',
    field: 'logo',
    component: 'ImageUpload',
    required: true,
    colProps: { span: 24 },
  },
  {
    label: '模块类型',
    field: 'type',
    component: 'RadioButtonGroup',
    defaultValue: FrameTypeEnum.NORMAL,
    componentProps: {
      options: dict.DicAuthFrameTypeOptions,
    },
    required: true,
    colProps: { span: 24 },
  },
  {
    label: '路由地址',
    field: 'path',
    component: 'Input',
    helpMessage: ['访问的路由地址，如：`https://doc.xueyitt.cn`'],
    colProps: { span: 12 },
  },
  {
    label: '路由参数',
    field: 'paramPath',
    component: 'Input',
    helpMessage: ['访问模块时传递的参数'],
    colProps: { span: 12 },
  },
  {
    label: '显隐状态',
    field: 'hideModule',
    component: 'RadioButtonGroup',
    defaultValue: DicShowHideEnum.SHOW,
    componentProps: {
      options: dict.DicShowHideOptions,
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '状态',
    field: 'status',
    component: 'RadioButtonGroup',
    defaultValue: DicStatusEnum.NORMAL,
    componentProps: {
      options: dict.DicNormalDisableOptions,
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '显示顺序',
    field: 'sort',
    component: 'InputNumber',
    defaultValue: DicSortEnum.ZERO,
    colProps: { span: 12 },
  },
  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
    colProps: { span: 24 },
  },
];

/** 详情数据 */
export const detailSchema: DescItem[] = [
  {
    label: '模块名称',
    field: 'name',
    span: 8,
  },
  {
    label: '公共模块',
    field: 'isCommon',
    render: (val) => {
      return dictConversion(dict.DicCommonPrivateOptions, val);
    },
    span: 8,
  },
  {
    label: '模块logo',
    field: 'logo',
    span: 8,
  },
  {
    label: '路由地址',
    field: 'path',
    span: 8,
  },
  {
    label: '路由参数',
    field: 'paramPath',
    span: 8,
  },
  {
    label: '模块类型',
    field: 'type',
    render: (val) => {
      return dictConversion(dict.DicAuthFrameTypeOptions, val);
    },
    span: 8,
  },
  {
    label: '显隐状态',
    field: 'hideModule',
    render: (val) => {
      return dictConversion(dict.DicShowHideOptions, val);
    },
    span: 8,
  },
  {
    label: '显示顺序',
    field: 'sort',
    span: 8,
  },
  {
    label: '状态',
    field: 'status',
    render: (val) => {
      return dictConversion(dict.DicNormalDisableOptions, val);
    },
    span: 8,
  },
  {
    label: '备注',
    field: 'remark',
    span: 8,
  },
];
