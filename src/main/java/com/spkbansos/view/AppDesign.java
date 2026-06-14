package com.spkbansos.view;

import java.awt.*;

/**
 * Kelas konstanta desain terpusat untuk seluruh aplikasi SPK Bansos.
 * Semua warna, font, ukuran, dan spacing didefinisikan di sini agar
 * konsisten di seluruh UI.
 *
 * Inspirasi: Dark professional theme dengan aksen biru keabu-abuan.
 */
public final class AppDesign {

    private AppDesign() {} // Tidak bisa di-instantiasi

    // =========================================================
    // CONFIGURATION
    // =========================================================
    public static final class Config {
        /** Threshold kelayakan bansos */
        public static final double KELAYAKAN_THRESHOLD = 0.6;
        
        /** Nama Aplikasi */
        public static final String APP_NAME = "SPK Bansos";
        
        /** Versi Aplikasi */
        public static final String APP_VERSION = "v1.0.0";
        
        private Config() {}
    }

    // =========================================================
    // COLORS
    // =========================================================
    public static final class Colors {

        // --- Sidebar ---
        /** Warna background utama sidebar */
        public static final Color SIDEBAR_BG          = new Color(22, 27, 34);
        /** Warna background header sidebar (sedikit lebih terang) */
        public static final Color SIDEBAR_HEADER_BG   = new Color(28, 34, 43);
        /** Warna tombol sidebar default */
        public static final Color SIDEBAR_BTN_DEFAULT = new Color(22, 27, 34);
        /** Warna tombol sidebar saat hover */
        public static final Color SIDEBAR_BTN_HOVER   = new Color(33, 41, 54);
        /** Warna tombol sidebar saat aktif/dipilih */
        public static final Color SIDEBAR_BTN_ACTIVE  = new Color(42, 52, 68);
        /** Warna border halus sidebar */
        public static final Color SIDEBAR_BORDER      = new Color(48, 54, 61);
        /** Warna teks section label di sidebar */
        public static final Color SIDEBAR_SECTION_TEXT = new Color(125, 133, 144);

        // --- Text ---
        /** Warna teks utama (putih terang) */
        public static final Color TEXT_PRIMARY         = new Color(230, 237, 243);
        /** Warna teks sekunder (abu-abu terang) */
        public static final Color TEXT_SECONDARY       = new Color(125, 133, 144);
        /** Warna teks muted (abu-abu gelap) */
        public static final Color TEXT_MUTED           = new Color(89, 96, 107);

        // --- Aksen ---
        /** Warna aksen utama — biru cerah */
        public static final Color ACCENT               = new Color(56, 132, 244);
        /** Warna aksen hover */
        public static final Color ACCENT_HOVER         = new Color(73, 148, 255);
        /** Warna aksen secondary — teal */
        public static final Color ACCENT_SECONDARY     = new Color(29, 158, 117);

        // --- Status ---
        /** Hijau untuk sukses/aktif */
        public static final Color SUCCESS              = new Color(46, 160, 67);
        /** Kuning untuk warning */
        public static final Color WARNING              = new Color(210, 153, 34);
        /** Merah untuk error/danger */
        public static final Color DANGER               = new Color(218, 54, 51);
        /** Oranye untuk logout */
        public static final Color LOGOUT               = new Color(218, 88, 57);

        // --- Background ---
        /** Background utama konten */
        public static final Color BG_PRIMARY           = new Color(13, 17, 23);
        /** Background sekunder (card, panel) */
        public static final Color BG_SECONDARY         = new Color(22, 27, 34);
        /** Background tertier (input, hover) */
        public static final Color BG_TERTIARY          = new Color(33, 41, 54);

        // --- Border ---
        /** Border utama */
        public static final Color BORDER               = new Color(48, 54, 61);
        /** Border subtle */
        public static final Color BORDER_SUBTLE        = new Color(38, 44, 51);

        // --- Login ---
        /** Gradient atas pada login */
        public static final Color LOGIN_GRADIENT_TOP   = new Color(13, 17, 23);
        /** Gradient bawah pada login */
        public static final Color LOGIN_GRADIENT_BOT   = new Color(22, 27, 34);
        /** Background card login */
        public static final Color LOGIN_CARD_BG        = new Color(22, 27, 34);
        /** Gradient utama panel kiri (Biru) */
        public static final Color LOGIN_BRAND_GRADIENT_START = new Color(56, 132, 244);
        /** Gradient utama panel kiri (Ungu) */
        public static final Color LOGIN_BRAND_GRADIENT_END   = new Color(111, 66, 193);

        // --- Navbar ---
        public static final Color NAVBAR_BG         = new Color(22, 27, 34);  // Sama dengan BG_SECONDARY
        public static final Color NAVBAR_BORDER     = new Color(48, 54, 61);  // Sama dengan BORDER

        // --- Table Inline Actions ---
        public static final Color ROW_ACTION_EDIT   = new Color(210, 153, 34);   // WARNING
        public static final Color ROW_ACTION_DELETE = new Color(218, 54, 51);    // DANGER
        public static final Color ROW_ACTION_TOGGLE = new Color(46, 160, 67);   // SUCCESS
        public static final Color ROW_ACTION_HOVER  = new Color(42, 52, 68);    // Sedikit highlight

        private Colors() {}
    }

    // =========================================================
    // TYPOGRAPHY
    // =========================================================
    public static final class Typography {

        /** Font keluarga utama — fallback ke Segoe UI di Windows */
        public static final String FONT_FAMILY     = "Segoe UI";
        /** Font keluarga mono — untuk kode/data */
        public static final String FONT_MONO       = "Consolas";

        /** Judul besar (login title, page header) */
        public static final Font HEADING_XL        = new Font(FONT_FAMILY, Font.BOLD, 28);
        /** Judul halaman */
        public static final Font HEADING_LG        = new Font(FONT_FAMILY, Font.BOLD, 22);
        /** Judul section */
        public static final Font HEADING_MD        = new Font(FONT_FAMILY, Font.BOLD, 18);
        /** Sub-heading */
        public static final Font HEADING_SM        = new Font(FONT_FAMILY, Font.BOLD, 14);

        /** Body text utama */
        public static final Font BODY              = new Font(FONT_FAMILY, Font.PLAIN, 14);
        /** Body text bold */
        public static final Font BODY_BOLD         = new Font(FONT_FAMILY, Font.BOLD, 14);
        /** Body text kecil */
        public static final Font BODY_SM           = new Font(FONT_FAMILY, Font.PLAIN, 13);

        /** Label / caption */
        public static final Font CAPTION           = new Font(FONT_FAMILY, Font.PLAIN, 12);
        /** Label bold */
        public static final Font CAPTION_BOLD      = new Font(FONT_FAMILY, Font.BOLD, 12);

        /** Label section sidebar — uppercase, kecil */
        public static final Font SIDEBAR_SECTION   = new Font(FONT_FAMILY, Font.BOLD, 10);
        /** Teks tombol sidebar */
        public static final Font SIDEBAR_BUTTON    = new Font(FONT_FAMILY, Font.PLAIN, 13);
        /** Nama aplikasi di sidebar */
        public static final Font SIDEBAR_APP_TITLE = new Font(FONT_FAMILY, Font.BOLD, 16);

        /** Angka besar (dashboard card) */
        public static final Font STAT_NUMBER       = new Font(FONT_FAMILY, Font.BOLD, 32);

        /** Judul Navbar */
        public static final Font NAVBAR_TITLE      = new Font(FONT_FAMILY, Font.BOLD, 16);

        private Typography() {}
    }

    // =========================================================
    // DIMENSIONS & SPACING
    // =========================================================
    public static final class Dimensions {

        /** Lebar sidebar dalam piksel */
        public static final int SIDEBAR_WIDTH      = 240;
        /** Ukuran icon sidebar */
        public static final int SIDEBAR_ICON_SIZE  = 18;
        /** Tinggi tombol sidebar */
        public static final int SIDEBAR_BTN_HEIGHT = 38;
        /** Padding horizontal sidebar */
        public static final int SIDEBAR_PAD_H      = 16;
        /** Padding vertikal sidebar */
        public static final int SIDEBAR_PAD_V      = 12;

        /** Ukuran icon kecil (untuk tombol aksi) */
        public static final int ICON_SM            = 16;
        /** Ukuran icon medium (untuk panel header) */
        public static final int ICON_MD            = 20;
        /** Ukuran icon besar (untuk placeholder) */
        public static final int ICON_LG            = 24;
        /** Ukuran icon ekstra besar (untuk center placeholder) */
        public static final int ICON_XL            = 48;

        /** Border radius default */
        public static final int BORDER_RADIUS      = 8;
        /** Border radius besar */
        public static final int BORDER_RADIUS_LG   = 12;

        /** Padding konten halaman */
        public static final int PAGE_PADDING       = 28;
        /** Gap antar elemen */
        public static final int GAP                = 12;

        /** Tinggi Navbar */
        public static final int NAVBAR_HEIGHT      = 56;
        /** Ukuran tombol aksi di dalam tabel */
        public static final int ROW_ACTION_BTN_SIZE = 28;

        private Dimensions() {}
    }
}
