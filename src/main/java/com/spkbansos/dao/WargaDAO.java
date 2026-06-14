package com.spkbansos.dao;

import com.spkbansos.model.Warga;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WargaDAO {
    
    public List<Warga> getAll() {
        return search("");
    }
    
    public Warga getById(int id) {
        String query = "SELECT * FROM warga WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToWarga(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Warga> search(String keyword) {
        List<Warga> list = new ArrayList<>();
        String query = "SELECT * FROM warga WHERE nik LIKE ? OR nama LIKE ? ORDER BY nama";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            String param = "%" + keyword + "%";
            pstmt.setString(1, param);
            pstmt.setString(2, param);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToWarga(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public boolean insert(Warga warga) {
        String query = "INSERT INTO warga (nik, nama, alamat, rt_rw, kelurahan, kecamatan, jenis_kelamin, tgl_lahir, no_telp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            setWargaParams(pstmt, warga);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean update(Warga warga) {
        String query = "UPDATE warga SET nik=?, nama=?, alamat=?, rt_rw=?, kelurahan=?, kecamatan=?, jenis_kelamin=?, tgl_lahir=?, no_telp=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            setWargaParams(pstmt, warga);
            pstmt.setInt(10, warga.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(int id) {
        String query = "DELETE FROM warga WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public int getCount() {
        String query = "SELECT COUNT(*) FROM warga";
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

    public List<Warga> getRecent(int limit) {
        List<Warga> list = new ArrayList<>();
        String query = "SELECT * FROM warga ORDER BY id DESC LIMIT ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToWarga(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean isNikExists(String nik, int excludeId) {
        String query = "SELECT COUNT(*) FROM warga WHERE nik = ? AND id != ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, nik);
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
    
    public int importFromExcel(File file) {
        int count = 0;
        Connection conn = null;
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
             
            conn = DatabaseHelper.getConnection();
            conn.setAutoCommit(false);
            
            String checkSql = "SELECT COUNT(*) FROM warga WHERE nik = ?";
            String insertSql = "INSERT INTO warga (nik, nama, alamat, rt_rw, kelurahan, kecamatan, jenis_kelamin, tgl_lahir, no_telp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                 PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                 
                Sheet sheet = workbook.getSheetAt(0);
                
                // Start from row 1 (assuming row 0 is header)
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;
                    
                    String nik = getCellValueAsString(row.getCell(0));
                    String nama = getCellValueAsString(row.getCell(1));
                    
                    if (nik == null || nik.trim().isEmpty() || nama == null || nama.trim().isEmpty()) {
                        continue; // Skip invalid rows
                    }
                    
                    // Validasi NIK (16 digit angka)
                    if (!nik.matches("\\d{16}")) {
                        continue;
                    }
                    
                    // Cek duplikat menggunakan connection transaksi
                    checkStmt.setString(1, nik);
                    boolean isExists = false;
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            isExists = true;
                        }
                    }
                    
                    if (isExists) {
                        continue;
                    }
                    
                    // Insert menggunakan batch atau eksekusi satu per satu
                    insertStmt.setString(1, nik);
                    insertStmt.setString(2, nama);
                    insertStmt.setString(3, getCellValueAsString(row.getCell(2))); // alamat
                    insertStmt.setString(4, getCellValueAsString(row.getCell(3))); // rt_rw
                    insertStmt.setString(5, getCellValueAsString(row.getCell(4))); // kelurahan
                    insertStmt.setString(6, getCellValueAsString(row.getCell(5))); // kecamatan
                    insertStmt.setString(7, getCellValueAsString(row.getCell(6))); // jk
                    insertStmt.setString(8, getCellValueAsString(row.getCell(7))); // tgl_lahir
                    insertStmt.setString(9, getCellValueAsString(row.getCell(8)));  // telp
                    
                    insertStmt.executeUpdate();
                    count++;
                }
                
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                ex.printStackTrace();
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Error code
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
        return count;
    }
    
    public boolean exportToExcel(File file) {
        List<Warga> list = getAll();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data Warga");
            
            // Create Header
            Row headerRow = sheet.createRow(0);
            String[] columns = {"NIK", "Nama Lengkap", "Alamat", "RT/RW", "Kelurahan", "Kecamatan", "Jenis Kelamin", "Tanggal Lahir", "No. Telepon"};
            
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Create Data Rows
            int rowNum = 1;
            for (Warga w : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(w.getNik());
                row.createCell(1).setCellValue(w.getNama());
                row.createCell(2).setCellValue(w.getAlamat());
                row.createCell(3).setCellValue(w.getRtRw());
                row.createCell(4).setCellValue(w.getKelurahan());
                row.createCell(5).setCellValue(w.getKecamatan());
                row.createCell(6).setCellValue(w.getJenisKelamin());
                row.createCell(7).setCellValue(w.getTglLahir());
                row.createCell(8).setCellValue(w.getNoTelp());
            }
            
            // Auto size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            try (java.io.FileOutputStream fileOut = new java.io.FileOutputStream(file)) {
                workbook.write(fileOut);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // format numeric to avoid E notations for NIK
                DataFormatter formatter = new DataFormatter();
                return formatter.formatCellValue(cell);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    private Warga mapRowToWarga(ResultSet rs) throws SQLException {
        return new Warga(
            rs.getInt("id"),
            rs.getString("nik"),
            rs.getString("nama"),
            rs.getString("alamat"),
            rs.getString("rt_rw"),
            rs.getString("kelurahan"),
            rs.getString("kecamatan"),
            rs.getString("jenis_kelamin"),
            rs.getString("tgl_lahir"),
            rs.getString("no_telp")
        );
    }
    
    private void setWargaParams(PreparedStatement pstmt, Warga warga) throws SQLException {
        pstmt.setString(1, warga.getNik());
        pstmt.setString(2, warga.getNama());
        pstmt.setString(3, warga.getAlamat());
        pstmt.setString(4, warga.getRtRw());
        pstmt.setString(5, warga.getKelurahan());
        pstmt.setString(6, warga.getKecamatan());
        pstmt.setString(7, warga.getJenisKelamin());
        pstmt.setString(8, warga.getTglLahir());
        pstmt.setString(9, warga.getNoTelp());
    }
}
