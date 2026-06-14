package com.spkbansos.view;

import com.spkbansos.dao.WargaDAO;
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
import java.io.File;
import java.util.List;

public class DataWargaPanel extends JPanel implements Searchable {

    private final WargaDAO wargaDAO;
    private JTable tblWarga;
    private DefaultTableModel tableModel;
    private EmptyStateOverlay emptyOverlay;

    private JButton btnTambah;
    private JButton btnImport;
    private JButton btnExport;
    private String currentKeyword = "";

    public DataWargaPanel() {
        this.wargaDAO = new WargaDAO();

        setLayout(new BorderLayout());
        setBackground(AppDesign.Colors.BG_PRIMARY);
        setBorder(new EmptyBorder(AppDesign.Dimensions.PAGE_PADDING, AppDesign.Dimensions.PAGE_PADDING,
                AppDesign.Dimensions.PAGE_PADDING, AppDesign.Dimensions.PAGE_PADDING));

        initUI();
        loadData("");
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                // If needed, refresh data
                // We rely on MainFrame passing search via onSearch
                loadData("");
            }
        });
    }

    @Override
    public void onSearch(String keyword) {
        this.currentKeyword = keyword;
        loadData(keyword);
    }

    private void initUI() {
        // --- Header & Toolbar ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(AppDesign.Colors.BG_PRIMARY);
        topPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Search and Import/Export
        JPanel searchExportPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchExportPanel.setBackground(AppDesign.Colors.BG_PRIMARY);

        btnTambah = createButton("Tambah Warga", "plus", AppDesign.Colors.ACCENT);
        btnTambah.addActionListener(e -> tambahWarga());
        btnImport = createButton("Import Excel", "file-import", AppDesign.Colors.ACCENT);
        btnExport = createButton("Export Excel", "file-export", AppDesign.Colors.SUCCESS);

        searchExportPanel.add(btnTambah);
        searchExportPanel.add(btnImport);
        searchExportPanel.add(btnExport);

        topPanel.add(searchExportPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // --- Table ---
        String[] columns = { "No", "NIK", "Nama", "L/P", "RT/RW", "Kelurahan", "No Telp", "Aksi", "ID" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };

        tblWarga = new JTable(tableModel);
        tblWarga.setRowHeight(35);
        tblWarga.getTableHeader().setFont(AppDesign.Typography.CAPTION_BOLD);
        tblWarga.setFont(AppDesign.Typography.BODY);
        tblWarga.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblWarga.setRowSorter(new TableRowSorter<>(tableModel));

        // Hide ID column
        tblWarga.getColumnModel().getColumn(8).setMinWidth(0);
        tblWarga.getColumnModel().getColumn(8).setMaxWidth(0);
        tblWarga.getColumnModel().getColumn(8).setWidth(0);

        // Set column widths
        tblWarga.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblWarga.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblWarga.getColumnModel().getColumn(2).setPreferredWidth(250);
        tblWarga.getColumnModel().getColumn(3).setPreferredWidth(80);
        tblWarga.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblWarga.getColumnModel().getColumn(5).setPreferredWidth(150);
        tblWarga.getColumnModel().getColumn(6).setPreferredWidth(150);
        tblWarga.getColumnModel().getColumn(7).setPreferredWidth(100);
        tblWarga.getColumnModel().getColumn(7).setMinWidth(100);
        tblWarga.getColumnModel().getColumn(7).setMaxWidth(100);

        // Alignments
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblWarga.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblWarga.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tblWarga.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tblWarga.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(tblWarga);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppDesign.Colors.BORDER_SUBTLE));
        add(scrollPane, BorderLayout.CENTER);
        
        emptyOverlay = EmptyStateOverlay.install(this, scrollPane, "database-off", "Belum ada data warga. Klik 'Tambah Warga' untuk memulai.");

        TableActionListener event = new TableActionListener() {
            @Override
            public void onEdit(int row) {
                editWarga(row);
            }

            @Override
            public void onDelete(int row) {
                hapusWarga(row);
            }
        };
        tblWarga.getColumnModel().getColumn(7).setCellRenderer(new ActionCellRenderer(false));
        tblWarga.getColumnModel().getColumn(7).setCellEditor(new ActionCellEditor(false, event));

        btnImport.addActionListener(e -> importExcel());
        btnExport.addActionListener(e -> exportExcel());
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

    private void loadData(String keyword) {
        tableModel.setRowCount(0);
        List<Warga> list = keyword.isEmpty() ? wargaDAO.getAll() : wargaDAO.search(keyword);

        int no = 1;
        for (Warga w : list) {
            String jk = w.getJenisKelamin() != null && w.getJenisKelamin().startsWith("Laki") ? "L" : "P";
            Object[] row = {
                    no++,
                    w.getNik(),
                    w.getNama(),
                    jk,
                    w.getRtRw(),
                    w.getKelurahan(),
                    w.getNoTelp(),
                    "",
                    w.getId()
            };
            tableModel.addRow(row);
        }
        if (emptyOverlay != null) emptyOverlay.updateState(tableModel.getRowCount());
    }

    private void tambahWarga() {
        Window window = SwingUtilities.getWindowAncestor(this);
        WargaFormDialog dialog = new WargaFormDialog((Frame) window, "Tambah Warga", null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData(currentKeyword);
        }
    }

    private void editWarga(int selectedRow) {
        if (selectedRow == -1)
            return;

        int id = (int) tableModel.getValueAt(selectedRow, 8);
        Warga selected = wargaDAO.getById(id);

        if (selected != null) {
            Window window = SwingUtilities.getWindowAncestor(this);
            WargaFormDialog dialog = new WargaFormDialog((Frame) window, "Edit Warga", selected);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                loadData(currentKeyword);
            }
        }
    }

    private void hapusWarga(int selectedRow) {
        if (selectedRow == -1)
            return;

        int id = (int) tableModel.getValueAt(selectedRow, 8);
        String nik = (String) tableModel.getValueAt(selectedRow, 1);
        String nama = (String) tableModel.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus data warga " + nama + " (" + nik
                        + ")?\nData penilaian terkait juga akan terhapus.",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (wargaDAO.delete(id)) {
                loadData(currentKeyword);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data warga.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void importExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Pilih File Excel");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files", "xlsx", "xls"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Tampilkan dialog progress sederhana
            JDialog progressDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Memproses Import",
                    true);
            progressDialog.setLayout(new BorderLayout(10, 10));
            progressDialog.setSize(300, 100);
            progressDialog.setLocationRelativeTo(this);

            JLabel lblProgress = new JLabel("Sedang mengimport data, harap tunggu...", SwingConstants.CENTER);
            progressDialog.add(lblProgress, BorderLayout.CENTER);
            progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

            SwingWorker<Integer, Void> worker = new SwingWorker<Integer, Void>() {
                @Override
                protected Integer doInBackground() throws Exception {
                    return wargaDAO.importFromExcel(selectedFile);
                }

                @Override
                protected void done() {
                    progressDialog.dispose();
                    try {
                        int count = get();
                        if (count >= 0) {
                            JOptionPane.showMessageDialog(DataWargaPanel.this,
                                    "Berhasil mengimport " + count + " data warga.",
                                    "Import Sukses", JOptionPane.INFORMATION_MESSAGE);
                            loadData(currentKeyword);
                        } else {
                            JOptionPane.showMessageDialog(DataWargaPanel.this,
                                    "Gagal mengimport data. Pastikan format file Excel benar.",
                                    "Error Import", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(DataWargaPanel.this,
                                "Terjadi kesalahan saat import data.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };

            worker.execute();
            progressDialog.setVisible(true);
        }
    }

    private void exportExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan File Excel");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files", "xlsx"));
        fileChooser.setSelectedFile(new File("Data_Warga_SPK_Bansos.xlsx"));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".xlsx")) {
                fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".xlsx");
            }
            
            JDialog progressDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Memproses Export", true);
            progressDialog.setLayout(new BorderLayout(10, 10));
            progressDialog.setSize(300, 100);
            progressDialog.setLocationRelativeTo(this);
            JLabel lblProgress = new JLabel("Sedang mengekspor data, harap tunggu...", SwingConstants.CENTER);
            progressDialog.add(lblProgress, BorderLayout.CENTER);
            progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            
            File finalFileToSave = fileToSave;
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return wargaDAO.exportToExcel(finalFileToSave);
                }

                @Override
                protected void done() {
                    progressDialog.dispose();
                    try {
                        if (get()) {
                            JOptionPane.showMessageDialog(DataWargaPanel.this, "Berhasil mengekspor data warga ke Excel.", "Export Sukses", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(DataWargaPanel.this, "Gagal mengekspor data ke Excel.", "Error Export", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(DataWargaPanel.this, "Terjadi kesalahan saat export data.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };

            worker.execute();
            progressDialog.setVisible(true);
        }
    }
}
