import { ConfigIM, ConfigLRM, ConfigPPM } from '@/model/system/dict';
import { defHttp } from '@/utils/http/axios';

enum Api {
  LIST_CONFIG = '/system/admin/config/list',
  GET_CONFIG = '/system/admin/config/',
  ADD_CONFIG = '/system/admin/config',
  EDIT_CONFIG = '/system/admin/config',
  DEL_BATCH_CONFIG = '/system/admin/config/batch/',
  REFRESH_CONFIG = '/system/admin/config/refresh',
}

/** 查询参数列表 */
export const listConfigApi = (params?: ConfigPPM) =>
  defHttp.get<ConfigLRM>({ url: Api.LIST_CONFIG, params });

/** 查询参数详细 */
export const getConfigApi = (id: string) =>
  defHttp.get<ConfigIM>({ url: Api.GET_CONFIG, params: id });

/** 新增参数 */
export const addConfigApi = (params: ConfigIM) => defHttp.post({ url: Api.ADD_CONFIG, params });

/** 修改参数 */
export const editConfigApi = (params: ConfigIM) => defHttp.put({ url: Api.EDIT_CONFIG, params });

/** 删除参数 */
export const delConfigApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH_CONFIG, params: ids.toString() });

/** 刷新参数缓存 */
export const refreshConfigApi = () => defHttp.get({ url: Api.REFRESH_CONFIG });
