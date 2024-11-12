<template>
  <div>
    <el-select
      v-model="selectedProvince"
      @change="onProvinceChange"
      placeholder="请选择省份"
      style="width: 30%"
    >
      <el-option
        v-for="(province, index) in provinces"
        :key="index"
        :label="province.regionName"
        :value="province.regionId"
      >
      </el-option>
    </el-select>

    <el-select
      v-model="selectedCity"
      @change="onCityChange"
      placeholder="请选择城市"
      :disabled="!selectedProvince"
      style="width: 30%"
    >
      <el-option
        v-for="(city, index) in cities"
        :key="index"
        :label="city.regionName"
        :value="city.regionId"
      >
      </el-option>
    </el-select>

    <el-select
      v-model="selectedDistrict"
      @change="onDistrictChange"
      placeholder="请选择区/县"
      :disabled="!selectedCity"
      style="width: 40%"
    >
      <el-option
        v-for="(district, index) in districts"
        :key="index"
        :label="district.regionName"
        :value="district.regionId"
      >
      </el-option>
    </el-select>
  </div>
</template>

<script>
import { listRegions } from "@/api/system/region";
import store from "@/store"; // 导入 store
export default {
  name: "RegionSelect",
  props: {
    inputRegion: {
      type: String,
      required: false,
    },
  },
  data() {
    return {
      provinces: [], // 省份数据
      cities: [], // 城市数据
      districts: [], // 区县数据
      selectedProvince: null,
      selectedCity: null,
      selectedDistrict: null,
      selectRegion: null,
    };
  },
  created() {
    this.loadProvinces();
  },
  methods: {
    loadProvinces() {
      listRegions({}).then((resp) => {
        this.provinces = resp.data;
      });
    },
    onProvinceChange() {
      this.selectedCity = null;
      this.selectedDistrict = null;
      this.cities = this.getCitiesByProvince(this.selectedProvince);
      this.selectRegion = this.selectedProvince;
    },
    getCitiesByProvince(provinceCode) {
      listRegions({ parentId: provinceCode }).then((resp) => {
        this.cities = resp.data;
      });

      return this.cities || [];
    },
    onCityChange() {
      this.selectedDistrict = null;
      this.districts = this.getDistrictsByCity(this.selectedCity);
      this.selectRegion = this.selectedCity;
    },
    getDistrictsByCity(cityCode) {
      listRegions({ parentId: cityCode }).then((resp) => {
        this.districts = resp.data;
      });

      return this.districts || [];
    },
    onDistrictChange() {
      this.selectRegion = this.selectedDistrict;
    
    },

    notifyChange() {
      this.$emit("change", {
        regionId: this.selectRegion,
      });
    },
  },
  watch: {
    selectedProvince(val) {
      this.getCitiesByProvince(val);
      this.notifyChange();
    },
    selectedCity(val) {
      this.getDistrictsByCity(val);
      this.notifyChange();
    },
    selectedDistrict(val) {
      this.notifyChange();
    },
    inputRegion: {
      //从外部输入新的值，要正确显示
      handler(newVal, oldVal) {
        
        if (newVal && newVal != oldVal) {
          const regionList = store.getters && store.getters.regions;
          if (regionList && Array.isArray(regionList)) {
            const province = newVal.slice(0, 2) + "0000";
            const city = newVal.slice(0, 4) + "00";
            this.selectedProvince = province;//会触发selectedProvince的watch事件
            this.selectedCity = city;//会触发selectedCity的watch事件
            if (Number.parseInt(newVal) > Number.parseInt(city)) {
                this.selectedDistrict=newVal;
            }
          }
        }
      },
      deep: true, // 如果需要深度监听，特别是对象
      mmediate: true, // 立即执行
    },
  },
};
</script>

<style scoped>
/* 可以在这里添加样式 */
</style>
