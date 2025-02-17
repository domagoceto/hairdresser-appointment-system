package org.appointment.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ContactInfo {
    @Id
    private String key;
    private String value;
}