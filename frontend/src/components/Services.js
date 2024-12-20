import React, { useRef } from 'react';
import './Services.css';

function Services() {
  const scrollRef = useRef(null);

  const scrollLeft = () => {
    if (scrollRef.current) {
      scrollRef.current.scrollBy({ left: -450, behavior: 'smooth' }); // Kaydırma mesafesi artırıldı
    }
  };

  const scrollRight = () => {
    if (scrollRef.current) {
      scrollRef.current.scrollBy({ left: 450, behavior: 'smooth' }); // Kaydırma mesafesi artırıldı
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
          <div className="service-card">
            <img src="/images/hizmet1.jpg" alt="Saç Kesimi" />
            <h3>Saç Kesimi</h3>
          </div>
          <div className="service-card">
            <img src="/images/hizmet2.jpg" alt="Saç Boyama" />
            <h3>Saç Boyama</h3>
          </div>
          <div className="service-card">
            <img src="/images/hizmet3.jpg" alt="Makyaj" />
            <h3>Makyaj</h3>
          </div>
          <div className="service-card">
            <img src="/images/hizmet4.jpg" alt="Fön" />
            <h3>Fön Çekimi</h3>
          </div>
        </div>
        <button className="scroll-btn right" onClick={scrollRight}>
          &gt;
        </button>
      </div>
    </section>
  );
}

export default Services;
