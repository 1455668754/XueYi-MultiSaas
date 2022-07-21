import { BasicFetchResult, BasicPageParams, BaseEntity } from '/@/model/src';

/** loginLog info model */
export interface LoginLogIM extends BaseEntity {
  id: string | number;
  enterpriseName: string;
  userName: string;
  userId: string | number;
  ipaddr: string;
  status: string;
  msg: string;
  accessTime: any;
}

/** loginLog list model */
export type LoginLogLM = LoginLogIM[];

/** loginLog param model */
export type LoginLogPM = LoginLogIM;

/** loginLog page param model */
export type LoginLogPPM = BasicPageParams & LoginLogPM;

/** loginLog list result model */
export type LoginLogLRM = BasicFetchResult<LoginLogIM>;
