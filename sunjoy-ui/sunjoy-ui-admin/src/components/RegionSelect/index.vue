<template>
    <div>
      <el-select v-model="selectedProvince" @change="onProvinceChange" placeholder="请选择省份" style="width: 30%;">
        <el-option
          v-for="(province, index) in provinces"
          :key="index"
          :label="province.regionName"
          :value="province.regionId">
        </el-option>
      </el-select>
  
      <el-select v-model="selectedCity" @change="onCityChange" placeholder="请选择城市" :disabled="!selectedProvince" style="width: 30%;">
        <el-option
          v-for="(city, index) in cities"
          :key="index"
          :label="city.regionName"
          :value="city.regionId">
        </el-option>
      </el-select>
  
      <el-select v-model="selectedDistrict" @change="onDistrictChange" placeholder="请选择区/县" :disabled="!selectedCity" style="width: 40%;">
        <el-option
          v-for="(district, index) in districts"
          :key="index"
          :label="district.regionName"
          :value="district.regionId">
        </el-option>
      </el-select>
    </div>
</template>
  
<script>
import {listRegions} from '@/api/system/region'
  export default {
    name: 'RegionSelect',
    props: ['region'],
    data() {
      return {
        provinces: [], // 省份数据
        cities: [], // 城市数据
        districts: [], // 区县数据
        selectedProvince: null,
        selectedCity: null,
        selectedDistrict: null,
        selectRegion: null
      };
    },
    created() {
      this.loadProvinces();
    },
    methods: {
      loadProvinces() {
      
        listRegions({}).then(resp=>{
           
            this.provinces=resp.data;
        });
        
      },
      onProvinceChange() {
        this.selectedCity = null;
        this.selectedDistrict = null;
        this.cities = this.getCitiesByProvince(this.selectedProvince);
        this.selectRegion=this.selectedProvince;
      },
      getCitiesByProvince(provinceCode) {
        
        listRegions({'parentId':provinceCode}).then(resp=>{
            this.cities=resp.data;
        });
       
        return this.cities || [];
      },
      onCityChange() {
        this.selectedDistrict = null;
        this.districts = this.getDistrictsByCity(this.selectedCity);
        this.selectRegion=this.selectedCity;
      },
      getDistrictsByCity(cityCode) {
        listRegions({'parentId':cityCode}).then(resp=>{
            this.districts=resp.data;
        });
        
        return this.districts || [];
      },
      onDistrictChange() {
        this.selectRegion=this.selectedDistrict;
        alert(this.selectRegion);
      },

      notifyChange() {
        this.$emit('change', {
            regionId: this.selectRegion
        });
      }
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
        region: {
            //从外部输入新的值，要正确显示
            handle(newVal,oldVal){
                if(newVal && newVal!=oldVal){
                    
                }
            },  
        }
    }
  };
</script>
  
<style scoped>
  /* 可以在这里添加样式 */
</style>