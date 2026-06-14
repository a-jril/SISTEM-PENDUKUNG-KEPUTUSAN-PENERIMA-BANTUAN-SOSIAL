package com.spkbansos.view;

import com.spkbansos.dao.PenilaianDAO;
import com.spkbansos.dao.WargaDAO;
import com.spkbansos.dao.KriteriaDAO;
import com.spkbansos.model.HasilSAW;
import com.spkbansos.controller.SAWController;
import com.spkbansos.util.IconHelper;
import com.spkbansos.util.LaporanHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Halaman Pusat Laporan — Fase 7.
 * Menampilkan 4 card laporan: Data Warga, Kriteria SAW, dan Rekomendasi Bansos.
 */
public class LaporanPanel extends JPanel {

    private final PenilaianDAO penilaianDAO;
    private final WargaDAO wargaDAO;
    private final KriteriaDAO kriteriaDAO;
    private JComboBox<String> cbPeriode;
    private String loggedInUser = "Administrator";
    
    private JLabel lblMetaWarga;
    private JLabel lblMetaKriteria;
    private JLabel lblMetaRekomendasi;

    public LaporanPanel() {
        penilaianDAO = new PenilaianDAO();
        wargaDAO = new WargaDAO();
        kriteriaDAO = new KriteriaDAO();

        setLayout(new BorderLayout());
        setBackground(AppDesign.Colors.BG_PRIMARY);
        setBorder(new EmptyBorder(
                AppDesign.Dimensions.PAGE_PADDING,
                AppDesign.Dimensions.PAGE_PADDING,
                AppDesign.Dimensions.PAGE_PADDING,
                AppDesign.Dimensions.PAGE_PADDING));

        initUI();
        loadPeriode();
        loadStaticMetadata();
    }

    /**
     * Dipanggil dari MainFrame saat user login agar nama operator tersedia di
     * laporan.
     */
    public void setLoggedInUser(String namaLengkap) {
        this.loggedInUser = namaLengkap != null ? namaLengkap : "Administrator";
    }

    // =========================================================================
    // UI Init
    // =========================================================================

    private void initUI() {
        // --- Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppDesign.Colors.BG_PRIMARY);
        headerPanel.setBorder(new EmptyBorder(0, 0, 28, 0));

        JLabel lblSub = new JLabel("Pilih laporan yang ingin dicetak atau di-preview");
        lblSub.setFont(AppDesign.Typography.BODY);
        lblSub.setForeground(AppDesign.Colors.TEXT_SECONDARY);

        JPanel titleStack = new JPanel();
        titleStack.setLayout(new BoxLayout(titleStack, BoxLayout.Y_AXIS));
        titleStack.setBackground(AppDesign.Colors.BG_PRIMARY);
        titleStack.add(lblSub);

        headerPanel.add(titleStack, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // --- Scrollable Content ---
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(AppDesign.Colors.BG_PRIMARY);

        lblMetaWarga = new JLabel("Memuat metadata...");
        lblMetaKriteria = new JLabel("Memuat metadata...");
        lblMetaRekomendasi = new JLabel("Memuat metadata...");

        // Baris 1: Laporan yang tidak butuh periode
        JPanel row1 = buildRow();
        row1.add(buildCard(
                "file-text",
                "Buku Induk Data Warga",
                "Daftar seluruh warga yang terdaftar dalam sistem beserta data lengkap demografinya.",
                false,
                e -> jalankanLaporan(LaporanJenis.WARGA, null),
                lblMetaWarga));
        row1.add(Box.createRigidArea(new Dimension(24, 0)));
        row1.add(buildCard(
                "circle-check",
                "Daftar Kriteria & Bobot SAW",
                "Daftar semua kriteria penilaian SAW beserta bobot, atribut, dan status aktif masing-masing.",
                false,
                e -> jalankanLaporan(LaporanJenis.KRITERIA, null),
                lblMetaKriteria));

        // Baris 2: Laporan yang butuh filter periode
        JPanel periodeRow = buildPeriodeFilterPanel();

        JPanel row2 = buildRow();
        row2.add(buildCard(
                "award",
                "Laporan Rekomendasi Penerima Bansos",
                "Peringkat hasil perhitungan SAW beserta rekomendasi kelayakan per warga untuk periode yang dipilih. Laporan utama Tugas Akhir.",
                true,
                e -> jalankanLaporan(LaporanJenis.REKOMENDASI, null),
                lblMetaRekomendasi));

        contentPanel.add(row1);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(periodeRow);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        contentPanel.add(row2);
        contentPanel.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(AppDesign.Colors.BG_PRIMARY);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildRow() {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setBackground(AppDesign.Colors.BG_PRIMARY);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        return row;
    }

    /** Panel filter periode (dipakai oleh laporan yang butuh periode). */
    private JPanel buildPeriodeFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setBackground(AppDesign.Colors.BG_SECONDARY);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppDesign.Colors.BORDER_SUBTLE, 1, true),
                new EmptyBorder(12, 16, 12, 16)));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel("Filter Periode untuk Laporan Rekomendasi:");
        lbl.setFont(AppDesign.Typography.BODY_BOLD);
        lbl.setForeground(AppDesign.Colors.TEXT_PRIMARY);

        cbPeriode = new JComboBox<>();
        cbPeriode.setPreferredSize(new Dimension(150, 34));
        cbPeriode.setBackground(AppDesign.Colors.BG_TERTIARY);
        cbPeriode.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        cbPeriode.setFont(AppDesign.Typography.BODY);
        
        cbPeriode.addActionListener(e -> updateRekomendasiMetadata());

        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(16, 0)));
        panel.add(cbPeriode);

        return panel;
    }

    /**
     * Bangun satu card laporan.
     *
     * @param iconName     Nama ikon Tabler
     * @param title        Judul laporan
     * @param description  Deskripsi singkat laporan
     * @param needsPeriode Jika true, badge "Perlu periode" ditampilkan
     * @param action       Action listener tombol Cetak
     * @param lblMeta      Label metadata yang disematkan di kiri tombol
     */
    private JPanel buildCard(String iconName, String title, String description,
            boolean needsPeriode, java.awt.event.ActionListener action, JLabel lblMeta) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(AppDesign.Colors.BG_SECONDARY);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppDesign.Colors.BORDER_SUBTLE, 1, true),
                new EmptyBorder(20, 20, 20, 20)));
        card.setPreferredSize(new Dimension(380, 200));
        card.setMaximumSize(new Dimension(600, 200));

        // --- Atas: ikon + judul ---
        JPanel topSection = new JPanel(new BorderLayout(12, 0));
        topSection.setBackground(AppDesign.Colors.BG_SECONDARY);

        // Ikon dalam lingkaran
        JPanel iconCircle = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(AppDesign.Colors.ACCENT.getRed(),
                        AppDesign.Colors.ACCENT.getGreen(),
                        AppDesign.Colors.ACCENT.getBlue(), 25));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconCircle.setOpaque(false);
        iconCircle.setPreferredSize(new Dimension(48, 48));
        iconCircle.setMinimumSize(new Dimension(48, 48));
        iconCircle.setMaximumSize(new Dimension(48, 48));
        Icon icon = IconHelper.getIcon(iconName, 24, AppDesign.Colors.ACCENT);
        iconCircle.add(new JLabel(icon));

        // Judul + badge
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(AppDesign.Colors.BG_SECONDARY);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(AppDesign.Typography.BODY_BOLD);
        lblTitle.setForeground(AppDesign.Colors.TEXT_PRIMARY);

        titlePanel.add(lblTitle);
        if (needsPeriode) {
            titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
            JLabel badge = new JLabel("Butuh filter periode");
            badge.setFont(AppDesign.Typography.CAPTION);
            badge.setForeground(AppDesign.Colors.ACCENT);
            titlePanel.add(badge);
        }

        topSection.add(iconCircle, BorderLayout.WEST);
        topSection.add(titlePanel, BorderLayout.CENTER);

        // --- Tengah: deskripsi ---
        JTextArea txtDesc = new JTextArea(description);
        txtDesc.setFont(AppDesign.Typography.CAPTION);
        txtDesc.setForeground(AppDesign.Colors.TEXT_SECONDARY);
        txtDesc.setBackground(AppDesign.Colors.BG_SECONDARY);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setLineWrap(true);
        txtDesc.setEditable(false);
        txtDesc.setFocusable(false);
        txtDesc.setBorder(null);
        txtDesc.setRows(3);

        // --- Bawah: Metadata + tombol Cetak ---
        JPanel bottomSection = new JPanel(new BorderLayout());
        bottomSection.setBackground(AppDesign.Colors.BG_SECONDARY);

        if (lblMeta != null) {
            lblMeta.setFont(AppDesign.Typography.CAPTION);
            lblMeta.setForeground(AppDesign.Colors.TEXT_MUTED);
            bottomSection.add(lblMeta, BorderLayout.WEST);
        }

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnPanel.setBackground(AppDesign.Colors.BG_SECONDARY);

        JButton btnCetak = new JButton("Preview & Cetak",
                IconHelper.getIcon("printer", 16, Color.WHITE));
        btnCetak.setBackground(AppDesign.Colors.ACCENT);
        btnCetak.setForeground(Color.WHITE);
        btnCetak.setFocusPainted(false);
        btnCetak.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCetak.putClientProperty("FlatLaf.style", "arc: 8; margin: 6,16,6,16");
        btnCetak.addActionListener(action);

        btnPanel.add(btnCetak);
        bottomSection.add(btnPanel, BorderLayout.EAST);

        card.add(topSection, BorderLayout.NORTH);
        card.add(txtDesc, BorderLayout.CENTER);
        card.add(bottomSection, BorderLayout.SOUTH);

        return card;
    }

    // =========================================================================
    // Logika cetak & metadata
    // =========================================================================

    private void loadStaticMetadata() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            int countWarga;
            int countKriteria;
            @Override
            protected Void doInBackground() {
                countWarga = wargaDAO.getCount();
                countKriteria = kriteriaDAO.getCountAktif();
                return null;
            }
            @Override
            protected void done() {
                lblMetaWarga.setText("Total: " + countWarga + " warga terdaftar");
                lblMetaKriteria.setText("Total: " + countKriteria + " kriteria aktif");
            }
        };
        worker.execute();
    }
    
    private void updateRekomendasiMetadata() {
        String periode = cbPeriode.getSelectedItem() != null ? cbPeriode.getSelectedItem().toString() : null;
        if (periode == null) {
            lblMetaRekomendasi.setText("Pilih periode terlebih dahulu");
            return;
        }
        
        lblMetaRekomendasi.setText("Memuat data...");
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            String metaText = "";
            @Override
            protected Void doInBackground() {
                try {
                    SAWController controller = new SAWController();
                    List<HasilSAW> hasilList = controller.hitung(periode);
                    int layak = 0;
                    for (HasilSAW h : hasilList) {
                        if (h.getSkorV() >= AppDesign.Config.KELAYAKAN_THRESHOLD) layak++;
                    }
                    metaText = "Data valid: " + hasilList.size() + " warga (" + layak + " Layak)";
                } catch (Exception ex) {
                    metaText = "Data belum lengkap untuk dihitung";
                }
                return null;
            }
            @Override
            protected void done() {
                lblMetaRekomendasi.setText(metaText);
            }
        };
        worker.execute();
    }

    private enum LaporanJenis {
        WARGA, KRITERIA, REKOMENDASI
    }

    private void jalankanLaporan(LaporanJenis jenis, String periodeOverride) {
        // Jalankan di background thread agar UI tidak freeze saat compile .jrxml
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            Exception error = null;

            @Override
            protected Void doInBackground() {
                try {
                    switch (jenis) {
                        case WARGA:
                            LaporanHelper.cetakLaporanWarga();
                            break;
                        case KRITERIA:
                            LaporanHelper.cetakLaporanKriteria();
                            break;
                        case REKOMENDASI:
                            String periode = periodeOverride != null
                                    ? periodeOverride
                                    : (cbPeriode.getSelectedItem() != null
                                            ? cbPeriode.getSelectedItem().toString()
                                            : null);
                            if (periode == null || periode.isEmpty()) {
                                error = new Exception("Silakan pilih periode terlebih dahulu.");
                                return null;
                            }
                            // Hitung SAW dulu baru cetak
                            SAWController sawController = new SAWController();
                            List<HasilSAW> hasilList = sawController.hitung(periode);
                            if (hasilList == null || hasilList.isEmpty()) {
                                error = new Exception("Tidak ada hasil perhitungan untuk periode " + periode
                                        + ".\nPastikan sudah menjalankan perhitungan di halaman Hasil Keputusan.");
                                return null;
                            }
                            LaporanHelper.cetakLaporanRekomendasi(hasilList, periode, loggedInUser);
                            break;
                    }
                } catch (Exception ex) {
                    error = ex;
                }
                return null;
            }

            @Override
            protected void done() {
                if (error != null) {
                    String msg = error.getMessage();
                    if (msg == null)
                        msg = "Terjadi kesalahan saat membuat laporan.";
                    JOptionPane.showMessageDialog(LaporanPanel.this,
                            msg, "Gagal Membuat Laporan", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        // Tampilkan loading sementara
        JOptionPane pane = new JOptionPane(
                "Sedang memproses laporan...\nMohon tunggu sebentar.",
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION, null, new Object[] {}, null);
        JDialog loadingDialog = pane.createDialog(this, "Memuat Laporan");
        loadingDialog.setModal(false);

        worker.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName())
                    && SwingWorker.StateValue.DONE == evt.getNewValue()) {
                loadingDialog.dispose();
            }
        });

        worker.execute();
        loadingDialog.setVisible(true);
    }

    /**
     * Dipanggil dari MainFrame setiap kali panel ini ditampilkan, supaya dropdown
     * periode selalu up-to-date.
     */
    public void refreshData() {
        loadPeriode();
        loadStaticMetadata();
    }

    private void loadPeriode() {
        if (cbPeriode == null)
            return;
        cbPeriode.removeAllItems();
        List<String> periodeList = penilaianDAO.getAllPeriode();
        for (String p : periodeList) {
            cbPeriode.addItem(p);
        }
        if (cbPeriode.getItemCount() > 0) {
            cbPeriode.setSelectedIndex(0);
        }
    }
}
