package com.spkbansos.view;

import com.spkbansos.util.IconHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Overlay transparan yang ditampilkan di atas JScrollPane tabel
 * saat data kosong. Menampilkan icon besar + pesan ajakan.
 *
 * Cara pakai:
 *   EmptyStateOverlay overlay = EmptyStateOverlay.install(parentPanel, scrollPane, "database-off", "Belum ada data.");
 *   // Setelah loadData():
 *   overlay.updateState(tableModel.getRowCount());
 */
public class EmptyStateOverlay extends JPanel {

    private boolean showing = false;

    private EmptyStateOverlay(String iconName, String message) {
        setOpaque(false);
        setLayout(new GridBagLayout());
        setVisible(false);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        Icon icon = IconHelper.getIcon(iconName, 48, AppDesign.Colors.TEXT_MUTED);
        if (icon != null) {
            JLabel lblIcon = new JLabel(icon);
            lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
            content.add(lblIcon);
            content.add(Box.createRigidArea(new Dimension(0, 12)));
        }

        JLabel lblMessage = new JLabel(message);
        lblMessage.setFont(AppDesign.Typography.BODY);
        lblMessage.setForeground(AppDesign.Colors.TEXT_MUTED);
        lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(lblMessage);

        add(content);
    }

    /**
     * Dipanggil setelah data tabel di-refresh.
     * @param rowCount Jumlah baris pada tabel setelah refresh.
     */
    public void updateState(int rowCount) {
        if (rowCount == 0 && !showing) {
            showing = true;
            setVisible(true);
        } else if (rowCount > 0 && showing) {
            showing = false;
            setVisible(false);
        }
    }

    /**
     * Memasang EmptyStateOverlay ke dalam parentPanel di atas scrollPane.
     * Mengganti scrollPane di parentPanel dengan JPanel wrapper ber-OverlayLayout.
     *
     * @param parentPanel Panel yang memiliki scrollPane sebagai child (harus menggunakan BorderLayout).
     * @param scrollPane  JScrollPane yang membungkus tabel.
     * @param iconName    Nama icon Tabler (misal "database-off").
     * @param message     Pesan yang ditampilkan.
     * @return Instance overlay yang dapat digunakan untuk memanggil updateState().
     */
    public static EmptyStateOverlay install(JPanel parentPanel, JScrollPane scrollPane,
                                            String iconName, String message) {
        EmptyStateOverlay overlay = new EmptyStateOverlay(iconName, message);

        // Tentukan constraint dari scrollPane di parentPanel
        Object constraint = BorderLayout.CENTER;
        if (parentPanel.getLayout() instanceof BorderLayout) {
            BorderLayout layout = (BorderLayout) parentPanel.getLayout();
            Object found = layout.getConstraints(scrollPane);
            if (found != null) constraint = found;
        }

        parentPanel.remove(scrollPane);

        // Wrapper dengan OverlayLayout — overlay di atas, scrollPane di bawah
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new OverlayLayout(wrapper));
        wrapper.setBackground(AppDesign.Colors.BG_PRIMARY);

        overlay.setAlignmentX(0.5f);
        overlay.setAlignmentY(0.5f);
        scrollPane.setAlignmentX(0.5f);
        scrollPane.setAlignmentY(0.5f);

        wrapper.add(overlay);
        wrapper.add(scrollPane);

        parentPanel.add(wrapper, constraint);
        parentPanel.revalidate();

        return overlay;
    }
}
