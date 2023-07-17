import { BasicFetchResult, BasicPageParams, TreeEntity } from '@/model/basic';
import { RoleLM } from '../authority';

/** dept item model */
export interface DeptIM extends TreeEntity<DeptIM> {
  id: string;
  code: string;
  name: string;
  leader: string;
  phone: string;
  email: string;
  sort: number;
  status: string;
  roleIds: string[];
  roles: RoleLM;
}

/** dept list model */
export type DeptLM = DeptIM[];

/** dept param model */
export interface DeptPM extends TreeEntity<DeptIM> {
  id?: string;
  code?: string;
  name?: string;
  leader?: string;
  phone?: string;
  email?: string;
  status?: string;
  roleIds?: string[];
}

/** dept page param model */
export type DeptPPM = BasicPageParams & DeptPM;

/** dept list result model */
export type DeptLRM = BasicFetchResult<DeptIM>;
