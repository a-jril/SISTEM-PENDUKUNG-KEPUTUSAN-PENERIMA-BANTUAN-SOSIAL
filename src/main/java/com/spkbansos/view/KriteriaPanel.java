package com.spkbansos.view;

import com.spkbansos.dao.KriteriaDAO;
import com.spkbansos.model.KriteriaSAW;
import com.spkbansos.util.IconHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class KriteriaPanel extends JPanel {

    private KriteriaDAO kriteriaDAO;
    private JTable tblKriteria;
    private DefaultTableModel tableModel;
    private JLabel lblTotalBobot;
    
    private JPanel warningBanner;
    private JLabel lblWarningText;
    
    private JButton btnTambah;
    private EmptyStateOverlay emptyOverlay;

    public KriteriaPanel() {
        this.kriteriaDAO = new KriteriaDAO();
        
        setLayout(new BorderLayout());
        setBackground(AppDesign.Colors.BG_PRIMARY);
        setBorder(new EmptyBorder(AppDesign.Dimensions.PAGE_PADDING, AppDesign.Dimensions.PAGE_PADDING, AppDesign.Dimensions.PAGE_PADDING, AppDesign.Dimensions.PAGE_PADDING));

        initUI();
        loadData();
    }

    private void initUI() {
        // --- Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppDesign.Colors.BG_PRIMARY);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        headerRight.setBackground(AppDesign.Colors.BG_PRIMARY);
        lblTotalBobot = new JLabel();
        lblTotalBobot.setFont(AppDesign.Typography.BODY_BOLD);
        
        btnTambah = createButton("Tambah Kriteria", "plus", AppDesign.Colors.ACCENT);
        btnTambah.addActionListener(e -> tambahKriteria());
        
        headerRight.add(lblTotalBobot);
        headerRight.add(btnTambah);

        headerPanel.add(headerRight, BorderLayout.EAST);
        
        // --- Warning Banner ---
        warningBanner = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        Color warningBg = new Color(210, 153, 34, 30); // subtle warning background
        warningBanner.setBackground(warningBg);
        warningBanner.setBorder(BorderFactory.createMatteBorder(0, 4, 0, 0, AppDesign.Colors.WARNING));
        
        lblWarningText = new JLabel(" Total bobot kriteria saat ini belum 100%. Perhitungan SAW tidak dapat dilakukan.");
        lblWarningText.setFont(AppDesign.Typography.BODY_BOLD);
        lblWarningText.setForeground(AppDesign.Colors.WARNING);
        lblWarningText.setIcon(IconHelper.getIcon("alert-triangle", 18, AppDesign.Colors.WARNING));
        
        warningBanner.add(lblWarningText);
        warningBanner.setVisible(false);
        
        headerPanel.add(warningBanner, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // --- Table ---
        String[] columns = {"No", "Kode", "Nama Kriteria", "Atribut", "Bobot (%)", "Status", "Aksi", "ID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        tblKriteria = new JTable(tableModel);
        tblKriteria.setRowHeight(35);
        tblKriteria.getTableHeader().setFont(AppDesign.Typography.CAPTION_BOLD);
        tblKriteria.setFont(AppDesign.Typography.BODY);
        tblKriteria.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblKriteria.setRowSorter(new TableRowSorter<>(tableModel));
        
        // Hide ID column
        tblKriteria.getColumnModel().getColumn(7).setMinWidth(0);
        tblKriteria.getColumnModel().getColumn(7).setMaxWidth(0);
        tblKriteria.getColumnModel().getColumn(7).setWidth(0);
        
        // Set column widths
        tblKriteria.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblKriteria.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblKriteria.getColumnModel().getColumn(2).setPreferredWidth(300);
        tblKriteria.getColumnModel().getColumn(3).setPreferredWidth(150);
        tblKriteria.getColumnModel().getColumn(4).setPreferredWidth(150);
        tblKriteria.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblKriteria.getColumnModel().getColumn(6).setPreferredWidth(120);
        tblKriteria.getColumnModel().getColumn(6).setMinWidth(120);
        tblKriteria.getColumnModel().getColumn(6).setMaxWidth(120);
        
        // Center alignment for some columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblKriteria.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblKriteria.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tblKriteria.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tblKriteria.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tblKriteria.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(tblKriteria);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppDesign.Colors.BORDER_SUBTLE));
        add(scrollPane, BorderLayout.CENTER);
        
        emptyOverlay = EmptyStateOverlay.install(this, scrollPane, "adjustments-horizontal", "Belum ada kriteria. Klik 'Tambah Kriteria' untuk memulai.");

        TableActionListener event = new TableActionListener() {
            @Override
            public void onEdit(int row) {
                editKriteria(row);
            }
            @Override
            public void onDelete(int row) {
                hapusKriteria(row);
            }
            @Override
            public void onToggle(int row) {
                toggleStatus(row);
            }
        };
        tblKriteria.getColumnModel().getColumn(6).setCellRenderer(new ActionCellRenderer(true));
        tblKriteria.getColumnModel().getColumn(6).setCellEditor(new ActionCellEditor(true, event));
    }

    private JButton createButton(String text, String iconName, Color bgColor) {
        JButton btn = new JButton(text);
        Icon icon = IconHelper.getIcon(iconName, 18, Color.WHITE);
        if (icon != null) {
            btn.setIcon(icon);
        }
        btn.setFont(AppDesign.Typography.BODY_SM);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty("FlatLaf.style", "arc: 8; margin: 4,12,4,12");
        return btn;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<KriteriaSAW> list = kriteriaDAO.getAll();
        
        int no = 1;
        for (KriteriaSAW k : list) {
            String status = k.getIsAktif() == 1 ? "Aktif" : "Nonaktif";
            Object[] row = {
                no++,
                k.getKode(),
                k.getNama(),
                k.getAtribut(),
                k.getBobot(),
                status,
                "",
                k.getId()
            };
            tableModel.addRow(row);
        }
        
        updateTotalBobot();
        if (emptyOverlay != null) emptyOverlay.updateState(tableModel.getRowCount());
    }

    private void updateTotalBobot() {
        double total = kriteriaDAO.getTotalBobotAktif();
        lblTotalBobot.setText(String.format("Total Bobot Aktif: %.1f%% / 100%%", total));
        
        if (Math.abs(total - 100.0) < 0.01) {
            lblTotalBobot.setForeground(AppDesign.Colors.SUCCESS);
            lblTotalBobot.setIcon(IconHelper.getIcon("circle-check", 20, AppDesign.Colors.SUCCESS));
            warningBanner.setVisible(false);
        } else {
            lblTotalBobot.setForeground(AppDesign.Colors.DANGER);
            lblTotalBobot.setIcon(IconHelper.getIcon("alert-triangle", 20, AppDesign.Colors.DANGER));
            lblWarningText.setText(String.format(" Total bobot kriteria saat ini %.1f%%. Harus tepat 100%% untuk perhitungan SAW.", total));
            warningBanner.setVisible(true);
        }
    }

    private void tambahKriteria() {
        Window window = SwingUtilities.getWindowAncestor(this);
        KriteriaFormDialog dialog = new KriteriaFormDialog((Frame) window, "Tambah Kriteria", null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }

    private void editKriteria(int selectedRow) {
        if (selectedRow == -1) return;

        int id = (int) tableModel.getValueAt(selectedRow, 7);
        
        // Cari objek dari DB
        List<KriteriaSAW> list = kriteriaDAO.getAll();
        KriteriaSAW selected = null;
        for (KriteriaSAW k : list) {
            if (k.getId() == id) {
                selected = k;
                break;
            }
        }
        
        if (selected != null) {
            Window window = SwingUtilities.getWindowAncestor(this);
            KriteriaFormDialog dialog = new KriteriaFormDialog((Frame) window, "Edit Kriteria", selected);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                loadData();
            }
        }
    }

    private void hapusKriteria(int selectedRow) {
        if (selectedRow == -1) return;

        int id = (int) tableModel.getValueAt(selectedRow, 7);
        String kode = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus kriteria " + kode + "?\nPenilaian yang terkait dengan kriteria ini juga akan ikut terhapus.",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (kriteriaDAO.delete(id)) {
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data kriteria.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void toggleStatus(int selectedRow) {
        if (selectedRow == -1) return;

        int id = (int) tableModel.getValueAt(selectedRow, 7);
        String statusStr = (String) tableModel.getValueAt(selectedRow, 5);
        int currentStatus = statusStr.equals("Aktif") ? 1 : 0;
        int newStatus = currentStatus == 1 ? 0 : 1;
        
        if (kriteriaDAO.toggleStatus(id, newStatus)) {
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengubah status kriteria.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
