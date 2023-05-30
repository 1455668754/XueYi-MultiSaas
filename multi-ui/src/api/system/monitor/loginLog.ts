import { LoginLogLRM, LoginLogPPM } from '/@/model/system';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  LIST_LOGIN_LOG = '/system/admin/loginLog/list',
  DEL_BATCH_LOGIN_LOG = '/system/admin/loginLog/batch/',
  CLEAN_LOGIN_LOG = '/system/admin/loginLog/clean',
}

/** 查询系统访问记录列表 */
export const listLoginLogApi = (params?: LoginLogPPM) =>
  defHttp.get<LoginLogLRM>({ url: Api.LIST_LOGIN_LOG, params });

/** 删除系统访问记录 */
export const delLoginLogApi = (ids: (string | number) | (string | number)[]) =>
  defHttp.delete({
    url: Api.DEL_BATCH_LOGIN_LOG,
    params: ids.toString(),
  });

/** 清空系统访问记录 */
export const cleanLoginLogApi = () => defHttp.delete({ url: Api.CLEAN_LOGIN_LOG });
