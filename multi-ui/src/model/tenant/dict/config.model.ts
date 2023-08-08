import { BaseEntity, BasicFetchResult, BasicPageParams } from '@/model/basic';
import { DicStatusEnum, DicYesNoEnum } from '@/enums';
import { DicCacheTypeEnum, DicDataTypeEnum } from '@/enums/tenant';
import { EnterpriseIM } from '@/model/system';

/** config info model */
export interface ConfigIM extends BaseEntity {
  id: string;
  name: string;
  code: string;
  value: string;
  type: DicYesNoEnum;
  /** 数据类型（0默认 1只增 2只减 3只读） */
  dataType: DicDataTypeEnum;
  /** 缓存类型（0租户 1全局） */
  cacheType: DicCacheTypeEnum;
  /** 状态（0启用 1禁用） */
  status: DicStatusEnum;
  /** 企业Id */
  tenantId?: string;
  /** 企业信息 */
  enterpriseInfo?: EnterpriseIM;
}

/** config list model */
export type ConfigLM = ConfigIM[];

/** config param model */
export interface ConfigPM extends BaseEntity {
  id?: string;
  name?: string;
  code?: string;
  type?: string;
  /** 数据类型（0默认 1只增 2只减 3只读） */
  dataType?: DicDataTypeEnum;
  /** 缓存类型（0租户 1全局） */
  cacheType?: DicCacheTypeEnum;
  /** 状态（0启用 1禁用） */
  status?: DicStatusEnum;
  tenantId?: string;
  tenantIds?: string[];
}

/** config page param model */
export type ConfigPPM = BasicPageParams & ConfigPM;

/** config list result model */
export type ConfigLRM = BasicFetchResult<ConfigIM>;
