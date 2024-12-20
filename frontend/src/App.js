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
import AdminPage from './components/AdminPage'; // Admin ekranı
import KuaforPage from './components/KuaforPage'; // Kuaför ekranı
import MusteriPage from './components/MusteriPage'; // Müşteri ekranı

function App() {
  return (
    <Router>
      <Routes>
        {/* Ana Sayfa */}
        <Route
          path="/"
          element={
            <>
              <Navbar />
              <Header />
              <Services />
              <Team />
              <Gallery />
              <Contact />
              <Footer />
            </>
          }
        />

        {/* Giriş Ekranı */}
        <Route path="/login" element={<Login />} />

        {/* Kayıt Ol Ekranı */}
        <Route path="/register" element={<Register />} />

        {/* Rol Bazlı Ekranlar */}
        <Route path="/admin" element={<AdminPage />} />
        <Route path="/kuafor" element={<KuaforPage />} />
        <Route path="/musteri" element={<MusteriPage />} />
      </Routes>
    </Router>
  );
}

export default App;
