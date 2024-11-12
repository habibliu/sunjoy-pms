import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询车辆
export function listVehicle(query) {
  return request({
    url: '/parkmodel/vehicle/list',
    method: 'get',
    params: query
  });
}
