package org.appointment.backend.service.impl;

import lombok.*;
import org.appointment.backend.dto.RandevuDto;
import org.appointment.backend.entity.Randevu;
import org.appointment.backend.repo.RandevuRepository;
import org.appointment.backend.service.RandevuService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class RandevuServiceImpl implements RandevuService {

    private final RandevuRepository randevuRepository;

    @Override
    @Transactional
    public RandevuDto save(RandevuDto randevuDto) {
        Randevu randevu=new Randevu();
        randevu.setTarih(randevuDto.getTarih());
        randevu.setKuafor(randevuDto.getKuafor());
        randevu.setHizmet(randevuDto.getHizmet());
        randevu.setDurum(randevuDto.getDurum());
        randevu.setNotlar(randevuDto.getNotlar());
        randevu.setSaat(randevuDto.getSaat());
        randevu.setUcret(randevuDto.getUcret());
        randevu.setUpdatedAt(randevuDto.getCreatedAt());
        randevu.setCreatedAt(randevuDto.getCreatedAt());
        randevu.setKullanici(randevuDto.getKullanici());
        randevu.setSure(randevuDto.getSure());
        final Randevu randevudb = randevuRepository.save(randevu);

        randevuDto.setRandevuId(randevudb.getRandevuId());
        return randevuDto;
    }

    @Override
    public void delete(long randevuId) {
        randevuRepository.deleteById(randevuId);
    }

    @Transactional
    @Override
    public RandevuDto update(Long ranrevuId, RandevuDto randevuDto){
        Randevu randevu = randevuRepository.findById(ranrevuId)
                .orElseThrow(()-> new RuntimeException("Randevu bulunamadı"));

        randevu.setTarih(randevuDto.getTarih() != null ? randevuDto.getTarih() : randevu.getTarih());
        randevu.setHizmet(randevuDto.getHizmet() != null ? randevuDto.getHizmet() : randevu.getHizmet());
        randevu.setSaat(randevuDto.getSaat() != null ? randevuDto.getSaat() : randevu.getSaat());
        randevu.setKullanici(randevuDto.getKullanici() != null ? randevuDto.getKullanici() : randevu.getKullanici());
        randevu.setUpdatedAt(randevuDto.getUpdatedAt() != null ? randevuDto.getUpdatedAt() : randevu.getUpdatedAt());
        randevu.setCreatedAt(randevuDto.getCreatedAt() != null ? randevuDto.getCreatedAt() : randevu.getCreatedAt());
        randevu.setKullanici(randevuDto.getKullanici() != null ? randevuDto.getKullanici() : randevu.getKullanici());
        randevu.setKuafor(randevuDto.getKuafor() != null ? randevuDto.getKuafor() : randevu.getKuafor());
        randevu.setDurum(randevuDto.getDurum() != null ? randevuDto.getDurum() : randevu.getDurum());
        randevu.setNotlar(randevuDto.getNotlar() != null ? randevuDto.getNotlar() : randevu.getNotlar());
        randevu.setSure(randevuDto.getSure() != null ? randevuDto.getSure() : randevu.getSure());

        Randevu updateRandevu = randevuRepository.save(randevu);

        RandevuDto updateRandevuDto = new RandevuDto();
        updateRandevuDto.setRandevuId(updateRandevu.getRandevuId());
        updateRandevuDto.setSure(updateRandevu.getSure());
        updateRandevuDto.setUpdatedAt(updateRandevu.getUpdatedAt());
        updateRandevuDto.setCreatedAt(updateRandevu.getCreatedAt());
        updateRandevuDto.setKullanici(updateRandevu.getKullanici());
        updateRandevuDto.setKuafor(updateRandevu.getKuafor());
        updateRandevuDto.setDurum(updateRandevu.getDurum());
        updateRandevuDto.setNotlar(updateRandevu.getNotlar());
        updateRandevuDto.setSaat(updateRandevu.getSaat());
        updateRandevuDto.setTarih(updateRandevu.getTarih());
        updateRandevuDto.setHizmet(updateRandevu.getHizmet());
        updateRandevuDto.setUcret(updateRandevu.getUcret());

        return updateRandevuDto;

    }


    @Override
    public List<RandevuDto> getAll(){
        List<Randevu> randevular=randevuRepository.findAll();
        List<RandevuDto> randevuDtos=new ArrayList<>();

        randevular.forEach(it ->{
            RandevuDto randevuDto=new RandevuDto();
            randevuDto.setRandevuId(it.getRandevuId());
            randevuDto.setTarih(it.getTarih());
            randevuDto.setKuafor(it.getKuafor());
            randevuDto.setHizmet(it.getHizmet());
            randevuDto.setDurum(it.getDurum());
            randevuDto.setNotlar(it.getNotlar());
            randevuDto.setSaat(it.getSaat());
            randevuDto.setUcret(it.getUcret());
            randevuDto.setUpdatedAt(it.getUpdatedAt());
            randevuDto.setCreatedAt(it.getCreatedAt());
            randevuDto.setKullanici(it.getKullanici());
            randevuDto.setSure(it.getSure());
            randevuDtos.add(randevuDto);
        });
        return randevuDtos;
    }
    @Override
    public Page<RandevuDto> getAll(Pageable pageable){return null;
    }
}
