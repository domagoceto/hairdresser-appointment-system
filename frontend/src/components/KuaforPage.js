import React, { useState, useEffect } from "react";
import axios from "../services/api"; // Axios servis yapılandırması
import "./KuaforPage.css";

const KuaforPage = () => {
  const [randevular, setRandevular] = useState([]);
  const [hizmetler, setHizmetler] = useState([]);
  const [allHizmetler, setAllHizmetler] = useState([]);
  const [selectedHizmetId, setSelectedHizmetId] = useState("");
  const [selectedDate, setSelectedDate] = useState("");
  const [isHizmetlerVisible, setIsHizmetlerVisible] = useState(false);
  const [isAddingHizmet, setIsAddingHizmet] = useState(false);
  const [kuaforId, setKuaforId] = useState(null);

  // Kuaför bilgileri
  const [kuaforAd, setKuaforAd] = useState("");
  const [kuaforSoyad, setKuaforSoyad] = useState("");
  const [kuaforEmail, setKuaforEmail] = useState("");
  const [kuaforTelefon, setKuaforTelefon] = useState("");
  const [yeniTelefon, setYeniTelefon] = useState("");

  useEffect(() => {
    fetchKuaforDetails();
  }, []);

  const fetchKuaforDetails = async () => {
    try {
      const response = await axios.get("/kuafor/me", {
        headers: { Authorization: `Bearer ${localStorage.getItem("authToken")}` },
      });

      setKuaforAd(response.data.ad);
      setKuaforSoyad(response.data.soyad);
      setKuaforEmail(response.data.email);
      setKuaforTelefon(response.data.telefon);
      setYeniTelefon(response.data.telefon);
      setKuaforId(response.data.kuaforId);
    } catch (error) {
      console.error("Kuaför bilgileri alınırken hata oluştu:", error);
    }
  };

  const updatePhoneNumber = async () => {
    try {
        await axios.put(
            `/kuafor/update/${kuaforId}`,
            { telefon: yeniTelefon }, // 📌 JSON formatında gönder
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("authToken")}`,
                    "Content-Type": "application/json",
                },
            }
        );
        alert("Telefon numarası başarıyla güncellendi!");
        setKuaforTelefon(yeniTelefon);
    } catch (error) {
        console.error("Telefon güncellenirken hata oluştu:", error);
        alert("Telefon numarası güncellenemedi.");
    }
};



  const listRandevular = async () => {
    if (!selectedDate) {
      alert("Lütfen bir tarih seçiniz.");
      return;
    }
    try {
      const response = await axios.get(`/kuafor/${kuaforId}/randevular`, {
        params: { tarih: selectedDate },
        headers: { Authorization: `Bearer ${localStorage.getItem("authToken")}` },
      });
      if (response.data.length === 0) {
        alert("Bu tarihte herhangi bir randevu bulunmamaktadır.");
      }
      setRandevular(response.data);
    } catch (error) {
      console.error("Randevular yüklenirken hata:", error);
    }
  };

  const fetchKuaforHizmetleri = async () => {
    // Eğer hizmetler zaten görünüyorsa, kapat
    if (isHizmetlerVisible) {
        setIsHizmetlerVisible(false);
        return;
    }

    // Değilse, hizmetleri getir ve aç
    try {
        const response = await axios.get(`/kuafor/${kuaforId}/hizmetler`, {
            headers: { Authorization: `Bearer ${localStorage.getItem("authToken")}` },
        });

        console.log("Gelen Hizmetler:", response.data); // 📌 Konsola gelen veriyi yazdır

        setHizmetler(response.data);
        setIsHizmetlerVisible(true);
    } catch (error) {
        console.error("Hizmetler yüklenirken hata oluştu:", error);
        alert("Hizmetler yüklenirken bir hata oluştu.");
    }
};


const fetchAllHizmetler = async () => {
  try {
      const response = await axios.get("/kuafor/hizmetler", {
          headers: { Authorization: `Bearer ${localStorage.getItem("authToken")}` },
      });

      console.log("Gelen Tüm Hizmetler:", response.data); // 🔍 Konsolda veri kontrolü
      setAllHizmetler(response.data);
      setIsAddingHizmet(true); // ✅ Listeyi aç
  } catch (error) {
      console.error("Tüm hizmetler yüklenirken hata oluştu:", error);
      alert("Hizmetler yüklenirken bir hata oluştu.");
  }
};



const handleAddHizmet = async () => {
  if (!selectedHizmetId) {
      alert("Lütfen bir hizmet seçiniz.");
      return;
  }
  try {
      const response = await axios.post(
          `/kuafor/${kuaforId}/hizmetler`,
  selectedHizmetId, // ✅ JSON nesnesi değil, sadece sayı
  {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("authToken")}`,
      "Content-Type": "application/json",
    },
  }
);
      console.log("Hizmet eklendi, API Yanıtı:", response.data); // ✅ API dönüşünü kontrol et

      alert("Hizmet başarıyla eklendi!");
      setSelectedHizmetId(""); // ✅ Seçili hizmet temizlensin
      setIsAddingHizmet(false);
      fetchKuaforHizmetleri(); // ✅ Güncellenmiş listeyi çek
  } catch (error) {
      console.error("Hizmet eklenirken hata oluştu:", error);
      alert("Hizmet eklenirken bir hata oluştu.");
  }
};



  return (
    <div className="kuafor-page">
      <h1>👋Merhaba, {kuaforAd}</h1>
      <div className="kuafor-container">
        {/* Kuaför Bilgileri */}
        <div className="profile-section">
          <h2>Hesap Bilgilerim</h2>
          <p><strong>Ad:</strong> {kuaforAd}</p>
          <p><strong>Soyad:</strong> {kuaforSoyad}</p>
          <p><strong>E-posta:</strong> {kuaforEmail}</p>
          <p><strong>Telefon:</strong></p>
          <input
            type="text"
            value={yeniTelefon}
            onChange={(e) => setYeniTelefon(e.target.value)}
          />
          <button className="kuafor-update-btn" onClick={updatePhoneNumber}>GÜNCELLE</button>
          <button className="kuafor-delete-btn">Hesabımı Sil</button>
        </div>

        {/* Randevular */}
        <div className="randevular-container">
          <h2>Randevularım</h2>
          <div className="date-picker">
            <input type="date" value={selectedDate} onChange={(e) => setSelectedDate(e.target.value)} />
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

        {/* Hizmetler */}
        <div className="kuafor-hizmetler-container">
    <h2>Hizmetler</h2>
    <div className="kuafor-hizmetler-buttons">
        <button className="kuafor-hizmetler-btn" onClick={fetchKuaforHizmetleri}>Hizmetlerim</button>
        <button className="kuafor-hizmetler-btn" onClick={() => { fetchAllHizmetler(); setIsAddingHizmet(true); }}>Hizmet Ekle</button>
    </div>

    {isAddingHizmet && (
        <div className="hizmet-ekleme-container">
            <h3>Hizmet Seç</h3>
            
            {/* ✅ Hizmet seçme dropdown'ı */}
            <select
                value={selectedHizmetId}
                onChange={(e) => setSelectedHizmetId(e.target.value)}
                className="hizmet-select"
            >
                <option value="">Hizmet Seçiniz</option>
                {allHizmetler.map((hizmet) => (
                    <option key={hizmet.hizmetId} value={hizmet.hizmetId}>
                        {hizmet.ad}
                    </option>
                ))}
            </select>

            {/* ✅ Buton şimdi aşağıda! */}
            <button className="hizmet-ekle-btn" onClick={handleAddHizmet}>
                Hizmet Ekle
            </button>
        </div>
    )}

    {isHizmetlerVisible && (
        <ul className="hizmetler-list">
            {hizmetler.length > 0 ? (
                hizmetler.map((hizmet, index) => (
                    <li key={index} className="hizmet-item">
                        {typeof hizmet === "object" ? hizmet.ad : hizmet}
                    </li>
                ))
            ) : (
                <li>Hizmet bulunamadı.</li>
            )}
        </ul>
    )}
</div>



      </div>
    </div>
  );
};

export default KuaforPage;
