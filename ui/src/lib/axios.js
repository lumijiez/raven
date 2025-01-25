import axios from "axios";

const api = axios.create({
    baseURL: "https://lumijiez.pw/",
});

export default api;