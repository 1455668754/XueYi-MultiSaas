import { defHttp } from '@/utils/http/axios';
import { DictLM } from '@/model/sys';

const basicApi = '/system/admin';

enum Api {
  CONFIG = basicApi + '/config/config',
  CONFIG_LIST = basicApi + '/config/configs',
  DICT = basicApi + '/dict/type/type/',
  DICT_LIST = basicApi + '/dict/type/types/',
}

/** 参数查询 */
export const dicConfig = (code: string) =>
  defHttp.get<Map<string, string>>({
    url: Api.CONFIG,
    params: { code: code },
  });

/** 参数批量查询 */
export const dicConfigList = (codeList: string[]) =>
  defHttp.get<Map<string, string>>({
    url: Api.CONFIG_LIST,
    params: { codeList: codeList.toString() },
  });

/** 字典查询 */
export const dicDict = (params: string) => defHttp.get<DictLM>({ url: Api.DICT, params });

/** 字典批量查询 */
export const dicDictList = (dictCodeList: string[]) =>
  defHttp.get<Map<string, DictLM>>({
    url: Api.DICT_LIST,
    params: dictCodeList.toString(),
  });
