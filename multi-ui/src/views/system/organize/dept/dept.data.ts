import { FormSchema } from '@/components/Form';
import { BasicColumn } from '@/components/Table';
import { DescItem } from '@/components/Description';
import { dicConfigList, dicDictList } from '@/api/sys/dict.api';
import { dictConversion } from '@/utils/xueyi';
import { ConfigCodeEnum, DicCodeEnum, DicSortEnum, DicStatusEnum, DicYesNoEnum } from '@/enums';
import { DeptIM } from '@/model/system/organize';
import { listRoleApi } from '@/api/system/authority/role.api';

/** 字典查询 */
export const dictMap = await dicDictList([DicCodeEnum.SYS_NORMAL_DISABLE]);

/** 字典表 */
export const dict: any = {
  DicNormalDisableOptions: dictMap[DicCodeEnum.SYS_NORMAL_DISABLE],
};

/** 参数查询 */
export const configMap = await dicConfigList([
  ConfigCodeEnum.SYS_CODE_SHOW,
  ConfigCodeEnum.SYS_CODE_MUST,
]);

/** 参数表 */
export const config: any = {
  ConfigCodeShow: configMap[ConfigCodeEnum.SYS_CODE_SHOW],
  ConfigCodeMust: configMap[ConfigCodeEnum.SYS_CODE_MUST],
};

/** 表格数据 */
export const columns: BasicColumn[] = [
  {
    title: '部门名称',
    dataIndex: 'name',
    align: 'left',
    width: 220,
  },
  {
    title: '负责人',
    dataIndex: 'leader',
    width: 220,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 220,
    customRender: ({ record }) => {
      const data = record as DeptIM;
      return dictConversion(dict.DicNormalDisableOptions, data.status);
    },
  },
];

/** 查询数据 */
export const searchFormSchema: FormSchema[] = [
  {
    label: '部门名称',
    field: 'name',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '部门编码',
    field: 'code',
    component: 'Input',
    ifShow: () => DicYesNoEnum.YES === config.ConfigCodeShow,
    colProps: { span: 6 },
  },
  {
    label: '负责人',
    field: 'leader',
    component: 'Input',
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
export const formSchema: FormSchema[] = [
  {
    label: '部门id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '上级部门',
    field: 'parentId',
    component: 'TreeSelect',
    componentProps: {
      showSearch: true,
      treeNodeFilterProp: 'title',
      fieldNames: {
        label: 'name',
        value: 'id',
      },
      getPopupContainer: () => document.body,
    },
    required: true,
    colProps: { span: 24 },
  },
  {
    label: '部门名称',
    field: 'name',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '部门编码',
    field: 'code',
    component: 'Input',
    ifShow: () => DicYesNoEnum.YES === config.ConfigCodeShow,
    required: () =>
      DicYesNoEnum.YES === config.ConfigCodeShow && DicYesNoEnum.YES === config.ConfigCodeMust,
    colProps: { span: 12 },
  },
  {
    label: '负责人',
    field: 'leader',
    component: 'Input',
    colProps: { span: 12 },
  },
  {
    label: '联系电话',
    field: 'phone',
    component: 'Input',
    rules: [
      {
        pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
        message: '请输入正确的手机号码',
        trigger: 'blur',
      },
    ],
    colProps: { span: 12 },
  },
  {
    label: '邮箱',
    field: 'email',
    component: 'Input',
    rules: [
      {
        type: 'email',
        message: '请输入正确的邮箱地址',
        trigger: ['change', 'blur'],
      },
    ],
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
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
    colProps: { span: 24 },
  },
];

/** 表单数据 - 角色分配 */
export const roleFormSchema: FormSchema[] = [
  {
    label: '部门Id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '部门名称',
    field: 'name',
    component: 'Input',
    dynamicDisabled: true,
    colProps: { span: 24 },
  },
  {
    label: '角色分配',
    field: 'roleIds',
    component: 'ApiTransfer',
    componentProps: {
      api: listRoleApi,
      params: { status: DicStatusEnum.NORMAL },
      titles: ['待选', '已选'],
      showSearch: true,
      resultField: 'items',
      labelField: 'name',
      valueField: 'id',
    },
  },
];

/** 详情数据 */
export const detailSchema: DescItem[] = [
  {
    label: '父部门id',
    field: 'parentId',
    span: 8,
  },
  {
    label: '部门编码',
    field: 'code',
    show: () => DicYesNoEnum.YES === config.ConfigCodeShow,
    span: 8,
  },
  {
    label: '部门名称',
    field: 'name',
    span: 8,
  },
  {
    label: '负责人',
    field: 'leader',
    span: 8,
  },
  {
    label: '联系电话',
    field: 'phone',
    span: 8,
  },
  {
    label: '邮箱',
    field: 'email',
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
