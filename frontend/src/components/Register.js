import React, { useState } from 'react';
import './Register.css';

function Register() {
  const [userType, setUserType] = useState('');
  const [showAdminKey, setShowAdminKey] = useState(false);
  const [showKuaforKey, setShowKuaforKey] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    surname: '',
    email: '',
    phone: '',
    password: '',
    adminKey: '',
    kuaforKey: '',
    gender: '',
  });
  const [errors, setErrors] = useState({});

  const handleUserTypeChange = (e) => {
    const value = e.target.value;
    setUserType(value);
    setShowAdminKey(value === 'admin');
    setShowKuaforKey(value === 'kuafor');
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.name) newErrors.name = 'Ad alanı boş bırakılamaz.';
    if (!formData.surname) newErrors.surname = 'Soyad alanı boş bırakılamaz.';
    if (!formData.email) newErrors.email = 'E-posta alanı boş bırakılamaz.';
    if (!formData.phone) newErrors.phone = 'Telefon alanı boş bırakılamaz.';
    if (!formData.password) {
      newErrors.password = 'Şifre alanı boş bırakılamaz.';
    } else if (
      !/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/.test(
        formData.password
      )
    ) {
      newErrors.password =
        'Şifre en az 8 karakter, bir büyük harf, bir küçük harf, bir sayı ve bir özel karakter içermelidir.';
    }
    if (showAdminKey && !formData.adminKey)
      newErrors.adminKey = 'AdminKey zorunludur.';
    if (showKuaforKey && !formData.kuaforKey)
      newErrors.kuaforKey = 'KuaförKey zorunludur.';
    if (!formData.gender) newErrors.gender = 'Cinsiyet seçiniz.';

    return newErrors;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const formErrors = validateForm();
    if (Object.keys(formErrors).length === 0) {
      alert('Form başarıyla gönderildi!');
      console.log(formData);
    } else {
      setErrors(formErrors);
    }
  };

  return (
    <div className="register-container">
      <div
        className="register-image"
        style={{ backgroundImage: "url('/images/kayit.jpg')" }}
      ></div>

      <div className="register-form">
        <h2>Kayıt Ol</h2>
        <p>
          Bir hesabın var mı?{' '}
          <a href="/login" className="link">
            Giriş Yap!
          </a>
        </p>

        <form onSubmit={handleSubmit}>
          <div className="user-gender-row">
            <select name="userType" value={userType} onChange={handleUserTypeChange}>
              <option value="">Kullanıcı Seçiniz</option>
              <option value="admin">Admin</option>
              <option value="kuafor">Kuaför</option>
              <option value="musteri">Müşteri</option>
            </select>
            <select name="gender" value={formData.gender} onChange={handleChange}>
              <option value="">Cinsiyet Seçiniz</option>
              <option value="kadin">Kadın</option>
              <option value="erkek">Erkek</option>
            </select>
          </div>
          {errors.gender && <span className="error">{errors.gender}</span>}

          <div className="input-row">
            <input
              type="text"
              name="name"
              placeholder="Adınızı giriniz"
              value={formData.name}
              onChange={handleChange}
            />
            <input
              type="text"
              name="surname"
              placeholder="Soyadınızı giriniz"
              value={formData.surname}
              onChange={handleChange}
            />
          </div>
          {errors.name && <span className="error">{errors.name}</span>}
          {errors.surname && <span className="error">{errors.surname}</span>}

          <div className="input-row">
            <input
              type="email"
              name="email"
              placeholder="E-posta adresinizi giriniz"
              value={formData.email}
              onChange={handleChange}
            />
            <input
              type="tel"
              name="phone"
              placeholder="Telefon numaranızı giriniz"
              value={formData.phone}
              onChange={handleChange}
            />
          </div>
          {errors.email && <span className="error">{errors.email}</span>}
          {errors.phone && <span className="error">{errors.phone}</span>}

          <div className="input-row">
            <input
              type="password"
              name="password"
              placeholder="Şifre giriniz"
              value={formData.password}
              onChange={handleChange}
            />
            {showAdminKey && (
              <input
                type="text"
                name="adminKey"
                placeholder="AdminKey giriniz"
                value={formData.adminKey}
                onChange={handleChange}
              />
            )}
            {showKuaforKey && (
              <input
                type="text"
                name="kuaforKey"
                placeholder="KuaförKey giriniz"
                value={formData.kuaforKey}
                onChange={handleChange}
              />
            )}
          </div>
          {errors.password && <span className="error">{errors.password}</span>}
          {errors.adminKey && <span className="error">{errors.adminKey}</span>}
          {errors.kuaforKey && <span className="error">{errors.kuaforKey}</span>}

          <button className="register-btn" type="submit">
            Kayıt Ol
          </button>
        </form>
      </div>
    </div>
  );
}

export default Register;
