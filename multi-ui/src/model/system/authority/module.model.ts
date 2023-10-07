import { BasicFetchResult, BasicPageParams, SubBaseEntity } from '@/model';
import { MenuIM } from './menu.model';
import { DicCommonPrivateEnum, DicShowHideEnum, DicStatusEnum, DicYesNoEnum } from '@/enums';
import { EnterpriseIM } from '@/model/system/organize';
import { FrameTypeEnum } from '@/enums/system/authority';

/** module info model */
export interface ModuleIM extends SubBaseEntity<MenuIM> {
  /** Id */
  id: string;
  /** 模块名称 */
  name: string;
  /** 模块Logo */
  logo: string;
  /** 路由地址 */
  path: string;
  /** 路由参数 */
  paramPath: string;
  /** 模块类型（0常规 1内嵌 2外链） */
  type: FrameTypeEnum;
  /** 模块显隐状态（Y隐藏 N显示） */
  hideModule: DicShowHideEnum;
  /** 状态（0启用 1禁用） */
  status: DicStatusEnum;
  /** 公共数据（Y是 N否） */
  isCommon: DicCommonPrivateEnum;
  /** 默认模块（Y是 N否） */
  isDefault: DicYesNoEnum;
  /** 企业Id */
  tenantId?: string;
  /** 企业信息 */
  enterpriseInfo?: EnterpriseIM;
}

/** module list model */
export type ModuleLM = ModuleIM[];

/** module param model */
export interface ModulePM extends SubBaseEntity<MenuIM> {
  /** Id */
  id?: string;
  /** 模块名称 */
  name?: string;
  /** 模块类型（0常规 1内嵌 2外链） */
  type?: FrameTypeEnum;
  /** 模块显隐状态（Y隐藏 N显示） */
  hideModule?: DicShowHideEnum;
  /** 状态（0启用 1禁用） */
  status?: DicStatusEnum;
  /** 公共数据（Y是 N否） */
  isCommon?: DicCommonPrivateEnum;
  /** 默认模块（Y是 N否） */
  isDefault?: DicYesNoEnum;
}

/** module page param model */
export type ModulePPM = BasicPageParams & ModulePM;

/** module list result model */
export type ModuleLRM = BasicFetchResult<ModuleIM>;
