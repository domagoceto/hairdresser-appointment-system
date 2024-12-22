import React, { useEffect, useState } from "react";
import "./MusteriPage.css";
import axios from "axios";

const MusteriPage = () => {
    const [kullaniciAdi, setKullaniciAdi] = useState("");
    const [hizmetler, setHizmetler] = useState([]); // Sadece hizmet adlarını tutacak
    const [kuaforler, setKuaforler] = useState([]);
    const [randevular, setRandevular] = useState([]);
    const [formData, setFormData] = useState({
      hizmet: "",
      kuafor: "",
      tarih: "",
      saat: "",
      notlar: "", // Notlar alanı burada ekleniyor
  });
  
    const [showRandevular, setShowRandevular] = useState(false);

    useEffect(() => {
        fetchKullanici();
        fetchKuaforler();
    }, []);

    const fetchKullanici = async () => {
        const token = localStorage.getItem("authToken");
        if (!token) return console.error("JWT token eksik.");
        try {
            const response = await axios.get("/user/me", {
                headers: { Authorization: `Bearer ${token}` },
            });
            setKullaniciAdi(response.data.ad);
        } catch (error) {
            console.error("Kullanıcı bilgisi alınamadı:", error);
        }
    };

    const fetchHizmetler = async (kuaforId) => {
      const token = localStorage.getItem("authToken");
      if (!token) return console.error("JWT token eksik.");
      if (!kuaforId) return console.error("Kuaför ID eksik.");
  
      try {
          const response = await axios.get(`/kuafor/${kuaforId}/hizmetler/kullanici`, {
              headers: { Authorization: `Bearer ${token}` },
          });
          console.log(response.data); // Backend yanıtını kontrol edin
  
          setHizmetler(
              response.data.yapabilecegiHizmetlerAdlari.map((ad, index) => ({
                  ad,
                  hizmetId: response.data.yapabilecegiHizmetlerIds[index],
              }))
          );
      } catch (error) {
          console.error("Hizmetler alınırken hata oluştu:", error);
      }
  };
  
  
  
  
  

    const fetchKuaforler = async () => {
        const token = localStorage.getItem("authToken");
        if (!token) return console.error("JWT token eksik.");
        try {
            const response = await axios.get("/kuafor/all", {
                headers: { Authorization: `Bearer ${token}` },
            });
            setKuaforler(response.data);
        } catch (error) {
            console.error("Kuaför bilgileri alınamadı:", error);
        }
    };

    const fetchRandevular = async () => {
        try {
            const token = localStorage.getItem("authToken");
            const response = await axios.get("/randevu/goruntule/kullanici", {
                headers: { Authorization: `Bearer ${token}` },
            });
            setRandevular(response.data);
        } catch (error) {
            console.error("Randevular alınırken hata oluştu:", error.response || error.message);
        }
    };

    const fetchRandevuDetay = async (randevuId) => {
        try {
            const token = localStorage.getItem("authToken");
            const response = await axios.get(`/randevu/görüntüle/${randevuId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            alert(
                `Tarih: ${response.data.tarih}, Saat: ${response.data.saat}, Hizmet: ${response.data.hizmet}`
            );
        } catch (error) {
            console.error("Randevu bilgisi alınırken hata oluştu:", error.response || error.message);
            alert("Randevu bilgisi alınamadı.");
        }
    };

    const handleFormChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleKuaforChange = (e) => {
      const selectedKuaforId = e.target.value;
      setFormData({ ...formData, kuafor: selectedKuaforId });
      fetchHizmetler(selectedKuaforId); // Seçilen kuaförün hizmetlerini getir
  };
  

  const handleRandevuAl = async () => {
    const token = localStorage.getItem("authToken");
    if (!token) return console.error("JWT token eksik.");

    try {
        const response = await axios.post(
            "/randevu/randevu",
            {
                kuaforId: formData.kuafor, // Kuaför ID
                hizmetId: formData.hizmet, // Hizmet ID (id olarak gidiyor)
                tarih: formData.tarih, // Tarih
                saat: formData.saat, // Saat
                notlar: formData.notlar || "", // Notlar (opsiyonel)
            },
            { headers: { Authorization: `Bearer ${token}` } }
        );

        alert("Randevu başarıyla alındı!");
        setFormData({ hizmet: "", kuafor: "", tarih: "", saat: "" });
    } catch (error) {
        console.error("Randevu alınırken hata oluştu:", error.response || error.message);
        alert("Randevu alınırken hata oluştu.");
    }
};

  

const handleRandevuIptal = async (randevuId) => {
  if (!randevuId) {
      alert("Geçerli bir randevu seçilmedi.");
      return;
  }
  try {
      const token = localStorage.getItem("authToken");
      if (!token) {
          console.error("JWT token eksik.");
          alert("Lütfen giriş yapın.");
          return;
      }
      // Randevuyu silme isteği
      await axios.delete(`/randevu/randevu/${randevuId}/sil`, {
          headers: { Authorization: `Bearer ${token}` },
      });
      alert("Randevu başarıyla iptal edildi!");

      // İptal işleminden sonra randevu listesini güncelle
      fetchRandevular();
  } catch (error) {
      console.error("Randevu iptal edilirken hata oluştu:", error.response || error.message);
      alert("Randevu iptal edilirken hata oluştu.");
  }
};





    return (
        <div className="musteri-container">
            <h1>👋 Merhaba, {kullaniciAdi || "Kullanıcı"}</h1>
            <div className="content">
                <button
                    className="randevularim-btn"
                    onClick={() => {
                        setShowRandevular(!showRandevular);
                        if (!showRandevular) fetchRandevular();
                    }}
                >
                    Randevularım
                </button>
                {showRandevular && (
                    <div className="randevular">
                        {randevular.length === 0 ? (
                            <p>Henüz bir randevunuz bulunmamaktadır.</p>
                        ) : (
                            randevular.map((randevu) => (
                              <div key={randevu.randevuId} className="randevu-card">
                                    <p>{randevu.tarih} {randevu.saat}</p>
                                    <p>İşlem: {randevu.hizmetAdi || "Belirtilmemiş"}</p>
                                    <button className="iptal-btn" onClick={() => handleRandevuIptal(randevu.randevuId)}>
                                      Randevuyu İptal Et
                                  </button>

                                </div>
                            ))
                        )}
                    </div>
                )}
                <div className="randevu-al">
                    <h2>Randevu Al</h2>
                    <div className="form">

                        <select name="kuafor" value={formData.kuafor} onChange={handleKuaforChange}>
                          <option value="">Kuaför Seçiniz</option>
                          {kuaforler.map((kuafor) => (
                              <option key={kuafor.kuaforId} value={kuafor.kuaforId}>
                                  {kuafor.ad} {kuafor.soyad}
                              </option>
                          ))}
                      </select>

                      <select
                      name="hizmet"
                      value={formData.hizmet}
                      onChange={(e) => {
                          const selectedHizmetId = e.target.value;
                          setFormData({ ...formData, hizmet: selectedHizmetId }); // ID'yi set ediyoruz
                      }}
                >
                      <option value="">Hizmet Seçiniz</option>
                      {hizmetler.map((hizmet) => (
                          <option key={hizmet.hizmetId} value={hizmet.hizmetId}>
                              {hizmet.ad} {/* Kullanıcıya hizmet adı gösteriliyor */}
                          </option>
                      ))}
                  </select>

                        <input
                            type="date"
                            name="tarih"
                            value={formData.tarih}
                            onChange={handleFormChange}
                        />
                        <input
                            type="time"
                            name="saat"
                            value={formData.saat}
                            onChange={handleFormChange}
                        />
                        <div className="form-group">
        <label htmlFor="notlar">Notlar</label>
        <textarea
            id="notlar"
            name="notlar"
            placeholder="Notlarınızı buraya yazabilirsiniz (isteğe bağlı)."
            value={formData.notlar || ""}
            onChange={handleFormChange}
            rows="3"
            className="notlar-textarea"
        />
    </div>
    <button className="randevu-al-btn" onClick={handleRandevuAl}>
        Randevu Al
    </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default MusteriPage;
