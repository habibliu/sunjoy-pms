<template>
    <div>
      <el-select v-model="selectedProvince" @change="onProvinceChange" placeholder="请选择省份" style="width: 30%;">
        <el-option
          v-for="(province, index) in provinces"
          :key="index"
          :label="province.name"
          :value="province.code">
        </el-option>
      </el-select>
  
      <el-select v-model="selectedCity" @change="onCityChange" placeholder="请选择城市" :disabled="!selectedProvince" style="width: 30%;">
        <el-option
          v-for="(city, index) in cities"
          :key="index"
          :label="city.name"
          :value="city.code">
        </el-option>
      </el-select>
  
      <el-select v-model="selectedDistrict" placeholder="请选择区/县" :disabled="!selectedCity" style="width: 40%;">
        <el-option
          v-for="(district, index) in districts"
          :key="index"
          :label="district.name"
          :value="district.code">
        </el-option>
      </el-select>
    </div>
</template>
  
<script>
  export default {
    name: 'RegionSelect',
    props: ['province', 'city', 'district'],
    data() {
      return {
        provinces: [], // 省份数据
        cities: [], // 城市数据
        districts: [], // 区县数据
        selectedProvince: null,
        selectedCity: null,
        selectedDistrict: null,
      };
    },
    created() {
      this.loadProvinces();
    },
    methods: {
      loadProvinces() {
        // 省份数据（示例数据）
        this.provinces = [
          { code: '110000', name: '北京市' },
          { code: '120000', name: '天津市' },
          { code: '130000', name: '河北省' },
          { code: '440000', name: '广东省' },
          // 其他省份...
        ];
      },
      onProvinceChange() {
        this.selectedCity = null;
        this.selectedDistrict = null;
        this.cities = this.getCitiesByProvince(this.selectedProvince);
      },
      getCitiesByProvince(provinceCode) {
        // 城市数据（示例数据）
        const cityData = {
          '110000': [{ code: '110101', name: '东城区' }, { code: '110102', name: '西城区' }],
          '120000': [{ code: '120101', name: '和平区' }, { code: '120102', name: '河东区' }],
          '130000': [{ code: '130100', name: '石家庄市' }],
          '440000': [{ code: '440100', name: '广州市' }, { code: '440300', name: '深圳市' }],
        };
        return cityData[provinceCode] || [];
      },
      onCityChange() {
        this.selectedDistrict = null;
        this.districts = this.getDistrictsByCity(this.selectedCity);
      },
      getDistrictsByCity(cityCode) {
        // 区县数据（示例数据）
        const districtData = {
          '110101': [{ code: '110101001', name: '东华门街道' }, { code: '110101002', name: '景山街道' }],
          '110102': [{ code: '110102001', name: '新街口街道' }],
          '440100': [{ code: '440100001', name: '越秀区' }, { code: '440100002', name: '荔湾区' }],
          '440300': [{ code: '440300001', name: '福田区' }],
        };
        return districtData[cityCode] || [];
      },
      notifyChange() {
        this.$emit('change', {
            province: this.selectedProvince,
            city: this.selectedCity,
            district: this.selectedDistrict,
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
        }
    }
  };
</script>
  
<style scoped>
  /* 可以在这里添加样式 */
</style>