import { BasicFetchResult, BasicPageParams, SubBaseEntity } from '@/model';
import { JobLogIM } from './jobLog.model';

/** job info model */
export interface JobIM extends SubBaseEntity<JobLogIM> {
  id: string;
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
export interface JobPM extends SubBaseEntity<JobLogIM> {
  id?: string;
  name?: string;
  jobGroup?: string;
  invokeTarget?: string;
  cronExpression?: string;
  misfirePolicy?: string;
  concurrent?: string;
  status?: string;
  remark?: string;
}

/** job page param model */
export type JobPPM = BasicPageParams & JobPM;

/** job list result model */
export type JobLRM = BasicFetchResult<JobIM>;
