import { FormSchema } from '@/components/Form';
import { BasicColumn } from '@/components/Table';
import { listSourceApi } from '@/api/tenant/source/source.api';
import { DicStatusEnum } from '@/enums';
import { SourceIM } from '@/model/tenant/source';

async function sourceList() {
  const list = await listSourceApi({ status: DicStatusEnum.NORMAL });
  list.items.push({
    slave: 'master',
    name: '主数据源',
  } as SourceIM);
  return list;
}

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
    colProps: { span: 12 },
  },
  {
    label: '表描述',
    field: 'comment',
    component: 'Input',
    colProps: { span: 12 },
  },
  {
    label: '所属数据源',
    field: 'sourceName',
    component: 'ApiSelect',
    componentProps: {
      api: sourceList,
      showSearch: true,
      optionFilterProp: 'label',
      resultField: 'items',
      labelField: 'name',
      valueField: 'slave',
    },
    colProps: { span: 12 },
  },
];
