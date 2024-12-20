import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from '../services/api'; // Backend API istekleri için axios kullanımı
import './Login.css';

function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errors, setErrors] = useState({});
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const newErrors = {};

    // Validation
    if (!email.trim()) {
      newErrors.email = 'E-posta adresi boş bırakılamaz.';
    }
    if (!password.trim()) {
      newErrors.password = 'Şifre alanı boş bırakılamaz.';
    }

    setErrors(newErrors);

    if (Object.keys(newErrors).length === 0) {
      try {
        // Backend'e login isteği gönderme
        const response = await axios.post('/auth/login', {
          email,
          password,
        });

        const token = response.data.split(" ")[1]; // "Bearer token" formatında gelir
        localStorage.setItem('authToken', token); // Token'ı localStorage'a kaydetme

        // Token'dan payload'ı decode etme
        const base64Payload = token.split('.')[1]; // JWT payload kısmını al
        const payload = JSON.parse(atob(base64Payload)); // Decode et
        const userRole = payload.roles ? payload.roles[0] : 'undefined'; // Kullanıcı rolünü al

        console.log('Token:', token);
        console.log('Decoded Payload:', payload);
        console.log('User Role:', userRole);

        // Role göre yönlendirme
        if (userRole === 'ROLE_ADMIN') {
          navigate('/admin'); // Admin paneline yönlendir
        } else if (userRole === 'ROLE_KUAFOR') {
          navigate('/kuafor'); // Kuaför paneline yönlendir
        } else if (userRole === 'ROLE_MUSTERI') {
          navigate('/musteri'); // Müşteri paneline yönlendir
        }
      } catch (error) {
        // Hatalı giriş durumunda hata mesajını göster
        if (error.response && error.response.status === 401) {
          setErrors({ general: 'E-posta veya şifre hatalı.' });
        } else {
          setErrors({ general: 'Beklenmeyen bir hata oluştu.' });
        }
      }
    }
  };

  return (
    <div className="login-container">
      <div
        className="login-image"
        style={{ backgroundImage: "url('/images/giris.jpg')" }}
      ></div>

      <div className="login-form">
        <h2>Giriş</h2>
        <p>
          Bir hesabın yok mu?{' '}
          <a href="/register" className="link">
            Kayıt Ol!
          </a>
        </p>

        {errors.general && <span className="error">{errors.general}</span>}

        <form onSubmit={handleSubmit}>
          <div>
            <input
              type="email"
              placeholder="E-posta adresinizi girin"
              className={`input-field ${errors.email ? 'error-input' : ''}`}
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            {errors.email && <span className="error">{errors.email}</span>}
          </div>

          <div>
            <input
              type="password"
              placeholder="Şifrenizi girin"
              className={`input-field ${errors.password ? 'error-input' : ''}`}
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            {errors.password && <span className="error">{errors.password}</span>}
          </div>

          <button type="submit" className="submit-btn">
            Giriş
          </button>
        </form>
      </div>
    </div>
  );
}

export default Login;
