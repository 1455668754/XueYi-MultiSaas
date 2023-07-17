import { BaseEntity, BasicFetchResult, BasicPageParams } from '/@/model/basic';

/** operateLog info model */
export interface OperateLogIM extends BaseEntity {
  id: string;
  title: string;
  businessType: string;
  method: string;
  requestMethod: string;
  operateType: string;
  userId: string;
  userName: string;
  userNick: string;
  url: string;
  ip: string;
  param: string;
  location: string;
  jsonResult: string;
  status: string;
  errorMsg: string;
  operateTime: string;
}

/** operateLog list model */
export type OperateLogLM = OperateLogIM[];

/** operateLog param model */
export interface OperateLogPM extends BaseEntity {
  id?: string;
  title?: string;
  businessType?: string;
  method?: string;
  requestMethod?: string;
  operateType?: string;
  userId?: string;
  userName?: string;
  userNick?: string;
  url?: string;
  ip?: string;
  param?: string;
  location?: string;
  jsonResult?: string;
  status?: string;
  errorMsg?: string;
  operateTime?: string;
}

/** operateLog page param model */
export type OperateLogPPM = BasicPageParams & OperateLogPM;

/** operateLog list result model */
export type OperateLogLRM = BasicFetchResult<OperateLogIM>;
