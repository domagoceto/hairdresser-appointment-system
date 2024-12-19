package org.appointment.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.KuaforDetailsResponse;
import org.appointment.backend.dto.KuaforRegisterRequest;
import org.appointment.backend.dto.KuaforUpdateRequest;
import org.appointment.backend.dto.RandevuDto;
import org.appointment.backend.entity.Kuafor;
import org.appointment.backend.entity.Kullanici;
import org.appointment.backend.entity.Randevu;
import org.appointment.backend.entity.Rol;
import org.appointment.backend.repo.KuaforRepository;
import org.appointment.backend.repo.KullaniciRepository;
import org.appointment.backend.repo.RandevuRepository;
import org.appointment.backend.service.KuaforService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KuaforServiceImpl implements KuaforService {

    private final KullaniciRepository kullaniciRepository;
    private final KuaforRepository kuaforRepository;
    private final PasswordEncoder passwordEncoder;
    private final RandevuRepository randevuRepository;

    @Override
    public Kuafor registerKuafor(KuaforRegisterRequest request) {
        // Kullanıcıyı oluşturuyoruz
        Kullanici kullanici = Kullanici.builder()
                .ad(request.getAd())
                .soyad(request.getSoyad())
                .email(request.getEmail())
                .telefon(request.getTelefon())
                .sifre(passwordEncoder.encode(request.getSifre()))
                .rol(Rol.KUAFOR)
                .build();

        Kullanici savedKullanici = kullaniciRepository.save(kullanici);

        // Kuaför detaylarını ekliyoruz
        Kuafor kuafor = Kuafor.builder()
                .kullanici(savedKullanici)
                .telefon(request.getTelefon())
                .build();

        return kuaforRepository.save(kuafor);
    }

    @Override
    public Kuafor save(Kuafor kuafor) {
        return kuaforRepository.save(kuafor);
    }


    @Override
    public List<RandevuDto> getKuaforRandevular(String currentUserEmail) {
        // Kuaförün email adresi ile Kuafor nesnesini bul
        Kuafor kuafor = kuaforRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Kuaför bulunamadı."));

        // Kuaföre ait randevuları al
        List<Randevu> randevular = randevuRepository.findByKuafor(kuafor);

        // Randevuları RandevuDto'ya dönüştür
        return randevular.stream()
                .map(this::toRandevuDto)  // Her randevuyu DTO'ya çeviriyoruz
                .collect(Collectors.toList());
    }


    @Override
    public KuaforDetailsResponse getKuaforDetails(Long kuaforId, String currentUserEmail) {
        Kuafor kuafor = kuaforRepository.findById(kuaforId)
                .orElseThrow(() -> new RuntimeException("Kuaför bulunamadı."));

        // Kullanıcı email'inin doğruluğunu kontrol et
        if (!kuafor.getKullanici().getEmail().equals(currentUserEmail)) {
            throw new RuntimeException("Bu kuaförün bilgilerine erişim izniniz yok.");
        }

        return toDto(kuafor);
    }

    @Override
    public Kuafor updateKuaforInfo(Long kuaforId, KuaforUpdateRequest updateRequest, String currentUserEmail) {
        Kuafor kuafor = kuaforRepository.findById(kuaforId)
                .orElseThrow(() -> new RuntimeException("Kuaför bulunamadı: " + kuaforId));

        // Kuaförün güncellenebilir bilgilerini alıyoruz
        if (updateRequest.getAd() != null) {
            kuafor.getKullanici().setAd(updateRequest.getAd());
        }
        if (updateRequest.getSoyad() != null) {
            kuafor.getKullanici().setSoyad(updateRequest.getSoyad());
        }
        if (updateRequest.getTelefon() != null) {
            kuafor.setTelefon(updateRequest.getTelefon());
        }
        if (updateRequest.getEmail() != null) {
            kuafor.getKullanici().setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getSifre() != null && !updateRequest.getSifre().isEmpty()) {
            kuafor.getKullanici().setSifre(updateRequest.getSifre());
        }

        // Güncelleme işlemini kaydediyoruz
        kuaforRepository.save(kuafor);

        return kuafor;
    }

    @Override
    public Kuafor addServiceToKuafor(Long kuaforId, Long hizmetId) {
        Kuafor kuafor = kuaforRepository.findById(kuaforId)
                .orElseThrow(() -> new RuntimeException("Kuaför bulunamadı."));

        // Hizmet ekleme mantığı burada yer almalı
        // Bu kısımda `HizmetRepository` kullanarak hizmeti eklemelisiniz
        return kuafor;
    }

    @Override
    public Kuafor getKuaforById(Long id) {
        return kuaforRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kuaför bulunamadı: " + id));
    }

    // Helper method for converting Kuafor entity to DTO
    private KuaforDetailsResponse toDto(Kuafor kuafor) {
        return new KuaforDetailsResponse(
                kuafor.getKuaforId(),
                kuafor.getKullanici().getAd(),
                kuafor.getKullanici().getSoyad(),
                kuafor.getKullanici().getEmail(),
                kuafor.getTelefon(),
                kuafor.getKullanici().getCinsiyet().name() // Enum -> String dönüşümü
        );
    }

    private RandevuDto toRandevuDto(Randevu randevu) {
        return RandevuDto.builder()
                .randevuId(randevu.getRandevuId())
                .tarih(randevu.getTarih()) // LocalDate tarih bilgisi
                .saat(randevu.getSaat()) // LocalTime saat bilgisi
                .kuaforId(randevu.getKuafor().getKuaforId()) // Kuaför ID'si
                .hizmetId(randevu.getHizmet().getHizmetId()) // Hizmet ID'si
                .kullaniciId(randevu.getKullanici().getKullaniciId()) // Kullanıcı ID'si
                .durum(randevu.getDurum()) // Durum
                .notlar(randevu.getNotlar()) // Notlar
                .ucret(randevu.getUcret()) // Ücret
                .sure(randevu.getSure()) // Süre
                .createdAt(randevu.getCreatedAt()) // CreatedAt
                .updatedAt(randevu.getUpdatedAt()) // UpdatedAt
                .build();
    }







}
