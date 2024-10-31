<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="1024px"
    append-to-body
  >
    <div class="app-container">
      <el-row :gutter="20">
        <el-form
          :model="queryParams"
          ref="queryForm"
          size="small"
          :inline="true"
          v-show="showSearch"
          label-width="68px"
        >
          <el-form-item label="设备名称">
            <el-input
              v-model="queryParams.deviceName"
              placeholder="请输入设备名称"
              clearable
              style="width: 160px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="设备型号">
            <el-input
              v-model="queryParams.deviceModel"
              placeholder="请输入设备型号"
              clearable
              style="width: 160px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="供应商">
            <el-input
              v-model="queryParams.vendor"
              placeholder="请输入供应商名称"
              clearable
              style="width: 160px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="设备功能">
            <el-select
              v-model="queryParams.functions"
              placeholder="设备功能"
              clearable
              style="width: 200px"
            >
              <el-option
                v-for="dict in dict.type.pms_device_functions"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="设备状态">
            <el-select
              v-model="queryParams.status"
              placeholder="设备状态"
              clearable
              style="width: 100px"
            >
              <el-option
                v-for="dict in dict.type.sys_normal_disable"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="创建时间">
            <el-date-picker
              v-model="dateRange"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              icon="el-icon-search"
              size="mini"
              @click="handleQuery"
              >搜索</el-button
            >
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
              >重置</el-button
            >
          </el-form-item>
        </el-form>

        <el-table
          v-loading="loading"
          :data="deviceList"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column
            label="设备编号"
            align="center"
            key="deviceId"
            prop="deviceId"
            v-if="columns[0].visible"
            width="100"
          />
          <el-table-column
            label="设备名称"
            align="center"
            key="deviceName"
            prop="deviceName"
            v-if="columns[1].visible"
            :show-overflow-tooltip="true"
          />
         
          <el-table-column
            label="设备型号"
            align="center"
            key="deviceModel"
            prop="deviceModel"
            v-if="columns[3].visible"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="设备功能"
            align="center"
            key="functions"
            prop="functions"
            v-if="columns[4].visible"
            width="120"
            :formatter="formatFunctions"
          />
          <el-table-column
            label="经营单位"
            align="center"
            key="opuName"
            prop="opuName"
            v-if="columns[2].visible"
            :show-overflow-tooltip="true"
            :formatter="opuNameFormatter"
          />
          <el-table-column
            label="供应商"
            align="center"
            key="vendor"
            prop="vendor"
            v-if="columns[5].visible"
            width="200"
          />
          <el-table-column
            label="生产商"
            align="center"
            key="producer"
            prop="producer"
            v-if="columns[6].visible"
            width="80"
          />
          <el-table-column
            label="状态"
            align="center"
            key="status"
            v-if="columns[7].visible"
            width="100"
          >
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                active-value="0"
                inactive-value="1"
                @change="handleStatusChange(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column
            label="创建时间"
            align="center"
            prop="createTime"
            v-if="columns[8].visible"
            width="160"
          >
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
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
import {opuTreeSelect} from '@/api/parking/park';
import { listDevice } from "@/api/parking/device";

export default {
  props: {
    dialogVisible: {
      type: Boolean,
      required: true,
    },
    title: {
      type: String,
      required: false,
    },
    opuId: {
      type: Number,
      required: true,
    },
  },
  name: "DeviceDialog",
  dicts: ["sys_normal_disable", "pms_device_functions"],

  data() {
    return {
      loading: false,
      total: 0,
      // 显示搜索条件
      showSearch: true,
      //创建title副本
      localTitle: this.title,

      deviceList: [],

      form: {},
      //组织架构树
      opuOptions:[],
      //选种的ID,
      ids: [],

      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceName: undefined,
        deviceModel: undefined,
        vendor: undefined,
        opuId: undefined,
        status: undefined,
      },

      // 列信息
      columns: [
        { key: 0, label: `设备编号`, visible: true },
        { key: 1, label: `设备名称`, visible: true },
        { key: 2, label: `经营单位`, visible: true },
        { key: 3, label: `设备型号`, visible: true },
        { key: 4, label: `设备功能`, visible: true },
        { key: 5, label: `供应商`, visible: true },
        { key: 6, label: `生产商`, visible: true },
        { key: 7, label: `状态`, visible: true },
        { key: 8, label: `创建时间`, visible: true },
      ],
    };
  },
  methods: {
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.deviceId);
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;

      this.getList();
    },
    /** 查询经营单位设备列表 */
    getList() {
      this.loading = true;
      this.queryParams.opuId = this.opuId;

      listDevice(this.queryParams).then((response) => {
        this.deviceList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 表单重置
    reset() {
      this.form = {
        status: "0",
      };

      this.resetForm("form");
    },
    resetQuery() {
      this.queryParams.deviceName = undefined;
      this.queryParams.deviceModel = undefined;
      this.queryParams.functions = [];
      this.queryParams.status = undefined;
    },
    submitForm() {
      if (this.ids && this.ids.length > 0) {
        const filteredRecords = this.deviceList.filter(record => this.ids.includes(record.deviceId));
        this.$emit("submit", filteredRecords);
      } else {
        alert("最少选择一条记录！");
      }
    },
    cancel() {
      this.closeForm("cancel");
    },
    closeForm(type) {
      this.reset();
      this.$emit("close", type);
    },
    //---格式化代码开始
    formatFunctions(row, column) {
      return this.selectDictLabels(
        this.dict.type.pms_device_functions,
        row.functions,
        ","
      );
    },
    opuNameFormatter(row, column) {
      if (this.opuOptions && this.opuOptions.length > 0) {
        return this.findTreeLabelById(this.opuOptions, row.opuId);
      }
    },
    /** 查询部门下拉树结构 */
    getOpuTree() {
      opuTreeSelect().then((response) => {
        this.opuOptions = response.data;
      });
    },
    
  },
  created() {
    this.getOpuTree();
    this.getList();
    
  },
  watch: {
    opuId: {
      handler(newVal, oldVal) {
        console.log("Device prop changed:", newVal);
        if (Number.isNaN(newVal) || newVal == undefined) {
          return;
        }
       
        this.getList();
        
      },
      deep: true, // 如果需要深度监听，特别是对象
      mmediate: true // 立即执行
    },
  },
};
</script>

<style scoped>
.dialog-footer {
  padding-top: 40px;
  display: flex;
  justify-content: flex-end;
}
</style>
