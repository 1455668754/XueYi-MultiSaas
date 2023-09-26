import { UserIM, UserLRM, UserPM, UserPPM } from '@/model/system/organize';
import { defHttp } from '@/utils/http/axios';
import { formatToDateTime } from '@/utils/dateUtil';
import dayjs from 'dayjs';

const basicApi = '/system/admin/user';

enum Api {
  LIST = basicApi + '/list',
  GET = basicApi + '/',
  GET_AUTH = basicApi + '/auth',
  ADD = basicApi,
  EDIT = basicApi,
  EDIT_AUTH = basicApi + '/auth',
  EDIT_STATUS = basicApi + '/status',
  DEL_BATCH = basicApi + '/batch/',
  EXPORT = basicApi + '/export',
  RESET_PWD = basicApi + '/resetPwd',
}

/** 查询用户列表 */
export const listUserApi = (params?: UserPPM) => defHttp.get<UserLRM>({ url: Api.LIST, params });

/** 查询用户详细 */
export const getUserApi = (id: string) => defHttp.get<UserIM>({ url: Api.GET, params: id });

/** 查询用户的角色权限节点集 */
export const getAuthUserApi = (id: string) =>
  defHttp.get<UserIM>({
    url: Api.GET_AUTH,
    params: { id: id },
  });

/** 新增用户 */
export const addUserApi = (params: UserIM) => defHttp.post({ url: Api.ADD, params });

/** 修改用户 */
export const editUserApi = (params: UserIM) => defHttp.put({ url: Api.EDIT, params });

/** 修改用户的角色权限 */
export const editAuthUserScopeApi = (id: string, roleIds: string[]) =>
  defHttp.put({
    url: Api.EDIT_AUTH,
    params: { id: id, roleIds: roleIds },
  });

/** 修改用户状态 */
export const editStatusUserApi = (id: string, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS,
    params: { id: id, status: status },
  });

/** 删除用户 */
export const delUserApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH, params: ids.toString() });

/** 用户导出 */
export const exportUserApi = async (params: UserPM) =>
  defHttp.export<any>(
    { url: Api.EXPORT, params: params },
    '用户_' + formatToDateTime(dayjs()) + '.xlsx',
  );

/** 用户密码重置 */
export const resetUserPwdApi = (id: string, password: string) =>
  defHttp.put({
    url: Api.RESET_PWD,
    params: { id: id, password: password },
  });
