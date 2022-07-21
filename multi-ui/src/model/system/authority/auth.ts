import { TreeEntity, BasicFetchResult, BasicPageParams } from '/@/model/src';

/** auth info model */
export interface AuthIM extends TreeEntity<AuthIM> {
  id: string | number;
  label: string;
  status: string;
  type: string;
}

/** auth list model */
export type AuthLM = AuthIM[];

/** auth param model */
export type AuthPM = AuthIM;

/** auth page param model */
export type AuthPPM = BasicPageParams & AuthPM;

/** auth list result model */
export type AuthLRM = BasicFetchResult<AuthIM>;
