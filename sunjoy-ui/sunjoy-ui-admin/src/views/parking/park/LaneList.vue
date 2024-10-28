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
            @click="handleAdd"
            >新增</el-button
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
        />
        <el-table-column
          label="连通外围"
          align="center"
          key="isOuter"
          prop="isOuter"
          v-if="columns[3].visible"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="收费放行"
          align="center"
          key="rap"
          prop="rap"
          v-if="columns[4].visible"
          :show-overflow-tooltip="true"
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
              @click="handleDelete(scope.row)"
              >删除</el-button
            >

            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="bindDevice(scope.row)"
              >绑定设备</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <el-divider content-position="left" class="divider">通道设备列表</el-divider>
    <el-row>

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
          align="left"
          key="deviceName"
          prop="deviceName"
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
        />
        <el-table-column
          label="设备功能"
          align="center"
          key="functions"
          prop="functions"
          v-if="columns[3].visible"
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
        
      </el-table>
    </el-row>

    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="通道名称" prop="laneName">
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
            <el-form-item label="是否外围" prop="isOuter">
              <el-select
                v-model="form.isOuter"
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
        <el-button type="primary" @click="addLane">确 定</el-button>
        <el-button @click="cancelLane">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: "ParkLane",
  dicts: ["sys_normal_disable", "pms_direction", "sys_yes_no"],
  props: {
    //通道列表
    laneList: {
      type: Array,
      required: true,
    },
    //设备列表
    deviceList: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      // 遮罩层
      loading: false,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 车场表格数据
      //laneList: [],
      //弹出框是否显示
      open: false,

      title: undefined,

      form: {},
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
        isOuter: [
          { required: true, message: "是否外围不能为空", trigger: "blur" },
        ],
        rap: [{ required: true, message: "收费放行不能为空", trigger: "blur" }],
      },
    };
  },
  methods: {
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.parkId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加通道";
    },
    handleUpdate(row) {},
    handleDelete(row) {
      //闭包传对象
      const that = this;
      this.$modal.confirm("是否确认删除当前行的通道？").then(function () {
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
        status: "0",
      };
      this.resetForm("form");
    },

    // 取消按钮
    cancelLane() {
      this.open = false;
      this.reset();
    },

    //提交按钮
    addLane() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.open = false;
          this.title = undefined;
          this.laneList.push(Object.assign({}, this.form));
          this.onLaneListChange();
        }
      });
    },

    bindDevice() {},
  },
};
</script>

<style scoped>
/* 可以在这里添加样式 */
.divider {
    margin: 10;
    padding: 10;
    color: #409eff;
    font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, SimSun, sans-serif;
    font-weight: 400;
}
</style>
