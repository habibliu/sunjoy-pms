<template>
  <div class="app-container">
    <el-form ref="form" :model="form"  label-width="100px">
      <el-form-item label="菜单权限">
        <el-checkbox
          v-model="menuExpand"
          @change="handleCheckedTreeExpand($event, 'menu')"
          >展开/折叠</el-checkbox
        >
        <el-checkbox
          v-model="menuNodeAll"
          @change="handleCheckedTreeNodeAll($event, 'menu')"
          >全选/全不选</el-checkbox
        >
        <el-checkbox
          v-model="form.menuCheckStrictly"
          @change="handleCheckedTreeConnect($event, 'menu')"
          >父子联动</el-checkbox
        >
        <el-tree
          class="tree-border"
          :data="menuOptions"
          show-checkbox
          ref="menu"
          node-key="id"
          :check-strictly="!form.menuCheckStrictly"
          empty-text="加载中，请稍候"
          :props="defaultProps"
        ></el-tree>
      </el-form-item>
      
    </el-form>
  </div>
</template>

<script>
import {
  treeselect as menuTreeselect,
  roleMenuTreeselect,
} from "@/api/system/menu";
export default {
  name: "TenantMenu",
  props: {
    initMenu: {
      type: Boolean,
      required: false,
    }
  },
  data() {
    return {
      defaultProps: {
        children: "children",
        label: "label"
      },
      form: {},
      // 是否显示弹出层（数据权限）
      openDataScope: false,
      menuExpand: false,
      menuNodeAll: false,
      deptExpand: true,
      deptNodeAll: false,
      // 菜单列表
      menuOptions: [],
      // 部门列表
      deptOptions: [],
      // 数据范围选项
      dataScopeOptions: [
        {
          value: "1",
          label: "全部数据权限",
        },
        {
          value: "2",
          label: "自定数据权限",
        },
        {
          value: "3",
          label: "本部门数据权限",
        },
        {
          value: "4",
          label: "本部门及以下数据权限",
        },
        {
          value: "5",
          label: "仅本人数据权限",
        },
      ],
    };
  },
  methods: {
    
    /** 查询菜单树结构 */
    getMenuTreeselect() {
     
      menuTreeselect().then(response => {
        this.menuOptions = response.data;
      });
    },
    // 所有菜单节点数据
    getMenuChecked() {
      // 目前被选中的菜单节点
      let checkedKeys = this.$refs.menu.getCheckedKeys();
      // 半选中的菜单节点
      let halfCheckedKeys = this.$refs.menu.getHalfCheckedKeys();
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
      return checkedKeys;
    },
    
    /** 选择角色权限范围触发 */
    dataScopeSelectChange(value) {
      if (value !== "2") {
        this.$refs.dept.setCheckedKeys([]);
      }
    },
    // 树权限（展开/折叠）
    handleCheckedTreeExpand(value, type) {
      if (type == 'menu') {
        let treeList = this.menuOptions;
        for (let i = 0; i < treeList.length; i++) {
          this.$refs.menu.store.nodesMap[treeList[i].id].expanded = value;
        }
      } 
    },
    // 树权限（全选/全不选）
    handleCheckedTreeNodeAll(value, type) {
      if (type == 'menu') {
        this.$refs.menu.setCheckedNodes(value ? this.menuOptions: []);
      }
    },
    // 树权限（父子联动）
    handleCheckedTreeConnect(value, type) {
      if (type == 'menu') {
        this.form.menuCheckStrictly = value ? true: false;
      } 
    },
  },
  watch: {
    initMenu:{
      handler(newVal,oldVal){
        if(newVal){
          this.getMenuTreeselect();
          this.form.menuCheckStrictly=true;
          this.form.deptCheckStrictly = true;
        }
      },
      deep:true,
      immediate:true
    },
    
  },

};
</script>
