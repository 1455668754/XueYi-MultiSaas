import { BaseEntity, BasicFetchResult, BasicPageParams } from '@/model/basic';
import { DicStatusEnum } from '@/enums';
import { DataScopeEnum } from '@/enums/system/authority';

/** role info model */
export interface RoleIM extends BaseEntity {
  /** Id */
  id: string;
  /** 编码 */
  code: string;
  /** 名称 */
  name: string;
  /** 角色权限字符串 */
  roleKey: string;
  /** 数据范围 */
  dataScope: DataScopeEnum;
  /** 状态（0正常 1停用） */
  status: DicStatusEnum;
  /** 权限Ids */
  authIds: string[];
  /** 模块Ids */
  moduleIds: string[];
  /** 菜单Ids */
  menuIds: string[];
  /** 组织Ids（数据权限） */
  organizeIds?: string[];
  /** 组织-部门Ids（数据权限） */
  orgDeptIds: string[];
  /** 组织-岗位Ids（数据权限） */
  orgPostIds: string[];
}

/** role list model */
export type RoleLM = RoleIM[];

/** role param model */
export interface RolePM extends BaseEntity {
  /** Id */
  id?: string;
  /** 编码 */
  code?: string;
  /** 名称 */
  name?: string;
  /** 角色权限字符串 */
  roleKey?: string;
  /** 数据范围 */
  dataScope?: DataScopeEnum;
  /** 状态（0正常 1停用） */
  status?: DicStatusEnum;
}

/** role page param model */
export type RolePPM = BasicPageParams & RolePM;

/** role list result model */
export type RoleLRM = BasicFetchResult<RoleIM>;
