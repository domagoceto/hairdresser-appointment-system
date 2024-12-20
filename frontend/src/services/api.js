import axios from 'axios';

// Backend URL'nizi burada belirleyin
const api = axios.create({
  baseURL: 'http://localhost:8080',  // Backend URL
  headers: {
    'Content-Type': 'application/json',
  },
});

// Token eklemek için interceptor
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');  // Token'ı localStorage'dan al
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;  // Token'ı Authorization header'ına ekle
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default api;
