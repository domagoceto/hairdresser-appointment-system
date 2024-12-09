package org.appointment.backend.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active=test") // Test profili etkinleştirildi
@AutoConfigureMockMvc // MockMvc'yi otomatik olarak yapılandır
public class SecurityTestConfigTest {

    @Autowired
    private MockMvc mockMvc;

    // Dinamik olarak test sırasında gerekli özellikleri ayarla
    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry) {
        // H2 Veritabanı yapılandırması
        registry.add("spring.datasource.url", () -> "jdbc:h2:mem:testdb");
        registry.add("spring.datasource.driver-class-name", () -> "org.h2.Driver");
        registry.add("spring.datasource.username", () -> "sa");
        registry.add("spring.datasource.password", () -> "password");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.H2Dialect");

        // Güvenlik yapılandırması
        registry.add("spring.security.enabled", () -> "false"); // Güvenliği devre dışı bırak
    }

    @Test
    public void whenSecurityConfigIsApplied_thenAllRequestsArePermitted() throws Exception {
        mockMvc.perform(get("/any-endpoint")) // Herhangi bir endpoint'e GET isteği gönder
                .andExpect(status().isOk()); // Yanıtın HTTP 200 (OK) olmasını bekle
    }
}
