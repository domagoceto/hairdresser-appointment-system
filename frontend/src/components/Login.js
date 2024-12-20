import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Login.css';

// Geçici kullanıcı verileri
const users = [
  { email: 'admin@example.com', password: 'admin123', role: 'admin' },
  { email: 'kuafor@example.com', password: 'kuafor123', role: 'kuafor' },
  { email: 'musteri@example.com', password: 'musteri123', role: 'musteri' },
];

function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errors, setErrors] = useState({});
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    const newErrors = {};

    if (!email.trim()) {
      newErrors.email = 'E-posta adresi boş bırakılamaz.';
    }

    if (!password.trim()) {
      newErrors.password = 'Şifre alanı boş bırakılamaz.';
    }

    setErrors(newErrors);

    if (Object.keys(newErrors).length === 0) {
      // Kullanıcıyı geçici kullanıcı listesinde kontrol et
      const user = users.find(
        (user) => user.email === email && user.password === password
      );

      if (user) {
        // Kullanıcı türüne göre yönlendirme
        if (user.role === 'admin') {
          navigate('/admin');
        } else if (user.role === 'kuafor') {
          navigate('/kuafor');
        } else if (user.role === 'musteri') {
          navigate('/musteri');
        }
      } else {
        // Hatalı giriş
        setErrors({ general: 'E-posta veya şifre hatalı.' });
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
