import { OperateLogIM, OperateLogLRM, OperateLogPPM } from '@/model/system/monitor';
import { defHttp } from '/@/utils/http/axios';

const basicApi = '/system/admin/operateLog';

enum Api {
  LIST = basicApi + '/list',
  GET = basicApi + '/',
  DEL_BATCH = basicApi + '/batch/',
  CLEAN = basicApi + '/clean',
}

/** 查询操作日志列表 */
export const listOperateLogApi = (params?: OperateLogPPM) =>
  defHttp.get<OperateLogLRM>({ url: Api.LIST, params });

/** 查询操作日志详细 */
export const getOperateLogApi = (id: string) =>
  defHttp.get<OperateLogIM>({ url: Api.GET, params: id });

/** 删除操作日志 */
export const delOperateLogApi = (ids: string | string[]) =>
  defHttp.delete({
    url: Api.DEL_BATCH,
    params: ids.toString(),
  });

/** 清空操作日志 */
export const cleanOperateLogApi = () => defHttp.delete({ url: Api.CLEAN });
