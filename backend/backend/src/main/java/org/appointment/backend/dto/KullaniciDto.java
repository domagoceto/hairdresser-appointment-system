package org.appointment.backend.dto;

public class KullaniciDto {
    private Long id;
    private String ad;
    private String soyad;

    public KullaniciDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KullaniciDto that = (KullaniciDto) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (ad != null ? !ad.equals(that.ad) : that.ad != null) return false;
        return soyad != null ? soyad.equals(that.soyad) : that.soyad == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (ad != null ? ad.hashCode() : 0);
        result = 31 * result + (soyad != null ? soyad.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "KullaniciDto{" +
                "id=" + id +
                ", ad='" + ad + '\'' +
                ", soyad='" + soyad + '\'' +
                '}';
    }
}