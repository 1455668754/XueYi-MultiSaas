import { isEmpty, isNotEmpty } from '@/utils/is';

/**
 *
 * @param treeList 树列表
 * @param nodeKeys 节点集合
 * @param nodeName 节点名称
 * @param childrenName 子节点集合名称
 * @param level 层级
 * @return 层级节点集合
 */
export function getTreeNodes(
  treeList: any[],
  nodeName: string,
  childrenName: string,
  nodeKeys?: string[],
  level?: number,
): string[] {
  // 递归函数获取指定层级的所有节点
  function getNodesAtLevel(
    treeNode: any,
    nodeName: string,
    childrenName: string,
    allSub: boolean,
    currentLevel = 0,
    nodeKeys?: string[],
    level?: number,
  ): string[] {
    const node = treeNode[nodeName];
    const allKey = nodeKeys === undefined;
    const hasNode = isNotEmpty(node) && !allKey ? nodeKeys.includes(node) : false;
    allSub = !allSub ? hasNode : allSub;
    if (isEmpty(level)) {
      if (isEmpty(treeNode[childrenName]) || treeNode[childrenName].length === 0) {
        return allSub || hasNode || allKey ? [node] : [];
      }
    } else {
      if (currentLevel === level) {
        return allSub || hasNode || allKey ? [node] : [];
      }
    }

    if (!treeNode[childrenName] || treeNode[childrenName].length === 0) {
      return [];
    }

    let nodes: string[] = [];
    for (const child of treeNode[childrenName]) {
      const subNodes = getNodesAtLevel(
        child,
        nodeName,
        childrenName,
        allSub,
        currentLevel + 1,
        nodeKeys,
        level,
      );
      nodes = nodes.concat(subNodes);
    }
    return nodes;
  }

  if (
    isEmpty(treeList) ||
    (treeList as any[]).length === 0 ||
    isEmpty(nodeName) ||
    isEmpty(childrenName)
  ) {
    return [];
  }
  let nodes: string[] = [];
  for (const treeNode of treeList as any[]) {
    const subNodes = getNodesAtLevel(treeNode, nodeName, childrenName, false, 0, nodeKeys, level);
    nodes = nodes.concat(subNodes);
  }
  return nodes;
}
