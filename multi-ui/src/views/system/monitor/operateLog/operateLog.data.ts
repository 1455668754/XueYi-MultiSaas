import { FormSchema } from '/@/components/Form';
import { BasicColumn } from '/@/components/Table';
import { DescItem } from '/@/components/Description';
import { dicDictList } from '@/api/sys/dict.api';
import { OperateLogIM } from '@/model/system';
import { dictConversion } from '/@/utils/xueyi';

/** 字典查询 */
export const dictMap = await dicDictList([
  'sys_operate_type',
  'sys_operate_status',
  'sys_operate_business',
]);

/** 字典表 */
export const dict: any = {
  DicOperateTypeOptions: dictMap['sys_operate_type'],
  DicOperateStatusOptions: dictMap['sys_operate_status'],
  DicOperateBusinessOptions: dictMap['sys_operate_business'],
};

/** 表格数据 */
export const columns: BasicColumn[] = [
  {
    title: '模块标题',
    dataIndex: 'title',
    width: 220,
  },
  {
    title: '业务类型',
    dataIndex: 'businessType',
    width: 220,
    customRender: ({ record }) => {
      const data = record as OperateLogIM;
      return dictConversion(dict.DicOperateBusinessOptions, data.businessType);
    },
  },
  {
    title: '方法名称',
    dataIndex: 'method',
    width: 220,
  },
  {
    title: '请求方式',
    dataIndex: 'requestMethod',
    width: 220,
  },
  {
    title: '操作类别',
    dataIndex: 'operateType',
    width: 220,
    customRender: ({ record }) => {
      const data = record as OperateLogIM;
      return dictConversion(dict.DicOperateTypeOptions, data.operateType);
    },
  },
  {
    title: '操作人员账号',
    dataIndex: 'userName',
    width: 220,
  },
  {
    title: '操作人员名称',
    dataIndex: 'userNick',
    width: 220,
  },
  {
    title: '主机地址',
    dataIndex: 'ip',
    width: 220,
  },
  {
    title: '操作状态',
    dataIndex: 'status',
    width: 220,
    customRender: ({ record }) => {
      const data = record as OperateLogIM;
      return dictConversion(dict.DicOperateStatusOptions, data.status);
    },
  },
  {
    title: '操作时间',
    dataIndex: 'operateTime',
    width: 220,
  },
];

/** 查询数据 */
export const searchFormSchema: FormSchema[] = [
  {
    label: '模块标题',
    field: 'title',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '业务类型',
    field: 'businessType',
    component: 'Select',
    componentProps: {
      options: dict.DicOperateBusinessOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    colProps: { span: 6 },
  },
  {
    label: '方法名称',
    field: 'method',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '请求方式',
    field: 'requestMethod',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '操作类别',
    field: 'operateType',
    component: 'Select',
    componentProps: {
      options: dict.DicOperateTypeOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    colProps: { span: 6 },
  },
  {
    label: '操作人员账号',
    field: 'userName',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '操作人员名称',
    field: 'userNick',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '主机地址',
    field: 'ip',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '操作地点',
    field: 'location',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '操作状态',
    field: 'status',
    component: 'Select',
    componentProps: {
      options: dict.DicOperateStatusOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    colProps: { span: 6 },
  },
  {
    label: '操作时间',
    field: 'operateTime',
    component: 'RangePicker',
    colProps: { span: 6 },
  },
];

/** 详情数据 */
export const detailSchema: DescItem[] = [
  {
    label: '模块标题',
    field: 'title',
    span: 8,
  },
  {
    label: '业务类型',
    field: 'businessType',
    render: (val) => {
      return dictConversion(dict.DicOperateBusinessOptions, val);
    },
    span: 8,
  },
  {
    label: '操作类别',
    field: 'operateType',
    render: (val) => {
      return dictConversion(dict.DicOperateTypeOptions, val);
    },
    span: 8,
  },
  {
    label: '操作人员账号',
    field: 'userName',
    span: 8,
  },
  {
    label: '操作人员名称',
    field: 'userNick',
    span: 8,
  },
  {
    label: '主机地址',
    field: 'ip',
    span: 8,
  },
  {
    label: '方法名称',
    field: 'method',
    span: 24,
  },
  {
    label: '请求URL',
    field: 'url',
    span: 24,
  },
  {
    label: '请求方式',
    field: 'requestMethod',
    span: 8,
  },
  {
    label: '操作状态',
    field: 'status',
    render: (val) => {
      return dictConversion(dict.DicOperateStatusOptions, val);
    },
    span: 8,
  },
  {
    label: '操作时间',
    field: 'operateTime',
    span: 8,
  },
  {
    label: '请求参数',
    field: 'param',
    span: 24,
  },
  {
    label: '操作地点',
    field: 'location',
    span: 24,
  },
  {
    label: '返回参数',
    field: 'jsonResult',
    span: 24,
  },
  {
    label: '错误消息',
    field: 'errorMsg',
    span: 24,
  },
];
