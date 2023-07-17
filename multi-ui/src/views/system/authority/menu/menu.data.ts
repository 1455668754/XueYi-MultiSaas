import { FormSchema } from '/@/components/Form';
import { BasicColumn } from '/@/components/Table';
import { DescItem } from '/@/components/Description';
import { dicDictList } from '@/api/sys/dict.api';
import {
  DicCommonPrivateEnum,
  DicShowHideEnum,
  DicSortEnum,
  DicStatusEnum,
  DicYesNoEnum,
} from '@/enums/basic';
import { MenuIM } from '@/model/system';
import { optionModuleApi } from '@/api/system/authority/module.api';
import { getMenuRouteListApi, getMenuRouteListExNodesApi } from '@/api/system/authority/menu.api';
import { COMMON_MENU, COMMON_MODULE, FrameTypeEnum, MenuTypeEnum } from '@/enums/system';
import { h } from 'vue';
import Icon from '@/components/Icon/Icon.vue';
import { Tag } from 'ant-design-vue';
import { useUserStore } from '/@/store/modules/user';
import { ColorEnum } from '@/enums';
import { dictConversion } from '/@/utils/xueyi';
import { isEmpty, isNil } from 'lodash-es';

/** 字典查询 */
export const dictMap = await dicDictList([
  'auth_frame_type',
  'sys_yes_no',
  'sys_show_hide',
  'auth_menu_type',
  'sys_normal_disable',
  'sys_common_private',
]);

/** 字典表 */
export const dict: any = {
  DicAuthFrameTypeOptions: dictMap['auth_frame_type'],
  DicYesNoOptions: dictMap['sys_yes_no'],
  DicShowHideOptions: dictMap['sys_show_hide'],
  DicAuthMenuTypeOptions: dictMap['auth_menu_type'],
  DicNormalDisableOptions: dictMap['sys_normal_disable'],
  DicCommonPrivateOptions: dictMap['sys_common_private'],
};

/** 菜单类型校验 */
export const isDir = (menuType: string) => menuType == MenuTypeEnum.DIR;
export const isMenu = (menuType: string) => menuType == MenuTypeEnum.MENU;
export const isDetails = (menuType: string) => menuType == MenuTypeEnum.DETAILS;
export const isButton = (menuType: string) => menuType == MenuTypeEnum.BUTTON;

/** 页面类型校验 */
export const isNormalMenu = (menuType: string, frameType: string) =>
  isMenu(menuType) && frameType == FrameTypeEnum.NORMAL;
export const isEmbeddedMenu = (menuType: string, frameType: string) =>
  isMenu(menuType) && frameType == FrameTypeEnum.EMBEDDED;
export const isExternalLinksMenu = (menuType: string, frameType: string) =>
  isMenu(menuType) && frameType == FrameTypeEnum.EXTERNAL_LINKS;

/** 生成提交参数 */
export function initialize(data: MenuIM) {
  const menu = {} as MenuIM;
  menu.id = data.id;
  menu.name = data.name;
  menu.title = data.title;
  menu.moduleId = data.moduleId;
  menu.parentId = data.parentId;
  menu.menuType = data.menuType;
  menu.frameType = FrameTypeEnum.NORMAL;
  menu.sort = data.sort;
  menu.isCommon = data.isCommon;
  menu.status = data.status;
  switch (data.menuType) {
    case MenuTypeEnum.DIR:
      menu.path = data.path;
      menu.icon = data.icon;
      menu.hideMenu = data.hideMenu;
      break;
    case MenuTypeEnum.MENU:
      menu.frameType = data.frameType;
      menu.icon = data.icon;
      menu.paramPath = data.paramPath;
      menu.perms = data.perms;
      if (isExternalLinksMenu(data.menuType, data.frameType)) {
        menu.frameSrc = data.frameSrc;
      } else {
        menu.path = data.path;
        menu.isCache = data.isCache;
        menu.isAffix = data.isAffix;
        menu.hideTab = data.hideTab;
        menu.hideMenu = data.hideMenu;
        if (isNormalMenu(data.menuType, data.frameType)) {
          menu.component = data.component;
        } else if (isEmbeddedMenu(data.menuType, data.frameType)) {
          menu.frameSrc = data.frameSrc;
        }
      }
      break;
    case MenuTypeEnum.DETAILS:
      menu.path = data.path;
      menu.component = data.component;
      menu.paramPath = data.paramPath;
      menu.perms = data.perms;
      menu.hideTab = data.hideTab;
      menu.hideMenu = DicShowHideEnum.HIDE;
      break;
    case MenuTypeEnum.BUTTON:
      menu.perms = data.perms;
      break;
  }
  return menu;
}

/** 表格数据 */
export const columns: BasicColumn[] = [
  {
    title: '菜单标题',
    dataIndex: 'title',
    width: 220,
    align: 'left',
  },
  {
    title: '菜单图标',
    dataIndex: 'icon',
    width: 220,
    customRender: ({ record }) => {
      const data = record as MenuIM;
      return h(Icon, { icon: data.icon });
    },
  },
  {
    title: '显示顺序',
    dataIndex: 'sort',
    width: 220,
  },
  {
    title: '菜单类型',
    dataIndex: 'menuType',
    width: 220,
    customRender: ({ record }) => {
      const data = record as MenuIM;
      return dictConversion(dict.DicAuthMenuTypeOptions, data.menuType);
    },
  },
  {
    title: '权限标识',
    dataIndex: 'perms',
    width: 220,
    customRender: ({ record }) => {
      const data = record as MenuIM;
      return !data.perms ? data.perms : h(Tag, { color: ColorEnum.ORANGE }, () => data.perms);
    },
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 220,
    customRender: ({ record }) => {
      const data = record as MenuIM;
      return dictConversion(dict.DicNormalDisableOptions, data.status);
    },
  },
  {
    title: '公共菜单',
    dataIndex: 'isCommon',
    width: 220,
    customRender: ({ record }) => {
      const data = record as MenuIM;
      return dictConversion(dict.DicCommonPrivateOptions, data.isCommon);
    },
  },
];

/** 查询数据 */
export const searchFormSchema: FormSchema[] = [
  {
    label: '模块',
    field: 'moduleId',
    component: 'ApiSelect',
    componentProps: {
      api: optionModuleApi,
      showSearch: true,
      optionFilterProp: 'label',
      resultField: 'items',
      labelField: 'name',
      valueField: 'id',
    },
    colProps: { span: 6 },
  },
  {
    label: '菜单标题',
    field: 'title',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '菜单类型',
    field: 'menuType',
    component: 'Select',
    componentProps: {
      options: dict.DicAuthMenuTypeOptions,
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
  {
    label: '公共菜单',
    field: 'isCommon',
    component: 'Select',
    componentProps: {
      options: dict.DicCommonPrivateOptions,
      showSearch: true,
      optionFilterProp: 'label',
    },
    colProps: { span: 6 },
  },
];

/** 表单数据 */
export const formSchema: FormSchema[] = [
  {
    label: '菜单Id',
    field: 'id',
    component: 'Input',
    show: false,
    colProps: { span: 12 },
  },
  {
    label: '模块',
    field: 'moduleId',
    component: 'ApiSelect',
    defaultValue: COMMON_MODULE,
    componentProps: ({ formModel, formActionType }) => {
      return {
        api: optionModuleApi,
        showSearch: true,
        optionFilterProp: 'label',
        resultField: 'items',
        labelField: 'name',
        valueField: 'id',
        onChange: async (e: any) => {
          if (formModel.moduleId !== e) {
            const treeData =
              e === undefined
                ? []
                : formModel.id === undefined
                ? await getMenuRouteListApi(e, MenuTypeEnum.DETAILS)
                : await getMenuRouteListExNodesApi(formModel.id, e, MenuTypeEnum.DETAILS);
            formModel.parentId = searchTree(treeData, formModel.parentId);
            const { updateSchema } = formActionType;
            updateSchema({
              field: 'parentId',
              componentProps: { treeData },
            });
          }

          function searchTree(nodes, searchKey) {
            for (let _i = 0; _i < nodes.length; _i++) {
              if (nodes[_i].id === searchKey) {
                return nodes[_i].id;
              } else {
                if (nodes[_i].children && nodes[_i].children.length > 0) {
                  const res = searchTree(nodes[_i].children, searchKey);
                  if (res) {
                    return res;
                  }
                }
              }
            }
            return undefined;
          }
        },
      };
    },
    required: true,
    colProps: { span: 24 },
  },
  {
    label: '父级菜单',
    field: 'parentId',
    component: 'TreeSelect',
    defaultValue: COMMON_MENU,
    componentProps: {
      showSearch: true,
      treeNodeFilterProp: 'title',
      fieldNames: {
        label: 'title',
        key: 'id',
        value: 'id',
      },
      getPopupContainer: () => document.body,
    },
    required: true,
    colProps: { span: 24 },
  },
  {
    label: '菜单名称',
    field: 'title',
    component: 'Input',
    helpMessage: ['显示的菜单名称，如：`用户管理`'],
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '显示顺序',
    field: 'sort',
    component: 'InputNumber',
    defaultValue: DicSortEnum.ZERO,
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '公共菜单',
    field: 'isCommon',
    component: 'RadioButtonGroup',
    defaultValue: DicCommonPrivateEnum.PRIVATE,
    componentProps: {
      options: dict.DicCommonPrivateOptions,
    },
    helpMessage: ['是否可以被其他租户使用'],
    dynamicDisabled: ({ values }) => !isNil(values.id) && !isEmpty(values.id),
    required: () => useUserStore().isLessor,
    ifShow: () => useUserStore().isLessor,
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
    label: '菜单类型',
    field: 'menuType',
    component: 'RadioButtonGroup',
    defaultValue: MenuTypeEnum.DIR,
    componentProps: {
      options: dict.DicAuthMenuTypeOptions,
    },
    required: true,
    colProps: { span: 24 },
  },
  {
    label: '页面类型',
    field: 'frameType',
    component: 'RadioButtonGroup',
    defaultValue: FrameTypeEnum.NORMAL,
    componentProps: {
      options: dict.DicAuthFrameTypeOptions,
    },
    required: ({ values }) => isMenu(values.menuType),
    ifShow: ({ values }) => isMenu(values.menuType),
    colProps: { span: 24 },
  },
  {
    label: '路由名称',
    field: 'path',
    component: 'Input',
    helpMessage: ['访问的路由，如：`user`'],
    required: ({ values }) =>
      !(isExternalLinksMenu(values.menuType, values.frameType) || isButton(values.menuType)),
    ifShow: ({ values }) =>
      !(isExternalLinksMenu(values.menuType, values.frameType) || isButton(values.menuType)),
    colProps: { span: 12 },
  },
  {
    label: '菜单图标',
    field: 'icon',
    component: 'IconPicker',
    ifShow: ({ values }) => isDir(values.menuType) || isMenu(values.menuType),
    colProps: { span: 12 },
  },
  {
    label: '外链路径',
    field: 'frameSrc',
    component: 'Input',
    helpMessage: ['访问的外网地址，以`http(s)://`开头'],
    ifShow: ({ values }) =>
      isEmbeddedMenu(values.menuType, values.frameType) ||
      isExternalLinksMenu(values.menuType, values.frameType),
    dynamicRules: ({ values }) =>
      isEmbeddedMenu(values.menuType, values.frameType) ||
      isExternalLinksMenu(values.menuType, values.frameType)
        ? [
            {
              required: true,
              message: '请输入外链路径',
            },
            {
              type: 'url',
              message: '请输入正确的外链路径',
              trigger: ['change', 'blur'],
            },
          ]
        : [],
    colProps: { span: 12 },
  },
  {
    label: '组件路径',
    field: 'component',
    component: 'Input',
    helpMessage: ['访问的组件路径，如：`system/user/index`，默认在`views`目录下'],
    required: ({ values }) =>
      isDetails(values.menuType) || isNormalMenu(values.menuType, values.frameType),
    ifShow: ({ values }) =>
      isDetails(values.menuType) || isNormalMenu(values.menuType, values.frameType),
    colProps: { span: 12 },
  },
  {
    label: '路由参数',
    field: 'paramPath',
    component: 'Input',
    helpMessage: ['访问菜单时传递的参数'],
    ifShow: ({ values }) => isDetails(values.menuType) || isMenu(values.menuType),
    colProps: { span: 12 },
  },
  {
    label: '权限标识',
    field: 'perms',
    component: 'Input',
    helpMessage: ["控制器中定义的权限字符，如：'system:user:list'"],
    required: ({ values }) =>
      !isDir(values.menuType) && !isExternalLinksMenu(values.menuType, values.frameType),
    ifShow: ({ values }) =>
      !isDir(values.menuType) && !isExternalLinksMenu(values.menuType, values.frameType),
    colProps: { span: 12 },
  },
  {
    label: '页面缓存',
    field: 'isCache',
    component: 'RadioButtonGroup',
    defaultValue: DicYesNoEnum.NO,
    componentProps: {
      options: dict.DicYesNoOptions,
    },
    helpMessage: ['选择是则该页面切换不会自动刷新'],
    required: ({ values }) =>
      (isMenu(values.menuType) || isDetails(values.menuType)) &&
      !isExternalLinksMenu(values.menuType, values.frameType),
    ifShow: ({ values }) =>
      (isMenu(values.menuType) || isDetails(values.menuType)) &&
      !isExternalLinksMenu(values.menuType, values.frameType),
    colProps: { span: 12 },
  },
  {
    label: '固定标签',
    field: 'isAffix',
    component: 'RadioButtonGroup',
    defaultValue: DicYesNoEnum.NO,
    componentProps: {
      options: dict.DicYesNoOptions,
    },
    helpMessage: ['选择是则该标签会始终固定在页签中'],
    required: ({ values }) =>
      (isMenu(values.menuType) || isDetails(values.menuType)) &&
      !isExternalLinksMenu(values.menuType, values.frameType),
    ifShow: ({ values }) =>
      (isMenu(values.menuType) || isDetails(values.menuType)) &&
      !isExternalLinksMenu(values.menuType, values.frameType),
    colProps: { span: 12 },
  },
  {
    label: '菜单状态',
    field: 'hideMenu',
    component: 'RadioButtonGroup',
    defaultValue: DicShowHideEnum.SHOW,
    componentProps: {
      options: dict.DicShowHideOptions,
    },
    helpMessage: ['选择隐藏则菜单将不会出现在侧边栏'],
    required: ({ values }) => isDir(values.menuType) || isMenu(values.menuType),
    ifShow: ({ values }) => isDir(values.menuType) || isMenu(values.menuType),
    colProps: { span: 12 },
  },
  {
    label: '标签状态',
    field: 'hideTab',
    component: 'RadioButtonGroup',
    defaultValue: DicShowHideEnum.SHOW,
    componentProps: {
      options: dict.DicShowHideOptions,
    },
    helpMessage: ['选择隐藏则打开该将页面不会出现'],
    required: ({ values }) =>
      (isMenu(values.menuType) || isDetails(values.menuType)) &&
      !isExternalLinksMenu(values.menuType, values.frameType),
    ifShow: ({ values }) =>
      (isMenu(values.menuType) || isDetails(values.menuType)) &&
      !isExternalLinksMenu(values.menuType, values.frameType),
    colProps: { span: 12 },
  },
];

/** 详情数据 */
export const detailSchema: DescItem[] = [
  {
    label: '父菜单Id',
    field: 'parentId',
    span: 8,
  },
  {
    label: '菜单标题',
    field: 'title',
    span: 8,
  },
  {
    label: '路由地址',
    field: 'path',
    span: 8,
  },
  {
    label: '外链地址',
    field: 'frameSrc',
    span: 8,
  },
  {
    label: '组件路径',
    field: 'component',
    span: 8,
  },
  {
    label: '路由参数',
    field: 'paramPath',
    span: 8,
  },
  {
    label: '路由切换动画',
    field: 'transitionName',
    span: 8,
  },
  {
    label: '是否忽略路由',
    field: 'ignoreRoute',
    render: (val) => dictConversion(dict.DicYesNoOptions, val),
    span: 8,
  },
  {
    label: '是否缓存',
    field: 'isCache',
    render: (val) => dictConversion(dict.DicYesNoOptions, val),
    span: 8,
  },
  {
    label: '是否固定标签',
    field: 'isAffix',
    render: (val) => dictConversion(dict.DicYesNoOptions, val),
    span: 8,
  },
  {
    label: '是否禁用',
    field: 'isDisabled',
    render: (val) => dictConversion(dict.DicYesNoOptions, val),
    span: 8,
  },
  {
    label: '页面类型',
    field: 'frameType',
    render: (val) => dictConversion(dict.DicAuthFrameTypeOptions, val),
    span: 8,
  },
  {
    label: '菜单类型',
    field: 'menuType',
    render: (val) => dictConversion(dict.DicAuthMenuTypeOptions, val),
    span: 8,
  },
  {
    label: '标签显隐状态',
    field: 'hideTab',
    render: (val) => dictConversion(dict.DicShowHideOptions, val),
    span: 8,
  },
  {
    label: '菜单显隐状态',
    field: 'hideMenu',
    render: (val) => dictConversion(dict.DicShowHideOptions, val),
    span: 8,
  },
  {
    label: '面包屑路由显隐状态',
    field: 'hideBreadcrumb',
    render: (val) => dictConversion(dict.DicShowHideOptions, val),
    span: 8,
  },
  {
    label: '子菜单显隐状态',
    field: 'hideChildren',
    render: (val) => dictConversion(dict.DicShowHideOptions, val),
    span: 8,
  },
  {
    label: '是否在子级菜单的完整path中忽略本级path',
    field: 'hidePathForChildren',
    render: (val) => dictConversion(dict.DicShowHideOptions, val),
    span: 8,
  },
  {
    label: '详情页可打开Tab页数',
    field: 'dynamicLevel',
    span: 8,
  },
  {
    label: '详情页的实际Path',
    field: 'realPath',
    span: 8,
  },
  {
    label: '详情页激活的菜单',
    field: 'currentActiveMenu',
    span: 8,
  },
  {
    label: '权限标识',
    field: 'perms',
    span: 8,
  },
  {
    label: '菜单图标',
    field: 'icon',
    render: (val) => h(Icon, { icon: val }),
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
    render: (val) => dictConversion(dict.DicNormalDisableOptions, val),
    span: 8,
  },
  {
    label: '公共菜单',
    field: 'isCommon',
    show: () => useUserStore().isLessor,
    render: (val) => dictConversion(dict.DicCommonPrivateOptions, val),
    span: 8,
  },
  {
    label: '归属模块',
    field: 'module.name',
    span: 8,
  },
  {
    label: '备注',
    field: 'remark',
    span: 24,
  },
];
