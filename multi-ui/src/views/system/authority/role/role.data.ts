import { FormSchema } from '/@/components/Form';
import { BasicColumn } from '/@/components/Table';
import { DescItem } from '/@/components/Description';
import { dicDictList } from '/@/api/sys/dict';
import { DicSortEnum, DicStatusEnum } from '/@/enums/basic';
import { RoleIM } from '/@/model/system';
import { dictConversion } from '/@/utils/xueyi';
import { isEmpty } from '/@/utils/is';
import { DataScopeEnum } from '/@/enums/system';

/** 字典查询 */
export const dictMap = await dicDictList(['sys_normal_disable', 'auth_data_scope']);

/** 字典表 */
export const dict: any = {
  DicNormalDisableOptions: dictMap['sys_normal_disable'],
  DicAuthDataScopeOptions: dictMap['auth_data_scope'],
};

/** 角色新增分页数据 */
export const roleInitList = [
  { key: 'role', title: '角色信息', current: 0 },
  { key: 'auth', title: '功能权限', current: 1 },
  { key: 'organize', title: '数据权限', current: 2 },
];

/** 表格数据 */
export const columns: BasicColumn[] = [
  {
    title: '角色编码',
    dataIndex: 'code',
    width: 220,
  },
  {
    title: '角色名称',
    dataIndex: 'name',
    width: 220,
  },
  {
    title: '数据范围',
    dataIndex: 'dataScope',
    width: 220,
    customRender: ({ record }) => {
      const data = record as RoleIM;
      return dictConversion(dict.DicAuthDataScopeOptions, data.dataScope);
    },
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 220,
    customRender: ({ record }) => {
      const data = record as RoleIM;
      return dictConversion(dict.DicNormalDisableOptions, data.status);
    },
  },
];

/** 查询数据 */
export const searchFormSchema: FormSchema[] = [
  {
    label: '角色编码',
    field: 'code',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '角色名称',
    field: 'name',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '数据范围',
    field: 'dataScope',
    component: 'Select',
    componentProps: {
      options: dict.DicAuthDataScopeOptions,
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
];

/** 表单数据 */
export const roleFormSchema: FormSchema[] = [
  {
    label: '角色Id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '角色编码',
    field: 'code',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '角色名称',
    field: 'name',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '权限字符串',
    field: 'roleKey',
    component: 'Input',
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

/** 表单数据 - 功能权限 */
export const authFormSchema: FormSchema[] = [
  {
    label: '角色Id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '角色编码',
    field: 'code',
    component: 'Input',
    dynamicDisabled: true,
    ifShow: ({ values }) => !isEmpty(values.id),
    colProps: { span: 12 },
  },
  {
    label: '角色名称',
    field: 'name',
    component: 'Input',
    dynamicDisabled: true,
    ifShow: ({ values }) => !isEmpty(values.id),
    colProps: { span: 12 },
  },
  {
    label: ' ',
    field: 'authIds',
    slot: 'auth',
    component: 'Input',
    colProps: { span: 24 },
  },
];

/** 表单数据 - 数据权限 */
export const organizeFormSchema: FormSchema[] = [
  {
    label: '角色Id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '角色编码',
    field: 'code',
    component: 'Input',
    dynamicDisabled: true,
    ifShow: ({ values }) => !isEmpty(values.id),
    colProps: { span: 12 },
  },
  {
    label: '角色名称',
    field: 'name',
    component: 'Input',
    dynamicDisabled: true,
    ifShow: ({ values }) => !isEmpty(values.id),
    colProps: { span: 12 },
  },
  {
    label: '数据范围',
    field: 'dataScope',
    component: 'Select',
    componentProps: {
      options: dict.DicAuthDataScopeOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    label: ' ',
    field: 'organizeIds',
    slot: 'organize',
    component: 'Input',
    show: ({ values }) => values.dataScope == DataScopeEnum.CUSTOM,
    colProps: { span: 24 },
  },
];

/** 详情数据 */
export const detailSchema: DescItem[] = [
  {
    label: '角色编码',
    field: 'code',
    span: 8,
  },
  {
    label: '角色名称',
    field: 'name',
    span: 8,
  },
  {
    label: '权限字符串',
    field: 'roleKey',
    span: 8,
  },
  {
    label: '数据范围',
    field: 'dataScope',
    render: (val) => {
      return dictConversion(dict.DicAuthDataScopeOptions, val);
    },
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
    label: '显示顺序',
    field: 'sort',
    span: 8,
  },
  {
    label: '备注',
    field: 'remark',
    span: 24,
  },
];
