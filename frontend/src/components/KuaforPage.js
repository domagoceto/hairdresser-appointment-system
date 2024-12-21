import React, { useState, useEffect } from "react";
import axios from "../services/api"; // Axios servis yapılandırması
import "./KuaforPage.css";

const KuaforPage = () => {
  const [randevular, setRandevular] = useState([]);
  const [hizmetler, setHizmetler] = useState([]);
  const [selectedDate, setSelectedDate] = useState("");
  const [kuaforAd, setKuaforAd] = useState("");
  const [kuaforId, setKuaforId] = useState(null);
  const [isHizmetlerVisible, setIsHizmetlerVisible] = useState(false);
  const [allHizmetler, setAllHizmetler] = useState([]); // Tüm hizmetler
  const [selectedHizmetId, setSelectedHizmetId] = useState(""); // Seçili hizmet ID
  const [isAddingHizmet, setIsAddingHizmet] = useState(false); // Hizmet ekleme modal durumu

  // Kuaför bilgilerini yükleme
  useEffect(() => {
    // Kuaför bilgilerini alma
    axios
      .get("/kuafor/me", {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("authToken")}`,
        },
      })
      .then((response) => {
        setKuaforAd(response.data.ad);
        setKuaforId(response.data.kuaforId);
      })
      .catch((error) => {
        console.error("Kuaför bilgileri yüklenirken hata:", error);
        alert("Kuaför bilgileri alınırken bir hata oluştu.");
      });

    // Tüm hizmetleri yükleme
    axios
      .get("/hizmetler", {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("authToken")}`,
        },
      })
      .then((response) => {
        setAllHizmetler(response.data); // Gelen hizmetleri kaydet
      })
      .catch((error) => {
        console.error("Tüm hizmetler yüklenirken hata oluştu:", error);
        alert("Hizmetler yüklenirken bir hata oluştu.");
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
        setRandevular(response.data);
      })
      .catch((error) => {
        console.error("Randevular yüklenirken hata:", error);
        alert("Randevular yüklenirken bir hata oluştu.");
      });
  };

  // Hizmetleri listeleme
  const toggleHizmetler = () => {
    if (!isHizmetlerVisible) {
      if (!kuaforId) {
        alert("Kuaför bilgileri yüklenemedi. Lütfen sayfayı yeniden yükleyin.");
        return;
      }

      axios
        .get(`/kuafor/${kuaforId}/hizmetler`, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("authToken")}`,
          },
        })
        .then((response) => {
          setHizmetler(response.data);
          setIsHizmetlerVisible(true); // Görünür yap
        })
        .catch((error) => {
          console.error("Hizmetler yüklenirken hata:", error);
          alert("Hizmetler yüklenirken bir hata oluştu.");
        });
    } else {
      setIsHizmetlerVisible(false); // Gizle
    }
  };

  // Hizmet ekleme modal'ını açma
  const addHizmet = () => {
    setIsAddingHizmet(true);
  };

  // Yeni hizmet ekleme işlemi
  const handleAddHizmet = () => {
    if (!selectedHizmetId) {
      alert("Lütfen bir hizmet seçiniz.");
      return;
    }

    axios
      .post(
        `/kuafor/${kuaforId}/hizmetler`,
        selectedHizmetId, // Hizmet ID'sini gönderiyoruz
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("authToken")}`,
          },
        }
      )
      .then(() => {
        alert("Hizmet başarıyla eklendi!");
        setSelectedHizmetId(""); // Dropdown'u temizle
        setIsAddingHizmet(false); // Modal'ı kapat
        toggleHizmetler(); // Yeni hizmetleri güncellemek için listeyi yenile
      })
      .catch((error) => {
        console.error("Hizmet eklenirken hata oluştu:", error);
        alert("Hizmet eklenirken bir hata oluştu.");
      });
  };

  // Modal'ı kapatma
  const closeAddHizmetModal = () => {
    setIsAddingHizmet(false);
  };

  return (
    <div className="kuafor-page">
      <h1>Merhaba, {kuaforAd}</h1>

      <div className="container">
        <div className="randevular-container">
          <h2>Randevularım</h2>
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

        <div className="hizmetler-container">
          <h2>Hizmetler</h2>
          <div className="hizmetler-buttons">
            <button className="left-button" onClick={toggleHizmetler}>
              Hizmetlerim
            </button>
            <button className="right-button" onClick={addHizmet}>
              Hizmet Ekle
            </button>
          </div>
          {isHizmetlerVisible && (
            <ul className="hizmetler-list">
              {hizmetler.map((hizmet, index) => (
                <li key={index}>{hizmet}</li>
              ))}
            </ul>
          )}
        </div>
      </div>

      {isAddingHizmet && (
        <div className="modal">
          <div className="modal-content">
            <h2>Yeni Hizmet Ekle</h2>
            <select
              value={selectedHizmetId}
              onChange={(e) => setSelectedHizmetId(e.target.value)}
            >
              <option value="">Hizmet seçin</option>
              {allHizmetler.map((hizmet) => (
                <option key={hizmet.id} value={hizmet.id}>
                  {hizmet.ad}
                </option>
              ))}
            </select>
            <button onClick={handleAddHizmet}>Ekle</button>
            <button onClick={closeAddHizmetModal}>İptal</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default KuaforPage;
