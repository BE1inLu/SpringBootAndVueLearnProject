

const Mock = require('mockjs')
const Random = Mock.Random

import DictView from '@/views/DictManage/dict.vue'
import UserView from '@/views/UserManage/user.vue'


// import {mockjs as Mock} from 'mockjs'
// const Random =Mock.Random
// import UserView from '@/views/UserManage/user.vue'



let Result = {
    code: 200,
    msg: 'access',
    obj: null,
}

Mock.mock('/captcha', 'get', () => {

    Result.obj = {
        token: Random.string(32),
        captchaImg: Random.dataImage('120x40', '090909')
    };
    return Result;
})



Mock.mock(RegExp('/login*'), 'post', () => {

    //... 
    // Result.code = 400,
    // Result.msg = "code error"
    // console.log("headers");
    // console.log(Headers)
    console.log("mock-login")


    return Result;
})

Mock.mock('/sys/userInfo', 'get', () => {

    Result.obj = {
        id: "1",
        username: "test",
        avatar: "../assets/logo.png",
    }

    return Result;
})

Mock.mock('/logout', 'post', () => {

    //... 
    Result.code = 200;
    Result.msg = "success";


    return Result;
})



Mock.mock('/views/index/menu', 'get', () => {

    let nav = [
        // {
        //     name:"Index",
        //     title:"主页",
        //     icon:"el-icon-s-home",
        //     path:"/view/home",
        //     component:'Home/homeview',
        //     children:[],
        // },
        {
            name: "SysManage",
            title: "系统管理",
            icon: "el-icon-s-operation",
            path: "",
            component: '',
            children: [
                {
                    name: "SysUser",
                    title: "用户管理",
                    icon: "el-icon-s-custom",
                    path: "/view/user",
                    component: "UserManage/user",
                    children: [],
                },
                {
                    name:"RoleManage",
                    title:"角色管理",
                    icon:"el-icon-rank",
                    path:"/view/role",
                    component:"RoleManage/role",
                    children: [],
                },
                {
                    name:"MenuManage",
                    title:"菜单管理",
                    icon:"el-icon-menu",
                    path:"/view/menu",
                    component:"MenuManage/menu",
                    children: [],
                },
            ],
        },
        {
            name: "SysTools",
            title: "系统工具",
            icon: "el-icon-s-tools",
            path: "",
            component: '',
            children: [
                {
                    name: "SysDict",
                    title: "数字字典",
                    icon: "el-icon-s-order",
                    path: "/view/dict",
                    component: "DictManage/dict",
                    children: [],
                },
            ],
        },
    ];
    let authoritys = ['SysUser', "SysUser:save"];

    Result.obj = {
        nav: nav,
        authoritys: authoritys,
    };

    return Result;
})

// Mock.mock("/view/test/menu/","psot",()=>{
// })

