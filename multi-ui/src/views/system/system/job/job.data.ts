import { FormSchema } from '@/components/Form';
import { BasicColumn } from '@/components/Table';
import { DescItem } from '@/components/Description';
import { dicDictList } from '@/api/sys/dict.api';
import { dictConversion } from '@/utils/xueyi';
import { JobIM } from '@/model/system/system';
import { JobConcurrentEnum, JobMisfireEnum, JobStatusEnum } from '@/enums/system/system';

/** 字典查询 */
export const dictMap = await dicDictList([
  'sys_job_concurrent',
  'sys_job_group',
  'sys_job_policy',
  'sys_job_status',
]);

/** 字典表 */
export const dict: any = {
  DicJobConcurrentOptions: dictMap['sys_job_concurrent'],
  DicJobGroupOptions: dictMap['sys_job_group'],
  DicJobPolicyOptions: dictMap['sys_job_policy'],
  DicJobStatusOptions: dictMap['sys_job_status'],
};

/** 表格数据 */
export const columns: BasicColumn[] = [
  {
    title: '任务名称',
    dataIndex: 'name',
    width: 220,
  },
  {
    title: '任务组名',
    dataIndex: 'jobGroup',
    width: 220,
    customRender: ({ record }) => {
      const data = record as JobIM;
      return dictConversion(dict.DicJobGroupOptions, data.jobGroup);
    },
  },
  {
    title: '调用目标字符串',
    dataIndex: 'invokeTarget',
    width: 220,
  },
  {
    title: 'cron执行表达式',
    dataIndex: 'cronExpression',
    width: 220,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 220,
    customRender: ({ record }) => {
      const data = record as JobIM;
      return dictConversion(dict.DicJobStatusOptions, data.status);
    },
  },
  {
    title: '计划执行错误策略',
    dataIndex: 'misfirePolicy',
    width: 220,
    customRender: ({ record }) => {
      const data = record as JobIM;
      return dictConversion(dict.DicJobPolicyOptions, data.misfirePolicy);
    },
  },
  {
    title: '是否并发执行',
    dataIndex: 'concurrent',
    width: 220,
    customRender: ({ record }) => {
      const data = record as JobIM;
      return dictConversion(dict.DicJobConcurrentOptions, data.concurrent);
    },
  },
];

/** 查询数据 */
export const searchFormSchema: FormSchema[] = [
  {
    label: '任务名称',
    field: 'name',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '任务组名',
    field: 'jobGroup',
    component: 'Select',
    componentProps: {
      options: dict.DicJobGroupOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    colProps: { span: 6 },
  },
  {
    label: '状态',
    field: 'status',
    component: 'Select',
    componentProps: {
      options: dict.DicJobStatusOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    colProps: { span: 6 },
  },
];

/** 表单数据 */
export const formSchema: FormSchema[] = [
  {
    label: '任务Id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '任务名称',
    field: 'name',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '任务组名',
    field: 'jobGroup',
    component: 'Select',
    componentProps: {
      options: dict.DicJobGroupOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '调用目标字符串',
    field: 'invokeTarget',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: 'cron执行表达式',
    field: 'cronExpression',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '计划执行错误策略',
    field: 'misfirePolicy',
    component: 'Select',
    defaultValue: JobMisfireEnum.DEFAULT,
    componentProps: {
      options: dict.DicJobPolicyOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '是否并发执行',
    field: 'concurrent',
    component: 'RadioButtonGroup',
    defaultValue: JobConcurrentEnum.FORBID,
    componentProps: {
      options: dict.DicJobConcurrentOptions,
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '状态',
    field: 'status',
    component: 'RadioButtonGroup',
    defaultValue: JobStatusEnum.PAUSE,
    componentProps: {
      options: dict.DicJobStatusOptions,
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

/** 详情数据 */
export const detailSchema: DescItem[] = [
  {
    label: '任务名称',
    field: 'name',
    span: 8,
  },
  {
    label: '任务组名',
    field: 'jobGroup',
    render: (val) => {
      return dictConversion(dict.DicJobGroupOptions, val);
    },
    span: 8,
  },
  {
    label: '调用目标字符串',
    field: 'invokeTarget',
    span: 8,
  },
  {
    label: 'cron执行表达式',
    field: 'cronExpression',
    span: 8,
  },
  {
    label: '计划执行错误策略',
    field: 'misfirePolicy',
    render: (val) => {
      return dictConversion(dict.DicJobPolicyOptions, val);
    },
    span: 8,
  },
  {
    label: '是否并发执行',
    field: 'concurrent',
    render: (val) => {
      return dictConversion(dict.DicJobConcurrentOptions, val);
    },
    span: 8,
  },
  {
    label: '状态',
    field: 'status',
    render: (val) => {
      return dictConversion(dict.DicJobStatusOptions, val);
    },
    span: 8,
  },
  {
    label: '备注',
    field: 'remark',
    span: 8,
  },
];
