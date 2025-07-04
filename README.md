# 📘 PBO_APP_KRS

Aplikasi Sistem KRS (Kartu Rencana Studi) berbasis **JavaFX + Maven**, dibuat untuk pembelajaran mata kuliah **Pemrograman Berorientasi Objek (PBO)** di **Fasilkom - UDINUS**.

---

## ✨ Fitur

- 🔐 Login pengguna (Mahasiswa / Dosen)
- 📋 Pengelolaan data Mahasiswa, Dosen, dan Mata Kuliah
- 📅 Jadwal dan pengisian KRS
- 🎨 Antarmuka berbasis JavaFX FXML
- 📦 Struktur modular berbasis MVC

---

## 🛠 Teknologi

- Java 17+
- JavaFX 24.0.1
- Apache Maven
- FXML + SceneBuilder
- Git & GitHub

---

## 🚀 Cara Menjalankan Proyek

### ✅ 1. Jalankan dengan Maven (Disarankan)

#### Persyaratan:
- Java JDK 17+ sudah terinstall
- Maven sudah terinstall dan bisa diakses dari terminal

#### Perintah:
```bash
mvn clean javafx:run
```

📌 Jalankan perintah ini dari folder root proyek (`app_krs`).

---

### ⚙️ 2. Jalankan Manual (Tanpa Maven)

#### Persyaratan:
- Java JDK 17+ sudah terinstall
- JavaFX SDK 24.0.1 sudah didownload dari: https://gluonhq.com/products/javafx/
- Extract JavaFX ke:  
  `C:\Users\HP\OneDrive\Documents\Java_FX\javafx-sdk-24.0.1`

#### Compile:
```bash
javac ^
--module-path "C:\Users\HP\OneDrive\Documents\Java_FX\javafx-sdk-24.0.1\lib" ^
--add-modules javafx.controls,javafx.fxml ^
src\main\java\com\dinus\App.java
```

#### Jalankan:
```bash
java ^
--module-path "C:\Users\HP\OneDrive\Documents\Java_FX\javafx-sdk-24.0.1\lib" ^
--add-modules javafx.controls,javafx.fxml ^
com.dinus.App
```

---

## 📁 Struktur Folder

```
PBO_APP_KRS/
├── src/
│   └── main/
│       ├── java/com/dinus/       ← Java class dan controller
│       └─ resources/com/dinus/  ← FXML dan aset
├── target/                       ← Folder hasil build (di-ignore Git)
├── pom.xml                       ← Konfigurasi Maven
└── README.md
```

---

## 🧾 Dosen Pengampu

**Suprayogi, S.Kom., M.Kom**  
📧 suprayogi@dsn.dinus.ac.id

---

## 📄 Lisensi

Proyek ini dibuat untuk keperluan akademik.  
Penggunaan di luar pembelajaran harus mendapat izin dari dosen pengampu.

---

## 🙌 Kontribusi

Silakan **fork** dan **pull request** jika ingin:
- Menambahkan fitur baru
- Memperbaiki bug
- Menyempurnakan UI atau struktur kode
