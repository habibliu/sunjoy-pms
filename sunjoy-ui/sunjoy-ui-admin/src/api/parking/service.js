import request from "@/utils/request";
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询车场服务列表
export function listParkService(query) {
  return request({
    url: "/parkmodel/service/list",
    method: "get",
    params: query,
  });
}

// 查询车场服务列表
export function getParkServices(parkId,status) {
  let requestUrl= "/parkmodel/service/" + parseStrEmpty(parkId);
  if(status){
    requestUrl=requestUrl+"/"+parseStrEmpty(status);
  }
  return request({
    url: requestUrl,
    method: "get"
  });
}


// 批量新增车场服务
export function batchAddParkServices(list) {
    return request({
      url: '/parkmodel/service/batch',
      method: 'post',
      data: list
    })
  }
/**
 * 
 * @param {*} serviceId 
 * @returns 
 */
export function changeParkServiceStatus(serviceId){
  return request({
    url: '/parkmodel/service/changeStatus/'+ parseStrEmpty(serviceId),
    method: 'put'
  })
}