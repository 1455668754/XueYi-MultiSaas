import { JobIM, JobLRM, JobPPM } from '@/model/system';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  LIST_JOB = '/schedule/job/list',
  OPTION_JOB = '/schedule/job/option',
  GET_JOB = '/schedule/job/',
  ADD_JOB = '/schedule/job',
  EDIT_JOB = '/schedule/job',
  RUN_JOB = '/schedule/job/run/',
  EDIT_STATUS_JOB = '/schedule/job/status',
  DEL_BATCH_JOB = '/schedule/job/batch/',
}

/** 查询调度任务列表 */
export const listJobApi = (params?: JobPPM) => defHttp.get<JobLRM>({ url: Api.LIST_JOB, params });

/** 查询调度任务选择框列表 */
export const optionJobApi = () => defHttp.get<JobLRM>({ url: Api.OPTION_JOB });

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
