import { BasicFetchResult, BasicPageParams, TreeEntity } from '@/model/basic';

/** auth info model */
export interface AuthIM extends TreeEntity<AuthIM> {
  id: string;
  label: string;
  status: string;
  type: string;
}

/** auth list model */
export type AuthLM = AuthIM[];

/** auth param model */
export interface AuthPM extends TreeEntity<AuthIM> {
  id?: string;
  label?: string;
  status?: string;
  type?: string;
}

/** auth page param model */
export type AuthPPM = BasicPageParams & AuthPM;

/** auth list result model */
export type AuthLRM = BasicFetchResult<AuthIM>;
