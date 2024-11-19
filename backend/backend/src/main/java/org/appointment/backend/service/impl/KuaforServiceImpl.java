package org.appointment.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.KuaforDto;
import org.appointment.backend.entity.Hizmet;
import org.appointment.backend.entity.Kuafor;
import org.appointment.backend.repo.HizmetRepository;
import org.appointment.backend.repo.KuaforRepository;
import org.appointment.backend.service.KuaforService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KuaforServiceImpl implements KuaforService {

    private final KuaforRepository kuaforRepository;
    private final HizmetRepository hizmetRepository;

    @Override
    @Transactional
    public KuaforDto save(KuaforDto kuaforDto) {

        Kuafor kuafor;

        // Eğer DTO'da kuaforId varsa, bu kuaförün zaten var olup olmadığını kontrol et
        if (kuaforDto.getKuaforId() != null) {
            kuafor = kuaforRepository.findById(kuaforDto.getKuaforId())
                    .orElseThrow(() -> new RuntimeException("Kuaför bulunamadı"));
        } else {
            // Eğer yoksa, yeni bir Kuaför oluştur
            kuafor = new Kuafor();
        }

        // Kuaför bilgilerini DTO'dan güncelle
        kuafor.setAd(kuaforDto.getAd());
        kuafor.setSoyad(kuaforDto.getSoyad());
        kuafor.setCinsiyet(kuaforDto.getCinsiyet());
        kuafor.setTelefon(kuaforDto.getTelefon());
        kuafor.setEmail(kuaforDto.getEmail());

        // Eğer DTO'da "yapabilecegiHizmetlerIds" varsa, bu hizmetleri bulup set et
        if (kuaforDto.getYapabilecegiHizmetlerIds() != null) {
            List<Hizmet> hizmetler = hizmetRepository.findAllById(kuaforDto.getYapabilecegiHizmetlerIds());
            kuafor.setYapabilecegiHizmetler(hizmetler);
        }

        // Kuaförü veritabanına kaydet
        Kuafor savedKuafor = kuaforRepository.save(kuafor);

        // Kaydedilen kuaförü DTO'ya dönüştür ve geri döndür
        return convertToDto(savedKuafor);
    }

    private KuaforDto convertKuaforToDto(Kuafor kuafor) {
        KuaforDto kuaforDto = new KuaforDto();
        kuaforDto.setKuaforId(kuafor.getKuaforId());
        kuaforDto.setAd(kuafor.getAd());
        kuaforDto.setSoyad(kuafor.getSoyad());
        kuaforDto.setCinsiyet(kuafor.getCinsiyet());
        kuaforDto.setTelefon(kuafor.getTelefon());
        kuaforDto.setEmail(kuafor.getEmail());

        if (kuafor.getYapabilecegiHizmetler() != null) {
            kuaforDto.setYapabilecegiHizmetlerIds(
                    kuafor.getYapabilecegiHizmetler().stream()
                            .map(Hizmet::getHizmetId)
                            .collect(Collectors.toList())
            );
        }

        return kuaforDto;
    }



    @Transactional
    @Override
    public KuaforDto update(Long kuaforId, KuaforDto kuaforDto) {
        Kuafor kuafor = kuaforRepository.findById(kuaforId)
                .orElseThrow(() -> new RuntimeException("Kuaför bulunamadı"));

        // Sadece gelen kuaforDto'da dolu olan alanları güncelle
        kuafor.setAd(kuaforDto.getAd() != null ? kuaforDto.getAd() : kuafor.getAd());
        kuafor.setSoyad(kuaforDto.getSoyad() != null ? kuaforDto.getSoyad() : kuafor.getSoyad());
        kuafor.setCinsiyet(kuaforDto.getCinsiyet() != null ? kuaforDto.getCinsiyet() : kuafor.getCinsiyet());
        kuafor.setTelefon(kuaforDto.getTelefon() != null ? kuaforDto.getTelefon() : kuafor.getTelefon());
        kuafor.setEmail(kuaforDto.getEmail() != null ? kuaforDto.getEmail() : kuafor.getEmail());

        // Güncellenmiş kuaförü kaydet
        Kuafor updatedKuafor = kuaforRepository.save(kuafor);

        // Güncellenmiş bilgileri geri döndür
        return convertToDto(updatedKuafor);
    }

    @Override
    public void delete(Long id) {
        Kuafor kuafor = kuaforRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kuaför bulunamadı"));

        // Kuaförü sil
        kuaforRepository.delete(kuafor);
    }

    @Override
    public List<KuaforDto> getAll() {
        List<Kuafor> kuaforler = kuaforRepository.findAll();
        List<KuaforDto> kuaforDtos = new ArrayList<>();

        kuaforler.forEach(it -> {
            KuaforDto kuaforDto = new KuaforDto();
            kuaforDto.setKuaforId(it.getKuaforId());
            kuaforDto.setAd(it.getAd());
            kuaforDto.setSoyad(it.getSoyad());
            kuaforDto.setCinsiyet(it.getCinsiyet());
            kuaforDto.setTelefon(it.getTelefon());
            kuaforDto.setEmail(it.getEmail());
            kuaforDtos.add(kuaforDto);
        });
        return kuaforDtos;
    }

    @Override
    public Page<KuaforDto> getAll(Pageable pageable) {
        return kuaforRepository.findAll(pageable).map(this::convertToDto);
    }

    private KuaforDto convertToDto(Kuafor kuafor) {
        KuaforDto kuaforDto = new KuaforDto();
        kuaforDto.setKuaforId(kuafor.getKuaforId());
        kuaforDto.setAd(kuafor.getAd());
        kuaforDto.setSoyad(kuafor.getSoyad());
        kuaforDto.setCinsiyet(kuafor.getCinsiyet());
        kuaforDto.setTelefon(kuafor.getTelefon());
        kuaforDto.setEmail(kuafor.getEmail());
        return kuaforDto;
    }

    private Kuafor convertToEntity(KuaforDto kuaforDto) {
        Kuafor kuafor = new Kuafor();
        kuafor.setKuaforId(kuaforDto.getKuaforId());
        kuafor.setAd(kuaforDto.getAd());
        kuafor.setSoyad(kuaforDto.getSoyad());
        kuafor.setCinsiyet(kuaforDto.getCinsiyet());
        kuafor.setTelefon(kuaforDto.getTelefon());
        kuafor.setEmail(kuaforDto.getEmail());
        return kuafor;
    }
}
