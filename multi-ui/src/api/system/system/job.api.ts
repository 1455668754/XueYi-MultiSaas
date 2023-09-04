import { JobIM, JobLRM, JobPPM } from '@/model/system/system';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  LIST_JOB = '/schedule/admin/job/list',
  GET_JOB = '/schedule/admin/job/',
  ADD_JOB = '/schedule/admin/job',
  EDIT_JOB = '/schedule/admin/job',
  RUN_JOB = '/schedule/admin/job/run/',
  EDIT_STATUS_JOB = '/schedule/admin/job/status',
  DEL_BATCH_JOB = '/schedule/admin/job/batch/',
}

/** 查询调度任务列表 */
export const listJobApi = (params?: JobPPM) => defHttp.get<JobLRM>({ url: Api.LIST_JOB, params });

/** 查询调度任务详细 */
export const getJobApi = (id: string) => defHttp.get<JobIM>({ url: Api.GET_JOB, params: id });

/** 新增调度任务 */
export const addJobApi = (params: JobIM) => defHttp.post({ url: Api.ADD_JOB, params });

/** 修改调度任务 */
export const editJobApi = (params: JobIM) => defHttp.put({ url: Api.EDIT_JOB, params });

/** 执行一次调度任务 */
export const runJobApi = (id: string) => defHttp.get({ url: Api.RUN_JOB, params: id });

/** 修改调度任务状态 */
export const editStatusJobApi = (id: string, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS_JOB,
    params: { id: id, status: status },
  });

/** 删除调度任务 */
export const delJobApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH_JOB, params: ids.toString() });
