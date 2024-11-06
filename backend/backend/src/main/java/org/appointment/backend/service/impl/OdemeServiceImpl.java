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
    public Page<OdemeDto> getAll(Pageable pageable){
        return null;
    }


}
