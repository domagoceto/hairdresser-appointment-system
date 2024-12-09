package org.appointment.backend.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.OdemeDto;
import org.appointment.backend.entity.*;
import org.appointment.backend.repo.OdemeRepository;
import org.appointment.backend.service.OdemeService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import java.util.ArrayList;
import java.util.List;
import org.appointment.backend.repo.KullaniciRepository;
import org.appointment.backend.repo.RandevuRepository;


@Service
@RequiredArgsConstructor
public class OdemeServiceImpl implements OdemeService {

    private final OdemeRepository odemeRepository;
    private final KullaniciRepository kullaniciRepository;
    private final RandevuRepository randevuRepository;

    @Override
    @Transactional
    public OdemeDto save(OdemeDto odemeDto) {
        // Kullanıcıyı veritabanından bulup çekme
        Kullanici kullanici = kullaniciRepository.findById(odemeDto.getKullaniciId())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // Randevuyu veritabanından bulup çekme
        Randevu randevu = randevuRepository.findById(odemeDto.getRandevuId())
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı"));

        // Ödeme nesnesi oluşturma
        Odeme odeme = new Odeme();
        odeme.setTutar(odemeDto.getTutar());
        odeme.setOdemeTarihi(odemeDto.getOdemeTarihi());
        odeme.setDurum(OdemeDurum.valueOf(odemeDto.getDurum()));
        odeme.setKullanici(kullanici);
        odeme.setRandevu(randevu);
        odeme.setOdemeYontemi(OdemeYontemi.valueOf(odemeDto.getOdemeYontemi()));
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

        // Eğer güncellenmesi isteniyorsa, Kullanıcıyı veritabanından bulup çekme
        if (odemeDto.getKullaniciId() != null) {
            Kullanici kullanici = kullaniciRepository.findById(odemeDto.getKullaniciId())
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
            odeme.setKullanici(kullanici);
        }

        // Eğer güncellenmesi isteniyorsa, Randevuyu veritabanından bulup çekme
        if (odemeDto.getRandevuId() != null) {
            Randevu randevu = randevuRepository.findById(odemeDto.getRandevuId())
                    .orElseThrow(() -> new RuntimeException("Randevu bulunamadı"));
            odeme.setRandevu(randevu);
        }

        odeme.setDurum(odemeDto.getDurum() != null ? OdemeDurum.valueOf(odemeDto.getDurum()) : odeme.getDurum());
        odeme.setOdemeYontemi(odemeDto.getOdemeYontemi() != null ? OdemeYontemi.valueOf(odemeDto.getOdemeYontemi()) : odeme.getOdemeYontemi());
        odeme.setOdemeTarihi(odemeDto.getOdemeTarihi() != null ? odemeDto.getOdemeTarihi() : odeme.getOdemeTarihi());
        odeme.setAciklama(odemeDto.getAciklama() != null ? odemeDto.getAciklama() : odeme.getAciklama());
        odeme.setTutar(odemeDto.getTutar() != 0 ? odemeDto.getTutar() : odeme.getTutar());

        Odeme updatedOdeme = odemeRepository.save(odeme);

        return convertOdemeToDto(updatedOdeme);
    }

    @Override
    public void delete(Long odemeId) {
        odemeRepository.deleteById(odemeId);
    }

    @Override
    public List<OdemeDto> getAll() {
        List<Odeme> odemeler = odemeRepository.findAll();
        List<OdemeDto> odemeDtos = new ArrayList<>();

        odemeler.forEach(odeme -> odemeDtos.add(convertOdemeToDto(odeme)));

        return odemeDtos;
    }

    @Override
    public Page<OdemeDto> getAll(Pageable pageable) {
        Page<Odeme> odemelerPage = odemeRepository.findAll(pageable);
        return odemelerPage.map(this::convertOdemeToDto);
    }

    // Ödeme nesnesini DTO'ya dönüştürmek için yardımcı metot
    private OdemeDto convertOdemeToDto(Odeme odeme) {
        OdemeDto odemeDto = new OdemeDto();
        odemeDto.setOdemeId(odeme.getOdemeId());
        odemeDto.setTutar(odeme.getTutar());
        odemeDto.setOdemeTarihi(odeme.getOdemeTarihi());
        odemeDto.setDurum(odeme.getDurum().toString());
        odemeDto.setKullaniciId(odeme.getKullanici().getKullaniciId());
        odemeDto.setRandevuId(odeme.getRandevu().getRandevuId());
        odemeDto.setOdemeYontemi(odeme.getOdemeYontemi().toString());
        odemeDto.setAciklama(odeme.getAciklama());

        return odemeDto;
    }
}
