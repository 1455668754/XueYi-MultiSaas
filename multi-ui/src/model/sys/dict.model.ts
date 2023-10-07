import { BaseEntity } from '@/model';

/** dict item model */
export interface DictIM extends BaseEntity {
  id: string;
  code: string;
  status: string;
  value: string;
  label: string;
  additionalA: string;
  additionalB: string;
  additionalC: string;
  cssClass: string;
  listClass: string;
  isDefault: string;
}

/** dict list model */
export type DictLM = DictIM[];
