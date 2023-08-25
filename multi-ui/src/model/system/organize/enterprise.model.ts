import { BaseEntity, BasicFetchResult, BasicPageParams } from '@/model/basic';
import { DicStatusEnum } from '@/enums';

/** enterprise item model */
export interface EnterpriseIM extends BaseEntity {
  /** Id */
  id: string;
  /** 名称 */
  name: string;
  /** 状态（0正常 1停用） */
  status: DicStatusEnum;
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
  /** Id */
  id?: string;
  /** 名称 */
  name?: string;
  /** 状态（0正常 1停用） */
  status?: DicStatusEnum;
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
