import { FormSchema } from '/@/components/Form';
import { BasicColumn } from '/@/components/Table';
import { DescItem } from '/@/components/Description';
import { dicDictList } from '@/api/sys/dict.api';
import { dictConversion } from '/@/utils/xueyi';
import { ColorEnum, DicSortEnum, DicStatusEnum, DicYesNoEnum } from '@/enums/basic';
import { TenantIM } from '@/model/tenant/tenant';
import { optionStrategyApi } from '@/api/tenant/source/strategy.api';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';

/** 字典查询 */
export const dictMap = await dicDictList(['sys_yes_no', 'sys_normal_disable']);

/** 字典表 */
export const dict: any = {
  DicYesNoOptions: dictMap['sys_yes_no'],
  DicNormalDisableOptions: dictMap['sys_normal_disable'],
};

/** 租户新增分页数据 */
export const tenantInitList = [
  { key: 'strategy', title: '策略源', current: 0 },
  { key: 'tenant', title: '租户信息', current: 1 },
  { key: 'organize', title: '租户数据', current: 2 },
  { key: 'authority', title: '租户权限', current: 3 },
];

/** 表格数据 */
export const columns: BasicColumn[] = [
  {
    title: '租户账号',
    dataIndex: 'name',
    customRender: ({ record }) => {
      const data = record as TenantIM;
      return h(Tag, { color: ColorEnum.BLUE }, () => data.name);
    },
    width: 220,
  },
  {
    title: '系统名称',
    dataIndex: 'systemName',
    width: 220,
  },
  {
    title: '租户名称',
    dataIndex: 'nick',
    width: 220,
  },
  {
    title: '租户logo',
    dataIndex: 'logo',
    slots: { customRender: 'logo' },
    width: 220,
  },
  {
    title: '源策略',
    dataIndex: 'strategyId',
    width: 220,
  },
  {
    title: '修改次数',
    dataIndex: 'nameFrequency',
    customRender: ({ record }) => {
      const data = record as TenantIM;
      return h(Tag, { color: ColorEnum.CYAN }, () => data.nameFrequency);
    },
    width: 220,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 220,
    customRender: ({ record }) => {
      const data = record as TenantIM;
      return dictConversion(dict.DicNormalDisableOptions, data.status);
    },
  },
  {
    title: '超管租户',
    dataIndex: 'isLessor',
    width: 220,
    customRender: ({ record }) => {
      const data = record as TenantIM;
      return dictConversion(dict.DicYesNoOptions, data.isLessor);
    },
  },
  {
    title: '默认租户',
    dataIndex: 'isDefault',
    width: 220,
    customRender: ({ record }) => {
      const data = record as TenantIM;
      return dictConversion(dict.DicYesNoOptions, data.isDefault);
    },
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 220,
  },
];

/** 查询数据 */
export const searchFormSchema: FormSchema[] = [
  {
    label: '源策略',
    field: 'strategyId',
    component: 'ApiSelect',
    componentProps: {
      api: optionStrategyApi,
      showSearch: true,
      optionFilterProp: 'label',
      resultField: 'items',
      labelField: 'name',
      valueField: 'id',
    },
    colProps: { span: 6 },
  },
  {
    label: '租户账号',
    field: 'name',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '系统名称',
    field: 'systemName',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '租户名称',
    field: 'nick',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '超管租户',
    field: 'isLessor',
    component: 'Select',
    componentProps: {
      options: dict.DicYesNoOptions,
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
    label: '默认租户',
    field: 'isDefault',
    component: 'Select',
    componentProps: {
      options: dict.DicYesNoOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    colProps: { span: 6 },
  },
];

/** 表单数据 - 策略源 */
export const strategyFormSchema: FormSchema[] = [
  {
    label: '源策略',
    field: 'tenant.strategyId',
    component: 'ApiSelect',
    componentProps: {
      api: optionStrategyApi,
      showSearch: true,
      optionFilterProp: 'label',
      resultField: 'items',
      labelField: 'name',
      valueField: 'id',
    },
    helpMessage: '租户使用过程中产生数据所存储数据源对应的源策略',
    required: true,
    colProps: { span: 24 },
  },
];

/** 表单数据 - 租户信息 */
export const tenantFormSchema: FormSchema[] = [
  {
    label: '租户名称',
    field: 'tenant.nick',
    component: 'Input',
    helpMessage: '租户的名称：如雪忆科技',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '租户账号',
    field: 'tenant.name',
    component: 'Input',
    helpMessage: '租户登录时的企业账号',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '系统名称',
    field: 'tenant.systemName',
    component: 'Input',
    helpMessage: '租户登录系统后显示的系统名称',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '租户logo',
    field: 'tenant.logo',
    component: 'ImageUpload',
    colProps: { span: 24 },
  },
  {
    label: '超管租户',
    field: 'tenant.isLessor',
    component: 'RadioButtonGroup',
    defaultValue: DicYesNoEnum.NO,
    componentProps: {
      options: dict.DicYesNoOptions,
    },
    helpMessage: '超管租户具备比普通租户更高的权限，比如租户管理等',
    colProps: { span: 12 },
  },
  {
    label: '状态',
    field: 'tenant.status',
    component: 'RadioButtonGroup',
    defaultValue: DicStatusEnum.NORMAL,
    componentProps: {
      options: dict.DicNormalDisableOptions,
    },
    colProps: { span: 12 },
  },
  {
    label: '修改次数',
    field: 'tenant.nameFrequency',
    component: 'InputNumber',
    defaultValue: DicSortEnum.FIVE,
    helpMessage: '租户账号允许被修改的次数',
    colProps: { span: 12 },
  },
  {
    label: '显示顺序',
    field: 'tenant.sort',
    component: 'InputNumber',
    defaultValue: DicSortEnum.ZERO,
    colProps: { span: 12 },
  },
  {
    label: '备注',
    field: 'tenant.remark',
    component: 'InputTextArea',
    colProps: { span: 24 },
  },
];

/** 表单数据 - 租户数据 */
export const organizeFormSchema: FormSchema[] = [
  {
    label: '组织信息',
    field: '',
    component: 'Divider',
    colProps: { span: 24 },
  },
  {
    label: '部门名称',
    defaultValue: '雪忆科技',
    field: 'dept.name',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '岗位名称',
    defaultValue: 'boss',
    field: 'post.name',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '管理员信息',
    field: '',
    component: 'Divider',
    colProps: { span: 24 },
  },
  {
    label: '账号',
    defaultValue: 'admin',
    field: 'user.userName',
    component: 'Input',
    helpMessage: '新租户初始管理员的用户账号',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '昵称',
    defaultValue: '雪忆',
    field: 'user.nickName',
    component: 'Input',
    helpMessage: '新租户初始管理员的用户昵称',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '密码',
    field: 'user.password',
    defaultValue: '123456',
    component: 'InputPassword',
    helpMessage: ['新租户初始管理员的密码', '初始密码：123456'],
    required: true,
    colProps: { span: 24 },
  },
];

/** 表单数据 - 租户权限 */
export const authorityFormSchema: FormSchema[] = [
  {
    label: ' ',
    field: 'authIds',
    slot: 'menu',
    component: 'Input',
    colProps: { span: 24 },
  },
];

/** 表单数据 - 修改 */
export const formSchema: FormSchema[] = [
  {
    label: '租户Id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '租户名称',
    field: 'nick',
    component: 'Input',
    helpMessage: '租户的名称：如雪忆科技',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '租户账号',
    field: 'name',
    component: 'Input',
    helpMessage: '租户登录时的企业账号',
    dynamicDisabled: true,
    colProps: { span: 12 },
  },
  {
    label: '系统名称',
    field: 'systemName',
    component: 'Input',
    helpMessage: '租户登录系统后显示的系统名称',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '源策略',
    field: 'strategyId',
    component: 'ApiSelect',
    componentProps: {
      api: optionStrategyApi,
      showSearch: true,
      optionFilterProp: 'label',
      resultField: 'items',
      labelField: 'name',
      valueField: 'id',
    },
    helpMessage: '租户使用过程中产生数据所存储数据源对应的源策略',
    dynamicDisabled: true,
    colProps: { span: 12 },
  },
  {
    label: '租户logo',
    field: 'logo',
    component: 'ImageUpload',
    colProps: { span: 24 },
  },
  {
    label: '超管租户',
    field: 'isLessor',
    component: 'RadioButtonGroup',
    defaultValue: DicYesNoEnum.NO,
    componentProps: {
      options: dict.DicYesNoOptions,
    },
    helpMessage: '超管租户具备比普通租户更高的权限，比如租户管理等',
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
    colProps: { span: 12 },
  },
  {
    label: '修改次数',
    field: 'nameFrequency',
    component: 'InputNumber',
    defaultValue: DicSortEnum.FIVE,
    helpMessage: '租户账号允许被修改的次数',
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

/** 表单数据 - 权限 */
export const authFormSchema: FormSchema[] = [
  {
    label: '租户Id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '租户名称',
    field: 'name',
    component: 'Input',
    dynamicDisabled: true,
    colProps: { span: 12 },
  },
  {
    label: ' ',
    field: 'authIds',
    slot: 'menu',
    component: 'Input',
    colProps: { span: 24 },
  },
];

/** 详情数据 */
export const detailSchema: DescItem[] = [
  {
    label: '策略Id',
    field: 'strategyId',
    span: 8,
  },
  {
    label: '租户账号',
    field: 'name',
    span: 8,
  },
  {
    label: '系统名称',
    field: 'systemName',
    span: 8,
  },
  {
    label: '租户名称',
    field: 'nick',
    span: 8,
  },
  {
    label: '租户logo',
    field: 'logo',
    span: 8,
  },
  {
    label: '修改次数',
    field: 'nameFrequency',
    span: 8,
  },
  {
    label: '超管租户',
    field: 'isLessor',
    render: (val) => {
      return dictConversion(dict.DicYesNoOptions, val);
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
  {
    label: '默认租户',
    field: 'isDefault',
    render: (val) => {
      return dictConversion(dict.DicYesNoOptions, val);
    },
    span: 8,
  },
];
