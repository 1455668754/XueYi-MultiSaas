import { BaseEntity, BasicFetchResult, BasicPageParams } from '@/model';
import { RoleLM } from '../authority';
import { DeptIM } from './dept.model';
import { DicStatusEnum } from '@/enums';

/** post item model */
export interface PostIM extends BaseEntity {
  /** Id */
  id: string;
  /** 归属部门Id */
  deptId: string;
  /** 编码 */
  code: string;
  /** 名称 */
  name: string;
  /** 状态（0正常 1停用） */
  status: DicStatusEnum;
  dept: DeptIM;
  roles: RoleLM;
  roleIds: string[];
}

/** post list model */
export type PostLM = PostIM[];

/** post param model */
export interface PostPM extends BaseEntity {
  /** Id */
  id?: string;
  /** 归属部门Id */
  deptId?: string;
  /** 编码 */
  code?: string;
  /** 名称 */
  name?: string;
  /** 状态（0正常 1停用） */
  status?: DicStatusEnum;
  roleIds?: string[];
}

/** post page param model */
export type PostPPM = BasicPageParams & PostPM;

/** post list result model */
export type PostLRM = BasicFetchResult<PostIM>;
