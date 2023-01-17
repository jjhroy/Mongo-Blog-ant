import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import "ant-design-vue/dist/antd.css";
import "./assets/css/index.css";
import { createPinia } from "pinia";
import piniaPluginPersist from "pinia-plugin-persist";
import { formDate, formDateFullTime } from "@/utils/dateFormat"
// 对vue进行类型补充说明
declare module '@vue/runtime-core' {
    interface ComponentCustomProperties {
        $formatDate: any
    }
    interface ComponentCustomProperties {
        $formatDateFullTime: any
    }
}

const pinia = createPinia();
pinia.use(piniaPluginPersist);

const app = createApp(App);
app.config.globalProperties.$formatDate = formDate
app.config.globalProperties.$formatDateFullTime = formDateFullTime
app.use(router).use(pinia).mount("#app");




