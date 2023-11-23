export enum ScreenSizeEnum {
  XS = 'XS',
  SM = 'SM',
  MD = 'MD',
  LG = 'LG',
  XL = 'XL',
  XXL = 'XXL',
}

export enum ScreenEnum {
  XS = 320,
  SM = 640,
  MD = 768,
  LG = 960,
  XL = 1280,
  XXL = 1536,
}

const screenMap = new Map<ScreenSizeEnum, number>();

screenMap.set(ScreenSizeEnum.XS, ScreenEnum.XS);
screenMap.set(ScreenSizeEnum.SM, ScreenEnum.SM);
screenMap.set(ScreenSizeEnum.MD, ScreenEnum.MD);
screenMap.set(ScreenSizeEnum.LG, ScreenEnum.LG);
screenMap.set(ScreenSizeEnum.XL, ScreenEnum.XL);
screenMap.set(ScreenSizeEnum.XXL, ScreenEnum.XXL);

export { screenMap };
