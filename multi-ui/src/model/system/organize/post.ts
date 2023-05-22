import { BasicFetchResult, BasicPageParams, BaseEntity } from '/@/model/src';
import { RoleLM } from '../authority';
import { DeptIM } from './dept';

/** post item model */
export interface PostIM extends BaseEntity {
  id: string | number;
  deptId: string | number;
  code: string;
  name: string;
  sort: number;
  status: string;
  dept: DeptIM;
  roles: RoleLM;
  roleIds: (string | number)[];
}

/** post list model */
export type PostLM = PostIM[];

/** post param model */
export type PostPM = PostIM;

/** post page param model */
export type PostPPM = BasicPageParams & PostPM;

/** post list result model */
export type PostLRM = BasicFetchResult<PostIM>;
