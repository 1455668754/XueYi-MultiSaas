import { BasicFetchResult, BasicPageParams, BaseEntity } from '/@/model/src';

/** config info model */
export interface ConfigIM extends BaseEntity {
  id: string | number;
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
export type ConfigPM = ConfigIM;

/** config page param model */
export type ConfigPPM = BasicPageParams & ConfigPM;

/** config list result model */
export type ConfigLRM = BasicFetchResult<ConfigIM>;
