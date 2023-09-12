import { BaseEntity, BasicFetchResult, BasicPageParams } from '@/model/basic';
import { DicStatusEnum, DicYesNoEnum } from '@/enums';
import { DictTypeIM } from '@/model/system/dict/index';

/** dictData info model */
export interface DictDataIM extends BaseEntity {
  id: string;
  code: string;
  value: string;
  label: string;
  additionalA: string;
  additionalB: string;
  additionalC: string;
  isDefault: DicYesNoEnum;
  cssClass: string;
  listClass: string;
  status: DicStatusEnum;
  /** 字典类型Id */
  dictTypeId?: string;
  /** 字典类型信息 */
  dictTypeInfo?: DictTypeIM;
}

/** dictData list model */
export type DictDataLM = DictDataIM[];

/** dictData param model */
export interface DictDataPM extends BaseEntity {
  code?: string;
  value?: string;
  label?: string;
  status?: string;
}

/** dictData page param model */
export type DictDataPPM = BasicPageParams & DictDataPM;

/** dictData list result model */
export type DictDataLRM = BasicFetchResult<DictDataIM>;
