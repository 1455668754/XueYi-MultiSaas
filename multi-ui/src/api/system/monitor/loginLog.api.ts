import { LoginLogLRM, LoginLogPPM } from '@/model/system/monitor';
import { defHttp } from '/@/utils/http/axios';

const basicApi = '/system/admin/loginLog';

enum Api {
  LIST = basicApi + '/list',
  DEL_BATCH = basicApi + '/batch/',
  CLEAN = basicApi + '/clean',
}

/** 查询系统访问记录列表 */
export const listLoginLogApi = (params?: LoginLogPPM) =>
  defHttp.get<LoginLogLRM>({ url: Api.LIST, params });

/** 删除系统访问记录 */
export const delLoginLogApi = (ids: string | string[]) =>
  defHttp.delete({
    url: Api.DEL_BATCH,
    params: ids.toString(),
  });

/** 清空系统访问记录 */
export const cleanLoginLogApi = () => defHttp.delete({ url: Api.CLEAN });
