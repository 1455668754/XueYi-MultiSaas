import { AuthLM } from '@/model/system/authority';
import { defHttp } from '/@/utils/http/axios';

const basicApi = '/system/admin/auth';

enum Api {
  AUTH_SCOPE_COMMON = basicApi + '/common/authScope',
  AUTH_SCOPE_ENTERPRISE = basicApi + '/enterprise/authScope',
}

/** 查询公共权限范围树 */
export const authScopeCommonApi = () => defHttp.get<AuthLM>({ url: Api.AUTH_SCOPE_COMMON });

/** 查询企业权限范围树 */
export const authScopeEnterpriseApi = () => defHttp.get<AuthLM>({ url: Api.AUTH_SCOPE_ENTERPRISE });
