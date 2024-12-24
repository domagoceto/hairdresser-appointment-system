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

  const handleDeleteAccount = async () => {
    if (!window.confirm("Hesabınızı silmek istediğinize emin misiniz? Bu işlem geri alınamaz!")) {
        return;
    }
    try {
        const token = localStorage.getItem("authToken");
        if (!token) return console.error("JWT token eksik.");
        await axios.delete("/user/delete", {
            headers: { Authorization: `Bearer ${token}` },
        });
        alert("Hesabınız başarıyla silindi. Hoşça kalın!");
        localStorage.removeItem("authToken");
        window.location.href = "/login";
    } catch (error) {
        console.error("Hesap silinirken hata oluştu:", error.response || error.message);
        alert("Hesap silinirken bir hata oluştu.");
    }
};
  
  const handleRandevuAl = async () => {
    const token = localStorage.getItem("authToken");
    if (!token) {
        alert("Lütfen giriş yapınız.");
        return;
    }

    // Ön kontrol: Tarih ve saat için mevcut randevuları kontrol et
    const existingRandevu = randevular.find(
        (randevu) =>
            randevu.tarih === formData.tarih && randevu.saat === formData.saat
    );

    if (existingRandevu) {
        alert("Bu tarih ve saatte zaten bir randevunuz var.");
        return;
    }

    try {
        const response = await axios.post(
            "/randevu/randevu",
            {
                kuaforId: formData.kuafor,
                hizmetId: formData.hizmet,
                tarih: formData.tarih,
                saat: formData.saat,
                notlar: formData.notlar || "",
            },
            { headers: { Authorization: `Bearer ${token}` } }
        );

        alert("Randevu başarıyla alındı!");
        setFormData({ hizmet: "", kuafor: "", tarih: "", saat: "", notlar: "" });
        fetchRandevular(); // Yeni randevuları yükle
    } catch (error) {
        if (error.response && error.response.status === 409) {
            alert(error.response.data || "Bu tarih ve saatte zaten bir randevu mevcut.");
        } else {
            alert("Randevu alınırken bir hata oluştu.");
        }
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
                            <p>Kuaför: {randevu.kuaforAd} {randevu.kuaforSoyad}</p>
                            <button className="iptal-btn" onClick={() => handleRandevuIptal(randevu.randevuId)}>
                                Randevuyu İptal Et
                            </button>
                        </div>
                    ))
                )}
            </div>
        )}

<button
        className="delete-account-btn"
        onClick={handleDeleteAccount}
    >
        Hesabımı Sil
    </button>
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
                            {hizmet.ad}
                        </option>
                    ))}
                </select>

                <input
                    type="date"
                    name="tarih"
                    value={formData.tarih}
                    onChange={handleFormChange}
                />

                <div className="form-group">
                    <label htmlFor="saat">Saat Seçiniz</label>
                    <select
                        id="saat"
                        name="saat"
                        value={formData.saat}
                        onChange={handleFormChange}
                        className="saat-select"
                    >
                        <option value="">Saat Seçiniz</option>
                        {Array.from({ length: 11 }, (_, index) => {
                            const hour = 9 + index; // 09:00'dan 19:00'a kadar saatler
                            const formattedHour = hour.toString().padStart(2, "0") + ":00"; // Saatleri formatla
                            return (
                                <option key={hour} value={formattedHour}>
                                    {formattedHour}
                                </option>
                            );
                        })}
                    </select>
                </div>

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
