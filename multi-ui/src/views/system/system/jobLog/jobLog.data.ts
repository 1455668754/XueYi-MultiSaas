import {FormSchema} from '/@/components/Form';
import {BasicColumn} from '/@/components/Table';
import {DescItem} from '/@/components/Description';
import {dicDictList} from '@/api/sys/dict.api';
import {dictConversion} from '/@/utils/xueyi';
import {JobLogIM} from '@/model/system';
import {optionJobApi} from '@/api/system/system/job.api';

/** 字典查询 */
export const dictMap = await dicDictList(['sys_job_group', 'sys_message_status']);

/** 字典表 */
export const dict: any = {
  DicJobGroupOptions: dictMap['sys_job_group'],
  DicMessageStatusOptions: dictMap['sys_message_status'],
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
      const data = record as JobLogIM;
      return dictConversion(dict.DicJobGroupOptions, data.jobGroup);
    },
  },
  {
    title: '执行状态',
    dataIndex: 'status',
    width: 220,
    customRender: ({ record }) => {
      const data = record as JobLogIM;
      return dictConversion(dict.DicMessageStatusOptions, data.status);
    },
  },
  {
    title: '执行时间',
    dataIndex: 'createTime',
    width: 220,
  },
  {
    title: '调用目标字符串',
    dataIndex: 'invokeTarget',
    width: 220,
  },
  {
    title: '日志信息',
    dataIndex: 'jobMessage',
    width: 220,
  },
];

/** 查询数据 */
export const searchFormSchema: FormSchema[] = [
  {
    label: '任务名称',
    field: 'jobId',
    component: 'ApiSelect',
    componentProps: {
      api: optionJobApi,
      showSearch: true,
      optionFilterProp: 'label',
      resultField: 'items',
      labelField: 'name',
      valueField: 'id',
    },
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
    label: '执行状态',
    field: 'status',
    component: 'Select',
    componentProps: {
      options: dict.DicMessageStatusOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    colProps: { span: 6 },
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
    label: '日志信息',
    field: 'jobMessage',
    span: 8,
  },
  {
    label: '执行状态',
    field: 'status',
    render: (val) => {
      return dictConversion(dict.DicMessageStatusOptions, val);
    },
    span: 8,
  },
  {
    label: '异常信息',
    field: 'exceptionInfo',
    span: 8,
  },
  {
    label: '执行时间',
    field: 'createTime',
    span: 8,
  },
];
