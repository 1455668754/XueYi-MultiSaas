import { ConfigIM, ConfigLRM, ConfigPPM } from '/@/model/tenant';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  LIST_CONFIG = '/system/config/list',
  OPTION_CONFIG = '/system/config/option',
  GET_CONFIG = '/system/config/',
  ADD_CONFIG = '/system/config',
  EDIT_CONFIG = '/system/config',
  DEL_BATCH_CONFIG = '/system/config/batch/',
  DEL_BATCH_FORCE_CONFIG = '/system/config/batch/force/',
  REFRESH_CONFIG = '/system/config/refresh',
}

/** 查询参数列表 */
export const listConfigApi = (params?: ConfigPPM) =>
  defHttp.get<ConfigLRM>({ url: Api.LIST_CONFIG, params });

/** 查询参数选择框列表 */
export const optionConfigApi = () => defHttp.get<ConfigLRM>({ url: Api.OPTION_CONFIG });

/** 查询参数详细 */
export const getConfigApi = (id: string | number) =>
  defHttp.get<ConfigIM>({ url: Api.GET_CONFIG, params: id });

/** 新增参数 */
export const addConfigApi = (params: ConfigIM) => defHttp.post({ url: Api.ADD_CONFIG, params });

/** 修改参数 */
export const editConfigApi = (params: ConfigIM) => defHttp.put({ url: Api.EDIT_CONFIG, params });

/** 删除参数 */
export const delConfigApi = (ids: (string | number) | (string | number)[]) =>
  defHttp.delete({ url: Api.DEL_BATCH_CONFIG, params: ids.toString() });

/** 强制删除参数 */
export const delForceConfigApi = (ids: (string | number) | (string | number)[]) =>
  defHttp.delete({
    url: Api.DEL_BATCH_FORCE_CONFIG,
    params: ids.toString(),
  });

/** 刷新参数缓存 */
export const refreshConfigApi = () => defHttp.get({ url: Api.REFRESH_CONFIG });
