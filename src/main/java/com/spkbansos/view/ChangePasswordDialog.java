package com.spkbansos.view;

import com.spkbansos.dao.UserDAO;
import com.spkbansos.model.User;
import com.spkbansos.util.IconHelper;
import com.spkbansos.util.PasswordUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChangePasswordDialog extends JDialog {

    private JPasswordField txOldPassword;
    private JPasswordField txNewPassword;
    private JPasswordField txConfirmPassword;
    private JLabel lblError;
    private JButton btnSave;
    
    private User currentUser;
    private UserDAO userDAO;

    public ChangePasswordDialog(JFrame parent, User user) {
        super(parent, "Ganti Password", true);
        this.currentUser = user;
        this.userDAO = new UserDAO();
        
        setSize(400, 450);
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

        JLabel lblTitle = new JLabel("Ganti Password");
        lblTitle.setFont(AppDesign.Typography.HEADING_MD);
        lblTitle.setForeground(AppDesign.Colors.TEXT_PRIMARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblSub = new JLabel("Masukkan password lama dan password baru Anda.");
        lblSub.setFont(AppDesign.Typography.BODY_SM);
        lblSub.setForeground(AppDesign.Colors.TEXT_SECONDARY);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        txOldPassword = createPasswordField("Password Lama");
        txNewPassword = createPasswordField("Password Baru");
        txConfirmPassword = createPasswordField("Konfirmasi Password Baru");

        lblError = new JLabel(" ");
        lblError.setFont(AppDesign.Typography.CAPTION);
        lblError.setForeground(AppDesign.Colors.DANGER);
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblError.setVisible(false);

        btnSave = new JButton("Simpan Password");
        btnSave.setFont(AppDesign.Typography.BODY_BOLD);
        btnSave.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSave.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnSave.setBackground(AppDesign.Colors.ACCENT);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.putClientProperty("FlatLaf.style", "arc: 8;");
        btnSave.addActionListener(e -> savePassword());

        btnSave.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if(btnSave.isEnabled()) btnSave.setBackground(AppDesign.Colors.ACCENT_HOVER);
            }
            public void mouseExited(MouseEvent e) {
                if(btnSave.isEnabled()) btnSave.setBackground(AppDesign.Colors.ACCENT);
            }
        });

        mainPanel.add(lblTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(lblSub);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        mainPanel.add(createLabel("Password Lama"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(txOldPassword);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        mainPanel.add(createLabel("Password Baru"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(txNewPassword);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        mainPanel.add(createLabel("Konfirmasi Password Baru"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(txConfirmPassword);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        mainPanel.add(lblError);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(btnSave);

        add(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(AppDesign.Typography.CAPTION_BOLD);
        lbl.setForeground(AppDesign.Colors.TEXT_SECONDARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPasswordField createPasswordField(String tooltip) {
        JPasswordField tx = new JPasswordField();
        tx.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
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

    private void savePassword() {
        String oldPwd = new String(txOldPassword.getPassword());
        String newPwd = new String(txNewPassword.getPassword());
        String confPwd = new String(txConfirmPassword.getPassword());

        if (oldPwd.isEmpty() || newPwd.isEmpty() || confPwd.isEmpty()) {
            showError("Semua field harus diisi!");
            return;
        }

        if (!newPwd.equals(confPwd)) {
            showError("Konfirmasi password baru tidak cocok!");
            return;
        }

        if (!PasswordUtil.checkPassword(oldPwd, currentUser.getPassword())) {
            showError("Password lama salah!");
            return;
        }
        
        if (newPwd.length() < 6) {
            showError("Password minimal 6 karakter!");
            return;
        }

        btnSave.setEnabled(false);
        lblError.setVisible(false);

        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                String hashedNew = PasswordUtil.hashPassword(newPwd);
                boolean success = userDAO.updatePassword(currentUser.getId(), hashedNew);
                if (success) {
                    currentUser.setPassword(hashedNew);
                }
                return success;
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        JOptionPane.showMessageDialog(ChangePasswordDialog.this, 
                            "Password berhasil diubah!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        showError("Gagal mengubah password.");
                        btnSave.setEnabled(true);
                    }
                } catch (Exception e) {
                    showError("Terjadi kesalahan sistem!");
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
