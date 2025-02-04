import React, { useRef, useEffect, useState } from "react";
import axios from "axios";
import "./Gallery.css";

function Gallery() {
  const scrollRef = useRef(null);
  const [images, setImages] = useState([]);

  useEffect(() => {
    fetchGalleryImages();
  }, []);

  const fetchGalleryImages = async () => {
    try {
      const response = await axios.get("http://localhost:8080/gallery/list");
      setImages(response.data);
    } catch (error) {
      console.error("Galeri yüklenemedi:", error);
    }
  };

  const scrollLeft = () => {
    const container = scrollRef.current;
    container.scrollBy({ left: -300, behavior: "smooth" }); // 300px sola kaydır
  };

  const scrollRight = () => {
    const container = scrollRef.current;
    container.scrollBy({ left: 300, behavior: "smooth" }); // 300px sağa kaydır
  };

  return (
    <section id="gallery" className="gallery">
      <h2>Galeri</h2>
      <button className="gallery-button left" onClick={scrollLeft}>
        &lt;
      </button>
      <div className="gallery-container" ref={scrollRef}>
        <div className="gallery-list">
          {images.length > 0 ? (
            images.map((image, index) => (
              <img
                key={index}
                src={`http://localhost:8080${image}`}
                alt={`Galeri ${index + 1}`}
              />
            ))
          ) : (
            <p>Henüz galeriye resim eklenmedi.</p>
          )}
        </div>
      </div>
      <button className="gallery-button right" onClick={scrollRight}>
        &gt;
      </button>
    </section>
  );
}

export default Gallery;
