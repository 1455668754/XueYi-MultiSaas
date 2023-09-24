import { DeptIM, DeptLM, DeptPM } from '@/model/system/organize';
import { defHttp } from '@/utils/http/axios';

const basicApi = '/system/admin/dept';

enum Api {
  LIST = basicApi + '/list',
  GET = basicApi + '/',
  GET_AUTH = basicApi + '/auth',
  ADD = basicApi,
  EDIT = basicApi,
  EDIT_AUTH = basicApi + '/auth',
  EDIT_STATUS = basicApi + '/status',
  DEL_BATCH = basicApi + '/batch/',
}

/** 查询部门列表 */
export const listDeptApi = (params?: DeptPM) => defHttp.get<DeptLM>({ url: Api.LIST, params });

/** 查询部门详细 */
export const getDeptApi = (id: string) => defHttp.get<DeptIM>({ url: Api.GET, params: id });

/** 查询部门的角色权限节点集 */
export const getAuthDeptApi = (id: string) =>
  defHttp.get<DeptIM>({
    url: Api.GET_AUTH,
    params: { id: id },
  });

/** 新增部门 */
export const addDeptApi = (params: DeptIM) => defHttp.post({ url: Api.ADD, params });

/** 修改部门 */
export const editDeptApi = (params: DeptIM) => defHttp.put({ url: Api.EDIT, params });

/** 修改部门的角色权限 */
export const editAuthDeptScopeApi = (id: string, roleIds: string[]) =>
  defHttp.put({
    url: Api.EDIT_AUTH,
    params: { id: id, roleIds: roleIds },
  });

/** 修改部门状态 */
export const editStatusDeptApi = (id: string, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS,
    params: { id: id, status: status },
  });

/** 删除部门 */
export const delDeptApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH, params: ids.toString() });
