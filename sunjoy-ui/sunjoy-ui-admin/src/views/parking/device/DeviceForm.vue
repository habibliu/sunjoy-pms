<template>
  <el-dialog :title="title" :visible.sync="dialogVisible" width="1440px" append-to-body>
    <div class="app-container">
    
      <el-row :gutter="20">
        <el-col :span="8">
          <el-tag>设备信息</el-tag>
          <el-form
            ref="form"
            :model="form"
            :rules="rules"
            label-width="80px"
            class="deviceForm"
          >
            <el-row>
              <el-col :span="24">
                <el-form-item label="设备名称" prop="deviceName">
                  <el-input
                    v-model="form.deviceName"
                    placeholder="请输入设备名称"
                    maxlength="30"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="经营单位" prop="opuId">
                  <Treeselect
                    v-model="form.opuId"
                    :options="opuOptions"
                    :show-count="true"
                    placeholder="请选择经营单位"
                    
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="设备型号 " prop="deviceModel">
                  <el-input
                    v-model="form.deviceModel"
                    placeholder="请输入设备型号"
                    maxlength="50"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="供应商" prop="vendor">
                  <el-input
                    v-model="form.vendor"
                    placeholder="请输入供应商名称"
                    maxlength="11"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="24">
                <el-form-item label="生产商" prop="producer">
                  <el-input
                    v-model="form.producer"
                    placeholder="请输入生产商名称"
                    maxlength="11"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="设备功能" prop="functions">
                  <el-checkbox-group
                    v-model="checkedCities"
                    @change="handleCheckedCitiesChange"
                  >
                    <el-checkbox
                      v-for="city in cities"
                      :label="city"
                      :key="city"
                      >{{ city }}</el-checkbox
                    >
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="24">
                <el-form-item label="状态">
                  <el-radio-group v-model="form.status">
                    <el-radio
                      v-for="dict in dict.type.sys_normal_disable"
                      :key="dict.value"
                      :label="dict.value"
                      >{{ dict.label }}</el-radio
                    >
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="备注">
                  <el-input
                    v-model="form.remark"
                    type="textarea"
                    placeholder="请输入内容"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-col>
        <el-col :span="16">
          <el-tag>设备参数</el-tag>
          <DeviceParam
            
          />
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <div class="dialog-footer">
            <el-button type="primary" @click="submitForm">确 定</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>
        </el-col>
      </el-row>
    </div>
  </el-dialog>
</template>
<script>
import { addDevice,updateDevice } from "@/api/parking/device";
import {opuTreeSelect} from '@/api/parking/park';
import Treeselect from "@riophae/vue-treeselect";
import DeviceParam  from "./DeviceParam.vue";
const cityOptions = ['放行', '显示', '广播', '感应','二维码展示','广告'];
export default {
  props: {
    dialogVisible: Boolean,
    title: String
  },
  name: "DeviceForm",
  dicts: ["sys_normal_disable"],
  components: { Treeselect,DeviceParam },
  
  data() {
    return {
        
        checkedCities: ['上海', '北京'],
        cities: cityOptions,
        form:{},

        opuOptions: [],
        // 表单校验
        rules: {
        deviceName: [
          { required: true, message: "设备名称不能为空", trigger: "blur" },
          {
            min: 2,
            max: 20,
            message: "设备名称长度必须介于 2 和 20 之间",
            trigger: "blur",
          },
        ],
        opuId: [
          { required: true, message: "经营单位不能为空", trigger: "blur" },
        ],

        phone: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
     /** 查询部门下拉树结构 */
     getOpuTree() {
     
        opuTreeSelect().then(response => {
        this.opuOptions = response.data;
        });
    },
    handleCheckedCitiesChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.cities.length;
      this.isIndeterminate =
        checkedCount > 0 && checkedCount < this.cities.length;
    },
    // 表单重置
    reset() {
      this.form = {
        status: "0",
      };
      checkedCities=[];
      this.resetForm("form");
    },
    submitForm(){
        this.$refs["form"].validate(valid => {
            if (valid) {
                let submitObj=this.form;
                if (submitObj.deviceId != undefined) {
                    updateDevice(submitObj).then(response => {
                    this.$modal.msgSuccess("修改成功");
                    //this.open = false;
                    closeForm("submit");
                    
                    });
                } else {
                    
                    addDevice(submitObj).then(response => {
                    this.$modal.msgSuccess("新增成功");
                    //this.open = false;
                    loseForm("submit");
                    
                    });
                }
            }
        });
    },
    cancel(){
        this.$modal.confirm("是否确认删除当前行的通道？").then(function () {
            closeForm("cancel");        
        });
    },
    closeForm(type){
        this.reset();
        this.$emit('close',type);

    }
  },
  created() {
    this.getOpuTree();
  }
};
</script>

<style scoped>
.dialog-footer{
  display: flex; 
  justify-content: flex-end;
}
</style>