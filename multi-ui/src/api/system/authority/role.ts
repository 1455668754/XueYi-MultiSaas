import { RoleIM, RolePPM, RoleLRM } from '/@/model/system';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  LIST_ROLE = '/system/role/list',
  OPTION_ROLE = '/system/role/option',
  GET_ROLE = '/system/role/',
  GET_AUTH_ROLE = '/system/role/auth/',
  GET_ORGANIZE_ROLE = '/system/role/organize/',
  ADD_ROLE = '/system/role',
  EDIT_ROLE = '/system/role',
  EDIT_AUTH_ROLE = '/system/role/auth',
  EDIT_ORGANIZE_ROLE = '/system/role/organize',
  EDIT_STATUS_ROLE = '/system/role/status',
  DEL_BATCH_ROLE = '/system/role/batch/',
}

/** 查询角色列表 */
export const listRoleApi = (params?: RolePPM) =>
  defHttp.get<RoleLRM>({ url: Api.LIST_ROLE, params });

/** 查询角色选择框列表 */
export const optionRoleApi = () => defHttp.get<RoleLRM>({ url: Api.OPTION_ROLE });

/** 查询角色详细 */
export const getRoleApi = (id: string | number) =>
  defHttp.get<RoleIM>({ url: Api.GET_ROLE, params: id });

/** 查询角色功能权限节点集（叶子节点） */
export const getAuthRoleApi = (id: string | number) =>
  defHttp.get<(string | number)[]>({
    url: Api.GET_AUTH_ROLE,
    params: id,
  });

/** 查询角色数据权限 */
export const getOrganizeRoleApi = (id: string | number) =>
  defHttp.get<(string | number)[]>({
    url: Api.GET_ORGANIZE_ROLE,
    params: id,
  });

/** 新增角色 */
export const addRoleApi = (params: RoleIM) => defHttp.post({ url: Api.ADD_ROLE, params });

/** 修改角色 */
export const editRoleApi = (params: RoleIM) => defHttp.put({ url: Api.EDIT_ROLE, params });

/** 修改角色功能权限 */
export const editAuthScopeApi = (id: string | number, authIds: (string | number)[]) =>
  defHttp.put({
    url: Api.EDIT_AUTH_ROLE,
    params: { id: id, authIds: authIds },
  });

/** 修改角色数据权限 */
export const editDataScopeApi = (params: RoleIM) =>
  defHttp.put({ url: Api.EDIT_ORGANIZE_ROLE, params });

/** 修改角色状态 */
export const editStatusRoleApi = (id: string | number, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS_ROLE,
    params: { id: id, status: status },
  });

/** 删除角色 */
export const delRoleApi = (ids: (string | number) | (string | number)[]) =>
  defHttp.delete({ url: Api.DEL_BATCH_ROLE, params: ids.toString() });
