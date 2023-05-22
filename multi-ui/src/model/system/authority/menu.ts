import { BasicFetchResult, BasicPageParams, TreeEntity } from '/@/model/src';

/** menu info model */
export interface MenuIM extends TreeEntity<MenuIM> {
  id: string | number;
  moduleId: string | number;
  name: string;
  title: string;
  path: string;
  frameSrc: string;
  component: string;
  paramPath: string;
  transitionName: string;
  ignoreRoute: string;
  isCache: string;
  isAffix: string;
  isDisabled: string;
  frameType: string;
  menuType: string;
  hideTab: string;
  hideMenu: string;
  hideBreadcrumb: string;
  hideChildren: string;
  hidePathForChildren: string;
  dynamicLevel: number;
  realPath: string;
  currentActiveMenu: string;
  perms: string;
  icon: string;
  sort: number;
  status: string;
  remark: string;
  isCommon: string;
  isDefault: string;
}

/** menu list model */
export type MenuLM = MenuIM[];

/** menu param model */
export type MenuPM = MenuIM;

/** menu page param model */
export type MenuPPM = BasicPageParams & MenuPM;

/** menu list result model */
export type MenuLRM = BasicFetchResult<MenuIM>;
