import { defHttp } from '/@/utils/http/axios';
import { DictLM } from '/@/model/sys';

enum Api {
  DIC_CONFIG = '/system/config/code/',
  DICT = '/system/dict/data/type/',
  DICT_LIST = '/system/dict/data/types/',
}

/** 参数查询 */
export const dicConfig = (params: string) => defHttp.get<string>({ url: Api.DIC_CONFIG, params });

/** 字典查询 */
export const dicDict = (params: string) => defHttp.get<DictLM>({ url: Api.DICT, params });

/** 字典批量查询 */
export const dicDictList = (dictCodeList: string[]) =>
  defHttp.get<Map<string, DictLM>>({
    url: Api.DICT_LIST,
    params: dictCodeList.toString(),
  });
