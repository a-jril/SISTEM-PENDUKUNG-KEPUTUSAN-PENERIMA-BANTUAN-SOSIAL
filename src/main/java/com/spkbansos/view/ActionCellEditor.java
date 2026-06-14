package com.spkbansos.view;

import com.spkbansos.util.IconHelper;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

public class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnToggle;
    
    private TableActionListener event;
    private int currentRow;
    private JButton clickedButton;

    public ActionCellEditor(boolean showToggle, TableActionListener event) {
        this.event = event;
        
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 2));
        
        btnEdit = createButton("edit", AppDesign.Colors.ROW_ACTION_EDIT, "Edit");
        btnDelete = createButton("trash", AppDesign.Colors.ROW_ACTION_DELETE, "Hapus");
        if (showToggle) {
            btnToggle = createButton("toggle-right", AppDesign.Colors.ROW_ACTION_TOGGLE, "Aktif/Nonaktif");
            panel.add(btnToggle);
        }
        
        panel.add(btnEdit);
        panel.add(btnDelete);
        
        btnEdit.addActionListener(e -> {
            fireEditingStopped();
            SwingUtilities.invokeLater(() -> {
                if (this.event != null) this.event.onEdit(currentRow);
            });
        });
        
        btnDelete.addActionListener(e -> {
            fireEditingStopped();
            SwingUtilities.invokeLater(() -> {
                if (this.event != null) this.event.onDelete(currentRow);
            });
        });
        
        if (showToggle) {
            btnToggle.addActionListener(e -> {
                fireEditingStopped();
                SwingUtilities.invokeLater(() -> {
                    if (this.event != null) this.event.onToggle(currentRow);
                });
            });
        }
    }

    private JButton createButton(String iconName, Color iconColor, String tooltip) {
        JButton btn = new JButton();
        btn.setIcon(IconHelper.getIcon(iconName, 16, iconColor));
        btn.setPreferredSize(new Dimension(AppDesign.Dimensions.ROW_ACTION_BTN_SIZE, AppDesign.Dimensions.ROW_ACTION_BTN_SIZE));
        btn.setToolTipText(tooltip);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setContentAreaFilled(true);
                btn.setBackground(AppDesign.Colors.ROW_ACTION_HOVER);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setContentAreaFilled(false);
            }
        });
        
        return btn;
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        if (e instanceof MouseEvent) {
            MouseEvent me = (MouseEvent) e;
            JTable table = (JTable) me.getSource();
            int row = table.rowAtPoint(me.getPoint());
            int col = table.columnAtPoint(me.getPoint());
            Rectangle cellRect = table.getCellRect(row, col, false);
            
            Point p = me.getPoint();
            p.translate(-cellRect.x, -cellRect.y);
            
            panel.setSize(cellRect.getSize());
            panel.doLayout();
            
            Component c = SwingUtilities.getDeepestComponentAt(panel, p.x, p.y);
            if (c instanceof JButton) {
                clickedButton = (JButton) c;
                return true;
            }
            return false;
        }
        return true;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.currentRow = row;
        panel.setBackground(table.getSelectionBackground());
        
        if (btnToggle != null) {
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
        
        if (clickedButton != null) {
            SwingUtilities.invokeLater(() -> {
                if (clickedButton != null) {
                    clickedButton.doClick();
                    clickedButton = null;
                }
            });
        }
        
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
}
