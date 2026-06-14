package com.spkbansos.view;

import com.spkbansos.controller.SAWController;
import com.spkbansos.dao.KriteriaDAO;
import com.spkbansos.dao.PenilaianDAO;
import com.spkbansos.model.HasilSAW;
import com.spkbansos.model.KriteriaSAW;
import com.spkbansos.model.User;
import com.spkbansos.util.IconHelper;
import com.spkbansos.util.LaporanHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.Map;

public class HasilKeputusanPanel extends JPanel {

    private final SAWController sawController;
    private final PenilaianDAO penilaianDAO;
    private final KriteriaDAO kriteriaDAO;

    private JComboBox<String> cbPeriode;
    private JButton btnHitung;
    private JLabel lblStatus;

    private JTable tblHasil;
    private DefaultTableModel tableModel;
    private JLabel lblInfo;
    
    private JPanel summaryPanel;
    private JLabel lblLayak;
    private JLabel lblTidakLayak;

    private EmptyStateOverlay emptyOverlay;

    private JButton btnDetail;
    private JButton btnCetak;

    private List<HasilSAW> currentHasilList;
    private User currentUser;

    public HasilKeputusanPanel(User user) {
        this.currentUser = user;
        sawController = new SAWController();
        penilaianDAO = new PenilaianDAO();
        kriteriaDAO = new KriteriaDAO();

        setLayout(new BorderLayout());
        setBackground(AppDesign.Colors.BG_PRIMARY);
        setBorder(new EmptyBorder(AppDesign.Dimensions.PAGE_PADDING, AppDesign.Dimensions.PAGE_PADDING,
                AppDesign.Dimensions.PAGE_PADDING, AppDesign.Dimensions.PAGE_PADDING));

        initUI();
        loadPeriodeDropdown();
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refresh();
            }
        });
    }

    private void initUI() {
        // --- Control Panel (Periode & Hitung) — sejajar dengan lblStatus, di atas
        // tabel ---
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controlPanel.setBackground(AppDesign.Colors.BG_PRIMARY);

        JLabel lblPeriode = new JLabel("Periode:");
        lblPeriode.setFont(AppDesign.Typography.BODY);
        lblPeriode.setForeground(AppDesign.Colors.TEXT_PRIMARY);

        cbPeriode = new JComboBox<>();
        cbPeriode.setPreferredSize(new Dimension(120, 35));
        cbPeriode.setBackground(AppDesign.Colors.BG_TERTIARY);
        cbPeriode.setForeground(AppDesign.Colors.TEXT_PRIMARY);

        btnHitung = new JButton("Hitung SAW", IconHelper.getIcon("player-play", 16, Color.WHITE));
        btnHitung.setPreferredSize(new Dimension(140, 35));
        btnHitung.setBackground(AppDesign.Colors.ACCENT);
        btnHitung.setForeground(Color.WHITE);
        btnHitung.setFocusPainted(false);
        btnHitung.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHitung.putClientProperty("FlatLaf.style", "arc: 8; margin: 4,12,4,12");
        btnHitung.addActionListener(e -> hitungSAW());

        controlPanel.add(lblPeriode);
        controlPanel.add(cbPeriode);
        controlPanel.add(btnHitung);

        // --- Center Panel (Tabel) ---
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(AppDesign.Colors.BG_PRIMARY);

        // Baris di atas tabel: lblStatus (kiri) + controlPanel periode/button (kanan)
        JPanel aboveTabelPanel = new JPanel(new BorderLayout());
        aboveTabelPanel.setBackground(AppDesign.Colors.BG_PRIMARY);
        aboveTabelPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel statusControlRow = new JPanel(new BorderLayout());
        statusControlRow.setBackground(AppDesign.Colors.BG_PRIMARY);
        lblStatus = new JLabel("Belum pernah dihitung");
        lblStatus.setFont(AppDesign.Typography.CAPTION);
        lblStatus.setForeground(AppDesign.Colors.TEXT_SECONDARY);
        statusControlRow.add(lblStatus, BorderLayout.WEST);
        statusControlRow.add(controlPanel, BorderLayout.EAST);
        
        aboveTabelPanel.add(statusControlRow, BorderLayout.NORTH);
        
        // Summary Panel
        summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        summaryPanel.setBackground(AppDesign.Colors.BG_PRIMARY);
        summaryPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        lblLayak = new JLabel("0 Warga Layak (0%)");
        lblLayak.setFont(AppDesign.Typography.BODY_BOLD);
        lblLayak.setForeground(AppDesign.Colors.SUCCESS);
        lblLayak.setIcon(IconHelper.getIcon("circle-check", 18, AppDesign.Colors.SUCCESS));
        
        lblTidakLayak = new JLabel("0 Warga Tidak Layak (0%)");
        lblTidakLayak.setFont(AppDesign.Typography.BODY_BOLD);
        lblTidakLayak.setForeground(AppDesign.Colors.DANGER);
        lblTidakLayak.setIcon(IconHelper.getIcon("circle-x", 18, AppDesign.Colors.DANGER));
        
        summaryPanel.add(lblLayak);
        summaryPanel.add(lblTidakLayak);
        summaryPanel.setVisible(false);
        
        aboveTabelPanel.add(summaryPanel, BorderLayout.SOUTH);

        centerPanel.add(aboveTabelPanel, BorderLayout.NORTH);

        String[] columns = { "Peringkat", "NIK", "Nama", "Skor V", "Rekomendasi" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblHasil = new JTable(tableModel);
        tblHasil.setRowHeight(35);
        tblHasil.getTableHeader().setFont(AppDesign.Typography.CAPTION_BOLD);
        tblHasil.setFont(AppDesign.Typography.BODY);
        tblHasil.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblHasil.setRowSorter(new TableRowSorter<>(tableModel));

        // Alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblHasil.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblHasil.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tblHasil.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        
        DefaultTableCellRenderer rekRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(JLabel.CENTER);
                if (value != null) {
                    String val = value.toString();
                    if ("Layak".equals(val)) {
                        label.setIcon(IconHelper.getIcon("circle-check", 16, AppDesign.Colors.SUCCESS));
                        if (!isSelected) label.setForeground(AppDesign.Colors.SUCCESS);
                    } else {
                        label.setIcon(IconHelper.getIcon("circle-x", 16, AppDesign.Colors.DANGER));
                        if (!isSelected) label.setForeground(AppDesign.Colors.DANGER);
                    }
                }
                return label;
            }
        };
        tblHasil.getColumnModel().getColumn(4).setCellRenderer(rekRenderer);

        tblHasil.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = tblHasil.getSelectedRow() != -1;
            btnDetail.setEnabled(hasSelection);
        });

        JScrollPane scrollPane = new JScrollPane(tblHasil);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppDesign.Colors.BORDER_SUBTLE));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        emptyOverlay = EmptyStateOverlay.install(centerPanel, scrollPane, "calculator", "Klik 'Hitung SAW' untuk melihat hasil perhitungan.");

        add(centerPanel, BorderLayout.CENTER);

        // --- Bottom Panel ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(AppDesign.Colors.BG_PRIMARY);
        bottomPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        lblInfo = new JLabel("Menampilkan 0 hasil");
        lblInfo.setFont(AppDesign.Typography.CAPTION_BOLD);
        lblInfo.setForeground(AppDesign.Colors.TEXT_SECONDARY);
        bottomPanel.add(lblInfo, BorderLayout.WEST);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(AppDesign.Colors.BG_PRIMARY);

        btnDetail = new JButton("Lihat Detail", IconHelper.getIcon("list-details", 16, Color.WHITE));
        btnDetail.setBackground(AppDesign.Colors.ACCENT_SECONDARY);
        btnDetail.setForeground(Color.WHITE);
        btnDetail.setFocusPainted(false);
        btnDetail.setEnabled(false);
        btnDetail.putClientProperty("FlatLaf.style", "arc: 8; margin: 6,16,6,16");
        btnDetail.addActionListener(e -> showDetail());

        btnCetak = new JButton("Cetak Laporan", IconHelper.getIcon("printer", 16, Color.WHITE));
        btnCetak.setBackground(AppDesign.Colors.SUCCESS);
        btnCetak.setForeground(Color.WHITE);
        btnCetak.setFocusPainted(false);
        btnCetak.setEnabled(false); // Diaktifkan jika tabel tidak kosong
        btnCetak.putClientProperty("FlatLaf.style", "arc: 8; margin: 6,16,6,16");
        btnCetak.addActionListener(e -> cetakLaporan());

        actionPanel.add(btnDetail);
        actionPanel.add(btnCetak);
        bottomPanel.add(actionPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void refresh() {
        loadPeriodeDropdown();
    }

    private void loadPeriodeDropdown() {
        cbPeriode.removeAllItems();
        List<String> list = penilaianDAO.getAllPeriode();
        for (String p : list) {
            cbPeriode.addItem(p);
        }
        if (cbPeriode.getItemCount() > 0) {
            cbPeriode.setSelectedIndex(0);
        }
    }

    private void hitungSAW() {
        if (cbPeriode.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih periode terlebih dahulu.", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String periode = cbPeriode.getSelectedItem().toString();

        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            currentHasilList = sawController.hitung(periode);

            tableModel.setRowCount(0);
            int countLayak = 0;
            int countTidak = 0;
            
            for (HasilSAW h : currentHasilList) {
                boolean isLayak = h.getSkorV() >= AppDesign.Config.KELAYAKAN_THRESHOLD;
                if (isLayak) countLayak++;
                else countTidak++;
                
                String rekomendasi = isLayak ? "Layak" : "Tidak Layak";
                Object[] row = {
                        h.getPeringkat(),
                        h.getNik(),
                        h.getNama(),
                        String.format("%.4f", h.getSkorV()),
                        rekomendasi
                };
                tableModel.addRow(row);
            }
            
            int total = currentHasilList.size();
            if (total > 0) {
                int pctLayak = (int) Math.round((double) countLayak / total * 100);
                int pctTidak = 100 - pctLayak;
                lblLayak.setText(countLayak + " Warga Layak (" + pctLayak + "%)");
                lblTidakLayak.setText(countTidak + " Warga Tidak Layak (" + pctTidak + "%)");
                summaryPanel.setVisible(true);
            } else {
                summaryPanel.setVisible(false);
            }

            lblStatus.setText("Terakhir dihitung: " + java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            lblInfo.setText("Menampilkan " + currentHasilList.size() + " hasil perhitungan");
            btnCetak.setEnabled(currentHasilList.size() > 0);
            if (emptyOverlay != null) emptyOverlay.updateState(tableModel.getRowCount());

        } catch (SAWController.SAWValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan internal: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void showDetail() {
        int selectedRow = tblHasil.getSelectedRow();
        if (selectedRow == -1 || currentHasilList == null)
            return;

        HasilSAW hasil = currentHasilList.get(selectedRow);
        List<KriteriaSAW> kriteriaList = kriteriaDAO.getAktif();

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Detail Perhitungan - " + hasil.getNama(),
                Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(750, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        String[] columns = { "Kode", "Kriteria", "Atribut", "Bobot", "Nilai Mentah", "Nilai Normalisasi",
                "Kontribusi" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        Map<Integer, Double> mentahMap = hasil.getNilaiMentahPerKriteria();
        Map<Integer, Double> normMap = hasil.getNilaiNormalisasiPerKriteria();

        for (KriteriaSAW k : kriteriaList) {
            int idK = k.getId();
            double mentah = mentahMap != null ? mentahMap.getOrDefault(idK, 0.0) : 0.0;
            double norm = normMap != null ? normMap.getOrDefault(idK, 0.0) : 0.0;
            double kontribusi = (k.getBobot() / 100.0) * norm;

            Object[] row = {
                    k.getKode(),
                    k.getNama(),
                    k.getAtribut(),
                    k.getBobot() + "%",
                    String.format("%.2f", mentah),
                    String.format("%.4f", norm),
                    String.format("%.4f", kontribusi)
            };
            model.addRow(row);
        }

        JTable detailTable = new JTable(model);
        detailTable.setRowHeight(30);
        detailTable.getTableHeader().setFont(AppDesign.Typography.CAPTION_BOLD);

        panel.add(new JScrollPane(detailTable), BorderLayout.CENTER);

        JLabel lblTotal = new JLabel("Total Skor V: " + String.format("%.4f", hasil.getSkorV()));
        lblTotal.setFont(AppDesign.Typography.HEADING_MD);
        panel.add(lblTotal, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void cetakLaporan() {
        if (currentHasilList == null || currentHasilList.isEmpty())
            return;

        String periode = cbPeriode.getSelectedItem() != null
                ? cbPeriode.getSelectedItem().toString()
                : "-";

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            Exception error = null;

            @Override
            protected Void doInBackground() {
                try {
                    String operatorNama = (currentUser != null) ? currentUser.getNamaLengkap() : "Administrator";
                    LaporanHelper.cetakLaporanRekomendasi(currentHasilList, periode, operatorNama);
                } catch (Exception ex) {
                    error = ex;
                }
                return null;
            }

            @Override
            protected void done() {
                if (error != null) {
                    JOptionPane.showMessageDialog(HasilKeputusanPanel.this,
                            "Gagal membuat laporan: " + error.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
}