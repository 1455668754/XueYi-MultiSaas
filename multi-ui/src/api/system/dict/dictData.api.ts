import { defHttp } from '@/utils/http/axios';
import { DictDataIM, DictDataLRM, DictDataPPM } from '@/model/system/dict';

const basicApi = '/system/admin/dict/data';

enum Api {
  LIST = basicApi + '/list',
  GET = basicApi + '/',
  ADD = basicApi,
  EDIT = basicApi,
  EDIT_STATUS = basicApi + '/status',
  DEL_BATCH = basicApi + '/batch/',
}

/** 查询字典数据列表 */
export const listDictDataApi = (params?: DictDataPPM) =>
  defHttp.get<DictDataLRM>({ url: Api.LIST, params });

/** 查询字典数据详细 */
export const getDictDataApi = (id: string) => defHttp.get<DictDataIM>({ url: Api.GET, params: id });

/** 新增字典数据 */
export const addDictDataApi = (params: DictDataIM) => defHttp.post({ url: Api.ADD, params });

/** 修改字典数据 */
export const editDictDataApi = (params: DictDataIM) => defHttp.put({ url: Api.EDIT, params });

/** 修改字典数据状态 */
export const editStatusDictDataApi = (id: string, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS,
    params: { id: id, status: status },
  });

/** 删除字典数据 */
export const delDictDataApi = (ids: string[]) =>
  defHttp.delete({
    url: Api.DEL_BATCH,
    params: ids.toString(),
  });
