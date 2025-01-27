import axios from "axios";

const api = axios.create({
    baseURL: "https://lumijiez.site/",
});

export default api;