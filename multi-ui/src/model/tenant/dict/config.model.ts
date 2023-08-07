import { BaseEntity, BasicFetchResult, BasicPageParams } from '@/model/basic';
import { DicYesNoEnum } from '@/enums';

/** config info model */
export interface ConfigIM extends BaseEntity {
  id: string;
  name: string;
  code: string;
  value: string;
  type: DicYesNoEnum;
}

/** config list model */
export type ConfigLM = ConfigIM[];

/** config param model */
export interface ConfigPM extends BaseEntity {
  id?: string;
  name?: string;
  code?: string;
  value?: string;
  type?: string;
}

/** config page param model */
export type ConfigPPM = BasicPageParams & ConfigPM;

/** config list result model */
export type ConfigLRM = BasicFetchResult<ConfigIM>;
