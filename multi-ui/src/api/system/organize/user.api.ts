import { UserIM, UserLRM, UserPM, UserPPM } from '@/model/system/organize';
import { defHttp } from '/@/utils/http/axios';
import { formatToDateTime } from '/@/utils/dateUtil';
import dayjs from 'dayjs';

enum Api {
  LIST_USER = '/system/admin/user/list',
  GET_USER = '/system/admin/user/',
  GET_AUTH_USER = '/system/admin/user/auth/',
  ADD_USER = '/system/admin/user',
  EDIT_USER = '/system/admin/user',
  EDIT_AUTH_USER = '/system/admin/user/auth',
  EDIT_STATUS_USER = '/system/admin/user/status',
  DEL_BATCH_USER = '/system/admin/user/batch/',
  EXPORT_USER = '/system/admin/user/export',
  RESET_USER_PWD = '/system/admin/user/resetPwd',
}

/** 查询用户列表 */
export const listUserApi = (params?: UserPPM) =>
  defHttp.get<UserLRM>({ url: Api.LIST_USER, params });

/** 查询用户详细 */
export const getUserApi = (id: string) => defHttp.get<UserIM>({ url: Api.GET_USER, params: id });

/** 查询用户的角色权限节点集 */
export const getAuthUserApi = (id: string) =>
  defHttp.get<string[]>({
    url: Api.GET_AUTH_USER,
    params: id,
  });

/** 新增用户 */
export const addUserApi = (params: UserIM) => defHttp.post({ url: Api.ADD_USER, params });

/** 修改用户 */
export const editUserApi = (params: UserIM) => defHttp.put({ url: Api.EDIT_USER, params });

/** 修改用户的角色权限 */
export const editAuthUserScopeApi = (id: string, roleIds: string[]) =>
  defHttp.put({
    url: Api.EDIT_AUTH_USER,
    params: { id: id, roleIds: roleIds },
  });

/** 修改用户状态 */
export const editStatusUserApi = (id: string, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS_USER,
    params: { id: id, status: status },
  });

/** 删除用户 */
export const delUserApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH_USER, params: ids.toString() });

/** 用户导出 */
export const exportUserApi = async (params: UserPM) =>
  defHttp.export<any>(
    { url: Api.EXPORT_USER, params: params },
    '用户_' + formatToDateTime(dayjs()) + '.xlsx',
  );

/** 用户密码重置 */
export const resetUserPwdApi = (id: string, password: string) =>
  defHttp.put({
    url: Api.RESET_USER_PWD,
    params: { id: id, password: password },
  });
