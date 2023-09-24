import { RoleIM, RoleLRM, RolePPM } from '@/model/system/authority';
import { defHttp } from '@/utils/http/axios';

const basicApi = '/system/admin/role';

enum Api {
  LIST = basicApi + '/list',
  GET = basicApi + '/',
  GET_AUTH = basicApi + '/auth',
  GET_ORGANIZE = basicApi + '/organize',
  ADD = basicApi,
  EDIT = basicApi,
  EDIT_AUTH = basicApi + '/auth',
  EDIT_ORGANIZE = basicApi + '/organize',
  EDIT_STATUS = basicApi + '/status',
  DEL_BATCH = basicApi + '/batch/',
}

/** 查询角色列表 */
export const listRoleApi = (params?: RolePPM) => defHttp.get<RoleLRM>({ url: Api.LIST, params });

/** 查询角色详细 */
export const getRoleApi = (id: string) => defHttp.get<RoleIM>({ url: Api.GET, params: id });

/** 查询角色功能权限 */
export const getAuthRoleApi = (id: string) =>
  defHttp.get<RoleIM>({
    url: Api.GET_AUTH,
    params: { id: id },
  });

/** 查询角色数据权限 */
export const getOrganizeRoleApi = (id: string) =>
  defHttp.get<RoleIM>({
    url: Api.GET_ORGANIZE,
    params: { id: id },
  });

/** 新增角色 */
export const addRoleApi = (params: RoleIM) => defHttp.post({ url: Api.ADD, params });

/** 修改角色 */
export const editRoleApi = (params: RoleIM) => defHttp.put({ url: Api.EDIT, params });

/** 修改角色功能权限 */
export const editAuthScopeApi = (params: RoleIM) =>
  defHttp.put({
    url: Api.EDIT_AUTH,
    params,
  });

/** 修改角色数据权限 */
export const editDataScopeApi = (params: RoleIM) => defHttp.put({ url: Api.EDIT_ORGANIZE, params });

/** 修改角色状态 */
export const editStatusRoleApi = (id: string, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS,
    params: { id: id, status: status },
  });

/** 删除角色 */
export const delRoleApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH, params: ids.toString() });
