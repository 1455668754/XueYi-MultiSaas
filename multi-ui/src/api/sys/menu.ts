import { defHttp } from '/@/utils/http/axios';
import { GetMenuLM } from '/@/model/sys';
import { ModuleLM } from '/@/model/system';

enum Api {
  GetMenuList = '/system/menu/getRouters/',
  GetModuleList = '/system/module/getRouters',
}

/**
 * @description: Get user menu based on id
 */
export const getMenuList = (moduleId: string) => {
  return defHttp.get<GetMenuLM>({
    url: Api.GetMenuList,
    params: moduleId,
  });
};

/**
 * @description: Get user module based
 */
export const getModuleList = () => {
  return defHttp.get<ModuleLM>({
    url: Api.GetModuleList,
  });
};
