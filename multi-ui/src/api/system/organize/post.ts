import { PostIM, PostPPM, PostLRM } from '/@/model/system';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  LIST_POST = '/system/post/list',
  OPTION_POST = '/system/post/option',
  GET_POST = '/system/post/',
  GET_AUTH_POST = '/system/post/auth/',
  ADD_POST = '/system/post',
  EDIT_POST = '/system/post',
  EDIT_AUTH_POST = '/system/post/auth',
  EDIT_STATUS_POST = '/system/post/status',
  DEL_BATCH_POST = '/system/post/batch/',
}

/** 查询岗位列表 */
export const listPostApi = (params?: PostPPM) =>
  defHttp.get<PostLRM>({ url: Api.LIST_POST, params });

/** 查询岗位选择框列表 */
export const optionPostApi = () => defHttp.get<PostLRM>({ url: Api.OPTION_POST });

/** 查询岗位详细 */
export const getPostApi = (id: string | number) =>
  defHttp.get<PostIM>({ url: Api.GET_POST, params: id });

/** 查询岗位的角色权限节点集 */
export const getAuthPostApi = (id: string | number) =>
  defHttp.get<(string | number)[]>({
    url: Api.GET_AUTH_POST,
    params: id,
  });

/** 新增岗位 */
export const addPostApi = (params: PostIM) => defHttp.post({ url: Api.ADD_POST, params });

/** 修改岗位 */
export const editPostApi = (params: PostIM) => defHttp.put({ url: Api.EDIT_POST, params });

/** 修改岗位的角色权限 */
export const editAuthPostScopeApi = (id: string | number, roleIds: (string | number)[]) =>
  defHttp.put({
    url: Api.EDIT_AUTH_POST,
    params: { id: id, roleIds: roleIds },
  });

/** 修改岗位状态 */
export const editStatusPostApi = (id: string | number, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS_POST,
    params: { id: id, status: status },
  });

/** 删除岗位 */
export const delPostApi = (ids: (string | number) | (string | number)[]) =>
  defHttp.delete({ url: Api.DEL_BATCH_POST, params: ids.toString() });
