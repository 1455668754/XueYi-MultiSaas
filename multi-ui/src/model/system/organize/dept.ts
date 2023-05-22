import { BasicFetchResult, BasicPageParams, TreeEntity } from '/@/model/src';
import { RoleLM } from '../authority';

/** dept item model */
export interface DeptIM extends TreeEntity<DeptIM> {
  id: string | number;
  code: string;
  name: string;
  leader: string;
  phone: string;
  email: string;
  sort: number;
  status: string;
  roleIds: (string | number)[];
  roles: RoleLM;
}

/** dept list model */
export type DeptLM = DeptIM[];

/** dept param model */
export type DeptPM = DeptIM;

/** dept page param model */
export type DeptPPM = BasicPageParams & DeptPM;

/** dept list result model */
export type DeptLRM = BasicFetchResult<DeptIM>;
