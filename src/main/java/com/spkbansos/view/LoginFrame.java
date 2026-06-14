package com.spkbansos.view;

import com.spkbansos.dao.UserDAO;
import com.spkbansos.model.User;
import com.spkbansos.util.IconHelper;
import com.spkbansos.util.PasswordUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.prefs.Preferences;

public class LoginFrame extends JFrame {

    private JTextField txUsername;
    private JPasswordField txPassword;
    private JButton btnLogin;
    private JLabel lblError;
    private JButton btnTogglePassword;
    private JCheckBox chkRemember;
    private boolean isPasswordVisible = false;

    private UserDAO userDAO;
    private Preferences prefs;

    public LoginFrame() {
        this.userDAO = new UserDAO();
        this.prefs = Preferences.userNodeForPackage(LoginFrame.class);

        setTitle(AppDesign.Config.APP_NAME + " - Login");
        setSize(1000, 600);
        setMinimumSize(new Dimension(800, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().setBackground(AppDesign.Colors.BG_PRIMARY);

        Image appIcon = IconHelper.getImage("app-window", 32);
        if (appIcon != null) {
            setIconImage(appIcon);
        }

        initUI();
        loadPreferences();
    }

    private void initUI() {
        // Main container using GridLayout for 50/50 split
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBackground(AppDesign.Colors.BG_PRIMARY);

        // --- Left Panel (Branding) ---
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                    0, 0, AppDesign.Colors.LOGIN_BRAND_GRADIENT_START,
                    getWidth(), getHeight(), AppDesign.Colors.LOGIN_BRAND_GRADIENT_END);
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Decorative circles
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillOval(-100, -100, 300, 300);
                g2.fillOval(getWidth() - 150, getHeight() - 200, 400, 400);

                g2.dispose();
            }
        };
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EmptyBorder(60, 40, 40, 40));

        // App Logo
        JLabel lblLogo = new JLabel();
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            URL logoUrl = getClass().getResource("/images/logo_spkbansos.png");
            if (logoUrl != null) {
                ImageIcon originalIcon = new ImageIcon(logoUrl);
                Image scaledImage = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(scaledImage));
            } else {
                lblLogo.setIcon(IconHelper.getIcon("layout-dashboard", 80, Color.WHITE));
            }
        } catch (Exception e) {
            lblLogo.setIcon(IconHelper.getIcon("layout-dashboard", 80, Color.WHITE));
        }

        JLabel lblBrandTitle = new JLabel(AppDesign.Config.APP_NAME);
        lblBrandTitle.setFont(new Font(AppDesign.Typography.FONT_FAMILY, Font.BOLD, 36));
        lblBrandTitle.setForeground(Color.WHITE);
        lblBrandTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblBrandSub = new JLabel("Sistem Pendukung Keputusan Penerima Bansos");
        lblBrandSub.setFont(AppDesign.Typography.BODY);
        lblBrandSub.setForeground(new Color(255, 255, 255, 200));
        lblBrandSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Illustration
        JLabel lblIllustration = new JLabel();
        lblIllustration.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            URL illUrl = getClass().getResource("/images/illustration_login.png");
            if (illUrl != null) {
                ImageIcon originalIcon = new ImageIcon(illUrl);
                Image scaledImage = originalIcon.getImage().getScaledInstance(300, 250, Image.SCALE_SMOOTH);
                lblIllustration.setIcon(new ImageIcon(scaledImage));
            }
        } catch (Exception e) {
            // No illustration fallback
        }

        leftPanel.add(lblLogo);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(lblBrandTitle);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(lblBrandSub);
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(lblIllustration);
        leftPanel.add(Box.createVerticalGlue());

        // --- Right Panel (Login Form) ---
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(AppDesign.Colors.LOGIN_CARD_BG);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(AppDesign.Colors.LOGIN_CARD_BG);
        formPanel.setBorder(new EmptyBorder(40, 60, 40, 60));
        formPanel.setPreferredSize(new Dimension(450, 500));

        JLabel lblWelcome = new JLabel("Selamat Datang!");
        lblWelcome.setFont(AppDesign.Typography.HEADING_XL);
        lblWelcome.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        lblWelcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblInstruction = new JLabel("Silakan masuk ke akun Anda.");
        lblInstruction.setFont(AppDesign.Typography.BODY);
        lblInstruction.setForeground(AppDesign.Colors.TEXT_SECONDARY);
        lblInstruction.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Username Field
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(AppDesign.Typography.CAPTION_BOLD);
        lblUsername.setForeground(AppDesign.Colors.TEXT_SECONDARY);
        lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);

        txUsername = new JTextField();
        txUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txUsername.setPreferredSize(new Dimension(300, 45));
        txUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        txUsername.putClientProperty("FlatLaf.style", 
            "arc: 8; margin: 0,10,0,10; borderColor: #30363d; focusedBorderColor: #3884f4; background: #212936"
        );
        txUsername.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        txUsername.setCaretColor(AppDesign.Colors.TEXT_PRIMARY);

        // Password Field
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(AppDesign.Typography.CAPTION_BOLD);
        lblPassword.setForeground(AppDesign.Colors.TEXT_SECONDARY);
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        txPassword = new JPasswordField();
        txPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txPassword.setPreferredSize(new Dimension(300, 45));
        txPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        txPassword.putClientProperty("FlatLaf.style", 
            "arc: 8; margin: 0,10,0,10; borderColor: #30363d; focusedBorderColor: #3884f4; background: #212936"
        );
        txPassword.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        txPassword.setCaretColor(AppDesign.Colors.TEXT_PRIMARY);

        btnTogglePassword = new JButton(IconHelper.getIcon("eye", 18, AppDesign.Colors.TEXT_SECONDARY));
        btnTogglePassword.setBorderPainted(false);
        btnTogglePassword.setContentAreaFilled(false);
        btnTogglePassword.setFocusPainted(false);
        btnTogglePassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTogglePassword.addActionListener(e -> togglePasswordVisibility());
        txPassword.putClientProperty("JTextField.trailingComponent", btnTogglePassword);

        // Remember Me & Error Label Row
        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.setBackground(AppDesign.Colors.LOGIN_CARD_BG);
        optionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        optionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        chkRemember = new JCheckBox("Ingat Saya");
        chkRemember.setFont(AppDesign.Typography.BODY_SM);
        chkRemember.setForeground(AppDesign.Colors.TEXT_SECONDARY);
        chkRemember.setBackground(AppDesign.Colors.LOGIN_CARD_BG);
        chkRemember.setFocusPainted(false);
        
        lblError = new JLabel(" ");
        lblError.setFont(AppDesign.Typography.CAPTION);
        lblError.setForeground(AppDesign.Colors.DANGER);
        lblError.setHorizontalAlignment(SwingConstants.RIGHT);
        lblError.setVisible(false);

        optionsPanel.add(chkRemember, BorderLayout.WEST);
        optionsPanel.add(lblError, BorderLayout.EAST);

        // Login Button
        btnLogin = new JButton("Masuk");
        btnLogin.setFont(AppDesign.Typography.BODY_BOLD);
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnLogin.setPreferredSize(new Dimension(300, 45));
        btnLogin.setBackground(AppDesign.Colors.ACCENT);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.putClientProperty("FlatLaf.style", "arc: 8; margin: 0,10,0,10;");
        btnLogin.addActionListener(e -> attemptLogin());

        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if(btnLogin.isEnabled()) btnLogin.setBackground(AppDesign.Colors.ACCENT_HOVER);
            }
            public void mouseExited(MouseEvent e) {
                if(btnLogin.isEnabled()) btnLogin.setBackground(AppDesign.Colors.ACCENT);
            }
        });

        // Register Row
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        registerPanel.setBackground(AppDesign.Colors.LOGIN_CARD_BG);
        registerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel lblNoAccount = new JLabel("Belum punya akun?");
        lblNoAccount.setFont(AppDesign.Typography.BODY_SM);
        lblNoAccount.setForeground(AppDesign.Colors.TEXT_SECONDARY);

        JButton btnRegister = new JButton("Buat Akun");
        btnRegister.setFont(AppDesign.Typography.BODY_BOLD);
        btnRegister.setForeground(AppDesign.Colors.ACCENT);
        btnRegister.setBorderPainted(false);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setFocusPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.setMargin(new Insets(0, 0, 0, 0));
        btnRegister.addActionListener(e -> new RegisterDialog(LoginFrame.this).setVisible(true));

        registerPanel.add(lblNoAccount);
        registerPanel.add(btnRegister);

        // Key listeners for Enter
        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    attemptLogin();
                }
            }
        };
        txUsername.addKeyListener(enterListener);
        txPassword.addKeyListener(enterListener);

        // Version Label
        JLabel lblVersion = new JLabel(AppDesign.Config.APP_VERSION + " \u00a9 2024");
        lblVersion.setFont(AppDesign.Typography.CAPTION);
        lblVersion.setForeground(AppDesign.Colors.TEXT_MUTED);
        lblVersion.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Construct Form
        formPanel.add(Box.createVerticalGlue());
        formPanel.add(lblWelcome);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(lblInstruction);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        formPanel.add(lblUsername);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(txUsername);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        formPanel.add(lblPassword);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(txPassword);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        formPanel.add(optionsPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        formPanel.add(btnLogin);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(registerPanel);
        
        formPanel.add(Box.createVerticalGlue());
        
        formPanel.add(lblVersion);

        rightPanel.add(formPanel);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel);
    }

    private void loadPreferences() {
        String savedUser = prefs.get("remembered_username", "");
        if (!savedUser.isEmpty()) {
            txUsername.setText(savedUser);
            chkRemember.setSelected(true);
            txPassword.requestFocus();
        }
    }

    private void savePreferences() {
        if (chkRemember.isSelected()) {
            prefs.put("remembered_username", txUsername.getText().trim());
        } else {
            prefs.remove("remembered_username");
        }
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            txPassword.setEchoChar((char) 0);
            btnTogglePassword.setIcon(IconHelper.getIcon("eye-off", 18, AppDesign.Colors.TEXT_PRIMARY));
        } else {
            txPassword.setEchoChar('•');
            btnTogglePassword.setIcon(IconHelper.getIcon("eye", 18, AppDesign.Colors.TEXT_SECONDARY));
        }
    }

    private void attemptLogin() {
        String username = txUsername.getText().trim();
        String password = new String(txPassword.getPassword());

        if (username.isEmpty()) {
            showError("Username tidak boleh kosong!");
            txUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            showError("Password tidak boleh kosong!");
            txPassword.requestFocus();
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setBackground(AppDesign.Colors.TEXT_MUTED);
        btnLogin.setText("Memproses...");
        lblError.setVisible(false);

        SwingWorker<User, Void> worker = new SwingWorker<User, Void>() {
            @Override
            protected User doInBackground() throws Exception {
                User user = userDAO.getUserByUsername(username);
                if (user != null && PasswordUtil.checkPassword(password, user.getPassword())) {
                    return user;
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    User user = get();
                    if (user != null) {
                        savePreferences();
                        LoginFrame.this.dispose();
                        new MainFrame(user).setVisible(true);
                    } else {
                        showError("Username atau password salah!");
                        resetLoginButton();
                    }
                } catch (Exception e) {
                    showError("Terjadi kesalahan sistem!");
                    resetLoginButton();
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void showError(String message) {
        lblError.setText(message);
        lblError.setVisible(true);
    }

    private void resetLoginButton() {
        btnLogin.setEnabled(true);
        btnLogin.setBackground(AppDesign.Colors.ACCENT);
        btnLogin.setText("Masuk");
    }
}
