package com.spkbansos.view;

public interface TableActionListener {
    void onEdit(int row);
    void onDelete(int row);
    default void onToggle(int row) {}
}
