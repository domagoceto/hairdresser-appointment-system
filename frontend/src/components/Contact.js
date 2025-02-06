import React, { useEffect, useState } from "react";
import { FaMapMarkerAlt, FaEnvelope, FaPhone } from "react-icons/fa";
import axios from "axios";
import "./Contact.css";

function Contact() {
  const [contactInfo, setContactInfo] = useState({
    adres: "Adres bilgisi yükleniyor...",
    email: "E-posta yükleniyor...",
    telefon: "Telefon yükleniyor...",
  });

  useEffect(() => {
    fetchContactInfo();
  }, []);

  const fetchContactInfo = async () => {
    try {
      const response = await axios.get("http://localhost:8080/contact/info");
      if (response.data) {
        setContactInfo(response.data);
      }
    } catch (error) {
      console.error("İletişim bilgileri alınamadı:", error);
      setContactInfo({
        adres: "Adres bilgisi mevcut değil",
        email: "E-posta bilgisi mevcut değil",
        telefon: "Telefon bilgisi mevcut değil",
      });
    }
  };

  return (
    <section id="contact" className="contact">
      <h2>İletişim</h2>
      <div className="contact-container">
  <span className="contact-card">
    <FaMapMarkerAlt className="icon" />
    <p>{contactInfo.adres}</p>
  </span>
  <span className="contact-card">
    <FaEnvelope className="icon" />
    <p>{contactInfo.email}</p>
  </span>
  <span className="contact-card">
    <FaPhone className="icon" />
    <p>{contactInfo.telefon}</p>
  </span>
</div>

    </section>
  );
}

export default Contact;
