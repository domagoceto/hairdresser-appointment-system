package org.appointment.backend.service;

import org.appointment.backend.dto.RandevuDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RandevuService {

    RandevuDto save(RandevuDto randevuDto);

    void delete(long randevuId);

    List<RandevuDto> getAll();

    Page<RandevuDto> getAll(Pageable pageable);
}
