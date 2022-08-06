import type { RouteRecordRaw } from 'vue-router';
import { usePermissionStore } from '/@/store/modules/permission';
import { useTabs } from './useTabs';
import { router, resetRouter } from '/@/router';
import { intersection } from 'lodash-es';
import { isArray } from '/@/utils/is';
import { useMultipleTabStore } from '/@/store/modules/multipleTab';

// User permissions related operations
export function usePermission() {
  const permissionStore = usePermissionStore();
  const { closeAll } = useTabs(router);

  /**
   * Reset and regain authority resource information
   * 重置和重新获得权限资源信息
   */
  async function resume() {
    const tabStore = useMultipleTabStore();
    tabStore.clearCacheTabs();
    resetRouter();
    const routes = await permissionStore.buildRoutesAction();
    routes.forEach((route) => {
      router.addRoute(route as unknown as RouteRecordRaw);
    });
    permissionStore.setLastBuildMenuTime();
    closeAll();
  }

  /**
   * Determine whether there is permission
   */
  function hasPermission(value?: string | string[], def = true): boolean {
    // Visible by default
    if (!value) {
      return def;
    }

    // check is admin lessor ?
    if (permissionStore.getAdminLessor) {
      return true;
    }

    const allCodeList = permissionStore.getPermCodeList as string[];
    if (!isArray(value)) {
      return allCodeList.includes(value);
    }
    return (intersection(value, allCodeList) as string[]).length > 0;
  }

  /**
   * refresh menu data
   */
  async function refreshMenu() {
    resume();
  }

  return { hasPermission, refreshMenu };
}
