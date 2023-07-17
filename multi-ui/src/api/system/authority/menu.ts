import { defHttp } from '/@/utils/http/axios';
import { MenuIM, MenuLM, MenuPM } from '/@/model/system';
import { MenuTypeEnum } from '/@/enums/system';

enum Api {
  LIST_MENU = '/system/admin/menu/list',
  LIST_MENU_EXCLUDE_NODES = '/system/admin/menu/list/exclude',
  LIST_MENU_BY_TYPE = '/system/admin/menu/routeList',
  LIST_MENU_BY_TYPE_EXCLUDE_NODES = '/system/admin/menu/routeList/exclude',
  GET_MENU = '/system/admin/menu/',
  ADD_MENU = '/system/admin/menu',
  EDIT_MENU = '/system/admin/menu',
  EDIT_STATUS_MENU = '/system/admin/menu/status',
  DEL_BATCH_MENU = '/system/admin/menu/batch/',
}

/** 查询菜单列表 */
export const listMenuApi = (params?: MenuPM) => defHttp.get<MenuLM>({ url: Api.LIST_MENU, params });

/** 查询菜单列表（排除节点） */
export const listMenuExNodesApi = (id: string | undefined, moduleId: string) =>
  defHttp.get<MenuLM>({
    url: Api.LIST_MENU_EXCLUDE_NODES,
    params: { id: id, moduleId: moduleId },
  });

/** 根据菜单类型获取指定模块的可配菜单集 */
export const getMenuRouteListApi = (moduleId: string, menuType: MenuTypeEnum) =>
  defHttp.post<MenuLM>({
    url: Api.LIST_MENU_BY_TYPE,
    params: { moduleId: moduleId, menuType: menuType },
  });

/** 根据菜单类型获取指定模块的可配菜单集（不包含自己及其子集） */
export const getMenuRouteListExNodesApi = (
  id: string | undefined,
  moduleId: string,
  menuType: MenuTypeEnum,
) =>
  defHttp.post<MenuLM>({
    url: Api.LIST_MENU_BY_TYPE_EXCLUDE_NODES,
    params: { id: id, moduleId: moduleId, menuType: menuType },
  });

/** 查询菜单详细 */
export const getMenuApi = (id: string) => defHttp.get<MenuIM>({ url: Api.GET_MENU, params: id });

/** 新增菜单 */
export const addMenuApi = (params: MenuIM) => defHttp.post({ url: Api.ADD_MENU, params });

/** 修改菜单 */
export const editMenuApi = (params: MenuIM) => defHttp.put({ url: Api.EDIT_MENU, params });

/** 修改菜单状态 */
export const editStatusMenuApi = (id: string, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS_MENU,
    params: { id: id, status: status },
  });

/** 删除菜单 */
export const delMenuApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH_MENU, params: ids.toString() });
