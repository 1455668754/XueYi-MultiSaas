import { BasicFetchResult, BasicPageParams, BaseEntity } from '/@/model/src';

/** log info model */
export interface JobLogIM extends BaseEntity {
  id: string | number;
  name: string;
  jobGroup: string;
  invokeTarget: string;
  jobMessage: string;
  status: string;
  exceptionInfo: string;
  delTime: any;
}

/** log list model */
export type JobLogLM = JobLogIM[];

/** log param model */
export type JobLogPM = JobLogIM;

/** log page param model */
export type JobLogPPM = BasicPageParams & JobLogPM;

/** log list result model */
export type JobLogLRM = BasicFetchResult<JobLogIM>;
