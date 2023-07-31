import { dicDictList } from '@/api/sys/dict.api';
import { BasicColumn, FormSchema } from '@/components/Table';
import { DictDataIM, DictTypeIM } from '@/model/tenant';
import { dictConversion } from '@/utils/xueyi';
import { isEmpty, isNil } from 'lodash-es';
import { DicSortEnum, DicStatusEnum, DicYesNoEnum } from '@/enums';
import { optionDictTypeApi } from '@/api/tenant/dict/dictType.api';

/** 字典查询 */
export const dictMap = await dicDictList(['sys_normal_disable']);

/** 字典表 */
export const dict: any = {
  DicNormalDisableOptions: dictMap['sys_normal_disable'],
};

/** 字典类型 - 表格数据 */
export const typeColumns: BasicColumn[] = [
  {
    title: '字典名称',
    dataIndex: 'name',
    width: 220,
  },
  {
    title: '字典类型',
    dataIndex: 'code',
    width: 220,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 220,
    customRender: ({ record }) => {
      const data = record as DictTypeIM;
      return dictConversion(dict.DicNormalDisableOptions, data.status);
    },
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 220,
  },
];

/** 字典数据 - 表格数据 */
export const dataColumns: BasicColumn[] = [
  {
    title: '数据标签',
    dataIndex: 'label',
    width: 220,
  },
  {
    title: '数据键值',
    dataIndex: 'value',
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
      const data = record as DictDataIM;
      return dictConversion(dict.DicNormalDisableOptions, data.status);
    },
  },
  {
    title: '备注',
    dataIndex: 'remark',
    width: 220,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 220,
  },
];

/** 字典类型 - 查询数据 */
export const typeSearchFormSchema: FormSchema[] = [
  {
    label: '字典名称',
    field: 'name',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '字典类型',
    field: 'code',
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

/** 字典数据 - 查询数据 */
export const dataSearchFormSchema: FormSchema[] = [
  {
    label: '字典名称',
    field: 'code',
    component: 'ApiSelect',
    componentProps: {
      api: optionDictTypeApi,
      showSearch: true,
      optionFilterProp: 'label',
      resultField: 'items',
      labelField: 'name',
      valueField: 'code',
    },
    colProps: { span: 6 },
  },
  {
    label: '数据标签',
    field: 'label',
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

/** 字典类型 - 表单数据 */
export const typeFormSchema: FormSchema[] = [
  {
    label: '字典主键',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '字典名称',
    field: 'name',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '字典类型',
    field: 'code',
    component: 'Input',
    dynamicDisabled: ({ values }) => !isNil(values.id) && !isEmpty(values.id),
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

/** 字典数据 - 表单数据 */
export const dataFormSchema: FormSchema[] = [
  {
    label: '数据Id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '字典编码',
    field: 'code',
    component: 'ApiSelect',
    componentProps: {
      api: optionDictTypeApi,
      resultField: 'items',
      labelField: 'name',
      valueField: 'code',
    },
    dynamicDisabled: true,
    colProps: { span: 24 },
  },
  {
    label: '数据标签',
    field: 'label',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '数据键值',
    field: 'value',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '样式属性',
    field: 'cssClass',
    component: 'Select',
    componentProps: {
      options: dict.DicDictColorOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    colProps: { span: 12 },
  },
  {
    label: '表格回显样式',
    field: 'listClass',
    component: 'Select',
    componentProps: {
      options: dict.DicDictColorOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    colProps: { span: 12 },
  },
  {
    label: '是否默认',
    field: 'isDefault',
    component: 'RadioButtonGroup',
    defaultValue: DicYesNoEnum.NO,
    componentProps: {
      options: dict.DicYesNoOptions,
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
