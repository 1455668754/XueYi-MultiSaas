import { defHttp } from '@/utils/http/axios';
import { UploadIM } from '@/model/sys';
import { UploadFileParams } from '#/axios';
import { useGlobSetting } from '@/hooks/setting';
import { AxiosProgressEvent } from 'axios';

const { uploadUrl = '' } = useGlobSetting();

enum Api {
  UPLOAD_FILE = '/file/admin/upload',
}

/** 文件上传 */
export const fileUploadApi = (
  params: UploadFileParams,
  onUploadProgress: (progressEvent: AxiosProgressEvent) => void,
) =>
  defHttp.uploadFile<UploadIM>(
    {
      url: uploadUrl + Api.UPLOAD_FILE,
      onUploadProgress,
    },
    params,
  );
