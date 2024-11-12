<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAddLane"
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
            @click="handleDeleteLanes"
            >删除</el-button
          >
        </el-col>
      </el-row>
      <el-table
        v-loading="loading"
        :data="laneList"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column
          label="通道编号"
          align="center"
          key="laneId"
          prop="laneId"
          v-if="columns[0].visible"
          width="100"
        />
        <el-table-column
          label="通道名称"
          align="left"
          key="laneName"
          prop="laneName"
          v-if="columns[1].visible"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="进出方向"
          align="center"
          key="direction"
          prop="direction"
          v-if="columns[2].visible"
          :show-overflow-tooltip="true"
          :formatter="directionFormatter"
        />
        <el-table-column
          label="连通外围"
          align="center"
          key="linkOuter"
          prop="linkOuter"
          v-if="columns[3].visible"
          :show-overflow-tooltip="true"
          :formatter="linkOuterFormatter"
        />
        <el-table-column
          label="收费放行"
          align="center"
          key="rap"
          prop="rap"
          v-if="columns[4].visible"
          :show-overflow-tooltip="true"
          :formatter="linkOuterFormatter"
        />
        <el-table-column
          label="状态"
          align="center"
          key="status"
          v-if="columns[5].visible"
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
          label="操作"
          align="center"
          width="200"
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
              >删除</el-button
            >

            <el-button
              size="mini"
              type="text"
              icon="el-icon-video-camera"
              @click="onBindDevice(scope.row)"
              >绑定设备</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <el-divider content-position="left" class="divider"
      >通道设备列表</el-divider
    >
    <el-row>
      <el-table
        v-loading="loading"
        :data="deviceList"
        @selection-change="handleDeviceSelectionChange"
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
          align="left"
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
          v-if="columns[4].visible"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="通道名称"
          align="center"
          key="laneName"
          prop="laneName"
        />
        <el-table-column
          label="进出方向"
          align="center"
          key="direction"
          prop="direction"
          v-if="columns[2].visible"
          :show-overflow-tooltip="true"
          :formatter="directionFormatter"
        />
        <el-table-column
          label="设备功能"
          align="center"
          key="functions"
          prop="functions"
          v-if="columns[3].visible"
          :show-overflow-tooltip="true"
          :formatter="formatFunctions"
          width="200"
        />

        <el-table-column
          label="操作"
          align="center"
          width="200"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope" v-if="scope.row.LaneId !== 1">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="unbindDevice(scope.row)"
              >解绑</el-button
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
            <el-form-item label="通道名称" prop="laneName">
              <el-input
                v-show:="false"
                v-model="form.laneId"
                style="display: none"
              />
              <el-input
                v-model="form.laneName"
                placeholder="请输入用户昵称"
                maxlength="30"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="进出方向" prop="direction">
              <el-select
                v-model="form.direction"
                placeholder="请选择车辆出入场方向"
              >
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
            <el-form-item label="是否外围" prop="linkOuter">
              <el-select
                v-model="form.linkOuter"
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
            <el-form-item label="收费放行" prop="rap">
              <el-select v-model="form.rap" placeholder="请选择是否收费才放行">
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
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitLane">确 定</el-button>
        <el-button @click="cancelLane">取 消</el-button>
      </div>
    </el-dialog>
    <DeviceDialog
      :dialogVisible="deviceOpen"
      :title="deviceTitle"
      :opuId="opuId"
      @close="closeDeviceDialog"
      @submit="bindDevice"
    />
  </div>
</template>
<script>
import {
  addLane,
  updateLane,
  delLane,
  getParkLaneList,
  getLaneDeviceList,
  bindDevice,
  unbindDevice,
} from "@/api/parking/lane";
import DeviceDialog from "./../device/DeviceDialog";
export default {
  name: "ParkLane",
  dicts: [
    "sys_normal_disable",
    "pms_direction",
    "sys_yes_no",
    "pms_device_functions",
  ],
  components: { DeviceDialog },
  props: {
    opuId: {
      type: Number,
      required: false,
      default: Math.NaN,
    },
    parkId: {
      type: Number,
      required: false,
      default: Math.NaN,
    },
  },
  data() {
    return {
      // 遮罩层
      loading: false,
      // 非单个禁用
      single: false,
      // 非多个禁用
      multiple: false,
      // 车场表格数据
      laneList: [],
      //弹出框是否显示
      open: false,
      //通道表单显示
      title: undefined,
      //本地设备列表
      deviceList: [],

      //设备选择框显示
      deviceTitle: undefined,
      deviceOpen: false,
      //选中的通道行
      selectedLane: [],
      //通道表单
      form: {},
      //通道ID列表，多选
      laneIds: [],
      //通道设备关系ID列表，多选
      ralationIds: [],
      // 列信息
      columns: [
        { key: 0, label: `通道编号`, visible: true },
        { key: 1, label: `通道名称`, visible: true },
        { key: 2, label: `进出方向`, visible: true },
        { key: 3, label: `是否外围`, visible: true },
        { key: 4, label: `收费放行`, visible: true },
        { key: 5, label: `状态`, visible: true },
      ],
      // 表单校验
      rules: {
        laneName: [
          { required: true, message: "通道名称不能为空", trigger: "blur" },
          {
            min: 2,
            max: 20,
            message: "通道名称长度必须介于 2 和 20 之间",
            trigger: "blur",
          },
        ],
        direction: [
          { required: true, message: "进出方向不能为空", trigger: "blur" },
        ],
        linkOuter: [
          { required: true, message: "是否外围不能为空", trigger: "blur" },
        ],
        rap: [{ required: true, message: "收费放行不能为空", trigger: "blur" }],
      },
    };
  },
  methods: {
    handleSelectionChange(selection) {
      this.laneIds = selection.map((item) => item.laneId);

      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    handleDeviceSelectionChange(selection) {
      this.ralationIds = selection.map((item) => item.id);
    },
    handleAddLane() {
      this.reset();
      this.open = true;
      this.title = "添加通道";
    },
    handleDeleteLanes() {
      const selectLaneIds = this.laneIds;
      //闭包传对象
      const that = this;
      selectLaneIds &&
        this.$modal
          .confirm("是否确认删除编号为" + selectLaneIds + "的通道？")
          .then(function () {
            if (that.parkId) {
              delLane(that.parkId, selectLaneIds).then((resp) => {});
            }
            const freshList = that.laneList.filter(
              (item) => !selectLaneIds.includes(item.laneId)
            );

            that.laneList = freshList;
            that.onLaneListChange();
          });
    },
    handleUpdate(row) {
      this.form = row;
      this.open = true;
      this.title = "修改通道";
    },
    handleDeleteLane(row) {
      //闭包传对象
      const that = this;
      const selectLaneIds = row.laneId || this.laneIds;
      this.$modal.confirm("是否确认删除当前行的通道？").then(function () {
        //如果已经有车场ID，直接调用后台接口删除

        if (that.parkId) {
          const laneIdList = [];
          laneIdList.push(selectLaneIds);
          delLane(that.parkId, laneIdList).then((resp) => {});
        }
        that.laneList = that.laneList.filter(
          (lane) => lane.laneName !== row.laneName
        );
        that.onLaneListChange();
      });
    },

    onLaneListChange() {
      this.$emit("change", this.laneList);
    },

    // 表单重置
    reset() {
    
      this.form = {
        parkId: NaN,
        status: "0",
      };
      this.laneList = [];
      this.deviceList = [];
      this.selectedLane = [],
        //通道ID列表，多选
      this.laneIds = [],
        //通道设备关系ID列表，多选
      this.ralationIds = [],
      this.resetForm("form");
    },

    // 取消按钮
    cancelLane() {
      this.open = false;
      this.deviceOpen = false;
      this.reset();
    },

    //通道表单提交按钮
    submitLane() {
      let that = this;
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (that.form.laneId) {
            //更新
            //先更新后台
            updateLane(that.form).then((resp) => {
              that.laneList.forEach((item) => {
                if (item.LaneId == that.form.laneId) {
                  item.laneName = that.form.laneName;
                  item.direction = that.form.direction;
                  item.linkOuter = that.form.linkOuter;
                  item.rap = that.form.rap;
                  item.status = that.form.status;
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
                lane.laneId = resp.data;
                this.laneList.push(lane);
              });
            } else {
              this.laneList.push(lane);
            }
          }

          this.onLaneListChange();
          this.open = false;
          this.title = undefined;
        }
      });
    },

    onBindDevice(row) {
      this.selectedLane = row;

      this.deviceOpen = true;
      this.deviceTitle = "车场设备选择";
    },

    closeDeviceDialog(type) {
      this.deviceOpen = false;
      this.deviceTitle = undefined;
    },
    //绑定通道设备
    bindDevice(devices) {
      //如果表单属于更新状态，即有车场id，即先提交到后台
      let that = this;
      if (this.parkId) {
        devices.forEach((item) => {
          Object.assign(item, {
            parkId: that.parkId,
            laneId: that.selectedLane.laneId,
          });
        });
        bindDevice(devices).then((resp) => {
          //this.selectedLane= [];
        });
      }
      const lane = that.selectedLane;

      devices.forEach((item) => {
        item.laneId = lane.laneid;
        item.laneName = lane.laneName;
        item.direction = that.selectedLane.direction;
      
        if (!that.deviceList || !Array.isArray(that.deviceList)) {
          that.deviceList = [];
        }
        that.deviceList.push(item);
      });

      this.deviceOpen = false;
    },
    //解绑设备
    unbindDevice(row) {
      let that = this;
      this.$modal
        .confirm(
          "是否确认解绑通道" + row.laneName + "的设备" + row.deviceName + "?"
        )
        .then(function () {
          debugger;
          const ids = row.id || that.ralationIds;
          unbindDevice(ids).then((resp) => {
            let delIds = [];
            if (!Array.isArray(ids)) {
              delIds.push(ids);
            } else {
              delIds = ids;
            }
            debugger;
            let updatedDeviceList = that.deviceList.filter(
              (ralation) => !delIds.includes(ralation.id)
            );
            that.deviceList = updatedDeviceList;
          });
        });
    },
    //---格式化代码开始
    formatFunctions(row, column) {
      return this.selectDictLabels(
        this.dict.type.pms_device_functions,
        row.functions,
        ","
      );
    },
    linkOuterFormatter(row, column) {
      return this.selectDictLabel(this.dict.type.sys_yes_no, row.linkOuter);
    },
    directionFormatter(row, column) {
      return this.selectDictLabel(this.dict.type.pms_direction, row.direction);
    },
  },

  watch: {
    parkId: {
      handler(newVal, oldVal) {
        debugger
        if (Number.isNaN(newVal) || newVal == undefined) {
          this.laneIdList=[];
          this.deviceList=[];
          return;
        }
        //从后台取车场通道列表
        getParkLaneList(newVal).then((resp) => {
          this.laneList = resp.data;
        });
        //从后台取通道设备列表
        getLaneDeviceList(newVal).then((resp) => {
          this.deviceList = resp.data;
        });
      },
      deep: true,
      immediate: true, // 立即执行,这个配置必需要，否则要第二次才能触发监控
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
</style>
