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
    ]
});
// 路由守卫
router.beforeEach((to,from,next)=>{
    if(to.path!=='/freshmanreport') return next()
    let tokenStr=window.sessionStorage.getItem('token')
    if (!tokenStr){
        return next('/login')
    }
    next()
})
export default router;