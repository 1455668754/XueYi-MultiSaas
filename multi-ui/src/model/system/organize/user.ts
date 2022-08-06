import { BasicFetchResult, BasicPageParams, BaseEntity } from '/@/model/src';
import { RoleLM } from '../authority';
import { PostIM } from './post';

/** user item model */
export interface UserIM extends BaseEntity {
  id: string | number;
  postId: string | number;
  deptId: string | number;
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
  roleIds: (string | number)[];
  homePath?: string;
}

/** user list model */
export type UserLM = UserIM[];

/** user param model */
export type UserPM = UserIM;

/** user page param model */
export type UserPPM = BasicPageParams & UserPM;

/** user list result model */
export type UserLRM = BasicFetchResult<UserIM>;
