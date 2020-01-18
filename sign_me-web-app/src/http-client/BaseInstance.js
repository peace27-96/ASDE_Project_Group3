import axios from 'axios'

export default axios.create({
    //baseURL:"http://192.168.43.219:8080/signme/"
    //baseURL:"http://192.168.43.48:8080/signme/"
    //baseURL: "http://192.168.43.23:8080/signme/"
    baseURL:"http://localhost:8080/signme/"
})      