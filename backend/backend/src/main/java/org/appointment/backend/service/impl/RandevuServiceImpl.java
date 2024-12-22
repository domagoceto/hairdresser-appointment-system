package org.appointment.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.appointment.backend.dto.*;
import org.appointment.backend.entity.*;
import org.appointment.backend.repo.HizmetRepository;
import org.appointment.backend.repo.KuaforRepository;
import org.appointment.backend.repo.KullaniciRepository;
import org.appointment.backend.repo.RandevuRepository;
import org.appointment.backend.service.RandevuService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class RandevuServiceImpl implements RandevuService {

    private final RandevuRepository randevuRepository;
    private final KuaforRepository kuaforRepository;
    private final KullaniciRepository kullaniciRepository;
    private final HizmetRepository hizmetRepository;


    @Override
    public RandevuDto alRandevu(RandevuDto randevuDto) {
        // Tarih ve saat uygun mu kontrol et
        boolean isAvailable = randevuRepository.findByKuafor_KuaforIdAndTarihAndSaat(
                randevuDto.getKuaforId(),
                randevuDto.getTarih(),
                randevuDto.getSaat()
        ).stream().noneMatch(r -> r.getDurum() != RandevuDurum.IPTAL);

        if (!isAvailable) {
            throw new RuntimeException("Belirtilen tarih ve saatte bu kuaför için randevu uygun değil");
        }

        Kullanici kullanici = kullaniciRepository.findById(randevuDto.getKullaniciId())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Kuafor kuafor = kuaforRepository.findById(randevuDto.getKuaforId())
                .orElseThrow(() -> new RuntimeException("Kuaför bulunamadı"));

        Hizmet hizmet = hizmetRepository.findById(randevuDto.getHizmetId())
                .orElseThrow(() -> new RuntimeException("Hizmet bulunamadı"));

        Randevu randevu = new Randevu();
        randevu.setKullanici(kullanici);
        randevu.setKuafor(kuafor);
        randevu.setHizmet(hizmet);
        randevu.setTarih(randevuDto.getTarih());
        randevu.setSaat(randevuDto.getSaat());
        randevu.setDurum(RandevuDurum.AKTIF);
        randevu.setNotlar(randevuDto.getNotlar());
        randevu.setUcret(randevuDto.getUcret());
        randevu.setSure(randevuDto.getSure());

        Randevu savedRandevu = randevuRepository.save(randevu);

        return toRandevuDto(savedRandevu);
    }

    public RandevuResponse createRandevu(RandevuRequest request, Long kullaniciId) {
        // Aynı tarih ve saatte randevu var mı kontrolü
        boolean randevuVarMi = randevuRepository.existsByKuaforIdAndTarihAndSaat(
                request.getKuaforId(),
                request.getTarih(),
                request.getSaat()
        );

        if (randevuVarMi) {
            throw new RuntimeException("Bu tarih ve saatte zaten bir randevu mevcut.");
        }

        // Kuaför ve hizmeti doğrula
        Kuafor kuafor = kuaforRepository.findById(request.getKuaforId())
                .orElseThrow(() -> new RuntimeException("Kuaför bulunamadı"));
        Hizmet hizmet = hizmetRepository.findById(request.getHizmetId())
                .orElseThrow(() -> new RuntimeException("Hizmet bulunamadı"));

        // Fiyat kontrolü ve ücret tanımlama
        Double fiyat = hizmet.getFiyat();
        if (fiyat == null) {
            throw new RuntimeException("Hizmetin fiyatı tanımlı değil.");
        }
        double ucret = fiyat; // Null değilse double olarak kullanılacak

        // Kullanıcı bilgisi
        Kullanici kullanici = kullaniciRepository.findById(kullaniciId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // Hizmet süresi kontrolü
        Integer sure = hizmet.getSure(); // Hizmet süresi dakika cinsinden
        String sureString = (sure != null) ? String.valueOf(sure) : null; // Integer'ı String'e çevir

        // Randevu durumunu belirle
        RandevuDurum durum = RandevuDurum.AKTIF;

        // Randevuyu oluşturma
        Randevu randevu = new Randevu();
        randevu.setKuafor(kuafor);
        randevu.setHizmet(hizmet);
        randevu.setKullanici(kullanici);
        randevu.setTarih(request.getTarih());
        randevu.setSaat(request.getSaat());
        randevu.setNotlar(request.getNotlar());
        randevu.setDurum(durum); // Enum tipi
        randevu.setUcret(ucret); // Hizmetin fiyatını burada kullanıyoruz
        randevu.setSure(sureString); // Hizmet süresi string olarak ayarlanıyor

        // Randevuyu kaydet
        Randevu savedRandevu = randevuRepository.save(randevu);

        // Randevu yanıtını döndürme
        return new RandevuResponse(
                savedRandevu.getRandevuId(),
                kuafor.getAd(),
                kuafor.getSoyad(),
                kullanici.getAd(),
                kullanici.getSoyad(),
                hizmet.getAd(),
                savedRandevu.getTarih(),
                savedRandevu.getSaat(),
                savedRandevu.getDurum().name(),
                savedRandevu.getUcret(),
                savedRandevu.getNotlar(),
                savedRandevu.getSure(),
                hizmet.getAd()

        );
    }

    @Override
    public List<KuaforRandevuResponseDto> getKuaforRandevular(String email, Long kuaforId, LocalDate tarih) {
        // Kuaförü doğrula
        Kuafor kuafor = kuaforRepository.findById(kuaforId)
                .orElseThrow(() -> new RuntimeException("Kuaför bulunamadı."));

        if (!kuafor.getKullanici().getEmail().equals(email)) {
            throw new RuntimeException("Bu kuaföre erişim yetkiniz yok.");
        }

        // Seçili tarihteki randevuları getir
        List<Randevu> randevular = randevuRepository.findByKuafor_KuaforIdAndTarih(kuaforId, tarih);

        // DTO'ya dönüştür
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





    @Override
    public RandevuDto guncelleRandevu(Long randevuId, RandevuDto randevuDto, Long kullaniciId) {
        // Randevu mevcut mu kontrol et
        Randevu randevu = randevuRepository.findById(randevuId)
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı"));

        // Kullanıcının bu randevuya sahip olup olmadığını kontrol et
        if (!randevu.getKullanici().getKullaniciId().equals(kullaniciId)) {
            throw new RuntimeException("Bu randevuyu güncelleme yetkiniz yok");
        }

        // Log ekleniyor
        log.info("Güncelleme için verilen tarih: {}", randevuDto.getTarih());
        log.info("Güncelleme için verilen saat: {}", randevuDto.getSaat());
        log.info("Mevcut tarih ve saat: {}, {}", randevu.getTarih(), randevu.getSaat());

        // Tarih ve saat değişmişse uygunluk kontrolü yap
        if (randevuDto.getTarih() != null || randevuDto.getSaat() != null) {
            LocalDate yeniTarih = randevuDto.getTarih() != null ? randevuDto.getTarih() : randevu.getTarih();
            LocalTime yeniSaat = randevuDto.getSaat() != null ? randevuDto.getSaat() : randevu.getSaat();

            boolean isAvailable = randevuRepository.findByKuafor_KuaforIdAndTarihAndSaatAndRandevuIdNot(
                    randevu.getKuafor().getKuaforId(),
                    yeniTarih,
                    yeniSaat,
                    randevu.getRandevuId()
            ).isEmpty();

            if (!isAvailable) {
                throw new RuntimeException("Belirtilen tarih ve saatte bu kuaför için randevu uygun değil");
            }

            randevu.setTarih(yeniTarih);
            randevu.setSaat(yeniSaat);
        }

        // Diğer alanları güncelle
        if (randevuDto.getDurum() != null) {
            randevu.setDurum(randevuDto.getDurum());
        }
        if (randevuDto.getNotlar() != null) {
            randevu.setNotlar(randevuDto.getNotlar());
        }
        if (randevuDto.getHizmetId() != null) {
            Hizmet hizmet = hizmetRepository.findById(randevuDto.getHizmetId())
                    .orElseThrow(() -> new RuntimeException("Hizmet bulunamadı"));
            randevu.setHizmet(hizmet);
        }
        if (randevuDto.getUcret() != null) {
            randevu.setUcret(randevuDto.getUcret());
        }

        // Randevuyu kaydet
        Randevu updatedRandevu = randevuRepository.save(randevu);

        return toRandevuDto(updatedRandevu);
    }

    @Override
    public List<KuaforRandevuDto> getRandevularByKuaforAndTarih(Long kuaforId, LocalDate tarih) {
        List<Randevu> randevular = randevuRepository.findByKuafor_KuaforIdAndTarih(kuaforId, tarih);

        return randevular.stream().map(randevu -> {
            KuaforRandevuDto dto = new KuaforRandevuDto();
            dto.setSaat(randevu.getSaat());
            dto.setAdSoyad(randevu.getKullanici().getAd() + " " + randevu.getKullanici().getSoyad());
            dto.setTelefon(randevu.getKullanici().getTelefon());
            dto.setIslem(randevu.getHizmet().getAd());
            dto.setUcret(randevu.getUcret()); // Ücret ekleniyor
            dto.setDurum(randevu.getDurum());
            return dto;
        }).collect(Collectors.toList());
    }




    @Override
    public void iptalRandevu(Long randevuId, Long kullaniciId) {
        // Randevu mevcut mu kontrol et
        Randevu randevu = randevuRepository.findById(randevuId)
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı"));

        // Kullanıcının bu randevuya sahip olup olmadığını kontrol et
        if (!randevu.getKullanici().getKullaniciId().equals(kullaniciId)) {
            throw new RuntimeException("Bu randevuyu iptal etme yetkiniz yok");
        }

        // Randevu durumunu IPTAL olarak işaretle
        randevu.setDurum(RandevuDurum.IPTAL);
        randevuRepository.save(randevu);
    }





    @Override
    public List<RandevuDto> getRandevularByKuaforId(Long kuaforId) {
        List<Randevu> randevular = randevuRepository.findByKuafor_KuaforId(kuaforId);
        return randevular.stream()
                .map(this::toRandevuDto)
                .collect(Collectors.toList());
    }

    @Override
    public RandevuDto getRandevu(Long randevuId) {
        Randevu randevu = randevuRepository.findById(randevuId)
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı"));

        return toRandevuDto(randevu);
    }

    @Override
    public List<RandevuDto> getAllRandevular() {
        List<Randevu> randevular = randevuRepository.findAll();
        return randevular.stream()
                .map(this::toRandevuDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RandevuDto> getRandevularByKullaniciId(Long kullaniciId) {
        // Kullanıcı ID'sine göre randevuları alıyoruz
        List<Randevu> randevular = randevuRepository.findByKullanici_KullaniciId(kullaniciId);

        // Randevuları DTO'ya mapliyoruz
        return randevular.stream().map(randevu -> {
            Hizmet hizmet = hizmetRepository.findById(randevu.getHizmet().getHizmetId())
                    .orElseThrow(() -> new RuntimeException("Hizmet bulunamadı"));

            return new RandevuDto(
                    randevu.getRandevuId(),
                    randevu.getTarih(),
                    randevu.getSaat(),
                    randevu.getKuafor().getKuaforId(), // Kuaför ID'si
                    randevu.getHizmet().getHizmetId(), // Hizmet ID'si
                    randevu.getKullanici().getKullaniciId(), // Kullanıcı ID'si
                    randevu.getDurum(), // Randevu durumu
                    randevu.getNotlar(), // Notlar
                    randevu.getUcret(), // Ücret
                    randevu.getSure(), // Süre
                    randevu.getCreatedAt(), // Oluşturulma zamanı
                    randevu.getUpdatedAt(), // Güncellenme zamanı
                    hizmet.getAd() // Hizmet adı
            );
        }).collect(Collectors.toList());
    }




    private RandevuDto toRandevuDto(Randevu randevu) {
        return RandevuDto.builder()
                .randevuId(randevu.getRandevuId())
                .tarih(randevu.getTarih())
                .saat(randevu.getSaat())
                .kuaforId(randevu.getKuafor() != null ? randevu.getKuafor().getKuaforId() : null)
                .hizmetId(randevu.getHizmet() != null ? randevu.getHizmet().getHizmetId() : null)
                .kullaniciId(randevu.getKullanici() != null ? randevu.getKullanici().getKullaniciId() : null)
                .durum(randevu.getDurum())
                .notlar(randevu.getNotlar())
                .ucret(randevu.getUcret())
                .sure(randevu.getSure())
                .createdAt(randevu.getCreatedAt())
                .updatedAt(randevu.getUpdatedAt())
                .build();
    }

}
