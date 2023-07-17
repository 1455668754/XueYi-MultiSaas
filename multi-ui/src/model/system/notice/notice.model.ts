import { BaseEntity, BasicFetchResult, BasicPageParams } from '/@/model/basic';

/** notice info model */
export interface NoticeIM extends BaseEntity {
  id: string;
  name: string;
  type: string;
  content: string;
  status: string;
  remark: string;
}

/** notice list model */
export type NoticeLM = NoticeIM[];

/** notice param model */
export interface NoticePM extends BaseEntity {
  id?: string;
  name?: string;
  type?: string;
  content?: string;
  status?: string;
  remark?: string;
}

/** notice page param model */
export type NoticePPM = BasicPageParams & NoticePM;

/** notice list result model */
export type NoticeLRM = BasicFetchResult<NoticeIM>;
