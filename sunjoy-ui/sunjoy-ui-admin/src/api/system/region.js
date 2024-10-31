import request from "@/utils/request";
import { parseStrEmpty } from "@/utils/ruoyi";

//查询地区列表
export function listRegions(query) {
    return request({
      url: "/system/region/list",
      method: "get",
      params: query,
    });
  }
  