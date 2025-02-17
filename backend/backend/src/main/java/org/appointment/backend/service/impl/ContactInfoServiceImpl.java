package org.appointment.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.appointment.backend.entity.ContactInfo;
import org.appointment.backend.repo.ContactInfoRepository;
import org.appointment.backend.service.ContactInfoService;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ContactInfoServiceImpl implements ContactInfoService {

    private final ContactInfoRepository contactInfoRepository;

    @Override
    public Map<String, String> getContactInfo() {
        List<ContactInfo> contactInfos = contactInfoRepository.findAll();
        Map<String, String> contactMap = new HashMap<>();
        for (ContactInfo info : contactInfos) {
            contactMap.put(info.getKey(), info.getValue());
        }
        return contactMap;
    }

    @Override
    public void updateContactInfo(Map<String, String> newContactInfo) {
        for (Map.Entry<String, String> entry : newContactInfo.entrySet()) {
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setKey(entry.getKey());
            contactInfo.setValue(entry.getValue());
            contactInfoRepository.save(contactInfo);
        }
    }
}
