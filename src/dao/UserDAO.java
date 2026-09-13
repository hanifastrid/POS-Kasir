package dao;

import model.Cashier;
import model.User;
import util.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO untuk User — verifikasi login dan ambil data Cashier.
 */
public class UserDAO {

    private Connection conn;

    public UserDAO() {
        this.conn = Koneksi.getConnection();
    }

    /**
     * Cek username + password.
     * @return Role user jika berhasil, null jika gagal login
     */
    public User.Role loginCheck(String username, String password) {
        String sql = "SELECT role FROM tb_user WHERE username=? AND password=? AND is_active=1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return User.Role.valueOf(rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loginCheck: " + e.getMessage());
        }
        return null;
    }

    /**
     * Ambil objek Cashier lengkap berdasarkan username.
     * Dipanggil setelah loginCheck() berhasil dan role == CASHIER.
     */
    public Cashier getCashier(String username) {
        String sql = "SELECT u.*, c.cashier_id, c.total_sales " +
                     "FROM tb_user u " +
                     "JOIN tb_cashier c ON u.user_id = c.user_id " +
                     "WHERE u.username = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cashier c = new Cashier(
                        rs.getString("cashier_id"),
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name")
                    );
                    c.setTotalSales(rs.getDouble("total_sales"));
                    return c;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getCashier: " + e.getMessage());
        }
        return null;
    }
}
