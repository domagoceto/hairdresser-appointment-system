package org.appointment.backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.*;
import org.appointment.backend.entity.*;
import org.appointment.backend.repo.HizmetRepository;
import org.appointment.backend.repo.KuaforRepository;
import org.appointment.backend.repo.KullaniciRepository;
import org.appointment.backend.repo.RandevuRepository;
import org.appointment.backend.service.KuaforService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class KuaforServiceImpl implements KuaforService {

    private final KullaniciRepository kullaniciRepository;
    private final KuaforRepository kuaforRepository;
    private final PasswordEncoder passwordEncoder;
    private final RandevuRepository randevuRepository;
    private final HizmetRepository hizmetRepository;

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
    public KuaforDetailsResponse getKuaforByEmailForDetails(String email) {
        Kuafor kuafor = kuaforRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kuaför bulunamadı: " + email));

        // Kuaförü DTO'ya dönüştür ve döndür
        return toDto(kuafor);
    }
    @Transactional
    @Override
    public KuaforDto getHizmetlerByKuaforId(Long kuaforId) {
        // Kuaförü ID'ye göre bul
        Kuafor kuafor = kuaforRepository.findById(kuaforId)
                .orElseThrow(() -> new EntityNotFoundException("Kuaför bulunamadı! ID: " + kuaforId));

        // Kuaförün yapabileceği hizmetleri al
        Set<Hizmet> hizmetler = kuafor.getYapabilecegiHizmetler();

        // Hizmet ID'lerini listeye dönüştür
        List<Long> hizmetlerIds = hizmetler.stream()
                .map(Hizmet::getHizmetId)
                .toList();

        // Hizmet adlarını listeye dönüştür
        List<String> hizmetlerAdlari = hizmetler.stream()
                .map(Hizmet::getAd)
                .toList();

        // DTO'yu oluştur ve döndür
        return KuaforDto.builder()
                .kuaforId(kuafor.getKuaforId())
                .ad(kuafor.getAd())
                .soyad(kuafor.getSoyad())
                .cinsiyet(kuafor.getCinsiyet())
                .telefon(kuafor.getTelefon())
                .email(kuafor.getEmail())
                .yapabilecegiHizmetlerIds(hizmetlerIds)
                .yapabilecegiHizmetlerAdlari(hizmetlerAdlari) // Yeni eklenen alan
                .build();
    }



    @Override
    public Kuafor save(Kuafor kuafor) {
        return kuaforRepository.save(kuafor);
    }

    @Override
    public Kuafor findKuaforByEmail(String email) {
        return kuaforRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kuaför bulunamadı."));
    }

    @Override
    public List<KuaforRandevuResponseDto> getKuaforRandevular(String email, Long kuaforId, LocalDate tarih) {
        Kuafor kuafor = kuaforRepository.findById(kuaforId)
                .orElseThrow(() -> new RuntimeException("Kuaför bulunamadı."));

        if (!kuafor.getKullanici().getEmail().equals(email)) {
            throw new RuntimeException("Bu kuaföre erişim yetkiniz yok.");
        }

        List<Randevu> randevular = randevuRepository.findByKuafor_KuaforIdAndTarih(kuaforId, tarih);

        return randevular.stream()
                .map(randevu -> {
                    KuaforRandevuResponseDto dto = new KuaforRandevuResponseDto();
                    dto.setAdSoyad(randevu.getKullanici().getAd() + " " + randevu.getKullanici().getSoyad());
                    dto.setHizmet(randevu.getHizmet().getAd());
                    dto.setSaat(randevu.getSaat().toString());
                    dto.setNotlar(randevu.getNotlar());
                    dto.setUcret(randevu.getUcret());
                    dto.setSure(randevu.getSure());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<String> getKuaforHizmetler(String email, Long kuaforId) {
        Kuafor kuafor = kuaforRepository.findWithHizmetlerByKuaforId(kuaforId)
                .orElseThrow(() -> new RuntimeException("Kuaför bulunamadı."));

        // Email doğrulaması
        if (!kuafor.getKullanici().getEmail().equals(email)) {
            throw new RuntimeException("Bu kuaföre erişim yetkiniz yok.");
        }

        // Kuaförün hizmetlerini listele
        return kuafor.getYapabilecegiHizmetler().stream() // Burada düzeltme yaptık
                .map(Hizmet::getAd)
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

        Kullanici kullanici = kuafor.getKullanici(); // Kuaför ile ilişkili kullanıcıyı al

        // Güncellenebilir alanları kontrol et ve güncelle
        if (updateRequest.getAd() != null) {
            kullanici.setAd(updateRequest.getAd());
        }
        if (updateRequest.getSoyad() != null) {
            kullanici.setSoyad(updateRequest.getSoyad());
        }
        if (updateRequest.getTelefon() != null) {
            kuafor.setTelefon(updateRequest.getTelefon());
            kullanici.setTelefon(updateRequest.getTelefon());
        }
        if (updateRequest.getEmail() != null) {
            kullanici.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getSifre() != null && !updateRequest.getSifre().isEmpty()) {
            kullanici.setSifre(updateRequest.getSifre());
        }

        // Güncellemeleri kaydet
        kuaforRepository.save(kuafor);
        kullaniciRepository.save(kullanici); // 📌 Kullanıcı tablosunda da değişiklikleri kaydet!

        return kuafor;
    }


    @Transactional
    @Override
    public Kuafor addServiceToKuafor(Long kuaforId, Long hizmetId) {
        Kuafor kuafor = kuaforRepository.findById(kuaforId)
                .orElseThrow(() -> new RuntimeException("Kuaför bulunamadı."));
        Hizmet hizmet = hizmetRepository.findById(hizmetId)
                .orElseThrow(() -> new RuntimeException("Hizmet bulunamadı."));

        kuafor.getYapabilecegiHizmetler().add(hizmet);
        hizmet.getKuaforler().add(kuafor);

        // Her iki varlığı da açıkça kaydedin
        kuaforRepository.save(kuafor);
        hizmetRepository.save(hizmet);

        return kuafor;
    }

    @Override
    public List<KuaforDto> getAllKuaforler() {
        return kuaforRepository.findAll().stream()
                .map(kuafor -> KuaforDto.builder()
                        .kuaforId(kuafor.getKuaforId())
                        .ad(kuafor.getKullanici().getAd())
                        .soyad(kuafor.getKullanici().getSoyad())
                        .email(kuafor.getKullanici().getEmail())
                        .telefon(kuafor.getTelefon())
                        .cinsiyet(kuafor.getKullanici().getCinsiyet())
                        .build()
                )
                .collect(Collectors.toList());
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
