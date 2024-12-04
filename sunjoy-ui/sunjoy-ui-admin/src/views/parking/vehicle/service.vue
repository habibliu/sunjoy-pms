<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-row :gutter="10" class="mb8">
        <el-col :span="9">
          <span>车场名称</span>
          <el-cascader
            v-model="parkIds"
            :options="parkOptions"
            :props="parkProps"
            :show-count="true"
            placeholder="请选择车场名称"
          />
        </el-col>

        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handlerAddService"
            >增加收费标准</el-button
          >
        </el-col>
      </el-row>
      <!-- 车辆收费标准 -->
      <el-table
        v-loading="loading"
        :data="serviceList"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column
          label="编号"
          align="center"
          key="serviceId"
          prop="serviceId"
          width="60"
        />
        <el-table-column
          label="收费标准"
          align="left"
          key="serviceName"
          prop="serviceName"
          :show-overflow-tooltip="true"
          width="300"
        />
        <el-table-column
          label="车场ID"
          align="left"
          key="parkId"
          prop="parkId"
          v-if="false"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="车场名称"
          align="left"
          key="parkName"
          prop="parkName"
          :show-overflow-tooltip="true"
        />

        <el-table-column label="车位编号" align="center" width="200">
          <template #default="scope">
            <div v-if="scope.row.status === '0'">
              <el-input
                v-model="scope.row.lotNos"
                placeholder="请输入车位编号"
                maxlength="20"
              />
            </div>
            <div v-else>
              {{ scope.row.lotNos }}
            </div>
          </template>
        </el-table-column>

        <el-table-column label="开始日期" width="120" align="center">
          <template #default="scope">
            <div v-if="scope.row.status === '0'">
              <el-date-picker
                v-model="scope.row.startDate"
                type="date"
                placeholder="选择日期"
              />
            </div>
            <div v-else>
              {{ formatDate(scope.row.startDate) }}
            </div>
          </template>
        </el-table-column>
        <el-table-column label="结束日期" width="120" align="center">
          <template #default="scope">
            <div v-if="scope.row.status === '0'">
              <el-date-picker
                v-model="scope.row.endDate"
                type="date"
                placeholder="选择日期"
              />
            </div>
            <div v-else>
              {{ formatDate(scope.row.endDate) }}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          label="状态"
          align="center"
          key="status"
          width="80"
          :formatter="statusFormatter"
        />

        <el-table-column
          label="操作"
          align="center"
          width="300"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="toUpdateVehicleService(scope.row)"
              :disabled="scope.row.status != 0"
              >修改</el-button
            >
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelVehiclePrice(scope.row)"
              :disabled="scope.row.status != 0"
              >删除</el-button
            >
            <el-button
              size="mini"
              type="text"
              icon="el-icon-video-play"
              @click="handleEnable(scope.row)"
              :disabled="scope.row.status != 0"
              v-if="vehicleId"
              >启用</el-button
            >
            <el-button
              size="mini"
              type="text"

              @click="toRecharge(scope.row)"
              :disabled="scope.row.status != 1"
              v-if="vehicleId"
              >
              <IconRecharge :color="scope.row.status != 1 ? '#2c2c2c' : '#1296db'"/>
              充值</el-button
            >
            <el-button
              size="mini"
              type="text"
              icon="el-icon-video-pause"
              @click="handleDisable(scope.row)"
              v-if="vehicleId"
              :disabled="scope.row.status != 1"
              >停用</el-button
            >
            <el-button
              size="mini"
              type="text"
              icon="el-icon-chat-dot-square"
              @click="toView(scope.row)"
              v-if="vehicleId"
              >详情</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <el-row>
      <div class="footer-center" v-show="buttonVisible">
        <el-button
          type="primary"
          :disabled="!activeSubmit"
          @click="onSubmitVehicleService"
          >提交</el-button
        >
      </div>
    </el-row>

    <el-dialog
      :title="title"
      :visible.sync="priceOpen"
      width="1024px"
      append-to-body
    >
      <el-row>
        <el-col :span="24">
          <el-table
            v-loading="loading"
            :data="parkServiceList"
            @selection-change="handlePriceSelectionChange"
          >
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column
              label="编号"
              align="center"
              key="priceId"
              prop="priceId"
              width="100"
            />
            <el-table-column
              label="收费标准名称"
              align="left"
              key="priceName"
              prop="priceName"
            />

            <el-table-column
              label="是否免费"
              align="center"
              key="free"
              prop="free"
              :formatter="freeFormatter"
            />
            <el-table-column
              label="统一价格"
              align="center"
              key="uniformPrice"
              prop="uniformPrice"
              width="120"
              :formatter="uniformPriceFormatter"
            />
            <el-table-column
              label="免费时长(分钟)"
              align="center"
              key="freeDuration"
              prop="freeDuration"
              width="160"
            />
            <el-table-column
              label="单价"
              align="center"
              key="price"
              prop="price"
              width="140"
              :formatter="priceFormatter"
            />
            <el-table-column
              label="最高收费"
              align="center"
              key="maxFee"
              prop="maxFee"
              width="140"
              :formatter="maxFeeFormatter"
            />

            <el-table-column
              label="状态"
              align="center"
              key="status"
              width="100"
              :formatter="statusFormatter"
            />
          </el-table>

          <pagination
            v-show="total > 0"
            :total="total"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="pageAction"
          />
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <div class="dialog-footer">
            <el-button type="primary" @click="onSelectParkService"
              >确 定</el-button
            >
            <el-button @click="cancelPrice">取 消</el-button>
          </div>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>
<script>
import {
  addService,
  updateService,
  deleteVehicleService,
  getVehicleService,
  changeVehicleServiceStatus,
} from "@/api/parking/vehicle";
import { formatDate } from "@/utils/formatters";
import { getParkServices } from "@/api/parking/service";
import { getParkTree } from "@/api/parking/park";
import { addOneMonth } from "@/utils/ruoyi";

import IconRecharge from '@/components/icons/IconRecharge.vue';

export default {
  name: "VehicleService",
  dicts: [
    "sys_normal_disable",
    "pms_price_unit",
    "sys_yes_no",
    "sys_entity_status",
  ],
  components: {IconRecharge},
  props: {
    opuId: {
      type: Number,
      required: false,
      default: NaN,
    },
    vehicleId: {
      type: Number,
      required: false,
      default: NaN,
    },
  },
  data() {
    return {
      rechargePath: require('@/assets/icons/svg/recharge.svg'), // 确保路径正确
      // 总条数
      total: 0,
      // 遮罩层
      loading: false,
      // 非单个禁用
      single: false,
      // 非多个禁用
      multiple: false,
      // 车场表格数据
      serviceList: [],
      // 弹出框是否显示
      open: false,
      // 通道表单显示
      title: undefined,
      // 按钮是否可见
      buttonVisible: true,

      // 车场服务对象表单信息
      form: {},

      // 车场下拉框选项
      parkOptions: [],

      selectParkId: NaN,

      // 车场服务ID，多选
      serviceIds: [],
      // 车场收费标准列表
      parkServiceList: [],

      selectedParkServiceList: [],

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        status: "1",
      },
      /**
       * 车场下拉框绑定的数据
       */
      parkIds: [],

      parkProps: {
        value: "parkId",
        label: "parkName",
        checkStrictly: true,
      },
      priceOpen: false,
      // 是否激活提交按钮
      activeSubmit: false,
    };
  },
  watch: {
    opuId: {
      handler(newVal, oldVal) {
        this.parkOptions = [];
        if (newVal) {
          this.getParkTreeList(newVal);
        }
      },
      deep: true,
      immediate: true, // 立即执行,这个配置必需要，否则要第二次才能触发监控
    },
    parkIds: {
      handler(newVal, oldVal) {
        if (newVal && Array.isArray(newVal)) {
          this.selectParkId = newVal[0];
        } else {
          this.selectParkId = undefined;
        }
      },
      deep: true,
      immediate: true, // 立即执行,这个配置必需要，否则要第二次才能触发监控
    },
    vehicleId: {
      handler(newVal, oldVal) {
        if (newVal) {
          this.getList(newVal);
        }
      },
      deep: true,
      immediate: true, // 立即执行,这个配置必需要，否则要第二次才能触发监控
    },
  },

  created() {
    try {
      const combine = this.$route.params && this.$route.params.vehicleId;
      const splitArray = combine.split("-");

      this.vehicleId = Number(splitArray[0]);
      this.opuId = Number(splitArray[1]);
    } catch (error) {
      this.buttonVisible = false;
    }

    // this.getList(this.vehicleId);
  },
  methods: {
    getParkTreeList(opuId) {
      getParkTree(opuId).then((resp) => {
        this.parkOptions = resp.data;
      });
    },

    /**
     * 查询车辆服务列表
     * @param vehicleId
     */
    getList(vehicleId) {
      this.loading = true;

      getVehicleService(vehicleId).then((resp) => {
        this.serviceList = resp.data;
      });
      this.loading = false;
    },

    pageAction() {
      if (this.vehicleId) {
        this.getList(this.vehicleId);
      }
    },

    /** 查询车场收费标准列表 */
    getPriceList() {
      this.loading = true;
      getParkServices(this.selectParkId, "1").then((response) => {
        this.parkServiceList = response.data;

        this.loading = false;
      });
    },

    // 转换 priceName 的示例函数
    transformPriceName(priceName) {
      // 在这里进行 priceName 的转换逻辑
      // 例如，可以简单地返回 priceName，或者进行其他处理
      return `${priceName}`; // 示例处理
    },

    handleSelectionChange(selection) {
      this.serviceIds = selection.map((item) => item.serviceId);

      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /**
     * 收费标准弹出框选中事件
     */
    handlePriceSelectionChange(selection) {
      this.selectedParkServiceList = selection;
    },
    /**
     * 弹出收费标准选择框
     */
    handlerAddService() {
      if (isNaN(this.selectParkId)) {
        alert("请先选择车场!");
        return;
      }
      this.getPriceList();
      this.priceOpen = true;
      this.title = "添加收费标准";
    },
    /**
     * 更新车辆服务
     * @param row
     */
    toUpdateVehicleService(row) {
      updateService(row)
        .then((resp) => {
          this.$modal.msgSuccess("更新成功");
        })
        .catch(() => {
          this.$modal.msgSuccess("更新失败");
        });
    },
    /**
     * 选择车场服务
     */
    onSelectParkService() {
      // 将selectedPriceList与已经保存的serviceList比较，不存在的才提效到后台
      // 使用 filter 和 some 来过滤 selectedParkServiceList

      const filteredPriceList = this.selectedParkServiceList.filter(
        (selectedPrice) =>
          !this.serviceList.some(
            (service) => service.serviceId === selectedPrice.serviceId
          )
      );

      // 转换函数
      const transformedArray = filteredPriceList.map((item) => ({
        opuId: this.opuId,
        vehicleId: this.vehicleId,
        serviceId: item.serviceId,
        serviceName: this.transformPriceName(item.priceName), // 转换 priceName 为 serviceName
        parkId: this.selectParkId,
        parkName: this.findParkName(this.parkOptions, this.selectParkId),
        status: "0",
        startDate: new Date(),
        endDate: addOneMonth(new Date()),
      }));

      this.serviceList = this.serviceList.concat(transformedArray);
      this.$emit("submit", this.serviceList);
      this.priceOpen = false;
      if (this.hasNewRecord()) {
        this.activeSubmit = true;
      }
    },
    onSubmitVehicleService() {
      const emptyOrMissingIdRecords = this.serviceList.filter(
        (service) =>
          service.id === null ||
          service.id === "" ||
          service.id === undefined ||
          !("id" in service) // 检查 id 属性是否存在
      );
      if (emptyOrMissingIdRecords.length > 0) {
        addService(emptyOrMissingIdRecords).then((resp) => {
          // 重新加载
          this.getList(this.vehicleId);
          this.activeSubmit = false;
        });
      }
    },
    /**
     * 充值
     */
    toRecharge(row) {},

    findParkName(parkOptions, targetId) {
      for (const park of parkOptions) {
        if (park.parkId === targetId) {
          return park.parkName; // 找到匹配的 parkId，返回 parkName
        }
        // 如果有子节点，递归查找
        if (park.children) {
          const result = this.findParkName(park.children, targetId);
          if (result) {
            return result; // 如果在子节点中找到，返回结果
          }
        }
      }
      return ""; // 如果没有找到，返回空字符串
    },

    cancelPrice() {
      this.priceOpen = false;
    },

    hasNewRecord() {
      const emptyOrMissingIdRecords = this.serviceList.filter(
        (service) =>
          service.id === null ||
          service.id === "" ||
          service.id === undefined ||
          !("id" in service) // 检查 id 属性是否存在
      );

      return emptyOrMissingIdRecords.length !== 0;
    },

    handleDelVehiclePrice(row) {
      // 闭包传对象
      const that = this;
      const selectserviceIds = row.serviceId || this.serviceIds;
      this.$modal
        .confirm("是否确认删除当前行[" + row.serviceName + "]的收费标准？")
        .then(function () {
          // 如果已经有车辆ID，直接调用后台接口删除

          if (row.id) {
            deleteVehicleService(that.vehicleId, selectserviceIds).then(
              (resp) => {}
            );
          }
          that.serviceList = that.serviceList.filter(
            (service) => service.serviceName !== row.serviceName
          );
          if (this.hasNewRecord()) {
            this.activeSubmit = true;
          } else {
            this.activeSubmit = false;
          }
        });
    },
    // 启用收费服务
    handleEnable(row) {
      this.$modal
        .confirm('是否确认启用编号为"' + row.id + '"的车场收费服务？')
        .then(function () {
          return changeVehicleServiceStatus({ id: row.id, status: "1" });
        })
        .then(() => {
          if (this.vehicleId) {
            this.getList(this.vehicleId);
          }
          this.$modal.msgSuccess("启用成功");
        })
        .catch(() => {});
    },
    // 停用收费服务
    handleDisable(row) {
      alert(row.status);
    },

    // 表单重置
    reset() {
      this.form = {};

      this.serviceList = [];

      // 服务ID列表，多选
      this.serviceIds = [];
      // 通道设备关系ID列表，多选
      this.resetForm("form");
    },

    // ---格式化代码开始
    formatDate(date) {
      return formatDate(date);
    },

    priceFormatter(row) {
      if (row.price) {
        return (
          row.price +
          " 元 / " +
          row.priceQuantity +
          " " +
          this.selectDictLabel(this.dict.type.pms_price_unit, row.priceUnit)
        );
      } else {
        return "";
      }
    },

    maxFeeFormatter(row) {
      if (row.maxFee) {
        return (
          row.maxFee +
          " 元 / " +
          row.maxQuantity +
          " " +
          this.selectDictLabel(this.dict.type.pms_price_unit, row.maxUnit)
        );
      } else {
        return "";
      }
    },
    statusFormatter(row) {
      return this.selectDictLabel(this.dict.type.sys_entity_status, row.status);
    },
    freeFormatter(row) {
      return this.selectDictLabel(this.dict.type.sys_yes_no, row.free);
    },
    uniformPriceFormatter(row) {
      return this.selectDictLabel(this.dict.type.sys_yes_no, row.uniformPrice);
    },
  },
};
</script>

<style scoped>
/* 可以在这里添加样式 */
.divider {
  margin: 10;
  padding: 10;
  color: #409eff;
  font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB,
    Microsoft YaHei, SimSun, sans-serif;
  font-weight: 400;
}
.footer-center {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  height: 200px; /* 设置高度以便垂直居中 */
}

.el-icon-recharge{
  background: url('~@/assets/icons/svg/search.svg') no-repeat;
  font-size: 16px;
  background-size: cover;
}
</style>
