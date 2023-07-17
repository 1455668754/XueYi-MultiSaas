export const SIDE_BAR_MINI_WIDTH = 48;
export const SIDE_BAR_SHOW_TIT_MINI_WIDTH = 80;

export enum ContentEnum {
  // auto width
  FULL = 'full',
  // fixed width
  FIXED = 'fixed',
}

// menu theme enum
export enum ThemeEnum {
  DARK = 'dark',
  LIGHT = 'light',
}

export enum SettingButtonPositionEnum {
  AUTO = 'auto',
  HEADER = 'header',
  FIXED = 'fixed',
}

export enum SessionTimeoutProcessingEnum {
  ROUTE_JUMP,
  PAGE_COVERAGE,
}

// tenant type enum
export enum TenantTypeEnum {
  // 租管租户
  ADMIN = 'Y',
  // 普通租户
  NORMAL = 'N',
}

// user type enum
export enum UserTypeEnum {
  // 超管用户
  ADMIN = '00',
  // 普通用户
  NORMAL = '01',
}

export enum PermEnum {
  // super admin
  ADMIN = '*:*:*',
}

export enum DescItemSizeEnum {
  DEFAULT = 24,
  SMALL = 12,
}

export enum ComponentSizeEnum {
  DEFAULT = 'default',
  SMALL = 'small',
  LARGE = 'large',
}

export enum ComponentSizeValueEnum {
  DEFAULT = 48,
  SMALL = 16,
  LARGE = 64,
}

// icon enum
export enum IconEnum {
  VIEW = 'ant-design:file-search-outlined',
  ADD = 'ant-design:plus-outlined',
  IMPORT = 'ant-design:vertical-align-top-outlined',
  EXPORT = 'ant-design:vertical-align-bottom-outlined',
  EDIT = 'clarity:note-edit-line',
  AUTH = 'ant-design:safety-certificate-outlined',
  DATA = 'clarity:note-edit-line',
  DELETE = 'ant-design:delete-outlined',
  SEARCH = 'ant-design:search-outlined',
  RESET = 'ant-design:sync-outlined',
  UPLOAD = 'ant-design:cloud-upload-outlined',
  DOWNLOAD = 'ant-design:cloud-download-outlined',
  PREVIEW = 'ant-design:eye-outlined',
  ADD_FOLD = 'ant-design:folder-add-outlined',
  LOG = 'ant-design:exception-outlined',
  PASSWORD = 'ant-design:key-outlined',
}

// color enum
export enum ColorEnum {
  PINK = 'pink',
  RED = 'red',
  ORANGE = 'orange',
  GREEN = 'green',
  CYAN = 'cyan',
  BLUE = 'blue',
  PURPLE = 'purple',
}

// Route switching animation
// 路由切换动画
export enum RouterTransitionEnum {
  ZOOM_FADE = 'zoom-fade',
  ZOOM_OUT = 'zoom-out',
  FADE_SIDE = 'fade-slide',
  FADE = 'fade',
  FADE_BOTTOM = 'fade-bottom',
  FADE_SCALE = 'fade-scale',
}
