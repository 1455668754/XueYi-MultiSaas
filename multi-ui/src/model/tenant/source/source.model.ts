import { BaseEntity, BasicFetchResult, BasicPageParams } from '@/model';
import { DicStatusEnum, DicYesNoEnum } from '@/enums';

/** source info model */
export interface SourceIM extends BaseEntity {
  id: string;
  name: string;
  slave: string;
  driverClassName: string;
  urlPrepend: string;
  urlAppend: string;
  username: string;
  password: string;
  status: DicStatusEnum;
  isDefault: DicYesNoEnum;
}

/** source list model */
export type SourceLM = SourceIM[];

/** source param model */
export interface SourcePM extends BaseEntity {
  id?: string;
  name?: string;
  slave?: string;
  driverClassName?: string;
  username?: string;
  status?: DicStatusEnum;
  isDefault?: DicYesNoEnum;
}

/** source page param model */
export type SourcePPM = BasicPageParams & SourcePM;

/** source list result model */
export type SourceLRM = BasicFetchResult<SourceIM>;
