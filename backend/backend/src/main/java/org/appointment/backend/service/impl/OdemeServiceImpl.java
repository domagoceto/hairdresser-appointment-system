package org.appointment.backend.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.OdemeDto;
import org.appointment.backend.entity.*;
import org.appointment.backend.repo.OdemeRepository;
import org.appointment.backend.repo.RandevuRepository;
import org.appointment.backend.service.OdemeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OdemeServiceImpl implements OdemeService {

    private final OdemeRepository odemeRepository;
    private final RandevuRepository randevuRepository;

    @Override
    @Transactional
    public OdemeDto save(OdemeDto odemeDto) {
        // Randevuyu veritabanından bulup çekme
        Randevu randevu = randevuRepository.findById(odemeDto.getRandevuId())
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı. ID: " + odemeDto.getRandevuId()));

        // Ödeme nesnesi oluşturma
        Odeme odeme = new Odeme();
        odeme.setRandevu(randevu);
        odeme.setTutar(randevu.getUcret()); // Tutar, randevudaki ücret
        odeme.setOdemeTarihi(odemeDto.getOdemeTarihi() != null ? odemeDto.getOdemeTarihi() : LocalDateTime.now());
        odeme.setDurum(odemeDto.getDurum() != null ? OdemeDurum.valueOf(odemeDto.getDurum()) : OdemeDurum.ODENMEDI);
        odeme.setKullanici(randevu.getKullanici()); // Kullanıcıyı randevudan al
        odeme.setOdemeYontemi(odemeDto.getOdemeYontemi() != null ? OdemeYontemi.valueOf(odemeDto.getOdemeYontemi()) : null);
        odeme.setAciklama(odemeDto.getAciklama());

        // Ödemeyi veritabanına kaydetme
        Odeme savedOdeme = odemeRepository.save(odeme);

        // Kaydedilen ödemeyi DTO'ya dönüştürme ve geri döndürme
        return convertOdemeToDto(savedOdeme);
    }


    @Override
    @Transactional
    public OdemeDto update(Long odemeId, OdemeDto odemeDto) {
        Odeme odeme = odemeRepository.findById(odemeId)
                .orElseThrow(() -> new RuntimeException("Ödeme bilgisi bulunamadı"));

        if (odemeDto.getRandevuId() != null) {
            // Eğer güncellenmesi isteniyorsa, randevuyu bul
            Randevu randevu = randevuRepository.findById(odemeDto.getRandevuId())
                    .orElseThrow(() -> new RuntimeException("Randevu bulunamadı"));
            odeme.setRandevu(randevu);
            odeme.setTutar(randevu.getUcret()); // Tutarı randevudan güncelle
        }

        if (odemeDto.getDurum() != null) {
            odeme.setDurum(Enum.valueOf(OdemeDurum.class, odemeDto.getDurum()));
        }

        if (odemeDto.getOdemeYontemi() != null) {
            odeme.setOdemeYontemi(Enum.valueOf(OdemeYontemi.class, odemeDto.getOdemeYontemi()));
        }

        if (odemeDto.getAciklama() != null) {
            odeme.setAciklama(odemeDto.getAciklama());
        }

        Odeme updatedOdeme = odemeRepository.save(odeme);

        return convertOdemeToDto(updatedOdeme);
    }

    @Override
    public List<OdemeDto> getAll() {
        List<Odeme> odemeler = odemeRepository.findAllWithDetails(); // JOIN FETCH kullanılan method
        return odemeler.stream().map(odeme -> new OdemeDto(
                odeme.getKullanici() != null
                        ? odeme.getKullanici().getAd() + " " + odeme.getKullanici().getSoyad()
                        : "Müşteri Bilgisi Eksik",
                odeme.getRandevu() != null && odeme.getRandevu().getHizmet() != null
                        ? odeme.getRandevu().getHizmet().getAd()
                        : "İşlem Bilgisi Eksik",
                odeme.getTutar(),
                odeme.getDurum() != null ? odeme.getDurum().name() : "Durum Eksik",
                odeme.getOdemeYontemi() != null ? odeme.getOdemeYontemi().name() : "Yöntem Eksik",
                odeme.getAciklama() != null ? odeme.getAciklama() : "Açıklama Yok"
        )).collect(Collectors.toList());
    }







    private OdemeDto convertOdemeToDto(Odeme odeme) {
        OdemeDto odemeDto = new OdemeDto();
        odemeDto.setOdemeId(odeme.getOdemeId());
        odemeDto.setTutar(odeme.getTutar());
        odemeDto.setOdemeTarihi(odeme.getOdemeTarihi());
        odemeDto.setDurum(odeme.getDurum().name());
        odemeDto.setKullaniciId(odeme.getKullanici().getKullaniciId());
        odemeDto.setAdSoyad(odeme.getKullanici().getAd() + " " + odeme.getKullanici().getSoyad());
        odemeDto.setRandevuId(odeme.getRandevu().getRandevuId());
        odemeDto.setOdemeYontemi(odeme.getOdemeYontemi() != null ? odeme.getOdemeYontemi().name() : null);
        odemeDto.setAciklama(odeme.getAciklama());
        return odemeDto;
    }
}
