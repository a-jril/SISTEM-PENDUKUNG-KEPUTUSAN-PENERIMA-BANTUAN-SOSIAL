package com.spkbansos;

import com.formdev.flatlaf.FlatDarkLaf;
import com.spkbansos.dao.DatabaseHelper;
import com.spkbansos.view.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // 1. Setup tema UI FlatLaf (Harus dijalankan paling awal)
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Gagal menginisialisasi FlatLaf");
        }

        // 2. Inisialisasi Database
        DatabaseHelper.initDatabase();
        
        // 3. Tampilkan halaman Login
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
