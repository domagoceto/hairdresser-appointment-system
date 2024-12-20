import React from 'react';

function Header() {
  const headerStyle = {
    textAlign: 'center',
    padding: '0',
    margin: '0',
    color: 'white',
    background: 'url("/images/a.png") no-repeat center center/cover',
    height: '100vh',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
  };

  const titleStyle = {
    fontSize: '8rem',
    fontStyle: 'italic',
    marginBottom: '20px',
    color: 'gold',
  };

  const paragraphStyle = {
    fontSize: '1.5rem',
    maxWidth: '800px',
    fontStyle: 'italic',
    textAlign: 'center',
  };

  return (
    <header id="header" style={headerStyle}>
      <h1 style={titleStyle}>Kuafix</h1>
      <p style={paragraphStyle}>
        Her saç bir sanat eseri, her dokunuş bir hikaye...
      </p>
      <p style={paragraphStyle}>
        Kuaför salonumuzda, sizin güzelliğinize ilham veriyor ve hayallerinizi gerçeğe dönüştürüyoruz.
      </p>
    </header>
  );
}

export default Header;
