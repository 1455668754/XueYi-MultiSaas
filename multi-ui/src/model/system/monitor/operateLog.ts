import { BasicFetchResult, BasicPageParams, BaseEntity } from '/@/model/src';

/** operateLog info model */
export interface OperateLogIM extends BaseEntity {
  id: string | number;
  title: string;
  businessType: string;
  method: string;
  requestMethod: string;
  operateType: string;
  userId: string | number;
  userName: string | number;
  userNick: string | number;
  url: string;
  ip: string;
  param: string;
  location: string;
  jsonResult: string;
  status: string;
  errorMsg: string;
  operateTime: any;
}

/** operateLog list model */
export type OperateLogLM = OperateLogIM[];

/** operateLog param model */
export type OperateLogPM = OperateLogIM;

/** operateLog page param model */
export type OperateLogPPM = BasicPageParams & OperateLogPM;

/** operateLog list result model */
export type OperateLogLRM = BasicFetchResult<OperateLogIM>;
