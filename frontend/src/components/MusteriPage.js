import React, { useState } from 'react';
import './MusteriPage.css';

function MusteriPage() {
  const [selectedService, setSelectedService] = useState('');
  const [availableHairdressers, setAvailableHairdressers] = useState([]);
  const [selectedHairdresser, setSelectedHairdresser] = useState('');
  const [selectedDate, setSelectedDate] = useState('');
  const [selectedTime, setSelectedTime] = useState('');
  const [showTimeModal, setShowTimeModal] = useState(false);
  const userName = 'Kullanıcı A'; // Örnek kullanıcı adı

  const bookedTimes = ['09:00', '10:00', '11:00']; // Dolu saatler
  const allTimes = ['09:00', '10:00', '11:00', '13:00', '14:00', '15:00', '16:00', '17:00', '18:00'];

  const handleServiceChange = (e) => {
    setSelectedService(e.target.value);
    setAvailableHairdressers([
      { id: 1, name: 'Kuaför A' },
      { id: 2, name: 'Kuaför B' },
    ]);
  };

  const handleTimeSelection = (time) => {
    setSelectedTime(time);
    setShowTimeModal(false);
  };

  const handleReserveAppointment = () => {
    if (!selectedService || !selectedHairdresser || !selectedDate || !selectedTime) {
      alert('Lütfen tüm alanları doldurun.');
      return;
    }
    alert(
      `Randevu alındı!\nİşlem: ${selectedService}\nKuaför: ${selectedHairdresser}\nTarih: ${selectedDate}\nSaat: ${selectedTime}`
    );
  };

  return (
    <div className="musteri-page">
      {/* Üstte Kullanıcı Adı */}
      <header className="user-header">
        <h2>👋 Merhaba, {userName}</h2>
      </header>

      {/* İçerik */}
      <div className="content-container">
        {/* Randevularım Bölümü */}
        <div className="appointments-section">
          <h3>Randevularım</h3>
          <div className="appointment-card">
            <p>23.12.2024 12:00</p>
            <p>İşlem: Saç Kesimi</p>
            <p>Kuaför: Kuaför A</p>
            <button className="cancel-button">Randevuyu İptal Et</button>
          </div>
        </div>

        {/* Randevu Al Bölümü */}
        <div className="reservation-section">
          <h3>Randevu Al</h3>
          <div className="form-group">
            <label>İşlem Seçiniz</label>
            <select
              value={selectedService}
              onChange={handleServiceChange}
              className="form-input"
            >
              <option value="">Seçiniz</option>
              <option value="sac-kesimi">Saç Kesimi</option>
              <option value="sac-boyama">Saç Boyama</option>
              <option value="makyaj">Makyaj</option>
              <option value="fon-cekimi">Fön Çekimi</option>
            </select>
          </div>

          <div className="form-group">
            <label>Kuaför Seçiniz</label>
            <select
              value={selectedHairdresser}
              onChange={(e) => setSelectedHairdresser(e.target.value)}
              className="form-input"
              disabled={!availableHairdressers.length}
            >
              <option value="">Seçiniz</option>
              {availableHairdressers.map((hairdresser) => (
                <option key={hairdresser.id} value={hairdresser.name}>
                  {hairdresser.name}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Tarih Seçiniz</label>
            <input
              type="date"
              value={selectedDate}
              onChange={(e) => setSelectedDate(e.target.value)}
              className="form-input"
            />
          </div>

          <div className="form-group">
            <label>Saat Seçiniz</label>
            <button
              className="time-select-button"
              onClick={() => setShowTimeModal(true)}
            >
              {selectedTime ? `Seçilen Saat: ${selectedTime}` : 'Saat Seçiniz'}
            </button>
          </div>

          <button className="reserve-button" onClick={handleReserveAppointment}>
            Randevu Al
          </button>
        </div>
      </div>

      {/* Saat Seçim Modali */}
      {showTimeModal && (
        <div className="time-modal">
          <div className="time-modal-content">
            <h4>{selectedDate || 'Tarih seçiniz'}</h4>
            <div className="time-grid">
              {allTimes.map((time) => (
                <button
                  key={time}
                  className={`time-slot ${
                    bookedTimes.includes(time) ? 'booked' : selectedTime === time ? 'selected' : ''
                  }`}
                  onClick={() => !bookedTimes.includes(time) && handleTimeSelection(time)}
                  disabled={bookedTimes.includes(time)}
                >
                  {time}
                </button>
              ))}
            </div>
            <button
              className="close-modal-button"
              onClick={() => setShowTimeModal(false)}
            >
              Kapat
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default MusteriPage;
