<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--行政区数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="regionName"
            placeholder="快速定位，请输入区域名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="regionOptions"
            :props="regionProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            node-key="regionId"
            :default-expand-all="false"
            highlight-current
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <!--租户查询表单-->
      <el-col :span="20" :xs="24">
        <el-form
          :model="queryParams"
          ref="queryForm"
          size="small"
          :inline="true"
          v-show="showSearch"
          label-width="68px"
        >
          <el-form-item label="租户名称" prop="tenantName">
            <el-input
              v-model="queryParams.tenantName"
              placeholder="请输入租户名称"
              clearable
              style="width: 200px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="租户代码" prop="tenantCode">
            <el-input
              v-model="queryParams.tenantCode"
              placeholder="请输入租户代码"
              clearable
              style="width: 200px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="负责人" prop="leader">
            <el-input
              v-model="queryParams.leader"
              placeholder="请输入租户负责人姓名"
              clearable
              style="width: 200px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="手机号码" prop="phone">
            <el-input
              v-model="queryParams.phone"
              placeholder="请输入手机号码"
              clearable
              style="width: 200px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="租户状态"
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
              v-hasPermi="['system:tenant:add']"
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
              v-hasPermi="['system:tenant:update']"
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
              v-hasPermi="['system:tenant:remove']"
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
              v-hasPermi="['system:tenant:import']"
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
              v-hasPermi="['system:tenant:export']"
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
          :data="tenantList"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column
            label="租户编号"
            align="center"
            key="tenantId"
            prop="tenantId"
            v-if="columns[0].visible"
            width="100"
          />
          <el-table-column
            label="租户名称"
            align="center"
            key="tenantName"
            prop="tenantName"
            v-if="columns[1].visible"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="租户代码"
            align="center"
            key="tenantCode"
            prop="tenantCode"
            v-if="columns[2].visible"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="负责人"
            align="center"
            key="leader"
            prop="leader"
            v-if="columns[3].visible"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="手机号码"
            align="center"
            key="phone"
            prop="phone"
            v-if="columns[4].visible"
            width="120"
          />
          <el-table-column
            label="注册日期"
            align="center"
            key="registrationDate"
            prop="registrationDate"
            v-if="columns[6].visible"
            width="200"
            :formatter="formatRegisterDate"
          />
          <el-table-column
            label="位置"
            align="center"
            key="region"
            prop="region"
            v-if="columns[5].visible"
            width="180"
            :formatter="formatRegion"
          />

          <el-table-column
            label="详细地址"
            align="center"
            key="address"
            prop="address"
            v-if="columns[7].visible"
            width="200"
          />
          <el-table-column
            label="状态"
            align="center"
            key="status"
            v-if="columns[8].visible"
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
            v-if="columns[9].visible"
            width="160"
            :formatter="formatCreateTime"
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
            <template slot-scope="scope" v-if="scope.row.tenantId !== 1">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['parking:park:edit']"
                >修改</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['parking:park:remove']"
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

    <!-- 添加或修改租户对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="1024px"
      append-to-body
    >
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form
            ref="form"
            :model="form"
            :rules="rules"
            label-width="100px"
            class="tenantForm"
          >
            <el-row >
              <el-col :span="12">
                <el-form-item label="租户名称" prop="tenantName">
                  <el-input
                    v-model="form.tenantName"
                    placeholder="请输入用户昵称"
                    maxlength="30"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="租户代码" prop="tenantCode">
                  <el-input
                    v-model="form.tenantCode"
                    placeholder="请选择租户代码"
                    maxlength="10"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row >
              <el-col :span="12">
                <el-form-item label="负责人" prop="leader">
                  <el-input
                    v-model="form.leader"
                    placeholder="请输入租户负责人"
                    maxlength="50"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="手机号码" prop="phone">
                  <el-input
                    v-model="form.phone"
                    placeholder="请输入手机号码"
                    maxlength="11"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row >
              <el-col :span="12">
                <el-form-item label="区域" prop="region">
                  <RegionSelect
                    v-model="form.region"
                    :region="form.region"
                    @change="onRegionChange"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="详细地址" prop="address">
                  <el-input
                    v-model="form.address"
                    placeholder="请输入详细地址"
                    maxlength="11"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row >
              <el-col :span="12">
                <el-form-item label="注册日期" prop="address">
                  <el-date-picker
                    v-model="form.registrationDate"
                    type="date"
                    placeholder="选择日期"
                  >
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="支付方式">
                  <el-select
                    v-model="form.paymentMethod"
                    placeholder="请选择支付方式"
                  >
                    <el-option
                      v-for="dict in dict.type.pms_park_type"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row > </el-row>

            <el-row >
              <el-col :span="12">
                <el-form-item label="结算周期">
                  <el-radio-group v-model="form.settlementCycle">
                    <el-radio
                      v-for="dict in dict.type.sys_settlement_cycle"
                      :key="dict.value"
                      :label="dict.value"
                      >{{ dict.label }}</el-radio
                    >
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="结算日">
                  <el-slider
                    v-model="form.settlementDate"
                    show-input
                    show-stops
                    :min="1"
                    :max="31"
                   
                  />
                  
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="微信帐号" prop="wechatAccoiunt">
                  <el-input
                    v-model="form.wechatAccoiunt"
                    placeholder="微信帐号"
                    maxlength="11"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="支付宝帐号" prop="alipayAccount">
                  <el-input
                    v-model="form.alipayAccount"
                    placeholder="支付宝帐号"
                    maxlength="11"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row >
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
          <div class="dialog-footer">
            <el-button type="primary" @click="submitForm">确 定</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import { getPark, addPark, updatePark, delPark } from "@/api/parking/park";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import RegionSelect from "@/components/RegionSelect";
import { getRegionTree, listTenant } from "@/api/system/tenant";
import dayjs from "dayjs";

export default {
  name: "Park",
  dicts: ["sys_normal_disable", "sys_settlement_cycle"],
  components: { Treeselect, RegionSelect },
  data() {
    return {
      regionProps: {
        children: "children", // 子节点属性名
        label: "regionName", // 标签属性名
      },
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
      // 租户表格数据
      tenantList: null,

      ids: [],

      // 日期范围
      dateRange: [],

      // 表单参数
      form: {},
      //弹出框标题
      title: undefined,
      //弹出框是否显示
      open: false,
       //区域树属性调整，适应区域
      defaultProps: {
        children: "children",
        label: "regionName",
      },
      // 地区树选项
      regionOptions: undefined,
      //区域名称，用于过滤
      regionName: undefined,

      // 日期范围
      dateRange: [],
      
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tenantName: undefined,
        phonenNmber: undefined,
        leader: undefined,
        tenantCode: undefined,
        region: undefined,
        status: undefined,
      },

      // 列信息
      columns: [
        { key: 0, label: `租户编号`, visible: true },
        { key: 1, label: `租户名称`, visible: true },
        { key: 2, label: `租户代码`, visible: true },
        { key: 3, label: `负责人`, visible: true },
        { key: 4, label: `手机号码`, visible: true },
        { key: 5, label: `区域`, visible: true },
        { key: 6, label: `注册日期`, visible: true },
        { key: 7, label: `详细地址`, visible: true },
        { key: 8, label: `状态`, visible: true },
        { key: 9, label: `创建时间`, visible: true },
      ],
      // 表单校验
      rules: {
        tenantName: [
          { required: true, message: "租户名称不能为空", trigger: "blur" },
          {
            min: 2,
            max: 20,
            message: "租户名称长度必须介于 2 和 20 之间",
            trigger: "blur",
          },
        ],
        tenantCode: [
          { required: true, message: "租户代码不能为空", trigger: "blur" },
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
  watch: {
    // 根据名称筛选区域树
    regionName(val) {
      this.$refs.tree.filter(val);
    },
  },
  created() {
    this.initRegionTree();
    this.getList();
  },
  methods: {
    formatRegion(region) {

    },

    //为this.form.region构建格式化数据
    onRegionChange(region) {
    
      this.form.region = region.regionId;
      
    },
    /** 查询租户列表 */
    getList() {
      this.loading = true;
      listTenant(this.queryParams).then((response) => {
        this.tenantList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询区域下拉树结构 */
    initRegionTree() {
      getRegionTree().then((response) => {
        this.regionOptions = response.data;
      });
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.regionName.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.region = data.regionId;
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
      this.queryParams.tenantCode = undefined;
      this.$refs.tree.setCurrentKey(null);
      this.handleQuery();
    },

    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.tenantId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },

    // 表单重置
    reset() {
      this.form = {
        tenantId: NaN,
        status: "0",
      };
      this.resetForm("form");
      
    },
    /** 响应新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加租户";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const tenantIds = row.tenantId || this.ids;

      if (tenantIds) {
        let tenantId = NaN;
        if (Array.isArray(tenantIds)) {
          tenantId = tenantIds[0];
        } else {
          tenantId = tenantIds;
        }

        getPark(tenantId).then((response) => {
          this.form = response.data;
          this.open = true;
          this.title = "修改租户";
        });
      }
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
          let submitObj = this.form;
         
          if (this.form.tenantId != undefined) {
            updatePark(submitObj).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTenant(submitObj).then((response) => {
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
      const tenantIds = row.tenantId || this.ids;
      this.$modal
        .confirm('是否确认删除租户编号为"' + tenantIds + '"的数据项？')
        .then(function () {
          return delPark(tenantIds);
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
      this.upload.title = "租户导入";
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
    //--------------------------格式化代码
    formatRegisterDate(row) {
      return dayjs(row.registrationDate).format("YYYY年MM月DD日"); // 自定义格式
    },
    formatCreateTime(row) {
      return dayjs(row.createTime).format("YYYY-MM-DD HH:MM:ss"); // 自定义格式
    },
  },
};
</script>

<style scoped>
.tenantForm .parkRow {
  margin-bottom: 10px; /* 调整行间距 */
}
.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style>
