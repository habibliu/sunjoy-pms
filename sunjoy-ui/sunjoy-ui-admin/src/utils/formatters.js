import store from "@/store"; // 导入 store
import dayjs from "dayjs"; //导入日期时间格式化库
/**
 * 在列表辊格式化显示region
 * @param  regionId
 */
export function formatRegion(regionId) {
  if (!regionId || regionId.length < 6) {
    return "";
  }
  //从缓存中取出regions
  const regionList = store.getters && store.getters.regions;

  if (regionList && Array.isArray(regionList) && regionList.length > 2) {
    const regionsMap = regionList.reduce((map, region) => {
      map.set(region.regionId, region); // 使用 regionId 作为键
      return map;
    }, new Map());

    let regionName = "";
    const province = regionId.slice(0, 2) + "0000";
    regionName = regionName.concat(regionsMap.get(province).regionName);
    const city = regionId.slice(0, 4) + "00";
    regionName = regionName.concat(regionsMap.get(city).regionName);

    if (Number.parseInt(regionId) > Number.parseInt(city)) {
      regionName = regionName.concat(regionsMap.get(regionId).regionName);
    }
    return regionName;
  } else {
    return regionId;
  }
}
/**
 * 日期时间格式化
 * @param {*} dateTime
 * @param {*} format
 */
export function formatDateTime(dateTime, format) {
  return dayjs(dateTime).format(format);
}

/**
 * 树状对象tree中找到匹配
 * @param {*} tree
 * @param {*} exitsId
 * @returns
 */
export function findLabelById(tree, matchId) {
  if (tree && Array.isArray(tree)) {
    for (let node of tree) {
      // 如果当前节点的 id 匹配，返回其 label
      if (node.id === matchId) {
        return node.label;
      }
      // 如果有子节点，递归查找
      if (node.children && node.children.length > 0) {
        const foundLabel = findLabelById(node.children, matchId);
        // 如果找到匹配的 label，返回
        if (foundLabel) {
          return foundLabel;
        }
      }
    }
  }
  // 如果没有找到，返回 matchId 或者其他指示值
  return matchId;
}
