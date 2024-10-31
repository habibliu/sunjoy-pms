<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--部门数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="opuName"
            placeholder="请输入经营单位名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="opuOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            node-key="id"
            default-expand-all
            highlight-current
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <!--车场查询表单-->
      <el-col :span="20" :xs="24">
        <el-form
          :model="queryParams"
          ref="queryForm"
          size="small"
          :inline="true"
          v-show="showSearch"
          label-width="68px"
        >
          <el-form-item label="设备名称" prop="deviceName">
            <el-input
              v-model="queryParams.deviceName"
              placeholder="请输入设备名称"
              clearable
              style="width: 160px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="设备型号" prop="deviceModel">
            <el-input
              v-model="queryParams.deviceModel"
              placeholder="请输入设备型号"
              clearable
              style="width: 160px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="供应商" prop="vendor">
            <el-input
              v-model="queryParams.vendor"
              placeholder="请输入供应商名称"
              clearable
              style="width: 160px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="设备功能" prop="functions">
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
          <el-form-item label="设备状态" prop="status">
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

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['parking:device:add']"
              >新增</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              plain
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
              v-hasPermi="['parking:device:edit']"
              >修改</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['parking:device:remove']"
              >删除</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="info"
              plain
              icon="el-icon-upload2"
              size="mini"
              @click="handleImport"
              v-hasPermi="['parking:device:import']"
              >导入</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['parking:device:export']"
              >导出</el-button
            >
          </el-col>
          <right-toolbar
            :showSearch.sync="showSearch"
            @queryTable="getList"
            :columns="columns"
          ></right-toolbar>
        </el-row>

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
            label="经营单位"
            align="center"
            key="opuName"
            prop="opuName"
            v-if="columns[2].visible"
            :show-overflow-tooltip="true"
            :formatter="opuNameFormatter"
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
          <el-table-column
            label="操作"
            align="center"
            width="160"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope" v-if="scope.row.deviceId !== 1">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['parking:device:edit']"
                >修改</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['parking:device:remove']"
                >删除</el-button
              >
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
      </el-col>
    </el-row>
    <!--设备表单弹出框-->
    <DeviceForm
      :deviceId="selectDeviceId"
      :dialogVisible="open"
      :title="title"
      @close="closeForm"
    />
  </div>
</template>

<script>
import { listDevice, getDevice, delDevice } from "@/api/parking/device";
import { opuTreeSelect } from "@/api/parking/park";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import DeviceForm from "./DeviceForm.vue";


export default {
  name: "ParkDevice",
  dicts: ["sys_normal_disable", "pms_device_functions"],
  components: { Treeselect, DeviceForm },
  data() {
    return {
      // 遮罩层
      loading: false,
      // 总条数
      total: 0,
      // 显示搜索条件
      showSearch: true,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      ids: [],
      // 车场表格数据
      deviceList: null,

      selectDeviceId: NaN,

      // 日期范围
      dateRange: [],

      // 表单参数
      form: {},
      //弹出框标题
      title: undefined,
      //弹出框是否显示
      open: false,

      defaultProps: {
        children: "children",
        label: "label",
      },
      // 经营单位树选项
      opuOptions: undefined,
      //经营单位名称，用于过滤
      opuName: undefined,

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
      },
      //格式化代码开始
    };
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val);
    },
  },
  created() {
    this.getOpuTree();
    this.getList();
  },
  methods: {
    //组opuName赋值
    onChangeOpu(selectedOpu) {
      this.form.opuName = selectedOpu.label;
    },

    /** 查询车场列表 */
    getList() {
      this.loading = true;
      listDevice(this.queryParams).then((response) => {
        console.log(response);
        this.deviceList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询部门下拉树结构 */
    getOpuTree() {
      opuTreeSelect().then((response) => {
        this.opuOptions = response.data;
      });
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.opuId = data.id;
      this.handleQuery();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.queryParams.opuId = undefined;
      this.$refs.tree.setCurrentKey(null);
      this.handleQuery();
    },

    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.deviceId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },

    // 表单重置
    reset() {
      this.form = {
        status: "0",
      };
      (this.selectDeviceId = NaN), this.resetForm("form");
    },
    /** 响应新增按钮操作 */
    handleAdd() {
      //this.currentComponent = DeviceForm; // 动态加载组件
      this.selectDeviceId = NaN;
      this.open = true;
      this.title = "添加设备资料";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.selectDeviceId = row.deviceId || this.ids[0];
      this.open = true;
      this.title = "修改设备资料";
    },

    // 取消按钮
    closeForm(type) {
      this.open = false;
      if (type == "submit") {
        this.getList();
      }
      this.reset();
    },

    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          let submitObj = this.form;

          if (this.form.deviceId != undefined) {
            updatePark(submitObj).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPark(submitObj).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const deviceIds = row.deviceId || this.ids;
      this.$modal
        .confirm('是否确认删除设备编号为"' + deviceIds + '"的数据项？')
        .then(function () {
          return delDevice(deviceIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "parking/device/export",
        {
          ...this.queryParams,
        },
        `user_${new Date().getTime()}.xlsx`
      );
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "车场导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download(
        "parking/device/importTemplate",
        {},
        `user_template_${new Date().getTime()}.xlsx`
      );
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(
        "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
          response.msg +
          "</div>",
        "导入结果",
        { dangerouslyUseHTMLString: true }
      );
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
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
   
  },
};
</script>
