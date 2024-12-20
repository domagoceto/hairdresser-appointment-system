import React from 'react';
import { FaMapMarkerAlt, FaEnvelope, FaPhone } from 'react-icons/fa';
import './Contact.css';

function Contact() {
  return (
    <section id="contact" className="contact">
      <h2>İletişim</h2>
      <div className="contact-container">
        <div className="contact-card">
          <FaMapMarkerAlt className="icon" />
          <p>Adres: Menekşe Sokak No:39, <br /> Beşiktaş / İstanbul</p>
        </div>
        <div className="contact-card">
          <FaEnvelope className="icon" />
          <p>E-posta: kuafix@gmail.com</p>
        </div>
        <div className="contact-card">
          <FaPhone className="icon" />
          <p>Telefon: 0822 415 45 44</p>
        </div>
      </div>
    </section>
  );
}

export default Contact;
