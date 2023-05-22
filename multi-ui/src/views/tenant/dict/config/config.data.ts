import { FormSchema } from '/@/components/Form';
import { BasicColumn } from '/@/components/Table';
import { DescItem } from '/@/components/Description';
import { dicDictList } from '/@/api/sys/dict';
import { DicSortEnum, DicYesNoEnum } from '/@/enums/basic';
import { ConfigIM } from '/@/model/tenant';
import { dictConversion } from '/@/utils/xueyi';
import { isEmpty, isNil } from 'lodash-es';

/** 字典查询 */
export const dictMap = await dicDictList(['sys_yes_no']);

/** 字典表 */
export const dict: any = {
  DicYesNoOptions: dictMap['sys_yes_no'],
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
    dynamicDisabled: ({ values }) => !isNil(values.id) && !isEmpty(values.id),
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
    label: '系统内置',
    field: 'type',
    component: 'RadioButtonGroup',
    defaultValue: DicYesNoEnum.NO,
    componentProps: {
      options: dict.DicYesNoOptions,
    },
    dynamicDisabled: ({ values }) => !isNil(values.id) && !isEmpty(values.id),
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
