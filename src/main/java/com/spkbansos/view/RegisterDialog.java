package com.spkbansos.view;

import com.spkbansos.dao.UserDAO;
import com.spkbansos.util.IconHelper;
import com.spkbansos.util.PasswordUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterDialog extends JDialog {

    private JTextField txNama;
    private JTextField txUsername;
    private JPasswordField txPassword;
    private JPasswordField txConfirmPassword;
    private JLabel lblError;
    private JButton btnSave;
    private JButton btnBatal;
    
    private UserDAO userDAO;

    public RegisterDialog(JFrame parent) {
        super(parent, "Buat Akun Baru", true);
        this.userDAO = new UserDAO();
        
        setSize(400, 520);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(AppDesign.Colors.BG_PRIMARY);

        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(AppDesign.Colors.BG_PRIMARY);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel lblTitle = new JLabel("Buat Akun");
        lblTitle.setFont(AppDesign.Typography.HEADING_MD);
        lblTitle.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblSub = new JLabel("Silakan isi data untuk membuat akun operator.");
        lblSub.setFont(AppDesign.Typography.BODY_SM);
        lblSub.setForeground(AppDesign.Colors.TEXT_SECONDARY);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        txNama = createTextField("Nama Lengkap");
        txUsername = createTextField("Username");
        txPassword = createPasswordField("Password");
        txConfirmPassword = createPasswordField("Konfirmasi Password");

        lblError = new JLabel(" ");
        lblError.setFont(AppDesign.Typography.CAPTION);
        lblError.setForeground(AppDesign.Colors.DANGER);
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblError.setVisible(false);

        btnSave = new JButton("Daftar");
        btnSave.setFont(AppDesign.Typography.BODY_BOLD);
        btnSave.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSave.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnSave.setBackground(AppDesign.Colors.SUCCESS);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.putClientProperty("FlatLaf.style", "arc: 8;");
        btnSave.addActionListener(e -> registerUser());

        btnBatal = new JButton("Batal");
        btnBatal.setFont(AppDesign.Typography.BODY_BOLD);
        btnBatal.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnBatal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnBatal.setBackground(AppDesign.Colors.BG_TERTIARY);
        btnBatal.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        btnBatal.setFocusPainted(false);
        btnBatal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBatal.putClientProperty("FlatLaf.style", "arc: 8;");
        btnBatal.addActionListener(e -> dispose());

        mainPanel.add(lblTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(lblSub);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        mainPanel.add(createLabel("Nama Lengkap"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(txNama);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(createLabel("Username"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(txUsername);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        mainPanel.add(createLabel("Password"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(txPassword);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        mainPanel.add(createLabel("Konfirmasi Password"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(txConfirmPassword);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        mainPanel.add(lblError);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(AppDesign.Colors.BG_PRIMARY);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        buttonPanel.add(btnBatal);
        buttonPanel.add(btnSave);

        mainPanel.add(buttonPanel);

        add(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(AppDesign.Typography.CAPTION_BOLD);
        lbl.setForeground(AppDesign.Colors.TEXT_SECONDARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField createTextField(String tooltip) {
        JTextField tx = new JTextField();
        tx.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tx.setPreferredSize(new Dimension(100, 40));
        tx.setAlignmentX(Component.LEFT_ALIGNMENT);
        tx.putClientProperty("FlatLaf.style", 
            "arc: 8; margin: 0,10,0,10; borderColor: #30363d; focusedBorderColor: #3884f4; background: #212936"
        );
        tx.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        tx.setCaretColor(AppDesign.Colors.TEXT_PRIMARY);
        return tx;
    }

    private JPasswordField createPasswordField(String tooltip) {
        JPasswordField tx = new JPasswordField();
        tx.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tx.setPreferredSize(new Dimension(100, 40));
        tx.setAlignmentX(Component.LEFT_ALIGNMENT);
        tx.putClientProperty("FlatLaf.style", 
            "arc: 8; margin: 0,10,0,10; borderColor: #30363d; focusedBorderColor: #3884f4; background: #212936"
        );
        tx.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        tx.setCaretColor(AppDesign.Colors.TEXT_PRIMARY);
        
        JButton btnToggle = new JButton(IconHelper.getIcon("eye", 18, AppDesign.Colors.TEXT_SECONDARY));
        btnToggle.setBorderPainted(false);
        btnToggle.setContentAreaFilled(false);
        btnToggle.setFocusPainted(false);
        btnToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnToggle.addActionListener(e -> {
            if (tx.getEchoChar() == '•') {
                tx.setEchoChar((char) 0);
                btnToggle.setIcon(IconHelper.getIcon("eye-off", 18, AppDesign.Colors.TEXT_PRIMARY));
            } else {
                tx.setEchoChar('•');
                btnToggle.setIcon(IconHelper.getIcon("eye", 18, AppDesign.Colors.TEXT_SECONDARY));
            }
        });
        tx.putClientProperty("JTextField.trailingComponent", btnToggle);
        
        return tx;
    }

    private void registerUser() {
        String nama = txNama.getText().trim();
        String username = txUsername.getText().trim();
        String pwd = new String(txPassword.getPassword());
        String confPwd = new String(txConfirmPassword.getPassword());

        if (nama.isEmpty() || username.isEmpty() || pwd.isEmpty() || confPwd.isEmpty()) {
            showError("Semua field harus diisi!");
            return;
        }

        if (username.length() < 4) {
            showError("Username minimal 4 karakter!");
            return;
        }

        if (pwd.length() < 6) {
            showError("Password minimal 6 karakter!");
            return;
        }

        if (!pwd.equals(confPwd)) {
            showError("Konfirmasi password tidak cocok!");
            return;
        }

        btnSave.setEnabled(false);
        lblError.setVisible(false);

        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                if (userDAO.isUsernameExists(username)) {
                    publish("Username sudah digunakan!");
                    return false;
                }

                String hashedPwd = PasswordUtil.hashPassword(pwd);
                return userDAO.registerUser(username, hashedPwd, nama);
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                if (!chunks.isEmpty()) {
                    showError(chunks.get(chunks.size() - 1));
                }
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        JOptionPane.showMessageDialog(RegisterDialog.this, 
                            "Akun berhasil dibuat! Silakan login.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else if (lblError.getText().equals(" ")) {
                        showError("Gagal membuat akun.");
                    }
                } catch (Exception e) {
                    showError("Terjadi kesalahan sistem!");
                } finally {
                    btnSave.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void showError(String msg) {
        lblError.setText(msg);
        lblError.setVisible(true);
    }
}
