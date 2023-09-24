import { FormSchema } from '@/components/Form';
import { BasicColumn } from '@/components/Table';
import { OnlineIM } from '@/model/system/monitor';
import { parseTime } from '@/utils/xueyi';

/** 表格数据 */
export const columns: BasicColumn[] = [
  {
    title: '会话编号',
    dataIndex: 'tokenId',
    width: 220,
  },
  {
    title: '用户账号',
    dataIndex: 'userName',
    width: 220,
  },
  {
    title: '用户名称',
    dataIndex: 'userNick',
    width: 220,
  },
  {
    title: '登录IP地址',
    dataIndex: 'ipaddr',
    width: 220,
  },
  {
    title: '登录时间',
    dataIndex: 'loginTime',
    customRender: ({ record }) => {
      const data = record as OnlineIM;
      return parseTime(data.loginTime);
    },
    width: 220,
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
    label: '登录IP地址',
    field: 'ipaddr',
    component: 'Input',
    colProps: { span: 6 },
  },
];
