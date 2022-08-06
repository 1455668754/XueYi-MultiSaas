import { BasicFetchResult, BasicPageParams, BaseEntity } from '/@/model/src';

/** tenant info model */
export interface TenantIM extends BaseEntity {
  id: string | number;
  strategyId: string | number;
  name: string;
  systemName: string;
  nick: string;
  logo: string;
  nameFrequency: number;
  isLessor: string;
  sort: number;
  status: string;
  remark: string;
  isDefault: string;
  authIds: (string | number)[];
}

/** tenant list model */
export type TenantLM = TenantIM[];

/** tenant param model */
export type TenantPM = TenantIM;

/** tenant page param model */
export type TenantPPM = BasicPageParams & TenantPM;

/** tenant list result model */
export type TenantLRM = BasicFetchResult<TenantIM>;
