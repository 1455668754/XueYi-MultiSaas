import { DeptIM, DeptLM, DeptPM } from '/@/model/system';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  LIST_DEPT = '/system/admin/dept/list',
  OPTION_DEPT = '/system/admin/dept/option',
  GET_DEPT = '/system/admin/dept/',
  GET_AUTH_DEPT = '/system/admin/dept/auth/',
  ADD_DEPT = '/system/admin/dept',
  EDIT_DEPT = '/system/admin/dept',
  EDIT_AUTH_DEPT = '/system/admin/dept/auth',
  EDIT_STATUS_DEPT = '/system/admin/dept/status',
  DEL_BATCH_DEPT = '/system/admin/dept/batch/',
}

/** 查询部门列表 */
export const listDeptApi = (params?: DeptPM) => defHttp.get<DeptLM>({ url: Api.LIST_DEPT, params });

/** 查询部门选择框列表 */
export const optionDeptApi = () => defHttp.get<DeptLM>({ url: Api.OPTION_DEPT });

/** 查询部门详细 */
export const getDeptApi = (id: string) => defHttp.get<DeptIM>({ url: Api.GET_DEPT, params: id });

/** 查询部门的角色权限节点集 */
export const getAuthDeptApi = (id: string) =>
  defHttp.get<string[]>({
    url: Api.GET_AUTH_DEPT,
    params: id,
  });

/** 新增部门 */
export const addDeptApi = (params: DeptIM) => defHttp.post({ url: Api.ADD_DEPT, params });

/** 修改部门 */
export const editDeptApi = (params: DeptIM) => defHttp.put({ url: Api.EDIT_DEPT, params });

/** 修改部门的角色权限 */
export const editAuthDeptScopeApi = (id: string, roleIds: string[]) =>
  defHttp.put({
    url: Api.EDIT_AUTH_DEPT,
    params: { id: id, roleIds: roleIds },
  });

/** 修改部门状态 */
export const editStatusDeptApi = (id: string, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS_DEPT,
    params: { id: id, status: status },
  });

/** 删除部门 */
export const delDeptApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH_DEPT, params: ids.toString() });
