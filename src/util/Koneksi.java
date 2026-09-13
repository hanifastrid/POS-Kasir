package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Singleton koneksi database MySQL.
 * Konsep OOP: Singleton Pattern — menjamin hanya ada satu
 * Connection aktif yang dipakai di seluruh aplikasi.
 */
public class Koneksi {

    private static final String URL  =
        "jdbc:mysql://localhost:3306/db_pos?useSSL=false&serverTimezone=Asia/Jakarta";
    private static final String USER = "root";
    private static final String PASS = "";   // kosong jika XAMPP default

    private static Connection conn = null;

    private Koneksi() {}   // konstruktor private → tidak bisa di-instansiasi

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("✓ Koneksi database berhasil!");
            }
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                "Driver MySQL tidak ditemukan!\nTambahkan mysql-connector-j.jar ke Libraries.",
                "Error Driver", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Koneksi database gagal!\n" + e.getMessage(),
                "Error Koneksi", JOptionPane.ERROR_MESSAGE);
        }
        return conn;
    }

    public static void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                conn = null;
                System.out.println("✓ Koneksi database ditutup.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isConnected() {
        try {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}