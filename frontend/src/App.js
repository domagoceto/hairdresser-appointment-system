import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Header from './components/Header';
import Services from './components/Services';
import Team from './components/Team';
import Gallery from './components/Gallery';
import Contact from './components/Contact';
import Footer from './components/Footer';
import Login from './components/Login';
import Register from './components/Register';
import AdminPage from './components/AdminPage';
import KuaforPage from './components/KuaforPage';
import MusteriPage from './components/MusteriPage';
import Unauthorized from './components/Unauthorized';
import ProtectedRoute from './routes/ProtectedRoute';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<><Navbar /><Header /><Services /><Team /><Gallery /><Contact /><Footer /></>} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* Admin Paneli */}
        <Route element={<ProtectedRoute allowedRoles={['ROLE_ADMIN']} />}>
          <Route path="/admin" element={<AdminPage />} />
        </Route>

        {/* Kuaför Paneli */}
        <Route element={<ProtectedRoute allowedRoles={['ROLE_KUAFOR']} />}>
          <Route path="/kuafor" element={<KuaforPage />} />
        </Route>

        {/* Müşteri Paneli */}
        <Route element={<ProtectedRoute allowedRoles={['ROLE_MUSTERI']} />}>
          <Route path="/musteri" element={<MusteriPage />} />
        </Route>

        {/* Yetkisiz Erişim */}
        <Route path="/unauthorized" element={<Unauthorized />} />
      </Routes>
    </Router>
  );
}

export default App;
