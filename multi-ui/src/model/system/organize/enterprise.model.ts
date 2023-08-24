import { BaseEntity, BasicFetchResult, BasicPageParams } from '@/model/basic';

/** enterprise item model */
export interface EnterpriseIM extends BaseEntity {
  id: string;
  name: string;
  status: string;
  type: string;
  nameFrequency: string;
  nick: string;
  systemName: string;
  isLessor: string;
  logo: string;
  isDefault: string;
  /** 企业权限组Ids */
  authGroupIds: string[];
}

/** enterprise list model */
export type EnterpriseLM = EnterpriseIM[];

/** enterprise param model */
export interface EnterprisePM extends BaseEntity {
  id?: string;
  name?: string;
  status?: string;
  type?: string;
  sort?: number;
  nameFrequency?: string;
  nick?: string;
  systemName?: string;
  isLessor?: string;
  logo?: string;
  isDefault?: string;
}

/** enterprise page param model */
export type EnterprisePPM = BasicPageParams & EnterprisePM;

/** enterprise list result model */
export type EnterpriseLRM = BasicFetchResult<EnterpriseIM>;
