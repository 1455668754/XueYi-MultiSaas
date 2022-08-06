import { FormSchema } from '/@/components/Form';
import { BasicColumn } from '/@/components/Table';
import { dicDictList } from '/@/api/sys/dict';
import { LoginLogIM } from '/@/model/system';
import { dictConversion } from '/@/utils/xueyi';

/** 字典查询 */
export const dictMap = await dicDictList(['sys_message_status']);

/** 字典表 */
export const dict: any = {
  DicMessageStatusOptions: dictMap['sys_message_status'],
};

/** 表格数据 */
export const columns: BasicColumn[] = [
  {
    title: '用户账号',
    dataIndex: 'userName',
    width: 180,
  },
  {
    title: '用户名称',
    dataIndex: 'userNick',
    width: 180,
  },
  {
    title: '登录IP地址',
    dataIndex: 'ipaddr',
    width: 180,
  },
  {
    title: '登录状态',
    dataIndex: 'status',
    width: 180,
    customRender: ({ record }) => {
      const data = record as LoginLogIM;
      return dictConversion(dict.DicMessageStatusOptions, data.status);
    },
  },
  {
    title: '提示信息',
    dataIndex: 'msg',
    width: 180,
  },
  {
    title: '访问时间',
    dataIndex: 'accessTime',
    width: 200,
  },
];

/** 查询数据 */
export const searchFormSchema: FormSchema[] = [
  {
    label: '用户账号',
    field: 'userName',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '用户名称',
    field: 'userName',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '登录IP地址',
    field: 'ipaddr',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '登录状态',
    field: 'status',
    component: 'Select',
    componentProps: {
      options: dict.DicMessageStatusOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    colProps: { span: 6 },
  },
  {
    label: '访问时间',
    field: 'accessTime',
    component: 'RangePicker',
    colProps: { span: 6 },
  },
];
