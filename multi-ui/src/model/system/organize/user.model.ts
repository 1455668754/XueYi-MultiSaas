import { BaseEntity, BasicFetchResult, BasicPageParams } from '/@/model/basic';
import { RoleLM } from '../authority';
import { PostIM } from './post.model';

/** user item model */
export interface UserIM extends BaseEntity {
  id: string;
  postId: string;
  deptId: string;
  code: string;
  userName: string;
  nickName: string;
  userType: string;
  status: string;
  sort: number;
  avatar: string;
  phone: string;
  email: string;
  sex: string;
  profile: string;
  login_ip: string;
  login_date: Date;
  post: PostIM;
  roles: RoleLM;
  roleIds: string[];
  homePath?: string;
}

/** user list model */
export type UserLM = UserIM[];

/** user param model */
export interface UserPM extends BaseEntity {
  id?: string;
  postId?: string;
  deptId?: string;
  code?: string;
  userName?: string;
  nickName?: string;
  userType?: string;
  status?: string;
  sort?: number;
  avatar?: string;
  phone?: string;
  email?: string;
  sex?: string;
  profile?: string;
  login_ip?: string;
  login_date?: Date;
  roleIds?: string[];
  homePath?: string;
}

/** user page param model */
export type UserPPM = BasicPageParams & UserPM;

/** user list result model */
export type UserLRM = BasicFetchResult<UserIM>;
