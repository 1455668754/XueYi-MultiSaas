import { FormSchema } from '/@/components/Form';
import { BasicColumn } from '/@/components/Table';
import { DescItem } from '/@/components/Description';
import { dicDictList } from '@/api/sys/dict.api';
import { DicSortEnum, DicStatusEnum } from '@/enums/basic';
import { UserIM } from '@/model/system';
import { DefaultPassword, OrganizeTypeEnum, SexEnum } from '@/enums/system';
import { organizeOptionApi } from '@/api/system/organize/organize.api';
import { isEmpty, isEqual, pull } from 'lodash-es';
import { dictConversion } from '/@/utils/xueyi';
import { isNotEmpty } from '@/utils/is';

/** 字典查询 */
export const dictMap = await dicDictList(['sys_normal_disable', 'sys_user_sex']);

/** 字典表 */
export const dict: any = {
  DicNormalDisableOptions: dictMap['sys_normal_disable'],
  DicUserSexOptions: dictMap['sys_user_sex'],
};

/** 表格数据 */
export const columns: BasicColumn[] = [
  {
    title: '用户编码',
    dataIndex: 'code',
    width: 220,
  },
  {
    title: '用户账号',
    dataIndex: 'userName',
    width: 220,
  },
  {
    title: '用户昵称',
    dataIndex: 'nickName',
    width: 220,
  },
  {
    title: '用户性别',
    dataIndex: 'sex',
    width: 220,
    customRender: ({ record }) => {
      const data = record as UserIM;
      return dictConversion(dict.DicUserSexOptions, data.sex);
    },
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 220,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 220,
    customRender: ({ record }) => {
      const data = record as UserIM;
      return dictConversion(dict.DicNormalDisableOptions, data.status);
    },
  },
];

/** 查询数据 */
export const searchFormSchema: FormSchema[] = [
  {
    label: '用户编码',
    field: 'code',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '用户账号',
    field: 'userName',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '用户昵称',
    field: 'nickName',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '手机号码',
    field: 'phone',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '用户性别',
    field: 'sex',
    component: 'Select',
    componentProps: {
      options: dict.DicUserSexOptions,
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
export const formSchema: FormSchema[] = [
  {
    label: '用户Id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '归属岗位',
    field: 'postIds',
    component: 'ApiTreeSelect',
    componentProps: ({ formModel }) => {
      return {
        api: organizeOptionApi,
        showSearch: true,
        multiple: true,
        treeNodeFilterProp: 'label',
        fieldNames: {
          label: 'label',
          key: 'id',
          value: 'id',
        },
        onSelect: (value: string, node: any) => {
          if (formModel !== undefined && isEqual(node?.type, OrganizeTypeEnum.DEPT)) {
            pull(formModel.postIds, value);
          }
        },
        getPopupContainer: () => document.body,
      };
    },
    required: true,
    colProps: { span: 24 },
  },
  {
    label: '用户账号',
    field: 'userName',
    component: 'Input',
    dynamicDisabled: ({ values }) => isNotEmpty(values.id),
    required: true,
    colProps: { span: 24 },
  },
  {
    label: '用户编码',
    field: 'code',
    component: 'Input',
    colProps: { span: 12 },
  },
  {
    label: '用户昵称',
    field: 'nickName',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '密码',
    field: 'password',
    defaultValue: DefaultPassword,
    component: 'InputPassword',
    helpMessage: ['用户登录密码，默认为' + DefaultPassword],
    required: ({ values }) => isEmpty(values.id),
    ifShow: ({ values }) => isEmpty(values.id),
    colProps: { span: 24 },
  },
  {
    label: '手机号码',
    field: 'phone',
    component: 'Input',
    rules: [
      {
        pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
        message: '请输入正确的手机号码',
        trigger: 'blur',
      },
    ],
    colProps: { span: 12 },
  },
  {
    label: '用户邮箱',
    field: 'email',
    component: 'Input',
    rules: [
      {
        type: 'email',
        message: '请输入正确的邮箱地址',
        trigger: ['change', 'blur'],
      },
    ],
    colProps: { span: 12 },
  },
  {
    label: '用户性别',
    field: 'sex',
    component: 'Select',
    defaultValue: SexEnum.UNKNOWN,
    componentProps: {
      options: dict.DicUserSexOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
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

/** 表单数据 - 角色分配 */
export const roleFormSchema: FormSchema[] = [
  {
    label: '用户Id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '用户名称',
    field: 'nickName',
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

/** 表单数据 - 重置密码 */
export const resPwdFormSchema: FormSchema[] = [
  {
    label: '用户Id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '用户账号',
    field: 'userName',
    component: 'Input',
    dynamicDisabled: true,
    colProps: { span: 24 },
  },
  {
    label: '用户编码',
    field: 'code',
    component: 'Input',
    dynamicDisabled: true,
    colProps: { span: 12 },
  },
  {
    label: '用户昵称',
    field: 'nickName',
    component: 'Input',
    dynamicDisabled: true,
    colProps: { span: 12 },
  },
  {
    label: '密码',
    field: 'password',
    component: 'InputPassword',
    required: true,
    colProps: { span: 24 },
  },
];

/** 详情数据 */
export const detailSchema: DescItem[] = [
  {
    label: '用户编码',
    field: 'code',
    span: 8,
  },
  {
    label: '用户账号',
    field: 'userName',
    span: 8,
  },
  {
    label: '用户昵称',
    field: 'nickName',
    span: 8,
  },
  {
    label: '用户类型',
    field: 'userType',
    span: 8,
  },
  {
    label: '手机号码',
    field: 'phone',
    span: 8,
  },
  {
    label: '用户邮箱',
    field: 'email',
    span: 8,
  },
  {
    label: '用户性别',
    field: 'sex',
    render: (val) => {
      return dictConversion(dict.DicUserSexOptions, val);
    },
    span: 8,
  },
  {
    label: '头像地址',
    field: 'avatar',
    span: 8,
  },
  {
    label: '个人简介',
    field: 'profile',
    span: 8,
  },
  {
    label: '密码',
    field: 'password',
    span: 8,
  },
  {
    label: '最后登录IP',
    field: 'loginIp',
    span: 8,
  },
  {
    label: '最后登录时间',
    field: 'loginDate',
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
