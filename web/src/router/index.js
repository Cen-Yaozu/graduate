// 引入路由
import {createRouter, createWebHistory} from "vue-router";
import HomeView from "../views/HomeView";
import LoginView from "../views/LoginView";
import LifeView from "../views/LifeView";
import HotView from "../views/HotView";
import ReportView from "../views/ReportView";
import ActivateView from "@/views/ActivateView";
import FreshmanReportView from "@/views/FreshmanReportView";
import SelectDormView from "@/views/SelectDormView";
import PayView from "@/views/PayView";
import FreshmenView from "@/views/FreshmenView";
import PhotoView from "@/views/PhotoView";
import InformationView from "@/views/InformationView";
import ArriveView from "@/views/ArriveView";
import SHomeView from "@/views/student/SHomeView.vue";
import AHomeView from "@/views/admin/AHomeView";
import SInformation from "@/views/student/SInformation.vue";
import Calendar from "@/views/student/Calendar.vue";
import AdminLayout from "@/views/admin/AdminLayout.vue";

const router=createRouter({
    history:createWebHistory(process.env.BASE_URL),
    routes:[
        {
            path:'/',
            redirect:'/home'
        },
        {
            path:'/home',
            name:'home',
            component:HomeView
        },
        {
            path:'/life',
            name:"life",
            component:LifeView
        },
        {
            path:'/hot',
            name:"hot",
            component:HotView
        },
        {
            path:'/report',
            name:"report",
            component:ReportView
        },
        {
            path:"/activate",
            name:"activate",
            component:ActivateView
        },
        {
          path:"/login",
          name:"login",
          component:LoginView
        },
        {
            path:"/freshmanreport",
            name:"freshmanreport",
            component:FreshmanReportView
        },
        {
          path:"/selectdorm",
          name:"selectdorm",
          component:SelectDormView
        },
        {
            path:"/pay",
            name:"pay",
            component:PayView
        },
        {
            path:"/freshman",
            name:"freshman",
            component:FreshmenView,
            children:[
                {
                    path:"/photo",
                    name:"photo",
                    component:PhotoView
                },
                {
                    path:"/information",
                    name:"information",
                    component:InformationView
                },
                {
                    path:"/arrive",
                    name:"arrive",
                    component:ArriveView
                },
            ]
        },
        {
            path:"/shome",
            name:"shome",
            component:SHomeView,
            meta:{title:"首页"},
            children:[
                {
                    path:"/calendar",
                    name:"Calendar",
                    component:Calendar,
                    meta: {title: "日历"}
                },
                {
                    path:"/sinformation",
                    name:"sinformation",
                    component:SInformation,
                    meta:{title:"信息管理"}
                },
                {
                    path:"/dorm",
                    name:"dorm",
                    component:()=>import('@/views/student/DormView.vue'),
                    meta:{title:"宿舍管理"}
                },
                {
                    path:"/payment",
                    name:"payment",
                    component:()=>import('@/views/student/PaymentView.vue'),
                    meta:{title:"缴费管理"}
                },
                {
                    path:"/arrival",
                    name:"arrival",
                    component:()=>import('@/views/student/ArrivalView.vue'),
                    meta:{title:"抵校管理"}
                },
                {
                    path:"/class",
                    name:"class",
                    component:()=>import('@/views/student/ClassView.vue'),
                    meta:{title:"班级管理"}
                }
            ]
        },
        {
            path:"/ahome",
            name:"ahome",
            component:AHomeView
        },
        // 添加管理员路由
        {
            path:"/admin",
            component: AdminLayout,
            meta: { requiresAdmin: true },
            children: [
                {
                    path: "dashboard",
                    name: "admin-dashboard",
                    component: () => import('@/views/admin/DashboardView.vue'),
                    meta: { title: "仪表盘" }
                },
                {
                    path: "students",
                    name: "admin-students",
                    component: () => import('@/views/admin/StudentManageView.vue'),
                    meta: { title: "学生管理" }
                }
                // 以下路由组件尚未创建，先注释掉，避免编译错误
                /*
                {
                    path: "payments",
                    name: "admin-payments",
                    component: () => import('@/views/admin/PaymentManageView.vue'), 
                    meta: { title: "缴费管理" }
                },
                {
                    path: "classes",
                    name: "admin-classes",
                    component: () => import('@/views/admin/ClassManageView.vue'),
                    meta: { title: "班级管理" }
                },
                {
                    path: "dormitories",
                    name: "admin-dormitories",
                    component: () => import('@/views/admin/DormManageView.vue'),
                    meta: { title: "宿舍管理" }
                },
                {
                    path: "teachers",
                    name: "admin-teachers",
                    component: () => import('@/views/admin/TeacherManageView.vue'),
                    meta: { title: "教师管理" }
                },
                {
                    path: "reports",
                    name: "admin-reports",
                    component: () => import('@/views/admin/ReportManageView.vue'),
                    meta: { title: "报表管理" }
                },
                {
                    path: "calendar",
                    name: "admin-calendar",
                    component: () => import('@/views/admin/CalendarManageView.vue'),
                    meta: { title: "校园日历" }
                },
                {
                    path: "password",
                    name: "admin-password",
                    component: () => import('@/views/admin/PasswordView.vue'),
                    meta: { title: "修改密码" }
                }
                */
            ]
        },
    ]
});
// 路由守卫
router.beforeEach((to,from,next)=>{
    // 检查是否需要管理员权限
    if(to.matched.some(record => record.meta.requiresAdmin)) {
        // 检查两种可能的角色存储
        const userRole = window.sessionStorage.getItem('userRole');
        const fullRole = window.sessionStorage.getItem('role');
        console.log('当前角色:', userRole, fullRole);
        
        // 同时检查两种格式的角色
        if(userRole !== 'ADMIN' && fullRole !== 'ROLE_ADMIN') {
            console.log('无管理员权限，重定向到登录页');
            return next('/login');
        }
    }
    
    if(to.path!=='/freshmanreport') return next()
    let tokenStr=window.sessionStorage.getItem('token')
    if (!tokenStr){
        return next('/login')
    }
    next()
})
export default router;