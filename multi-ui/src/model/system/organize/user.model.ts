import { BaseEntity, BasicFetchResult, BasicPageParams } from '@/model';
import { RoleLM } from '../authority';
import { PostIM } from './post.model';
import { DicStatusEnum } from '@/enums';

/** user item model */
export interface UserIM extends BaseEntity {
  /** Id */
  id: string;
  postId: string;
  deptId: string;
  /** 编码 */
  code: string;
  userName: string;
  nickName: string;
  userType: string;
  /** 状态（0正常 1停用） */
  status: DicStatusEnum;
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
  /** Id */
  id?: string;
  postId?: string;
  deptId?: string;
  /** 编码 */
  code?: string;
  userName?: string;
  nickName?: string;
  userType?: string;
  /** 状态（0正常 1停用） */
  status?: DicStatusEnum;
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
