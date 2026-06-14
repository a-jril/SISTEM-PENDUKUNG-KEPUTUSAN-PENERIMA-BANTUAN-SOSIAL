package com.spkbansos.view;

import com.spkbansos.dao.KriteriaDAO;
import com.spkbansos.model.KriteriaSAW;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KriteriaFormDialog extends JDialog {

    private JTextField txKode;
    private JTextField txNama;
    private JComboBox<String> cbAtribut;
    private JSpinner spBobot;
    private JTextArea txKeterangan;

    private KriteriaSAW kriteria;
    private final KriteriaDAO kriteriaDAO;
    private boolean isSaved = false;
    private int excludeId = -1;

    public KriteriaFormDialog(Frame parent, String title, KriteriaSAW kriteria) {
        super(parent, title, true);
        this.kriteria = kriteria;
        this.kriteriaDAO = new KriteriaDAO();
        if (kriteria != null) {
            this.excludeId = kriteria.getId();
        }

        setSize(400, 520);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(AppDesign.Colors.BG_PRIMARY);

        initUI();
        
        if (kriteria != null) {
            loadData();
        }
    }

    private void initUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        mainPanel.setBackground(AppDesign.Colors.BG_PRIMARY);

        // Header
        JLabel lblTitle = new JLabel(getTitle());
        lblTitle.setFont(AppDesign.Typography.HEADING_MD);
        lblTitle.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Kode
        mainPanel.add(createLabel("Kode Kriteria (Contoh: K1)"));
        txKode = new JTextField();
        styleTextField(txKode);
        mainPanel.add(txKode);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Nama
        mainPanel.add(createLabel("Nama Kriteria"));
        txNama = new JTextField();
        styleTextField(txNama);
        mainPanel.add(txNama);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Atribut
        mainPanel.add(createLabel("Atribut Kriteria"));
        cbAtribut = new JComboBox<>(new String[]{"benefit", "cost"});
        cbAtribut.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cbAtribut.setBackground(AppDesign.Colors.BG_TERTIARY);
        cbAtribut.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        cbAtribut.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(cbAtribut);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Bobot
        mainPanel.add(createLabel("Bobot (%)"));
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(10.0, 0.1, 100.0, 0.5);
        spBobot = new JSpinner(spinnerModel);
        spBobot.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        spBobot.setAlignmentX(Component.LEFT_ALIGNMENT);
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spBobot, "#,##0.0");
        spBobot.setEditor(editor);
        mainPanel.add(spBobot);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Keterangan
        mainPanel.add(createLabel("Keterangan"));
        txKeterangan = new JTextArea(3, 20);
        txKeterangan.setLineWrap(true);
        txKeterangan.setWrapStyleWord(true);
        txKeterangan.setBackground(AppDesign.Colors.BG_TERTIARY);
        txKeterangan.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        txKeterangan.setCaretColor(AppDesign.Colors.TEXT_PRIMARY);
        txKeterangan.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppDesign.Colors.BORDER, 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        
        JScrollPane scrollPane = new JScrollPane(txKeterangan);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(scrollPane);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(AppDesign.Colors.BG_PRIMARY);
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnBatal = new JButton("Batal");
        btnBatal.setFocusPainted(false);
        btnBatal.putClientProperty("FlatLaf.style", "arc: 8; margin: 6,16,6,16");
        btnBatal.addActionListener(e -> dispose());

        JButton btnSimpan = new JButton("Simpan");
        btnSimpan.setBackground(AppDesign.Colors.ACCENT);
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        btnSimpan.putClientProperty("FlatLaf.style", "arc: 8; margin: 6,16,6,16");
        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simpanData();
            }
        });

        btnPanel.add(btnBatal);
        btnPanel.add(btnSimpan);

        mainPanel.add(btnPanel);

        add(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppDesign.Typography.CAPTION_BOLD);
        label.setForeground(AppDesign.Colors.TEXT_SECONDARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void styleTextField(JTextField tf) {
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        tf.setBackground(AppDesign.Colors.BG_TERTIARY);
        tf.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        tf.setCaretColor(AppDesign.Colors.TEXT_PRIMARY);
        tf.putClientProperty("FlatLaf.style", "arc: 8; margin: 6,10,6,10");
    }

    private void loadData() {
        txKode.setText(kriteria.getKode());
        txNama.setText(kriteria.getNama());
        cbAtribut.setSelectedItem(kriteria.getAtribut());
        spBobot.setValue(kriteria.getBobot());
        txKeterangan.setText(kriteria.getKeterangan());
    }

    private void simpanData() {
        String kode = txKode.getText().trim();
        String nama = txNama.getText().trim();
        String atribut = cbAtribut.getSelectedItem().toString();
        double bobot = (Double) spBobot.getValue();
        String keterangan = txKeterangan.getText().trim();

        // Validasi
        if (kode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kode kriteria tidak boleh kosong!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama kriteria tidak boleh kosong!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Cek Kode Duplikat
        if (kriteriaDAO.isKodeExists(kode, excludeId)) {
            JOptionPane.showMessageDialog(this, "Kode kriteria sudah digunakan!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Cek Total Bobot (jika aktif)
        // Kita hanya menghitung validasi 100% jika user menyimpannya sebagai aktif
        int isAktif = kriteria != null ? kriteria.getIsAktif() : 1;
        if (isAktif == 1) {
            double currentTotal = kriteriaDAO.getTotalBobotAktif();
            double excludeBobot = 0;
            if (kriteria != null && kriteria.getIsAktif() == 1) {
                excludeBobot = kriteria.getBobot();
            }
            
            double newTotal = currentTotal - excludeBobot + bobot;
            if (newTotal > 100.0) {
                JOptionPane.showMessageDialog(this, 
                    "Total bobot aktif melebihi 100%!\nTotal saat ini jika disimpan: " + newTotal + "%", 
                    "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // Set values
        if (kriteria == null) {
            kriteria = new KriteriaSAW(0, kode, nama, atribut, bobot, keterangan, 1);
            isSaved = kriteriaDAO.insert(kriteria);
        } else {
            kriteria.setKode(kode);
            kriteria.setNama(nama);
            kriteria.setAtribut(atribut);
            kriteria.setBobot(bobot);
            kriteria.setKeterangan(keterangan);
            isSaved = kriteriaDAO.update(kriteria);
        }

        if (isSaved) {
            JOptionPane.showMessageDialog(this, "Data kriteria berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data ke database!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return isSaved;
    }
}
