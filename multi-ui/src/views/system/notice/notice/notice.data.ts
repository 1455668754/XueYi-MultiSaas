import { FormSchema } from '/@/components/Form';
import { BasicColumn } from '/@/components/Table';
import { DescItem } from '/@/components/Description';
import { dicDictList } from '/@/api/sys/dict';
import { h } from 'vue';
import { Tinymce } from '/@/components/Tinymce';
import { NoticeIM } from '/@/model/system';
import { NoticeTypeEnum } from '/@/enums/system';
import { dictConversion } from '/@/utils/xueyi';
import { isEmpty } from '/@/utils/is';

/** 字典查询 */
export const dictMap = await dicDictList(['sys_notice_type', 'sys_notice_status']);

/** 字典表 */
export const dict: any = {
  DicNoticeTypeOptions: dictMap['sys_notice_type'],
  DicNoticeStatusOptions: dictMap['sys_notice_status'],
};

/** 表格数据 */
export const columns: BasicColumn[] = [
  {
    title: '公告标题',
    dataIndex: 'name',
    width: 220,
  },
  {
    title: '公告类型',
    dataIndex: 'type',
    width: 220,
    customRender: ({ record }) => {
      const data = record as NoticeIM;
      return dictConversion(dict.DicNoticeTypeOptions, data.type);
    },
  },
  {
    title: '公告状态',
    dataIndex: 'status',
    width: 220,
    customRender: ({ record }) => {
      const data = record as NoticeIM;
      return dictConversion(dict.DicNoticeStatusOptions, data.status);
    },
  },
  {
    title: '备注',
    dataIndex: 'remark',
    width: 220,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 220,
  },
];

/** 查询数据 */
export const searchFormSchema: FormSchema[] = [
  {
    label: '公告标题',
    field: 'name',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '公告类型',
    field: 'type',
    component: 'Select',
    componentProps: {
      options: dict.DicNoticeTypeOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    colProps: { span: 6 },
  },
  {
    label: '公告状态',
    field: 'status',
    component: 'Select',
    componentProps: {
      options: dict.DicNoticeStatusOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    colProps: { span: 6 },
  },
];

/** 表单数据 */
export const formSchema: FormSchema[] = [
  {
    label: '公告Id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '公告标题',
    field: 'name',
    component: 'Input',
    required: true,
    colProps: { span: 24 },
  },
  {
    label: '公告类型',
    field: 'type',
    component: 'RadioButtonGroup',
    defaultValue: NoticeTypeEnum.NOTIFY,
    componentProps: {
      options: dict.DicNoticeTypeOptions,
    },
    colProps: { span: 24 },
  },
  {
    label: '公告内容',
    field: 'content',
    component: 'Input',
    render: ({ model, field }) => {
      return h(Tinymce, {
        value: model[field],
        onChange: (value: string) => {
          model[field] = value;
        },
      });
    },
    colProps: { span: 24 },
  },
  {
    label: '公告状态',
    field: 'status',
    component: 'Select',
    componentProps: {
      options: dict.DicNoticeStatusOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    dynamicDisabled: true,
    ifShow: ({ values }) => !isEmpty(values.id),
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
    label: '公告标题',
    field: 'name',
    span: 8,
  },
  {
    label: '公告类型',
    field: 'type',
    render: (val) => {
      return dictConversion(dict.DicNoticeTypeOptions, val);
    },
    span: 8,
  },
  {
    label: '公告内容',
    field: 'content',
    span: 8,
  },
  {
    label: '公告状态',
    field: 'status',
    render: (val) => {
      return dictConversion(dict.DicNoticeStatusOptions, val);
    },
    span: 8,
  },
  {
    label: '备注',
    field: 'remark',
    span: 8,
  },
];
