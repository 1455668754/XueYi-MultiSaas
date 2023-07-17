import { BaseEntity, BasicFetchResult, BasicPageParams } from '/@/model/src';

/** strategy info model */
export interface StrategyIM extends BaseEntity {
  id: string;
  name: string;
  sourceId: string;
  sourceSlave: string;
  sort: number;
  status: string;
  remark: string;
  isDefault: string;
}

/** strategy list model */
export type StrategyLM = StrategyIM[];

/** strategy param model */
export interface StrategyPM extends BaseEntity {
  id?: string;
  name?: string;
  sourceId?: string;
  sourceSlave?: string;
  status?: string;
  isDefault?: string;
}

/** strategy page param model */
export type StrategyPPM = BasicPageParams & StrategyPM;

/** strategy list result model */
export type StrategyLRM = BasicFetchResult<StrategyIM>;
