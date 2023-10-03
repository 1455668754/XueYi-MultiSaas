import { defHttp } from '@/utils/http/axios';
import { DictLM } from '@/model/sys';

enum Api {
  CONFIG = '/system/admin/config/config',
  CONFIG_LIST = '/system/admin/config/configs',
  DICT = '/system/admin/dict/type/type/',
  DICT_LIST = '/system/admin/dict/type/types/',
}

/** 参数查询 */
export const dicConfig = (code: string) =>
  defHttp.get<string>({
    url: Api.CONFIG,
    params: { code: code },
  });

/** 参数批量查询 */
export const dicConfigList = (codeList: string[]) =>
  defHttp.get<string>({
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
