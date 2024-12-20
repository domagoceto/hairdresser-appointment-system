import axios from 'axios';

// Backend URL'nizi burada belirleyin
const api = axios.create({
  baseURL: 'http://localhost:3000', // Backend URL
  headers: {
    'Content-Type': 'application/json',
  },
});

// API istekleri için token eklemek için interceptor
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken'); // Token'ı localStorage'dan al
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`; // Bearer token başlığı ekle
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Kullanıcı giriş fonksiyonu
export const login = async (email, password) => {
  try {
    const response = await api.post('/auth/login', { email, password });
    return response.data; // Backend'den gelen yanıt
  } catch (error) {
    throw error; // Hata durumunda hata fırlat
  }
};

// Kullanıcı kaydettirme fonksiyonu
export const register = async (userData) => {
  try {
    const response = await api.post('/auth/register', userData);
    return response.data; // Backend'den gelen yanıt
  } catch (error) {
    throw error; // Hata durumunda hata fırlat
  }
};

// Diğer auth işlemleri
export const logout = () => {
  localStorage.removeItem('authToken'); // Token'ı temizle
  localStorage.removeItem('userRole');  // Kullanıcı rolünü temizle
};
