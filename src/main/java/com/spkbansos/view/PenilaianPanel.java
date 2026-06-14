package com.spkbansos.view;

import com.spkbansos.dao.KriteriaDAO;
import com.spkbansos.dao.PenilaianDAO;
import com.spkbansos.dao.WargaDAO;
import com.spkbansos.model.KriteriaSAW;
import com.spkbansos.model.Warga;
import com.spkbansos.util.IconHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PenilaianPanel extends JPanel implements Searchable {

    private final WargaDAO wargaDAO;
    private final KriteriaDAO kriteriaDAO;
    private final PenilaianDAO penilaianDAO;

    private JComboBox<String> cbPeriode;
    private DefaultTableModel tableModel;
    private JTable tblDaftarWarga;
    private JLabel lblProgress;

    private JPanel rightPanel;
    private JPanel dynamicFormPanel;
    private JLabel lblWargaDipilih;
    private JButton btnSimpan;
    private JButton btnHapus;
    private JButton btnBatal;

    private int selectedWargaId = -1;
    private String selectedPeriode = "-";
    private String currentKeyword = "";
    private Map<Integer, JSpinner> fieldMap = new HashMap<>();

    public PenilaianPanel() {
        wargaDAO = new WargaDAO();
        kriteriaDAO = new KriteriaDAO();
        penilaianDAO = new PenilaianDAO();

        setLayout(new BorderLayout(20, 0));
        setBackground(AppDesign.Colors.BG_PRIMARY);
        setBorder(new EmptyBorder(AppDesign.Dimensions.PAGE_PADDING, AppDesign.Dimensions.PAGE_PADDING, AppDesign.Dimensions.PAGE_PADDING, AppDesign.Dimensions.PAGE_PADDING));

        initLeftPanel();
        initRightPanel();
        
        loadPeriodeDropdown();
        loadDaftarWarga("");
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                // If needed, refresh data
                // We rely on MainFrame passing search via onSearch
                loadDaftarWarga("");
            }
        });
    }

    @Override
    public void onSearch(String keyword) {
        this.currentKeyword = keyword;
        loadDaftarWarga(keyword);
    }

    private void initLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(AppDesign.Colors.BG_PRIMARY);

        // --- Header Kiri ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppDesign.Colors.BG_PRIMARY);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Filter Bar (Periode + Tambah + Cari)
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.setBackground(AppDesign.Colors.BG_PRIMARY);

        JLabel lblPeriode = new JLabel("Periode:");
        lblPeriode.setFont(AppDesign.Typography.BODY);
        lblPeriode.setForeground(AppDesign.Colors.TEXT_PRIMARY);

        cbPeriode = new JComboBox<>();
        cbPeriode.setPreferredSize(new Dimension(120, 35));
        cbPeriode.setBackground(AppDesign.Colors.BG_TERTIARY);
        cbPeriode.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        cbPeriode.addActionListener(e -> {
            if (cbPeriode.getSelectedItem() != null) {
                selectedPeriode = cbPeriode.getSelectedItem().toString();
                // When period changes, we probably just load all or apply global search.
                // It's safer to just load empty. MainFrame maintains the global search.
                loadDaftarWarga("");
                resetForm();
            }
        });

        JButton btnTambahPeriode = new JButton("+ Periode Baru");
        btnTambahPeriode.setPreferredSize(new Dimension(130, 35));
        btnTambahPeriode.setBackground(AppDesign.Colors.ACCENT);
        btnTambahPeriode.setForeground(Color.WHITE);
        btnTambahPeriode.setFocusPainted(false);
        btnTambahPeriode.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTambahPeriode.putClientProperty("FlatLaf.style", "arc: 8; margin: 4,12,4,12");
        btnTambahPeriode.addActionListener(e -> tambahPeriode());

        filterPanel.add(lblPeriode);
        filterPanel.add(cbPeriode);
        filterPanel.add(btnTambahPeriode);

        headerPanel.add(filterPanel, BorderLayout.EAST);
        leftPanel.add(headerPanel, BorderLayout.NORTH);

        // --- Tabel Warga ---
        String[] columns = {"No", "NIK", "Nama", "Status", "ID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblDaftarWarga = new JTable(tableModel);
        tblDaftarWarga.setRowHeight(35);
        tblDaftarWarga.getTableHeader().setFont(AppDesign.Typography.CAPTION_BOLD);
        tblDaftarWarga.setFont(AppDesign.Typography.BODY);
        tblDaftarWarga.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblDaftarWarga.setSelectionBackground(AppDesign.Colors.ACCENT);
        tblDaftarWarga.setSelectionForeground(Color.WHITE);
        tblDaftarWarga.setRowSorter(new TableRowSorter<>(tableModel));
        
        tblDaftarWarga.getColumnModel().getColumn(4).setMinWidth(0);
        tblDaftarWarga.getColumnModel().getColumn(4).setMaxWidth(0);
        tblDaftarWarga.getColumnModel().getColumn(4).setWidth(0);
        
        tblDaftarWarga.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblDaftarWarga.getColumnModel().getColumn(1).setPreferredWidth(120);
        tblDaftarWarga.getColumnModel().getColumn(2).setPreferredWidth(180);
        tblDaftarWarga.getColumnModel().getColumn(3).setPreferredWidth(80);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblDaftarWarga.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblDaftarWarga.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(JLabel.CENTER);
                if (value != null) {
                    String val = value.toString();
                    if ("Sudah".equals(val)) {
                        label.setIcon(IconHelper.getIcon("circle-check", 16, AppDesign.Colors.SUCCESS));
                        if (!isSelected) label.setForeground(AppDesign.Colors.SUCCESS);
                    } else {
                        label.setIcon(IconHelper.getIcon("circle-x", 16, AppDesign.Colors.TEXT_MUTED));
                        if (!isSelected) label.setForeground(AppDesign.Colors.TEXT_MUTED);
                    }
                }
                return label;
            }
        };
        tblDaftarWarga.getColumnModel().getColumn(3).setCellRenderer(statusRenderer);

        tblDaftarWarga.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblDaftarWarga.getSelectedRow() != -1) {
                int row = tblDaftarWarga.getSelectedRow();
                int idWarga = (int) tableModel.getValueAt(row, 4);
                String nik = (String) tableModel.getValueAt(row, 1);
                String nama = (String) tableModel.getValueAt(row, 2);
                selectWarga(idWarga, nik, nama);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblDaftarWarga);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppDesign.Colors.BORDER_SUBTLE));
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        // --- Progress ---
        lblProgress = new JLabel("Progress: 0 / 0 warga sudah dinilai");
        lblProgress.setFont(AppDesign.Typography.CAPTION_BOLD);
        lblProgress.setForeground(AppDesign.Colors.TEXT_SECONDARY);
        lblProgress.setBorder(new EmptyBorder(10, 0, 0, 0));
        leftPanel.add(lblProgress, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.CENTER);
    }

    private void initRightPanel() {
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(AppDesign.Colors.BG_SECONDARY);
        rightPanel.setPreferredSize(new Dimension(450, 0));
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppDesign.Colors.BORDER),
            new EmptyBorder(20, 25, 20, 25)
        ));

        // --- Header Kanan ---
        JPanel rightHeader = new JPanel(new BorderLayout());
        rightHeader.setBackground(AppDesign.Colors.BG_SECONDARY);
        rightHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        lblWargaDipilih = new JLabel("Pilih warga dari tabel di sebelah kiri");
        lblWargaDipilih.setFont(AppDesign.Typography.HEADING_MD);
        lblWargaDipilih.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        rightHeader.add(lblWargaDipilih, BorderLayout.CENTER);
        
        rightPanel.add(rightHeader, BorderLayout.NORTH);

        // --- Form Dinamis ---
        dynamicFormPanel = new JPanel();
        dynamicFormPanel.setLayout(new BoxLayout(dynamicFormPanel, BoxLayout.Y_AXIS));
        dynamicFormPanel.setBackground(AppDesign.Colors.BG_SECONDARY);
        
        JScrollPane formScroll = new JScrollPane(dynamicFormPanel);
        formScroll.setBorder(null);
        formScroll.getVerticalScrollBar().setUnitIncrement(16);
        rightPanel.add(formScroll, BorderLayout.CENTER);

        // --- Buttons ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(AppDesign.Colors.BG_SECONDARY);
        btnPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        btnBatal = new JButton("Batal");
        btnBatal.setFocusPainted(false);
        btnBatal.putClientProperty("FlatLaf.style", "arc: 8; margin: 6,16,6,16");
        btnBatal.addActionListener(e -> resetForm());

        btnHapus = new JButton("Hapus Penilaian", IconHelper.getIcon("trash", 16, Color.WHITE));
        btnHapus.setBackground(AppDesign.Colors.DANGER);
        btnHapus.setForeground(Color.WHITE);
        btnHapus.setFocusPainted(false);
        btnHapus.putClientProperty("FlatLaf.style", "arc: 8; margin: 6,16,6,16");
        btnHapus.addActionListener(e -> hapusPenilaian());

        btnSimpan = new JButton("Simpan Penilaian", IconHelper.getIcon("device-floppy", 16, Color.WHITE));
        btnSimpan.setBackground(AppDesign.Colors.SUCCESS);
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        btnSimpan.putClientProperty("FlatLaf.style", "arc: 8; margin: 6,16,6,16");
        btnSimpan.addActionListener(e -> simpanPenilaian());

        btnPanel.add(btnHapus);
        btnPanel.add(btnBatal);
        btnPanel.add(btnSimpan);

        rightPanel.add(btnPanel, BorderLayout.SOUTH);
        
        // Sembunyikan form di awal
        rightPanel.setVisible(false);

        add(rightPanel, BorderLayout.EAST);
    }

    private void loadPeriodeDropdown() {
        cbPeriode.removeAllItems();
        List<String> list = penilaianDAO.getAllPeriode();
        if (list.isEmpty()) {
            list.add("2024-01"); // Default if none exists
        }
        for (String p : list) {
            cbPeriode.addItem(p);
        }
        if (cbPeriode.getItemCount() > 0) {
            cbPeriode.setSelectedIndex(0);
        }
    }

    private void tambahPeriode() {
        String input = JOptionPane.showInputDialog(this, 
            "Masukkan Periode Baru (Format: YYYY-MM):", 
            "Tambah Periode", 
            JOptionPane.PLAIN_MESSAGE);
            
        if (input != null && !input.trim().isEmpty()) {
            String regex = "^\\d{4}-\\d{2}$";
            if (!input.matches(regex)) {
                JOptionPane.showMessageDialog(this, "Format periode salah! Gunakan format YYYY-MM.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if already exists in dropdown
            boolean exists = false;
            for (int i = 0; i < cbPeriode.getItemCount(); i++) {
                if (cbPeriode.getItemAt(i).equals(input)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                cbPeriode.insertItemAt(input, 0);
            }
            cbPeriode.setSelectedItem(input);
        }
    }

    private void loadDaftarWarga(String keyword) {
        if (selectedPeriode == null || selectedPeriode.equals("-")) return;
        
        tableModel.setRowCount(0);
        List<Warga> list = keyword.isEmpty() ? wargaDAO.getAll() : wargaDAO.search(keyword);
        Map<Integer, Boolean> statusMap = penilaianDAO.getStatusPenilaianWarga(selectedPeriode);
        
        int no = 1;
        int sudahDinilai = 0;
        
        for (Warga w : list) {
            boolean dinilai = statusMap.getOrDefault(w.getId(), false);
            if (dinilai) sudahDinilai++;
            
            String statusStr = dinilai ? "Sudah" : "Belum";
            Object[] row = {
                no++,
                w.getNik(),
                w.getNama(),
                statusStr,
                w.getId()
            };
            tableModel.addRow(row);
        }
        
        lblProgress.setText("Progress: " + sudahDinilai + " / " + list.size() + " warga sudah dinilai");
    }

    private void resetForm() {
        rightPanel.setVisible(false);
        tblDaftarWarga.clearSelection();
        selectedWargaId = -1;
    }

    private void selectWarga(int idWarga, String nik, String nama) {
        selectedWargaId = idWarga;
        lblWargaDipilih.setText("Menilai: " + nama + " — " + nik);
        buildFormNilai();
        rightPanel.setVisible(true);
        animateRightPanelBorder();
    }

    private void animateRightPanelBorder() {
        Color endColor = AppDesign.Colors.BORDER;
        
        Timer timer = new Timer(15, null);
        long startTime = System.currentTimeMillis();
        long duration = 300;
        
        timer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            if (elapsed >= duration) {
                rightPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(endColor),
                    new EmptyBorder(20, 25, 20, 25)
                ));
                timer.stop();
            } else {
                float ratio = (float) elapsed / duration;
                int alpha = (int) (ratio * 255);
                Color currentColor = new Color(endColor.getRed(), endColor.getGreen(), endColor.getBlue(), alpha);
                rightPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(currentColor),
                    new EmptyBorder(20, 25, 20, 25)
                ));
            }
        });
        timer.start();
    }

    private void buildFormNilai() {
        dynamicFormPanel.removeAll();
        fieldMap.clear();
        
        List<KriteriaSAW> kriteriaList = kriteriaDAO.getAktif();
        Map<Integer, Double> existingNilai = penilaianDAO.getPenilaianWarga(selectedWargaId, selectedPeriode);
        
        for (KriteriaSAW k : kriteriaList) {
            JLabel lbl = new JLabel(k.getKode() + " — " + k.getNama() + " (" + k.getAtribut() + ")");
            lbl.setFont(AppDesign.Typography.BODY_BOLD);
            lbl.setForeground(AppDesign.Colors.TEXT_PRIMARY);
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // Menggunakan JSpinner untuk input angka yang lebih aman
            SpinnerNumberModel model = new SpinnerNumberModel(0.0, 0.0, 999999999.0, 1.0);
            JSpinner sp = new JSpinner(model);
            sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            sp.setAlignmentX(Component.LEFT_ALIGNMENT);
            sp.putClientProperty("FlatLaf.style", "arc: 8");
            JSpinner.NumberEditor editor = new JSpinner.NumberEditor(sp, "#,##0.##");
            sp.setEditor(editor);
            
            // Pre-fill if exists
            if (existingNilai.containsKey(k.getId())) {
                sp.setValue(existingNilai.get(k.getId()));
            }
            
            fieldMap.put(k.getId(), sp);
            
            dynamicFormPanel.add(lbl);
            dynamicFormPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            dynamicFormPanel.add(sp);
            dynamicFormPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
        btnHapus.setEnabled(!existingNilai.isEmpty());
        
        dynamicFormPanel.revalidate();
        dynamicFormPanel.repaint();
    }

    private void simpanPenilaian() {
        if (selectedWargaId == -1 || selectedPeriode.equals("-")) return;
        
        Map<Integer, Double> nilaiMap = new HashMap<>();
        for (Map.Entry<Integer, JSpinner> entry : fieldMap.entrySet()) {
            try {
                // Ensure value is double
                Object val = entry.getValue().getValue();
                double d = ((Number) val).doubleValue();
                if (d < 0) {
                    JOptionPane.showMessageDialog(this, "Nilai tidak boleh negatif!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                nilaiMap.put(entry.getKey(), d);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Format angka tidak valid!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
        if (penilaianDAO.savePenilaian(selectedWargaId, selectedPeriode, nilaiMap)) {
            loadDaftarWarga(currentKeyword);
            // Tetap pilih baris warga yang sama setelah simpan
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if ((int) tableModel.getValueAt(i, 4) == selectedWargaId) {
                    tblDaftarWarga.setRowSelectionInterval(i, i);
                    break;
                }
            }
            JOptionPane.showMessageDialog(this, "Penilaian berhasil disimpan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan penilaian.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusPenilaian() {
        if (selectedWargaId == -1 || selectedPeriode.equals("-")) return;
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus data penilaian warga ini untuk periode " + selectedPeriode + "?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (penilaianDAO.deletePenilaian(selectedWargaId, selectedPeriode)) {
                loadDaftarWarga(currentKeyword);
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus penilaian.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
