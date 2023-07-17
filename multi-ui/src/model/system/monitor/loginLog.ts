import { BaseEntity, BasicFetchResult, BasicPageParams } from '/@/model/src';

/** loginLog info model */
export interface LoginLogIM extends BaseEntity {
  id: string;
  enterpriseName: string;
  userName: string;
  userId: string;
  ipaddr: string;
  status: string;
  msg: string;
  accessTime: string;
}

/** loginLog list model */
export type LoginLogLM = LoginLogIM[];

/** loginLog param model */
export interface LoginLogPM extends BaseEntity {
  id?: string;
  enterpriseName?: string;
  userName?: string;
  userId?: string;
  ipaddr?: string;
  status?: string;
  msg?: string;
  accessTime?: string;
  accessTimeStart?: string;
  accessTimeEnd?: string;
}

/** loginLog page param model */
export type LoginLogPPM = BasicPageParams & LoginLogPM;

/** loginLog list result model */
export type LoginLogLRM = BasicFetchResult<LoginLogIM>;
