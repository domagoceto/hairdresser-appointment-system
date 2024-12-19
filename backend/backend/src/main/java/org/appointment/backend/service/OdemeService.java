package org.appointment.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.appointment.backend.dto.OdemeDto;
import java.util.List;

public interface OdemeService {

    List<OdemeDto> getAll();

    OdemeDto update(Long odemeId, OdemeDto odemeDto);

    OdemeDto save(OdemeDto odemeDto);
}
