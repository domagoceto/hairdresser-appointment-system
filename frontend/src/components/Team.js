import React from 'react';
import './Team.css';

function Team() {
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
        <div className="team-member">
          <img src="/images/ekip1.webp" alt="Gökhan Çağ" />
          <h3>Gökhan Çağ</h3>
        </div>
        <div className="team-member">
          <img src="/images/ekip2.webp" alt="Ayça Akbaş" />
          <h3>Ayça Akbaş</h3>
        </div>
        <div className="team-member">
          <img src="/images/ekip3.webp" alt="Elif Yılmaz" />
          <h3>Elif Yılmaz</h3>
        </div>
      </div>
    </section>
  );
}

export default Team;
