import {
  GenCodeLM,
  GenTableColumnLRM,
  GenTableIM,
  GenTableLRM,
  GenTablePPM,
} from '@/model/gen/generate';
import { defHttp } from '@/utils/http/axios';

const basicApi = '/code/admin/gen';

enum Api {
  LIST = basicApi + '/list',
  LIST_DB = basicApi + '/db/list',
  LIST_COLUMN = basicApi + '/column/',
  PREVIEW = basicApi + '/preview/',
  DOWNLOAD = basicApi + '/download/',
  GENERATE = basicApi + '/generate/',
  GET = basicApi + '/',
  GET_SUB = basicApi + '/sub/',
  IMPORT_DB = basicApi + '/importTable',
  EDIT = basicApi + '',
  DEL_BATCH = basicApi + '/batch/force/',
}

/** 查询业务表列表 */
export const listGenApi = (params?: GenTablePPM) =>
  defHttp.get<GenTableLRM>({ url: Api.LIST, params });

/** 查询生成表的字段列表 */
export const listGenColumnApi = (tableId: string) =>
  defHttp.get<GenTableColumnLRM>({
    url: Api.LIST_COLUMN,
    params: tableId,
  });

/** 查询数据库表列表 */
export const listDBGenApi = (params?: GenTablePPM) =>
  defHttp.get<GenTableLRM>({ url: Api.LIST_DB, params });

/** 查询生成表配置详细 */
export const getGenApi = (id: string) => defHttp.get<GenTableIM>({ url: Api.GET, params: id });

/** 查询生成表配置详细 | 带子数据 */
export const getSubGenApi = (id: string) =>
  defHttp.get<GenTableIM>({ url: Api.GET_SUB, params: id });

/** 预览代码 */
export const previewGenApi = (tableId: string) =>
  defHttp.get<GenCodeLM>({ url: Api.PREVIEW, params: tableId });

/** 生成代码（下载方式） */
export const downloadGenApi = async (tableId: string, title: string) =>
  defHttp.download<any>({ url: Api.DOWNLOAD, params: tableId }, title);

/** 生成代码（自定义路径） */
export const generateGenApi = (tableId: string) =>
  defHttp.get<GenCodeLM>({ url: Api.GENERATE, params: tableId });

/** 导入数据表 */
export const importDBGenApi = (names: string[], sourceName?: string) =>
  defHttp.post(
    {
      url: Api.IMPORT_DB,
      params: sourceName
        ? {
            tables: names.toString(),
            sourceName: sourceName,
          }
        : { tables: names.toString() },
    },
    { joinParamsToUrl: true },
  );

/** 修改数据表 */
export const editGenApi = (params: GenTableIM) => defHttp.put({ url: Api.EDIT, params });

/** 强制删除业务表 */
export const delForceGenApi = (ids: string | string[]) =>
  defHttp.delete({
    url: Api.DEL_BATCH,
    params: ids.toString(),
  });
