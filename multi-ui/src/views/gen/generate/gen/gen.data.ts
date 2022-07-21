import { FormSchema } from '/@/components/Form';
import { BasicColumn } from '/@/components/Table';

/** 表格数据 */
export const columns: BasicColumn[] = [
  {
    title: '表名称',
    dataIndex: 'name',
    width: 160,
  },
  {
    title: '表描述',
    dataIndex: 'comment',
    width: 160,
  },
  {
    title: '实体',
    dataIndex: 'className',
    width: 160,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180,
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    width: 180,
  },
];

/** 查询数据 */
export const searchFormSchema: FormSchema[] = [
  {
    label: '表名称',
    field: 'name',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    label: '表描述',
    field: 'comment',
    component: 'Input',
    colProps: { span: 8 },
  },
];

export const modelColumns: BasicColumn[] = [
  {
    title: '表名称',
    dataIndex: 'name',
    width: 160,
    align: 'left',
  },
  {
    title: '表描述',
    dataIndex: 'comment',
    width: 160,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180,
  },
];

export const searchModelFormSchema: FormSchema[] = [
  {
    label: '表名称',
    field: 'name',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    label: '表描述',
    field: 'comment',
    component: 'Input',
    colProps: { span: 8 },
  },
];
