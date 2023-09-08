import { CodeImgIM, GetEnterpriseIM, GetUserIM, LoginIM, LoginPM } from '@/model/sys';
import { defHttp } from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';
import { ContentTypeEnum } from '@/enums/basic';

enum Api {
  GetCodeImg = '/code',
  Login = '/auth/login',
  OauthLogin = '/auth/oauth2/token',
  Logout = '/auth/logout',
  GetUserInfo = '/system/admin/user/getInfo',
  GetEnterpriseInfo = '/system/admin/enterprise/getInfo',
}

/**
 * @description: code img api
 */
export function getCodeImg() {
  return defHttp.get<CodeImgIM>(
    { url: Api.GetCodeImg, timeout: 20000 },
    { errorMessageMode: 'none', withToken: false },
  );
}

/**
 * @description: user oauth2 login api
 */
export function oauthLoginApi(params: LoginPM, mode: ErrorMessageMode = 'modal') {
  params.grant_type = 'password';
  params.account_type = 'admin';
  params.scope = 'server';
  const basicAuth = 'Basic ' + window.btoa('xueyi:xueyi');
  return defHttp.post<LoginIM>(
    {
      headers: {
        Authorization: basicAuth,
        'Content-Type': ContentTypeEnum.FORM_URLENCODED,
      },
      url: Api.OauthLogin,
      data: params,
    },
    {
      errorMessageMode: mode,
      withToken: false,
    },
  );
}

/**
 * @description: get user info
 */
export function getUserInfo() {
  return defHttp.get<GetUserIM>({ url: Api.GetUserInfo }, { errorMessageMode: 'none' });
}

/**
 * @description: get enterprise info
 */
export function getEnterpriseInfo() {
  return defHttp.get<GetEnterpriseIM>({ url: Api.GetEnterpriseInfo }, { errorMessageMode: 'none' });
}

/**
 * @description: login out
 */
export function doLogout() {
  return defHttp.delete({ url: Api.Logout });
}
