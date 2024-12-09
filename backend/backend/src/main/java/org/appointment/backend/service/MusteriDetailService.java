package org.appointment.backend.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface MusteriDetailService extends UserDetailsService {
    // Kullanıcı detaylarını yüklemenin yanı sıra ek işlemler için genişletilebilir
}
