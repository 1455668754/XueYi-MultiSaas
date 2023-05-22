import { BasicFetchResult, BasicPageParams, TreeEntity } from '/@/model/src';

/** organize info model */
export interface OrganizeIM extends TreeEntity<OrganizeIM> {
  id: string | number;
  label: string;
  status: string;
  type: string;
}

/** organize list model */
export type OrganizeLM = OrganizeIM[];

/** organize param model */
export type OrganizePM = OrganizeIM;

/** organize page param model */
export type OrganizePPM = BasicPageParams & OrganizePM;

/** organize list result model */
export type OrganizeLRM = BasicFetchResult<OrganizeIM>;
