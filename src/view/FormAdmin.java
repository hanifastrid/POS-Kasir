package view;

import javax.swing.*;
import java.awt.*;

/**
 * PLACEHOLDER SEMENTARA — FormAdmin
 * Tujuan: supaya FormLogin bisa langsung di-test tanpa menunggu
 * FormAdmin versi lengkap selesai dibuat.
 *
 * Nanti file ini akan DIGANTI TOTAL dengan versi lengkap
 * (form CRUD produk, tabel, dll).
 */
public class FormAdmin extends JFrame {

    public FormAdmin() {
        setTitle("Admin Panel — Placeholder Sementara");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);   // buka full screen desktop
        setSize(1000, 700);                         // ukuran fallback jika maximize gagal

        JLabel lbl = new JLabel(
            "<html><center>✅ Login ADMIN berhasil!<br><br>" +
            "Ini halaman placeholder sementara.<br>" +
            "FormAdmin versi lengkap akan menyusul.</center></html>",
            SwingConstants.CENTER
        );
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(lbl, BorderLayout.CENTER);

        setVisible(true);
    }
}