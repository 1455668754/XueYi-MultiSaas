import { CodeImgIM, GetEnterpriseIM, GetUserIM, LoginIM, LoginPM } from '/@/model/sys';
import { defHttp } from '/@/utils/http/axios';
import { ErrorMessageMode } from '/#/axios';

enum Api {
  GetCodeImg = '/code',
  Login = '/auth/login',
  Logout = '/auth/logout',
  GetUserInfo = '/system/user/getInfo',
  GetEnterpriseInfo = '/system/enterprise/getInfo',
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
 * @description: user login api
 */
export function loginApi(params: LoginPM, mode: ErrorMessageMode = 'modal') {
  return defHttp.post<LoginIM>(
    {
      url: Api.Login,
      params,
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
