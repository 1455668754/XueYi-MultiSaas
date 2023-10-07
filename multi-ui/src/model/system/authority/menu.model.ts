import { BasicFetchResult, BasicPageParams, TreeEntity } from '@/model';
import { FrameTypeEnum, MenuTypeEnum } from '@/enums/system/authority';
import { DicCommonPrivateEnum, DicShowHideEnum, DicStatusEnum, DicYesNoEnum } from '@/enums';
import { EnterpriseIM } from '@/model/system/organize';
import { ModuleIM } from '@/model/system/authority/module.model';

/** menu info model */
export interface MenuIM extends TreeEntity<MenuIM> {
  /** Id */
  id: string;
  /** 模块Id */
  moduleId: string;
  /** 菜单唯一名称 */
  name: string;
  /** 菜单标题 */
  title: string;
  /** 路由名称 */
  path: string;
  /** 外链路径 */
  frameSrc: string;
  /** 组件路径 */
  component: string;
  /** 路由参数 */
  paramPath: string;
  /** 路由切换动画 */
  transitionName: string;
  /** 是否忽略路由（Y是 N否） */
  ignoreRoute: DicYesNoEnum;
  /** 是否缓存（Y是 N否） */
  isCache: DicYesNoEnum;
  /** 是否固定标签（Y是 N否） */
  isAffix: DicYesNoEnum;
  /** 是否禁用（Y是 N否） */
  isDisabled: DicYesNoEnum;
  /** 页面类型（0常规 1内嵌 2外链） */
  frameType: FrameTypeEnum;
  /** 菜单类型（M目录 C菜单 X详情 F按钮） */
  menuType: MenuTypeEnum;
  /** 标签显隐状态（Y隐藏 N显示） */
  hideTab: DicShowHideEnum;
  /** 菜单显隐状态（Y隐藏 N显示） */
  hideMenu: DicShowHideEnum;
  /** 面包屑路由显隐状态（Y隐藏 N显示） */
  hideBreadcrumb: DicShowHideEnum;
  /** 子菜单显隐状态（Y隐藏 N显示） */
  hideChildren: DicShowHideEnum;
  /** 是否在子级菜单的完整path中忽略本级path（Y隐藏 N显示） */
  hidePathForChildren: DicShowHideEnum;
  /** 详情页可打开Tab页数 */
  dynamicLevel: number;
  /** 详情页的实际Path */
  realPath: string;
  /** 详情页激活的菜单 */
  currentActiveMenu: string;
  /** 权限标识 */
  perms: string;
  /** 菜单图标 */
  icon: string;
  /** 状态（0启用 1禁用） */
  status: DicStatusEnum;
  /** 公共数据（Y是 N否） */
  isCommon: DicCommonPrivateEnum;
  /** 默认模块（Y是 N否） */
  isDefault: DicYesNoEnum;
  /** 企业Id */
  tenantId?: string;
  /** 企业信息 */
  enterpriseInfo?: EnterpriseIM;
  /** 模块信息 */
  module: ModuleIM;
}

/** menu list model */
export type MenuLM = MenuIM[];

/** menu param model */
export interface MenuPM extends TreeEntity<MenuIM> {
  /** Id */
  id?: string;
  /** 模块Id */
  moduleId?: string;
  /** 菜单唯一名称 */
  name?: string;
  /** 菜单标题 */
  title?: string;
  /** 是否忽略路由（Y是 N否） */
  ignoreRoute?: DicYesNoEnum;
  /** 是否缓存（Y是 N否） */
  isCache?: DicYesNoEnum;
  /** 是否固定标签（Y是 N否） */
  isAffix?: DicYesNoEnum;
  /** 是否禁用（Y是 N否） */
  isDisabled?: DicYesNoEnum;
  /** 页面类型（0常规 1内嵌 2外链） */
  frameType?: FrameTypeEnum;
  /** 菜单类型（M目录 C菜单 X详情 F按钮） */
  menuType?: MenuTypeEnum;
  /** 标签显隐状态（Y隐藏 N显示） */
  hideTab?: DicShowHideEnum;
  /** 菜单显隐状态（Y隐藏 N显示） */
  hideMenu?: DicShowHideEnum;
  /** 面包屑路由显隐状态（Y隐藏 N显示） */
  hideBreadcrumb?: DicShowHideEnum;
  /** 子菜单显隐状态（Y隐藏 N显示） */
  hideChildren?: DicShowHideEnum;
  /** 是否在子级菜单的完整path中忽略本级path（Y隐藏 N显示） */
  hidePathForChildren?: DicShowHideEnum;
  /** 状态（0启用 1禁用） */
  status?: DicStatusEnum;
  /** 公共数据（Y是 N否） */
  isCommon?: DicCommonPrivateEnum;
  /** 默认模块（Y是 N否） */
  isDefault?: DicYesNoEnum;
  /** 企业Id */
  tenantId?: string;
  /** 菜单类型限制（M目录 C菜单 X详情 F按钮） */
  menuTypeLimit?: MenuTypeEnum;
}

/** menu page param model */
export type MenuPPM = BasicPageParams & MenuPM;

/** menu list result model */
export type MenuLRM = BasicFetchResult<MenuIM>;
