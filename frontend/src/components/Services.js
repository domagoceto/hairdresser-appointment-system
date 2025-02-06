import React, { useEffect, useState, useRef } from "react";
import axios from "axios";
import "./Services.css";

const Services = () => {
  const [services, setServices] = useState([]);
  const scrollRef = useRef(null);

  useEffect(() => {
    fetchServices();
  }, []);

  const fetchServices = async () => {
    const token = localStorage.getItem("authToken");
    if (!token) return console.error("JWT token eksik.");
    
    try {
      const response = await axios.get("/hizmet/list", {
        headers: { Authorization: `Bearer ${token}` },
      });
      console.log("API'den Gelen Hizmetler:", response.data); // **EKLENDİ**
      setServices(response.data);
    } catch (error) {
      console.error("Hizmetler alınamadı:", error);
    }
  };
  const scrollLeft = () => {
    if (scrollRef.current) {
      scrollRef.current.scrollBy({ left: -450, behavior: "smooth" });
    }
  };

  const scrollRight = () => {
    if (scrollRef.current) {
      scrollRef.current.scrollBy({ left: 450, behavior: "smooth" });
    }
  };

  return (
    <section id="services" className="services">
      <h2>Hizmetlerimiz</h2>
      <div className="services-container">
        <button className="scroll-btn left" onClick={scrollLeft}>
          &lt;
        </button>
        <div className="services-list" ref={scrollRef}>
          {services.map((service) => (
            <div key={service.hizmetId} className="service-card">
              {service.imageUrl ? (
                <img src={service.imageUrl} alt={service.ad} />
              ) : (
                <img src="/images/default_service.jpg" alt="Varsayılan" />
              )}
              <h3>{service.ad}</h3>
            </div>
          ))}
        </div>
        <button className="scroll-btn right" onClick={scrollRight}>
          &gt;
        </button>
      </div>
    </section>
  );
};

export default Services;
