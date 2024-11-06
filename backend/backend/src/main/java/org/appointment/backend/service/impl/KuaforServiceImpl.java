package org.appointment.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.KuaforDto;
import org.appointment.backend.entity.Kuafor;
import org.appointment.backend.repo.KuaforRepository;
import org.appointment.backend.service.KuaforService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KuaforServiceImpl implements KuaforService {

    private final KuaforRepository kuaforRepository;

    @Override
    public List<KuaforDto> getAllKuaforler() {
        return kuaforRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public KuaforDto getKuaforById(Long id) {
        return kuaforRepository.findById(id).map(this::convertToDto).orElseThrow(() -> new RuntimeException("Kuafor not found"));
    }

    @Override
    public KuaforDto createKuafor(KuaforDto kuaforDto) {
        Kuafor kuafor = convertToEntity(kuaforDto);
        return convertToDto(kuaforRepository.save(kuafor));
    }

    @Override
    public KuaforDto updateKuafor(Long id, KuaforDto kuaforDto) {
        Kuafor existingKuafor = kuaforRepository.findById(id).orElseThrow(() -> new RuntimeException("Kuafor not found"));
        existingKuafor.setAd(kuaforDto.getAd());
        existingKuafor.setSoyad(kuaforDto.getSoyad());
        existingKuafor.setCinsiyet(kuaforDto.getCinsiyet());
        existingKuafor.setTelefon(kuaforDto.getTelefon());
        existingKuafor.setEmail(kuaforDto.getEmail());
        return convertToDto(kuaforRepository.save(existingKuafor));
    }

    @Override
    public void deleteKuafor(Long id) {
        kuaforRepository.deleteById(id);
    }

    private KuaforDto convertToDto(Kuafor kuafor) {
        return KuaforDto.builder()
                .kuaforId(kuafor.getKuaforId())
                .ad(kuafor.getAd())
                .soyad(kuafor.getSoyad())
                .cinsiyet(kuafor.getCinsiyet())
                .telefon(kuafor.getTelefon())
                .email(kuafor.getEmail())
                .build();
    }

    private Kuafor convertToEntity(KuaforDto kuaforDto) {
        return Kuafor.builder()
                .kuaforId(kuaforDto.getKuaforId())
                .ad(kuaforDto.getAd())
                .soyad(kuaforDto.getSoyad())
                .cinsiyet(kuaforDto.getCinsiyet())
                .telefon(kuaforDto.getTelefon())
                .email(kuaforDto.getEmail())
                .build();
    }
}
