package view;

import model.Cashier;

import javax.swing.*;
import java.awt.*;

/**
 * PLACEHOLDER SEMENTARA — FormKasir
 * Tujuan: supaya FormLogin bisa langsung di-test, khususnya
 * untuk memverifikasi bahwa objek Cashier hasil login berhasil
 * "dibawa" masuk ke form ini lewat constructor.
 *
 * Nanti file ini akan DIGANTI TOTAL dengan versi lengkap
 * (katalog produk, keranjang belanja, proses bayar, dll).
 */
public class FormKasir extends JFrame {

    public FormKasir(Cashier cashier) {
        setTitle("Kasir POS — Placeholder Sementara");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);   // buka full screen desktop
        setSize(1000, 700);                         // ukuran fallback jika maximize gagal

        // Tampilkan data Cashier untuk membuktikan data berhasil "lewat"
        // dari FormLogin ke form ini
        JLabel lbl = new JLabel(
            "<html><center>✅ Login KASIR berhasil!<br><br>" +
            "Cashier ID : " + cashier.getCashierId() + "<br>" +
            "Nama       : " + cashier.getFullName() + "<br><br>" +
            "Ini halaman placeholder sementara.<br>" +
            "FormKasir versi lengkap akan menyusul.</center></html>",
            SwingConstants.CENTER
        );
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        add(lbl, BorderLayout.CENTER);

        setVisible(true);
    }
}