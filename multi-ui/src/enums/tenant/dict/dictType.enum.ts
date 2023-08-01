/** 字典类型主页路由名称 */
export const DictTypeIndexGo = 'b08569b9c6044608913cae26f427f842';

/** 字典类型详情页路由名称 */
export const DictTypeDetailGo = 'bf7ca66a708f4ee88d5e58df65b558fb';

/** 字典数据主页路由名称 */
export const DictDataIndexGo = '72f4012cdc744c048367c50de2bf603b';

/** 字典编码：字典 */
export enum DicCodeDictEnum {
  // 字典管理-数据类型
  SYS_DICT_DATA_TYPE = 'sys_dict_data_type',
  // 字典管理-缓存类型
  SYS_DICT_CACHE_TYPE = 'sys_dict_cache_type',
}

/** 字典：字典数据类型列表（0默认 1只增 2只减 3只读） */
export enum DicDataTypeEnum {
  // 默认
  DEFAULT = '0',
  // 只增
  INCREASE = '1',
  // 只减
  SUBTRACT = '2',
  // 只读
  READ = '3',
}

/** 字典：缓存类型列表（0租户 1全局） */
export enum DicCacheTypeEnum {
  // 租户
  TENANT = '0',
  // 全局
  OVERALL = '1',
}
