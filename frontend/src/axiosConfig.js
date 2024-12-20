import axios from "axios";

const instance = axios.create({
  baseURL: "http://localhost:8080", // Backend adresi
});

instance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token"); // Token'ı localStorage'dan al
    if (token) {
      config.headers.Authorization = `Bearer ${token}`; // Authorization başlığını ekle
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default instance;
