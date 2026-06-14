package com.spkbansos.util;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;

/**
 * Helper class untuk memuat icon SVG dari folder "icon/".
 * Menggunakan FlatSVGIcon dari flatlaf-extras agar icon SVG
 * bisa di-render dengan crisp di berbagai ukuran.
 */
public class IconHelper {

    private static final String ICON_DIR = "icon";

    /**
     * Mendapatkan icon SVG berdasarkan nama file (tanpa ekstensi .svg).
     *
     * @param name Nama icon tanpa ekstensi, misal "layout-dashboard"
     * @param size Ukuran icon dalam piksel (width & height sama)
     * @return Icon yang sudah di-scale, atau null jika file tidak ditemukan
     */
    public static Icon getIcon(String name, int size) {
        return getIcon(name, size, null);
    }

    /**
     * Mendapatkan icon SVG dengan warna tertentu.
     *
     * @param name  Nama icon tanpa ekstensi
     * @param size  Ukuran icon dalam piksel
     * @param color Warna stroke icon (null = gunakan currentColor / default)
     * @return Icon yang sudah di-scale dan diwarnai
     */
    public static Icon getIcon(String name, int size, Color color) {
        try {
            File iconFile = new File(ICON_DIR, name + ".svg");
            if (!iconFile.exists()) {
                System.err.println("Icon tidak ditemukan: " + iconFile.getAbsolutePath());
                return null;
            }

            FlatSVGIcon icon = new FlatSVGIcon(iconFile.toURI().toURL());

            // Terapkan warna jika diberikan
            if (color != null) {
                FlatSVGIcon.ColorFilter colorFilter = new FlatSVGIcon.ColorFilter();
                colorFilter.add(Color.BLACK, color);       // replace #000 with desired color
                colorFilter.add(new Color(0x2C2C2C), color); // fallback dark color
                icon.setColorFilter(colorFilter);
            }

            // Scale icon ke ukuran yang diminta
            // FlatSVGIcon default 24x24 (dari viewBox SVG Tabler Icons)
            float scale = size / 24.0f;
            return icon.derive(scale);

        } catch (MalformedURLException e) {
            System.err.println("Gagal memuat icon '" + name + "': " + e.getMessage());
            return null;
        }
    }

    /**
     * Mendapatkan ImageIcon dari SVG untuk title bar / window icon.
     *
     * @param name Nama icon tanpa ekstensi
     * @param size Ukuran icon
     * @return Image yang bisa digunakan untuk JFrame.setIconImage()
     */
    public static Image getImage(String name, int size) {
        Icon icon = getIcon(name, size);
        if (icon instanceof ImageIcon) {
            return ((ImageIcon) icon).getImage();
        }
        // Fallback: render icon ke BufferedImage
        if (icon != null) {
            java.awt.image.BufferedImage img =
                    new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            icon.paintIcon(null, g2, 0, 0);
            g2.dispose();
            return img;
        }
        return null;
    }
}
