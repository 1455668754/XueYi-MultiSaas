import { JobLogIM, JobLogLRM, JobLogPPM } from '@/model/system/system';
import { defHttp } from '/@/utils/http/axios';

const basicApi = '/schedule/admin/job/log';

enum Api {
  LIST = basicApi + '/list',
  GET = basicApi + '/',
  CLEAN = basicApi + '/clean',
}

/** 查询调度日志列表 */
export const listJobLogApi = (params?: JobLogPPM) =>
  defHttp.get<JobLogLRM>({ url: Api.LIST, params });

/** 查询调度日志详细 */
export const getJobLogApi = (id: string) => defHttp.get<JobLogIM>({ url: Api.GET, params: id });

/** 删除调度日志 */
export const cleanJobLogApi = () => defHttp.delete({ url: Api.CLEAN });
