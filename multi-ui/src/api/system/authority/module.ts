import { ModuleIM, ModulePPM, ModuleLRM } from '/@/model/system';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  LIST_MODULE = '/system/module/list',
  OPTION_MODULE = '/system/module/option',
  GET_MODULE = '/system/module/',
  ADD_MODULE = '/system/module',
  EDIT_MODULE = '/system/module',
  EDIT_STATUS_MODULE = '/system/module/status',
  DEL_BATCH_MODULE = '/system/module/batch/',
}

/** 查询模块列表 */
export const listModuleApi = (params?: ModulePPM) =>
  defHttp.get<ModuleLRM>({ url: Api.LIST_MODULE, params });

/** 查询模块选择框列表 */
export const optionModuleApi = () => defHttp.get<ModuleLRM>({ url: Api.OPTION_MODULE });

/** 查询模块详细 */
export const getModuleApi = (id: string | number) =>
  defHttp.get<ModuleIM>({ url: Api.GET_MODULE, params: id });

/** 新增模块 */
export const addModuleApi = (params: ModuleIM) => defHttp.post({ url: Api.ADD_MODULE, params });

/** 修改模块 */
export const editModuleApi = (params: ModuleIM) => defHttp.put({ url: Api.EDIT_MODULE, params });

/** 修改模块状态 */
export const editStatusModuleApi = (id: string | number, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS_MODULE,
    params: { id: id, status: status },
  });

/** 删除模块 */
export const delModuleApi = (ids: (string | number) | (string | number)[]) =>
  defHttp.delete({ url: Api.DEL_BATCH_MODULE, params: ids.toString() });
