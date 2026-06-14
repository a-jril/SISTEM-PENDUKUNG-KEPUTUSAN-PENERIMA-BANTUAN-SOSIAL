# Rencana Pengembangan Sistem Aplikasi SPK Bansos (Revisi v3)

**Tugas Akhir — Sistem Pendukung Keputusan Penerima Bantuan Sosial Menggunakan Metode Simple Additive Weighting (SAW)**

> [!IMPORTANT]
> Dokumen ini disusun sebagai panduan pengembangan sistem untuk **tugas akhir mahasiswa Teknik Informatika semester akhir**. Setiap fase dirancang agar sejalan dengan metodologi pengembangan perangkat lunak (Waterfall) dan memenuhi persyaratan akademis sidang TA.

---

## Ringkasan Fase & Estimasi Waktu

| Fase | Nama Fase | Estimasi Waktu | Deliverable Utama |
|------|-----------|---------------|-------------------|
| 0 | Analisis Kebutuhan & Studi Literatur | 2–3 minggu | Dokumen analisis, studi literatur SAW |
| 1 | Perancangan Sistem (UML & Database) | 2 minggu | Use case, activity diagram, class diagram, ERD |
| 2 | Persiapan Lingkungan Pengembangan | 2–3 hari | Environment siap, project template berjalan |
| 3 | Konstruksi Database & Seed Data | 2–3 hari | Database + data sampel siap |
| 4 | Konstruksi Kerangka Antarmuka (Shell) | 3–4 hari | Navigasi berfungsi, halaman kosong |
| 5 | Pengembangan Halaman & Fitur CRUD | 3–4 minggu | Semua halaman fungsional |
| 6 | Implementasi Algoritma SAW & Integrasi | 1–2 minggu | Perhitungan SAW + halaman hasil |
| 7 | Halaman Laporan & Cetak PDF | 1–2 minggu | 4 template laporan siap cetak |
| 8 | Pengujian & Validasi | 1–2 minggu | Dokumen black-box, validasi SAW, UAT |
| 9 | Packaging, Deployment & Dokumentasi | 3–5 hari | Executable + buku panduan |

**Total estimasi: ±10–14 minggu (2,5–3,5 bulan)**

---

## Fase 0: Analisis Kebutuhan & Studi Literatur

> **Mengapa fase ini ada?** Tugas akhir Teknik Informatika **wajib** memiliki landasan teori dan analisis kebutuhan yang jelas. Dosen penguji akan menanyakan dasar pemilihan metode, identifikasi masalah, dan solusi yang ditawarkan. Fase ini menghasilkan Bab 1–3 dari laporan TA.

### 0.1 Identifikasi Masalah

Lakukan wawancara atau observasi di lokasi studi kasus (Balai Desa Sukarame) untuk menjawab:

- [ ] Bagaimana proses seleksi penerima bansos saat ini? (manual? musyawarah? berkas fisik?)
- [ ] Apa saja masalah yang terjadi? (subjektivitas? data tidak akurat? lambat?)
- [ ] Kriteria apa saja yang saat ini digunakan untuk menentukan penerima?
- [ ] Berapa jumlah data warga yang harus dikelola?
- [ ] Siapa saja pengguna sistem nantinya? (operator desa? kepala desa? RT/RW?)

**Deliverable:**
- Deskripsi masalah (untuk Bab 1 — Latar Belakang)
- Rumusan masalah
- Batasan masalah
- Tujuan dan manfaat penelitian

### 0.2 Studi Literatur

Kumpulkan dan pahami teori-teori yang menjadi landasan sistem:

| Topik | Isi yang Harus Dipahami |
|-------|------------------------|
| **Sistem Pendukung Keputusan (SPK)** | Definisi, komponen SPK (data management, model management, UI), jenis-jenis SPK |
| **Metode SAW** | Rumus normalisasi (benefit & cost), rumus nilai preferensi (V), langkah-langkah SAW secara formal, kelebihan & kekurangan |
| **Bantuan Sosial** | Definisi bansos, regulasi terkait (Permensos, PKH, BPNT), kriteria kemiskinan BPS |
| **Java Swing** | Arsitektur MVC di Java Swing, layout manager, event handling |
| **SQLite** | Karakteristik embedded database, kapan cocok digunakan |

**Deliverable:**
- Landasan teori (untuk Bab 2 — Tinjauan Pustaka)
- Minimal 5 referensi jurnal/paper terkait SPK + SAW
- Contoh perhitungan SAW manual (3 alternatif × 4 kriteria) yang ditulis tangan di laporan

### 0.3 Analisis Kebutuhan Sistem

#### Kebutuhan Fungsional

| ID | Kebutuhan | Prioritas |
|----|-----------|-----------|
| F-01 | Sistem harus menyediakan mekanisme login dengan username dan password | Wajib |
| F-02 | Sistem harus dapat menampilkan dashboard ringkasan data | Wajib |
| F-03 | Sistem harus dapat melakukan CRUD data warga | Wajib |
| F-04 | Sistem harus dapat mengimpor data warga dari file Excel (.xlsx) | Wajib |
| F-05 | Sistem harus dapat melakukan CRUD kriteria SAW beserta bobot | Wajib |
| F-06 | Sistem harus memvalidasi total bobot kriteria = 100% | Wajib |
| F-07 | Sistem harus dapat menginput penilaian warga per kriteria per periode | Wajib |
| F-08 | Sistem harus dapat menghitung perangkingan dengan metode SAW | Wajib |
| F-09 | Sistem harus menampilkan hasil perangkingan dan detail perhitungan | Wajib |
| F-10 | Sistem harus dapat mencetak laporan ke format PDF | Wajib |
| F-11 | Sistem harus dapat mengekspor data warga ke Excel | Opsional |
| F-12 | Sistem harus dapat mengaktifkan/menonaktifkan kriteria tanpa menghapus | Opsional |

#### Kebutuhan Non-Fungsional

| ID | Kebutuhan | Keterangan |
|----|-----------|------------|
| NF-01 | Password disimpan dalam bentuk hash, bukan plain text | Keamanan |
| NF-02 | Sistem berjalan tanpa koneksi internet (offline/standalone) | Portabilitas |
| NF-03 | Waktu respons halaman < 2 detik untuk data ≤ 1000 warga | Performa |
| NF-04 | Antarmuka menggunakan bahasa Indonesia yang mudah dipahami | Usability |
| NF-05 | Sistem dapat dijalankan di Windows 10/11 tanpa instalasi server | Kompatibilitas |

**Deliverable:**
- Tabel kebutuhan fungsional & non-fungsional (untuk Bab 3)

### 0.4 Checklist Akhir Fase 0

- [ ] Latar belakang masalah sudah ditulis
- [ ] Rumusan masalah, batasan, dan tujuan sudah ditetapkan
- [ ] Studi literatur SPK dan SAW selesai
- [ ] Contoh perhitungan SAW manual selesai
- [ ] Analisis kebutuhan fungsional & non-fungsional selesai
- [ ] Dosen pembimbing sudah menyetujui ruang lingkup

---

## Fase 1: Perancangan Sistem (UML & Database)

> **Mengapa fase ini ada?** Perancangan adalah **syarat mutlak** tugas akhir Teknik Informatika. Dosen penguji akan menilai apakah mahasiswa mampu memodelkan sistem sebelum implementasi. Tanpa fase ini, TA tidak akan lolos sidang.

### 1.1 Perancangan UML

Buat diagram-diagram berikut menggunakan tool seperti **StarUML**, **draw.io**, atau **PlantUML**:

#### A. Use Case Diagram

Aktor: **Admin/Operator Desa** (satu aktor saja — sistem single-user).

Use case yang harus ada:

```
Aktor: Admin
├── Login
├── Melihat Dashboard
├── Mengelola Data Warga
│   ├── Tambah warga manual
│   ├── Edit data warga
│   ├── Hapus data warga
│   ├── Cari data warga
│   └── Import data warga dari Excel
├── Mengelola Kriteria SAW
│   ├── Tambah kriteria
│   ├── Edit kriteria
│   ├── Hapus kriteria
│   └── Aktifkan/Nonaktifkan kriteria
├── Menginput Penilaian
│   ├── Memilih periode penilaian
│   ├── Menginput nilai per warga
│   └── Menghapus penilaian warga
├── Melihat Hasil Keputusan SAW
│   ├── Menjalankan perhitungan SAW
│   ├── Melihat perangkingan
│   └── Melihat detail perhitungan per warga
├── Mencetak Laporan
│   ├── Cetak laporan data warga
│   ├── Cetak laporan kriteria
│   ├── Cetak laporan matriks penilaian
│   └── Cetak laporan rekomendasi penerima bansos
└── Logout
```

#### B. Activity Diagram

Buat **minimal 4** activity diagram untuk proses utama:

1. **Proses Login** — alur dari input username/password hingga berhasil/gagal masuk
2. **Proses Input Penilaian** — alur dari pilih warga → isi nilai → simpan
3. **Proses Perhitungan SAW** — alur dari klik hitung → ambil data → normalisasi → skor V → perangkingan
4. **Proses Import Data Excel** — alur dari pilih file → validasi → insert → ringkasan

#### C. Class Diagram

Minimal class-class berikut harus ada:

```
Model Layer:
├── User              (id, username, password, namaLengkap, createdAt)
├── Warga             (id, nik, nama, alamat, rtRw, kelurahan, kecamatan, jenisKelamin, tglLahir, noTelp)
├── KriteriaSAW       (id, kode, nama, atribut, bobot, keterangan, isAktif)
├── PenilaianEvaluasi (id, idWarga, idKriteria, nilai, periode)
└── HasilSAW          (idWarga, nama, nik, skorV, nilaiNormalisasiPerKriteria)

Controller / DAO Layer:
├── UserDAO           (login, getUserByUsername)
├── WargaDAO          (getAll, getById, insert, update, delete, search, importFromExcel)
├── KriteriaDAO       (getAll, getAktif, insert, update, delete, toggleStatus, getTotalBobot)
├── PenilaianDAO      (getByWargaPeriode, save, delete, getDistinctPeriode)
└── SAWController     (hitung, ambilData, normalisasi, hitungSkor)

View Layer:
├── LoginFrame
├── MainFrame
├── DashboardPanel
├── DataWargaPanel
├── KriteriaPanel
├── PenilaianPanel
├── HasilKeputusanPanel
└── LaporanPanel

Utility:
├── DatabaseHelper    (getConnection, createTables, closeConnection)
└── PasswordUtil      (hash, verify)
```

> [!TIP]
> Class diagram ini menunjukkan arsitektur **DAO Pattern (Data Access Object)** — setiap tabel punya kelas DAO tersendiri yang menangani semua operasi database. Logika bisnis (SAW) dipisah di `SAWController`. UI tidak boleh mengakses database secara langsung.

### 1.2 Perancangan Database (ERD)

Buat **Entity Relationship Diagram** yang menunjukkan relasi antar tabel:

```
[users] ←(tidak berelasi langsung)

[warga] 1 ──────── * [penilaian_evaluasi] * ──────── 1 [kriteria_saw]
         (satu warga punya                    (satu kriteria digunakan
          banyak penilaian)                     di banyak penilaian)
```

**Relasi:**
- `warga` → `penilaian_evaluasi` : **One-to-Many** (1 warga bisa punya banyak record penilaian)
- `kriteria_saw` → `penilaian_evaluasi` : **One-to-Many** (1 kriteria digunakan untuk menilai banyak warga)
- `penilaian_evaluasi` memiliki composite unique constraint: `(id_warga, id_kriteria, periode)`

### 1.3 Perancangan Antarmuka (Wireframe/Mockup)

Buat sketsa kasar (bisa di kertas atau Figma/draw.io) untuk setiap halaman:

- [ ] Wireframe halaman Login
- [ ] Wireframe layout utama (sidebar + area konten)
- [ ] Wireframe halaman Dashboard
- [ ] Wireframe halaman Data Warga (tabel + toolbar)
- [ ] Wireframe dialog tambah/edit warga
- [ ] Wireframe halaman Kriteria SAW
- [ ] Wireframe halaman Input Penilaian
- [ ] Wireframe halaman Hasil Keputusan
- [ ] Wireframe halaman Laporan

> [!NOTE]
> Wireframe tidak harus sempurna — cukup sketsa layout yang menunjukkan posisi komponen. Tujuannya supaya saat coding sudah ada gambaran jelas tanpa perlu memikirkan desain lagi.

### 1.4 Checklist Akhir Fase 1

- [ ] Use case diagram selesai
- [ ] Minimal 4 activity diagram selesai
- [ ] Class diagram selesai
- [ ] ERD selesai
- [ ] Wireframe semua halaman selesai
- [ ] Semua diagram sudah ditinjau dosen pembimbing

---

## Fase 2: Persiapan Lingkungan Pengembangan

> **Mengapa fase ini terpisah?** Agar saat mulai coding, tidak ada waktu terbuang untuk troubleshooting setup. Semua harus beres sebelum satu baris kode ditulis.

### 2.1 Instalasi Perangkat Lunak

| Software | Versi Minimum | Keterangan |
|----------|--------------|------------|
| JDK (Java Development Kit) | JDK 8 atau lebih baru | JDK 8 paling stabil untuk Swing |
| IDE | IntelliJ IDEA Community **atau** Apache NetBeans | NetBeans lebih mudah untuk Swing (ada GUI Builder) |
| SQLite Browser | DB Browser for SQLite | Untuk inspeksi database secara visual |
| Jaspersoft Studio | Versi Community | Untuk mendesain template laporan `.jrxml` |
| Git | Versi terbaru | Version control — **wajib**, supaya bisa rollback jika error |

### 2.2 Library Eksternal (file `.jar`)

| Library | Versi | Fungsi |
|---------|-------|--------|
| **SQLite JDBC** | 3.41+ | Driver koneksi Java ↔ SQLite |
| **FlatLaf** | 3.x | Tema UI modern pengganti tampilan default Java |
| **Apache POI** | 5.x | Membaca dan menulis file Excel `.xlsx` |
| **Apache POI-OOXML** | 5.x | Dependensi tambahan Apache POI untuk format `.xlsx` |
| **JasperReports** | 6.x | Engine generate laporan PDF |
| **jBCrypt** | 0.4 | Library hashing password (lebih aman dari MD5) |

> [!WARNING]
> **Jangan pakai MD5 untuk hash password.** MD5 sudah tidak aman dan akan dipertanyakan penguji. Gunakan **BCrypt** via library jBCrypt — implementasinya sama mudahnya (1 baris untuk hash, 1 baris untuk verify).

### 2.3 Setup Project

1. Buat project baru di IDE
2. Buat struktur folder sebagai berikut:

```
src/
├── main/
│   ├── java/
│   │   └── com/spkbansos/
│   │       ├── Main.java                  ← Entry point
│   │       ├── model/                     ← POJO / data class
│   │       │   ├── User.java
│   │       │   ├── Warga.java
│   │       │   ├── KriteriaSAW.java
│   │       │   ├── PenilaianEvaluasi.java
│   │       │   └── HasilSAW.java
│   │       ├── dao/                       ← Database operations
│   │       │   ├── DatabaseHelper.java
│   │       │   ├── UserDAO.java
│   │       │   ├── WargaDAO.java
│   │       │   ├── KriteriaDAO.java
│   │       │   └── PenilaianDAO.java
│   │       ├── controller/                ← Business logic
│   │       │   └── SAWController.java
│   │       ├── view/                      ← Semua kelas UI
│   │       │   ├── LoginFrame.java
│   │       │   ├── MainFrame.java
│   │       │   ├── DashboardPanel.java
│   │       │   ├── DataWargaPanel.java
│   │       │   ├── KriteriaPanel.java
│   │       │   ├── PenilaianPanel.java
│   │       │   ├── HasilKeputusanPanel.java
│   │       │   └── LaporanPanel.java
│   │       └── util/                      ← Helper classes
│   │           └── PasswordUtil.java
│   └── resources/
│       ├── database/
│       │   └── bansos.db                  ← File database SQLite
│       └── laporan/
│           ├── laporan_warga.jrxml
│           ├── laporan_kriteria.jrxml
│           ├── laporan_matriks.jrxml
│           └── laporan_rekomendasi.jrxml
└── lib/
    ├── sqlite-jdbc-3.x.x.jar
    ├── flatlaf-3.x.jar
    ├── poi-5.x.jar
    ├── poi-ooxml-5.x.jar
    ├── jasperreports-6.x.jar
    └── jbcrypt-0.4.jar
```

3. Tambahkan semua file `.jar` dari folder `lib/` ke classpath project
4. Inisialisasi **Git repository**: `git init` → `git add .` → `git commit -m "Initial project setup"`

### 2.4 Verifikasi Setup

Buat class `Main.java` minimal untuk memastikan semuanya berfungsi:

```java
public class Main {
    public static void main(String[] args) {
        // Test 1: FlatLaf bisa diinisialisasi
        FlatLightLaf.setup();
        System.out.println("✓ FlatLaf OK");

        // Test 2: SQLite bisa konek
        Connection conn = DriverManager.getConnection("jdbc:sqlite:database/bansos.db");
        System.out.println("✓ SQLite OK");
        conn.close();

        // Test 3: Tampilkan JFrame sederhana
        JFrame frame = new JFrame("Test");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        System.out.println("✓ Swing + FlatLaf OK");
    }
}
```

### 2.5 Checklist Akhir Fase 2

- [ ] JDK terinstall, `java -version` berjalan
- [ ] IDE terinstall dan project sudah dibuat
- [ ] Semua `.jar` library sudah ditambahkan ke classpath
- [ ] Struktur folder project sudah dibuat
- [ ] `Main.java` test berhasil dijalankan tanpa error
- [ ] Git repository sudah diinisialisasi
- [ ] DB Browser for SQLite terinstall
- [ ] Jaspersoft Studio terinstall

---

## Fase 3: Konstruksi Database & Seed Data

> **Mengapa database dibangun sebelum UI?** Karena struktur tabel menentukan data apa yang bisa ditampilkan dan disimpan oleh setiap halaman. Kalau langsung bikin UI tanpa database, nanti harus bolak-balik revisi.

### 3.1 Buat Kelas `DatabaseHelper.java`

Kelas ini bertanggung jawab untuk:
1. Menyediakan koneksi database (singleton pattern)
2. Membuat tabel-tabel jika belum ada (`CREATE TABLE IF NOT EXISTS`)
3. Menginsert seed data awal

```java
public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:database/bansos.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initDatabase() {
        // Panggil semua CREATE TABLE IF NOT EXISTS
        // Panggil insertSeedData() jika tabel masih kosong
    }
}
```

### 3.2 Skema Tabel

**Tabel 1: `users`** — Kredensial akun login

```sql
CREATE TABLE IF NOT EXISTS users (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    username        TEXT    NOT NULL UNIQUE,
    password        TEXT    NOT NULL,  -- disimpan dalam bentuk BCrypt hash
    nama_lengkap    TEXT    NOT NULL,
    role            TEXT    NOT NULL DEFAULT 'admin',  -- untuk pengembangan ke depan
    created_at      TEXT    NOT NULL DEFAULT (datetime('now','localtime'))
);
```

**Tabel 2: `warga`** — Data induk demografi warga

```sql
CREATE TABLE IF NOT EXISTS warga (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    nik             TEXT    NOT NULL UNIQUE,
    nama            TEXT    NOT NULL,
    alamat          TEXT,
    rt_rw           TEXT,
    kelurahan       TEXT,
    kecamatan       TEXT,
    jenis_kelamin   TEXT    CHECK(jenis_kelamin IN ('Laki-laki','Perempuan')),
    tgl_lahir       TEXT,
    no_telp         TEXT,
    created_at      TEXT    NOT NULL DEFAULT (datetime('now','localtime')),
    updated_at      TEXT    NOT NULL DEFAULT (datetime('now','localtime'))
);
```

**Tabel 3: `kriteria_saw`** — Daftar kriteria penilaian SAW

```sql
CREATE TABLE IF NOT EXISTS kriteria_saw (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    kode            TEXT    NOT NULL UNIQUE,  -- K1, K2, K3, ...
    nama            TEXT    NOT NULL,
    atribut         TEXT    NOT NULL CHECK(atribut IN ('benefit','cost')),
    bobot           REAL    NOT NULL CHECK(bobot > 0 AND bobot <= 100),
    keterangan      TEXT,
    is_aktif        INTEGER NOT NULL DEFAULT 1 CHECK(is_aktif IN (0,1))
);
```

**Tabel 4: `penilaian_evaluasi`** — Nilai per warga per kriteria (model EAV)

```sql
CREATE TABLE IF NOT EXISTS penilaian_evaluasi (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    id_warga        INTEGER NOT NULL,
    id_kriteria     INTEGER NOT NULL,
    nilai           REAL    NOT NULL CHECK(nilai >= 0),
    periode         TEXT    NOT NULL,  -- format: "2024-01"
    created_at      TEXT    NOT NULL DEFAULT (datetime('now','localtime')),
    FOREIGN KEY (id_warga)    REFERENCES warga(id) ON DELETE CASCADE,
    FOREIGN KEY (id_kriteria) REFERENCES kriteria_saw(id) ON DELETE CASCADE,
    UNIQUE(id_warga, id_kriteria, periode)
);
```

> [!NOTE]
> **Mengapa pakai model EAV (Entity-Attribute-Value)?** Jika jumlah kriteria berubah (ditambah/dikurangi), tidak perlu mengubah struktur tabel. Cukup tambah/hapus baris di `kriteria_saw`, dan form input di UI otomatis menyesuaikan. Ini juga **poin plus saat sidang** karena menunjukkan desain database yang fleksibel.

### 3.3 Aktifkan Foreign Key di SQLite

SQLite **tidak** mengaktifkan foreign key secara default. Tambahkan baris ini setiap kali buka koneksi:

```java
// Di dalam getConnection() atau setelah mendapat Connection
Statement stmt = conn.createStatement();
stmt.execute("PRAGMA foreign_keys = ON");
```

Tanpa ini, `ON DELETE CASCADE` tidak akan berfungsi.

### 3.4 Seed Data Awal

Insert data berikut saat pertama kali aplikasi dijalankan (cek dulu apakah tabel kosong):

**Akun admin default:**

| username | password (plain) | password (BCrypt hash) | nama_lengkap |
|----------|-----------------|----------------------|--------------|
| admin | admin123 | *(hasil BCrypt.hashpw)* | Administrator |

**Kriteria SAW contoh (5 kriteria, total bobot = 100%):**

| Kode | Nama | Atribut | Bobot | Keterangan |
|------|------|---------|-------|------------|
| K1 | Penghasilan per Bulan | cost | 25% | Semakin kecil penghasilan, semakin layak menerima bansos |
| K2 | Jumlah Tanggungan | benefit | 20% | Semakin banyak tanggungan, semakin layak |
| K3 | Kondisi Tempat Tinggal | benefit | 25% | Skor 1-5 (1=bagus, 5=sangat buruk) |
| K4 | Kepemilikan Aset | cost | 15% | Nilai total aset dalam rupiah |
| K5 | Tingkat Pendidikan KK | benefit | 15% | Skor 1-5 (1=S2/S1, 5=tidak sekolah) |

> [!IMPORTANT]
> **Total bobot HARUS = 100%.** Ini adalah prasyarat metode SAW. Jika tidak 100%, hasil perhitungan akan salah. Sistem harus memvalidasi ini di halaman Kriteria.

**Data warga sampel:** Siapkan **minimal 10 warga** dengan data lengkap untuk testing. Gunakan data fiktif tetapi realistis.

### 3.5 Buat Kelas DAO Dasar

Buat kerangka untuk setiap kelas DAO (isi implementasi di Fase 5):

```java
public class WargaDAO {
    public List<Warga> getAll() { /* TODO */ }
    public Warga getById(int id) { /* TODO */ }
    public List<Warga> search(String keyword) { /* TODO */ }
    public boolean insert(Warga warga) { /* TODO */ }
    public boolean update(Warga warga) { /* TODO */ }
    public boolean delete(int id) { /* TODO */ }
    public int importFromExcel(File file) { /* TODO */ }
    public int getCount() { /* TODO */ }
}
```

### 3.6 Verifikasi Database

Buka file `bansos.db` di **DB Browser for SQLite** dan verifikasi:

- [ ] Semua 4 tabel sudah terbuat dengan kolom yang benar
- [ ] Seed data akun admin sudah ter-insert
- [ ] Seed data 5 kriteria sudah ter-insert (total bobot = 100%)
- [ ] Seed data 10 warga sudah ter-insert
- [ ] Foreign key constraint berfungsi (coba delete warga yang punya penilaian — penilaian harus ikut terhapus)

### 3.7 Checklist Akhir Fase 3

- [ ] `DatabaseHelper.java` selesai dan berfungsi
- [ ] Semua tabel terbuat dengan constraint yang benar
- [ ] PRAGMA foreign_keys = ON sudah ditambahkan
- [ ] Seed data admin, kriteria, dan warga sudah masuk
- [ ] Semua kelas Model (POJO) sudah dibuat
- [ ] Kerangka semua kelas DAO sudah dibuat
- [ ] Verifikasi di DB Browser berhasil
- [ ] Commit git: `"feat: database schema and seed data"`

---

## Fase 4: Konstruksi Kerangka Antarmuka Utama (Shell)

> **Mengapa build "cangkang" dulu?** Tujuannya memastikan navigasi antar halaman berfungsi sebelum mengisi konten. Ini mencegah bug routing yang sulit dilacak di kemudian hari.

### 4.1 Inisialisasi Tema FlatLaf

Taruh di method `main()`, **sebelum** membuat JFrame apapun:

```java
public static void main(String[] args) {
    // 1. Setup tema — HARUS paling pertama
    FlatLightLaf.setup();
    // Atau FlatDarkLaf.setup() untuk tema gelap

    // 2. Inisialisasi database
    DatabaseHelper.initDatabase();

    // 3. Tampilkan Login
    SwingUtilities.invokeLater(() -> {
        new LoginFrame().setVisible(true);
    });
}
```

> [!WARNING]
> Urutan `FlatLaf.setup()` **harus sebelum** pembuatan komponen Swing manapun. Jika dibalik, tema tidak akan teralikasi.

### 4.2 Arsitektur Single-Window (MainFrame)

Aplikasi menggunakan **satu JFrame utama** dengan **CardLayout** untuk berpindah halaman:

```
JFrame (MainFrame) — ukuran default: 1200×700, posisi tengah layar
├── Panel Kiri — Sidebar Navigasi
│   │   Layout: BoxLayout Y_AXIS
│   │   Lebar tetap: ~220px
│   │   Warna background: sesuai tema
│   │
│   ├── JLabel lblAppName     → Nama aplikasi + ikon
│   ├── JLabel lblUserInfo    → "Logged in: [nama user]"
│   ├── JSeparator            → garis pemisah
│   ├── JButton btnDashboard  → "📊 Dashboard"
│   ├── JButton btnWarga      → "👥 Data Warga"
│   ├── JButton btnKriteria   → "⚙ Kriteria SAW"
│   ├── JButton btnPenilaian  → "📝 Input Penilaian"
│   ├── JButton btnHasil      → "📈 Hasil Keputusan"
│   ├── JButton btnLaporan    → "🖨 Laporan"
│   ├── (glue / spacer vertikal)
│   └── JButton btnLogout     → "🚪 Logout" (di paling bawah)
│
└── Panel Kanan — Area Konten (CardLayout)
    ├── Card "dashboard"   → DashboardPanel.java
    ├── Card "warga"       → DataWargaPanel.java
    ├── Card "kriteria"    → KriteriaPanel.java
    ├── Card "penilaian"   → PenilaianPanel.java
    ├── Card "hasil"       → HasilKeputusanPanel.java
    └── Card "laporan"     → LaporanPanel.java
```

### 4.3 Navigasi

Setiap tombol sidebar memanggil `CardLayout.show()`:

```java
btnDashboard.addActionListener(e -> {
    CardLayout cl = (CardLayout) contentPanel.getLayout();
    cl.show(contentPanel, "dashboard");
    highlightActiveButton(btnDashboard);  // Visual feedback tombol aktif
});
```

**Fitur visual navigasi:**
- Tombol yang sedang aktif diberi warna/highlight berbeda
- Hover effect pada tombol (FlatLaf sudah support ini otomatis)
- Tidak ada JFrame baru yang dibuka — semua konten di panel kanan

### 4.4 Halaman Login (JFrame Terpisah)

Login **tidak** masuk ke CardLayout — dibuat sebagai `JFrame` atau `JDialog` terpisah.

**Alur:**
1. Aplikasi dimulai → `LoginFrame` tampil
2. User memasukkan username & password → klik Login
3. Jika berhasil → tutup `LoginFrame`, buka `MainFrame` (kirim objek `User` ke konstruktor)
4. Jika gagal → tampilkan pesan error, tetap di `LoginFrame`

### 4.5 Isi Semua Panel dengan Placeholder

Untuk saat ini, isi setiap panel dengan label placeholder:

```java
public class DashboardPanel extends JPanel {
    public DashboardPanel() {
        setLayout(new BorderLayout());
        JLabel lbl = new JLabel("Halaman Dashboard — under construction", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(lbl, BorderLayout.CENTER);
    }
}
```

### 4.6 Verifikasi

Jalankan aplikasi dan pastikan:

- [ ] `LoginFrame` tampil dengan tema FlatLaf (bukan tampilan Java default yang jadul)
- [ ] Semua 6 tombol sidebar bisa diklik
- [ ] Setiap klik sidebar menampilkan panel placeholder yang benar
- [ ] Tombol Logout menutup MainFrame dan membuka kembali LoginFrame
- [ ] Tidak ada error atau exception di console

### 4.7 Checklist Akhir Fase 4

- [ ] `LoginFrame.java` sudah bisa tampil
- [ ] `MainFrame.java` dengan sidebar dan CardLayout berfungsi
- [ ] 6 panel placeholder sudah terdaftar di CardLayout
- [ ] Navigasi sidebar berfungsi penuh
- [ ] Logout kembali ke LoginFrame
- [ ] Commit git: `"feat: application shell with navigation"`

---

## Fase 5: Pengembangan Halaman & Fitur CRUD

> **Urutan pengembangan:** Login → Dashboard → Kriteria → Data Warga → Input Penilaian. Urutan ini mengikuti **dependency data** — kriteria harus ada sebelum bisa menginput penilaian, data warga harus ada sebelum bisa dinilai.

---

### 5.1 Halaman Login (Implementasi Penuh)

#### Komponen UI

| Komponen | ID/Nama | Detail |
|----------|---------|--------|
| `JLabel` | lblJudul | "Sistem Pendukung Keputusan" — font besar, bold |
| `JLabel` | lblSubjudul | "Penerima Bantuan Sosial — Metode SAW" |
| `JLabel` | lblNamaDesa | "Desa Sukarame" |
| `JLabel` | lblUsername | Label "Username" |
| `JTextField` | txUsername | Input username — focus otomatis saat form dibuka |
| `JLabel` | lblPassword | Label "Password" |
| `JPasswordField` | txPassword | Input password — karakter tersembunyi |
| `JCheckBox` | cbShowPassword | "Tampilkan password" — toggle visibility |
| `JButton` | btnLogin | Tombol "Masuk" — warna aksen, ukuran besar |
| `JLabel` | lblError | Pesan error — warna merah, default tersembunyi |

#### Fitur & Logika

| Fitur | Detail Implementasi |
|-------|-------------------|
| **Validasi input** | Field tidak boleh kosong → tampilkan pesan spesifik ("Username tidak boleh kosong") |
| **Login via Enter** | KeyListener di `txPassword`: tekan Enter → trigger aksi login |
| **Query autentikasi** | `SELECT * FROM users WHERE username = ?` → ambil hash → `BCrypt.checkpw(inputPassword, hashDariDB)` |
| **Login berhasil** | Tutup LoginFrame → buka `new MainFrame(userObject)` → user info dikirim ke MainFrame |
| **Login gagal** | `lblError.setText("Username atau password salah")` → `lblError.setVisible(true)` |
| **Anti double-click** | `btnLogin.setEnabled(false)` saat proses query, `setEnabled(true)` saat selesai |
| **Show password** | CheckBox toggle: `txPassword.setEchoChar(cb.isSelected() ? (char)0 : '•')` |

> [!NOTE]
> **Jangan query password dengan WHERE.** Cara yang benar: query berdasarkan username saja, ambil hash dari DB, lalu bandingkan dengan `BCrypt.checkpw()`. Ini karena BCrypt hash berbeda setiap kali untuk password yang sama (ada salt).

---

### 5.2 Halaman Dashboard

#### Komponen UI

| Komponen | Detail |
|----------|--------|
| `JLabel` lblJudul | "Dashboard" — font besar |
| `JLabel` lblSapaan | "Selamat datang, [nama user]!" |
| **Card Statistik 1** | JPanel custom: ikon 👥 + angka besar + label "Total Warga Terdaftar" |
| **Card Statistik 2** | JPanel custom: ikon ⚙ + angka besar + label "Kriteria Aktif" |
| **Card Statistik 3** | JPanel custom: ikon ✅ + angka besar + label "Sudah Dinilai (Periode Ini)" |
| **Card Statistik 4** | JPanel custom: ikon ⬜ + angka besar + label "Belum Dinilai" |
| `JLabel` lblPeriode | "Periode aktif: [periode terakhir]" |
| `JLabel` lblTerakhirHitung | "Perhitungan SAW terakhir: [tanggal]" atau "Belum pernah dihitung" |

#### Fitur & Logika

| Fitur | Detail |
|-------|--------|
| **Refresh otomatis** | Setiap kali panel ditampilkan (navigasi ke Dashboard), query ulang semua statistik |
| **Query statistik** | `SELECT COUNT(*) FROM warga` — total warga |
| | `SELECT COUNT(*) FROM kriteria_saw WHERE is_aktif = 1` — kriteria aktif |
| | `SELECT COUNT(DISTINCT id_warga) FROM penilaian_evaluasi WHERE periode = ?` — sudah dinilai |
| **Belum dinilai** | Hitung: total warga − sudah dinilai |
| **Periode aktif** | Ambil dari `SELECT DISTINCT periode FROM penilaian_evaluasi ORDER BY periode DESC LIMIT 1` |

**Cara buat card statistik:**
```java
private JPanel buatCardStatistik(String ikon, String angka, String label, Color warna) {
    JPanel card = new JPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(warna, 2),
        BorderFactory.createEmptyBorder(15, 20, 15, 20)
    ));
    // ... tambahkan JLabel untuk ikon, angka, dan label
    return card;
}
```

---

### 5.3 Halaman Manajemen Kriteria SAW

> Dibangun **sebelum** halaman Data Warga karena kriteria harus sudah tersedia sebelum proses penilaian bisa dilakukan.

#### Komponen UI — Area Tabel

| Komponen | Detail |
|----------|--------|
| `JLabel` lblJudul | "Manajemen Kriteria SAW" |
| `JLabel` lblTotalBobot | Dinamis: "Total Bobot: 85% / 100%" — **merah** jika belum 100%, **hijau** jika sudah 100% |
| `JTable` tblKriteria | Tabel daftar kriteria |
| `JScrollPane` | Wrapper tabel |
| `JButton` btnTambah | "+ Tambah Kriteria" |
| `JButton` btnEdit | "✏ Edit" — disabled jika tidak ada baris dipilih |
| `JButton` btnHapus | "🗑 Hapus" — disabled jika tidak ada baris dipilih |
| `JButton` btnToggle | "Aktifkan / Nonaktifkan" — disabled jika tidak ada baris dipilih |

**Kolom tabel:**

| No | Kode | Nama Kriteria | Atribut | Bobot (%) | Status |
|----|------|--------------|---------|-----------|--------|

#### Komponen UI — Form Input (JDialog)

| Komponen | Detail |
|----------|--------|
| `JTextField` txKode | Input kode: K1, K2, dst. |
| `JTextField` txNama | Input nama kriteria |
| `JComboBox` cbAtribut | Dropdown: "benefit" / "cost" |
| `JSpinner` spBobot | Input bobot (angka desimal, 0.1 – 100.0, step 0.5) |
| `JTextArea` txKeterangan | Keterangan opsional |
| `JButton` btnSimpan | "Simpan" |
| `JButton` btnBatal | "Batal" |

#### Fitur & Logika — CRUD Lengkap

**CREATE — Tambah Kriteria:**
1. Klik "Tambah" → buka JDialog form kosong
2. Validasi sebelum simpan:
   - Kode tidak boleh kosong dan harus unik → `SELECT COUNT(*) FROM kriteria_saw WHERE kode = ?`
   - Nama tidak boleh kosong
   - Bobot harus > 0
   - **Total bobot aktif + bobot baru ≤ 100%** → `SELECT SUM(bobot) FROM kriteria_saw WHERE is_aktif = 1`
3. Jika valid: `INSERT INTO kriteria_saw (kode, nama, atribut, bobot, keterangan) VALUES (?,?,?,?,?)`
4. Tutup dialog → refresh tabel → update label total bobot
5. Jika tidak valid: tampilkan `JOptionPane` dengan pesan error spesifik

**READ — Tampil Daftar:**
- Saat panel ditampilkan: `SELECT * FROM kriteria_saw ORDER BY kode`
- Load ke `DefaultTableModel`
- Hitung total bobot: `SELECT SUM(bobot) FROM kriteria_saw WHERE is_aktif = 1`
- Warnai label total bobot: merah jika ≠ 100%, hijau jika = 100%

**UPDATE — Edit Kriteria:**
1. Pilih baris di tabel → klik "Edit" → buka JDialog dengan data ter-preload
2. Validasi sama seperti CREATE, **kecuali** pengecekan kode duplikat dikecualikan untuk record yang sedang diedit sendiri
3. `UPDATE kriteria_saw SET kode=?, nama=?, atribut=?, bobot=?, keterangan=? WHERE id=?`
4. Refresh tabel

**DELETE — Hapus Kriteria:**
1. Pilih baris → klik "Hapus"
2. Konfirmasi: `JOptionPane.showConfirmDialog` — "Yakin hapus kriteria [nama]? Semua data penilaian terkait kriteria ini juga akan terhapus."
3. Jika Ya: `DELETE FROM kriteria_saw WHERE id=?` (cascade delete ke penilaian_evaluasi otomatis)
4. Refresh tabel + update total bobot

**TOGGLE STATUS:**
- `UPDATE kriteria_saw SET is_aktif = CASE WHEN is_aktif=1 THEN 0 ELSE 1 END WHERE id=?`
- Refresh tabel + recalculate total bobot (hanya dari kriteria aktif)

> [!TIP]
> Beri warna baris yang status-nya nonaktif dengan warna abu-abu di tabel. Gunakan custom `TableCellRenderer` untuk ini.

---

### 5.4 Halaman Data Warga

#### Komponen UI — Toolbar & Tabel

| Komponen | Detail |
|----------|--------|
| `JLabel` lblJudul | "Data Warga" |
| `JTextField` txCari | Kotak pencarian — placeholder: "Cari berdasarkan NIK atau nama..." |
| `JButton` btnCari | "🔍 Cari" |
| `JButton` btnTambah | "+ Tambah Manual" |
| `JButton` btnEdit | "✏ Edit" — disabled jika tidak ada baris dipilih |
| `JButton` btnHapus | "🗑 Hapus" — disabled jika tidak ada baris dipilih |
| `JButton` btnImport | "📂 Import dari Excel" |
| `JButton` btnExport | "📤 Export ke Excel" |
| `JTable` tblWarga | Tabel data warga |
| `JScrollPane` | Wrapper tabel |
| `JLabel` lblInfo | "Menampilkan X dari Y data" |

**Kolom tabel:**

| No | NIK | Nama | Alamat | RT/RW | Kelurahan | Jenis Kelamin | No. Telp |
|----|-----|------|--------|-------|-----------|---------------|----------|

#### Komponen UI — Form Input (JDialog)

| Komponen | Detail |
|----------|--------|
| `JTextField` txNIK | NIK — 16 digit angka |
| `JTextField` txNama | Nama lengkap |
| `JTextArea` txAlamat | Alamat |
| `JTextField` txRTRW | RT/RW |
| `JTextField` txKelurahan | Kelurahan |
| `JTextField` txKecamatan | Kecamatan |
| `JComboBox` cbJK | "Laki-laki" / "Perempuan" |
| `JTextField` txTglLahir | Tanggal lahir (format: dd-MM-yyyy) |
| `JTextField` txNoTelp | Nomor telepon |
| `JButton` btnSimpan | "Simpan" |
| `JButton` btnBatal | "Batal" |

#### Fitur & Logika — CRUD Lengkap

**CREATE — Tambah Manual:**
1. Klik "Tambah" → buka JDialog form kosong
2. Validasi:
   - NIK wajib diisi, harus tepat 16 digit angka → regex: `^\d{16}$`
   - Nama wajib diisi
   - NIK tidak boleh duplikat → `SELECT COUNT(*) FROM warga WHERE nik = ?`
3. `INSERT INTO warga (nik, nama, alamat, rt_rw, kelurahan, kecamatan, jenis_kelamin, tgl_lahir, no_telp) VALUES (?,?,?,?,?,?,?,?,?)`
4. Tutup dialog → refresh tabel

**READ — Tampil & Cari:**
- Load awal: `SELECT * FROM warga ORDER BY nama`
- Pencarian: `SELECT * FROM warga WHERE nik LIKE '%keyword%' OR nama LIKE '%keyword%' ORDER BY nama`
- Update label info: "Menampilkan X dari Y data"

**UPDATE — Edit:**
1. Pilih baris → klik "Edit" → JDialog dengan data ter-preload
2. Validasi sama seperti CREATE, kecuali NIK duplikat dikecualikan untuk record sendiri
3. `UPDATE warga SET nik=?, nama=?, ... , updated_at=datetime('now','localtime') WHERE id=?`

**DELETE — Hapus:**
1. Konfirmasi: "Yakin hapus data [nama]? Semua data penilaian warga ini juga akan terhapus."
2. `DELETE FROM warga WHERE id=?` (cascade ke penilaian)
3. Refresh tabel

**IMPORT EXCEL:**

Alur lengkap:
1. Klik "Import" → `JFileChooser` terbuka, filter hanya `.xlsx`
2. Dialog konfirmasi: "Import dari file [nama_file.xlsx]?"
3. Proses import dengan Apache POI:

```java
// Pseudocode
FileInputStream fis = new FileInputStream(file);
Workbook workbook = new XSSFWorkbook(fis);
Sheet sheet = workbook.getSheetAt(0);  // Sheet pertama

int berhasil = 0, duplikat = 0, gagal = 0;

// Skip baris pertama (header)
for (int i = 1; i <= sheet.getLastRowNum(); i++) {
    Row row = sheet.getRow(i);
    String nik = getCellStringValue(row.getCell(0));  // Kolom 0 = NIK
    String nama = getCellStringValue(row.getCell(1)); // Kolom 1 = Nama
    // ... dst

    // Validasi
    if (nik kosong atau nama kosong) { gagal++; continue; }
    if (nik sudah ada di DB) { duplikat++; continue; }

    // Insert
    insertWarga(...);
    berhasil++;
}
```

4. Tampilkan ringkasan: "Berhasil: X | Duplikat (dilewati): Y | Gagal: Z"
5. Refresh tabel

**Format Excel yang diterima:**

| Kolom A | Kolom B | Kolom C | Kolom D | Kolom E | Kolom F | Kolom G | Kolom H | Kolom I |
|---------|---------|---------|---------|---------|---------|---------|---------|---------|
| NIK | Nama | Alamat | RT/RW | Kelurahan | Kecamatan | Jenis Kelamin | Tgl Lahir | No. Telp |

> Baris pertama **harus** header. Data dimulai dari baris ke-2.

**EXPORT EXCEL:**
- Klik "Export" → `JFileChooser` mode save → pilih lokasi
- Apache POI membuat `.xlsx` dari data yang sedang ditampilkan di tabel
- Tambahkan header otomatis

---

### 5.5 Halaman Input Penilaian

#### Komponen UI — Panel Kiri (Daftar Warga)

| Komponen | Detail |
|----------|--------|
| `JLabel` lblJudul | "Input Penilaian Warga" |
| `JComboBox` cbPeriode | Dropdown periode: "2024-01", "2024-02", ... |
| `JButton` btnTambahPeriode | "+ Periode Baru" |
| `JTextField` txCariWarga | Pencarian warga |
| `JTable` tblDaftarWarga | Daftar warga + status penilaian |
| `JLabel` lblProgress | "Progress: X / Y warga sudah dinilai" |

**Kolom tabel `tblDaftarWarga`:**

| No | NIK | Nama | Status |
|----|-----|------|--------|
| 1 | 3201... | Ahmad | ✅ Sudah |
| 2 | 3202... | Budi | ⬜ Belum |

#### Komponen UI — Panel Kanan (Form Nilai)

Muncul setelah memilih warga dari tabel kiri.

| Komponen | Detail |
|----------|--------|
| `JLabel` lblWargaDipilih | "Menilai: [Nama] — [NIK]" |
| *(dinamis/loop)* `JLabel` | Label nama kriteria ke-N + keterangan atribut |
| *(dinamis/loop)* `JTextField` atau `JSpinner` | Input nilai untuk kriteria ke-N |
| `JButton` btnSimpan | "💾 Simpan Penilaian" |
| `JButton` btnHapus | "🗑 Hapus Penilaian" — hanya muncul jika warga sudah pernah dinilai |
| `JButton` btnBatal | "Batal" |

> [!IMPORTANT]
> **Form dinamis!** Jumlah field input dibangun secara otomatis dari query `SELECT * FROM kriteria_saw WHERE is_aktif = 1 ORDER BY kode`. Jangan hardcode jumlah field — gunakan loop. Simpan referensi field ke `Map<Integer, JTextField>` dengan key = `id_kriteria`.

#### Fitur & Logika

**Membangun form dinamis:**
```java
private Map<Integer, JTextField> fieldMap = new HashMap<>();

private void buildFormNilai() {
    formPanel.removeAll();
    fieldMap.clear();
    List<KriteriaSAW> kriteriaList = kriteriaDAO.getAktif();

    for (KriteriaSAW k : kriteriaList) {
        JLabel lbl = new JLabel(k.getKode() + " — " + k.getNama()
            + " (" + k.getAtribut() + ")");
        JTextField tf = new JTextField(10);
        fieldMap.put(k.getId(), tf);
        formPanel.add(lbl);
        formPanel.add(tf);
    }
    formPanel.revalidate();
    formPanel.repaint();
}
```

**Pilih warga dari tabel:**
- Klik baris → ambil `id_warga` → query penilaian yang sudah ada:
  `SELECT * FROM penilaian_evaluasi WHERE id_warga=? AND periode=?`
- Jika ada: preload nilai ke form (mode edit)
- Jika tidak ada: form kosong (mode tambah)

**Simpan penilaian:**
1. Validasi: semua field harus terisi dan berupa angka ≥ 0
2. Gunakan **satu transaksi database** untuk semua kriteria:
```java
conn.setAutoCommit(false);
try {
    for (Map.Entry<Integer, JTextField> entry : fieldMap.entrySet()) {
        int idKriteria = entry.getKey();
        double nilai = Double.parseDouble(entry.getValue().getText());

        // UPSERT: coba update dulu, jika 0 rows affected maka insert
        int updated = updateStmt.executeUpdate();
        if (updated == 0) {
            insertStmt.executeUpdate();
        }
    }
    conn.commit();
} catch (Exception e) {
    conn.rollback();
    // Tampilkan error
}
```
3. Setelah simpan: update status di tabel kiri menjadi "✅ Sudah"

**Hapus penilaian:**
- Konfirmasi → `DELETE FROM penilaian_evaluasi WHERE id_warga=? AND periode=?`
- Update status menjadi "⬜ Belum"

**Manajemen periode:**
- Dropdown diisi dari: `SELECT DISTINCT periode FROM penilaian_evaluasi ORDER BY periode DESC`
- Tambah periode baru: `JOptionPane.showInputDialog` → validasi format (YYYY-MM) → tambahkan ke dropdown
- Ganti periode → refresh tabel + status penilaian

---

### 5.6 Checklist Akhir Fase 5

- [ ] Login berfungsi penuh (validasi, hash password, anti double-click)
- [ ] Dashboard menampilkan 4 statistik yang benar dari database
- [ ] CRUD Kriteria lengkap (tambah, lihat, edit, hapus, toggle aktif)
- [ ] Validasi total bobot kriteria ≤ 100% berfungsi
- [ ] CRUD Warga lengkap (tambah, lihat, edit, hapus, cari)
- [ ] Import Excel warga berfungsi (termasuk handle duplikat)
- [ ] Export Excel warga berfungsi
- [ ] Input penilaian dengan form dinamis berfungsi
- [ ] Penilaian bisa disimpan, diedit, dan dihapus
- [ ] Manajemen periode berfungsi
- [ ] Semua fitur di-test secara manual — tidak ada exception
- [ ] Commit git: `"feat: all CRUD pages implemented"`

---

## Fase 6: Implementasi Algoritma SAW & Halaman Hasil Keputusan

> **Mengapa SAW dan halaman hasil digabung dalam satu fase?** Karena keduanya saling bergantung — algoritma membutuhkan halaman untuk menampilkan output, dan halaman membutuhkan algoritma untuk mengisi data. Implementasi bersamaan lebih efisien.

### 6.1 Kelas `SAWController.java`

Buat di package `controller`. **Tidak ada kode UI di sini** — murni logika bisnis.

```java
public class SAWController {

    /**
     * Method utama: jalankan seluruh proses SAW untuk periode tertentu.
     * @return List hasil diurutkan dari skor tertinggi ke terendah
     */
    public List<HasilSAW> hitung(String periode) {
        // 1. Ambil data penilaian dari DB
        Map<Integer, Map<Integer, Double>> matriksKeputusan = ambilData(periode);

        // 2. Ambil daftar kriteria aktif
        List<KriteriaSAW> kriteriaList = getKriteriaAktif();

        // 3. Validasi: pastikan semua warga punya nilai untuk semua kriteria
        validasiKelengkapan(matriksKeputusan, kriteriaList);

        // 4. Normalisasi matriks
        Map<Integer, Map<Integer, Double>> matriksNormalisasi =
            normalisasi(matriksKeputusan, kriteriaList);

        // 5. Hitung skor V (nilai preferensi)
        Map<Integer, Double> skorMap = hitungSkor(matriksNormalisasi, kriteriaList);

        // 6. Gabungkan dengan data warga dan urutkan
        List<HasilSAW> hasilList = buildHasil(skorMap, matriksNormalisasi);
        hasilList.sort(Comparator.comparingDouble(HasilSAW::getSkorV).reversed());

        return hasilList;
    }
}
```

### 6.2 Langkah Perhitungan SAW (Detail)

#### Langkah 1: Ambil Matriks Keputusan

```
Struktur data: Map<idWarga, Map<idKriteria, nilai>>

Query: SELECT pe.id_warga, pe.id_kriteria, pe.nilai
       FROM penilaian_evaluasi pe
       JOIN kriteria_saw ks ON pe.id_kriteria = ks.id
       WHERE pe.periode = ? AND ks.is_aktif = 1
```

Contoh hasil (3 warga × 4 kriteria):

|        | K1 (Penghasilan) | K2 (Tanggungan) | K3 (Rumah) | K4 (Aset) |
|--------|:-:|:-:|:-:|:-:|
| Warga A | 1.500.000 | 4 | 4 | 500.000 |
| Warga B | 3.000.000 | 2 | 3 | 2.000.000 |
| Warga C | 800.000 | 5 | 5 | 300.000 |

#### Langkah 2: Cari Nilai Max dan Min per Kriteria

```java
for (KriteriaSAW kriteria : kriteriaList) {
    int idK = kriteria.getId();
    double max = Double.MIN_VALUE;
    double min = Double.MAX_VALUE;

    for (Map<Integer, Double> nilaiWarga : matriksKeputusan.values()) {
        double val = nilaiWarga.get(idK);
        if (val > max) max = val;
        if (val < min) min = val;
    }
    maxPerKriteria.put(idK, max);
    minPerKriteria.put(idK, min);
}
```

Dari contoh di atas:
- K1: max=3.000.000, min=800.000
- K2: max=5, min=2
- K3: max=5, min=3
- K4: max=2.000.000, min=300.000

#### Langkah 3: Normalisasi

```java
for (setiap warga) {
    for (setiap kriteria) {
        if (kriteria.atribut == "benefit") {
            r_ij = nilai_ij / max_j;    // Semakin besar semakin baik
        } else { // cost
            r_ij = min_j / nilai_ij;    // Semakin kecil semakin baik
        }
    }
}
```

> [!WARNING]
> **Hati-hati pembagian nol!** Jika `max_j = 0` (benefit) atau `nilai_ij = 0` (cost), akan terjadi error. Tambahkan validasi: jika ada nilai 0 di kriteria cost, tampilkan pesan error dan hentikan perhitungan.

Hasil normalisasi (bilangan desimal 0.0 – 1.0):

|        | K1 (cost) | K2 (benefit) | K3 (benefit) | K4 (cost) |
|--------|:-:|:-:|:-:|:-:|
| Warga A | 800.000/1.500.000 = 0.5333 | 4/5 = 0.8000 | 4/5 = 0.8000 | 300.000/500.000 = 0.6000 |
| Warga B | 800.000/3.000.000 = 0.2667 | 2/5 = 0.4000 | 3/5 = 0.6000 | 300.000/2.000.000 = 0.1500 |
| Warga C | 800.000/800.000 = 1.0000 | 5/5 = 1.0000 | 5/5 = 1.0000 | 300.000/300.000 = 1.0000 |

#### Langkah 4: Hitung Skor V (Nilai Preferensi)

```java
// Bobot sudah dalam persen, konversi ke desimal (bagi 100)
for (setiap warga) {
    double skorV = 0;
    for (setiap kriteria) {
        double bobotDesimal = kriteria.bobot / 100.0;
        skorV += bobotDesimal * r_ij;
    }
    hasilMap.put(idWarga, skorV);
}
```

> [!IMPORTANT]
> **Pastikan total bobot desimal = 1.0 (100%).** Jika tidak, hasil SAW akan salah. Sistem harus memvalidasi ini sebelum menjalankan perhitungan — jika total bobot ≠ 100%, tampilkan error dan arahkan user ke halaman Kriteria untuk memperbaiki.

#### Langkah 5: Urutkan dan Ranking

```java
hasilList.sort(Comparator.comparingDouble(HasilSAW::getSkorV).reversed());

// Assign ranking
for (int i = 0; i < hasilList.size(); i++) {
    hasilList.get(i).setPeringkat(i + 1);
}
```

### 6.3 Kelas `HasilSAW.java`

```java
public class HasilSAW {
    private int peringkat;
    private int idWarga;
    private String nik;
    private String nama;
    private double skorV;
    private Map<Integer, Double> nilaiMentahPerKriteria;      // untuk breakdown
    private Map<Integer, Double> nilaiNormalisasiPerKriteria;  // untuk breakdown

    // Constructor, getter, setter
}
```

### 6.4 Halaman Hasil Keputusan (UI)

#### Komponen UI

| Komponen | Detail |
|----------|--------|
| `JLabel` lblJudul | "Hasil Keputusan SAW" |
| `JComboBox` cbPeriode | Pilih periode |
| `JButton` btnHitung | "▶ Hitung SAW" — warna aksen, menonjol |
| `JLabel` lblStatus | "Terakhir dihitung: [tanggal jam]" atau "Belum pernah dihitung" |
| `JLabel` lblValidasi | Peringatan jika total bobot ≠ 100% atau ada warga belum dinilai |
| `JTable` tblHasil | Tabel hasil perangkingan |
| `JScrollPane` | Wrapper tabel |
| `JLabel` lblInfo | "Menampilkan X warga" |
| `JButton` btnDetail | "📋 Lihat Detail" — aktif jika ada baris dipilih |
| `JButton` btnCetak | "🖨 Cetak Laporan Rekomendasi" |

**Kolom tabel `tblHasil`:**

| Peringkat | NIK | Nama | Skor V | Rekomendasi |
|:---------:|-----|------|:------:|:-----------:|
| 1 | 3201... | Ahmad | 0.8234 | ✅ Layak |
| 2 | 3203... | Citra | 0.7891 | ✅ Layak |
| 3 | 3202... | Budi | 0.4512 | ❌ Tidak Layak |

> Kolom "Rekomendasi" menggunakan **threshold**: skor ≥ 0.6 → Layak, skor < 0.6 → Tidak Layak. Nilai threshold bisa disesuaikan.

#### Fitur & Logika

**Tombol Hitung:**
1. Validasi pra-hitung:
   - Total bobot kriteria aktif = 100%? Jika tidak → error, arahkan ke halaman Kriteria
   - Ada warga yang sudah dinilai di periode ini? Jika tidak → error
2. Jalankan `SAWController.hitung(periode)`
3. Tampilkan loading indicator (opsional, hanya jika data sangat besar)
4. Populate `tblHasil` dengan hasil
5. Update label status dengan timestamp

**Tombol Detail (dialog breakdown):**
Double-click baris atau klik tombol "Lihat Detail" → buka JDialog berisi:

| Kriteria | Atribut | Bobot | Nilai Mentah | Nilai Normalisasi | Kontribusi Skor |
|----------|---------|:-----:|:------------:|:-----------------:|:--------------:|
| K1 - Penghasilan | cost | 25% | 1.500.000 | 0.5333 | 0.1333 |
| K2 - Tanggungan | benefit | 20% | 4 | 0.8000 | 0.1600 |
| K3 - Rumah | benefit | 25% | 4 | 0.8000 | 0.2000 |
| K4 - Aset | cost | 15% | 500.000 | 0.6000 | 0.0900 |
| **Total Skor V** | | **100%** | | | **0.5833** |

> [!TIP]
> **Dialog detail ini sangat penting untuk sidang TA.** Penguji pasti akan bertanya "bagaimana cara kerjanya?" — dengan fitur ini, Anda bisa menunjukkan transparansi perhitungan secara visual langsung di aplikasi.

### 6.5 Checklist Akhir Fase 6

- [ ] `SAWController.java` selesai dan berfungsi
- [ ] Validasi pra-hitung (total bobot, kelengkapan data) berfungsi
- [ ] Normalisasi benefit dan cost menghasilkan nilai yang benar
- [ ] Skor V dihitung dengan benar
- [ ] Perangkingan terurut dari skor tertinggi
- [ ] Halaman hasil menampilkan tabel perangkingan
- [ ] Dialog detail breakdown per warga berfungsi
- [ ] Tombol cetak terhubung (implementasi cetak di Fase 7)
- [ ] Commit git: `"feat: SAW algorithm and result page"`

---

## Fase 7: Halaman Laporan & Cetak PDF

> **Mengapa fase ini terpisah dari Fase 6?** Karena pembuatan template laporan Jasper membutuhkan tool terpisah (Jaspersoft Studio) dan skill yang berbeda dari coding Java. Lebih efisien dikerjakan setelah semua data dan perhitungan sudah benar.

### 7.1 Komponen UI Halaman Laporan

| Komponen | Detail |
|----------|--------|
| `JLabel` lblJudul | "Pusat Laporan" |
| `JComboBox` cbPeriode | Pilih periode (untuk laporan yang butuh filter periode) |
| **Card Laporan 1** | JPanel: judul "📄 Buku Induk Data Warga" + deskripsi + tombol Cetak |
| **Card Laporan 2** | JPanel: judul "📄 Daftar Kriteria & Bobot" + deskripsi + tombol Cetak |
| **Card Laporan 3** | JPanel: judul "📄 Matriks Penilaian Mentah" + deskripsi + tombol Cetak — butuh periode |
| **Card Laporan 4** | JPanel: judul "📄 Laporan Rekomendasi Penerima Bansos" + deskripsi + tombol Cetak — butuh periode |

### 7.2 Template Laporan (Dibuat di Jaspersoft Studio)

Buat 4 file template `.jrxml`:

#### A. `laporan_warga.jrxml` — Buku Induk Data Warga
- **Header:** Judul "BUKU INDUK DATA WARGA", nama desa, tanggal cetak
- **Tabel:** No | NIK | Nama | Alamat | RT/RW | Kelurahan | Jenis Kelamin
- **Footer:** Total data, tanda tangan

#### B. `laporan_kriteria.jrxml` — Daftar Kriteria & Bobot
- **Header:** Judul "DAFTAR KRITERIA PENILAIAN SAW", tanggal cetak
- **Tabel:** No | Kode | Nama Kriteria | Atribut | Bobot (%) | Keterangan | Status
- **Footer:** Total bobot, catatan

#### C. `laporan_matriks.jrxml` — Matriks Penilaian
- **Header:** Judul "MATRIKS PENILAIAN WARGA", periode, tanggal cetak
- **Tabel:** No | NIK | Nama | Nilai K1 | Nilai K2 | Nilai K3 | ... (kolom dinamis sesuai jumlah kriteria)
- **Catatan:** Template ini mungkin perlu pendekatan berbeda karena jumlah kolom dinamis. Alternatif: generate tabel secara programatis tanpa Jasper, atau buat template dengan kolom maksimum.

#### D. `laporan_rekomendasi.jrxml` — Laporan Rekomendasi (**Output Utama TA**)
- **Header:** Kop surat desa, judul "LAPORAN REKOMENDASI PENERIMA BANTUAN SOSIAL", periode, metode: SAW
- **Tabel:** Peringkat | NIK | Nama | Skor V | Rekomendasi
- **Footer:** Catatan metodologi, kolom tanda tangan (Operator, Kepala Desa), tanggal cetak

### 7.3 Alur Cetak Laporan (Kode Java)

```java
// Pseudocode untuk cetak laporan
public void cetakLaporan(String namaTemplate, Map<String, Object> parameters, List<?> data) {
    // 1. Load template
    InputStream template = getClass().getResourceAsStream("/laporan/" + namaTemplate + ".jasper");

    // 2. Siapkan data source
    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

    // 3. Isi parameter (judul, periode, tanggal cetak, dll.)
    parameters.put("tanggalCetak", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));

    // 4. Generate report
    JasperPrint jasperPrint = JasperFillManager.fillReport(template, parameters, dataSource);

    // 5. Tampilkan preview
    JasperViewer.viewReport(jasperPrint, false);
    // JasperViewer sudah punya tombol bawaan: Print, Save PDF, Zoom
}
```

### 7.4 Compile Template

Template `.jrxml` (XML) perlu di-compile menjadi `.jasper` (binary):
- Bisa dilakukan di Jaspersoft Studio (klik kanan → Compile)
- Atau secara programatis: `JasperCompileManager.compileReport("template.jrxml")`

> [!TIP]
> Simpan file `.jasper` yang sudah di-compile di folder `resources/laporan/`. Ini lebih cepat dibaca saat runtime dibanding compile dari `.jrxml` setiap kali cetak.

### 7.5 Checklist Akhir Fase 7

- [ ] 4 template `.jrxml` sudah didesain di Jaspersoft Studio
- [ ] Semua template sudah di-compile menjadi `.jasper`
- [ ] Laporan data warga bisa dicetak/preview
- [ ] Laporan kriteria bisa dicetak/preview
- [ ] Laporan matriks penilaian bisa dicetak/preview (dengan filter periode)
- [ ] Laporan rekomendasi bisa dicetak/preview (dengan filter periode)
- [ ] Semua laporan bisa di-export ke PDF dari JasperViewer
- [ ] Layout laporan rapi dan profesional
- [ ] Commit git: `"feat: report generation with JasperReports"`

---

## Fase 8: Pengujian & Validasi

> **Mengapa fase ini penting?** Untuk tugas akhir, pengujian adalah **bab wajib** dalam laporan (biasanya Bab 5). Tanpa dokumentasi pengujian yang baik, laporan TA tidak lengkap.

### 8.1 Pengujian Black-Box (Fungsional)

Buat tabel pengujian untuk **setiap fitur** dengan format:

| No | Skenario Uji | Kondisi/Data Input | Hasil yang Diharapkan | Hasil Aktual | Status |
|----|-------------|-------------------|----------------------|-------------|--------|

#### Modul Login

| No | Skenario | Input | Hasil Diharapkan | Status |
|----|----------|-------|-----------------|--------|
| 1 | Login berhasil | Username & password benar | Masuk ke Dashboard, nama user tampil di sidebar | ☐ |
| 2 | Login gagal — password salah | Username benar, password salah | Pesan "Username atau password salah" tampil | ☐ |
| 3 | Login gagal — username salah | Username tidak terdaftar | Pesan "Username atau password salah" tampil | ☐ |
| 4 | Login field kosong — username | Username kosong | Pesan "Username tidak boleh kosong" | ☐ |
| 5 | Login field kosong — password | Password kosong | Pesan "Password tidak boleh kosong" | ☐ |
| 6 | Login via Enter | Tekan Enter di field password | Sama seperti klik tombol Login | ☐ |

#### Modul Data Warga

| No | Skenario | Input | Hasil Diharapkan | Status |
|----|----------|-------|-----------------|--------|
| 7 | Tambah warga valid | Semua field terisi benar, NIK 16 digit | Data tersimpan, muncul di tabel | ☐ |
| 8 | Tambah warga — NIK kurang digit | NIK hanya 10 digit | Pesan error "NIK harus 16 digit" | ☐ |
| 9 | Tambah warga — NIK duplikat | NIK sudah ada di database | Pesan error "NIK sudah terdaftar" | ☐ |
| 10 | Tambah warga — nama kosong | Nama tidak diisi | Pesan error "Nama tidak boleh kosong" | ☐ |
| 11 | Edit warga | Ubah nama, klik simpan | Data terupdate di tabel | ☐ |
| 12 | Hapus warga tanpa penilaian | Pilih warga tanpa data penilaian | Konfirmasi muncul, data terhapus | ☐ |
| 13 | Hapus warga dengan penilaian | Pilih warga yang sudah punya penilaian | Konfirmasi muncul, data warga + penilaian terhapus | ☐ |
| 14 | Cari warga berdasarkan NIK | Ketik sebagian NIK | Tabel terfilter, hanya warga yang cocok tampil | ☐ |
| 15 | Cari warga berdasarkan nama | Ketik sebagian nama | Tabel terfilter sesuai | ☐ |
| 16 | Import Excel valid | File Excel sesuai format | Data masuk, ringkasan tampil | ☐ |
| 17 | Import Excel — duplikat NIK | Beberapa NIK sudah ada | Duplikat dilewati, baris baru masuk | ☐ |
| 18 | Import Excel — format salah | File bukan `.xlsx` | Pesan error format tidak valid | ☐ |

#### Modul Kriteria SAW

| No | Skenario | Input | Hasil Diharapkan | Status |
|----|----------|-------|-----------------|--------|
| 19 | Tambah kriteria valid | Semua field benar, total bobot masih ≤ 100% | Data tersimpan | ☐ |
| 20 | Tambah kriteria — bobot melebihi | Bobot baru menyebabkan total > 100% | Pesan error, data tidak tersimpan | ☐ |
| 21 | Tambah kriteria — kode duplikat | Kode sudah ada | Pesan error "Kode sudah digunakan" | ☐ |
| 22 | Edit kriteria | Ubah nama dan bobot | Data terupdate | ☐ |
| 23 | Hapus kriteria | Pilih kriteria, klik hapus | Konfirmasi muncul, data terhapus + penilaian terkait | ☐ |
| 24 | Toggle status kriteria | Klik tombol aktifkan/nonaktifkan | Status berubah, total bobot direcalculate | ☐ |

#### Modul Input Penilaian

| No | Skenario | Input | Hasil Diharapkan | Status |
|----|----------|-------|-----------------|--------|
| 25 | Input penilaian baru | Pilih warga, isi semua nilai, simpan | Penilaian tersimpan, status jadi ✅ | ☐ |
| 26 | Edit penilaian | Pilih warga yang sudah dinilai, ubah nilai | Nilai terupdate | ☐ |
| 27 | Input penilaian — field kosong | Ada nilai yang tidak diisi | Pesan error | ☐ |
| 28 | Input penilaian — bukan angka | Isi huruf di field nilai | Pesan error "Nilai harus berupa angka" | ☐ |
| 29 | Hapus penilaian | Klik hapus penilaian | Konfirmasi, penilaian terhapus, status jadi ⬜ | ☐ |
| 30 | Ganti periode | Pilih periode lain di dropdown | Tabel refresh, status penilaian sesuai periode baru | ☐ |

#### Modul Perhitungan SAW & Hasil

| No | Skenario | Input | Hasil Diharapkan | Status |
|----|----------|-------|-----------------|--------|
| 31 | Hitung SAW — data lengkap | Semua warga sudah dinilai, bobot = 100% | Tabel perangkingan tampil | ☐ |
| 32 | Hitung SAW — bobot ≠ 100% | Total bobot kriteria aktif ≠ 100% | Pesan error, arahkan ke halaman Kriteria | ☐ |
| 33 | Hitung SAW — tidak ada penilaian | Belum ada warga yang dinilai di periode ini | Pesan error | ☐ |
| 34 | Lihat detail perhitungan | Double-click baris hasil | Dialog breakdown per kriteria tampil | ☐ |

#### Modul Laporan

| No | Skenario | Input | Hasil Diharapkan | Status |
|----|----------|-------|-----------------|--------|
| 35 | Cetak laporan data warga | Klik cetak | JasperViewer terbuka dengan laporan | ☐ |
| 36 | Cetak laporan kriteria | Klik cetak | JasperViewer terbuka | ☐ |
| 37 | Cetak laporan matriks | Pilih periode, klik cetak | JasperViewer terbuka | ☐ |
| 38 | Cetak laporan rekomendasi | Pilih periode, klik cetak | JasperViewer terbuka | ☐ |
| 39 | Export PDF | Klik save di JasperViewer | File PDF tersimpan | ☐ |

### 8.2 Validasi Perhitungan SAW (**Kritis untuk TA**)

> [!CAUTION]
> Ini adalah bagian **paling krusial** dari pengujian TA. Dosen penguji **pasti** akan menguji apakah perhitungan SAW di aplikasi benar. Jika hasilnya tidak cocok dengan perhitungan manual, TA **tidak lulus**.

**Langkah validasi:**

1. **Siapkan data uji** — 5 warga × 5 kriteria dengan nilai yang sudah ditentukan (gunakan angka bulat agar mudah diverifikasi)

   Contoh data:

   | Alternatif | K1 (cost) | K2 (benefit) | K3 (benefit) | K4 (cost) | K5 (benefit) |
   |------------|:-:|:-:|:-:|:-:|:-:|
   | Warga A | 1.500.000 | 4 | 4 | 500.000 | 4 |
   | Warga B | 3.000.000 | 2 | 3 | 2.000.000 | 2 |
   | Warga C | 800.000 | 5 | 5 | 300.000 | 5 |
   | Warga D | 2.000.000 | 3 | 2 | 1.000.000 | 3 |
   | Warga E | 1.200.000 | 4 | 4 | 800.000 | 4 |

   Bobot: K1=25%, K2=20%, K3=25%, K4=15%, K5=15%

2. **Hitung manual di Microsoft Excel:**
   - Buat tabel matriks keputusan
   - Hitung normalisasi per kolom (benefit: `nilai/max`, cost: `min/nilai`)
   - Hitung skor V: `=SUMPRODUCT(baris_normalisasi, baris_bobot)`
   - Urutkan dari skor tertinggi

3. **Jalankan perhitungan di aplikasi** dengan data yang sama

4. **Bandingkan hasil:**

   | Peringkat | Warga | Skor V (Excel) | Skor V (Aplikasi) | Selisih | Status |
   |:---------:|-------|:--------------:|:-----------------:|:-------:|:------:|
   | 1 | Warga C | 0.xxxx | 0.xxxx | 0.0000 | ✅ Cocok |
   | 2 | Warga E | 0.xxxx | 0.xxxx | 0.0000 | ✅ Cocok |
   | ... | ... | ... | ... | ... | ... |

   **Toleransi perbedaan floating point: < 0.0001**

5. **Dokumentasikan** tabel perbandingan ini di laporan TA (Bab 5 — Pengujian dan Hasil)

### 8.3 User Acceptance Test (UAT) — Opsional tapi Nilai Plus

Jika memungkinkan, minta **petugas desa** atau **pihak terkait** untuk mencoba aplikasi dan mengisi kuesioner kepuasan:

| No | Pertanyaan | STS | TS | N | S | SS |
|----|-----------|:---:|:--:|:-:|:-:|:--:|
| 1 | Aplikasi mudah digunakan | | | | | |
| 2 | Tampilan aplikasi menarik dan rapi | | | | | |
| 3 | Fitur import Excel mempercepat input data | | | | | |
| 4 | Hasil perangkingan sesuai harapan | | | | | |
| 5 | Laporan yang dihasilkan informatif | | | | | |

*(STS=Sangat Tidak Setuju, TS=Tidak Setuju, N=Netral, S=Setuju, SS=Sangat Setuju)*

### 8.4 Checklist Akhir Fase 8

- [ ] Semua 39 skenario black-box diuji dan didokumentasikan
- [ ] Semua skenario menghasilkan status ✅ (pass)
- [ ] Validasi SAW: perhitungan manual vs aplikasi cocok 100%
- [ ] Tabel perbandingan SAW didokumentasikan untuk laporan TA
- [ ] UAT dilakukan (jika memungkinkan)
- [ ] Semua bug yang ditemukan sudah diperbaiki
- [ ] Commit git: `"test: all black-box testing completed"`

---

## Fase 9: Packaging, Deployment & Dokumentasi

### 9.1 Build Executable JAR

1. Di IDE: Build → Build Artifacts → JAR
2. Pastikan semua dependensi `.jar` ter-include (fat JAR) atau disertakan di folder `lib/`
3. Test jalankan: `java -jar bansos-app.jar`

### 9.2 Struktur Folder Distribusi

```
bansos-app/
├── bansos-app.jar              ← Executable utama
├── database/
│   └── bansos.db               ← File database SQLite (dengan seed data)
├── laporan/
│   ├── laporan_warga.jasper
│   ├── laporan_kriteria.jasper
│   ├── laporan_matriks.jasper
│   └── laporan_rekomendasi.jasper
├── lib/                        ← (jika pakai external JAR, bukan fat JAR)
│   ├── sqlite-jdbc-3.x.x.jar
│   ├── flatlaf-3.x.jar
│   └── ...
├── template/
│   └── template_import_warga.xlsx  ← Template Excel untuk import
└── README.txt                  ← Panduan penggunaan
```

### 9.3 Opsi Packaging Tambahan

| Opsi | Keterangan |
|------|------------|
| **Launch4j** | Bungkus JAR menjadi `.exe` agar bisa double-click di Windows tanpa perintah `java -jar` |
| **Bundle JRE** | Sertakan JRE di dalam folder distribusi agar tidak bergantung Java yang terinstall di komputer tujuan |
| **Inno Setup** | Buat installer `.exe` yang profesional (dengan wizard install, shortcut desktop, dll.) |

> [!TIP]
> Untuk tugas akhir, **Launch4j** sudah lebih dari cukup. Installer (Inno Setup) adalah bonus. Yang penting aplikasi bisa dijalankan di komputer yang bersih (tanpa IDE).

### 9.4 Dokumentasi

#### A. `README.txt` — Panduan Pengguna
Isi minimal:
- Nama aplikasi dan versi
- Persyaratan sistem (Windows 10/11, JRE 8+)
- Cara menjalankan (double-click `.exe` atau `java -jar bansos-app.jar`)
- Akun default: username `admin`, password `admin123`
- Cara backup database: copy file `database/bansos.db`
- Kontak developer

#### B. Buku Panduan Pengguna (Opsional — Nilai Plus)
Dokumen terpisah berisi screenshot langkah-langkah penggunaan setiap fitur. Format Word/PDF.

### 9.5 Checklist Sebelum Serah Terima / Sidang

- [ ] Aplikasi berjalan di komputer yang **tidak ada IDE-nya** (hanya JRE)
- [ ] Semua path file (database, template laporan) menggunakan **path relatif**
- [ ] Uji di Windows 10 dan Windows 11 jika memungkinkan
- [ ] Login dengan akun default berhasil
- [ ] Semua menu dan fitur berfungsi tanpa error
- [ ] Import Excel dari file template berfungsi
- [ ] Perhitungan SAW menghasilkan perangkingan yang benar
- [ ] Semua 4 jenis laporan bisa dicetak dan di-export ke PDF
- [ ] File `README.txt` sudah dibuat
- [ ] Template Excel untuk import sudah disertakan
- [ ] Database sudah berisi seed data (admin + kriteria contoh)
- [ ] Source code sudah di-push ke Git repository
- [ ] **Backup seluruh folder distribusi ke flash drive / cloud**

### 9.6 Checklist Akhir Fase 9

- [ ] JAR/EXE bisa dijalankan di komputer bersih
- [ ] Folder distribusi lengkap dan rapi
- [ ] README.txt sudah dibuat
- [ ] Backup project selesai
- [ ] Commit git: `"release: v1.0.0 — ready for deployment"`

---

## Lampiran: Tips untuk Sidang TA

> [!IMPORTANT]
> Tips-tips berikut berdasarkan pola pertanyaan umum dosen penguji Teknik Informatika.

### Pertanyaan yang Sering Ditanyakan Penguji

| Pertanyaan | Cara Menjawab |
|-----------|--------------|
| "Mengapa pakai metode SAW?" | SAW paling sederhana dan transparan untuk DSS. Cocok untuk kasus dengan kriteria numerik yang jelas. Jelaskan kelebihan (mudah dipahami, perhitungan jelas) dan kekurangan (tidak bisa handle ketidakpastian). |
| "Apa bedanya benefit dan cost?" | Benefit = semakin besar nilainya semakin baik (contoh: jumlah tanggungan). Cost = semakin kecil nilainya semakin baik (contoh: penghasilan — untuk bansos, penghasilan rendah lebih layak). |
| "Bagaimana jika kriteria berubah?" | Tunjukkan model EAV di database — kriteria bisa ditambah/dihapus tanpa ubah struktur tabel. Form input otomatis menyesuaikan. |
| "Tunjukkan perhitungannya!" | Buka halaman Hasil Keputusan → klik Hitung → double-click salah satu warga → tunjukkan dialog breakdown. Sebutkan rumus normalisasi dan skor V. |
| "Kenapa pakai SQLite, bukan MySQL?" | Sistem ini standalone/offline untuk satu komputer di balai desa. SQLite tidak butuh server, filenya portable, dan cukup untuk skala data desa (<10.000 record). |
| "Bagaimana keamanan datanya?" | Password di-hash dengan BCrypt, database lokal (tidak online → tidak ada risiko serangan jaringan). Backup bisa dilakukan dengan copy file `.db`. |
| "Apakah bisa multi-user?" | Versi saat ini single-user. Untuk multi-user perlu migrasi ke MySQL/PostgreSQL + arsitektur client-server. Ini bisa jadi saran pengembangan selanjutnya. |

### Saran Pengembangan Selanjutnya (untuk Bab 6 — Penutup)

Cantumkan di laporan TA sebagai "Saran untuk Penelitian Selanjutnya":
1. Implementasi multi-user dengan sistem role (admin, operator, kepala desa)
2. Migrasi ke arsitektur web-based agar bisa diakses dari browser
3. Penambahan metode perbandingan (TOPSIS, WP, AHP) untuk validasi silang
4. Integrasi dengan data kependudukan nasional (SIAK/Dukcapil)
5. Dashboard analitik dengan grafik tren penerima bansos per periode

---

*Dokumen ini terakhir diperbarui: Mei 2026*
*Versi: 3.0 (Revisi untuk Tugas Akhir)*
