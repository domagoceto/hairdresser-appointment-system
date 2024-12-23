import React, { useEffect, useState } from 'react';
import './Team.css';
import axios from 'axios';

function Team() {
  const [kuaforler, setKuaforler] = useState([]);

  useEffect(() => {
    const fetchKuaforler = async () => {
      try {
        const response = await axios.get("/kuafor/tumKuaforler"); // Token gerekmez
        console.log("Kuaförler:", response.data);
        setKuaforler(response.data);
      } catch (error) {
        console.error("Kuaför bilgileri alınamadı:", error);
      }
    };

    fetchKuaforler();
  }, []);

  const teamStyle = {
    backgroundImage: "url('/images/a.png')",
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    backgroundRepeat: 'no-repeat',
  };

  return (
    <section id="team" className="team" style={teamStyle}>
      <h2>Ekibimiz</h2>
      <div className="team-list">
        {kuaforler.length > 0 ? (
          kuaforler.map((kuafor) => (
            <div className="team-member" key={kuafor.kuaforId}>
              <img
                src={
                  kuafor.cinsiyet === 'ERKEK'
                    ? '/images/ekip1.webp'
                    : '/images/ekip2.webp'
                }
                alt={`${kuafor.ad} ${kuafor.soyad}`}
              />
              <h3>{`${kuafor.ad} ${kuafor.soyad}`}</h3>
            </div>
          ))
        ) : (
          <p>Yükleniyor...</p>
        )}
      </div>
    </section>
  );
}

export default Team;
