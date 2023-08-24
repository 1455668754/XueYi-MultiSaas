import { BaseEntity, BasicFetchResult, BasicPageParams } from '@/model/basic';
import { DicStatusEnum } from '@/enums';

/** role info model */
export interface RoleIM extends BaseEntity {
  id: string;
  code: string;
  name: string;
  roleKey: string;
  dataScope: string;
  /** 状态（0正常 1停用） */
  status: DicStatusEnum;
  /** 权限Ids */
  authIds: string[];
  /** 模块Ids */
  moduleIds: string[];
  /** 菜单Ids */
  menuIds: string[];
  /** 组织Ids */
  organizeIds: string[];
}

/** role list model */
export type RoleLM = RoleIM[];

/** role param model */
export interface RolePM extends BaseEntity {
  id?: string;
  code?: string;
  name?: string;
  roleKey?: string;
  dataScope?: string;
  status?: DicStatusEnum;
}

/** role page param model */
export type RolePPM = BasicPageParams & RolePM;

/** role list result model */
export type RoleLRM = BasicFetchResult<RoleIM>;
