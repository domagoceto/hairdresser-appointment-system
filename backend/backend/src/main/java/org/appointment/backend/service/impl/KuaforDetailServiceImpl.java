package org.appointment.backend.service.impl;

import org.appointment.backend.entity.Kuafor;
import org.appointment.backend.repo.KuaforRepository;

import org.appointment.backend.service.KuaforDetailService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KuaforDetailServiceImpl implements KuaforDetailService {
    private final KuaforRepository kuaforRepository;

    public KuaforDetailServiceImpl(KuaforRepository kuaforRepository) {
        this.kuaforRepository = kuaforRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Kuafor kuafor = kuaforRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Kuaför bulunamadı: " + email));

        return new User(
                kuafor.getEmail(),
                kuafor.getSifre(),
                List.of(new SimpleGrantedAuthority("ROLE_KUAFOR"))
        );
    }
}

