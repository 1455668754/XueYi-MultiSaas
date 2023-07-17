import { BasicFetchResult, BasicPageParams, TreeEntity } from '@/model/basic';

/** organize info model */
export interface OrganizeIM extends TreeEntity<OrganizeIM> {
  id: string;
  label: string;
  status: string;
  type: string;
}

/** organize list model */
export type OrganizeLM = OrganizeIM[];

/** organize param model */
export interface OrganizePM extends TreeEntity<OrganizeIM> {
  id?: string;
  label?: string;
  status?: string;
  type?: string;
}

/** organize page param model */
export type OrganizePPM = BasicPageParams & OrganizePM;

/** organize list result model */
export type OrganizeLRM = BasicFetchResult<OrganizeIM>;
