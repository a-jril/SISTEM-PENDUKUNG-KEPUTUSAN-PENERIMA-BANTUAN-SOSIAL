package com.spkbansos.view;

import com.spkbansos.model.User;
import com.spkbansos.util.IconHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {

    private User currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel lblNavbarTitle;
    private JLabel lblNavbarIcon;
    private JTextField txGlobalSearch;

    // Sidebar Buttons
    private JButton btnDashboard;
    private JButton btnWarga;
    private JButton btnKriteria;
    private JButton btnPenilaian;
    private JButton btnHasil;
    private JButton btnLaporan;
    private JButton btnLogout;

    // Array untuk iterasi tombol navigasi (tanpa logout)
    private JButton[] navButtons;

    private HasilKeputusanPanel hasilPanel;
    private LaporanPanel laporanPanel;

    public MainFrame(User user) {
        this.currentUser = user;

        setTitle("Sistem Pendukung Keputusan Bansos");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(960, 600));

        // Set app icon
        Image appIcon = IconHelper.getImage("app-window", 32);
        if (appIcon != null) {
            setIconImage(appIcon);
        }

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 0));

        // =============================================
        // 1. SIDEBAR
        // =============================================
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(AppDesign.Dimensions.SIDEBAR_WIDTH, 0));
        sidebar.setBackground(AppDesign.Colors.SIDEBAR_BG);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, AppDesign.Colors.SIDEBAR_BORDER));

        // --- Header: Nama Aplikasi + Info User ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(AppDesign.Colors.SIDEBAR_BG);
        headerPanel.setBorder(
                new EmptyBorder(20, AppDesign.Dimensions.SIDEBAR_PAD_H, 16, AppDesign.Dimensions.SIDEBAR_PAD_H));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // App title row with icon
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        titleRow.setBackground(AppDesign.Colors.SIDEBAR_BG);
        titleRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        Icon appIcon = IconHelper.getIcon("app-window", 22, AppDesign.Colors.ACCENT);
        if (appIcon != null) {
            JLabel iconLabel = new JLabel(appIcon);
            titleRow.add(iconLabel);
        }

        JLabel lblAppName = new JLabel("SPK BANSOS");
        lblAppName.setFont(AppDesign.Typography.SIDEBAR_APP_TITLE);
        lblAppName.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        titleRow.add(lblAppName);

        headerPanel.add(titleRow);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        sidebar.add(headerPanel);

        // --- Garis pemisah ---
        sidebar.add(createSeparator());

        // =============================================
        // SECTION 1: MENU UTAMA
        // =============================================
        sidebar.add(createSectionLabel("Menu Utama"));

        btnDashboard = createSidebarButton("Dashboard", "layout-dashboard");
        btnWarga = createSidebarButton("Data Warga", "users");
        btnKriteria = createSidebarButton("Kriteria SAW", "adjustments-horizontal");

        sidebar.add(btnDashboard);
        sidebar.add(Box.createRigidArea(new Dimension(0, 2)));
        sidebar.add(btnWarga);
        sidebar.add(Box.createRigidArea(new Dimension(0, 2)));
        sidebar.add(btnKriteria);

        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));

        // =============================================
        // SECTION 2: ANALISIS & LAPORAN
        // =============================================
        sidebar.add(createSectionLabel("Analisis & Laporan"));

        btnPenilaian = createSidebarButton("Input Penilaian", "clipboard-list");
        btnHasil = createSidebarButton("Hasil Keputusan", "chart-bar");
        btnLaporan = createSidebarButton("Laporan", "printer");

        sidebar.add(btnPenilaian);
        sidebar.add(Box.createRigidArea(new Dimension(0, 2)));
        sidebar.add(btnHasil);
        sidebar.add(Box.createRigidArea(new Dimension(0, 2)));
        sidebar.add(btnLaporan);

        // Kumpulkan tombol navigasi untuk iterasi
        navButtons = new JButton[] { btnDashboard, btnWarga, btnKriteria, btnPenilaian, btnHasil, btnLaporan };

        // --- Spacer ---
        sidebar.add(Box.createVerticalGlue());
        
        sidebar.add(createSeparator());
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));

        btnLogout = createSidebarButton("Logout", "logout");
        btnLogout.setForeground(AppDesign.Colors.DANGER);
        Icon logoutIcon = IconHelper.getIcon("logout", AppDesign.Dimensions.SIDEBAR_ICON_SIZE, AppDesign.Colors.DANGER);
        if (logoutIcon != null) {
            btnLogout.setIcon(logoutIcon);
        }
        sidebar.add(btnLogout);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        add(sidebar, BorderLayout.WEST);

        // =============================================
        // 2. RIGHT AREA (Navbar + Content)
        // =============================================
        JPanel rightArea = new JPanel(new BorderLayout());

        // --- Navbar ---
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setPreferredSize(new Dimension(0, AppDesign.Dimensions.NAVBAR_HEIGHT));
        navbar.setBackground(AppDesign.Colors.NAVBAR_BG);
        navbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppDesign.Colors.NAVBAR_BORDER));

        // Navbar Kiri: Judul Halaman
        JPanel navbarLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        navbarLeft.setBackground(AppDesign.Colors.NAVBAR_BG);
        navbarLeft.setBorder(new EmptyBorder((AppDesign.Dimensions.NAVBAR_HEIGHT - 24) / 2, 10, 0, 0));

        lblNavbarIcon = new JLabel();
        lblNavbarTitle = new JLabel("Dashboard");
        lblNavbarTitle.setFont(AppDesign.Typography.NAVBAR_TITLE);
        lblNavbarTitle.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        
        navbarLeft.add(lblNavbarIcon);
        navbarLeft.add(lblNavbarTitle);
        navbar.add(navbarLeft, BorderLayout.WEST);

        // Navbar Kanan: Global Search, Notification, User Profile
        JPanel navbarRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        navbarRight.setBackground(AppDesign.Colors.NAVBAR_BG);
        navbarRight.setBorder(new EmptyBorder((AppDesign.Dimensions.NAVBAR_HEIGHT - 35) / 2, 0, 0, 20));

        txGlobalSearch = new JTextField(20);
        txGlobalSearch.setPreferredSize(new Dimension(250, 35));
        txGlobalSearch.putClientProperty("JTextField.placeholderText", "Cari data...");
        txGlobalSearch.putClientProperty("FlatLaf.style", "arc: 18; margin: 0,12,0,12");
        txGlobalSearch.setBackground(AppDesign.Colors.BG_TERTIARY);
        txGlobalSearch.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        txGlobalSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                for (Component c : contentPanel.getComponents()) {
                    if (c.isVisible() && c instanceof Searchable) {
                        ((Searchable) c).onSearch(txGlobalSearch.getText().trim());
                        break;
                    }
                }
            }
        });
        txGlobalSearch.setVisible(false);

        JButton btnNotification = new JButton(IconHelper.getIcon("bell", 18, AppDesign.Colors.TEXT_SECONDARY));
        btnNotification.setPreferredSize(new Dimension(35, 35));
        btnNotification.setFocusPainted(false);
        btnNotification.setBorderPainted(false);
        btnNotification.setContentAreaFilled(false);
        btnNotification.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNotification.addActionListener(e -> new AboutDialog(MainFrame.this).setVisible(true));

        JButton btnProfile = new JButton(IconHelper.getIcon("user-circle", 24, AppDesign.Colors.TEXT_PRIMARY));
        btnProfile.setPreferredSize(new Dimension(35, 35));
        btnProfile.setFocusPainted(false);
        btnProfile.setBorderPainted(false);
        btnProfile.setContentAreaFilled(false);
        btnProfile.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnProfile.addActionListener(e -> {
            JPopupMenu popup = new JPopupMenu();
            
            JMenuItem itemGantiPwd = new JMenuItem("Ganti Password", IconHelper.getIcon("lock", 16, AppDesign.Colors.TEXT_PRIMARY));
            itemGantiPwd.addActionListener(ev -> new ChangePasswordDialog(MainFrame.this, currentUser).setVisible(true));
            
            JMenuItem itemLogout = new JMenuItem("Logout", IconHelper.getIcon("logout", 16, AppDesign.Colors.DANGER));
            itemLogout.setForeground(AppDesign.Colors.DANGER);
            itemLogout.addActionListener(ev -> btnLogout.doClick());
            
            JMenuItem headerItem = new JMenuItem("Halo, " + currentUser.getNamaLengkap());
            headerItem.setEnabled(false);
            
            popup.add(headerItem);
            popup.addSeparator();
            popup.add(itemGantiPwd);
            popup.addSeparator();
            popup.add(itemLogout);
            
            popup.show(btnProfile, 0, btnProfile.getHeight());
        });

        JLabel lblNavUserName = new JLabel(currentUser != null ? currentUser.getNamaLengkap() : "Administrator");
        lblNavUserName.setFont(AppDesign.Typography.BODY_BOLD);
        lblNavUserName.setForeground(AppDesign.Colors.TEXT_PRIMARY);

        navbarRight.add(txGlobalSearch);
        navbarRight.add(Box.createRigidArea(new Dimension(10, 0)));
        navbarRight.add(btnNotification);
        navbarRight.add(Box.createRigidArea(new Dimension(10, 0)));
        navbarRight.add(lblNavUserName);
        navbarRight.add(Box.createRigidArea(new Dimension(5, 0)));
        navbarRight.add(btnProfile);
        navbar.add(navbarRight, BorderLayout.EAST);
        
        rightArea.add(navbar, BorderLayout.NORTH);

        // =============================================
        // 2. CONTENT AREA (CardLayout)
        // =============================================
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(AppDesign.Colors.BG_PRIMARY);

        hasilPanel = new HasilKeputusanPanel(currentUser);
        contentPanel.add(new DashboardPanel(currentUser), "dashboard");
        contentPanel.add(new DataWargaPanel(), "warga");
        contentPanel.add(new KriteriaPanel(), "kriteria");
        contentPanel.add(new PenilaianPanel(), "penilaian");
        contentPanel.add(hasilPanel, "hasil");
        laporanPanel = new LaporanPanel();
        laporanPanel.setLoggedInUser(currentUser.getNamaLengkap());
        contentPanel.add(laporanPanel, "laporan");

        rightArea.add(contentPanel, BorderLayout.CENTER);
        add(rightArea, BorderLayout.CENTER);

        // =============================================
        // 3. ACTION LISTENERS
        // =============================================
        btnDashboard.addActionListener(e -> switchPanel("dashboard", btnDashboard));
        btnWarga.addActionListener(e -> switchPanel("warga", btnWarga));
        btnKriteria.addActionListener(e -> switchPanel("kriteria", btnKriteria));
        btnPenilaian.addActionListener(e -> switchPanel("penilaian", btnPenilaian));
        btnHasil.addActionListener(e -> {
            hasilPanel.refresh();
            switchPanel("hasil", btnHasil);
        });
        btnLaporan.addActionListener(e -> {
            laporanPanel.refreshData();
            switchPanel("laporan", btnLaporan);
        });

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Apakah Anda yakin ingin logout?", "Konfirmasi Logout",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginFrame().setVisible(true);
            }
        });

        // Default panel aktif
        switchPanel("dashboard", btnDashboard);
    }

    // =========================================================
    // FACTORY METHODS
    // =========================================================

    /**
     * Membuat label section untuk sidebar (contoh: "MENU UTAMA").
     */
    private JPanel createSectionLabel(String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, AppDesign.Dimensions.SIDEBAR_PAD_H + 10, 0));
        panel.setBackground(AppDesign.Colors.SIDEBAR_BG);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.setBorder(new EmptyBorder(8, 0, 4, 0));

        JLabel label = new JLabel(text);
        label.setFont(AppDesign.Typography.SIDEBAR_SECTION);
        label.setForeground(AppDesign.Colors.SIDEBAR_SECTION_TEXT);
        panel.add(label);

        return panel;
    }

    /**
     * Membuat tombol sidebar dengan icon SVG dan teks.
     */
    private JButton createSidebarButton(String text, String iconName) {
        // Muat icon
        Icon icon = IconHelper.getIcon(iconName, AppDesign.Dimensions.SIDEBAR_ICON_SIZE,
                AppDesign.Colors.TEXT_SECONDARY);

        JButton btn = new JButton(text);
        if (icon != null) {
            btn.setIcon(icon);
        }
        btn.setIconTextGap(10);
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, AppDesign.Dimensions.SIDEBAR_BTN_HEIGHT));
        btn.setPreferredSize(
                new Dimension(AppDesign.Dimensions.SIDEBAR_WIDTH - 24, AppDesign.Dimensions.SIDEBAR_BTN_HEIGHT));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.setFont(AppDesign.Typography.SIDEBAR_BUTTON);
        btn.setForeground(AppDesign.Colors.TEXT_SECONDARY);
        btn.setBackground(AppDesign.Colors.SIDEBAR_BTN_DEFAULT);

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setBorder(new EmptyBorder(0, AppDesign.Dimensions.SIDEBAR_PAD_H, 0, AppDesign.Dimensions.SIDEBAR_PAD_H));

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!btn.isSelected()) {
                    btn.setBackground(AppDesign.Colors.SIDEBAR_BTN_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!btn.isSelected()) {
                    btn.setBackground(AppDesign.Colors.SIDEBAR_BTN_DEFAULT);
                }
            }
        });

        return btn;
    }

    // Removed createNavbarLogoutButton

    /**
     * Membuat garis pemisah halus di sidebar.
     */
    private JSeparator createSeparator() {
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(AppDesign.Colors.SIDEBAR_BORDER);
        sep.setBackground(AppDesign.Colors.SIDEBAR_BG);
        return sep;
    }

    // =========================================================
    // NAVIGATION
    // =========================================================

    /**
     * Mengatur visibilitas global search bar.
     */
    public void setSearchVisible(boolean visible) {
        if (txGlobalSearch != null) {
            txGlobalSearch.setVisible(visible);
        }
    }

    /**
     * Berpindah panel dan highlight tombol sidebar yang aktif.
     */
    private void switchPanel(String cardName, JButton activeButton) {
        cardLayout.show(contentPanel, cardName);

        for (Component c : contentPanel.getComponents()) {
            if (c.isVisible()) {
                setSearchVisible(c instanceof Searchable);
                if (txGlobalSearch != null) {
                    txGlobalSearch.setText("");
                }
                break;
            }
        }

        // Reset semua tombol ke state default
        for (JButton btn : navButtons) {
            btn.setSelected(false);
            btn.setBackground(AppDesign.Colors.SIDEBAR_BTN_DEFAULT);
            btn.setForeground(AppDesign.Colors.TEXT_SECONDARY);

            // Reset icon ke warna secondary
            // (Tidak perlu update icon secara manual, karena hover/active dihandle oleh
            // warna button)
        }

        // Highlight tombol aktif
        if (activeButton != null) {
            activeButton.setSelected(true);
            activeButton.setBackground(AppDesign.Colors.SIDEBAR_BTN_ACTIVE);
            activeButton.setForeground(AppDesign.Colors.TEXT_PRIMARY);

            // Update icon ke warna primary (terang)
            String iconName = getIconNameForButton(activeButton);
            if (iconName != null) {
                Icon activeIcon = IconHelper.getIcon(iconName, AppDesign.Dimensions.SIDEBAR_ICON_SIZE,
                        AppDesign.Colors.TEXT_PRIMARY);
                if (activeIcon != null) {
                    activeButton.setIcon(activeIcon);
                    lblNavbarIcon.setIcon(activeIcon);
                }
                lblNavbarTitle.setText(activeButton.getText());
            }

            // Reset icon tombol lain ke warna secondary
            for (JButton btn : navButtons) {
                if (btn != activeButton) {
                    String otherIconName = getIconNameForButton(btn);
                    if (otherIconName != null) {
                        Icon secondaryIcon = IconHelper.getIcon(otherIconName, AppDesign.Dimensions.SIDEBAR_ICON_SIZE,
                                AppDesign.Colors.TEXT_SECONDARY);
                        if (secondaryIcon != null) {
                            btn.setIcon(secondaryIcon);
                        }
                    }
                }
            }
        }
    }

    /**
     * Mengembalikan nama icon berdasarkan tombol.
     */
    private String getIconNameForButton(JButton btn) {
        if (btn == btnDashboard)
            return "layout-dashboard";
        if (btn == btnWarga)
            return "users";
        if (btn == btnKriteria)
            return "adjustments-horizontal";
        if (btn == btnPenilaian)
            return "clipboard-list";
        if (btn == btnHasil)
            return "chart-bar";
        if (btn == btnLaporan)
            return "printer";
        return null;
    }
}