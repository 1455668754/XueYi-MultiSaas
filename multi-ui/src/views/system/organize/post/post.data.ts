import { FormSchema } from '@/components/Form';
import { BasicColumn } from '@/components/Table';
import { DescItem } from '@/components/Description';
import { dicDictList } from '@/api/sys/dict.api';
import { dictConversion } from '@/utils/xueyi';
import { DicSortEnum, DicStatusEnum } from '@/enums';
import { PostIM } from '@/model/system/organize';
import { listDeptApi } from '@/api/system/organize/dept.api';

/** 字典查询 */
export const dictMap = await dicDictList(['sys_normal_disable']);

/** 字典表 */
export const dict: any = {
  DicNormalDisableOptions: dictMap['sys_normal_disable'],
};

/** 表格数据 */
export const columns: BasicColumn[] = [
  {
    title: '岗位名称',
    dataIndex: 'name',
    width: 220,
  },
  {
    title: '岗位编码',
    dataIndex: 'code',
    width: 220,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 220,
    customRender: ({ record }) => {
      const data = record as PostIM;
      return dictConversion(dict.DicNormalDisableOptions, data.status);
    },
  },
];

/** 查询数据 */
export const searchFormSchema: FormSchema[] = [
  {
    label: '部门Id',
    field: 'deptId',
    show: false,
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '岗位编码',
    field: 'code',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '岗位名称',
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
    label: '岗位Id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '归属部门',
    field: 'deptId',
    component: 'ApiTreeSelect',
    componentProps: {
      showSearch: true,
      api: listDeptApi,
      treeNodeFilterProp: 'name',
      labelField: 'name',
      valueField: 'id',
      getPopupContainer: () => document.body,
    },
    required: true,
    colProps: { span: 24 },
  },
  {
    label: '岗位名称',
    field: 'name',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '岗位编码',
    field: 'code',
    component: 'Input',
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

/** 表单数据 - 角色分配 */
export const roleFormSchema: FormSchema[] = [
  {
    label: '岗位Id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '岗位名称',
    field: 'name',
    component: 'Input',
    dynamicDisabled: true,
    colProps: { span: 24 },
  },
  {
    label: '角色分配',
    field: 'roleIds',
    slot: 'role',
    component: 'Input',
  },
];

/** 详情数据 */
export const detailSchema: DescItem[] = [
  {
    label: '部门Id',
    field: 'deptId',
    span: 8,
  },
  {
    label: '岗位编码',
    field: 'code',
    span: 8,
  },
  {
    label: '岗位名称',
    field: 'name',
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
