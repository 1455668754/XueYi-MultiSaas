import { FormSchema } from '/@/components/Form';
import { BasicColumn } from '/@/components/Table';
import { DescItem } from '/@/components/Description';
import { dicDictList } from '/@/api/sys/dict';
import { dictConversion } from '/@/utils/xueyi';
import { DicSortEnum, DicStatusEnum } from '/@/enums/basic';
import { StrategyIM, TenantIM } from '/@/model/tenant';
import { optionSourceApi } from '/@/api/tenant/source/source';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';
import { ColorEnum } from '/@/enums/appEnum';

/** 字典查询 */
export const dictMap = await dicDictList(['sys_yes_no', 'sys_normal_disable']);

/** 字典表 */
export const dict: any = {
  DicYesNoOptions: dictMap['sys_yes_no'],
  DicNormalDisableOptions: dictMap['sys_normal_disable'],
};

/** 表格数据 */
export const columns: BasicColumn[] = [
  {
    title: '策略名称',
    dataIndex: 'name',
    width: 220,
  },
  {
    title: '数据源Id',
    dataIndex: 'sourceId',
    width: 220,
  },
  {
    title: '数据源编码',
    dataIndex: 'sourceSlave',
    customRender: ({ record }) => {
      const data = record as StrategyIM;
      return h(Tag, { color: ColorEnum.ORANGE }, () => data.sourceSlave);
    },
    width: 280,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 220,
    customRender: ({ record }) => {
      const data = record as StrategyIM;
      return dictConversion(dict.DicNormalDisableOptions, data.status);
    },
  },
  {
    title: '默认策略',
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
    label: '策略名称',
    field: 'name',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '数据源',
    field: 'sourceId',
    component: 'ApiSelect',
    componentProps: {
      api: optionSourceApi,
      showSearch: true,
      optionFilterProp: 'label',
      resultField: 'items',
      labelField: 'name',
      valueField: 'id',
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
export const formSchema: FormSchema[] = [
  {
    label: '策略Id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '策略名称',
    field: 'name',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '数据源',
    field: 'sourceId',
    component: 'ApiSelect',
    componentProps: {
      api: optionSourceApi,
      showSearch: true,
      optionFilterProp: 'label',
      resultField: 'items',
      labelField: 'name',
      valueField: 'id',
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
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
    colProps: { span: 24 },
  },
];

/** 详情数据 */
export const detailSchema: DescItem[] = [
  {
    label: '策略名称',
    field: 'name',
    span: 8,
  },
  {
    label: '数据源Id',
    field: 'sourceId',
    span: 8,
  },
  {
    label: '数据源编码',
    field: 'sourceSlave',
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
    label: '创建时间',
    field: 'createTime',
    span: 8,
  },
  {
    label: '备注',
    field: 'remark',
    span: 24,
  },
];
