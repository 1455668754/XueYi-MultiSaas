import { FormSchema } from '@/components/Form';
import { BasicColumn } from '@/components/Table';
import { DescItem } from '@/components/Description';
import { dicConfigList, dicDictList } from '@/api/sys/dict.api';
import { ConfigCodeEnum, DicCodeEnum, DicSortEnum, DicStatusEnum, DicYesNoEnum } from '@/enums';
import { RoleIM } from '@/model/system/authority';
import { dictConversion } from '@/utils/xueyi';
import { DataScopeEnum, DicCodeRoleEnum } from '@/enums/system/authority';
import { isEmpty } from '@/utils/core/ObjectUtil';
import { OrganizeIM, OrganizeLM } from '@/model/system/organize';
import { OrganizeTypeEnum } from '@/enums/system/organize';

/** 字典查询 */
export const dictMap = await dicDictList([
  DicCodeEnum.SYS_NORMAL_DISABLE,
  DicCodeRoleEnum.AUTH_DATA_SCOPE,
]);

/** 字典表 */
export const dict: any = {
  DicNormalDisableOptions: dictMap[DicCodeEnum.SYS_NORMAL_DISABLE],
  DicAuthDataScopeOptions: dictMap[DicCodeRoleEnum.AUTH_DATA_SCOPE],
};

/** 参数查询 */
export const configMap = await dicConfigList([
  ConfigCodeEnum.SYS_CODE_SHOW,
  ConfigCodeEnum.SYS_CODE_MUST,
]);

/** 参数表 */
export const config: any = {
  ConfigCodeShow: configMap[ConfigCodeEnum.SYS_CODE_SHOW],
  ConfigCodeMust: configMap[ConfigCodeEnum.SYS_CODE_MUST],
};

/** 角色新增分页数据 */
export const roleInitList = [
  { key: 'role', title: '角色信息', current: 0 },
  { key: 'auth', title: '功能权限', current: 1 },
  { key: 'organize', title: '数据权限', current: 2 },
];

/** 获取组织树中部门节点Ids */
export function getOrgDeptIds(orgTree: OrganizeLM) {
  // 递归函数获取指定层级的所有节点
  function getDeptNodes(treeNode: OrganizeIM): string[] {
    let nodes: string[] = treeNode?.type === OrganizeTypeEnum.DEPT ? [treeNode?.id] : [];
    if (treeNode?.children && treeNode?.children.length > 0) {
      for (const child of treeNode.children) {
        const subNodes = getDeptNodes(child);
        nodes = nodes.concat(subNodes);
      }
    }
    return nodes;
  }

  if (isEmpty(orgTree) || (orgTree as any[]).length === 0) {
    return [];
  }
  let nodes: string[] = [];
  for (const treeNode of orgTree as any[]) {
    const subNodes = getDeptNodes(treeNode);
    nodes = nodes.concat(subNodes);
  }
  return nodes;
}

/** 表格数据 */
export const columns: BasicColumn[] = [
  {
    title: '角色编码',
    dataIndex: 'code',
    ifShow: () => DicYesNoEnum.YES === config.ConfigCodeShow,
    width: 220,
  },
  {
    title: '角色名称',
    dataIndex: 'name',
    width: 220,
  },
  {
    title: '数据范围',
    dataIndex: 'dataScope',
    width: 220,
    customRender: ({ record }) => {
      const data = record as RoleIM;
      return dictConversion(dict.DicAuthDataScopeOptions, data.dataScope);
    },
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 220,
    customRender: ({ record }) => {
      const data = record as RoleIM;
      return dictConversion(dict.DicNormalDisableOptions, data.status);
    },
  },
];

/** 查询数据 */
export const searchFormSchema: FormSchema[] = [
  {
    label: '角色编码',
    field: 'code',
    component: 'Input',
    ifShow: () => DicYesNoEnum.YES === config.ConfigCodeShow,
    colProps: { span: 6 },
  },
  {
    label: '角色名称',
    field: 'name',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '数据范围',
    field: 'dataScope',
    component: 'Select',
    componentProps: {
      options: dict.DicAuthDataScopeOptions,
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
      options: dict.DicNormalDisableOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    colProps: { span: 6 },
  },
];

/** 表单数据 */
export const roleFormSchema: FormSchema[] = [
  {
    label: '角色Id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '角色编码',
    field: 'code',
    component: 'Input',
    ifShow: () => DicYesNoEnum.YES === config.ConfigCodeShow,
    required: () =>
      DicYesNoEnum.YES === config.ConfigCodeShow && DicYesNoEnum.YES === config.ConfigCodeMust,
    colProps: { span: 12 },
  },
  {
    label: '角色名称',
    field: 'name',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '权限字符串',
    field: 'roleKey',
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

/** 表单数据 - 功能权限 */
export const authFormSchema: FormSchema[] = [
  {
    label: '角色Id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '角色编码',
    field: 'code',
    component: 'Input',
    ifShow: () => DicYesNoEnum.YES === config.ConfigCodeShow,
    required: () =>
      DicYesNoEnum.YES === config.ConfigCodeShow && DicYesNoEnum.YES === config.ConfigCodeMust,
    dynamicDisabled: true,
    colProps: { span: 12 },
  },
  {
    label: '角色名称',
    field: 'name',
    component: 'Input',
    dynamicDisabled: true,
    colProps: { span: 12 },
  },
  {
    label: ' ',
    field: 'authIds',
    slot: 'auth',
    component: 'Input',
    colProps: { span: 24 },
  },
];

/** 表单数据 - 数据权限 */
export const organizeFormSchema: FormSchema[] = [
  {
    label: '角色Id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '角色编码',
    field: 'code',
    component: 'Input',
    ifShow: () => DicYesNoEnum.YES === config.ConfigCodeShow,
    required: () =>
      DicYesNoEnum.YES === config.ConfigCodeShow && DicYesNoEnum.YES === config.ConfigCodeMust,
    dynamicDisabled: true,
    colProps: { span: 12 },
  },
  {
    label: '角色名称',
    field: 'name',
    component: 'Input',
    dynamicDisabled: true,
    colProps: { span: 12 },
  },
  {
    label: '数据范围',
    field: 'dataScope',
    component: 'Select',
    componentProps: {
      options: dict.DicAuthDataScopeOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    label: ' ',
    field: 'organizeIds',
    slot: 'organize',
    component: 'Input',
    show: ({ values }) => values.dataScope == DataScopeEnum.CUSTOM,
    colProps: { span: 24 },
  },
];

/** 详情数据 */
export const detailSchema: DescItem[] = [
  {
    label: '角色编码',
    field: 'code',
    show: () => DicYesNoEnum.YES === config.ConfigCodeShow,
    span: 8,
  },
  {
    label: '角色名称',
    field: 'name',
    span: 8,
  },
  {
    label: '权限字符串',
    field: 'roleKey',
    span: 8,
  },
  {
    label: '数据范围',
    field: 'dataScope',
    render: (val) => {
      return dictConversion(dict.DicAuthDataScopeOptions, val);
    },
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
    label: '显示顺序',
    field: 'sort',
    span: 8,
  },
  {
    label: '备注',
    field: 'remark',
    span: 24,
  },
];
