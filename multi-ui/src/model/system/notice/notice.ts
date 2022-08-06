import { BasicFetchResult, BasicPageParams, BaseEntity } from '/@/model/src';

/** notice info model */
export interface NoticeIM extends BaseEntity {
  id: string | number;
  name: string;
  type: string;
  content: string;
  status: string;
  remark: string;
}

/** notice list model */
export type NoticeLM = NoticeIM[];

/** notice param model */
export type NoticePM = NoticeIM;

/** notice page param model */
export type NoticePPM = BasicPageParams & NoticePM;

/** notice list result model */
export type NoticeLRM = BasicFetchResult<NoticeIM>;
