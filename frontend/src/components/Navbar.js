import React from 'react';
import { useNavigate } from 'react-router-dom'; // React Router'dan useNavigate
import './Navbar.css';

function Navbar() {
  const navigate = useNavigate(); // Yönlendirme için navigate fonksiyonu

  const handleRandevuClick = () => {
    navigate('/login'); // Giriş ekranına yönlendirme
  };

  return (
    <nav className="navbar">
      <div className="logo">Kuafix</div>
      <ul className="menu">
        <li><a href="#header">Ana Sayfa</a></li>
        <li><a href="#services">Hizmetler</a></li>
        <li><a href="#team">Ekibimiz</a></li>
        <li><a href="#gallery">Galeri</a></li>
        <li><a href="#contact">İletişim</a></li>
      </ul>
      <button className="randevu-button" onClick={handleRandevuClick}>
        Randevu Sistemi
      </button>
    </nav>
  );
}

export default Navbar;
