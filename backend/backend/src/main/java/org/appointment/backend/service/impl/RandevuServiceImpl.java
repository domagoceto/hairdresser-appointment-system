package org.appointment.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.appointment.backend.dto.KuaforRandevuDto;
import org.appointment.backend.dto.KuaforRandevuResponseDto;
import org.appointment.backend.dto.RandevuDto;
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
        List<Randevu> randevular = randevuRepository.findByKullanici_KullaniciId(kullaniciId);
        return randevular.stream()
                .map(this::toRandevuDto)
                .collect(Collectors.toList());
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
