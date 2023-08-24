import { BasicFetchResult, BasicPageParams, SubBaseEntity } from '@/model/basic';
import { MenuIM } from './menu.model';

/** module info model */
export interface ModuleIM extends SubBaseEntity<MenuIM> {
  id: string;
  name: string;
  logo: string;
  path: string;
  paramPath: string;
  type: string;
  hideModule: string;
  status: string;
  isCommon: string;
  isDefault: string;
}

/** module list model */
export type ModuleLM = ModuleIM[];

/** module param model */
export interface ModulePM extends SubBaseEntity<MenuIM> {
  id?: string;
  name?: string;
  logo?: string;
  path?: string;
  paramPath?: string;
  type?: string;
  hideModule?: string;
  status?: string;
  isCommon?: string;
  isDefault?: string;
}

/** module page param model */
export type ModulePPM = BasicPageParams & ModulePM;

/** module list result model */
export type ModuleLRM = BasicFetchResult<ModuleIM>;
