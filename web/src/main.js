import { createApp } from 'vue'
import App from './App.vue'
import router from "./router";
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import zhCn from 'element-plus/dist/locale/zh-cn'

import axios from "axios";
axios.defaults.baseURL='http://127.0.0.1:8080'

// 3 拦截请求 处理 再放行
axios.interceptors.request.use(config => {
    // 从sessionStorage中获取token并设置到请求头
    const token = sessionStorage.getItem('token');
    console.log('请求拦截器 - 原始token:', token);
    
    if (token) {
        // 确保token格式正确，添加Bearer前缀
        if (!token.startsWith('Bearer ')) {
            config.headers['Authorization'] = `Bearer ${token}`;
            console.log('请求拦截器 - 添加Bearer前缀后:', `Bearer ${token}`);
        } else {
            config.headers['Authorization'] = token;
            console.log('请求拦截器 - 保持原有token:', token);
        }
    }
    
    // 如果是FormData类型的请求（文件上传），不要手动设置Content-Type
    // axios会自动设置正确的Content-Type和boundary
    if (!(config.data instanceof FormData)) {
        config.headers['Content-Type'] = 'application/json';
    }
    
    return config;
}, error => {
    // 处理请求错误
    console.error('请求拦截器错误:', error);
    return Promise.reject(error);
});

// 添加响应拦截器，处理token失效的情况
axios.interceptors.response.use(response => {
    return response;
}, error => {
    console.error('响应错误:', error.response);
    // 处理401错误（未授权）
    if (error.response && error.response.status === 401) {
        console.log('未授权，响应数据:', error.response.data);
        // 如果token校验失败，清除本地token并重定向到登录页
        if (error.response.data && error.response.data.msg && 
            error.response.data.msg.includes('token校验失败')) {
            console.log('Token校验失败，清除token并重定向');
            sessionStorage.removeItem('token');
            sessionStorage.removeItem('role');
            sessionStorage.removeItem('studentNumber');
            window.location.href = '/login';
        }
    }
    return Promise.reject(error);
});

const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.config.globalProperties.$http=axios
app.use(ElementPlus,{locale:zhCn})
app.use(router)
app.mount('#app')



