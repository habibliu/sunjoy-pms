import request from "@/utils/request";
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询区域树结构数据
export function getRegionTree() {
  return request({
    url: "/system/tenant/regionTree",
    method: "get",
  });
}
//查询租户列表
export function listTenant(query) {
  return request({
    url: "/system/tenant/list",
    method: "get",
    params: query,
  });
}


// 新增租户
export function addTenant(data) {
    return request({
      url: '/system/tenant',
      method: 'post',
      data: data
    })
  }
  
  // 修改租户
  export function updateTenant(data) {
    return request({
      url: '/system/tenant',
      method: 'put',
      data: data
    })
  }

  // 查询租户详细
export function getPark(tenantId) {
    return request({
      url: '/system/tenant/' + parseStrEmpty(tenantId),
      method: 'get'
    })
  }