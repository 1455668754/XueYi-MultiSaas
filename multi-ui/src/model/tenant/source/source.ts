import { BasicFetchResult, BasicPageParams, BaseEntity } from '/@/model/src';

/** source info model */
export interface SourceIM extends BaseEntity {
  id: string | number;
  name: string;
  slave: string;
  driverClassName: string;
  urlPrepend: string;
  urlAppend: string;
  username: string;
  password: string;
  sort: number;
  status: string;
  remark: string;
  isDefault: string;
}

/** source list model */
export type SourceLM = SourceIM[];

/** source param model */
export type SourcePM = SourceIM;

/** source page param model */
export type SourcePPM = BasicPageParams & SourcePM;

/** source list result model */
export type SourceLRM = BasicFetchResult<SourceIM>;
