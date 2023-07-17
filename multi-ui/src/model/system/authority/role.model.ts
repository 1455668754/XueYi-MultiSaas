import { BaseEntity, BasicFetchResult, BasicPageParams } from '/@/model/basic';

/** role info model */
export interface RoleIM extends BaseEntity {
  id: string;
  code: string;
  name: string;
  roleKey: string;
  dataScope: string;
  sort: number;
  status: string;
  remark: string;
  authIds: string[];
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
  sort?: number;
  status?: string;
  remark?: string;
  authIds?: string[];
  organizeIds?: string[];
}

/** role page param model */
export type RolePPM = BasicPageParams & RolePM;

/** role list result model */
export type RoleLRM = BasicFetchResult<RoleIM>;
