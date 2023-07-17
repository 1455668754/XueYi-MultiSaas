import { defHttp } from '/@/utils/http/axios';
import { MenuIM, MenuLM, MenuPM } from '@/model/system';

enum Api {
  LIST_MENU = '/system/admin/menu/list',
  LIST_MENU_BY_TYPE = '/system/admin/menu/routeList',
  GET_MENU = '/system/admin/menu/',
  ADD_MENU = '/system/admin/menu',
  EDIT_MENU = '/system/admin/menu',
  EDIT_STATUS_MENU = '/system/admin/menu/status',
  DEL_BATCH_MENU = '/system/admin/menu/batch/',
}

/** 查询菜单列表 */
export const listMenuApi = (params?: MenuPM) => defHttp.get<MenuLM>({ url: Api.LIST_MENU, params });

/** 根据菜单类型获取指定模块的可配菜单集 */
export const getMenuRouteListApi = (params?: MenuPM) =>
  defHttp.get<MenuLM>({
    url: Api.LIST_MENU,
    params,
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
