package org.appointment.backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.KuaforDto;
import org.appointment.backend.entity.Hizmet;
import org.appointment.backend.entity.Kuafor;
import org.appointment.backend.repo.HizmetRepository;
import org.appointment.backend.repo.KuaforRepository;
import org.appointment.backend.service.KuaforService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.HashSet;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KuaforServiceImpl implements KuaforService {

    private final KuaforRepository kuaforRepository;
    private final HizmetRepository hizmetRepository;
    private final PasswordEncoder passwordEncoder;  // PasswordEncoder'ı enjekte ediyoruz

    @Override
    @Transactional
    public KuaforDto save(KuaforDto kuaforDto) {
        Kuafor kuafor = new Kuafor();

        // Kuaför bilgilerini DTO'dan güncelle
        kuafor.setAd(kuaforDto.getAd());
        kuafor.setSoyad(kuaforDto.getSoyad());
        kuafor.setCinsiyet(kuaforDto.getCinsiyet());
        kuafor.setTelefon(kuaforDto.getTelefon());
        kuafor.setEmail(kuaforDto.getEmail());

        // Şifre hashleme ve ayarlama
        if (kuaforDto.getSifre() != null && !kuaforDto.getSifre().isEmpty()) {
            kuafor.setSifre(passwordEncoder.encode(kuaforDto.getSifre()));  // Şifreyi hash'liyoruz
        }

        // Eğer DTO'da "yapabilecegiHizmetlerIds" varsa, bu hizmetleri bulup set et
        if (kuaforDto.getYapabilecegiHizmetlerIds() != null) {
            Set<Hizmet> yeniHizmetler = new HashSet<>(hizmetRepository.findAllById(kuaforDto.getYapabilecegiHizmetlerIds()));

            // Eski ilişkileri kaldır
            if (kuafor.getYapabilecegiHizmetler() != null) {
                kuafor.getYapabilecegiHizmetler().forEach(hizmet -> hizmet.getKuaforler().remove(kuafor));
            }

            kuafor.setYapabilecegiHizmetler(yeniHizmetler);

            // Yeni hizmetlere kuaförü ekle
            yeniHizmetler.forEach(hizmet -> hizmet.getKuaforler().add(kuafor));
        }

        // Kuaförü veritabanına kaydet
        Kuafor savedKuafor = kuaforRepository.save(kuafor);

        return convertKuaforToDto(savedKuafor);
    }

    private KuaforDto convertKuaforToDto(Kuafor kuafor) {
        KuaforDto kuaforDto = new KuaforDto();
        kuaforDto.setKuaforId(kuafor.getKuaforId());
        kuaforDto.setAd(kuafor.getAd());
        kuaforDto.setSoyad(kuafor.getSoyad());
        kuaforDto.setCinsiyet(kuafor.getCinsiyet());
        kuaforDto.setTelefon(kuafor.getTelefon());
        kuaforDto.setEmail(kuafor.getEmail());

        // Şifreyi DTO'ya eklemiyoruz (güvenlik nedeniyle).

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

        kuafor.setAd(kuaforDto.getAd() != null ? kuaforDto.getAd() : kuafor.getAd());
        kuafor.setSoyad(kuaforDto.getSoyad() != null ? kuaforDto.getSoyad() : kuafor.getSoyad());
        kuafor.setCinsiyet(kuaforDto.getCinsiyet() != null ? kuaforDto.getCinsiyet() : kuafor.getCinsiyet());
        kuafor.setTelefon(kuaforDto.getTelefon() != null ? kuaforDto.getTelefon() : kuafor.getTelefon());
        kuafor.setEmail(kuaforDto.getEmail() != null ? kuaforDto.getEmail() : kuafor.getEmail());

        // Şifre güncelleme
        if (kuaforDto.getSifre() != null && !kuaforDto.getSifre().isEmpty()) {
            // Şifre hashleme işlemi
            String hashedPassword = passwordEncoder.encode(kuaforDto.getSifre());
            kuafor.setSifre(hashedPassword);
        }

        if (kuaforDto.getYapabilecegiHizmetlerIds() != null) {
            Set<Hizmet> yeniHizmetler = new HashSet<>(hizmetRepository.findAllById(kuaforDto.getYapabilecegiHizmetlerIds()));

            // Eski ilişkileri kaldır
            if (kuafor.getYapabilecegiHizmetler() != null) {
                kuafor.getYapabilecegiHizmetler().forEach(hizmet -> hizmet.getKuaforler().remove(kuafor));
            }

            // Yeni hizmetleri kuaföre set et
            kuafor.setYapabilecegiHizmetler(yeniHizmetler);

            // Yeni hizmetlere kuaförü ekle
            yeniHizmetler.forEach(hizmet -> hizmet.getKuaforler().add(kuafor));
        } else {
            // Eğer hizmet bilgisi yoksa, mevcut tüm hizmetleri temizle
            if (kuafor.getYapabilecegiHizmetler() != null) {
                kuafor.getYapabilecegiHizmetler().forEach(hizmet -> hizmet.getKuaforler().remove(kuafor));
                kuafor.getYapabilecegiHizmetler().clear();
            }
        }

        Kuafor updatedKuafor = kuaforRepository.save(kuafor);
        return convertKuaforToDto(updatedKuafor);
    }

    @Transactional
    @Override
    public void delete(Long kuaforId) {
        Kuafor kuafor = kuaforRepository.findById(kuaforId)
                .orElseThrow(() -> new EntityNotFoundException("Kuaför bulunamadı"));

        // Kuaföre bağlı randevuların yönetimi (randevuların kuaför bilgisi null yapılabilir)
        if (kuafor.getRandevular() != null) {
            kuafor.getRandevular().forEach(randevu -> randevu.setKuafor(null));
        }

        // Kuaföre bağlı hizmetlerden kuaförü kaldır
        if (kuafor.getYapabilecegiHizmetler() != null) {
            kuafor.getYapabilecegiHizmetler().forEach(hizmet -> hizmet.getKuaforler().remove(kuafor));
        }

        // Kuaförü sil
        kuaforRepository.delete(kuafor);
    }

    @Override
    public List<KuaforDto> getAll() {
        List<Kuafor> kuaforler = kuaforRepository.findAll();
        List<KuaforDto> kuaforDtos = new ArrayList<>();

        kuaforler.forEach(it -> {
            KuaforDto kuaforDto = convertKuaforToDto(it);
            kuaforDtos.add(kuaforDto);
        });
        return kuaforDtos;
    }

    @Override
    public Page<KuaforDto> getAll(Pageable pageable) {
        return kuaforRepository.findAll(pageable).map(this::convertKuaforToDto);
    }
}


