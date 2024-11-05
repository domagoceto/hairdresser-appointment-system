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

public class RandevuServiceımpl implements RandevuService {

    private final RandevuRepository randevuRepository;

    @Override
    @Transactional
    public RandevuDto save(RandevuDto randevuDto) {
        Randevu randevu=new Randevu();
        randevu.setTarih(randevuDto.getTarih());
        randevu.setKuafor(randevuDto.getKuafor());
        randevu.setIslem(randevuDto.getIslem());
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
    };

    @Override
    public List<RandevuDto> getAll(){
        List<Randevu> randevular=randevuRepository.findAll();
        List<RandevuDto> randevuDtos=new ArrayList<>();

        randevular.forEach(it ->{
            RandevuDto randevuDto=new RandevuDto();
            randevuDto.setRandevuId(it.getRandevuId());
            randevuDto.setTarih(it.getTarih());
            randevuDto.setKuafor(it.getKuafor());
            randevuDto.setIslem(it.getIslem());
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
