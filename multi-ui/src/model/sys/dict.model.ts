import { BaseEntity } from '/@/model/basic';

/** dict item model */
export interface DictIM extends BaseEntity {
  id: string;
  code: string;
  status: string;
  sort: number;
  value: string;
  label: string;
  cssClass: string;
  listClass: string;
  isDefault: string;
}

/** dict list model */
export type DictLM = DictIM[];
