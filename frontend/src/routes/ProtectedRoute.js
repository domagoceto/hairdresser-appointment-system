import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

const ProtectedRoute = ({ allowedRoles }) => {
  const token = localStorage.getItem('authToken'); // Token'ı localStorage'dan al
  let userRole = null;

  if (token) {
    const base64Payload = token.split('.')[1]; // JWT payload kısmını al
    const payload = JSON.parse(atob(base64Payload)); // Decode et
    userRole = payload.roles ? payload.roles[0] : null; // roles varsa, 1. rolü al
  }

  console.log('Token:', token);
  console.log('User Role:', userRole);

  // Eğer token yoksa, login sayfasına yönlendir
  if (!token) {
    return <Navigate to="/login" replace />;
  }

  // Eğer rol izin verilen roller arasında değilse, yetkisiz erişim sayfasına yönlendir
  if (!allowedRoles.includes(userRole)) {
    return <Navigate to="/unauthorized" replace />;
  }

  // Varsayılan olarak, Outlet ile alt bileşenlerin render edilmesine izin verilir
  return <Outlet />;
};

export default ProtectedRoute;
