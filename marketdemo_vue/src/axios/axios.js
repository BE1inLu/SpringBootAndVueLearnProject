import axios from "axios";
import router from "@/router"
import ElementUI from "element-ui";



axios.defaults.baseURL = 'http://localhost:8090'

const request = axios.create({
    timeout: 5000,
    headers: {
        // 'Content-Type': "application/json; charset=utf-8;"
        'Content-Type':"application/json",
    }
})

request.interceptors.request.use(config => {
    
    config.headers["Authorization"] = localStorage.getItem("token");
    

    // test
    console.log("=====axios=====")
    console.log("config message")
    console.log(config)
    // console.log("config headers['Authorization']")
    // console.log(config.headers["Authorization"])
    // console.log("request")
    // console.log(request)
    console.log("localStorage")
    console.log(localStorage)
    console.log("=====axios_exit=====")

    return config
})

request.interceptors.response.use(response => {

    let res = response.data

    if (res.code === 200) {
        return response
    } else {
        ElementUI.Message.error(!res.msg ? "system error" : res.msg)
        return Promise.reject(response.data.msg)
    }
}, error => {

    if (error.response.data) {
        error.massage = error.response.data.msg
    }

    if (error.response.status === 401) {
        router.push("./login")
    }

    ElementUI.Message.error(error.massage, { duration: 3000 })
    return Promise.reject(error)
}

)

export default request
