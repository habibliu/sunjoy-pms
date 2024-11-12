import { listAllRegions } from "@/api/system/region";
const region = {
  state: {
    regions: [], // 存储省市区的列表数据
  },
  mutations: {
    setRegions(state, regions) {
      state.regions = regions; // 更新数据
    },
  },
  actions: {
    fetchRegions({ commit }) {
      
      listAllRegions().then((resp) => {
     
        commit("setRegions", resp.data); // 提交 mutation 更新状态
      });
    },
  },
  //getters: {
  //  regions: state => state.regions // 获取省市区数据
  //}
};
export default region;
