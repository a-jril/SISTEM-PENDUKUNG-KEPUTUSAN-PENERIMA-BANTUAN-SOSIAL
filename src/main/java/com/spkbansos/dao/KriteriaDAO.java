package com.spkbansos.dao;

import com.spkbansos.model.KriteriaSAW;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class KriteriaDAO {

    public List<KriteriaSAW> getAll() {
        List<KriteriaSAW> list = new ArrayList<>();
        String query = "SELECT * FROM kriteria_saw ORDER BY kode";
        
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
             
            while (rs.next()) {
                list.add(new KriteriaSAW(
                    rs.getInt("id"),
                    rs.getString("kode"),
                    rs.getString("nama"),
                    rs.getString("atribut"),
                    rs.getDouble("bobot"),
                    rs.getString("keterangan"),
                    rs.getInt("is_aktif")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<KriteriaSAW> getAktif() {
        List<KriteriaSAW> list = new ArrayList<>();
        String query = "SELECT * FROM kriteria_saw WHERE is_aktif = 1 ORDER BY kode";
        
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
             
            while (rs.next()) {
                list.add(new KriteriaSAW(
                    rs.getInt("id"),
                    rs.getString("kode"),
                    rs.getString("nama"),
                    rs.getString("atribut"),
                    rs.getDouble("bobot"),
                    rs.getString("keterangan"),
                    rs.getInt("is_aktif")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(KriteriaSAW kriteria) {
        String query = "INSERT INTO kriteria_saw (kode, nama, atribut, bobot, keterangan, is_aktif) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, kriteria.getKode());
            pstmt.setString(2, kriteria.getNama());
            pstmt.setString(3, kriteria.getAtribut());
            pstmt.setDouble(4, kriteria.getBobot());
            pstmt.setString(5, kriteria.getKeterangan());
            pstmt.setInt(6, kriteria.getIsAktif());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(KriteriaSAW kriteria) {
        String query = "UPDATE kriteria_saw SET kode = ?, nama = ?, atribut = ?, bobot = ?, keterangan = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, kriteria.getKode());
            pstmt.setString(2, kriteria.getNama());
            pstmt.setString(3, kriteria.getAtribut());
            pstmt.setDouble(4, kriteria.getBobot());
            pstmt.setString(5, kriteria.getKeterangan());
            pstmt.setInt(6, kriteria.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String query = "DELETE FROM kriteria_saw WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean toggleStatus(int id, int newStatus) {
        String query = "UPDATE kriteria_saw SET is_aktif = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, newStatus);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public double getTotalBobotAktif() {
        String query = "SELECT SUM(bobot) FROM kriteria_saw WHERE is_aktif = 1";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
             
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean isKodeExists(String kode, int excludeId) {
        String query = "SELECT COUNT(*) FROM kriteria_saw WHERE kode = ? AND id != ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, kode);
            pstmt.setInt(2, excludeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // For Dashboard
    public int getCountAktif() {
        String query = "SELECT COUNT(*) FROM kriteria_saw WHERE is_aktif = 1";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
             
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
