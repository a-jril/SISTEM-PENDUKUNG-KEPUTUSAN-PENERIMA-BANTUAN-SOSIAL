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

public class LoginFrame extends JFrame {

    private JTextField txUsername;
    private JPasswordField txPassword;
    private JButton btnLogin;
    private JLabel lblError;
    private JButton btnTogglePassword;
    private boolean isPasswordVisible = false;

    private UserDAO userDAO;

    public LoginFrame() {
        this.userDAO = new UserDAO();

        setTitle("Login - Sistem Bansos");
        setSize(900, 600);
        setMinimumSize(new Dimension(520, 540));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().setBackground(AppDesign.Colors.BG_PRIMARY);

        Image appIcon = IconHelper.getImage("app-window", 32);
        if (appIcon != null) {
            setIconImage(appIcon);
        }

        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(AppDesign.Colors.LOGIN_CARD_BG);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppDesign.Colors.BORDER_SUBTLE, 1),
                new EmptyBorder(40, 40, 40, 40)
        ));
        mainPanel.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));
        mainPanel.setPreferredSize(new Dimension(400, 480));

        JPanel wrapperPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                    0, 0, AppDesign.Colors.LOGIN_GRADIENT_TOP,
                    0, getHeight(), AppDesign.Colors.LOGIN_GRADIENT_BOT);
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(mainPanel);

        // --- Header ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.setBackground(AppDesign.Colors.LOGIN_CARD_BG);

        Icon lockIcon = IconHelper.getIcon("lock", 40, AppDesign.Colors.ACCENT);
        if (lockIcon != null) {
            JLabel iconLabel = new JLabel(lockIcon);
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            headerPanel.add(iconLabel);
            headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JLabel lblJudul = new JLabel("SPK Bansos");
        lblJudul.setFont(AppDesign.Typography.HEADING_LG);
        lblJudul.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        lblJudul.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubjudul = new JLabel("Metode Simple Additive Weighting");
        lblSubjudul.setFont(AppDesign.Typography.BODY_SM);
        lblSubjudul.setForeground(AppDesign.Colors.TEXT_SECONDARY);
        lblSubjudul.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(lblJudul);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(lblSubjudul);

        mainPanel.add(headerPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 35)));

        // --- Form ---
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(AppDesign.Colors.LOGIN_CARD_BG);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(AppDesign.Typography.CAPTION_BOLD);
        lblUsername.setForeground(AppDesign.Colors.TEXT_SECONDARY);
        lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);

        txUsername = new JTextField();
        txUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txUsername.setPreferredSize(new Dimension(300, 38));
        txUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Let FlatLaf handle the border and rounded corners! 
        // We just supply styling string to customize it.
        txUsername.putClientProperty("FlatLaf.style", 
            "arc: 8;" + 
            "margin: 0,10,0,10;" + 
            "borderColor: #30363d;" + 
            "focusedBorderColor: #3884f4;" +
            "background: #212936"
        );
        txUsername.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        txUsername.setCaretColor(AppDesign.Colors.TEXT_PRIMARY);

        // Password with toggle
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(AppDesign.Typography.CAPTION_BOLD);
        lblPassword.setForeground(AppDesign.Colors.TEXT_SECONDARY);
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        txPassword = new JPasswordField();
        txPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txPassword.setPreferredSize(new Dimension(300, 38));
        txPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        txPassword.putClientProperty("FlatLaf.style", 
            "arc: 8;" + 
            "margin: 0,10,0,10;" + 
            "borderColor: #30363d;" + 
            "focusedBorderColor: #3884f4;" +
            "background: #212936"
        );
        txPassword.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        txPassword.setCaretColor(AppDesign.Colors.TEXT_PRIMARY);

        // Toggle button inside the password field!
        btnTogglePassword = new JButton(IconHelper.getIcon("eye", 18, AppDesign.Colors.TEXT_SECONDARY));
        btnTogglePassword.setBorderPainted(false);
        btnTogglePassword.setContentAreaFilled(false);
        btnTogglePassword.setFocusPainted(false);
        btnTogglePassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTogglePassword.addActionListener(e -> togglePasswordVisibility());
        
        // This FlatLaf feature embeds the component inside the text field
        txPassword.putClientProperty("JTextField.trailingComponent", btnTogglePassword);

        // --- Error Label ---
        lblError = new JLabel(" ");
        lblError.setFont(AppDesign.Typography.CAPTION);
        lblError.setForeground(AppDesign.Colors.DANGER);
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblError.setVisible(false);

        // --- Login Button ---
        btnLogin = new JButton("Masuk");
        btnLogin.setFont(AppDesign.Typography.BODY_SM);
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnLogin.setPreferredSize(new Dimension(300, 38));
        btnLogin.setBackground(AppDesign.Colors.ACCENT);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.putClientProperty("FlatLaf.style", 
            "arc: 12;" + 
            "margin: 0,10,0,10;" 
        );
        
        btnLogin.addActionListener(e -> attemptLogin());

        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if(btnLogin.isEnabled()) btnLogin.setBackground(AppDesign.Colors.ACCENT_HOVER);
            }
            public void mouseExited(MouseEvent e) {
                if(btnLogin.isEnabled()) btnLogin.setBackground(AppDesign.Colors.ACCENT);
            }
        });

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

        // Layout adding
        formPanel.add(lblUsername);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        formPanel.add(txUsername);
        formPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        formPanel.add(lblPassword);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        formPanel.add(txPassword); // No more pwdPanel!
        formPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        
        formPanel.add(lblError);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        formPanel.add(btnLogin);

        mainPanel.add(formPanel);

        add(wrapperPanel);
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

        // Perform login check in a background thread to keep UI responsive
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
                        // Success
                        LoginFrame.this.dispose();
                        new MainFrame(user).setVisible(true);
                    } else {
                        // Fail
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
