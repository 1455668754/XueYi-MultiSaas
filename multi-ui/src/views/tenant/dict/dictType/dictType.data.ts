import { FormSchema } from '/@/components/Form';
import { BasicColumn } from '/@/components/Table';
import { DescItem } from '/@/components/Description';
import { dicDictList } from '/@/api/sys/dict';
import { dictConversion } from '/@/utils/xueyi';
import { isEmpty, isNil } from 'lodash-es';
import { DicSortEnum, DicStatusEnum } from '/@/enums/basic';
import { DictTypeIM } from '/@/model/tenant';

/** 字典查询 */
export const dictMap = await dicDictList(['sys_normal_disable']);

/** 字典表 */
export const dict: any = {
  DicNormalDisableOptions: dictMap['sys_normal_disable'],
};

/** 表格数据 */
export const columns: BasicColumn[] = [
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

/** 查询数据 */
export const searchFormSchema: FormSchema[] = [
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

/** 表单数据 */
export const formSchema: FormSchema[] = [
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

/** 详情数据 */
export const detailSchema: DescItem[] = [
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
