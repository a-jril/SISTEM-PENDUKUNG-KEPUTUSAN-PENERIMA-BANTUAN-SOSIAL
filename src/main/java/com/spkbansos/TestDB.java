package com.spkbansos;

import com.spkbansos.dao.DatabaseHelper;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestDB {
    public static void main(String[] args) {
        System.out.println("--- VERIFIKASI DATABASE ---");
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement()) {
             
            // 1. Cek Tabel
            System.out.println("\n[DAFTAR TABEL]");
            ResultSet rsTables = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%'");
            while (rsTables.next()) {
                System.out.println("- " + rsTables.getString("name"));
            }
            
            // 2. Cek Admin
            System.out.println("\n[DATA USERS]");
            ResultSet rsUsers = stmt.executeQuery("SELECT id, username, nama_lengkap, role FROM users");
            while (rsUsers.next()) {
                System.out.println(String.format("User %d: %s (%s) - %s", 
                    rsUsers.getInt("id"), rsUsers.getString("username"), 
                    rsUsers.getString("nama_lengkap"), rsUsers.getString("role")));
            }
            
            // 3. Cek Kriteria
            System.out.println("\n[DATA KRITERIA SAW]");
            ResultSet rsKriteria = stmt.executeQuery("SELECT kode, nama, atribut, bobot FROM kriteria_saw");
            double totalBobot = 0;
            while (rsKriteria.next()) {
                double bobot = rsKriteria.getDouble("bobot");
                totalBobot += bobot;
                System.out.println(String.format("%s: %s [%s] -> Bobot: %.1f%%", 
                    rsKriteria.getString("kode"), rsKriteria.getString("nama"), 
                    rsKriteria.getString("atribut"), bobot));
            }
            System.out.println("Total Bobot: " + totalBobot + "%");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
