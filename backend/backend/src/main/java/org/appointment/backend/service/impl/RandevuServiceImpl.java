package org.appointment.backend.service.impl;

import lombok.*;
import org.appointment.backend.dto.RandevuDto;
import org.appointment.backend.entity.*;
import org.appointment.backend.repo.*;
import org.appointment.backend.service.KullaniciService;
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
    private final KullaniciRepository kullaniciRepository;
    private final HizmetRepository hizmetRepository;
    private final KuaforRepository kuaforRepository;
    private final OdemeRepository odemeRepository;

    @Override
    @Transactional
    public RandevuDto save(RandevuDto randevuDto) {
        // Kullanıcıyı veritabanından bulup çekme
        Kullanici kullanici = kullaniciRepository.findById(randevuDto.getKullanici().getKullaniciId())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // Hizmeti veritabanından bulup çekme
        Hizmet hizmet = hizmetRepository.findById(randevuDto.getHizmet().getHizmetId())
                .orElseThrow(() -> new RuntimeException("Hizmet bulunamadı"));

        // Kuaförü veritabanından bulup çekme
        Kuafor kuafor = kuaforRepository.findById(randevuDto.getKuafor().getKuaforId())
                .orElseThrow(() -> new RuntimeException("Kuaför bulunamadı"));

        // Randevu nesnesi oluşturma
        Randevu randevu = new Randevu();
        randevu.setTarih(randevuDto.getTarih());
        randevu.setKuafor(kuafor); // Veritabanından çekilen kuaför
        randevu.setHizmet(hizmet); // Veritabanından çekilen hizmet
        randevu.setDurum(randevuDto.getDurum());
        randevu.setNotlar(randevuDto.getNotlar());
        randevu.setSaat(randevuDto.getSaat());
        randevu.setUcret(randevuDto.getUcret());
        randevu.setKullanici(kullanici); // Veritabanından çekilen kullanıcı
        randevu.setSure(randevuDto.getSure());

        // Randevu kaydetme
        final Randevu randevudb = randevuRepository.save(randevu);
        randevuDto.setRandevuId(randevudb.getRandevuId());

        return randevuDto;
    }


    @Override
    @Transactional
    public void delete(Long id) {
        // Randevuyu bul
        Randevu randevu = randevuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı"));

        // Randevuya bağlı ödeme varsa, önce ödeme kaydını sil
        List<Odeme> odemeler = odemeRepository.findByRandevu(randevu);
        if (!odemeler.isEmpty()) {
            odemeRepository.deleteAll(odemeler);
        }

        // Sonra randevuyu sil
        randevuRepository.deleteById(id);
    }








    @Transactional
    @Override
    public RandevuDto update(Long randevuId, RandevuDto randevuDto) {
        Randevu randevu = randevuRepository.findById(randevuId)
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı"));

        if (randevuDto.getTarih() != null) {
            randevu.setTarih(randevuDto.getTarih());
        }
        if (randevuDto.getHizmet() != null) {
            Hizmet hizmet = hizmetRepository.findById(randevuDto.getHizmet().getHizmetId())
                    .orElseThrow(() -> new RuntimeException("Hizmet bulunamadı"));
            randevu.setHizmet(hizmet);
        }
        if (randevuDto.getSaat() != null) {
            randevu.setSaat(randevuDto.getSaat());
        }
        if (randevuDto.getKullanici() != null) {
            Kullanici kullanici = kullaniciRepository.findById(randevuDto.getKullanici().getKullaniciId())
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
            randevu.setKullanici(kullanici);
        }
        if (randevuDto.getKuafor() != null) {
            Kuafor kuafor = kuaforRepository.findById(randevuDto.getKuafor().getKuaforId())
                    .orElseThrow(() -> new RuntimeException("Kuaför bulunamadı"));
            randevu.setKuafor(kuafor);
        }
        if (randevuDto.getDurum() != null) {
            randevu.setDurum(randevuDto.getDurum());
        }
        if (randevuDto.getNotlar() != null) {
            randevu.setNotlar(randevuDto.getNotlar());
        }
        if (randevuDto.getSure() != null) {
            randevu.setSure(randevuDto.getSure());
        }

        Randevu updatedRandevu = randevuRepository.save(randevu);

        RandevuDto updatedRandevuDto = new RandevuDto();
        updatedRandevuDto.setRandevuId(updatedRandevu.getRandevuId());
        updatedRandevuDto.setSure(updatedRandevu.getSure());
        updatedRandevuDto.setUpdatedAt(updatedRandevu.getUpdatedAt());
        updatedRandevuDto.setCreatedAt(updatedRandevu.getCreatedAt());
        updatedRandevuDto.setKullanici(updatedRandevu.getKullanici());
        updatedRandevuDto.setKuafor(updatedRandevu.getKuafor());
        updatedRandevuDto.setDurum(updatedRandevu.getDurum());
        updatedRandevuDto.setNotlar(updatedRandevu.getNotlar());
        updatedRandevuDto.setSaat(updatedRandevu.getSaat());
        updatedRandevuDto.setTarih(updatedRandevu.getTarih());
        updatedRandevuDto.setHizmet(updatedRandevu.getHizmet());
        updatedRandevuDto.setUcret(updatedRandevu.getUcret());

        return updatedRandevuDto;
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
