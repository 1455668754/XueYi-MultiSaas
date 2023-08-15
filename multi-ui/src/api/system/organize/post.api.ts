import { PostIM, PostLRM, PostPPM } from '@/model/system/organize';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  LIST_POST = '/system/admin/post/list',
  GET_POST = '/system/admin/post/',
  GET_AUTH_POST = '/system/admin/post/auth/',
  ADD_POST = '/system/admin/post',
  EDIT_POST = '/system/admin/post',
  EDIT_AUTH_POST = '/system/admin/post/auth',
  EDIT_STATUS_POST = '/system/admin/post/status',
  DEL_BATCH_POST = '/system/admin/post/batch/',
}

/** 查询岗位列表 */
export const listPostApi = (params?: PostPPM) =>
  defHttp.get<PostLRM>({ url: Api.LIST_POST, params });

/** 查询岗位详细 */
export const getPostApi = (id: string) => defHttp.get<PostIM>({ url: Api.GET_POST, params: id });

/** 查询岗位的角色权限节点集 */
export const getAuthPostApi = (id: string) =>
  defHttp.get<string[]>({
    url: Api.GET_AUTH_POST,
    params: id,
  });

/** 新增岗位 */
export const addPostApi = (params: PostIM) => defHttp.post({ url: Api.ADD_POST, params });

/** 修改岗位 */
export const editPostApi = (params: PostIM) => defHttp.put({ url: Api.EDIT_POST, params });

/** 修改岗位的角色权限 */
export const editAuthPostScopeApi = (id: string, roleIds: string[]) =>
  defHttp.put({
    url: Api.EDIT_AUTH_POST,
    params: { id: id, roleIds: roleIds },
  });

/** 修改岗位状态 */
export const editStatusPostApi = (id: string, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS_POST,
    params: { id: id, status: status },
  });

/** 删除岗位 */
export const delPostApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH_POST, params: ids.toString() });
