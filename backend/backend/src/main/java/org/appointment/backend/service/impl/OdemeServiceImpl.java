package org.appointment.backend.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.OdemeDto;
import org.appointment.backend.entity.Odeme;
import org.appointment.backend.repo.OdemeRepository;
import org.appointment.backend.service.OdemeService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OdemeServiceImpl implements OdemeService {

    private final OdemeRepository odemeRepository;
    @Transactional
    @Override
    public OdemeDto save(OdemeDto odemeDto){
        Odeme odeme =new Odeme();
        odeme.setTutar(odemeDto.getTutar());
        odeme.setOdemeTarihi(odemeDto.getOdemeTarihi());
        odeme.setOdemeYontemi(odemeDto.getOdemeYontemi());
        odeme.setAciklama(odemeDto.getAciklama());
        odeme.setKullanici(odemeDto.getKullanici());
        odeme.setRandevu(odemeDto.getRandevu());
        odeme.setDurum(odemeDto.getDurum());
        final Odeme odemedb=odemeRepository.save(odeme);

        odemeDto.setOdemeId(odemedb.getOdemeId());
        return odemeDto;
    }

    @Override
    public void delete(Long odemeId){
        odemeRepository.deleteById(odemeId);
    }

    @Override
    @Transactional
    public OdemeDto update(Long odemeId, OdemeDto odemeDto){
        Odeme odeme =odemeRepository.findById(odemeId)
                .orElseThrow(()-> new RuntimeException("Odeme bilgisi bulunamadı"));

        odeme.setDurum(odemeDto.getDurum() != null ? odemeDto.getDurum() : odeme.getDurum());
        odeme.setOdemeYontemi(odemeDto.getOdemeYontemi() != null ? odemeDto.getOdemeYontemi() : odeme.getOdemeYontemi());
        odeme.setOdemeTarihi(odemeDto.getOdemeTarihi() != null ? odemeDto.getOdemeTarihi() : odeme.getOdemeTarihi());
        odeme.setAciklama(odemeDto.getAciklama() != null ? odemeDto.getAciklama() : odeme.getAciklama());
        odeme.setKullanici(odemeDto.getKullanici() != null ? odemeDto.getKullanici() : odeme.getKullanici());
        odeme.setTutar(odemeDto.getTutar() != null ? odemeDto.getTutar() : odeme.getTutar());
        odeme.setRandevu(odemeDto.getRandevu() != null ? odemeDto.getRandevu(): odeme.getRandevu());

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
