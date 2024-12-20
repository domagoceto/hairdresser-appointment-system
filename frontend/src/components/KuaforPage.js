import React, { useState, useEffect } from "react";
import axios from "../services/api"; // Axios servis yapılandırması
import "./KuaforPage.css";

const KuaforPage = () => {
  const [randevular, setRandevular] = useState([]);
  const [selectedDate, setSelectedDate] = useState("");
  const [kuaforAd, setKuaforAd] = useState("");
  const [kuaforId, setKuaforId] = useState(null); // Kuaför ID'si backend'den alınacak
  const [message, setMessage] = useState("");

  // Kuaför bilgilerini yükleme
  useEffect(() => {
    axios
      .get("/kuafor/me", {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("authToken")}`,
        },
      })
      .then((response) => {
        console.log("Kuaför bilgileri:", response.data);
        setKuaforAd(response.data.ad); // Kuaför adını ayarla
        setKuaforId(response.data.kuaforId); // Kuaför ID'sini ayarla
      })
      .catch((error) => {
        console.error("Kuaför bilgileri yüklenirken hata:", error);
        alert("Kuaför bilgileri alınırken bir hata oluştu.");
      });
  }, []);

  const handleDateChange = (event) => {
    setSelectedDate(event.target.value);
  };

  // Randevuları listeleme
  const listRandevular = () => {
    if (!selectedDate) {
      alert("Lütfen bir tarih seçiniz.");
      return;
    }

    if (!kuaforId) {
      alert("Kuaför bilgileri yüklenemedi. Lütfen sayfayı yeniden yükleyin.");
      return;
    }

    axios
      .get(`/kuafor/${kuaforId}/randevular`, {
        params: { tarih: selectedDate },
        headers: {
          Authorization: `Bearer ${localStorage.getItem("authToken")}`,
        },
      })
      .then((response) => {
        console.log("Randevular:", response.data);
        setRandevular(response.data); // Randevuları state'e ata
      })
      .catch((error) => {
        console.error("Randevular yüklenirken hata:", error);
        alert("Randevular yüklenirken bir hata oluştu.");
      });
  };

  return (
    <div className="kuafor-page">
      <h1>Merhaba, {kuaforAd}</h1>

      <div className="container">
        <div className="randevular-container">
          <h2>Randevular</h2>
          <div className="date-picker">
            <input
              type="date"
              value={selectedDate}
              onChange={handleDateChange}
            />
            <button onClick={listRandevular}>Listele</button>
          </div>
          <table>
            <thead>
              <tr>
                <th>Saat</th>
                <th>Müşteri</th>
                <th>İşlem</th>
                <th>Not</th>
              </tr>
            </thead>
            <tbody>
              {randevular.map((randevu, index) => (
                <tr key={index}>
                  <td>{randevu.saat}</td>
                  <td>{randevu.adSoyad}</td>
                  <td>{randevu.hizmet}</td>
                  <td>{randevu.notlar}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default KuaforPage;
