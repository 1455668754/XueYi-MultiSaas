import { OrganizeLM } from '@/model/system/organize';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  ORGANIZE_SCOPE = '/system/admin/organize/organizeScope',
  ORGANIZE_OPTION = '/system/admin/organize/option',
}

/** 获取企业部门|岗位树 */
export const organizeScopeApi = () => defHttp.get<OrganizeLM>({ url: Api.ORGANIZE_SCOPE });

/** 获取企业部门|岗位树 | 无部门叶子节点 */
export const organizeOptionApi = () => defHttp.get<OrganizeLM>({ url: Api.ORGANIZE_OPTION });
