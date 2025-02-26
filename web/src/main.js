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
axios.interceptors.request.use(req=>{
    req.headers.Authorization=window.sessionStorage.getItem('token')
    req.headers['Content-Type']="application/json"
    // console.log(req);
    return req
},error => {
    // 处理请求错误
    return Promise.reject(error);
})

const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.config.globalProperties.$http=axios
app.use(ElementPlus,{locale:zhCn})
app.use(router)
app.mount('#app')



