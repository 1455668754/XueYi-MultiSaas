import { FormSchema } from '/@/components/Form';
import { BasicColumn } from '/@/components/Table';
import { DescItem } from '/@/components/Description';
import { dicDictList } from '@/api/sys/dict.api';
import { dictConversion } from '/@/utils/xueyi';
import { isEmpty, isNil } from 'lodash-es';
import { DicSortEnum, DicStatusEnum } from '@/enums/basic';
import { SourceIM } from '@/model/tenant';
import { DRIVER_CLASSNAME, URL_APPEND, URL_PREPEND } from '@/enums/tenant';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';
import { ColorEnum } from '@/enums';

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
    title: '数据源名称',
    dataIndex: 'name',
    width: 220,
  },
  {
    title: '数据源编码',
    dataIndex: 'slave',
    customRender: ({ record }) => {
      const data = record as SourceIM;
      return h(Tag, { color: ColorEnum.ORANGE }, () => data.slave);
    },
    width: 280,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 220,
    customRender: ({ record }) => {
      const data = record as SourceIM;
      return dictConversion(dict.DicNormalDisableOptions, data.status);
    },
  },
  {
    title: '默认数据源',
    dataIndex: 'isDefault',
    width: 220,
    customRender: ({ record }) => {
      const data = record as SourceIM;
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
    label: '数据源名称',
    field: 'name',
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
    label: '数据源Id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '数据源名称',
    field: 'name',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '数据源编码',
    field: 'slave',
    component: 'Input',
    ifShow: ({ values }) => !isNil(values.id) && !isEmpty(values.id),
    dynamicDisabled: true,
    colProps: { span: 12 },
  },
  {
    label: '驱动',
    field: 'driverClassName',
    component: 'InputTextArea',
    defaultValue: DRIVER_CLASSNAME,
    required: true,
    colProps: { span: 24 },
  },
  {
    label: '连接地址',
    field: 'urlPrepend',
    component: 'InputTextArea',
    defaultValue: URL_PREPEND,
    required: true,
    colProps: { span: 24 },
  },
  {
    label: '连接参数',
    field: 'urlAppend',
    component: 'InputTextArea',
    defaultValue: URL_APPEND,
    required: true,
    colProps: { span: 24 },
  },
  {
    label: '用户名',
    field: 'userName',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '密码',
    field: 'password',
    component: 'InputPassword',
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
    label: '数据源名称',
    field: 'name',
    span: 8,
  },
  {
    label: '数据源编码',
    field: 'slave',
    span: 8,
  },
  {
    label: '驱动',
    field: 'driverClassName',
    span: 8,
  },
  {
    label: '连接地址',
    field: 'urlPrepend',
    span: 8,
  },
  {
    label: '连接参数',
    field: 'urlAppend',
    span: 8,
  },
  {
    label: '用户名',
    field: 'userName',
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
    label: '默认数据源',
    field: 'isDefault',
    render: (val) => {
      return dictConversion(dict.DicYesNoOptions, val);
    },
    span: 8,
  },
];
