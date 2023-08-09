import { JobLogIM, JobLogLRM, JobLogPPM } from '@/model/system/system';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  LIST_LOG = '/schedule/job/log/list',
  GET_JOB_LOG = '/schedule/job/log/',
  CLEAN_LOG = '/schedule/job/log/clean',
}

/** 查询调度日志列表 */
export const listJobLogApi = (params?: JobLogPPM) =>
  defHttp.get<JobLogLRM>({ url: Api.LIST_LOG, params });

/** 查询调度日志详细 */
export const getJobLogApi = (id: string) =>
  defHttp.get<JobLogIM>({ url: Api.GET_JOB_LOG, params: id });

/** 删除调度日志 */
export const cleanJobLogApi = () => defHttp.delete({ url: Api.CLEAN_LOG });
