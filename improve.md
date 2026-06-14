# SPK Bansos — UI/UX Audit & Implementation Plan

## Audit

### Score

| Aspect | Score |
|----------|----------|
| Visual hierarchy | 6/10 |
| Consistency | 8/10 |
| UX flow | 6/10 |
| Information density | 5/10 |
| Empty states | 4/10 |
| Feedback clarity | 6/10 |

---

## Critical issues

### Critical
Dashboard tidak punya chart sama sekali. Progress bar doang itu terlalu sederhana buat sebuah "dashboard" — user nggak dapat gambaran distribusi data secara visual.

**DashboardPanel.java — infoPanel cuma JProgressBar + 1 JLabel periode**

### Critical
Tombol "Tambah Warga" di DataWargaPanel posisinya ada di BAWAH tabel (bottomPanel) — ini anti-pattern. User harus scroll ke bawah dulu buat nemu tombol paling penting.

**DataWargaPanel.java — btnTambah ada di BorderLayout.SOUTH, bukan di toolbar atas**

### Critical
Global search di navbar invisibel by default (`txGlobalSearch.setVisible(false)`) tapi pas panel berganti visibility-nya nggak selalu di-toggle dengan benar — bisa bikin user bingung kenapa search bar kadang ada kadang nggak.

**MainFrame.java — search visibility logic tersebar, rawan desync**

### Critical
PenilaianPanel punya layout split kiri-kanan yang nggak ada indikator mana yang "active state." User pilih warga dari kiri, form muncul di kanan — tapi transisi ini nggak ada visual cue yang jelas.

**PenilaianPanel.java — rightPanel muncul pas selectedWargaId berubah, tanpa animasi/highlight**

---

## Medium issues

### Medium
Login screen ukurannya 420×520 dan non-resizable. Di monitor resolusi tinggi ini kelihatan kotak kecil di tengah layar gelap — terasa dated dan nggak polished.

**LoginFrame.java — setSize(420,520), setResizable(false)**

### Medium
KriteriaPanel punya lblTotalBobot tapi kalau bobotnya belum 100%, user nggak dapat visual warning yang prominent — hanya warna text yang berubah. Ini bisa lolos.

**KriteriaPanel.java — warning hanya teks, tidak ada banner/badge merah yang mencolok**

### Medium
HasilKeputusanPanel — setelah SAW dihitung, nggak ada summary "berapa yang layak vs tidak layak" yang langsung terlihat. User harus scan tabel manual.

**HasilKeputusanPanel.java — hasil langsung ke tabel, tidak ada stat summary di atas**

### Medium
Semua dialog form (WargaFormDialog, KriteriaFormDialog) nggak punya validation state yang live — error baru muncul pas tombol Simpan diklik.

**WargaFormDialog.java, KriteriaFormDialog.java — validation on-submit, bukan on-blur/on-change**

### Medium
LaporanPanel kurang preview — user nggak bisa lihat gambaran laporan sebelum cetak/export. Ini bikin flow-nya terasa buta.

**LaporanPanel.java — langsung generate, tidak ada thumbnail/preview state**

---

## Low / polish issues

### Polish
Empty state tabel (saat data kosong) hanya tabel kosong — tidak ada placeholder icon + teks ajakan. Kelihatan seperti loading gagal, bukan "memang belum ada data."

### Polish
Sidebar tidak menampilkan user avatar atau nama lengkap. Sidebar header hanya "SPK BANSOS" + icon. Informasi siapa yang login tidak terlihat sampai user ke halaman tertentu.

### Polish
Semua halaman punya header judul sendiri (HEADING_LG), tapi navbar juga punya lblNavbarTitle — duplikasi informasi yang memakan vertical space sia-sia.

### Polish
MainFrame.setSize(1200,700) tapi tidak ada handling untuk window yang lebih kecil dari minimumSize — layout bisa "squish" di laptop 13".

---

## Things done well ✦

### Good
AppDesign.java — design token terpusat. Ini keputusan arsitektur yang sangat bagus. Warna, font, spacing semuanya di satu tempat. Konsistensi tinggi di seluruh app.

### Good
Inline row action (edit/delete icon di dalam tabel) via ActionCellRenderer sudah diimplementasikan — ini UX pattern modern yang tepat. Tinggal di-polish hover state-nya.

### Good
SwingWorker dipakai di DashboardPanel untuk background data loading — artinya UI tidak freeze. Ini best practice yang sering dilewatkan developer Swing.

### Good
FlatLaf styling via putClientProperty — pendekatan yang clean dan tidak perlu custom painting. Arc radius, border color, margin semua dikontrol dengan proper.

### Good
Dark theme yang dipilih (GitHub-inspired) sudah kohesif dan konsisten. BG_PRIMARY #0d1117, aksen biru #3884f4 — contrast ratio decent untuk readability.

---

# Implementation Plan

Klik tiap fase untuk lihat detail task-nya. Urutan ini berdasarkan impact/effort ratio.

---

## Phase 1 — Fix critical UX breaks (~1–2 hari)

- Pindah `btnTambah` ke toolbar atas di DataWargaPanel — satukan dengan btnImport & btnExport jadi satu unified action bar di kanan header.  
  **Tag:** DataWargaPanel.java

- Refactor global search visibility: buat `setSearchVisible(boolean)` di MainFrame yang dipanggil konsisten setiap `showPanel()` — jangan toggle manual di sana-sini.  
  **Tag:** MainFrame.java

- PenilaianPanel: tambah highlight row warna biru (`ACCENT`) pada warga yang dipilih, dan animasikan border rightPanel dari transparent ke `BORDER` saat muncul.  
  **Tag:** PenilaianPanel.java

- Hapus duplikasi judul halaman: pilih salah satu — pakai navbar title ATAU in-panel title, jangan dua-duanya. Rekomendasi: pakai navbar title saja, hapus `lblJudul` dari setiap panel agar space lebih lega.  
  **Tag:** Semua panel

---

## Phase 2 — Dashboard overhaul (~2–3 hari)

- Ganti `infoPanel` (progress bar doang) dengan split layout: kiri tetap progress bar penilaian, kanan tambah bar chart distribusi "Layak vs Tidak Layak" per kriteria pakai JFreeChart atau custom paint di JPanel.  
  **Tag:** DashboardPanel.java

- Stat cards (4 cards atas): tambah persentase perubahan dari periode sebelumnya — misal "+5 dari periode lalu" dalam teks kecil di bawah angka besar. Ini kasih konteks ke user.  
  **Tag:** DashboardPanel.java

- Recent Warga tabel: tambah kolom "Status Penilaian" (sudah/belum dinilai) dengan badge berwarna. Saat ini cuma NIK, Nama, Kelurahan — kurang actionable.  
  **Tag:** DashboardPanel.java

- Tambah user info di sidebar — di bawah "SPK BANSOS" header, tambah avatar initials + nama lengkap + role user. Pakai `currentUser.getNamaLengkap()` yang sudah ada.  
  **Tag:** MainFrame.java

---

## Phase 3 — Form UX & feedback improvements (~2 hari)

- WargaFormDialog: tambah inline validation on-blur untuk field NIK (harus 16 digit angka), No Telp (format valid), dan Nama (tidak boleh angka). Tampilkan error label merah di bawah field, bukan dialog popup.  
  **Tag:** WargaFormDialog.java

- KriteriaPanel: tambah banner warning yang prominent (background WARNING, border kiri tebal) kalau total bobot ≠ 100%. Banner ini harus "sticky" di atas tabel dan tidak bisa diabaikan.  
  **Tag:** KriteriaPanel.java

- HasilKeputusanPanel: tambah summary bar di atas tabel setelah SAW dihitung — "X warga layak (XX%) · Y warga tidak layak (XX%)" dengan warna SUCCESS dan DANGER.  
  **Tag:** HasilKeputusanPanel.java

- LaporanPanel: tambah preview panel yang menampilkan metadata laporan (jumlah record, periode, tanggal generate) sebelum user klik cetak/export — supaya ada konfirmasi visual.  
  **Tag:** LaporanPanel.java

---

## Phase 4 — Polish & empty states (~1–2 hari)

- Buat utility method `AppDesign.renderEmptyState(JTable, iconName, message)` yang menampilkan icon besar + pesan di tengah tabel saat data kosong. Terapkan ke semua tabel (DataWarga, Kriteria, Penilaian, Hasil).

- LoginFrame: ubah ke fullscreen/maximizable dengan card login di tengah canvas gelap. Pakai JFrame yang bisa resize, dengan GridBagLayout supaya card tetap center.

- ActionCellRenderer: tambah hover state yang lebih jelas — saat ini ROW_ACTION_HOVER ada di AppDesign tapi implementasi renderer mungkin belum pakai MouseMotionListener di level row. Audit dan pastikan hover row highlight konsisten.

- Tambah column sorting ke semua JTable dengan `TableRowSorter` — user perlu bisa sort by nama, NIK, skor, dll. Ini low-effort, high-value.

---

# Quick Wins

Perubahan kecil, impact langsung terasa. Bisa dikerjain dalam hitungan jam.

### ≈30 min
**TableRowSorter** di semua tabel — 3 baris kode per tabel, tapi user langsung bisa sort by kolom apapun.

`tblWarga.setRowSorter(new TableRowSorter<>(tableModel))`

### ≈1 jam
**User info di sidebar** — tambah initials avatar dan nama di bawah app title. Data sudah ada di `currentUser`.

**MainFrame.java — headerPanel sidebar**

### ≈1 jam
**Summary bar di HasilKeputusan** — setelah `hitungSAW()` selesai, hitung layak/tidak dan render dua JLabel berwarna di atas tabel. Datanya sudah ada.

**HasilKeputusanPanel.java**

### ≈45 min
**Pindahkan btnTambah ke toolbar atas** di DataWargaPanel — cut dari bottomPanel, paste ke searchExportPanel di kiri.

**DataWargaPanel.java**

### ≈2 jam
**Warning banner KriteriaPanel** — tambah komponen di atas tabel yang muncul/hilang berdasarkan totalBobot. Tempel listener ke model.

**KriteriaPanel.java**

### ≈30 min
**Tooltip pada action icons** — tambah `setToolTipText("Edit")` dan `"Hapus"` pada icon button di ActionCellRenderer supaya user tahu fungsinya.

**ActionCellRenderer.java**

### ≈1 jam
**Konfirmasi dialog hapus** — pastikan dialog hapus warga/kriteria menampilkan nama item yang akan dihapus: "Hapus warga *Budi Santoso*?" bukan hanya "Hapus data ini?".

**DataWargaPanel.java, KriteriaPanel.java**