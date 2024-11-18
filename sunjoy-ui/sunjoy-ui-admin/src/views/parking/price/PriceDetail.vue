<template>
  <div class="app-container">
    <el-row :gutter="20">
      <div style="margin-bottom: 10px">
        <span>收费标准</span>
        <el-divider direction="vertical"></el-divider>
        <span>特殊时段</span>
        <el-divider direction="vertical"></el-divider>
      </div>
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
        :data="priceDetailList"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column
          label="编号"
          align="center"
          key="id"
          prop="id"
          v-if="columns[0].visible"
          width="60"
        />

        <el-table-column
          label="时段"
          align="center"
          key="timeRange"
          prop="timeRange"
          v-if="columns[1].visible"
          :show-overflow-tooltip="true"
          :formatter="timeRangFormatter"
        />

        <el-table-column
          label="单价"
          align="center"
          key="price"
          prop="price"
          width="140"
          v-if="columns[3].visible"
          :show-overflow-tooltip="true"
         :formatter="priceFormatter"
        />

        <el-table-column
          label="说明"
          align="center"
          key="remark"
          v-if="columns[5].visible"
          width="160"
        />

        <el-table-column
          label="操作"
          align="center"
          width="100"
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
          </template>
        </el-table-column>
      </el-table>
    </el-row>

    <!-- 添加或修改计费时段对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-col :span="12">
              <el-form-item label="开始" prop="timeStart">
                <el-time-picker
                  v-model="form.timeStart"
                  format="HH:mm"
                  placeholder="任意时间点"
                >
                </el-time-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="结束" prop="timeEnd">
                <el-time-picker
                  arrow-control
                  v-model="form.timeEnd"
                  format="HH:mm"
                  placeholder="任意时间点"
                >
                </el-time-picker>
              </el-form-item>
            </el-col>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="单价" prop="price">
                    <el-input
                      v-model="form.price"
                      placeholder="请输入单价"
                      maxlength="11"
                      style="width: 80px"
                    /> 元 / 
                    <el-input
                      v-model="form.priceQuantity"
                      placeholder="请输入计费量"
                      maxlength="11"
                      style="width: 60px"
                    /><el-select
                      v-model="form.priceUnit"
                      placeholder="请选择量化单位"
                      style="width: 80px"
                    >
                      <el-option
                        v-for="dict in dict.type.pms_price_unit"
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
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancelForm">取 消</el-button>
      </div>
    </el-dialog>
    
  </div>
</template>
<script>
import {
  addDetail,
  updateDetail,
  deleteDetail,
  getDetailList,
  
} from "@/api/parking/price";
import {formatDateTime} from '@/utils/formatters'

export default {
  name: "PriceDetail",
  dicts: [
    "sys_yes_no",
    "pms_price_unit",
  ],
  
  props: {
    opuId: {
      type: Number,
      required: false,
      default: Math.NaN,
    },
    priceId: {
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
      priceDetailList: [],
      //弹出框是否显示
      open: false,
      //收费标准明细表单显示
      title: undefined,


      //选中的通道行
      selectedLane: [],
      //通道表单
      form: {},
      //收费标准明细列表，多选
      detailIds: [],
      //通道设备关系ID列表，多选
      ralationIds: [],

      // 列信息
      columns: [
        { key: 0, label: `编号`, visible: true },
        { key: 1, label: `时段开始`, visible: true },
        { key: 2, label: `时段结束`, visible: true },
        { key: 3, label: `单价`, visible: true },
        { key: 4, label: `收费放行`, visible: true },
        { key: 5, label: `状态`, visible: true },
      ],
      // 表单校验
      rules: {
        timeStart: [
          { required: true, message: "时段不能为空", trigger: "blur" },
        ],
        timeEnd: [{ required: true, message: "时段不能为空", trigger: "blur" }],

        price: [
          { required: true, message: "单价不能为空", trigger: "blur" },
          {
            pattern: /^\d+(\.\d+)?$/, // 正则表达式：正小数
            message: "请输入正确的单价",
            trigger: "blur",
          },
        ],
        priceQuantity: [
          { required: true, message: "计价量不能为空", trigger: "blur" },
          {
            pattern: /^\d+?$/, // 正则表达式：正数
            message: "请输入正确的计价数量",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    handleSelectionChange(selection) {
      this.detailIds = selection.map((item) => item.id);

      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    
    handleAddLane() {
      this.reset();
      this.open = true;
      this.title = "添加收费标准明细";
    },
    handleDeleteLanes() {
      const selectLaneIds = this.detailIds;
      //闭包传对象
      const that = this;
      selectLaneIds &&
        this.$modal
          .confirm("是否确认删除编号为" + selectLaneIds + "的收费标准明细？")
          .then(function () {
            if (that.priceId) {
              deleteDetail(that.priceId, selectLaneIds).then((resp) => {});
            }
            const freshList = that.priceDetailList.filter(
              (item) => !selectLaneIds.includes(item.laneId)
            );

            that.priceDetailList = freshList;
            that.onListChange();
          });
    },
    handleUpdate(row) {
      this.form = row;
      this.open = true;
      this.title = "修改收费标准明细";
    },
    handleDeleteLane(row) {
      //闭包传对象
      const that = this;
      const selectLaneIds = row.laneId || this.detailIds;
      this.$modal
        .confirm("是否确认删除当前行的收费标准明细？")
        .then(function () {
          //如果已经有车场ID，直接调用后台接口删除

          if (that.priceId) {
            const laneIdList = [];
            laneIdList.push(selectLaneIds);
            deleteDetail(that.priceId, laneIdList).then((resp) => {});
          }
          that.priceDetailList = that.priceDetailList.filter(
            (priceDetail) => priceDetail.laneName !== row.laneName
          );
          that.onListChange();
        });
    },

    onListChange() {
      this.$emit("change", this.priceDetailList);
    },

    // 表单重置
    reset() {
      this.form = {
        priceId: NaN,
        priceUnit: 'HOUR',
        priceQuantity: 1,
        status: "0",
      };
      this.priceDetailList = [];

      (this.selectedLane = []),
        //通道ID列表，多选
        (this.detailIds = []),
        
        this.resetForm("form");
    },

    // 取消按钮
    cancelForm() {
      this.open = false;
      
      this.reset();
    },

    //收费标准明细表单提交按钮
    submitForm() {
      let that = this;
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (that.form.id) {
            //更新
            const priceDetail = Object.assign({}, this.form);
            priceDetail.timeStart = formatDateTime(this.form.timeStart,'HH:mm');
            priceDetail.timeEnd = formatDateTime(this.form.timeEnd,'HH:mm');
            //先更新后台
            updateDetail(priceDetail).then((resp) => {
              that.priceDetailList.forEach((item) => {
                if (item.id == that.form.id) {
                  item.timeStart = that.form.timeStart
                  item.timeEnd = that.timeEnd;
                  item.priceUnit = that.form.priceUnit;
                  item.priceQuantity = that.form.priceQuantity;
                  item.remark = that.form.remark;
                }
              });
            });
          } else {
            //新增
            const priceDetail = Object.assign({}, this.form);

            //如果已经有车场ID可以绑定，就直接提效到后台
            if (this.priceId) {
              //提交到后台再返回
          
              priceDetail.priceId = this.priceId;
              priceDetail.timeStart = formatDateTime(this.form.timeStart,'HH:mm');
              priceDetail.timeEnd = formatDateTime(this.form.timeEnd,'HH:mm');
              addDetail(priceDetail).then((resp) => {
                priceDetail.id = resp.data;
                this.priceDetailList.push(this.form);
              });
            } else {
              this.priceDetailList.push(priceDetail);
            }
          }

          this.onListChange();
          this.open = false;
          this.title = undefined;
        }
      });
    },

   
    
    //---格式化代码开始
    timeRangFormatter(row){
        if(row.timeStart.length==5){
            return row.timeStart+" - "+row.timeEnd;
        }
        return formatDateTime(row.timeStart,'HH:mm')+" - "+formatDateTime(row.timeEnd,'HH:mm');
    },
    priceFormatter(row){
        return row.price+" 元 / "+row.priceQuantity+" "+this.selectDictLabel(this.dict.type.pms_price_unit, row.priceUnit);
    }
    
  },
  created(){
   
  
  },
  watch: {
    priceId: {
      handler(newVal, oldVal) {
        
        
        if (Number.isNaN(newVal) || newVal == undefined) {
          this.laneIdList = [];

          return;
        }
        //从后台取车场通道列表
        getDetailList(newVal).then((resp) => {
          this.priceDetailList = resp.data;
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
