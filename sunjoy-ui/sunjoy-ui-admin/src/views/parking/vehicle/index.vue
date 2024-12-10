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
          <el-form-item label="车牌号码" prop="licensePlate">
            <el-input
              v-model="queryParams.licensePlate"
              placeholder="请输入车牌号码"
              clearable
              style="width: 200px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="车主姓名" prop="ownerName">
            <el-input
              v-model="queryParams.ownerName"
              placeholder="请输入车主姓名"
              clearable
              style="width: 200px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="手机号码" prop="ownerPhone">
            <el-input
              v-model="queryParams.ownerPhone"
              placeholder="请输入手机号码"
              clearable
              style="width: 200px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="车场" prop="parkId">
            <el-cascader
              v-model="queryParams.parkId"
              :options="parkOptions"
              :props="parkProps"
              clearable
            ></el-cascader>
          </el-form-item>
          <el-form-item label="服务套餐" prop="serviceId">
            <el-select
              v-model="queryParams.serviceId"
              placeholder="服务套餐"
              clearable
              style="width: 100px"
            >
              <el-option
                v-for="dict in dict.type.sys_entity_status"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="车场状态"
              clearable
              style="width: 100px"
            >
              <el-option
                v-for="dict in dict.type.sys_entity_status"
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
              v-hasPermi="['parking:park:add']"
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
              v-hasPermi="['parking:park:edit']"
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
              v-hasPermi="['parking:park:remove']"
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
              v-hasPermi="['parking:park:import']"
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
              v-hasPermi="['parking:park:export']"
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
          :data="vehicleList"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column
            label="编号"
            align="center"
            key="vehicleId"
            prop="vehicleId"
            v-if="columns[0].visible"
            width="100"
          />
          <el-table-column
            label="车牌号码"
            align="left"
            width="140"
            key="licensePlate"
            prop="licensePlate"
            v-if="columns[1].visible"
            :show-overflow-tooltip="true"
          />

          <el-table-column
            label="经营单位"
            align="left"
            key="opuName"
            prop="opuName"
            v-if="columns[3].visible"
            :show-overflow-tooltip="true"
            width="140"
            :formatter="opuNameFormatter"
          />
          <el-table-column
            label="车主姓名"
            align="center"
            key="ownerName"
            prop="ownerName"
            v-if="columns[4].visible"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="手机号码"
            align="center"
            key="ownerPhone"
            prop="ownerPhone"
            v-if="columns[5].visible"
            width="120"
          />
          <el-table-column
            label="车辆品牌"
            align="left"
            key="brand"
            prop="brand"
            v-if="columns[6].visible"
            width="100"
          />


          <el-table-column
            label="车辆型号"
            align="center"
            key="model"
            prop="model"
            v-if="columns[7].visible"
            width="100"
          />
          <el-table-column
            label="车辆类型"
            align="center"
            key="parkType"
            prop="parkType"
            v-if="columns[7].visible"
            width="80"
            :formatter="vehicleTypeFormatter"
          />
          <el-table-column
            label="车位号码"
            align="left"
            key="lots"
            prop="lots"
            v-if="columns[8].visible"
            width="220"
            :formatter="lotsFormatter"
          />
          <el-table-column
            label="服务套餐"
            align="left"
            key="serviceName"
            prop="serviceName"
            v-if="columns[9].visible"
            width="220"
          />
          <el-table-column
            label="状态"
            align="center"
            key="status"
            v-if="columns[10].visible"
            width="100"
            :formatter="statusFormatter"
          />

          <el-table-column
            label="创建时间"
            align="center"
            prop="createTime"
            v-if="columns[11].visible"
            width="160"
          >
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="360"
            fixed="right"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['parking:park:edit']"
                :disabled="scope.row.status != 0"
                >修改</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['parking:park:remove']"
                :disabled="scope.row.status != 0"
                >删除</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-video-play"
                @click="handleEnable(scope.row)"
                v-hasPermi="['parking:park:enable']"
                :disabled="scope.row.status != 0"
                >审批</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-video-pause"
                @click="handleDisable(scope.row)"
                v-hasPermi="['parking:park:enable']"
                :disabled="
                  scope.row.status == 0 ||
                  scope.row.status == 2 ||
                  scope.row.status == 9
                "
                >注消</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-chat-dot-square"
                @click="toView(scope.row)"
                v-hasPermi="['parking:park:list']"
                >查看</el-button
              >
              <el-divider direction="vertical"></el-divider>
              <router-link
                :to="
                  '/parking/vehicle-service/index/' +
                  scope.row.vehicleId +
                  '-' +
                  scope.row.opuId
                "
                class="link-type"
              >
                <span><i class="el-icon-set-up"></i>服务套餐</span>
              </router-link>
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

    <!-- 添加或修改车辆对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="openVehicleForm"
      width="1024px"
      append-to-body
    >
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form
            ref="form"
            :model="form"
            :rules="rules"
            label-width="80px"
            class="vehicleForm"
          >
            <el-row>
              <el-col :span="12">
                <el-form-item label="经营单位" prop="opuId">
                  <treeselect
                    v-model="form.opuId"
                    :options="opuOptions"
                    :show-count="true"
                    placeholder="请选择经营单位"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12"> </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="车牌号码" prop="licensePlate">
                  <el-select
                    v-model="province"
                    placeholder="请选择"
                    style="width: 80px"
                  >
                    <el-option
                      v-for="p in provinces"
                      :key="p.value"
                      :label="p.label"
                      :value="p.value"
                    ></el-option>
                  </el-select>
                  <el-input
                    style="width: 100px"
                    v-model="form.licensePlate"
                    placeholder="请输入车牌号码"
                    maxlength="30"
                    @blur="validateLicensePlate"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="车辆类型" prop="vehicleType">
                  <el-select v-model="form.vehicleType" placeholder="请选择">
                    <el-option
                      v-for="dict in dict.type.pms_vehicle_type"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="车辆品牌" prop="brand">
                  <el-input
                    v-model="form.brand"
                    placeholder="请输入车辆品牌"
                    maxlength="20"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="车辆型号" prop="mod">
                  <el-input v-model="form.model" placeholder="请输入车辆型号" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="车主姓名" prop="ownerName">
                  <el-input
                    v-model="form.ownerName"
                    placeholder="请输入车场车主姓名"
                    maxlength="50"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="手机号码" prop="ownerPhone">
                  <el-input
                    v-model="form.ownerPhone"
                    placeholder="请输入手机号码"
                    maxlength="11"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="24">
                <el-form-item label="居住地址" prop="address">
                  <el-input
                    v-model="form.ownerAddr"
                    placeholder="请输入详细地址"
                    maxlength="11"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="24">
                <el-form-item label="注册日期" prop="registDate">
                  <el-date-picker
                    v-model="form.registDate"
                    type="date"
                    placeholder="选择日期"
                  >
                  </el-date-picker>
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
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-tag>收费标准</el-tag>
          <VehicleService
            :opuId="opuIdPassToService"
            :vehicleId="form.vehicleId"
            @submit="onServiceChange"
          />
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <div class="dialog-footer">
            <el-button type="primary" @click="submitVehicle">确 定</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import { opuTreeSelect, getParkTree } from "@/api/parking/park";
import {
  listVehicle,
  getVehicle,
  addVehicle,
  updateVehicle,
  delVehicle,
  changeVehicleStatus
} from "@/api/parking/vehicle";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import VehicleService from "./service";

export default {
  name: "Park",
  dicts: ["sys_normal_disable", "pms_vehicle_type", "sys_entity_status"],
  components: { Treeselect, VehicleService },
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
      // 车场表格数据
      vehicleList: null,

      ids: [],

      selectOpuNode: undefined,
      /**
       * 传递给车辆收费标准组件的opuId
       */
      opuIdPassToService: undefined,

      // 表单参数
      form: {},

      // 弹出框标题
      title: "",
      // 弹出框是否显示
      open: false,
      // 车辆维护form打开
      openVehicleForm: false,

      defaultProps: {
        children: "children",
        label: "label",
      },

      // 经营单位树选项
      opuOptions: [],
      // 经营单位名称，用于过滤
      opuName: undefined,

      // 车场下拉框选项
      parkOptions: [],

      // 日期范围
      dateRange: [],
      // 通道列表
      serviceList: [],

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        licensePlate: undefined,
        ownerPhone: undefined,
        ownerName: undefined,
        opuId: undefined,
        status: undefined,
      },

      parkProps: {
        value: "parkId",
        label: "parkName",
        checkStrictly: true,
      },

      // 列信息
      columns: [
        { key: 0, label: `编号`, visible: true },
        { key: 1, label: `车牌号码`, visible: true },
        { key: 2, label: `车场名称`, visible: true },
        { key: 3, label: `经营单位`, visible: true },
        { key: 4, label: `车主姓名`, visible: true },
        { key: 5, label: `手机号码`, visible: true },
        { key: 6, label: `车辆品牌`, visible: true },
        { key: 7, label: `车辆类型`, visible: true },
        { key: 8, label: `车位号码`, visible: true },
        { key: 9, label: `服务套餐`, visible: true },
        { key: 10, label: `状态`, visible: true },
        { key: 11, label: `创建时间`, visible: true },
      ],
      // 表单校验
      rules: {
        licensePlate: [
          { required: true, message: "车牌号码不能为空", trigger: "blur" },
          {
            min: 6,
            message: "车牌号码长度至少为６个字符，新能源车为７个",
            trigger: "blur",
          },
        ],
        opuId: [
          { required: true, message: "经营单位不能为空", trigger: "blur" },
        ],
        vehicleType: [
          { required: true, message: "车辆类型不能为空", trigger: "blur" },
        ],

        ownerName: [
          { required: true, message: "车主姓名不能为空", trigger: "blur" },
        ],

        ownerPhone: [
          { required: true, message: "车主姓名不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur",
          },
        ],
        registDate: [
          { required: true, message: "登记日期不能为空", trigger: "blur" },
        ],
      },
      province: undefined,
      provinces: [
        { value: "京", label: "京" },
        { value: "津", label: "津" },
        { value: "沪", label: "沪" },
        { value: "粤", label: "粤" },
        { value: "冀", label: "冀" },
        { value: "豫", label: "豫" },
        { value: "鲁", label: "鲁" },
        { value: "苏", label: "苏" },
        { value: "浙", label: "浙" },
        { value: "皖", label: "皖" },
        { value: "湘", label: "湘" },
        { value: "鄂", label: "鄂" },
        { value: "桂", label: "桂" },
        { value: "甘", label: "甘" },
        { value: "晋", label: "晋" },
        { value: "蒙", label: "蒙" },
        { value: "陕", label: "陕" },
        { value: "吉", label: "吉" },
        { value: "闽", label: "闽" },
        { value: "贵", label: "贵" },
        { value: "青", label: "青" },
        { value: "藏", label: "藏" },
        { value: "川", label: "川" },
        { value: "宁", label: "宁" },
        { value: "琼", label: "琼" },
        { value: "使领", label: "使领" },
      ],
    };
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val);
    },
    "form.opuId": {
      handler(newVal, oldVal) {
        if (newVal) {
          this.opuIdPassToService = newVal;
        } else {
          this.opuIdPassToService = undefined;
        }

        this.parkOptions = [];
        if (newVal) {
          this.getParkTreeList(newVal);
        }
      },
      deep: true,
      immediate: true, // 立即执行,这个配置必需要，否则要第二次才能触发监控
    },
    openVehicleForm: {
      handler(newVal, oldVal) {
        if (newVal && this.form.licensePlate) {
          //正确显示车牌号码
          this.provinces.forEach((item) => {
            if (this.form.licensePlate.includes(item.value)) {
              this.province = item.value;
              this.form.licensePlate = this.form.licensePlate.substring(
                1,
                this.form.licensePlate.length
              );
              return;
            }
          });
        }
        if (newVal) {
          if (this.form.opuId) {
            this.getParkTreeList(this.form.opuId);
          }
        }
      },
      immediate: true, // 立即执行,这个配置必需要，否则要第二次才能触发监控
    },
  },
  created() {
    this.getOpuTree();
    this.getList();
  },
  methods: {
    getParkTreeList(opuId) {
      getParkTree(opuId).then((resp) => {
        this.parkOptions = resp.data;
      });
    },
    //接收通道的变化信息
    onServiceChange(list) {
      this.serviceList = list;
    },

    /** 查询辆列表 */
    getList() {
      this.loading = true;
      listVehicle(this.queryParams).then((response) => {
        this.vehicleList = response.rows;
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
      this.form.opuId = data.id;
      this.queryParams.ancestors = data.ancestors;
      this.selectOpuNode = data;
      this.handleQuery();
      // 初始化车场树
      // this.getParkTreeList(data.id);
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
      this.ids = selection.map((item) => item.vehicleId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },

    // 表单重置
    reset() {
      this.form = {
        opuId: this.selectOpuNode ? this.selectOpuNode.id : undefined,
        vehicleId: undefined,
        status: "0",
      };
      this.resetForm("form");
      this.serviceList = [];

      this.serviceOpen = false;
      this.openVehicleForm = false;
      this.open = false;
    },
    /** 响应新增按钮操作 */
    handleAdd() {
      this.reset();
      this.openVehicleForm = true;
      this.title = "车辆登记";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const vehicleIds = row.vehicleId || this.ids;

      if (vehicleIds) {
        let vehicleId = NaN;
        if (Array.isArray(vehicleIds)) {
          vehicleId = vehicleIds[0];
        } else {
          vehicleId = vehicleIds;
        }
        let that = this;
        getVehicle(vehicleId).then((response) => {
          that.form = response.data;
          that.title = "修改车辆档案";
          that.openVehicleForm = true;
        });
      }
    },
    // 启用车辆，启用后，不能物理删除，只能逻辑删除
    handleEnable(row) {
      this.$modal
        .confirm('是否确认编号为:' + row.vehicleId + '，车牌号码为:' + row.licensePlate + '的车辆信息无误？')
        .then(function () {
          return changeVehicleStatus(row.vehicleId, "1");
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("审批成功");
        })
        .catch(() => {});
    },

    handleDisable(row) {
      this.$modal
        .confirm('是否确认停用编号为"' + row.vehicleId + '"的车场？')
        .then(function () {
          return changeVehicleStatus(row.vehicleId, "9");
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("停用成功");
        })
        .catch(() => {});
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.openVehicleForm = false;

      this.reset();
    },

    /** 提交按钮 */
    submitVehicle: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          const submitObj = this.form;
          submitObj.licensePlate = this.province + this.form.licensePlate;
          //加上车辆服务才提交
          if (this.serviceList.length > 0) {
            submitObj.vehicleServiceList = this.serviceList;
          }
          if (this.form.vehicleId != undefined && !isNaN(this.form.vehicleId)) {
            //修改
            updateVehicle(submitObj).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.openVehicleForm = false;
              this.getList();
            });
          } else {
            addVehicle(submitObj).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.openVehicleForm = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const vehicleIds = row.vehicleId || this.ids;
      this.$modal
        .confirm('是否确认删除用户编号为"' + vehicleIds + '"的数据项？')
        .then(function () {
          return delVehicle(vehicleIds);
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
        "parking/park/export",
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
        "parking/park/importTemplate",
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
    /**
     * 车牌校验
     */
    validateLicensePlate() {
      // 正则表达式验证车牌格式
      const reg = new RegExp(
        `^(${this.province})[A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳]$`
      );

      if (!reg.test(this.province + this.form.licensePlate)) {
        this.$set(this.rules.licensePlate[1], "message", "车牌号码不正确!");

        this.$refs.form.validateField("licensePlate");
      }
    },
    // ---------- formatters

    vehicleTypeFormatter(row) {
      return this.selectDictLabel(
        this.dict.type.pms_vehicle_type,
        row.vehicleType
      );
    },
    statusFormatter(row) {
      return this.selectDictLabel(this.dict.type.sys_entity_status, row.status);
    },
    opuNameFormatter(row, column) {
      if (this.opuOptions && this.opuOptions.length > 0) {
        return this.findTreeLabelById(this.opuOptions, row.opuId);
      }
    },
    lotsFormatter(row, column) {
      if (row.serviceId) {
        return row.parkName + (row.lotNos ? '/' + row.lotNos : '')
      }
    }
  },
};
</script>

<style scoped>
.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style>
