package com.spkbansos.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;

public class DatabaseHelper {
    private static final String DB_DIR = "database";
    private static final String DB_URL = "jdbc:sqlite:" + DB_DIR + "/bansos.db";

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        // Aktifkan foreign keys di SQLite
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }
        return conn;
    }

    public static void initDatabase() {
        // Pastikan direktori database ada
        File dir = new File(DB_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            // Tabel 1: users
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT NOT NULL UNIQUE, " +
                    "password TEXT NOT NULL, " +
                    "nama_lengkap TEXT NOT NULL, " +
                    "role TEXT NOT NULL DEFAULT 'admin', " +
                    "created_at TEXT NOT NULL DEFAULT (datetime('now','localtime'))" +
                    ")");

            // Tabel 2: warga
            stmt.execute("CREATE TABLE IF NOT EXISTS warga (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nik TEXT NOT NULL UNIQUE, " +
                    "nama TEXT NOT NULL, " +
                    "alamat TEXT, " +
                    "rt_rw TEXT, " +
                    "kelurahan TEXT, " +
                    "kecamatan TEXT, " +
                    "jenis_kelamin TEXT CHECK(jenis_kelamin IN ('Laki-laki','Perempuan')), " +
                    "tgl_lahir TEXT, " +
                    "no_telp TEXT, " +
                    "created_at TEXT NOT NULL DEFAULT (datetime('now','localtime')), " +
                    "updated_at TEXT NOT NULL DEFAULT (datetime('now','localtime'))" +
                    ")");

            // Tabel 3: kriteria_saw
            stmt.execute("CREATE TABLE IF NOT EXISTS kriteria_saw (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "kode TEXT NOT NULL UNIQUE, " +
                    "nama TEXT NOT NULL, " +
                    "atribut TEXT NOT NULL CHECK(atribut IN ('benefit','cost')), " +
                    "bobot REAL NOT NULL CHECK(bobot > 0 AND bobot <= 100), " +
                    "keterangan TEXT, " +
                    "is_aktif INTEGER NOT NULL DEFAULT 1 CHECK(is_aktif IN (0,1))" +
                    ")");

            // Tabel 4: penilaian_evaluasi
            stmt.execute("CREATE TABLE IF NOT EXISTS penilaian_evaluasi (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_warga INTEGER NOT NULL, " +
                    "id_kriteria INTEGER NOT NULL, " +
                    "nilai REAL NOT NULL CHECK(nilai >= 0), " +
                    "periode TEXT NOT NULL, " +
                    "created_at TEXT NOT NULL DEFAULT (datetime('now','localtime')), " +
                    "FOREIGN KEY (id_warga) REFERENCES warga(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (id_kriteria) REFERENCES kriteria_saw(id) ON DELETE CASCADE, " +
                    "UNIQUE(id_warga, id_kriteria, periode)" +
                    ")");

            System.out.println("Database dan tabel berhasil diinisialisasi.");

            // Masukkan data default jika database masih kosong
            insertSeedData(conn);

        } catch (SQLException e) {
            System.err.println("Gagal menginisialisasi database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void insertSeedData(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Cek apakah tabel users kosong
            java.sql.ResultSet rsUsers = stmt.executeQuery("SELECT COUNT(*) FROM users");
            if (rsUsers.next() && rsUsers.getInt(1) == 0) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO users (username, password, nama_lengkap, role) VALUES (?, ?, ?, ?)")) {
                    ps.setString(1, "admin");
                    ps.setString(2, com.spkbansos.util.PasswordUtil.hashPassword("admin123"));
                    ps.setString(3, "Administrator");
                    ps.setString(4, "admin");
                    ps.executeUpdate();
                }
                System.out.println("Seed data: Akun admin default berhasil ditambahkan.");
            }
            rsUsers.close();

            // Cek apakah tabel kriteria_saw kosong
            java.sql.ResultSet rsKriteria = stmt.executeQuery("SELECT COUNT(*) FROM kriteria_saw");
            if (rsKriteria.next() && rsKriteria.getInt(1) == 0) {
                stmt.execute("INSERT INTO kriteria_saw (kode, nama, atribut, bobot, keterangan) VALUES " +
                        "('K1', 'Penghasilan per Bulan', 'cost', 25, 'Semakin kecil penghasilan, semakin layak'), " +
                        "('K2', 'Jumlah Tanggungan', 'benefit', 20, 'Semakin banyak tanggungan, semakin layak'), " +
                        "('K3', 'Kondisi Tempat Tinggal', 'benefit', 25, 'Skor 1-5 (1=bagus, 5=sangat buruk)'), " +
                        "('K4', 'Kepemilikan Aset', 'cost', 15, 'Nilai total aset dalam rupiah'), " +
                        "('K5', 'Tingkat Pendidikan KK', 'benefit', 15, 'Skor 1-5 (1=S2/S1, 5=tidak sekolah)')");
                System.out.println("Seed data: 5 kriteria awal berhasil ditambahkan.");
            }
            rsKriteria.close();
        }
    }
}
