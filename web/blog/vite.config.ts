import { defineConfig } from "vite";
import path from "path";
import vue from "@vitejs/plugin-vue";
import Components from "unplugin-vue-components/vite";
import { AntDesignVueResolver } from "unplugin-vue-components/resolvers";
export default defineConfig({
  resolve: {
    alias: {
      '@': path.resolve('./src') // @代替src
    },
  },
  plugins: [
    vue(),
    Components({
      resolvers: [AntDesignVueResolver()],
    }),
  ],
  server: {
    port: 4000, //启动端口
    proxy: {
      "/api": {
        //对/api开头的请求才进行跨域
        target: "http://localhost:8080",
        changeOrigin: true, // 开启跨域
        rewrite: (path) => path.replace(/^\/api/, ""), //删除请求的'/api'
      },
    },
  },
});
