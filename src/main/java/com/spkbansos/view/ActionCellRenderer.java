package com.spkbansos.view;

import com.spkbansos.util.IconHelper;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ActionCellRenderer extends JPanel implements TableCellRenderer {
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnToggle;

    public ActionCellRenderer(boolean showToggle) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 4, 2));
        setBackground(AppDesign.Colors.BG_PRIMARY);

        btnEdit = createButton("edit", AppDesign.Colors.ROW_ACTION_EDIT, "Edit");
        btnDelete = createButton("trash", AppDesign.Colors.ROW_ACTION_DELETE, "Hapus");
        if (showToggle) {
            btnToggle = createButton("toggle-right", AppDesign.Colors.ROW_ACTION_TOGGLE, "Aktif/Nonaktif");
            add(btnToggle);
        }
        
        add(btnEdit);
        add(btnDelete);
    }

    private JButton createButton(String iconName, Color iconColor, String tooltip) {
        JButton btn = new JButton();
        btn.setIcon(IconHelper.getIcon(iconName, 16, iconColor));
        btn.setPreferredSize(new Dimension(AppDesign.Dimensions.ROW_ACTION_BTN_SIZE, AppDesign.Dimensions.ROW_ACTION_BTN_SIZE));
        btn.setToolTipText(tooltip);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setContentAreaFilled(false);
        return btn;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            // Apply alternate row color if defined
            Color alternateColor = UIManager.getColor("Table.alternateRowColor");
            if (alternateColor != null && row % 2 != 0) {
                setBackground(alternateColor);
            } else {
                setBackground(table.getBackground());
            }
        }
        
        if (btnToggle != null) {
            // Kolom Status di KriteriaPanel ada di index 5
            Object statusObj = table.getValueAt(row, 5);
            if (statusObj != null) {
                if ("Aktif".equals(statusObj.toString())) {
                    btnToggle.setIcon(IconHelper.getIcon("toggle-right", 16, AppDesign.Colors.ROW_ACTION_TOGGLE));
                    btnToggle.setToolTipText("Nonaktifkan");
                } else {
                    btnToggle.setIcon(IconHelper.getIcon("toggle-left", 16, AppDesign.Colors.TEXT_MUTED));
                    btnToggle.setToolTipText("Aktifkan");
                }
            }
        }
        
        return this;
    }
}
