import { dicDictList } from '@/api/sys/dict.api';
import { BasicColumn, FormSchema } from '@/components/Table';
import { DictDataIM, DictTypeIM } from '@/model/tenant';
import { dictConversion } from '@/utils/xueyi';
import { DicCodeEnum, DicSortEnum, DicStatusEnum, DicYesNoEnum } from '@/enums';
import { optionDictTypeApi } from '@/api/tenant/dict/dictType.api';
import { DicCacheTypeEnum, DicCodeDictEnum, DicDataTypeEnum } from '@/enums/tenant';
import { isNotEmpty } from '@/utils/is';
import { DescItem } from '@/components/Description';
import { listTenantApi } from '@/api/tenant/tenant/tenant.api';

/** 字典查询 */
export const dictMap = await dicDictList([
  DicCodeEnum.SYS_NORMAL_DISABLE,
  DicCodeDictEnum.SYS_DICT_DATA_TYPE,
  DicCodeDictEnum.SYS_DICT_CACHE_TYPE,
]);

/** 字典表 */
export const dict: any = {
  DicNormalDisableOptions: dictMap[DicCodeEnum.SYS_NORMAL_DISABLE],
  DicDictDataTypeOptions: dictMap[DicCodeDictEnum.SYS_DICT_DATA_TYPE],
  DicDictCacheTypeOptions: dictMap[DicCodeDictEnum.SYS_DICT_CACHE_TYPE],
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
    title: '数据类型',
    dataIndex: 'dataType',
    width: 120,
    customRender: ({ record }) => {
      const data = record as DictTypeIM;
      return dictConversion(dict.DicDictDataTypeOptions, data?.dataType);
    },
  },
  {
    title: '缓存类型',
    dataIndex: 'cacheType',
    width: 120,
    customRender: ({ record }) => {
      const data = record as DictTypeIM;
      return dictConversion(dict.DicDictCacheTypeOptions, data?.cacheType);
    },
  },
  {
    title: '租户',
    dataIndex: 'enterpriseInfo.nick',
    width: 120,
    customRender: ({ record }) => {
      const data = record as DictTypeIM;
      return data.cacheType === DicCacheTypeEnum.TENANT ? data?.enterpriseInfo?.nick : '通用';
    },
  },
  {
    title: '数据类型',
    dataIndex: 'dataType',
    width: 120,
    customRender: ({ record }) => {
      const data = record as DictTypeIM;
      return dictConversion(dict.DicDictDataTypeOptions, data?.dataType);
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
    colProps: { span: 8 },
  },
  {
    label: '字典类型',
    field: 'code',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    label: '数据类型',
    field: 'dataType',
    component: 'Select',
    componentProps: {
      options: dict.DicDictDataTypeOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    colProps: { span: 8 },
  },
  {
    label: '缓存类型',
    field: 'cacheType',
    component: 'Select',
    componentProps: {
      options: dict.DicDictCacheTypeOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    colProps: { span: 8 },
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
    colProps: { span: 8 },
  },
];

/** 字典数据 - 查询数据 */
export const dataSearchFormSchema: FormSchema[] = [
  {
    label: '数据键值',
    field: 'value',
    component: 'Input',
    colProps: { span: 12 },
  },
  {
    label: '数据标签',
    field: 'label',
    component: 'Input',
    colProps: { span: 12 },
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
    colProps: { span: 12 },
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
    dynamicDisabled: ({ values }) => isNotEmpty(values.id),
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '缓存类型',
    field: 'cacheType',
    component: 'RadioButtonGroup',
    defaultValue: DicCacheTypeEnum.OVERALL,
    componentProps: {
      options: dict.DicDictCacheTypeOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    dynamicDisabled: ({ values }) => isNotEmpty(values.id),
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '租户名称',
    field: 'tenantId',
    component: 'ApiSelect',
    componentProps: {
      api: listTenantApi,
      params: { status: DicStatusEnum.NORMAL },
      showSearch: true,
      optionFilterProp: 'label',
      resultField: 'items',
      labelField: 'nick',
      valueField: 'id',
    },
    dynamicDisabled: ({ values }) => isNotEmpty(values.id),
    ifShow: ({ values }) => values.cacheType === DicCacheTypeEnum.TENANT,
    required: ({ values }) => values.cacheType === DicCacheTypeEnum.TENANT,
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
    label: '数据类型',
    field: 'dataType',
    component: 'RadioButtonGroup',
    defaultValue: DicDataTypeEnum.DEFAULT,
    componentProps: {
      options: dict.DicDictDataTypeOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    colProps: { span: 24 },
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

/** 字典 - 详情数据 */
export const typeDetailSchema: DescItem[] = [
  {
    label: '字典名称',
    field: 'name',
    span: 8,
  },
  {
    label: '字典类型',
    field: 'code',
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
