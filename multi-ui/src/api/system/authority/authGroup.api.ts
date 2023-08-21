import { AuthGroupIM, AuthGroupPPM, AuthGroupLRM } from '@/model/system/authority';
import { defHttp } from '@/utils/http/axios';

enum Api {
  LIST_AUTH_GROUP = '/system/admin/authGroup/list',
  GET_AUTH_GROUP = '/system/admin/authGroup/',
  ADD_AUTH_GROUP = '/system/admin/authGroup',
  EDIT_AUTH_GROUP = '/system/admin/authGroup',
  EDIT_STATUS_AUTH_GROUP = '/system/admin/authGroup/status',
  DEL_BATCH_AUTH_GROUP = '/system/admin/authGroup/batch/',
}

/** 查询权限组列表 */
export const listAuthGroupApi = (params?: AuthGroupPPM) =>
  defHttp.get<AuthGroupLRM>({ url: Api.LIST_AUTH_GROUP, params });

/** 查询权限组详细 */
export const getAuthGroupApi = (id: string) =>
  defHttp.get<AuthGroupIM>({ url: Api.GET_AUTH_GROUP, params: id });

/** 新增权限组 */
export const addAuthGroupApi = (params: AuthGroupIM) =>
  defHttp.post({ url: Api.ADD_AUTH_GROUP, params });

/** 修改权限组 */
export const editAuthGroupApi = (params: AuthGroupIM) =>
  defHttp.put({ url: Api.EDIT_AUTH_GROUP, params });

/** 修改权限组状态 */
export const editStatusAuthGroupApi = (id: string, status: string) =>
  defHttp.put({
    url: Api.EDIT_STATUS_AUTH_GROUP,
    params: { id: id, status: status },
  });

/** 删除权限组 */
export const delAuthGroupApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH_AUTH_GROUP, params: ids.toString() });
