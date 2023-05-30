import { AuthLM } from '/@/model/system';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  AUTH_SCOPE_ENTERPRISE = '/system/admin/auth/enterprise/authScope',
}

/** 查询企业权限范围树 */
export const authScopeEnterpriseApi = () => defHttp.get<AuthLM>({ url: Api.AUTH_SCOPE_ENTERPRISE });
