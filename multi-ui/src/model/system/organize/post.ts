import { BaseEntity, BasicFetchResult, BasicPageParams } from '/@/model/src';
import { RoleLM } from '../authority';
import { DeptIM } from './dept';

/** post item model */
export interface PostIM extends BaseEntity {
  id: string;
  deptId: string;
  code: string;
  name: string;
  sort: number;
  status: string;
  dept: DeptIM;
  roles: RoleLM;
  roleIds: string[];
}

/** post list model */
export type PostLM = PostIM[];

/** post param model */
export interface PostPM extends BaseEntity {
  id?: string;
  deptId?: string;
  code?: string;
  name?: string;
  sort?: number;
  status?: string;
  roleIds?: string[];
}

/** post page param model */
export type PostPPM = BasicPageParams & PostPM;

/** post list result model */
export type PostLRM = BasicFetchResult<PostIM>;
