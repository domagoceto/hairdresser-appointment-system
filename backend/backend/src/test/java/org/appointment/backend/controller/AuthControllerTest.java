package org.appointment.backend.controller;

import org.appointment.backend.dto.KullaniciDto;
import org.appointment.backend.service.KullaniciService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KullaniciService kullaniciService;

    @Test
    public void saveSuccessTest() throws Exception {
        // Mocklama: KullaniciService.save başarılı bir dönüş yapıyor
        when(kullaniciService.save(any(KullaniciDto.class))).thenReturn(new KullaniciDto());

        // Test işlemi
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ad\":\"Test\", \"soyad\":\"User\", \"email\":\"test@example.com\", \"sifre\":\"password\"}"))
                .andExpect(status().isOk()); // Beklenen HTTP durumu
    }

    @Test
    public void saveFailureTest() throws Exception {
        // Mocklama: KullaniciService.save bir RuntimeException fırlatıyor
        when(kullaniciService.save(any(KullaniciDto.class))).thenThrow(new RuntimeException("Kayıt sırasında hata oluştu"));

        // Test işlemi
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ad\":\"Test\", \"soyad\":\"User\", \"email\":\"test@example.com\", \"sifre\":\"password\"}"))
                .andExpect(status().isInternalServerError()); // Beklenen HTTP hata durumu
    }
}
