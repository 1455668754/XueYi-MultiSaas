import { OperateLogIM, OperateLogLRM, OperateLogPPM } from '@/model/system/monitor';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  LIST_OPERATE_LOG = '/system/admin/operateLog/list',
  GET_OPERATE_LOG = '/system/admin/operateLog/',
  DEL_BATCH_OPERATE_LOG = '/system/admin/operateLog/batch/',
  CLEAN_OPERATE_LOG = '/system/admin/operateLog/clean',
}

/** 查询操作日志列表 */
export const listOperateLogApi = (params?: OperateLogPPM) =>
  defHttp.get<OperateLogLRM>({ url: Api.LIST_OPERATE_LOG, params });

/** 查询操作日志详细 */
export const getOperateLogApi = (id: string) =>
  defHttp.get<OperateLogIM>({ url: Api.GET_OPERATE_LOG, params: id });

/** 删除操作日志 */
export const delOperateLogApi = (ids: string | string[]) =>
  defHttp.delete({
    url: Api.DEL_BATCH_OPERATE_LOG,
    params: ids.toString(),
  });

/** 清空操作日志 */
export const cleanOperateLogApi = () => defHttp.delete({ url: Api.CLEAN_OPERATE_LOG });
