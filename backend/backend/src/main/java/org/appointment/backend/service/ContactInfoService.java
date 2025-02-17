package org.appointment.backend.service;

import java.util.Map;

public interface ContactInfoService {
    Map<String, String> getContactInfo();
    void updateContactInfo(Map<String, String> newContactInfo);
}
