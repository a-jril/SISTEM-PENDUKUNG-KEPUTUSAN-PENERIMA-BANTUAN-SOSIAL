package com.spkbansos.view;

import com.spkbansos.controller.SAWController;
import com.spkbansos.dao.KriteriaDAO;
import com.spkbansos.dao.PenilaianDAO;
import com.spkbansos.dao.WargaDAO;
import com.spkbansos.model.HasilSAW;
import com.spkbansos.model.User;
import com.spkbansos.model.Warga;
import com.spkbansos.util.IconHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class DashboardPanel extends JPanel {
    
    private User currentUser;
    private JLabel lblTotalWargaValue;
    private JLabel lblKriteriaAktifValue;
    private JLabel lblSudahDinilaiValue;
    private JLabel lblBelumDinilaiValue;
    
    private JLabel lblTrendWarga;
    private JLabel lblTrendKriteria;
    private JLabel lblTrendSudah;
    private JLabel lblTrendBelum;
    
    private JLabel lblPeriode;
    private JProgressBar progressPenilaian;
    
    private BarChartPanel chartPanel;
    
    private DefaultTableModel tableModel;
    private JTable tblRecentWarga;
    
    private WargaDAO wargaDAO;
    private KriteriaDAO kriteriaDAO;
    private PenilaianDAO penilaianDAO;

    public DashboardPanel(User user) {
        this.currentUser = user;
        this.wargaDAO = new WargaDAO();
        this.kriteriaDAO = new KriteriaDAO();
        this.penilaianDAO = new PenilaianDAO();

        setLayout(new BorderLayout());
        setBackground(AppDesign.Colors.BG_PRIMARY);
        setBorder(new EmptyBorder(AppDesign.Dimensions.PAGE_PADDING, AppDesign.Dimensions.PAGE_PADDING, AppDesign.Dimensions.PAGE_PADDING, AppDesign.Dimensions.PAGE_PADDING));

        initUI();

        // Auto refresh when panel is shown
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshData();
            }
        });
    }

    private void initUI() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(AppDesign.Colors.BG_PRIMARY);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String nama = currentUser != null ? currentUser.getNamaLengkap() : "Administrator";
        JLabel lblSapaan = new JLabel("Selamat datang, " + nama + "!");
        lblSapaan.setFont(AppDesign.Typography.BODY);
        lblSapaan.setForeground(AppDesign.Colors.TEXT_SECONDARY);

        headerPanel.add(lblSapaan);
        
        // Cards Panel
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, AppDesign.Dimensions.GAP, 0));
        cardsPanel.setBackground(AppDesign.Colors.BG_PRIMARY);
        cardsPanel.setBorder(new EmptyBorder(25, 0, 25, 0));

        lblTotalWargaValue = createStatValueLabel();
        lblKriteriaAktifValue = createStatValueLabel();
        lblSudahDinilaiValue = createStatValueLabel();
        lblBelumDinilaiValue = createStatValueLabel();

        lblTrendWarga = createTrendLabel();
        lblTrendKriteria = createTrendLabel();
        lblTrendSudah = createTrendLabel();
        lblTrendBelum = createTrendLabel();

        cardsPanel.add(createStatCard("users", "Total Warga", lblTotalWargaValue, lblTrendWarga, AppDesign.Colors.ACCENT));
        cardsPanel.add(createStatCard("adjustments-horizontal", "Kriteria Aktif", lblKriteriaAktifValue, lblTrendKriteria, AppDesign.Colors.WARNING));
        cardsPanel.add(createStatCard("check", "Sudah Dinilai", lblSudahDinilaiValue, lblTrendSudah, AppDesign.Colors.SUCCESS));
        cardsPanel.add(createStatCard("x", "Belum Dinilai", lblBelumDinilaiValue, lblTrendBelum, AppDesign.Colors.DANGER));

        // Info Wrapper (Split Layout)
        JPanel infoWrapper = new JPanel(new GridLayout(1, 2, AppDesign.Dimensions.GAP, 0));
        infoWrapper.setBackground(AppDesign.Colors.BG_PRIMARY);

        // Kiri: Progress Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(AppDesign.Colors.BG_SECONDARY);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppDesign.Colors.BORDER, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        infoPanel.putClientProperty("FlatLaf.style", "arc: 12");

        lblPeriode = new JLabel("Periode aktif: Memuat...");
        lblPeriode.setFont(AppDesign.Typography.BODY);
        lblPeriode.setForeground(AppDesign.Colors.TEXT_PRIMARY);

        progressPenilaian = new JProgressBar(0, 100);
        progressPenilaian.setStringPainted(true);
        progressPenilaian.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        progressPenilaian.setBackground(AppDesign.Colors.BG_PRIMARY);
        progressPenilaian.setForeground(AppDesign.Colors.SUCCESS);

        JLabel lblInfo = new JLabel("Progres Penilaian Warga pada Periode Aktif");
        lblInfo.setFont(AppDesign.Typography.BODY_SM);
        lblInfo.setForeground(AppDesign.Colors.TEXT_SECONDARY);

        infoPanel.add(lblPeriode);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(lblInfo);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(progressPenilaian);
        
        // Kanan: Chart Panel
        chartPanel = new BarChartPanel();
        chartPanel.setBackground(AppDesign.Colors.BG_SECONDARY);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppDesign.Colors.BORDER, 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        chartPanel.putClientProperty("FlatLaf.style", "arc: 12");

        infoWrapper.add(infoPanel);
        infoWrapper.add(chartPanel);

        // Recent Warga Panel
        JPanel recentPanel = new JPanel(new BorderLayout());
        recentPanel.setBackground(AppDesign.Colors.BG_PRIMARY);
        recentPanel.setBorder(new EmptyBorder(25, 0, 0, 0));
        
        JLabel lblRecent = new JLabel("5 Warga Terbaru");
        lblRecent.setFont(AppDesign.Typography.HEADING_MD);
        lblRecent.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        lblRecent.setBorder(new EmptyBorder(0, 0, 10, 0));
        recentPanel.add(lblRecent, BorderLayout.NORTH);
        
        String[] columns = {"NIK", "Nama", "Kelurahan", "Status Penilaian"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblRecentWarga = new JTable(tableModel);
        tblRecentWarga.setRowHeight(30);
        tblRecentWarga.getTableHeader().setFont(AppDesign.Typography.CAPTION_BOLD);
        tblRecentWarga.setFont(AppDesign.Typography.BODY_SM);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblRecentWarga.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        
        tblRecentWarga.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(JLabel.CENTER);
                if ("Sudah".equals(value)) {
                    label.setForeground(AppDesign.Colors.SUCCESS);
                    label.setIcon(IconHelper.getIcon("circle-check", 14, AppDesign.Colors.SUCCESS));
                } else if ("Belum".equals(value)) {
                    label.setForeground(AppDesign.Colors.DANGER);
                    label.setIcon(IconHelper.getIcon("circle-x", 14, AppDesign.Colors.DANGER));
                } else {
                    label.setIcon(null);
                }
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblRecentWarga);
        scrollPane.setPreferredSize(new Dimension(0, 180));
        scrollPane.setBorder(BorderFactory.createLineBorder(AppDesign.Colors.BORDER_SUBTLE));
        recentPanel.add(scrollPane, BorderLayout.CENTER);

        // Top Wrapper
        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setBackground(AppDesign.Colors.BG_PRIMARY);
        topWrapper.add(headerPanel, BorderLayout.NORTH);
        topWrapper.add(cardsPanel, BorderLayout.CENTER);
        topWrapper.add(infoWrapper, BorderLayout.SOUTH);

        add(topWrapper, BorderLayout.NORTH);
        add(recentPanel, BorderLayout.CENTER);
    }

    private JLabel createStatValueLabel() {
        JLabel label = new JLabel("-");
        label.setFont(AppDesign.Typography.STAT_NUMBER);
        label.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        return label;
    }
    
    private JLabel createTrendLabel() {
        JLabel label = new JLabel("-");
        label.setFont(AppDesign.Typography.CAPTION);
        label.setForeground(AppDesign.Colors.TEXT_SECONDARY);
        return label;
    }

    private JPanel createStatCard(String iconName, String title, JLabel valueLabel, JLabel trendLabel, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(AppDesign.Colors.BG_SECONDARY);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppDesign.Colors.BORDER, 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        card.putClientProperty("FlatLaf.style", "arc: 12");

        // Icon Header
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        iconPanel.setBackground(AppDesign.Colors.BG_SECONDARY);
        
        Icon icon = IconHelper.getIcon(iconName, 24, accentColor);
        JLabel lblIcon = new JLabel(icon);
        iconPanel.add(lblIcon);
        
        JLabel lblTitle = new JLabel(" " + title);
        lblTitle.setFont(AppDesign.Typography.CAPTION_BOLD);
        lblTitle.setForeground(AppDesign.Colors.TEXT_SECONDARY);
        iconPanel.add(lblTitle);

        card.add(iconPanel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(valueLabel);
        
        if (trendLabel != null) {
            trendLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(Box.createRigidArea(new Dimension(0, 5)));
            card.add(trendLabel);
        }

        return card;
    }

    private void refreshData() {
        chartPanel.showMessage("Memuat data...");
        // Run in background worker
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            int totalWarga = 0;
            int kriteriaAktif = 0;
            int sudahDinilai = 0;
            int belumDinilai = 0;
            
            int prevSudahDinilai = 0;
            int prevBelumDinilai = 0;
            
            String periode = "-";
            List<Warga> recentWarga;
            List<HasilSAW> hasilList = null;
            String exceptionMessage = null;

            @Override
            protected Void doInBackground() throws Exception {
                totalWarga = wargaDAO.getCount();
                kriteriaAktif = kriteriaDAO.getCountAktif();
                
                periode = penilaianDAO.getLatestPeriode();
                if (!periode.equals("-")) {
                    sudahDinilai = penilaianDAO.getCountSudahDinilai(periode);
                    String prevPeriode = penilaianDAO.getPreviousPeriode(periode);
                    if (prevPeriode != null) {
                        prevSudahDinilai = penilaianDAO.getCountSudahDinilai(prevPeriode);
                    }
                    
                    try {
                        SAWController controller = new SAWController();
                        hasilList = controller.hitung(periode);
                    } catch (SAWController.SAWValidationException ex) {
                        exceptionMessage = "Data penilaian belum lengkap";
                    } catch (Exception ex) {
                        exceptionMessage = "Gagal memuat kelayakan";
                    }
                } else {
                    sudahDinilai = 0;
                    exceptionMessage = "Belum ada periode penilaian";
                }
                belumDinilai = totalWarga - sudahDinilai;
                prevBelumDinilai = totalWarga - prevSudahDinilai;

                recentWarga = wargaDAO.getRecent(5);
                return null;
            }

            @Override
            protected void done() {
                lblTotalWargaValue.setText(String.valueOf(totalWarga));
                lblTrendWarga.setText("Total keseluruhan");
                
                lblKriteriaAktifValue.setText(String.valueOf(kriteriaAktif));
                lblTrendKriteria.setText("Kriteria aktif saat ini");
                
                lblSudahDinilaiValue.setText(String.valueOf(sudahDinilai));
                int diffSudah = sudahDinilai - prevSudahDinilai;
                lblTrendSudah.setText((diffSudah >= 0 ? "+" : "") + diffSudah + " dari periode lalu");
                lblTrendSudah.setForeground(diffSudah >= 0 ? AppDesign.Colors.SUCCESS : AppDesign.Colors.WARNING);
                
                lblBelumDinilaiValue.setText(String.valueOf(belumDinilai));
                int diffBelum = belumDinilai - prevBelumDinilai;
                lblTrendBelum.setText((diffBelum > 0 ? "+" : "") + diffBelum + " dari periode lalu");
                lblTrendBelum.setForeground(diffBelum > 0 ? AppDesign.Colors.DANGER : AppDesign.Colors.SUCCESS);
                
                if (periode.equals("-")) {
                    lblPeriode.setText("Belum ada periode penilaian.");
                } else {
                    lblPeriode.setText("Periode aktif: " + periode);
                }
                
                // Update Progress
                if (totalWarga > 0) {
                    int percent = (int) (((double) sudahDinilai / totalWarga) * 100);
                    progressPenilaian.setValue(percent);
                    progressPenilaian.setString(percent + "% (" + sudahDinilai + "/" + totalWarga + ")");
                } else {
                    progressPenilaian.setValue(0);
                    progressPenilaian.setString("0%");
                }
                
                // Update Chart
                if (exceptionMessage != null) {
                    chartPanel.showMessage(exceptionMessage);
                } else if (hasilList != null) {
                    int layak = 0;
                    int tidak = 0;
                    for (HasilSAW h : hasilList) {
                        if (h.getSkorV() >= AppDesign.Config.KELAYAKAN_THRESHOLD) layak++;
                        else tidak++;
                    }
                    chartPanel.updateData(layak, tidak);
                }
                
                // Update Table
                tableModel.setRowCount(0);
                if (recentWarga != null) {
                    for (Warga w : recentWarga) {
                        boolean sudah = !periode.equals("-") && penilaianDAO.isDinilai(w.getId(), periode);
                        tableModel.addRow(new Object[]{w.getNik(), w.getNama(), w.getKelurahan(), sudah ? "Sudah" : "Belum"});
                    }
                }
            }
        };
        worker.execute();
    }
    
    // Inner class for Bar Chart
    class BarChartPanel extends JPanel {
        private int layakCount = 0;
        private int tidakLayakCount = 0;
        private String message = "Memuat data...";

        public void updateData(int layak, int tidakLayak) {
            this.layakCount = layak;
            this.tidakLayakCount = tidakLayak;
            this.message = null;
            repaint();
        }

        public void showMessage(String msg) {
            this.message = msg;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            g2.setColor(AppDesign.Colors.TEXT_PRIMARY);
            g2.setFont(AppDesign.Typography.BODY_BOLD);
            g2.drawString("Distribusi Kelayakan (SAW)", 20, 25);

            if (message != null) {
                g2.setColor(AppDesign.Colors.TEXT_SECONDARY);
                g2.setFont(AppDesign.Typography.BODY);
                FontMetrics fm = g2.getFontMetrics();
                int msgWidth = fm.stringWidth(message);
                g2.drawString(message, (width - msgWidth) / 2, height / 2 + 10);
                return;
            }

            int total = layakCount + tidakLayakCount;
            if (total == 0) {
                g2.setColor(AppDesign.Colors.TEXT_SECONDARY);
                g2.setFont(AppDesign.Typography.BODY);
                g2.drawString("Belum ada hasil kelayakan", width/2 - 60, height/2 + 10);
                return;
            }

            int maxBarWidth = Math.max(50, width - 120);
            int barHeight = 18;
            
            int yLayak = 55;
            int yTidak = 85;

            int wLayak = (int) (((double) layakCount / total) * maxBarWidth);
            int wTidak = (int) (((double) tidakLayakCount / total) * maxBarWidth);

            g2.setFont(AppDesign.Typography.BODY_SM);
            g2.setColor(AppDesign.Colors.TEXT_PRIMARY);
            g2.drawString("Layak", 20, yLayak + 14);
            g2.setColor(AppDesign.Colors.SUCCESS);
            g2.fillRoundRect(85, yLayak, wLayak, barHeight, 4, 4);
            g2.setColor(AppDesign.Colors.TEXT_SECONDARY);
            g2.drawString(String.valueOf(layakCount), 85 + wLayak + 5, yLayak + 14);

            g2.setColor(AppDesign.Colors.TEXT_PRIMARY);
            g2.drawString("Tdk Layak", 20, yTidak + 14);
            g2.setColor(AppDesign.Colors.DANGER);
            g2.fillRoundRect(85, yTidak, wTidak, barHeight, 4, 4);
            g2.setColor(AppDesign.Colors.TEXT_SECONDARY);
            g2.drawString(String.valueOf(tidakLayakCount), 85 + wTidak + 5, yTidak + 14);
        }
    }
}
