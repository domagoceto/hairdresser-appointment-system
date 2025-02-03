
Hairdresser Appointment System

![Ekran görüntüsü 2025-01-27 235137](https://github.com/user-attachments/assets/0ec1caf1-d3f5-4d09-b307-303a308c7400)

Bu proje, bir kuaför randevu sistemi olup, kullanıcıların kuaför hizmetlerini kolayca planlamasını, yönetmesini ve rezervasyon yapmasını sağlayan bir web tabanlı uygulamadır.

🛠️ Proje Özellikleri
Kullanıcı Yönetimi: Kullanıcıların sisteme kayıt olmasını ve giriş yapmasını sağlar.
Rol Tabanlı Erişim: Sistem, üç farklı kullanıcı rolüne göre özelleştirilmiştir:
Kullanıcı: Kuaför hizmetlerini görüntüleyebilir ve randevu oluşturabilir.
Kuaför: Verilen randevuları yönetebilir ve kendi hizmetlerini ekleyebilir.
Admin: Sistemin genel yönetiminden sorumludur. Kullanıcıları, kuaförleri ve hizmetleri düzenleyebilir.
Hizmet Yönetimi: Kuaförler, sistemde sunacakları hizmetleri tanımlayabilir (örneğin: saç kesimi, saç boyama).
Randevu Sistemi: Kullanıcılar, kuaför ve hizmet seçerek uygun tarih ve saat için randevu alabilir.
Ödeme Yönetimi: Randevu oluşturma sırasında ödeme bilgileri kaydedilir ve randevu iptal edilirse ödeme kaydı tamamen silinir.
Güvenli Giriş ve Kayıt: Kullanıcı, Kuaför veya Admin rolleri için ayrı giriş ekranları bulunmaktadır.
![Ekran görüntüsü 2025-01-27 235155](https://github.com/user-attachments/assets/fafe4292-9b55-4228-95df-db24ebb79973)
🚀 Kullanıcı Rolleri ve İşlevler

1. Kullanıcı
Sisteme kaydolabilir ve giriş yapabilir.
Kuaförleri ve sunulan hizmetleri görüntüleyebilir.
Kuaför ve hizmet seçerek randevu oluşturabilir.
Kendi randevularını görüntüleyebilir ve gerekirse iptal edebilir.

![Ekran görüntüsü 2025-01-27 235326](https://github.com/user-attachments/assets/a16b3455-fb1e-4dad-853e-502004f7e67f)
3. Kuaför
Kendi hizmetlerini ekleyebilir ve düzenleyebilir.
Randevularını görüntüleyebilir ve yönetebilir.
Randevu durumlarını (örneğin: onaylandı, iptal edildi) güncelleyebilir.
![Ekran görüntüsü 2025-01-27 235344](https://github.com/user-attachments/assets/50160710-9e14-4bbe-b75c-243d24236e20)
4. Admin
Sistemdeki tüm kullanıcıları, kuaförleri ve hizmetleri yönetebilir.
Kuaförlerin ve kullanıcıların hesaplarını düzenleyebilir veya silebilir.
Hizmet ve randevu yönetimi yapabilir.
![Ekran görüntüsü 2025-01-27 235230](https://github.com/user-attachments/assets/5f6b8dad-a00a-4a79-98a6-81c8fe90696e)

📦 Teknolojiler
Frontend: React.js
Backend: Spring Boot
Veritabanı: PostgreSQL
API: RESTful API ile frontend ve backend iletişimi sağlanır.

![Ekran görüntüsü 2025-01-27 235203](https://github.com/user-attachments/assets/934399d7-a354-4d47-a8ab-d07799f9aaa8)

Kullanıcı, kayıt ekranında giriş yapar. Eğer AdminKey veya KuaforKey belirtilmeden kayıt olunursa kullanıcı rolü otomatik olarak Kullanıcı olarak atanır.
Admin ya da Kuaför yetkisine sahip kişiler doğru anahtarlarla kayıt olabilir.
Kullanıcılar sisteme giriş yaptıktan sonra kuaför ve hizmet seçerek randevu oluşturabilir.
Admin ve kuaförler, kendilerine özel panellerden işlemlerini yönetebilir.

Proje güncellenmeye devam edecektir...

@kadircetin.inu@gmail.com
@turelhaticezehra@gmail.com

