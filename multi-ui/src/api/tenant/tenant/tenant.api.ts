import { TenantIM, TenantLRM, TenantPPM } from '@/model/tenant/tenant';
import { defHttp } from '@/utils/http/axios';
import { EnterpriseIM } from '@/model/system/organize';

const basicApi = '/tenant/admin/tenant';
const basicAuthApi = '/system/admin/enterprise';

enum Api {
  LIST = basicApi + '/list',
  GET = basicApi + '/',
  GET_AUTH = basicAuthApi + '/enterpriseGroup',
  EDIT_AUTH = basicAuthApi + '/enterpriseGroup',
  ADD = basicApi,
  EDIT = basicApi,
  EDIT_STATUS = basicApi + '/status',
  DEL_BATCH = basicApi + '/batch/',
}

/** 查询租户列表 */
export const listTenantApi = (params?: TenantPPM) =>
  defHttp.get<TenantLRM>({ url: Api.LIST, params });

/** 查询租户详细 */
export const getTenantApi = (id: string) => defHttp.get<TenantIM>({ url: Api.GET, params: id });

/** 查询租户权限组 */
export const getAuthGroupTenantApi = (id: string) =>
  defHttp.get<EnterpriseIM>({ url: Api.GET_AUTH, params: { id: id } });

/** 新增租户 */
export const addTenantApi = (params: TenantIM) => defHttp.post({ url: Api.ADD, params });

/** 修改租户 */
export const editTenantApi = (params: TenantIM) => defHttp.put({ url: Api.EDIT, params });

/** 修改租户权限组 */
export const editAuthGroupTenantApi = (id: string, authGroupIds: string[]) =>
  defHttp.put({ url: Api.EDIT_AUTH, params: { id: id, authGroupIds: authGroupIds } });

/** 修改租户状态 */
export const editStatusTenantApi = (id: string, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS,
    params: { id: id, status: status },
  });

/** 删除租户 */
export const delTenantApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH, params: ids.toString() });
