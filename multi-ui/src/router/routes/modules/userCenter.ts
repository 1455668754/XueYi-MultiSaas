import type { AppRouteModule } from '/@/router/types';
import { LAYOUT } from '/@/router/constant';
import { t } from '/@/hooks/web/useI18n';

const userCenter: AppRouteModule = {
  path: '/userCenter',
  name: 'UserCenter',
  component: LAYOUT,
  redirect: '/userCenter/index',
  meta: {
    hideChildrenInMenu: true,
    icon: 'simple-icons:about-dot-me',
    title: t('layout.header.userCenter'),
    orderNo: 100000,
    hideMenu: true,
  },
  children: [
    {
      path: 'index',
      name: 'AboutPage',
      component: () => import('/@/views/sys/userCenter/index.vue'),
      meta: {
        title: t('layout.header.userCenter'),
        icon: 'simple-icons:about-dot-me',
        hideMenu: true,
      },
    },
  ],
};

export default userCenter;
