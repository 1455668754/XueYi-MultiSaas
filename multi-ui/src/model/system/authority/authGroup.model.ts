import { BaseEntity, BasicFetchResult, BasicPageParams } from '@/model';
import { DicStatusEnum } from '@/enums';

/** authGroup info model */
export interface AuthGroupIM extends BaseEntity {
  /** 权限组Id */
  id: string;
  /** 权限组编码 */
  code: string;
  /** 权限组名称 */
  name: string;
  /** 权限组权限字符串 */
  authKey: string;
  /** 状态（0正常 1停用） */
  status: DicStatusEnum;
  /** 权限Ids */
  authIds: string[];
  /** 模块Ids */
  moduleIds: string[];
  /** 菜单Ids */
  menuIds: string[];
}

/** authGroup list model */
export type AuthGroupLM = AuthGroupIM[];

/** authGroup param model */
export interface AuthGroupPM extends BaseEntity {
  /** 权限组Id */
  id?: string;
  /** 权限组编码 */
  code?: string;
  /** 权限组名称 */
  name?: string;
  /** 权限组权限字符串 */
  authKey?: string;
  /** 状态（0正常 1停用） */
  status?: DicStatusEnum;
}

/** authGroup page param model */
export type AuthGroupPPM = BasicPageParams & AuthGroupPM;

/** authGroup list result model */
export type AuthGroupLRM = BasicFetchResult<AuthGroupIM>;
