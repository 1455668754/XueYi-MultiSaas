import { ConfigIM, ConfigLRM, ConfigPPM } from '@/model/system/dict';
import { defHttp } from '@/utils/http/axios';

const basicApi = '/system/admin/config';

enum Api {
  LIST = basicApi + '/list',
  GET = basicApi + '/',
  ADD = basicApi,
  EDIT = basicApi,
  DEL_BATCH = basicApi + '/batch/',
  REFRESH = basicApi + '/refresh',
}

/** 查询参数列表 */
export const listConfigApi = (params?: ConfigPPM) =>
  defHttp.get<ConfigLRM>({ url: Api.LIST, params });

/** 查询参数详细 */
export const getConfigApi = (id: string) => defHttp.get<ConfigIM>({ url: Api.GET, params: id });

/** 新增参数 */
export const addConfigApi = (params: ConfigIM) => defHttp.post({ url: Api.ADD, params });

/** 修改参数 */
export const editConfigApi = (params: ConfigIM) => defHttp.put({ url: Api.EDIT, params });

/** 删除参数 */
export const delConfigApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH, params: ids.toString() });

/** 刷新参数缓存 */
export const refreshConfigApi = () => defHttp.get({ url: Api.REFRESH });
