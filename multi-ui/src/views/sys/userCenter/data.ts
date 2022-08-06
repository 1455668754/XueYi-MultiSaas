import { FormSchema } from '/@/components/Form';
import { dicDictList } from '/@/api/sys/dict';
import { SexEnum } from '/@/enums/system';

export interface ListItem {
  key: string;
  code: string;
  title: string;
  description?: string;
  extra?: string;
  avatar?: string;
  color?: string;
}

/** 安全设置枚举 */
export enum SecureEnum {
  // 企业级
  USER_NAME = 'userName',
  // 部门级
  PASSWORD = 'password',
  // 岗位级
  PHONE = 'phone',
  // 用户级
  EMAIL = 'email',
}

/** 字典查询 */
export const dictMap = await dicDictList(['sys_user_sex']);

/** 字典表 */
export const dict: any = {
  DicUserSexOptions: dictMap['sys_user_sex'],
};

/** tab的list */
export const settingList = [
  {
    key: '1',
    name: '基本设置',
    component: 'BaseSetting',
  },
  {
    key: '2',
    name: '安全设置',
    component: 'SecureSetting',
  },
  {
    key: '3',
    name: '账号绑定',
    component: 'AccountBind',
  },
  {
    key: '4',
    name: '新消息通知',
    component: 'MsgNotify',
  },
];

/** 表单数据 - 基础设置 */
export const baseSettingSchemas: FormSchema[] = [
  {
    field: 'code',
    label: '用户编码',
    component: 'Input',
    dynamicDisabled: true,
    colProps: { span: 18 },
  },
  {
    field: 'nickName',
    label: '昵称',
    component: 'Input',
    colProps: { span: 18 },
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
    colProps: { span: 18 },
  },
  {
    field: 'profile',
    label: '个人简介',
    component: 'InputTextArea',
    colProps: { span: 18 },
  },
];

/** 表单数据 - 安全设置 */
export const secureSettingList: ListItem[] = [
  {
    key: '1',
    code: SecureEnum.USER_NAME,
    title: '账户账号',
    extra: '修改',
  },
  {
    key: '2',
    code: SecureEnum.PASSWORD,
    title: '账户密码',
    extra: '修改',
  },
  {
    key: '3',
    code: SecureEnum.PHONE,
    title: '密保手机',
    extra: '绑定',
  },
  {
    key: '4',
    code: SecureEnum.EMAIL,
    title: '备用邮箱',
    extra: '绑定',
  },
];

/** 表单数据 - 账号绑定 */
export const accountBindList: ListItem[] = [
  {
    key: '1',
    code: 'word',
    title: '绑定淘宝',
    description: '当前未绑定淘宝账号',
    extra: '绑定',
    avatar: 'ri:taobao-fill',
    color: '#ff4000',
  },
  {
    key: '2',
    code: 'word',
    title: '绑定支付宝',
    description: '当前未绑定支付宝账号',
    extra: '绑定',
    avatar: 'fa-brands:alipay',
    color: '#2eabff',
  },
  {
    key: '3',
    code: 'word',
    title: '绑定钉钉',
    description: '当前未绑定钉钉账号',
    extra: '绑定',
    avatar: 'ri:dingding-fill',
    color: '#2eabff',
  },
];

/** 表单数据 - 新消息通知 */
export const msgNotifyList: ListItem[] = [
  {
    key: '1',
    code: 'word',
    title: '账户密码',
    description: '其他用户的消息将以站内信的形式通知',
  },
  {
    key: '2',
    code: 'word',
    title: '系统消息',
    description: '系统消息将以站内信的形式通知',
  },
  {
    key: '3',
    code: 'word',
    title: '待办任务',
    description: '待办任务将以站内信的形式通知',
  },
];

/** 表单数据 - 安全设置 */
export const secureFormSchema: FormSchema[] = [
  {
    field: 'code',
    label: '表单类型',
    component: 'Input',
    show: false,
  },
  {
    field: 'userName',
    label: '用户账号',
    component: 'Input',
    required: ({ values }) => values.code === SecureEnum.USER_NAME,
    ifShow: ({ values }) => values.code === SecureEnum.USER_NAME,
    colProps: { span: 24 },
  },
  {
    field: 'phone',
    label: '密保手机',
    component: 'Input',
    ifShow: ({ values }) => values.code === SecureEnum.PHONE,
    dynamicRules: ({ values }) =>
      values.code === SecureEnum.PHONE
        ? [
            {
              required: true,
              message: '请输入手机号',
            },
            {
              pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
              message: '请输入正确的手机号码',
              trigger: 'blur',
            },
          ]
        : [],
    colProps: { span: 24 },
  },
  {
    field: 'email',
    label: '备用邮箱',
    component: 'Input',
    ifShow: ({ values }) => values.code === SecureEnum.EMAIL,
    dynamicRules: ({ values }) =>
      values.code === SecureEnum.EMAIL
        ? [
            {
              required: true,
              message: '请输入邮箱',
            },
            {
              type: 'email',
              message: '请输入正确的邮箱地址',
              trigger: ['change', 'blur'],
            },
          ]
        : [],
    colProps: { span: 24 },
  },
  {
    field: 'passwordOld',
    label: '当前密码',
    component: 'InputPassword',
    required: ({ values }) => values.code === SecureEnum.PASSWORD,
    ifShow: ({ values }) => values.code === SecureEnum.PASSWORD,
    colProps: { span: 24 },
  },
  {
    field: 'passwordNew',
    label: '新密码',
    component: 'StrengthMeter',
    componentProps: {
      placeholder: '新密码',
    },
    ifShow: ({ values }) => values.code === SecureEnum.PASSWORD,
    dynamicRules: ({ values }) =>
      values.code === SecureEnum.PASSWORD
        ? [
            {
              required: true,
              message: '请输入新密码',
            },
          ]
        : [],
    colProps: { span: 24 },
  },
  {
    field: 'confirmPassword',
    label: '确认密码',
    component: 'InputPassword',
    ifShow: ({ values }) => values.code === SecureEnum.PASSWORD,
    dynamicRules: ({ values }) =>
      values.code === SecureEnum.PASSWORD
        ? [
            {
              required: true,
              validator: (_, value) => {
                if (!value) {
                  return Promise.reject('不能为空');
                }
                if (value !== values.passwordNew) {
                  return Promise.reject('两次输入的密码不一致!');
                }
                return Promise.resolve();
              },
            },
          ]
        : [],
    colProps: { span: 24 },
  },
];
