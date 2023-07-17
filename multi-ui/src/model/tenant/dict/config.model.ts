import { BaseEntity, BasicFetchResult, BasicPageParams } from '@/model/basic';

/** config info model */
export interface ConfigIM extends BaseEntity {
  id: string;
  name: string;
  code: string;
  value: string;
  type: string;
  sort: number;
  remark: string;
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
