package org.appointment.backend.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.appointment.backend.dto.HizmetDto;
import org.appointment.backend.entity.Hizmet;
import org.appointment.backend.entity.Kuafor;
import org.appointment.backend.entity.Randevu;
import org.appointment.backend.repo.HizmetRepository;
import org.appointment.backend.repo.KuaforRepository;
import org.appointment.backend.repo.RandevuRepository;
import org.appointment.backend.service.HizmetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HizmetServiceImpl implements HizmetService {

    private final HizmetRepository hizmetRepository;
    private final RandevuRepository randevuRepository;
    private final KuaforRepository kuaforRepository;

    @Override
    @Transactional
    public HizmetDto save(HizmetDto hizmetDto) {
        Hizmet hizmet;

        if (hizmetDto.getHizmetId() != null) {
            hizmet = hizmetRepository.findById(hizmetDto.getHizmetId())
                    .orElseThrow(() -> new RuntimeException("Hizmet bulunamadı"));
        } else {
            hizmet = new Hizmet();
        }

        hizmet.setAd(hizmetDto.getAd());
        hizmet.setSure(hizmetDto.getSure());
        hizmet.setAciklama(hizmetDto.getAciklama());
        hizmet.setFiyat(hizmetDto.getFiyat());

        // Eğer randevu ve kuafor ilişkileri de eklenmişse, bu varlıkları güncelle
        if (hizmetDto.getRandevuIds() != null) {
            List<Randevu> randevular = randevuRepository.findAllById(hizmetDto.getRandevuIds());
            hizmet.setRandevular(randevular);
        }

        if (hizmetDto.getKuaforIds() != null) {
            Set<Kuafor> kuaforler = new HashSet<>(kuaforRepository.findAllById(hizmetDto.getKuaforIds()));

            hizmet.setKuaforler(new HashSet<>(kuaforler));

        }

        Hizmet savedHizmet = hizmetRepository.save(hizmet);

        return convertHizmetToDto(savedHizmet);
    }


    private HizmetDto convertHizmetToDto(Hizmet hizmet) {
        HizmetDto hizmetDto = new HizmetDto();
        hizmetDto.setHizmetId(hizmet.getHizmetId());
        hizmetDto.setAd(hizmet.getAd());
        hizmetDto.setSure(hizmet.getSure());
        hizmetDto.setFiyat(hizmet.getFiyat());
        hizmetDto.setAciklama(hizmet.getAciklama());

        // Eğer HizmetDto'ya randevuların ID'lerini eklemek istiyorsanız:
        if (hizmet.getRandevular() != null) {
            List<Long> randevuIds = hizmet.getRandevular().stream()
                    .map(Randevu::getRandevuId)
                    .collect(Collectors.toList());
            hizmetDto.setRandevuIds(randevuIds);
        }

        // Eğer HizmetDto'ya kuaförlerin ID'lerini eklemek istiyorsanız:
        if (hizmet.getKuaforler() != null) {
            List<Long> kuaforIds = hizmet.getKuaforler().stream()
                    .map(Kuafor::getKuaforId)
                    .collect(Collectors.toList());
            hizmetDto.setKuaforIds(kuaforIds);
        }

        return hizmetDto;
    }

    @Transactional
    @Override
    public HizmetDto update(Long hizmetId, HizmetDto hizmetDto) {
        Hizmet hizmet = hizmetRepository.findById(hizmetId)
                .orElseThrow(() -> new RuntimeException("Hizmet bulunamadı."));

                hizmet.setAd(hizmetDto.getAd() != null ? hizmetDto.getAd() : hizmet.getAd());
                hizmet.setAciklama(hizmetDto.getAciklama() != null ? hizmetDto.getAciklama(): hizmet.getAciklama());
                hizmet.setFiyat(hizmetDto.getFiyat() != null ? hizmetDto.getFiyat() : hizmet.getFiyat());
                hizmet.setSure(hizmetDto.getSure() != null ? hizmetDto.getSure() : hizmet.getSure());

                // Randevuların güncellenmesi
                if (hizmetDto.getRandevuIds() != null) {
                List<Randevu> randevular = randevuRepository.findAllById(hizmetDto.getRandevuIds());
                hizmet.setRandevular(randevular);
                }

                // Kuaförlerin güncellenmesi
                if (hizmetDto.getKuaforIds() != null) {
                    List<Kuafor> kuaforlerList = kuaforRepository.findAllById(hizmetDto.getKuaforIds());
                    Set<Kuafor> kuaforlerSet = new HashSet<>(kuaforlerList);
                    hizmet.setKuaforler(kuaforlerSet);
                }

                Hizmet updatedHizmet = hizmetRepository.save(hizmet);

                HizmetDto updatedHizmetDto = new HizmetDto();
                updatedHizmetDto.setHizmetId(updatedHizmet.getHizmetId());
                updatedHizmetDto.setAd(updatedHizmet.getAd());
                updatedHizmetDto.setAciklama(updatedHizmet.getAciklama());
                updatedHizmetDto.setSure(updatedHizmet.getSure());
                updatedHizmetDto.setFiyat(updatedHizmet.getFiyat());

                // Randevu ve Kuaför ID'leri DTO'ya ekleniyor
                List<Long> randevuIds = updatedHizmet.getRandevular() != null
                        ? updatedHizmet.getRandevular().stream().map(Randevu::getRandevuId).collect(Collectors.toList())
                        : null;
                updatedHizmetDto.setRandevuIds(randevuIds);

                List<Long> kuaforIds = updatedHizmet.getKuaforler() != null
                        ? updatedHizmet.getKuaforler().stream().map(Kuafor::getKuaforId).collect(Collectors.toList())
                        : null;
                updatedHizmetDto.setKuaforIds(kuaforIds);


                return updatedHizmetDto;
    }

    @Transactional
    @Override
    public void delete(Long hizmetId) {
        // Hizmet var mı kontrol et, yoksa hata fırlat
        Hizmet hizmet = hizmetRepository.findById(hizmetId)
                .orElseThrow(() -> new RuntimeException("Hizmet bulunamadı."));

        // Randevularda bu hizmeti null yap
        List<Randevu> randevular = randevuRepository.findByHizmet(hizmet);
        for (Randevu randevu : randevular) {
            randevu.setHizmet(null);
        }
        randevuRepository.saveAll(randevular);

        // Kuaförlerde bu hizmeti listeden çıkar
        List<Kuafor> kuaforler = kuaforRepository.findAll();
        for (Kuafor kuafor : kuaforler) {
            if (kuafor.getYapabilecegiHizmetler() != null && kuafor.getYapabilecegiHizmetler().contains(hizmet)) {
                kuafor.getYapabilecegiHizmetler().remove(hizmet);
            }
        }
        kuaforRepository.saveAll(kuaforler);

        // Hizmeti sil
        hizmetRepository.delete(hizmet);
    }

    @Override
    public List<HizmetDto> getAll() {
        return hizmetRepository.findAll().stream().map(this::convertHizmetToDto).collect(Collectors.toList());
    }

    @Override
    public Page<HizmetDto> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public HizmetDto getHizmetById(Long id) {
        return hizmetRepository.findById(id).map(this::convertHizmetToDto).orElseThrow(() -> new RuntimeException("Hizmet not found"));
    }


    private Hizmet convertToEntity(HizmetDto hizmetDto) {
        return Hizmet.builder()
                .hizmetId(hizmetDto.getHizmetId())
                .ad(hizmetDto.getAd())
                .aciklama(hizmetDto.getAciklama())
                .sure(hizmetDto.getSure())
                .fiyat(hizmetDto.getFiyat())
                .build();
    }
}

