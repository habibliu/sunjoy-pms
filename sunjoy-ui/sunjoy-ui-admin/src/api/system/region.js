import request from "@/utils/request";
import { parseStrEmpty } from "@/utils/ruoyi";
import { mapGetters } from 'vuex';

//查询地区列表
export function listRegions(query) {
    return request({
      url: "/system/region/list",
      method: "get",
      params: query,
    });
  }
  

//查询全部地区列表
export function listAllRegions() {
    return request({
      url: "/system/region/list/all",
      method: "get"
      
    });
  }

  /**
   * 在列表辊格式化显示region
   * @param  regionId 
   */
export function formatRegion(regionId){

}