import { TenantIM, TenantLRM, TenantPPM } from '@/model/tenant/tenant';
import { defHttp } from '/@/utils/http/axios';
import { EnterpriseIM } from '@/model/system/organize';

enum Api {
  LIST_TENANT = '/tenant/admin/tenant/list',
  GET_TENANT = '/tenant/admin/tenant/',
  GET_AUTH_TENANT = '/system/admin/enterprise/enterpriseGroup',
  EDIT_AUTH_TENANT = '/system/admin/enterprise/enterpriseGroup',
  ADD_TENANT = '/tenant/admin/tenant',
  EDIT_TENANT = '/tenant/admin/tenant',
  EDIT_STATUS_TENANT = '/tenant/admin/tenant/status',
  DEL_BATCH_TENANT = '/tenant/admin/tenant/batch/',
}

/** 查询租户列表 */
export const listTenantApi = (params?: TenantPPM) =>
  defHttp.get<TenantLRM>({ url: Api.LIST_TENANT, params });

/** 查询租户详细 */
export const getTenantApi = (id: string) =>
  defHttp.get<TenantIM>({ url: Api.GET_TENANT, params: id });

/** 查询租户权限组 */
export const getAuthGroupTenantApi = (id: string) =>
  defHttp.get<EnterpriseIM>({ url: Api.GET_AUTH_TENANT, params: { id: id } });

/** 新增租户 */
export const addTenantApi = (params: TenantIM) => defHttp.post({ url: Api.ADD_TENANT, params });

/** 修改租户 */
export const editTenantApi = (params: TenantIM) => defHttp.put({ url: Api.EDIT_TENANT, params });

/** 修改租户权限组 */
export const editAuthGroupTenantApi = (id: string, authGroupIds: string[]) =>
  defHttp.put({ url: Api.EDIT_AUTH_TENANT, params: { id: id, authGroupIds: authGroupIds } });

/** 修改租户状态 */
export const editStatusTenantApi = (id: string, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS_TENANT,
    params: { id: id, status: status },
  });

/** 删除租户 */
export const delTenantApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH_TENANT, params: ids.toString() });
