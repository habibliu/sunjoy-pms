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
      <!--收费标准查询表单-->
      <el-col :span="20" :xs="24">
        <el-form
          :model="queryParams"
          ref="queryForm"
          size="small"
          :inline="true"
          v-show="showSearch"
          label-width="68px"
        >
          <el-form-item label="名称" prop="priceName">
            <el-input
              v-model="queryParams.priceName"
              placeholder="请输入收费标准名称"
              clearable
              style="width: 200px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="是否免费" prop="free">
            <el-select
              v-model="queryParams.free"
              placeholder="请选择"
              clearable
              style="width: 200px"
              @keyup.enter.native="handleQuery"
              ><el-option
                v-for="dict in dict.type.sys_yes_no"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="统一价格" prop="uniformPrice">
            <el-select
              v-model="queryParams.uniformPrice"
              placeholder="请选择"
              clearable
              style="width: 200px"
              @keyup.enter.native="handleQuery"
              ><el-option
                v-for="dict in dict.type.sys_yes_no"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="收费标准状态"
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
              :disabled="canMultiDel"
              @click="handleDelete"
              v-hasPermi="['parking:park:remove']"
              >删除</el-button
            >
          </el-col>
        </el-row>

        <el-table
          v-loading="loading"
          :data="priceList"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column
            label="编号"
            align="center"
            key="priceId"
            prop="priceId"
            v-if="columns[0].visible"
            width="100"
          />
          <el-table-column
            label="收费标准名称"
            align="left"
            key="priceName"
            prop="priceName"
            v-if="columns[1].visible"
            :show-overflow-tooltip="true"
            width="300"
          />
          <el-table-column
            label="经营单位"
            align="left"
            key="opuId"
            prop="opuId"
            v-if="columns[2].visible"
            :show-overflow-tooltip="true"
            :formatter="opuFormatter"
            width="200"
          />
          <el-table-column
            label="是否免费"
            align="center"
            key="free"
            prop="free"
            v-if="columns[3].visible"
            :show-overflow-tooltip="true"
            :formatter="freeFormatter"
            width="120"
          />

          <el-table-column
            label="统一价格"
            align="center"
            key="uniformPrice"
            prop="uniformPrice"
            v-if="columns[4].visible"
            width="120"
            :formatter="uniformPriceFormatter"
          />
          <el-table-column
            label="免费时长(分钟)"
            align="center"
            key="freeDuration"
            prop="freeDuration"
            v-if="columns[4].visible"
            width="160"
          />
          <el-table-column
            label="单价"
            align="center"
            key="price"
            prop="price"
            v-if="columns[5].visible"
            width="140"
            :formatter="priceFormatter"
          />
          <el-table-column
            label="最高收费"
            align="center"
            key="maxFee"
            prop="maxFee"
            v-if="columns[6].visible"
            width="140"
            :formatter="maxFeeFormatter"
          />

          <el-table-column
            label="状态"
            align="center"
            key="status"
            v-if="columns[8].visible"
            width="100"
            :formatter="statusFormatter"
          />

          <el-table-column
            label="创建时间"
            align="center"
            prop="createTime"
            v-if="columns[9].visible"
            width="160"
          >
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="270"
            class-name="small-padding fixed-width"
            fixed="right"
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
                >启用</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-video-pause"
                @click="handleDisable(scope.row)"
                v-hasPermi="['parking:park:enable']"
                :disabled="scope.row.status == 0 || scope.row.status == 9"
                >停用</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-chat-dot-square"
                @click="toView(scope.row)"
                v-hasPermi="['parking:price:list']"
                >详情</el-button
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

    <!-- 添加或修改收费标准对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      :width="dialogWidth"
      append-to-body
    >
      <el-row :gutter="20">
        <el-col :span="dialogFormSpan">
          <el-form ref="form" :model="form" :rules="rules" label-width="80px">
            <el-row class="parkRow">
              <el-col :span="24">
                <el-form-item label="经营单位" prop="opuId">
                  <Treeselect
                    v-model="form.opuId"
                    :options="opuOptions"
                    :show-count="true"
                    placeholder="请选择经营单位"
                    @select="onChangeOpu"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="parkRow">
              <el-col :span="24">
                <el-form-item label="名称" prop="priceName">
                  <el-input
                    v-model="form.priceName"
                    placeholder="请输入收费标准名称"
                    maxlength="30"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="parkRow">
              <el-col :span="24">
                <el-form-item label="是否免费" prop="free">
                  <el-radio-group v-model="form.free" @change="onFreeChange">
                    <el-radio
                      v-for="dict in dict.type.sys_yes_no"
                      :key="dict.value"
                      :label="dict.value"
                      >{{ dict.label }}</el-radio
                    >
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <div v-show="form.free == 'N'">
              <el-row class="parkRow">
                <el-col :span="24">
                  <el-form-item label="统一价格" prop="uniformPrice">
                    <el-radio-group
                      v-model="form.uniformPrice"
                      @change="onUniformPriceChange"
                    >
                      <el-radio
                        v-for="dict in dict.type.sys_yes_no"
                        :key="dict.value"
                        :label="dict.value"
                        >{{ dict.label }}</el-radio
                      >
                    </el-radio-group>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row class="parkRow">
                <el-col :span="24">
                  <el-form-item label="免费时长" prop="region">
                    <el-input-number
                      v-model="form.freeDuration"
                      placeholder="请输入免费时长"
                      maxlength="11"
                      :min="0"
                      style="width: 200px"
                    />分钟
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row class="parkRow">
                <el-col :span="24">
                  <el-form-item label="单价" prop="price">
                    <el-input
                      v-model="form.price"
                      placeholder="请输入单价"
                      maxlength="11"
                      style="width: 80px"
                    />
                    元/
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

              <el-row class="parkRow">
                <el-col :span="24">
                  <el-form-item label="最高收费">
                    <el-input
                      v-model="form.maxFee"
                      placeholder="请输入最高费用"
                      maxlength="11"
                      style="width: 80px"
                    />
                    元/
                    <el-input
                      v-model="form.maxQuantity"
                      placeholder="请输入计费量"
                      maxlength="11"
                      style="width: 60px"
                    />
                    <el-select
                      v-model="form.maxUnit"
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
            </div>
            <el-row class="parkRow">
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
        <el-col :span="dialogDetailSpan">
          <PriceDetail
            @change="onDetailListChange"
            :opuId="form.opuId"
            :priceId="form.priceId"
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
    </el-dialog>

    <!-- 收费标准查看页面，不可编辑 -->
    <el-dialog
      :title="viewTitle"
      :visible.sync="viewOpen"
      :width="dialogWidth"
      append-to-body
    >
      <el-row :gutter="20">
        <el-col :span="dialogFormSpan">
          <el-form ref="form" :model="form" :rules="rules" label-width="80px">
            <el-row class="parkRow">
              <el-col :span="24">
                <el-form-item label="经营单位" prop="opuId">
                  <Treeselect
                    v-model="form.opuId"
                    :options="opuOptions"
                    :show-count="true"
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="parkRow">
              <el-col :span="24">
                <el-form-item label="名称" prop="priceName">
                  <el-input
                    v-model="form.priceName"
                    :disabled="true"
                    maxlength="30"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row class="parkRow">
              <el-col :span="24">
                <el-form-item label="是否免费" prop="free">
                  <el-radio-group v-model="form.free" :disabled="true">
                    <el-radio
                      v-for="dict in dict.type.sys_yes_no"
                      :key="dict.value"
                      :label="dict.value"
                      >{{ dict.label }}</el-radio
                    >
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <div v-show="form.free == 'N'">
              <el-row class="parkRow">
                <el-col :span="24">
                  <el-form-item label="统一价格" prop="uniformPrice">
                    <el-radio-group
                      v-model="form.uniformPrice"
                      :disabled="true"
                    >
                      <el-radio
                        v-for="dict in dict.type.sys_yes_no"
                        :key="dict.value"
                        :label="dict.value"
                        >{{ dict.label }}</el-radio
                      >
                    </el-radio-group>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row class="parkRow">
                <el-col :span="24">
                  <el-form-item label="免费时长" prop="region">
                    <el-input-number
                      v-model="form.freeDuration"
                      :disabled="true"
                      maxlength="11"
                      :min="0"
                      style="width: 200px"
                    />分钟
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row class="parkRow">
                <el-col :span="24">
                  <el-form-item label="单价" prop="price">
                    <el-input
                      v-model="form.price"
                      :disabled="true"
                      maxlength="11"
                      style="width: 80px"
                    />
                    元/
                    <el-input
                      v-model="form.priceQuantity"
                      :disabled="true"
                      maxlength="11"
                      style="width: 60px"
                    /><el-select
                      v-model="form.priceUnit"
                      :disabled="true"
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

              <el-row class="parkRow">
                <el-col :span="24">
                  <el-form-item label="最高收费">
                    <el-input
                      v-model="form.maxFee"
                      :disabled="true"
                      maxlength="11"
                      style="width: 80px"
                    />
                    元/
                    <el-input
                      v-model="form.maxQuantity"
                      :disabled="true"
                      maxlength="11"
                      style="width: 60px"
                    />
                    <el-select
                      v-model="form.maxUnit"
                      :disabled="true"
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
            </div>
            <el-row class="parkRow">
              <el-col :span="24">
                <el-form-item label="备注">
                  <el-input
                    v-model="form.remark"
                    type="textarea"
                    :disabled="true"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-col>
        <el-col :span="dialogDetailSpan">
          <PriceDetail
            @change="onDetailListChange"
            :opuId="form.opuId"
            :priceId="form.priceId"
            :disabled=true
          />
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <div class="dialog-footer">
            <el-button @click="closeViewForm">关 闭</el-button>
          </div>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import { opuTreeSelect } from "@/api/parking/park";
import {
  listPrice,
  getPrice,
  addPrice,
  updatePrice,
  delPrice,
  changePriceStatus,
} from "@/api/parking/price";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import RegionSelect from "@/components/RegionSelect";
import PriceDetail from "./PriceDetail";
import { findLabelById } from "@/utils/formatters";

export default {
  name: "ParkPrice",
  dicts: [
    "sys_normal_disable",
    "sys_yes_no",
    "pms_price_unit",
    "sys_entity_status",
  ],
  components: { Treeselect, RegionSelect, PriceDetail },
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

      canMultiDel: false,
      // 收费标准表格数据
      priceList: null,

      ids: [],

      // 日期范围
      dateRange: [],

      // 表单参数
      form: {},

      regionId: undefined,
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
      // 经营单位名称，用于过滤
      opuName: undefined,

      // 通道列表
      detailList: [],

      dialogWidth: "1024px",
      dialogFormSpan: 24,
      dialogDetailSpan: 0,

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        priceName: undefined,
        uniformPrice: undefined,
        free: undefined,
        opuId: undefined,
        status: undefined,
      },

      // 列信息
      columns: [
        { key: 0, label: `编号`, visible: true },
        { key: 1, label: `收费标准名称`, visible: true },
        { key: 2, label: `经营单位`, visible: true },
        { key: 3, label: `是否免费`, visible: true },
        { key: 4, label: `统一价格`, visible: true },
        { key: 5, label: `免费时长`, visible: true },
        { key: 6, label: `分类`, visible: true },
        { key: 7, label: `总车位数`, visible: true },
        { key: 8, label: `状态`, visible: true },
        { key: 9, label: `创建时间`, visible: true },
      ],
      // 表单校验
      rules: {
        priceName: [
          { required: true, message: "收费标准名称不能为空", trigger: "blur" },
          {
            min: 2,
            max: 20,
            message: "收费标准名称长度必须介于 2 和 20 之间",
            trigger: "blur",
          },
        ],
        opuId: [
          { required: true, message: "经营单位不能为空", trigger: "blur" },
        ],

        price: [
          {
            pattern: /^\d+(\.\d+)?$/, // 正则表达式：正小数
            message: "请输入正确的单价",
            trigger: "blur",
          },
        ],
      },
      viewTitle: "",
      viewOpen: false,
    };
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val);
    },
    "form.uniformPrice"(newVal, oldVal) {
      this.onUniformPriceChange(newVal);
    },
  },
  created() {
    this.reset();
    this.getOpuTree();
    this.getList();
  },
  methods: {
    //接收收费标准明细的变化信息
    onDetailListChange(list) {
      this.detailList = list;
    },

    //组opuName赋值
    onChangeOpu(selectedOpu) {
      this.form.opuName = selectedOpu.label;
    },

    /** 查询收费标准列表 */
    getList() {
      this.loading = true;
      listPrice(this.queryParams).then((response) => {
        this.priceList = response.rows;
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
      //请求
      this.queryParams.opuId = data.id;
      this.queryParams.params = { ancestors: data.ancestors };

      this.form.opuId = data.id;
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

    onFreeChange(value) {
      if ("Y" == value) {
        this.rules.price = this.rules.price.filter((rule) => !rule.required);
        //收起明细
        this.onUniformPriceChange("Y");
      } else {
        // 检查是否已经有 required 规则，避免重复添加
        const hasRequiredRule = this.rules.price.some((rule) => rule.required);
        if (!hasRequiredRule) {
          this.rules.price.unshift({
            required: true,
            message: "请输入价格",
            trigger: "blur",
          });
        }

        this.onUniformPriceChange(this.form.uniformPrice);
      }
    },

    onUniformPriceChange(value) {
      if (value == "N") {
        this.dialogWidth = "1440px";
        this.dialogFormSpan = 10;
        this.dialogDetailSpan = 14;
      } else {
        this.dialogWidth = "800px";
        this.dialogFormSpan = 24;
        this.dialogDetailSpan = 0;
      }
    },

    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.priceId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
      this.canMultiDel = selection.some((item) => item.status !== "0");
    },

    // 表单重置
    reset() {
      this.form = {
        priceId: NaN,
        free: "N",
        uniformPrice: "Y",
        priceUnit: "HOUR",
        priceQuantity: 1,
        maxUnit: "DAY",
        maxQuantity: 1,
        status: "0",
      };
      this.resetForm("form");
      this.detailList = [];

      this.onUniformPriceChange("Y");
    },
    /** 响应新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加收费标准";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const priceIds = row.priceId || this.ids;

      if (priceIds) {
        let priceId = NaN;
        if (Array.isArray(priceIds)) {
          priceId = priceIds[0];
        } else {
          priceId = priceIds;
        }

        getPrice(priceId).then((response) => {
          this.form = response.data;
          this.open = true;
          this.title = "修改收费标准";
        });
      }
    },
    // 启用收费标准，启用后，不能物理删除，只能逻辑删除
    handleEnable(row) {
      this.$modal
        .confirm('是否确认启用编号为"' + row.priceId + '"的收费标准？')
        .then(function () {
          return changePriceStatus(row.priceId, "1");
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("启用成功");
        })
        .catch(() => {});
    },

    handleDisable(row) {},

    toView(row) {
      const priceIds = row.priceId || this.ids;
      if (priceIds) {
        let priceId = NaN;
        if (Array.isArray(priceIds)) {
          priceId = priceIds[0];
        } else {
          priceId = priceIds;
        }

        getPrice(priceId).then((response) => {
          this.form = response.data;
          this.viewOpen = true;
          this.viewTitle = "收费标准详情";
        });
      }
    },
    /**
     * 关闭收费标准详情窗口
     */
    closeViewForm() {
      this.viewOpen = false;
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },

    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          //回填form中的region，提交到后台
          this.form.region = this.regionId;
          if (this.form.priceId != undefined && !isNaN(this.form.priceId)) {
            //修改
            updatePrice(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            if (this.detailList.length > 0) {
              this.form.detailList = this.detailList;
            }

            addPrice(this.form).then((response) => {
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
      const priceIds = row.priceId || this.ids;
      this.$modal
        .confirm('是否确认删除用户编号为"' + priceIds + '"的数据项？')
        .then(function () {
          return delPrice(priceIds);
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
      this.upload.title = "收费标准导入";
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
    // ---------- formatters

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
    opuFormatter(row) {
      return findLabelById(this.opuOptions, row.opuId);
    },
  },
};
</script>

<style scoped>
.parkForm .parkRow {
  margin-bottom: 10px; /* 调整行间距 */
}
.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style>
