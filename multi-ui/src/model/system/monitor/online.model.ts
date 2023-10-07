import { BaseEntity, BasicFetchResult, BasicPageParams } from '@/model';

/** online info model */
export interface OnlineIM extends BaseEntity {
  tokenId: string;
  userName: string;
  userNick: string;
  ipaddr: string;
  loginLocation: string;
  browser: string;
  os: string;
  loginTime: string;
}

/** online list model */
export type OnlineLM = OnlineIM[];

/** online param model */
export interface OnlinePM extends BaseEntity {
  tokenId?: string;
  userName?: string;
  userNick?: string;
  ipaddr?: string;
  loginLocation?: string;
  browser?: string;
  os?: string;
  loginTime?: string;
}

/** online page param model */
export type OnlinePPM = BasicPageParams & OnlinePM;

/** online list result model */
export type OnlineLRM = BasicFetchResult<OnlineIM>;
