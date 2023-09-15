import { PostIM, PostLRM, PostPPM } from '@/model/system/organize';
import { defHttp } from '/@/utils/http/axios';

const basicApi = '/system/admin/post';

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

/** 查询岗位列表 */
export const listPostApi = (params?: PostPPM) => defHttp.get<PostLRM>({ url: Api.LIST, params });

/** 查询岗位详细 */
export const getPostApi = (id: string) => defHttp.get<PostIM>({ url: Api.GET, params: id });

/** 查询岗位的角色权限节点集 */
export const getAuthPostApi = (id: string) =>
  defHttp.get<PostIM>({
    url: Api.GET_AUTH,
    params: { id: id },
  });

/** 新增岗位 */
export const addPostApi = (params: PostIM) => defHttp.post({ url: Api.ADD, params });

/** 修改岗位 */
export const editPostApi = (params: PostIM) => defHttp.put({ url: Api.EDIT, params });

/** 修改岗位的角色权限 */
export const editAuthPostScopeApi = (id: string, roleIds: string[]) =>
  defHttp.put({
    url: Api.EDIT_AUTH,
    params: { id: id, roleIds: roleIds },
  });

/** 修改岗位状态 */
export const editStatusPostApi = (id: string, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS,
    params: { id: id, status: status },
  });

/** 删除岗位 */
export const delPostApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH, params: ids.toString() });
