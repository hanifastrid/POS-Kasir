import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * TEST KONEKSI — jalankan file ini SAJA dulu sebelum lanjut ke project utama.
 * Tujuan: memastikan JDBC driver dan koneksi MySQL berfungsi dengan benar.
 *
 * CARA PAKAI:
 * 1. Buat Java Class baru bernama "TestKoneksi" di package mana saja
 * 2. Copy isi file ini ke dalamnya
 * 3. Run (klik kanan file → Run File, atau Shift+F6)
 * 4. Lihat hasilnya di jendela Output NetBeans
 */
public class TestKoneksi {

    public static void main(String[] args) {

        // ── Konfigurasi koneksi ──────────────────────────────────
        String host     = "localhost";
        String port     = "3306";
        String database = "db_pos";
        String user     = "root";
        String password = "";   // kosongkan jika XAMPP default

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database +
                     "?useSSL=false&serverTimezone=Asia/Jakarta";

        System.out.println("Mencoba koneksi ke: " + url);
        System.out.println("------------------------------------------------");

        try {
            // STEP 1: Load driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✓ Driver MySQL berhasil dimuat.");

            // STEP 2: Buka koneksi
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✓ KONEKSI BERHASIL ke database '" + database + "'!");

            // STEP 3: Test query sederhana — hitung jumlah produk
            java.sql.Statement st = conn.createStatement();
            java.sql.ResultSet rs = st.executeQuery("SELECT COUNT(*) AS total FROM tb_product");
            if (rs.next()) {
                System.out.println("✓ Jumlah produk di database: " + rs.getInt("total"));
            }

            // STEP 4: Tutup koneksi
            conn.close();
            System.out.println("------------------------------------------------");
            System.out.println("🎉 SEMUA TES BERHASIL! Koneksi Java-MySQL siap dipakai.");

        } catch (ClassNotFoundException e) {
            System.out.println("❌ GAGAL: Driver MySQL tidak ditemukan!");
            System.out.println("   → Pastikan mysql-connector-j-x.x.x.jar sudah ditambahkan ke Libraries.");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("❌ GAGAL: Tidak bisa terhubung ke database!");
            System.out.println("   Kemungkinan penyebab:");
            System.out.println("   1. XAMPP MySQL belum di-Start");
            System.out.println("   2. Nama database 'db_pos' salah ketik atau belum dibuat");
            System.out.println("   3. Username/password salah");
            System.out.println("   Detail error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}