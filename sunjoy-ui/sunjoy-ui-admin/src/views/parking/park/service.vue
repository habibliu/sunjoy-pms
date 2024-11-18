<template>
  <div class="app-container">
    <el-row>
      <el-form
        ref="parkForm"
        :model="parkForm"
        :rules="rules"
        label-width="80px"
      >
        <el-row>
          <el-col :span="6">
            <el-form-item label="车场编号">
              <el-input v-model="parkForm.parkId" :disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="车场名称">
              <el-input v-model="parkForm.parkName" :disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="车场类型">
              <el-select v-model="parkForm.parkType">
                <el-option
                  v-for="dict in dict.type.pms_park_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                  :disabled="true"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="总车位数">
              <el-input v-model="parkForm.totalLots" :disabled="true" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-row>
    <el-row :gutter="20">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAddPrice"
            >新增</el-button
          >
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="!multiple && !single"
            @click="handleDeletePrice"
            >删除</el-button
          >
        </el-col>
      </el-row>
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
      
          width="80"
        />
        <el-table-column
          label="收费标准"
          align="left"
          key="priceName"
          prop="priceName"
      
          :show-overflow-tooltip="true"
          width="300"
        />
        <el-table-column label="是否" align="center">
          <el-table-column
            label="免费"
            align="center"
        
            :show-overflow-tooltip="true"
            :formatter="freeFormatter"
            width="80"
          />
          <el-table-column
            label="统一价"
            align="center"
            key="uniformPrice"
            prop="uniformPrice"
           
            :show-overflow-tooltip="true"
            :formatter="uniformPriceFormatter"
            width="80"
          />
        </el-table-column>
        <el-table-column
            label="免费时长(分钟)"
            align="center"
            key="freeDuration"
            prop="freeDuration"
            v-if="columns[4].visible"
            width="120"
          
          />
        <el-table-column
          label="单价"
          align="center"
          key="price"
        
          :show-overflow-tooltip="true"
          :formatter="priceFormatter"
        />
        <el-table-column
          label="最高收费"
          align="center"
          key="maxFee"
         
          width="160"
          :formatter="maxFeeFormatter"
        />
        <el-table-column label="服务过期处理" align="center">
          <el-table-column
            label="允许通行"
            align="center"
            key="expiredAllowed"
        
            width="100"
            :formatter="allowedFormatter"
          />
          <el-table-column
            label="保留身份时限"
            align="center"
            key="expiredDuration"
          
            width="100"
            :formatter="durationFormatter"
          />
        </el-table-column>
        <el-table-column label="欢迎语" align="center">
          <el-table-column
            label="入场"
            align="center"
            key="entryMessage"
            width="120"
            :formatter="entryMessageFormatter"
          />
          <el-table-column
            label="出场"
            align="center"
            key="exitMessage"
            width="120"
            :formatter="exitMessageFormatter"
          />
        </el-table-column>
        <el-table-column
          label="状态"
          align="center"
          key="status"
         
          width="100"
          :formatter="statusFormatter"
        />

        <el-table-column
          label="操作"
          align="center"
          width="200"
          fixed="right"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope" v-if="scope.row.LaneId !== 1">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              >修改</el-button
            >
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDeleteLane(scope.row)"
              :disabled="scope.row.status!=0"
              >删除</el-button
            >
            <el-button
                size="mini"
                type="text"
                icon="el-icon-video-play"
                @click="handleEnable(scope.row)"
                
                :disabled="scope.row.status!=0"
                >启用</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-video-pause"
                @click="handleDisable(scope.row)"
                :disabled="scope.row.status!=1"
                >停用</el-button
              >
          </template>
        </el-table-column>
      </el-table>
    </el-row>

    <!-- 添加或修改通道配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="收费标准" prop="priceName">
              <el-input v-model="form.serviceId" style="display: none" />
              <el-input
                v-model="form.priceName"
                placeholder="请输入用户昵称"
                maxlength="30"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="进出方向" prop="free">
              <el-select v-model="form.free" placeholder="请选择车辆出入场方向">
                <el-option
                  v-for="dict in dict.type.pms_direction"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="是否外围" prop="uniformPrice">
              <el-select
                v-model="form.uniformPrice"
                placeholder="请选择是否与外界连通"
              >
                <el-option
                  v-for="dict in dict.type.sys_yes_no"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="收费放行" prop="price">
              <el-select
                v-model="form.price"
                placeholder="请选择是否收费才放行"
              >
                <el-option
                  v-for="dict in dict.type.sys_yes_no"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="状态">
              <el-radio-group v-model="form.expiredDuration">
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
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitChange">确 定</el-button>
        <el-button @click="cancelChange">取 消</el-button>
      </div>
    </el-dialog>

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
            :data="priceList"
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
            @pagination="getPriceList"
          />
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <div class="dialog-footer">
            <el-button type="primary" @click="submitPrice">确 定</el-button>
            <el-button @click="cancelPrice">取 消</el-button>
          </div>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>
<script>
import { addLane, updateLane, delLane } from "@/api/parking/lane";

import { getParkServices, batchAddParkServices,changeParkServiceStatus } from "@/api/parking/service";
import { listPrice } from "@/api/parking/price";
import { getPark } from "@/api/parking/park";

export default {
  name: "ParkService",
  dicts: [
    "sys_normal_disable",
    "pms_price_unit",
    "sys_yes_no",
    "sys_entity_status",
  ],

  data() {
    return {
      // 总条数
      total: 0,
      // 遮罩层
      loading: false,
      // 非单个禁用
      single: false,
      // 非多个禁用
      multiple: false,
      // 车场收费标准数据
      serviceList: [],
      //弹出框是否显示
      open: false,
      //通道表单显示
      title: undefined,
      /**
       * 车场ID,从route参数中传递过来
       */
      parkId: undefined,

      //车场服务对象表单信息
      form: {},
      //车场信息
      parkForm: {},
      //车场服务ID，多选
      serviceIds: [],
      //收费标准列表
      priceList: [],

      selectedPriceList: [],

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        status: "1",
      },

      priceOpen: false,

      // 列信息
      columns: [
        { key: 0, label: `编号`, visible: true },
        { key: 1, label: `收费标准`, visible: true },
        { key: 2, label: `是否免费`, visible: true },
        { key: 3, label: `是否统一价`, visible: true },
        { key: 4, label: `单价`, visible: true },
        { key: 5, label: `最高收费`, visible: true },
        { key: 6, label: `过期允许通行`, visible: true },
        { key: 7, label: `过期身份保留时间`, visible: true },
        { key: 8, label: `状态`, visible: true },
      ],
      // 表单校验
      rules: {
        priceName: [
          { required: true, message: "通道名称不能为空", trigger: "blur" },
          {
            min: 2,
            max: 20,
            message: "通道名称长度必须介于 2 和 20 之间",
            trigger: "blur",
          },
        ],
        free: [
          { required: true, message: "进出方向不能为空", trigger: "blur" },
        ],
        uniformPrice: [
          { required: true, message: "是否外围不能为空", trigger: "blur" },
        ],
        price: [
          { required: true, message: "收费放行不能为空", trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    getList(parkId) {
      this.loading = true;
      
      getParkServices(parkId).then((resp) => {
        this.serviceList = resp.data;
       
      });
      this.loading = false;
    },
    /**
     * 获取车场信息
     * @param parkId
     */
    getParkInfo(parkId) {
      getPark(parkId).then((resp) => {
        this.parkForm = resp.data;
      });
    },

    /** 查询收费标准列表 */
    getPriceList() {
      this.loading = true;
      listPrice(this.queryParams).then((response) => {
        this.priceList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    handleSelectionChange(selection) {
      this.serviceIds = selection.map((item) => item.serviceId);

      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /**
     * 收费标准弹出框选中事件
     */
    handlePriceSelectionChange(selection) {
      this.selectedPriceList = selection;
    },
    /**
     * 弹出收费标准选择框
     */
    handleAddPrice() {
      this.getPriceList();
      this.priceOpen = true;
      this.title = "添加收费标准";
    },

    submitPrice() {
      //将selectedPriceList与已经保存的serviceList比较，不存在的才提效到后台
      // 使用 filter 和 some 来过滤 selectedPriceList
      
      const filteredPriceList = this.selectedPriceList.filter(
        (selectedPrice) =>
          !this.serviceList.some(
            (service) => service.priceId === selectedPrice.priceId
          )
      );

      //提交到后台
      if (filteredPriceList && Array.isArray(filteredPriceList)) {
        //提交前补充一些信息
        filteredPriceList.forEach((item) => {
          item.parkId = this.parkForm.parkId;
          item.opuId = this.parkForm.opuId;
        });
        batchAddParkServices(filteredPriceList).then((resp) => {
          this.serviceList.push(filteredPriceList);
        });
      }

      this.priceOpen = false;
    },
    cancelPrice() {
      this.priceOpen = false;
    },

    handleDeletePrice() {
      const selectserviceIds = this.serviceIds;
      //闭包传对象
      const that = this;
      selectserviceIds &&
        this.$modal
          .confirm("是否确认删除编号为" + selectserviceIds + "的通道？")
          .then(function () {
            if (that.parkId) {
              delLane(that.parkId, selectserviceIds).then((resp) => {});
            }
            const freshList = that.serviceList.filter(
              (item) => !selectserviceIds.includes(item.serviceId)
            );

            that.serviceList = freshList;
          });
    },
    handleUpdate(row) {
      this.form = row;
      this.open = true;
      this.title = "修改车场服务";
    },
    handleDeleteLane(row) {
      //闭包传对象
      const that = this;
      const selectserviceIds = row.serviceId || this.serviceIds;
      this.$modal.confirm("是否确认删除当前行的通道？").then(function () {
        //如果已经有车场ID，直接调用后台接口删除

        if (that.parkId) {
          const laneIdList = [];
          laneIdList.push(selectserviceIds);
          delLane(that.parkId, laneIdList).then((resp) => {});
        }
        that.serviceList = that.serviceList.filter(
          (lane) => lane.priceName !== row.priceName
        );
      });
    },
    //启用收费服务
    handleEnable(row){
      
        this.$modal
        .confirm('是否确认启用编号为"' + row.serviceId + '"的车场收费服务？')
        .then(function () {
          return changeParkServiceStatus(row.serviceId);
        })
        .then(() => {
          
          this.getList(this.parkId);
          this.$modal.msgSuccess("启用成功");
        })
        .catch(() => {});
        
    },
    //停用收费服务
    handleDisable(row){
      alert(row.status);
    },

    // 表单重置
    reset() {
      this.form = {
        parkId: NaN,
        expiredDuration: "0",
      };
    
      this.serviceList = [];

      //服务ID列表，多选
      (this.serviceIds = []),
        //通道设备关系ID列表，多选

        this.resetForm("form");
    },

    // 取消按钮
    cancelChange() {
      this.open = false;
      this.deviceOpen = false;
    },

    //通道表单提交按钮
    submitChange() {
      let that = this;
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (that.form.serviceId) {
            //更新
            //先更新后台
            updateLane(that.form).then((resp) => {
              that.serviceList.forEach((item) => {
                if (item.LaneId == that.form.serviceId) {
                  item.priceName = that.form.priceName;
                  item.free = that.form.free;
                  item.uniformPrice = that.form.uniformPrice;
                  item.price = that.form.price;
                  item.expiredDuration = that.form.expiredDuration;
                }
              });
            });
          } else {
            //新增
            const lane = Object.assign({}, this.form);

            //如果已经有车场ID可以绑定，就直接提效到后台
            if (this.parkId) {
              //提交到后台再返回
              lane.parkId = this.parkId;
              lane.opuId = this.opuId;
              addLane(lane).then((resp) => {
                lane.serviceId = resp.data;
                this.serviceList.push(lane);
              });
            } else {
              this.serviceList.push(lane);
            }
          }

          this.open = false;
          this.title = undefined;
        }
      });
    },

    //---格式化代码开始

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

    allowedFormatter(row) {
      if (row.expiredAllowed) {
        return this.selectDictLabel(
          this.dict.type.sys_yes_no,
          row.expiredAllowed
        );
      } else {
        return "否";
      }
    },
    durationFormatter(row) {
      if (row.expiredDuration) {
        return row.expiredDuration + " 天";
      } else {
        return "０天";
      }
    },
    entryMessageFormatter(row){
      if (!row.entryMessage) {
        return "系统默认"
      }
    },
    exitMessageFormatter(row){
      if (!row.exitMessage) {
        return "系统默认"
      }
    },
  },
  created() {
    this.parkId = this.$route.params && this.$route.params.parkId;
    this.getParkInfo(this.parkId);
    this.getList(this.parkId);
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
</style>
