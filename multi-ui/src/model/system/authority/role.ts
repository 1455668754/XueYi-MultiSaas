import { BasicFetchResult, BasicPageParams, BaseEntity } from '/@/model/src';

/** role info model */
export interface RoleIM extends BaseEntity {
  id: string | number;
  code: string;
  name: string;
  roleKey: string;
  dataScope: string;
  sort: number;
  status: string;
  remark: string;
  authIds: (string | number)[];
  organizeIds: (string | number)[];
}

/** role list model */
export type RoleLM = RoleIM[];

/** role param model */
export type RolePM = RoleIM;

/** role page param model */
export type RolePPM = BasicPageParams & RolePM;

/** role list result model */
export type RoleLRM = BasicFetchResult<RoleIM>;
