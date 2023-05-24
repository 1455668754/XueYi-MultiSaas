import { UserIM } from '/@/model/system';
import { defHttp } from '/@/utils/http/axios';
import { UploadIM } from '/@/model/sys';
import { UploadFileParams } from '/#/axios';
import { useGlobSetting } from '/@/hooks/setting';
import { AxiosProgressEvent } from 'axios';

const { uploadUrl = '' } = useGlobSetting();

enum Api {
  GET_USER_PROFILE = '/system/admin/user/profile',
  UPDATE_USER_PROFILE = '/system/admin/user/profile',
  RESET_USER_NAME = '/system/admin/user/profile/userName',
  RESET_USER_PWD = '/system/admin/user/profile/password',
  RESET_USER_PHONE = '/system/admin/user/profile/phone',
  RESET_USER_EMAIL = '/system/admin/user/profile/email',
  RESET_USER_AVATAR = '/system/admin/user/profile/avatar',
}

/** 查询用户个人信息 */
export const getUserProfileApi = () => defHttp.get<UserIM>({ url: Api.GET_USER_PROFILE });

/** 修改用户个人信息 */
export const editUserProfileApi = (params: UserIM) =>
  defHttp.put({ url: Api.UPDATE_USER_PROFILE, params });

/** 用户账号修改 */
export const resetUserNameApi = (userName: string) =>
  defHttp.put(
    {
      url: Api.RESET_USER_NAME,
      params: { userName },
    },
    { joinParamsToUrl: true },
  );

/** 密保手机绑定 */
export const resetUserPhoneApi = (phone: string) =>
  defHttp.put(
    {
      url: Api.RESET_USER_PHONE,
      params: { phone },
    },
    { joinParamsToUrl: true },
  );

/** 备用邮箱绑定 */
export const resetUserEmailApi = (email: string) =>
  defHttp.put(
    {
      url: Api.RESET_USER_EMAIL,
      params: { email },
    },
    { joinParamsToUrl: true },
  );

/** 密码重置 */
export const resetUserPwdApi = (oldPassword: string, newPassword: string) =>
  defHttp.put(
    {
      url: Api.RESET_USER_PWD,
      params: { oldPassword, newPassword },
    },
    { joinParamsToUrl: true },
  );

/** 用户头像上传 */
export const editAvatarApi = (
  params: UploadFileParams,
  onUploadProgress: (progressEvent: AxiosProgressEvent) => void,
) =>
  defHttp.uploadFile<UploadIM>(
    {
      url: uploadUrl + Api.RESET_USER_AVATAR,
      onUploadProgress,
    },
    params,
  );
