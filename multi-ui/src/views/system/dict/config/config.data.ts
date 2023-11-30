import { FormSchema } from '@/components/Form';
import { BasicColumn } from '@/components/Table';
import { DescItem } from '@/components/Description';
import { dicDictList } from '@/api/sys/dict.api';
import {
  ColorEnum,
  COMMON_TENANT_ID,
  DicCodeEnum,
  DicSortEnum,
  DicStatusEnum,
  DicYesNoEnum,
} from '@/enums';
import { ConfigIM } from '@/model/system/dict';
import { dictConversion } from '@/utils/xueyi';
import { DicCacheTypeEnum, DicCodeDictEnum, DicDataTypeEnum } from '@/enums/system/dict';
import { isNotEmpty } from '@/utils/core/ObjectUtil';
import { listTenantApi } from '@/api/tenant/tenant/tenant.api';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';

/** 字典查询 */
export const dictMap = await dicDictList([
  DicCodeEnum.SYS_YES_NO,
  DicCodeDictEnum.SYS_DICT_DATA_TYPE,
  DicCodeDictEnum.SYS_DICT_CACHE_TYPE,
]);

/** 字典表 */
export const dict: any = {
  DicYesNoOptions: dictMap[DicCodeEnum.SYS_YES_NO],
  DicDictDataTypeOptions: dictMap[DicCodeDictEnum.SYS_DICT_DATA_TYPE],
  DicDictCacheTypeOptions: dictMap[DicCodeDictEnum.SYS_DICT_CACHE_TYPE],
};

/** 表格数据 */
export const columns: BasicColumn[] = [
  {
    title: '参数名称',
    dataIndex: 'name',
    width: 220,
  },
  {
    title: '参数编码',
    dataIndex: 'code',
    width: 220,
  },
  {
    title: '参数键值',
    dataIndex: 'value',
    width: 220,
  },
  {
    title: '数据类型',
    dataIndex: 'dataType',
    width: 100,
    customRender: ({ record }) => {
      const data = record as ConfigIM;
      return dictConversion(dict.DicDictDataTypeOptions, data?.dataType);
    },
  },
  {
    title: '缓存类型',
    dataIndex: 'cacheType',
    width: 100,
    customRender: ({ record }) => {
      const data = record as ConfigIM;
      return dictConversion(dict.DicDictCacheTypeOptions, data?.cacheType);
    },
  },
  {
    title: '租户',
    dataIndex: 'enterpriseInfo.nick',
    width: 140,
    customRender: ({ record }) => {
      const data = record as ConfigIM;
      return data.cacheType === DicCacheTypeEnum.TENANT
        ? h(
            Tag,
            { color: data?.enterpriseInfo?.nick ? ColorEnum.PURPLE : ColorEnum.ORANGE },
            () => data?.enterpriseInfo?.nick || '通用',
          )
        : h(Tag, { color: ColorEnum.BLUE }, () => '公共');
    },
  },
  {
    title: '系统内置',
    dataIndex: 'type',
    width: 220,
    customRender: ({ record }) => {
      const data = record as ConfigIM;
      return dictConversion(dict.DicYesNoOptions, data.type);
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
    label: '参数名称',
    field: 'name',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '参数编码',
    field: 'code',
    component: 'Input',
    colProps: { span: 6 },
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
    colProps: { span: 6 },
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
    colProps: { span: 6 },
  },
  {
    label: '所属租户',
    field: 'tenantId',
    component: 'Select',
    componentProps: {
      showSearch: true,
      optionFilterProp: 'label',
      fieldNames: {
        label: 'nick',
        value: 'id',
      },
    },
    colProps: { span: 6 },
  },
  {
    label: '系统内置',
    field: 'type',
    component: 'Select',
    componentProps: {
      options: dict.DicYesNoOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    colProps: { span: 6 },
  },
];

/** 表单数据 */
export const formSchema: FormSchema[] = [
  {
    label: '参数主键',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '参数名称',
    field: 'name',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '参数编码',
    field: 'code',
    component: 'Input',
    dynamicDisabled: ({ values }) => isNotEmpty(values.id),
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '参数键值',
    field: 'value',
    component: 'Input',
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
    ifShow: ({ values }) =>
      isNotEmpty(values.tenantId) &&
      values.tenantId !== COMMON_TENANT_ID &&
      values.cacheType === DicCacheTypeEnum.TENANT,
    required: ({ values }) =>
      isNotEmpty(values.tenantId) &&
      values.tenantId !== COMMON_TENANT_ID &&
      values.cacheType === DicCacheTypeEnum.TENANT,
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
    dynamicDisabled: ({ values }) => isNotEmpty(values.tenantId),
    required: true,
    colProps: { span: 24 },
  },
  {
    label: '系统内置',
    field: 'type',
    component: 'RadioButtonGroup',
    defaultValue: DicYesNoEnum.NO,
    componentProps: {
      options: dict.DicYesNoOptions,
    },
    dynamicDisabled: ({ values }) => isNotEmpty(values.id),
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
    label: '参数名称',
    field: 'name',
    span: 8,
  },
  {
    label: '参数编码',
    field: 'code',
    span: 8,
  },
  {
    label: '参数键值',
    field: 'value',
    span: 8,
  },
  {
    label: '系统内置',
    field: 'type',
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
    label: '备注',
    field: 'remark',
    span: 8,
  },
];
