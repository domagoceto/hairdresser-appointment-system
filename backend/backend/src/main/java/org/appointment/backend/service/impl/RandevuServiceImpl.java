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

import java.time.LocalDateTime;
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
        Kullanici kullanici = kullaniciRepository.findById(randevuDto.getKullaniciId())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // Hizmeti veritabanından bulup çekme
        Hizmet hizmet = hizmetRepository.findById(randevuDto.getHizmetId())
                .orElseThrow(() -> new RuntimeException("Hizmet bulunamadı"));

        // Kuaförü veritabanından bulup çekme
        Kuafor kuafor = kuaforRepository.findById(randevuDto.getKuaforId())
                .orElseThrow(() -> new RuntimeException("Kuaför bulunamadı"));

        // Randevu nesnesi oluşturma
        Randevu randevu = new Randevu();
        randevu.setTarih(randevuDto.getTarih());
        randevu.setKuafor(kuafor);
        randevu.setHizmet(hizmet);
        randevu.setDurum(randevuDto.getDurum());
        randevu.setNotlar(randevuDto.getNotlar());
        randevu.setSaat(randevuDto.getSaat());
        randevu.setUcret(randevuDto.getUcret());
        randevu.setKullanici(kullanici);
        randevu.setSure(randevuDto.getSure());

        // Randevu kaydetme
        Randevu savedRandevu = randevuRepository.save(randevu);

        return convertRandevuToDto(savedRandevu);
    }

    private RandevuDto convertRandevuToDto(Randevu randevu) {
        RandevuDto randevuDto = new RandevuDto();
        randevuDto.setRandevuId(randevu.getRandevuId());
        randevuDto.setTarih(randevu.getTarih());
        randevuDto.setSaat(randevu.getSaat());
        randevuDto.setDurum(randevu.getDurum());
        randevuDto.setNotlar(randevu.getNotlar());
        randevuDto.setUcret(randevu.getUcret());
        randevuDto.setSure(randevu.getSure());
        randevuDto.setCreatedAt(randevu.getCreatedAt());
        randevuDto.setUpdatedAt(randevu.getUpdatedAt());

        // Kuafor, Hizmet ve Kullanici varlıklarının sadece ID'lerini DTO'ya ekle
        if (randevu.getKuafor() != null) {
            randevuDto.setKuaforId(randevu.getKuafor().getKuaforId());
        }
        if (randevu.getHizmet() != null) {
            randevuDto.setHizmetId(randevu.getHizmet().getHizmetId());
        }
        if (randevu.getKullanici() != null) {
            randevuDto.setKullaniciId(randevu.getKullanici().getKullaniciId());
        }

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

        if (randevuDto.getTarih() != null) { randevuDto.getTarih();
            randevu.setTarih(randevuDto.getTarih());
        }
        if (randevuDto.getHizmetId() != null) {
            Hizmet hizmet = hizmetRepository.findById(randevuDto.getHizmetId())
                    .orElseThrow(() -> new RuntimeException("Hizmet bulunamadı"));
            randevu.setHizmet(hizmet);
        }
        if (randevuDto.getSaat() != null) {
            System.out.println("Saat güncelleniyor: " + randevuDto.getSaat());
            randevu.setSaat(randevuDto.getSaat());
        }
        if (randevuDto.getKullaniciId() != null) {
            Kullanici kullanici = kullaniciRepository.findById(randevuDto.getKullaniciId())
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
            randevu.setKullanici(kullanici);
        }
        if (randevuDto.getKuaforId() != null) {
            Kuafor kuafor = kuaforRepository.findById(randevuDto.getKuaforId())
                    .orElseThrow(() -> new RuntimeException("Kuaför bulunamadı"));
            randevu.setKuafor(kuafor);
        }
        if (randevuDto.getDurum() != null) {
            System.out.println("Durum güncelleniyor: " + randevuDto.getDurum());
            randevu.setDurum(randevuDto.getDurum());
        }
        if (randevuDto.getNotlar() != null) {
            System.out.println("Notlar güncelleniyor: " + randevuDto.getNotlar());
            randevu.setNotlar(randevuDto.getNotlar());
        }
        if (randevuDto.getSure() != null) {
            System.out.println("Süre güncelleniyor: " + randevuDto.getSure());
            randevu.setSure(randevuDto.getSure());
        }
        if (randevuDto.getUcret() != null) {
            System.out.println("Ücret güncelleniyor: " + randevuDto.getUcret());
            randevu.setUcret(randevuDto.getUcret());
        }

        // Manuel olarak updatedAt alanını güncelle
        randevu.setUpdatedAt(LocalDateTime.now());

        Randevu updatedRandevu = randevuRepository.save(randevu);
        return convertRandevuToDto(updatedRandevu);
    }


    @Override
    public List<RandevuDto> getAll(){
        List<Randevu> randevular=randevuRepository.findAll();
        List<RandevuDto> randevuDtos=new ArrayList<>();

        randevular.forEach(it ->{
            RandevuDto randevuDto=new RandevuDto();
            randevuDto.setRandevuId(it.getRandevuId());
            randevuDto.setTarih(it.getTarih());
            randevuDto.setKuaforId(it.getKuafor().getKuaforId());
            randevuDto.setHizmetId(it.getHizmet().getHizmetId());
            randevuDto.setDurum(it.getDurum());
            randevuDto.setNotlar(it.getNotlar());
            randevuDto.setSaat(it.getSaat());
            randevuDto.setUcret(it.getUcret());
            randevuDto.setUpdatedAt(it.getUpdatedAt());
            randevuDto.setCreatedAt(it.getCreatedAt());
            randevuDto.setKullaniciId(it.getKullanici().getKullaniciId());
            randevuDto.setSure(it.getSure());
            randevuDtos.add(randevuDto);
        });
        return randevuDtos;
    }
    @Override
    public Page<RandevuDto> getAll(Pageable pageable){return null;
    }
}
