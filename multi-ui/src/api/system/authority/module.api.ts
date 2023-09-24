import { ModuleIM, ModuleLRM, ModulePPM } from '@/model/system/authority';
import { defHttp } from '@/utils/http/axios';

const basicApi = '/system/admin/module';

enum Api {
  LIST = basicApi + '/list',
  GET = basicApi + '/',
  ADD = basicApi,
  EDIT = basicApi,
  EDIT_STATUS = basicApi + '/status',
  DEL_BATCH = basicApi + '/batch/',
}

/** 查询模块列表 */
export const listModuleApi = (params?: ModulePPM) =>
  defHttp.get<ModuleLRM>({ url: Api.LIST, params });

/** 查询模块详细 */
export const getModuleApi = (id: string) => defHttp.get<ModuleIM>({ url: Api.GET, params: id });

/** 新增模块 */
export const addModuleApi = (params: ModuleIM) => defHttp.post({ url: Api.ADD, params });

/** 修改模块 */
export const editModuleApi = (params: ModuleIM) => defHttp.put({ url: Api.EDIT, params });

/** 修改模块状态 */
export const editStatusModuleApi = (id: string, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS,
    params: { id: id, status: status },
  });

/** 删除模块 */
export const delModuleApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH, params: ids.toString() });
