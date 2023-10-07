/** 参数字典编码：基础 */
export enum ConfigCodeEnum {
  // 系统模块:数据编码配置:功能开关
  SYS_CODE_SHOW = 'sys.code.show',
  // 系统模块:数据编码配置:必须字段
  SYS_CODE_MUST = 'sys.code.must',
}

/** 字典编码：基础 */
export enum DicCodeEnum {
  // 系统开关
  SYS_NORMAL_DISABLE = 'sys_normal_disable',
  // 是否
  SYS_YES_NO = 'sys_yes_no',
  // 功能状态
  SYS_FUNCTION_STATUS = 'sys_function_status',
  // 公共私有
  SYS_COMMON_PRIVATE = 'sys_common_private',
}

/** 字典：状态（0正常 1停用） */
export enum DicStatusEnum {
  // 正常
  NORMAL = '0',
  // 停用
  DISABLE = '1',
}

/** 字典：序号 */
export enum DicSortEnum {
  ZERO,
  ONE,
  TWO,
  THREE,
  FOUR,
  FIVE,
}

/** 字典：是否列表（Y是 N否） */
export enum DicYesNoEnum {
  // 是
  YES = 'Y',
  // 否
  NO = 'N',
}

/** 字典：显隐列表（0显示 1隐藏） */
export enum DicShowHideEnum {
  // 显示
  SHOW = '0',
  // 隐藏
  HIDE = '1',
}

/** 字典：公共私有列表（0公共 1私有） */
export enum DicCommonPrivateEnum {
  // 公共
  COMMON = '0',
  // 私有
  PRIVATE = '1',
}
