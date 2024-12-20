import React, { useRef } from 'react';
import './Gallery.css';

function Gallery() {
  const scrollRef = useRef(null);

  const scrollLeft = () => {
    const container = scrollRef.current;
    container.scrollBy({ left: -300, behavior: 'smooth' }); // 300px sola kaydır
  };

  const scrollRight = () => {
    const container = scrollRef.current;
    container.scrollBy({ left: 300, behavior: 'smooth' }); // 300px sağa kaydır
  };

  return (
    <section id="gallery" className="gallery">
      <h2>Galeri</h2>
      <button className="gallery-button left" onClick={scrollLeft}>
        &lt;
      </button>
      <div className="gallery-container" ref={scrollRef}>
        <div className="gallery-list">
          <img src="/images/galeri1.jpg" alt="Galeri 1" />
          <img src="/images/galeri2.jpg" alt="Galeri 2" />
          <img src="/images/galeri3.jpg" alt="Galeri 3" />
          <img src="/images/galeri4.jpg" alt="Galeri 4" />
          <img src="/images/galeri5.jpg" alt="Galeri 5" />
          <img src="/images/galeri6.jpg" alt="Galeri 6" />
        </div>
      </div>
      <button className="gallery-button right" onClick={scrollRight}>
        &gt;
      </button>
    </section>
  );
}

export default Gallery;
