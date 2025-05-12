# Destek Talep ve Takip Sistemi

## Proje Tanıtımı
Kullanıcıların destek talebi oluşturup, ilgili birimlerin takip edeceği bir web uygulamasıdır.

## Kullanılan Teknolojiler

### Backend
- Java
  - Spring
  - Spring Boot
  - JPA/Hibernate
  - H2 Database
  - JJWT
  - JUnit
  - Mockito
  - MapStruct
  - JDK-21

### Frontend
- NodeJS
  - React
  - Ant Design

## Kurulum Adımları

### Git Reposunu İndirme
```bash
git clone https://github.com/RBaykan/Ticket_Web_App.git
```

### Docker ile Çalıştırma
```bash
cd Ticket_Web_App/
docker-compose up
```

### Manuel Çalıştırma

#### Backend Kurulumu
```bash
cd Ticket_Web_App/backend
mvn spring-boot:run
```

#### Frontend Kurulumu
```bash
cd Ticket_Web_App/frontend
npm install
npm run dev
```

## Uygulamaya erişim Bilgileri

- Web Arayüzü: http://localhost:5173
- Backend Sunucusu: http://localhost:8080

### Giriş Bilgileri
**ADMIN Hesabı**
- Kullanıcı Adı: `admin`
- Parola: `ubuntutr123`

## Geliştirici Araçları

### Swagger UI
Swagger UI bağlantısı: http://localhost:8080/swagger-ui/index.html

### Postman Koleksiyonu
Proje dizinindeki `ticket_postman_collection.json` dosyasını Postman uygulamasına import edebilirsiniz.
