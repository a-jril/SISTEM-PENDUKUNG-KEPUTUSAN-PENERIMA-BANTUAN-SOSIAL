package com.spkbansos.view;

import com.spkbansos.util.IconHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AboutDialog extends JDialog {

    public AboutDialog(JFrame parent) {
        super(parent, "Tentang Aplikasi", true);
        setResizable(false);
        getContentPane().setBackground(AppDesign.Colors.BG_PRIMARY);

        initUI();
        
        pack();
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(AppDesign.Colors.BG_PRIMARY);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Logo
        JLabel lblLogo = new JLabel();
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            java.net.URL logoUrl = getClass().getResource("/images/logo_spkbansos.png");
            if (logoUrl != null) {
                ImageIcon originalIcon = new ImageIcon(logoUrl);
                Image scaledImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(scaledImage));
            } else {
                lblLogo.setIcon(IconHelper.getIcon("layout-dashboard", 80, Color.WHITE));
            }
        } catch (Exception e) {
            lblLogo.setIcon(IconHelper.getIcon("layout-dashboard", 80, Color.WHITE));
        }

        JLabel lblTitle = new JLabel(AppDesign.Config.APP_NAME);
        lblTitle.setFont(AppDesign.Typography.HEADING_LG);
        lblTitle.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblVersion = new JLabel("Versi " + AppDesign.Config.APP_VERSION);
        lblVersion.setFont(AppDesign.Typography.BODY_SM);
        lblVersion.setForeground(AppDesign.Colors.TEXT_SECONDARY);
        lblVersion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea txDesc = new JTextArea(
            "Sistem Pendukung Keputusan Penerima Bantuan Sosial menggunakan metode Simple Additive Weighting (SAW). " +
            "Dibuat untuk mempermudah perangkat desa dalam menentukan warga yang layak menerima bantuan secara objektif."
        );
        txDesc.setFont(AppDesign.Typography.BODY);
        txDesc.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        txDesc.setBackground(AppDesign.Colors.BG_PRIMARY);
        txDesc.setColumns(30);
        txDesc.setWrapStyleWord(true);
        txDesc.setLineWrap(true);
        txDesc.setEditable(false);
        txDesc.setFocusable(false);
        txDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        txDesc.setBorder(new EmptyBorder(15, 0, 15, 0));

        JButton btnClose = new JButton("Tutup");
        btnClose.setFont(AppDesign.Typography.BODY_BOLD);
        btnClose.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnClose.setMaximumSize(new Dimension(150, 35));
        btnClose.setBackground(AppDesign.Colors.ACCENT);
        btnClose.setForeground(Color.WHITE);
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.putClientProperty("FlatLaf.style", "arc: 8;");
        btnClose.addActionListener(e -> dispose());

        mainPanel.add(lblLogo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(lblTitle);
        mainPanel.add(lblVersion);
        mainPanel.add(txDesc);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(btnClose);

        add(mainPanel);
    }
}
