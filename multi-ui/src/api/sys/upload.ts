import { defHttp } from '/@/utils/http/axios';
import { UploadIM } from '/@/model/sys';
import { UploadFileParams } from '/#/axios';
import { useGlobSetting } from '/@/hooks/setting';

const { uploadUrl = '' } = useGlobSetting();

enum Api {
  UPLOAD_FILE = '/file/upload',
}


/** 文件上传 */
export const fileUploadApi = (
  params: UploadFileParams,
  onUploadProgress: (progressEvent: ProgressEvent) => void,
) =>
  defHttp.uploadFile<UploadIM>(
    {
      url: uploadUrl + Api.UPLOAD_FILE,
      onUploadProgress,
    },
    params,
  );
