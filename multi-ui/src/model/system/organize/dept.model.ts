import { BasicFetchResult, BasicPageParams, TreeEntity } from '@/model';
import { RoleLM } from '../authority';
import { DicStatusEnum } from '@/enums';

/** dept item model */
export interface DeptIM extends TreeEntity<DeptIM> {
  /** Id */
  id: string;
  /** 编码 */
  code: string;
  /** 名称 */
  name: string;
  leader: string;
  phone: string;
  email: string;
  /** 状态（0正常 1停用） */
  status: DicStatusEnum;
  roleIds: string[];
  roles: RoleLM;
}

/** dept list model */
export type DeptLM = DeptIM[];

/** dept param model */
export interface DeptPM extends TreeEntity<DeptIM> {
  /** Id */
  id?: string;
  /** 编码 */
  code?: string;
  /** 名称 */
  name?: string;
  leader?: string;
  phone?: string;
  email?: string;
  /** 状态（0正常 1停用） */
  status?: DicStatusEnum;
  roleIds?: string[];
}

/** dept page param model */
export type DeptPPM = BasicPageParams & DeptPM;

/** dept list result model */
export type DeptLRM = BasicFetchResult<DeptIM>;
