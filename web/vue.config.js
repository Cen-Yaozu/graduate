const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  
  // 开发服务器配置
  devServer: {
    port: 8082, // 前端开发服务器端口
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 后端服务器地址
        changeOrigin: true, // 允许跨域
        pathRewrite: {
          '^/api': '/api' // 不重写路径
        }
      }
    }
  }
})
