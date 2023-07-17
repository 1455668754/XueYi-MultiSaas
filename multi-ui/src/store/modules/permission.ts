import type { AppRouteRecordRaw, Menu } from '/@/router/types';
import { defineStore } from 'pinia';
import { store } from '/@/store';
import { useI18n } from '/@/hooks/web/useI18n';
import { useUserStore } from './user';
import { toRaw } from 'vue';
import { flatMultiLevelRoutes, transformObjToRoute } from '/@/router/helper/routeHelper';
import { transformRouteToMenu } from '/@/router/helper/menuHelper';
import { PageEnum, PermEnum } from '@/enums';
import { asyncRoutes } from '/@/router/routes';
import { filter } from '/@/utils/helper/treeHelper';
import { useMessage } from '/@/hooks/web/useMessage';
import { COMMON_MODULE, MODULE_CACHE } from '@/enums/system';
import { getMenuList } from '@/api/sys/menu.api';

interface PermissionState {
  // Permission code list
  permCodeList: string[] | number[];
  // has admin perm
  isAdminLessor: boolean;
  // Whether the route has been dynamically added
  // 路由是否动态添加
  isDynamicAddedRoute: boolean;
  // To trigger a menu update
  // 触发菜单更新
  lastBuildMenuTime: number;
  // black and reception menu list
  menuList: Menu[];
  // now module
  moduleId: string;
}

export const usePermissionStore = defineStore({
  id: 'app-permission',
  state: (): PermissionState => ({
    // 权限代码列表
    permCodeList: [],
    // has admin perm
    isAdminLessor: false,
    // Whether the route has been dynamically added
    // 路由是否动态添加
    isDynamicAddedRoute: false,
    // To trigger a menu update
    // 触发菜单更新
    lastBuildMenuTime: 0,
    // black and reception route
    menuList: [],
    // now module
    moduleId: sessionStorage.getItem(MODULE_CACHE) || COMMON_MODULE,
  }),
  getters: {
    getPermCodeList(state): string[] | number[] {
      return state.permCodeList;
    },
    getAdminLessor(state): boolean {
      return state.isAdminLessor;
    },
    getMenuList(state): Menu[] {
      return state.menuList;
    },
    getLastBuildMenuTime(state): number {
      return state.lastBuildMenuTime;
    },
    getIsDynamicAddedRoute(state): boolean {
      return state.isDynamicAddedRoute;
    },
    getModuleId(state): string {
      return state.moduleId;
    },
  },
  actions: {
    setPermCodeList(codeList: string[]) {
      this.permCodeList = codeList;
      this.setAdminLessor(codeList);
    },

    setAdminLessor(codeList: string[]) {
      this.isAdminLessor = codeList.includes(PermEnum.ADMIN);
    },

    setMenuList(list: Menu[]) {
      this.menuList = list;
      list?.length > 0 && this.setLastBuildMenuTime();
    },

    setLastBuildMenuTime() {
      this.lastBuildMenuTime = new Date().getTime();
    },

    setDynamicAddedRoute(added: boolean) {
      this.isDynamicAddedRoute = added;
    },

    setModuleId(moduleId: string | number) {
      this.moduleId = moduleId.toString();
    },

    resetState(): void {
      this.isDynamicAddedRoute = false;
      this.permCodeList = [];
      this.isAdminLessor = false;
      this.menuList = [];
      this.lastBuildMenuTime = 0;
      this.moduleId = sessionStorage.getItem(MODULE_CACHE) || COMMON_MODULE;
    },

    async changePermissionCode() {
      const userStore = useUserStore();
      const permList = toRaw(userStore.getPermCodeList) || [];
      this.setPermCodeList(permList);
    },

    // 构建路由
    async buildRoutesAction(): Promise<AppRouteRecordRaw[]> {
      const { t } = useI18n();
      const userStore = useUserStore();

      let routes: AppRouteRecordRaw[] = [];
      const roleList = toRaw(userStore.getRoleList) || [];
      // 路由过滤器 在 函数filter 作为回调传入遍历使用
      const routeFilter = (route: AppRouteRecordRaw) => {
        const { meta } = route;
        // 抽出角色
        const { roles } = meta || {};
        if (!roles) return true;
        // 进行角色权限判断
        return roleList.some((role) => roles.includes(role));
      };

      const routeRemoveIgnoreFilter = (route: AppRouteRecordRaw) => {
        const { meta } = route;
        // ignoreRoute 为true 则路由仅用于菜单生成，不会在实际的路由表中出现
        const { ignoreRoute } = meta || {};
        // arr.filter 返回 true 表示该元素通过测试
        return !ignoreRoute;
      };

      /**
       * @description 根据设置的首页path，修正routes中的affix标记（固定首页）
       * */
      const patchHomeAffix = (routes: AppRouteRecordRaw[]) => {
        if (!routes || routes.length === 0) return;
        let homePath: string = userStore.getUserInfo.homePath || PageEnum.BASE_HOME;

        function patcher(routes: AppRouteRecordRaw[], parentPath = '') {
          if (parentPath) parentPath = parentPath + '/';
          routes.forEach((route: AppRouteRecordRaw) => {
            const { path, children, redirect } = route;
            const currentPath = path.startsWith('/') ? path : parentPath + path;
            if (currentPath === homePath) {
              if (redirect) {
                homePath = route.redirect! as string;
              } else {
                route.meta = Object.assign({}, route.meta, { affix: true });
                throw new Error('end');
              }
            }
            children && children.length > 0 && patcher(children, currentPath);
          });
        }

        try {
          patcher(routes);
        } catch (e) {
          // 已处理完毕跳出循环
        }
        return;
      };

      const { createMessage } = useMessage();
      createMessage.loading(t('sys.app.menuLoading'));

      // !Simulate to obtain permission codes from the background,
      // this function may only need to be executed once, and the actual project can be put at the right time by itself
      routes = filter(asyncRoutes, routeFilter);
      routes = routes.filter(routeFilter);
      const receptionList = transformRouteToMenu(routes, true);
      receptionList.sort((a, b) => {
        return (a.meta?.orderNo || 0) - (b.meta?.orderNo || 0);
      });

      let backRouteList: AppRouteRecordRaw[] = [];
      try {
        this.changePermissionCode();
        backRouteList = (await getMenuList(this.getModuleId)) as AppRouteRecordRaw[];
      } catch (error) {
        console.error(error);
      }

      // Dynamically introduce components
      backRouteList = transformObjToRoute(backRouteList);

      //  Background routing to menu structure
      const backstageList = transformRouteToMenu(backRouteList);
      const linkRouteList = receptionList.concat(backstageList);
      this.setMenuList(linkRouteList);

      // remove meta.ignoreRoute item
      routes = filter(routes.concat(backRouteList), routeRemoveIgnoreFilter);
      routes = routes.filter(routeRemoveIgnoreFilter);

      routes = flatMultiLevelRoutes(routes);

      patchHomeAffix(routes);
      return routes;
    },
  },
});

// Need to be used outside the setup
// 需要在设置之外使用
export function usePermissionStoreWithOut() {
  return usePermissionStore(store);
}
