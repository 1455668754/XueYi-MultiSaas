import { JobIM, JobLRM, JobPPM } from '@/model/system/system';
import { defHttp } from '/@/utils/http/axios';

const basicApi = '/schedule/admin/job';

enum Api {
  LIST = basicApi + '/list',
  GET = basicApi + '/',
  ADD = basicApi,
  EDIT = basicApi,
  RUN = basicApi + '/run/',
  EDIT_STATUS = basicApi + '/status',
  DEL_BATCH = basicApi + '/batch/',
}

/** 查询调度任务列表 */
export const listJobApi = (params?: JobPPM) => defHttp.get<JobLRM>({ url: Api.LIST, params });

/** 查询调度任务详细 */
export const getJobApi = (id: string) => defHttp.get<JobIM>({ url: Api.GET, params: id });

/** 新增调度任务 */
export const addJobApi = (params: JobIM) => defHttp.post({ url: Api.ADD, params });

/** 修改调度任务 */
export const editJobApi = (params: JobIM) => defHttp.put({ url: Api.EDIT, params });

/** 执行一次调度任务 */
export const runJobApi = (id: string) => defHttp.get({ url: Api.RUN, params: id });

/** 修改调度任务状态 */
export const editStatusJobApi = (id: string, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS,
    params: { id: id, status: status },
  });

/** 删除调度任务 */
export const delJobApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH, params: ids.toString() });
