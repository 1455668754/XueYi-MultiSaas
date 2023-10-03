import { FormSchema } from '@/components/Form';
import { BasicColumn } from '@/components/Table';
import { DescItem } from '@/components/Description';
import { dicConfigList, dicDictList } from '@/api/sys/dict.api';
import { dictConversion } from '@/utils/xueyi';
import { ConfigCodeEnum, DicCodeEnum, DicSortEnum, DicStatusEnum, DicYesNoEnum } from '@/enums';
import { AuthGroupIM } from '@/model/system/authority';

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
    title: '权限组编码',
    dataIndex: 'code',
    ifShow: () => DicYesNoEnum.YES === config.ConfigCodeShow,
    width: 220,
  },
  {
    title: '权限组名称',
    dataIndex: 'name',
    width: 220,
  },
  {
    title: '权限字符串',
    dataIndex: 'authKey',
    width: 220,
  },
  {
    title: '显示顺序',
    dataIndex: 'sort',
    width: 220,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 220,
    customRender: ({ record }) => {
      const data = record as AuthGroupIM;
      return dictConversion(dict.DicNormalDisableOptions, data.status);
    },
  },
];

/** 查询数据 */
export const searchFormSchema: FormSchema[] = [
  {
    label: '权限组编码',
    field: 'code',
    component: 'Input',
    ifShow: () => DicYesNoEnum.YES === config.ConfigCodeShow,
    colProps: { span: 6 },
  },
  {
    label: '权限组名称',
    field: 'name',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '权限字符串',
    field: 'authKey',
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
    label: '权限组Id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '权限组编码',
    field: 'code',
    component: 'Input',
    ifShow: () => DicYesNoEnum.YES === config.ConfigCodeShow,
    required: () =>
      DicYesNoEnum.YES === config.ConfigCodeShow && DicYesNoEnum.YES === config.ConfigCodeMust,
    colProps: { span: 12 },
  },
  {
    label: '权限组名称',
    field: 'name',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '权限字符串',
    field: 'authKey',
    component: 'Input',
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

/** 表单数据 - 权限 */
export const authFormSchema: FormSchema[] = [
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
    label: '权限组编码',
    field: 'code',
    show: () => DicYesNoEnum.YES === config.ConfigCodeShow,
    span: 8,
  },
  {
    label: '权限组名称',
    field: 'name',
    span: 8,
  },
  {
    label: '权限字符串',
    field: 'authKey',
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
