import { BasicFetchResult, BasicPageParams, BaseEntity } from '/@/model/src';

/** strategy info model */
export interface StrategyIM extends BaseEntity {
  id: string | number;
  name: string;
  sourceId: string | number;
  sourceSlave: string;
  sort: number;
  status: string;
  remark: string;
  isDefault: string;
}

/** strategy list model */
export type StrategyLM = StrategyIM[];

/** strategy param model */
export type StrategyPM = StrategyIM;

/** strategy page param model */
export type StrategyPPM = BasicPageParams & StrategyPM;

/** strategy list result model */
export type StrategyLRM = BasicFetchResult<StrategyIM>;
