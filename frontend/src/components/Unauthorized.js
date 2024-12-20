import React from 'react';
import { Link } from 'react-router-dom';

const Unauthorized = () => {
  return (
    <div style={{ textAlign: 'center', marginTop: '50px' }}>
      <h1>403 - Yetkisiz Erişim</h1>
      <p>Bu sayfaya erişim yetkiniz bulunmamaktadır.</p>
      <Link to="/">Ana Sayfaya Dön</Link>
    </div>
  );
};

export default Unauthorized;
