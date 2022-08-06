import { BasicFetchResult, BasicPageParams, SubBaseEntity } from '/@/model/src';
import { JobLogIM } from './jobLog';

/** job info model */
export interface JobIM extends SubBaseEntity<JobLogIM> {
  id: string | number;
  name: string;
  jobGroup: string;
  invokeTarget: string;
  cronExpression: string;
  misfirePolicy: string;
  concurrent: string;
  status: string;
  remark: string;
}

/** job list model */
export type JobLM = JobIM[];

/** job param model */
export type JobPM = JobIM;

/** job page param model */
export type JobPPM = BasicPageParams & JobPM;

/** job list result model */
export type JobLRM = BasicFetchResult<JobIM>;
