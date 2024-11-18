package org.appointment.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.appointment.backend.dto.OdemeDto;
import java.util.List;

public interface OdemeService {

    OdemeDto save(OdemeDto odemeDto);

    void delete(Long odemeId);

    List<OdemeDto> getAll();

    Page<OdemeDto> getAll(Pageable pageable);

    OdemeDto update(Long odemeId, OdemeDto odemeDto);
}
