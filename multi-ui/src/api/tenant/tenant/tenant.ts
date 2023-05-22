import { TenantIM, TenantPPM, TenantLRM } from '/@/model/tenant';
import { defHttp } from '/@/utils/http/axios';
import { AuthLM } from '/@/model/system';

enum Api {
  LIST_TENANT = '/tenant/tenant/list',
  GET_TENANT = '/tenant/tenant/',
  AUTH_SCOPE_TENANT = '/system/auth/tenant/authScope',
  GET_AUTH_TENANT = '/tenant/tenant/auth/',
  EDIT_AUTH_TENANT = '/tenant/tenant/auth',
  ADD_TENANT = '/tenant/tenant',
  EDIT_TENANT = '/tenant/tenant',
  EDIT_STATUS_TENANT = '/tenant/tenant/status',
  DEL_BATCH_TENANT = '/tenant/tenant/batch/',
}

/** 查询租户列表 */
export const listTenantApi = (params?: TenantPPM) =>
  defHttp.get<TenantLRM>({ url: Api.LIST_TENANT, params });

/** 查询租户详细 */
export const getTenantApi = (id: string | number) =>
  defHttp.get<TenantIM>({ url: Api.GET_TENANT, params: id });

/** 查询租户权限（叶子节点） */
export const getAuthTenantApi = (id: string | number) =>
  defHttp.get<[]>({ url: Api.GET_AUTH_TENANT, params: id });

/** 查询公共权限范围树 */
export const authScopeTenantApi = () => defHttp.get<AuthLM>({ url: Api.AUTH_SCOPE_TENANT });

/** 新增租户 */
export const addTenantApi = (params: TenantIM) => defHttp.post({ url: Api.ADD_TENANT, params });

/** 修改租户 */
export const editTenantApi = (params: TenantIM) => defHttp.put({ url: Api.EDIT_TENANT, params });

/** 修改租户权限 */
export const editAuthTenantApi = (params: TenantIM) =>
  defHttp.put({ url: Api.EDIT_AUTH_TENANT, params });

/** 修改租户状态 */
export const editStatusTenantApi = (id: string | number, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS_TENANT,
    params: { id: id, status: status },
  });

/** 删除租户 */
export const delTenantApi = (ids: (string | number) | (string | number)[]) =>
  defHttp.delete({ url: Api.DEL_BATCH_TENANT, params: ids.toString() });
