import { BasicFetchResult, BasicPageParams, TreeEntity } from '@/model';
import { OrganizeTypeEnum } from '@/enums/system/organize';
import { DicStatusEnum } from '@/enums';

/** organize info model */
export interface OrganizeIM extends TreeEntity<OrganizeIM> {
  /** Id */
  id: string;
  /** 名称 */
  label: string;
  /** 状态（0正常 1停用） */
  status: DicStatusEnum;
  /** 类型（0企业 1部门 2岗位 3用户） */
  type: OrganizeTypeEnum;
}

/** organize list model */
export type OrganizeLM = OrganizeIM[];

/** organize param model */
export interface OrganizePM extends TreeEntity<OrganizeIM> {
  /** Id */
  id?: string;
  /** 名称 */
  label?: string;
  /** 状态（0正常 1停用） */
  status?: DicStatusEnum;
  /** 类型（0企业 1部门 2岗位 3用户） */
  type?: OrganizeTypeEnum;
}

/** organize page param model */
export type OrganizePPM = BasicPageParams & OrganizePM;

/** organize list result model */
export type OrganizeLRM = BasicFetchResult<OrganizeIM>;
