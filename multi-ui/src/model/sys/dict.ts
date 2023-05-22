import { BaseEntity } from '/@/model/src';

/** dict item model */
export interface DictIM extends BaseEntity {
  id: string | number;
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
