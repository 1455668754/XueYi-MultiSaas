import {BaseEntity, BasicFetchResult, BasicPageParams} from '@/model/basic';

/** tenant info model */
export interface TenantIM extends BaseEntity {
  id: string;
  strategyId: string;
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
  authIds: string[];
}

/** tenant list model */
export type TenantLM = TenantIM[];

/** tenant param model */
export interface TenantPM extends BaseEntity {
  id?: string;
  strategyId?: string;
  name?: string;
  systemName?: string;
  nick?: string;
  nameFrequency?: number;
  isLessor?: string;
  status?: string;
  isDefault?: string;
  authIds?: string[];
}

/** tenant page param model */
export type TenantPPM = BasicPageParams & TenantPM;

/** tenant list result model */
export type TenantLRM = BasicFetchResult<TenantIM>;
