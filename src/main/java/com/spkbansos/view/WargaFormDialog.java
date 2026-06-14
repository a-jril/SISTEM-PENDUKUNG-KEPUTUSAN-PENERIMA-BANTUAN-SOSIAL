package com.spkbansos.view;

import com.spkbansos.dao.WargaDAO;
import com.spkbansos.model.Warga;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class WargaFormDialog extends JDialog {

    private JTextField txNik;
    private JTextField txNama;
    private JTextArea txAlamat;
    private JTextField txRtRw;
    private JTextField txKelurahan;
    private JTextField txKecamatan;
    private JComboBox<String> cbJenisKelamin;
    private JTextField txTglLahir;
    private JTextField txNoTelp;

    private JLabel lblErrorNik;
    private JLabel lblErrorNama;
    private JLabel lblErrorNoTelp;

    private Warga warga;
    private final WargaDAO wargaDAO;
    private boolean isSaved = false;
    private int excludeId = -1;

    public WargaFormDialog(Frame parent, String title, Warga warga) {
        super(parent, title, true);
        this.warga = warga;
        this.wargaDAO = new WargaDAO();
        if (warga != null) {
            this.excludeId = warga.getId();
        }

        setSize(450, 700);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(AppDesign.Colors.BG_PRIMARY);

        initUI();
        
        if (warga != null) {
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

        // Input Fields (Scrollable area)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(AppDesign.Colors.BG_PRIMARY);
        
        txNik = new JTextField();
        lblErrorNik = createErrorLabel();
        addValidatedTextField(formPanel, "NIK (16 Digit)", txNik, lblErrorNik);
        txNik.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validateNik();
            }
        });

        txNama = new JTextField();
        lblErrorNama = createErrorLabel();
        addValidatedTextField(formPanel, "Nama Lengkap", txNama, lblErrorNama);
        txNama.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validateNama();
            }
        });
        
        formPanel.add(createLabel("Alamat Lengkap"));
        txAlamat = new JTextArea(3, 20);
        txAlamat.setLineWrap(true);
        txAlamat.setWrapStyleWord(true);
        txAlamat.setBackground(AppDesign.Colors.BG_TERTIARY);
        txAlamat.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        txAlamat.setCaretColor(AppDesign.Colors.TEXT_PRIMARY);
        txAlamat.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppDesign.Colors.BORDER, 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        JScrollPane scrollPane = new JScrollPane(txAlamat);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(scrollPane);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Horizontal grouping for RT/RW and Kelurahan
        JPanel row1 = new JPanel(new GridLayout(1, 2, 10, 0));
        row1.setBackground(AppDesign.Colors.BG_PRIMARY);
        row1.setAlignmentX(Component.LEFT_ALIGNMENT);
        row1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        JPanel pRt = new JPanel();
        pRt.setLayout(new BoxLayout(pRt, BoxLayout.Y_AXIS));
        pRt.setBackground(AppDesign.Colors.BG_PRIMARY);
        pRt.add(createLabel("RT/RW (cth: 001/002)"));
        txRtRw = new JTextField();
        styleTextField(txRtRw);
        pRt.add(txRtRw);
        
        JPanel pKel = new JPanel();
        pKel.setLayout(new BoxLayout(pKel, BoxLayout.Y_AXIS));
        pKel.setBackground(AppDesign.Colors.BG_PRIMARY);
        pKel.add(createLabel("Kelurahan"));
        txKelurahan = new JTextField();
        styleTextField(txKelurahan);
        pKel.add(txKelurahan);
        
        row1.add(pRt);
        row1.add(pKel);
        formPanel.add(row1);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        txKecamatan = addTextField(formPanel, "Kecamatan");
        
        formPanel.add(createLabel("Jenis Kelamin"));
        cbJenisKelamin = new JComboBox<>(new String[]{"Laki-laki", "Perempuan"});
        cbJenisKelamin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cbJenisKelamin.setBackground(AppDesign.Colors.BG_TERTIARY);
        cbJenisKelamin.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        cbJenisKelamin.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(cbJenisKelamin);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        txTglLahir = addTextField(formPanel, "Tanggal Lahir (YYYY-MM-DD)");
        
        txNoTelp = new JTextField();
        lblErrorNoTelp = createErrorLabel();
        addValidatedTextField(formPanel, "No. Telepon", txNoTelp, lblErrorNoTelp);
        txNoTelp.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validateNoTelp();
            }
        });

        JScrollPane mainScroll = new JScrollPane(formPanel);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(mainScroll);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

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
        btnSimpan.addActionListener(e -> simpanData());

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
    
    private JLabel createErrorLabel() {
        JLabel label = new JLabel(" ");
        label.setFont(AppDesign.Typography.CAPTION);
        label.setForeground(AppDesign.Colors.DANGER);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setVisible(false);
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
    
    private JTextField addTextField(JPanel panel, String labelText) {
        panel.add(createLabel(labelText));
        JTextField tf = new JTextField();
        styleTextField(tf);
        panel.add(tf);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        return tf;
    }
    
    private void addValidatedTextField(JPanel panel, String labelText, JTextField tf, JLabel errorLabel) {
        panel.add(createLabel(labelText));
        styleTextField(tf);
        panel.add(tf);
        panel.add(Box.createRigidArea(new Dimension(0, 2)));
        panel.add(errorLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void showError(JLabel errorLabel, JTextField tf, String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        tf.putClientProperty("JComponent.outline", "error"); // FlatLaf error outline
    }

    private void hideError(JLabel errorLabel, JTextField tf) {
        errorLabel.setVisible(false);
        tf.putClientProperty("JComponent.outline", null);
    }

    private boolean validateNik() {
        String nik = txNik.getText().trim();
        if (nik.isEmpty()) {
            showError(lblErrorNik, txNik, "NIK tidak boleh kosong");
            return false;
        } else if (!nik.matches("\\d{16}")) {
            showError(lblErrorNik, txNik, "NIK harus terdiri dari 16 digit angka");
            return false;
        } else if (wargaDAO.isNikExists(nik, excludeId)) {
            showError(lblErrorNik, txNik, "NIK sudah terdaftar");
            return false;
        }
        hideError(lblErrorNik, txNik);
        return true;
    }

    private boolean validateNama() {
        String nama = txNama.getText().trim();
        if (nama.isEmpty()) {
            showError(lblErrorNama, txNama, "Nama tidak boleh kosong");
            return false;
        } else if (nama.matches(".*\\d.*")) {
            showError(lblErrorNama, txNama, "Nama tidak boleh mengandung angka");
            return false;
        }
        hideError(lblErrorNama, txNama);
        return true;
    }

    private boolean validateNoTelp() {
        String telp = txNoTelp.getText().trim();
        if (!telp.isEmpty() && !telp.matches("\\+?\\d{9,15}")) {
            showError(lblErrorNoTelp, txNoTelp, "Format nomor telepon tidak valid");
            return false;
        }
        hideError(lblErrorNoTelp, txNoTelp);
        return true;
    }

    private void loadData() {
        txNik.setText(warga.getNik());
        txNama.setText(warga.getNama());
        txAlamat.setText(warga.getAlamat());
        txRtRw.setText(warga.getRtRw());
        txKelurahan.setText(warga.getKelurahan());
        txKecamatan.setText(warga.getKecamatan());
        cbJenisKelamin.setSelectedItem(warga.getJenisKelamin());
        txTglLahir.setText(warga.getTglLahir());
        txNoTelp.setText(warga.getNoTelp());
    }

    private void simpanData() {
        boolean validNik = validateNik();
        boolean validNama = validateNama();
        boolean validTelp = validateNoTelp();

        if (!validNik || !validNama || !validTelp) {
            JOptionPane.showMessageDialog(this, "Perbaiki kesalahan pada form terlebih dahulu.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tglLahir = txTglLahir.getText().trim();
        if (!tglLahir.isEmpty() && !tglLahir.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Format Tanggal Lahir harus YYYY-MM-DD!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nik = txNik.getText().trim();
        String nama = txNama.getText().trim();

        if (warga == null) {
            warga = new Warga(0, nik, nama, txAlamat.getText().trim(), txRtRw.getText().trim(),
                txKelurahan.getText().trim(), txKecamatan.getText().trim(),
                cbJenisKelamin.getSelectedItem().toString(), txTglLahir.getText().trim(),
                txNoTelp.getText().trim());
            isSaved = wargaDAO.insert(warga);
        } else {
            warga.setNik(nik);
            warga.setNama(nama);
            warga.setAlamat(txAlamat.getText().trim());
            warga.setRtRw(txRtRw.getText().trim());
            warga.setKelurahan(txKelurahan.getText().trim());
            warga.setKecamatan(txKecamatan.getText().trim());
            warga.setJenisKelamin(cbJenisKelamin.getSelectedItem().toString());
            warga.setTglLahir(txTglLahir.getText().trim());
            warga.setNoTelp(txNoTelp.getText().trim());
            isSaved = wargaDAO.update(warga);
        }

        if (isSaved) {
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data ke database!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return isSaved;
    }
}
