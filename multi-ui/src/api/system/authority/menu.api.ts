import { defHttp } from '/@/utils/http/axios';
import { MenuIM, MenuLM, MenuPM } from '@/model/system/authority';

const basicApi = '/system/admin/menu';

enum Api {
  LIST = basicApi + '/list',
  GET = basicApi + '/',
  ADD = basicApi,
  EDIT = basicApi,
  EDIT_STATUS = basicApi + '/status',
  DEL_BATCH = basicApi + '/batch/',
}

/** 查询菜单列表 */
export const listMenuApi = (params?: MenuPM) => defHttp.get<MenuLM>({ url: Api.LIST, params });

/** 根据菜单类型获取指定模块的可配菜单集 */
export const getMenuRouteListApi = (params?: MenuPM) =>
  defHttp.get<MenuLM>({
    url: Api.LIST,
    params,
  });

/** 查询菜单详细 */
export const getMenuApi = (id: string) => defHttp.get<MenuIM>({ url: Api.GET, params: id });

/** 新增菜单 */
export const addMenuApi = (params: MenuIM) => defHttp.post({ url: Api.ADD, params });

/** 修改菜单 */
export const editMenuApi = (params: MenuIM) => defHttp.put({ url: Api.EDIT, params });

/** 修改菜单状态 */
export const editStatusMenuApi = (id: string, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS,
    params: { id: id, status: status },
  });

/** 删除菜单 */
export const delMenuApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH, params: ids.toString() });
