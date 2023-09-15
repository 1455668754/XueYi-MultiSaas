import { AuthGroupIM, AuthGroupLRM, AuthGroupPPM } from '@/model/system/authority';
import { defHttp } from '@/utils/http/axios';

const basicApi = '/system/admin/authGroup';

enum Api {
  LIST = basicApi + '/list',
  GET = basicApi + '/',
  ADD = basicApi,
  EDIT = basicApi,
  EDIT_STATUS = basicApi + '/status',
  DEL_BATCH = basicApi + '/batch/',
}

/** 查询权限组列表 */
export const listAuthGroupApi = (params?: AuthGroupPPM) =>
  defHttp.get<AuthGroupLRM>({ url: Api.LIST, params });

/** 查询权限组详细 */
export const getAuthGroupApi = (id: string) =>
  defHttp.get<AuthGroupIM>({ url: Api.GET, params: id });

/** 新增权限组 */
export const addAuthGroupApi = (params: AuthGroupIM) => defHttp.post({ url: Api.ADD, params });

/** 修改权限组 */
export const editAuthGroupApi = (params: AuthGroupIM) => defHttp.put({ url: Api.EDIT, params });

/** 修改权限组状态 */
export const editStatusAuthGroupApi = (id: string, status: string) =>
  defHttp.put({
    url: Api.EDIT_STATUS,
    params: { id: id, status: status },
  });

/** 删除权限组 */
export const delAuthGroupApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH, params: ids.toString() });
