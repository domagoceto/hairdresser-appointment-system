
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
2. Kuaför
Kendi hizmetlerini ekleyebilir ve düzenleyebilir.
Randevularını görüntüleyebilir ve yönetebilir.
Randevu durumlarını (örneğin: onaylandı, iptal edildi) güncelleyebilir.
3. Admin
Sistemdeki tüm kullanıcıları, kuaförleri ve hizmetleri yönetebilir.
Kuaförlerin ve kullanıcıların hesaplarını düzenleyebilir veya silebilir.
Hizmet ve randevu yönetimi yapabilir.
📦 Teknolojiler
Frontend: React.js
Backend: Spring Boot
Veritabanı: PostgreSQL
API: RESTful API ile frontend ve backend iletişimi sağlanır.
Kullanıcı, kayıt ekranında giriş yapar. Eğer AdminKey veya KuaforKey belirtilmeden kayıt olunursa kullanıcı rolü otomatik olarak Kullanıcı olarak atanır.
Admin ya da Kuaför yetkisine sahip kişiler doğru anahtarlarla kayıt olabilir.
Kullanıcılar sisteme giriş yaptıktan sonra kuaför ve hizmet seçerek randevu oluşturabilir.
Admin ve kuaförler, kendilerine özel panellerden işlemlerini yönetebilir.

