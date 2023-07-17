import { EnterpriseIM, UserIM } from '../system';

/**
 * @description: Login interface parameters
 */
export interface LoginPM {
  enterpriseName?: string;
  userName: string;
  password: string;
  code: string;
  uuid: string;
  grant_type?: string;
  account_type?: string;
  scope?: string;
}

/**
 * @description: CodeImg interface return value
 */
export interface CodeImgIM {
  captchaOnOff: boolean;
  img: string;
  uuid: string;
}

/**
 * @description: Login interface return value
 */
export interface LoginIM {
  access_token: string;
  expires_in: number;
  data: {};
}

/**
 * @description: Get user information return value
 */
export interface GetUserIM {
  user: UserIM;
  enterprise: EnterpriseIM;
  roles: [];
  permissions: [];
  routes: Map<string, string>;
}

/**
 * @description: Get enterprise information return value
 */
export type GetEnterpriseIM = EnterpriseIM;
