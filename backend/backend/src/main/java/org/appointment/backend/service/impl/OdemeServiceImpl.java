package org.appointment.backend.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.OdemeDto;
import org.appointment.backend.entity.Kullanici;
import org.appointment.backend.entity.Odeme;
import org.appointment.backend.entity.Randevu;
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
    @Transactional
    @Override
    public OdemeDto save(OdemeDto odemeDto) {
        // Kullanıcıyı veritabanından bulup çekme
        Kullanici kullanici = kullaniciRepository.findById(odemeDto.getKullanici().getKullaniciId())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // Randevuyu veritabanından bulup çekme
        Randevu randevu = randevuRepository.findById(odemeDto.getRandevu().getRandevuId())
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı"));

        // Yeni ödeme nesnesini oluşturup mevcut entity'leri ekleme
        Odeme odeme = new Odeme();
        odeme.setTutar(odemeDto.getTutar());
        odeme.setOdemeTarihi(odemeDto.getOdemeTarihi());
        odeme.setOdemeYontemi(odemeDto.getOdemeYontemi());
        odeme.setAciklama(odemeDto.getAciklama());
        odeme.setKullanici(kullanici); // Veritabanından çekilen kullanıcı
        odeme.setRandevu(randevu); // Veritabanından çekilen randevu
        odeme.setDurum(odemeDto.getDurum());

        // Yeni ödeme nesnesini kaydetme
        Odeme savedOdeme = odemeRepository.save(odeme);

        // Kaydedilmiş ödeme nesnesini DTO'ya çevirme ve geri döndürme
        OdemeDto savedOdemeDto = new OdemeDto();
        savedOdemeDto.setOdemeId(savedOdeme.getOdemeId());
        savedOdemeDto.setTutar(savedOdeme.getTutar());
        savedOdemeDto.setOdemeTarihi(savedOdeme.getOdemeTarihi());
        savedOdemeDto.setOdemeYontemi(savedOdeme.getOdemeYontemi());
        savedOdemeDto.setAciklama(savedOdeme.getAciklama());
        savedOdemeDto.setKullanici(savedOdeme.getKullanici());
        savedOdemeDto.setRandevu(savedOdeme.getRandevu());
        savedOdemeDto.setDurum(savedOdeme.getDurum());

        return savedOdemeDto;
    }


    @Override
    public void delete(Long odemeId){
        odemeRepository.deleteById(odemeId);
    }

    @Transactional
    @Override
    public OdemeDto update(Long odemeId, OdemeDto odemeDto) {
        Odeme odeme = odemeRepository.findById(odemeId)
                .orElseThrow(() -> new RuntimeException("Odeme bilgisi bulunamadı"));

        // Eğer güncellenmesi isteniyorsa, Kullanıcıyı veritabanından bulup çekme
        if (odemeDto.getKullanici() != null) {
            Kullanici kullanici = kullaniciRepository.findById(odemeDto.getKullanici().getKullaniciId())
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
            odeme.setKullanici(kullanici);
        }

        // Eğer güncellenmesi isteniyorsa, Randevuyu veritabanından bulup çekme
        if (odemeDto.getRandevu() != null) {
            Randevu randevu = randevuRepository.findById(odemeDto.getRandevu().getRandevuId())
                    .orElseThrow(() -> new RuntimeException("Randevu bulunamadı"));
            odeme.setRandevu(randevu);
        }

        odeme.setDurum(odemeDto.getDurum() != null ? odemeDto.getDurum() : odeme.getDurum());
        odeme.setOdemeYontemi(odemeDto.getOdemeYontemi() != null ? odemeDto.getOdemeYontemi() : odeme.getOdemeYontemi());
        odeme.setOdemeTarihi(odemeDto.getOdemeTarihi() != null ? odemeDto.getOdemeTarihi() : odeme.getOdemeTarihi());
        odeme.setAciklama(odemeDto.getAciklama() != null ? odemeDto.getAciklama() : odeme.getAciklama());
        odeme.setTutar(odemeDto.getTutar() != 0 ? odemeDto.getTutar() : odeme.getTutar());

        Odeme updatedOdeme = odemeRepository.save(odeme);

        OdemeDto updatedOdemeDto = new OdemeDto();
        updatedOdemeDto.setOdemeId(updatedOdeme.getOdemeId());
        updatedOdemeDto.setDurum(updatedOdeme.getDurum());
        updatedOdemeDto.setAciklama(updatedOdeme.getAciklama());
        updatedOdemeDto.setKullanici(updatedOdeme.getKullanici());
        updatedOdemeDto.setOdemeYontemi(updatedOdeme.getOdemeYontemi());
        updatedOdemeDto.setOdemeTarihi(updatedOdeme.getOdemeTarihi());
        updatedOdemeDto.setTutar(updatedOdeme.getTutar());
        updatedOdemeDto.setRandevu(updatedOdeme.getRandevu());

        return updatedOdemeDto;
    }

    @Override
    public List<OdemeDto> getAll(){
        List<Odeme> odemeler = odemeRepository.findAll();
        List<OdemeDto> odemeDtos = new ArrayList<>();

        odemeler.forEach(it->{
            OdemeDto odemeDto = new OdemeDto();
            odemeDto.setOdemeId(it.getOdemeId());
            odemeDto.setTutar(it.getTutar());
            odemeDto.setAciklama(it.getAciklama());
            odemeDto.setKullanici(it.getKullanici());
            odemeDto.setRandevu(it.getRandevu());
            odemeDto.setDurum(it.getDurum());
            odemeDtos.add(odemeDto);
        });
        return odemeDtos;
    }

    @Override
    public Page<OdemeDto> getAll(Pageable pageable) {
        Page<Odeme> odemelerPage = odemeRepository.findAll(pageable);
        return odemelerPage.map(odeme -> {
            OdemeDto odemeDto = new OdemeDto();
            odemeDto.setOdemeId(odeme.getOdemeId());
            odemeDto.setTutar(odeme.getTutar());
            odemeDto.setAciklama(odeme.getAciklama());
            odemeDto.setKullanici(odeme.getKullanici());
            odemeDto.setRandevu(odeme.getRandevu());
            odemeDto.setDurum(odeme.getDurum());
            return odemeDto;
        });
    }


}
