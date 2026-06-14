package design;

import java.awt.*;

/**
 * ResendDesignSystem.java
 * ========================
 * Adaptasi Resend Design System ke konstanta Java
 * untuk aplikasi SPK Kelayakan Penerima Bantuan Sosial
 * di AnyiGravity (Swing/JavaFX).
 *
 * Cara pakai:
 *   panel.setBackground(ResendDesignSystem.Colors.CANVAS);
 *   label.setForeground(ResendDesignSystem.Colors.INK);
 *   label.setFont(ResendDesignSystem.Typography.DISPLAY_XL);
 */
public class ResendDesignSystem {

    // =========================================================
    // COLORS
    // =========================================================
    public static final class Colors {

        // --- Brand & Canvas ---
        /** #000000 — Latar belakang utama semua halaman */
        public static final Color CANVAS          = new Color(0x000000);

        /** #fcfdff — Teks utama di atas canvas hitam */
        public static final Color INK             = new Color(0xfcfdff);

        /** #fcfdff @ 86% — Body text panjang */
        public static final Color BODY            = new Color(252, 253, 255, 220);

        /** #fcfdff @ 70% — Caption, label nav sekunder */
        public static final Color CHARCOAL        = new Color(252, 253, 255, 179);

        /** #a1a4a5 — Teks pendukung, label tidak aktif */
        public static final Color MUTE            = new Color(0xa1a4a5);

        /** #888e90 — Teks tersier, teks footer */
        public static final Color ASH             = new Color(0x888e90);

        /** #464a4d — Foreground disabled */
        public static final Color STONE           = new Color(0x464a4d);

        // --- Surface (Elevasi Bertingkat) ---
        /** #0a0a0c — Surface kartu standar (elevasi 1) */
        public static final Color SURFACE_CARD     = new Color(0x0a0a0c);

        /** #101012 — Surface terangkat, tier pricing unggulan (elevasi 2) */
        public static final Color SURFACE_ELEVATED = new Color(0x101012);

        /** #06060a — Background code window (elevasi 3) */
        public static final Color SURFACE_DEEP     = new Color(0x06060a);

        // --- Border / Hairline ---
        /** rgba(255,255,255,0.06) — Divider halus antar baris */
        public static final Color HAIRLINE         = new Color(255, 255, 255, 15);

        /** rgba(255,255,255,0.14) — Border struktural kartu & input */
        public static final Color HAIRLINE_STRONG  = new Color(255, 255, 255, 36);

        /** rgba(255,255,255,0.04) — Divider sangat halus (footer kolom) */
        public static final Color DIVIDER_SOFT     = new Color(255, 255, 255, 10);

        // --- Primary CTA ---
        /** #fcfdff — Tombol utama (putih di atas hitam) */
        public static final Color PRIMARY          = new Color(0xfcfdff);

        /** #000000 — Label di atas tombol putih */
        public static final Color PRIMARY_ON       = new Color(0x000000);

        /** #f1f7fe — State pressed tombol utama */
        public static final Color SURFACE_LIGHT    = new Color(0xf1f7fe);

        // --- Accent Solid ---
        /** #ff801f — Orange aksen (highlight inline) */
        public static final Color ACCENT_ORANGE    = new Color(0xff801f);

        /** #ffc53d — Yellow aksen (callout utama dev) */
        public static final Color ACCENT_YELLOW    = new Color(0xffc53d);

        /** #3b9eff — Blue aksen (link, glow cool) */
        public static final Color ACCENT_BLUE      = new Color(0x3b9eff);

        /** #11ff99 — Green aksen (status sukses) */
        public static final Color ACCENT_GREEN     = new Color(0x11ff99);

        /** #ff2047 — Red aksen (error, perhatian) */
        public static final Color ACCENT_RED       = new Color(0xff2047);

        // --- Accent Glow (radial wash, opacity rendah) ---
        /** rgba(255,89,0,0.22) — Atmosfer orange */
        public static final Color GLOW_ORANGE      = new Color(255, 89, 0, 56);

        /** rgba(0,117,255,0.34) — Atmosfer biru */
        public static final Color GLOW_BLUE        = new Color(0, 117, 255, 87);

        /** rgba(34,255,153,0.18) — Atmosfer hijau */
        public static final Color GLOW_GREEN       = new Color(34, 255, 153, 46);

        /** rgba(255,32,71,0.34) — Atmosfer merah */
        public static final Color GLOW_RED         = new Color(255, 32, 71, 87);

        // --- Text di atas kartu putih (email mockup) ---
        /** #000000 — Label di atas surface terang */
        public static final Color ON_LIGHT         = new Color(0x000000);

        /** rgba(0,0,51,0.7) — Teks sekunder di surface terang */
        public static final Color ON_LIGHT_MUTE    = new Color(0, 0, 51, 179);

        /** #3b9eff — Warna link inline */
        public static final Color LINK             = new Color(0x3b9eff);

        private Colors() {}
    }


    // =========================================================
    // TYPOGRAPHY  (menggunakan font sistem sebagai fallback)
    // =========================================================
    public static final class Typography {

        /*
         * Resend aslinya pakai:
         *   - Domaine Display  → fallback: "Palatino Linotype" / "Georgia" (serif)
         *   - ABC Favorit      → fallback: "Segoe UI" / "Arial" (sans-serif)
         *   - Inter            → fallback: "Segoe UI" / "SansSerif"
         *   - Geist Mono       → fallback: "Courier New" / "Monospaced"
         *
         * Di Java, ganti dengan font yang terinstall di sistem.
         * Kalau mau akurat, embed TTF lewat Font.createFont().
         */

        private static final String SERIF    = "Palatino Linotype";
        private static final String SANS     = "Segoe UI";
        private static final String UI       = "Segoe UI";
        private static final String MONO     = "Courier New";

        // --- Display (Hero Headline — pakai SERIF / Domaine Display) ---
        /** 96px — Judul hero utama, satu per halaman */
        public static final Font DISPLAY_XXL = new Font(SERIF, Font.PLAIN, 96);

        /** 76px — Section opener utama */
        public static final Font DISPLAY_XL  = new Font(SERIF, Font.PLAIN, 76);

        /** 56px — Sub-judul display ABC Favorit */
        public static final Font DISPLAY_LG  = new Font(SANS, Font.PLAIN, 56);

        // --- Heading ---
        /** 24px Medium — Judul kartu, sub-judul seksi */
        public static final Font HEADING_MD  = new Font(UI, Font.BOLD, 24);

        /** 20px Medium — Header list */
        public static final Font HEADING_SM  = new Font(UI, Font.BOLD, 20);

        // --- Body & Subtitle ---
        /** 20px Regular — Subtitle hero */
        public static final Font SUBTITLE    = new Font(SANS, Font.PLAIN, 20);

        /** 18px Regular — Prosa marketing */
        public static final Font BODY_LG     = new Font(SANS, Font.PLAIN, 18);

        /** 16px Regular — Body ABC Favorit */
        public static final Font BODY_MD     = new Font(SANS, Font.PLAIN, 16);

        /** 14px Regular — Caption, metadata */
        public static final Font BODY_SM     = new Font(UI, Font.PLAIN, 14);

        // --- Button ---
        /** 14px Medium — Label tombol default */
        public static final Font BUTTON_MD   = new Font(UI, Font.BOLD, 14);

        /** 14px Medium — Pill label, link inline */
        public static final Font BUTTON_SM   = new Font(UI, Font.BOLD, 14);

        // --- Caption ---
        /** 12px Regular — Disclosure footer, copyright */
        public static final Font CAPTION     = new Font(UI, Font.PLAIN, 12);

        /** 14px SemiBold — Caption emfatik kecil */
        public static final Font CAPTION_EMPH = new Font(UI, Font.BOLD, 14);

        // --- Code ---
        /** 13px Regular — Code block, inline code */
        public static final Font CODE_MD     = new Font(MONO, Font.PLAIN, 13);

        private Typography() {}
    }


    // =========================================================
    // SPACING  (dalam piksel, basis unit 4px)
    // =========================================================
    public static final class Spacing {
        public static final int XXS     = 2;
        public static final int XS      = 4;
        public static final int SM      = 8;
        public static final int MD      = 12;
        public static final int LG      = 16;
        public static final int XL      = 24;
        public static final int XXL     = 32;
        public static final int XXXL    = 48;
        public static final int SECTION = 96;
        public static final int BAND    = 128;

        private Spacing() {}
    }


    // =========================================================
    // BORDER RADIUS  (dalam piksel, untuk swing: arc di drawRoundRect)
    // =========================================================
    public static final class Rounded {
        public static final int NONE = 0;
        public static final int XS   = 4;
        public static final int SM   = 6;
        public static final int MD   = 8;   // Tombol, input
        public static final int LG   = 12;  // Kartu, code well
        public static final int XL   = 16;  // Panel besar
        public static final int FULL = 9999; // Pill, avatar

        private Rounded() {}
    }


    // =========================================================
    // DIMENSION KOMPONEN  (referensi ukuran standar)
    // =========================================================
    public static final class Dimension {
        /** Tinggi tombol default (desktop) */
        public static final int BUTTON_HEIGHT       = 36;

        /** Tinggi tombol mobile */
        public static final int BUTTON_HEIGHT_MOBILE = 44;

        /** Tinggi input teks */
        public static final int INPUT_HEIGHT        = 40;

        /** Tinggi navigation bar */
        public static final int NAV_HEIGHT          = 64;

        /** Lebar maksimum konten */
        public static final int MAX_CONTENT_WIDTH   = 1200;

        /** Ukuran avatar kontributor */
        public static final int AVATAR_SIZE         = 32;

        /** Ukuran status dot */
        public static final int STATUS_DOT_SIZE     = 8;

        private Dimension() {}
    }


    // =========================================================
    // FACTORY METHOD — helper untuk komponen Swing umum
    // =========================================================

    /**
     * Membuat Insets standar untuk kartu fitur (padding 32px semua sisi).
     */
    public static Insets cardInsets() {
        return new Insets(Spacing.XXL, Spacing.XXL, Spacing.XXL, Spacing.XXL);
    }

    /**
     * Membuat Insets standar untuk tombol MD (8px atas-bawah, 16px kiri-kanan).
     */
    public static Insets buttonInsets() {
        return new Insets(Spacing.SM, Spacing.LG, Spacing.SM, Spacing.LG);
    }

    /**
     * Membuat Insets untuk input teks (10px atas-bawah, 14px kiri-kanan).
     */
    public static Insets inputInsets() {
        return new Insets(10, 14, 10, 14);
    }

    /**
     * Membuat Insets untuk section band (96px atas-bawah, 32px kiri-kanan).
     */
    public static Insets sectionInsets() {
        return new Insets(Spacing.SECTION, Spacing.XXL, Spacing.SECTION, Spacing.XXL);
    }


    // =========================================================
    // CONTOH PENGGUNAAN DALAM SPK BANTUAN SOSIAL
    // =========================================================
    /*
     * Di AnyiGravity / Swing:
     *
     * // Panel utama
     * JPanel mainPanel = new JPanel();
     * mainPanel.setBackground(Colors.CANVAS);
     *
     * // Judul aplikasi
     * JLabel title = new JLabel("SPK Bantuan Sosial");
     * title.setFont(Typography.DISPLAY_XL);
     * title.setForeground(Colors.INK);
     *
     * // Tombol primary (CTA utama)
     * JButton btnCTA = new JButton("Hitung Kelayakan");
     * btnCTA.setBackground(Colors.PRIMARY);
     * btnCTA.setForeground(Colors.PRIMARY_ON);
     * btnCTA.setFont(Typography.BUTTON_MD);
     * // AnyiGravity: set corner arc = Rounded.MD (8px)
     *
     * // Kartu hasil
     * JPanel card = new JPanel();
     * card.setBackground(Colors.SURFACE_CARD);
     * card.setBorder(BorderFactory.createLineBorder(Colors.HAIRLINE_STRONG, 1));
     * // AnyiGravity: set corner arc = Rounded.LG (12px)
     *
     * // Label status layak
     * JLabel statusOk = new JLabel("● Layak");
     * statusOk.setForeground(Colors.ACCENT_GREEN);
     *
     * // Label status tidak layak
     * JLabel statusNo = new JLabel("● Tidak Layak");
     * statusNo.setForeground(Colors.ACCENT_RED);
     *
     * // Input form
     * JTextField inputField = new JTextField();
     * inputField.setBackground(Colors.SURFACE_CARD);
     * inputField.setForeground(Colors.INK);
     * inputField.setBorder(BorderFactory.createLineBorder(Colors.HAIRLINE_STRONG, 1));
     * // AnyiGravity: corner arc = Rounded.MD (8px)
     *
     * // Tabel data penerima
     * JTable table = new JTable(model);
     * table.setBackground(Colors.SURFACE_CARD);
     * table.setForeground(Colors.BODY);
     * table.setGridColor(Colors.HAIRLINE);
     * table.setFont(Typography.BODY_SM);
     *
     * // Footer / catatan
     * JLabel footer = new JLabel("© 2025 Dinas Sosial");
     * footer.setForeground(Colors.ASH);
     * footer.setFont(Typography.CAPTION);
     */

    private ResendDesignSystem() {}
}