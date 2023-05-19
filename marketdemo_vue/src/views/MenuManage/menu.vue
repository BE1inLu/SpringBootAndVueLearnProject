<template>
    <div>
      Menuview
      <!-- 创建表单 -->
      <el-form :inline="true">
        <el-form-item>
          <el-button type="primary" @click="dialogVisible = true">新增</el-button>
        </el-form-item>
      </el-form>
  
      <el-table
        :data="tableData"
        style="width: 100%; margin-bottom: 20px"
        stripe
        row-key="id"
        default-expand-all
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column prop="name" label="name" width="180" />
        <el-table-column prop="perms" label="权限编码" width="180" />
        <el-table-column prop="icon" label="图标"> </el-table-column>
  
        <el-table-column prop="type" label="类型">
          <template slot-scope="scope">
            <el-tag size="small" v-if="scope.row.type === 0">目录</el-tag>
            <el-tag size="small" v-else-if="scope.row.type === 1" type="success">
              菜单
            </el-tag>
            <el-tag size="small" v-else-if="scope.row.type === 2" type="info">
              按钮
            </el-tag>
          </template>
        </el-table-column>
  
        <el-table-column prop="path" label="菜单URL"> </el-table-column>
        <el-table-column prop="component" label="菜单组件"> </el-table-column>
        <el-table-column prop="orderNum" label="排序号"> </el-table-column>
        <el-table-column prop="statu" label="状态">
          <template slot-scope="scope">
            <el-tag size="small" v-if="scope.row.statu === 1" type="success">
              正常
            </el-tag>
            <el-tag size="small" v-else-if="scope.row.statu === 0" type="danger">
              禁用
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="icon" label="操作">
          <template slot-scope="scope">
            <el-button type="text" @click="editHandle(scope.row.id)">
              编辑
            </el-button>
            <el-divider direction="vertical"></el-divider>
  
            <template>
              <el-popconfirm
                title="这是一段内容确定删除吗？"
                @confirm="delHandle(scope.row.id)"
              >
                <el-button type="text" slot="reference">删除</el-button>
              </el-popconfirm>
            </template>
          </template>
        </el-table-column>
      </el-table>
  
      <!-- 对话框 -->
      <el-dialog
        title="提示"
        :visible.sync="dialogVisible"
        width="600px"
        draggable
        show-close
      >
        <el-form
          :model="editForm"
          :rules="editFormRules"
          ref="editForm"
          label-width="100px"
          class="demo-editForm"
        >
          <el-form-item label="上级菜单" prop="parentId">
            <el-select v-model="editForm.parentId" placeholder="请选择上级菜单">
              <template v-for="item in tableData">
                <el-option :key="item.name" :label="item.name" :value="item.id" />
                <template v-for="child in item.children">
                  <el-option
                    :key="item + child"
                    :label="child.name"
                    :value="child.id"
                  >
                    <span>{{ "- " + child.name }}</span>
                  </el-option>
                </template>
              </template>
            </el-select>
          </el-form-item>
  
          <el-form-item label="菜单名称" prop="name" label-width="100px">
            <el-input v-model="editForm.name" autocomplete="off"></el-input>
          </el-form-item>
  
          <el-form-item label="权限编码" prop="perms" label-width="100px">
            <el-input v-model="editForm.perms" autocomplete="off"></el-input>
          </el-form-item>
  
          <el-form-item label="图标" prop="icon" label-width="100px">
            <el-input v-model="editForm.icon" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="菜单URL" prop="path" label-width="100px">
            <el-input v-model="editForm.path" autocomplete="off"></el-input>
          </el-form-item>
  
          <el-form-item label="菜单组件" prop="component" label-width="100px">
            <el-input v-model="editForm.component" autocomplete="off"></el-input>
          </el-form-item>
  
          <el-form-item label="类型" prop="type" label-width="100px">
            <el-radio-group v-model="editForm.type">
              <el-radio :label="0">目录</el-radio>
              <el-radio :label="1">菜单</el-radio>
              <el-radio :label="2">按钮</el-radio>
            </el-radio-group>
          </el-form-item>
  
          <el-form-item label="状态" prop="statu" label-width="100px">
            <el-radio-group v-model="editForm.statu">
              <el-radio :label="0">禁用</el-radio>
              <el-radio :label="1">正常</el-radio>
            </el-radio-group>
          </el-form-item>
  
          <el-form-item label="排序号" prop="orderNum" label-width="100px">
            <el-input-number v-model="editForm.orderNum" :min="1" label="排序号"
              >1</el-input-number
            >
          </el-form-item>
  
          <el-form-item>
            <el-button type="primary" @click="submitForm('editForm')">
              立即创建
            </el-button>
            <el-button type="info" @click="resetForm('editForm')">重置</el-button>
            <el-button type="danger" @click="dialogVisible = false"
              >关闭</el-button
            >
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
  </template>
  
  
  
  <script>
  export default {
    name: "menu.vue",
    data() {
      return {
        // 表单定义
        searchForm: {
          name: "",
        },
        tableData: [],
  
        multipleSelection: [],
        dialogVisible: false,
        editForm: {},
        // 验证规则定义
        editFormRules: {
          parentId: [
            { required: true, message: "请选择上级菜单", trigger: "blur" },
          ],
          name: [{ required: true, message: "请输入名称", trigger: "blur" }],
          perms: [{ required: true, message: "请输入权限编码", trigger: "blur" }],
          type: [{ required: true, message: "请选择状态", trigger: "blur" }],
          orderNum: [
            { required: true, message: "请填入排序号", trigger: "blur" },
          ],
          statu: [{ required: true, message: "请选择状态", trigger: "blur" }],
        },
      };
    },
    // 方法定义
    methods: {
      // 获取目录树
      getMenuTree() {
        this.$axios.get("/sys/menu/list").then((res) => {
          console.log(res.data.obj);
          this.tableData = res.data.obj;
        });
      },
      // handleClose() {
      //   this.resetForm("editFrom");
      // },
  
      // 表单新增方法
      submitEditForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.$axios
              .post(
                "/view/test/menu/" +
                  (this.editForm.id
                    ? "update"
                    : "save") /*这里通过判断有无id来进行修改或提交操作*/,
                this.editForm
              )
              .then((res) => {
                console.log(res.data);
                this.resetForm(formName);
                this.$message({
                  showClose: true,
                  message: "恭喜你，操作成功",
                  type: "success",
                  // 弹窗完成后更新树状列表
                  onClose: () => {
                    this.getMenuTree();
                  },
                });
              });
          } else {
            console.log("error submit!!");
            return false;
          }
        });
      },
      // 表单修改方法
      editHandle(id) {
        console.log(id);
        this.$axios.get("/sys/menu/info/" + id).then((res) => {
          this.editForm = res.data.obj;
          // 更新后隐藏窗口
          this.dialogFormVisible = true;
        });
      },
      // 表单删除方法
      delHandle(id) {
        this.$axios.post("/sys/menu/delete/" + id).then((res) => {
          console.log(res);
          this.$message({
            showClose: true,
            message: "恭喜你，操作成功",
            type: "success",
            onClose: () => {
              this.getMenuTree();
            },
          });
        });
      },
      // 表单重置方法
      resetForm(formName) {
        this.$refs[formName].resetFields();
        this.editForm = {};
        this.dialogFormVisible = false;
      },
    },
    created() {
      this.getMenuTree();
    },
  };
  </script>
  
  <style>
  </style>