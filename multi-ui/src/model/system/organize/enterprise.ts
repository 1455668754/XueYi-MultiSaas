import { BasicFetchResult, BasicPageParams, BaseEntity } from '/@/model/src';

/** enterprise item model */
export interface EnterpriseIM extends BaseEntity {
  id: string | number;
  name: string;
  status: string;
  type: string;
  sort: number;
  nameFrequency: string;
  nick: string;
  systemName: string;
  isLessor: string;
  logo: string;
  isDefault: string;
}

/** enterprise list model */
export type EnterpriseLM = EnterpriseIM[];

/** enterprise param model */
export type EnterprisePM = EnterpriseIM;

/** enterprise page param model */
export type EnterprisePPM = BasicPageParams & EnterprisePM;

/** enterprise list result model */
export type EnterpriseLRM = BasicFetchResult<EnterpriseIM>;
