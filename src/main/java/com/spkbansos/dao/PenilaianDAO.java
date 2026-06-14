package com.spkbansos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PenilaianDAO {
    
    // Get latest active period
    public String getLatestPeriode() {
        String query = "SELECT DISTINCT periode FROM penilaian_evaluasi ORDER BY periode DESC LIMIT 1";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
             
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "-";
    }
    
    // Get all distinct periods
    public List<String> getAllPeriode() {
        List<String> list = new ArrayList<>();
        String query = "SELECT DISTINCT periode FROM penilaian_evaluasi ORDER BY periode DESC";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
             
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get previous period
    public String getPreviousPeriode(String currentPeriode) {
        if (currentPeriode == null || currentPeriode.equals("-")) return null;
        String query = "SELECT DISTINCT periode FROM penilaian_evaluasi WHERE periode < ? ORDER BY periode DESC LIMIT 1";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, currentPeriode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get count of warga already evaluated in a specific period
    public int getCountSudahDinilai(String periode) {
        if (periode == null || periode.equals("-")) return 0;
        
        String query = "SELECT COUNT(DISTINCT id_warga) FROM penilaian_evaluasi WHERE periode = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, periode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Check if a specific warga is evaluated in a specific period
    public boolean isDinilai(int idWarga, String periode) {
        String query = "SELECT COUNT(*) FROM penilaian_evaluasi WHERE id_warga = ? AND periode = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, idWarga);
            pstmt.setString(2, periode);
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
    
    // Get batch status of evaluation for all warga in a specific period
    public Map<Integer, Boolean> getStatusPenilaianWarga(String periode) {
        Map<Integer, Boolean> map = new HashMap<>();
        String query = "SELECT DISTINCT id_warga FROM penilaian_evaluasi WHERE periode = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, periode);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getInt(1), true);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
    
    // Get existing evaluation for a warga in a period
    public Map<Integer, Double> getPenilaianWarga(int idWarga, String periode) {
        Map<Integer, Double> map = new HashMap<>();
        String query = "SELECT id_kriteria, nilai FROM penilaian_evaluasi WHERE id_warga = ? AND periode = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, idWarga);
            pstmt.setString(2, periode);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getInt("id_kriteria"), rs.getDouble("nilai"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
    
    // Save evaluation (Transaction: Insert or Update)
    public boolean savePenilaian(int idWarga, String periode, Map<Integer, Double> nilaiMap) {
        Connection conn = null;
        try {
            conn = DatabaseHelper.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            String updateQuery = "UPDATE penilaian_evaluasi SET nilai = ? WHERE id_warga = ? AND id_kriteria = ? AND periode = ?";
            String insertQuery = "INSERT INTO penilaian_evaluasi (id_warga, id_kriteria, periode, nilai) VALUES (?, ?, ?, ?)";
            
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                 PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                 
                for (Map.Entry<Integer, Double> entry : nilaiMap.entrySet()) {
                    int idKriteria = entry.getKey();
                    double nilai = entry.getValue();
                    
                    // Try update first
                    updateStmt.setDouble(1, nilai);
                    updateStmt.setInt(2, idWarga);
                    updateStmt.setInt(3, idKriteria);
                    updateStmt.setString(4, periode);
                    
                    int updated = updateStmt.executeUpdate();
                    
                    if (updated == 0) {
                        // If no rows updated, do insert
                        insertStmt.setInt(1, idWarga);
                        insertStmt.setInt(2, idKriteria);
                        insertStmt.setString(3, periode);
                        insertStmt.setDouble(4, nilai);
                        insertStmt.executeUpdate();
                    }
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
    
    // Delete evaluation for a warga in a period
    public boolean deletePenilaian(int idWarga, String periode) {
        String query = "DELETE FROM penilaian_evaluasi WHERE id_warga = ? AND periode = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, idWarga);
            pstmt.setString(2, periode);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}