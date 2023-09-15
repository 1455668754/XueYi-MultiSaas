import { UserIM } from '@/model/system/organize';
import { defHttp } from '/@/utils/http/axios';
import { UploadIM } from '@/model/sys';
import { UploadFileParams } from '/#/axios';
import { useGlobSetting } from '/@/hooks/setting';
import { AxiosProgressEvent } from 'axios';

const { uploadUrl = '' } = useGlobSetting();

const basicApi = '/system/admin/user/profile';

enum Api {
  GET = basicApi,
  UPDATE = basicApi,
  RESET_NAME = basicApi + '/userName',
  RESET_PWD = basicApi + '/password',
  RESET_PHONE = basicApi + '/phone',
  RESET_EMAIL = basicApi + '/email',
  RESET_AVATAR = basicApi + '/avatar',
}

/** 查询用户个人信息 */
export const getUserProfileApi = () => defHttp.get<UserIM>({ url: Api.GET });

/** 修改用户个人信息 */
export const editUserProfileApi = (params: UserIM) => defHttp.put({ url: Api.UPDATE, params });

/** 用户账号修改 */
export const resetUserNameApi = (userName: string) =>
  defHttp.put(
    {
      url: Api.RESET_NAME,
      params: { userName },
    },
    { joinParamsToUrl: true },
  );

/** 密保手机绑定 */
export const resetUserPhoneApi = (phone: string) =>
  defHttp.put(
    {
      url: Api.RESET_PHONE,
      params: { phone },
    },
    { joinParamsToUrl: true },
  );

/** 备用邮箱绑定 */
export const resetUserEmailApi = (email: string) =>
  defHttp.put(
    {
      url: Api.RESET_EMAIL,
      params: { email },
    },
    { joinParamsToUrl: true },
  );

/** 密码重置 */
export const resetUserPwdApi = (oldPassword: string, newPassword: string) =>
  defHttp.put(
    {
      url: Api.RESET_PWD,
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
      url: uploadUrl + Api.RESET_AVATAR,
      onUploadProgress,
    },
    params,
  );
