package dao;

import model.CartItem;
import model.Ledger;
import model.Transaksi;
import util.Koneksi;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DAO Transaksi — simpan transaksi + detail ke database.
 * Menggunakan database TRANSACTION (commit/rollback) supaya
 * proses simpan header, detail, kurangi stok, dan catat ledger
 * berjalan ATOMIK — semua berhasil atau semua dibatalkan.
 */
public class TransaksiDAO {

    private Connection conn;

    public TransaksiDAO() {
        this.conn = Koneksi.getConnection();
    }

    // ── GENERATE ID TRANSAKSI ─────────────────────────────────────────
    public String generateId() {
        String prefix = "TRX-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "-";
        String sql = "SELECT COUNT(*)+1 AS seq FROM tb_transaksi WHERE transaction_id LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return prefix + String.format("%04d", rs.getInt("seq"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prefix + "0001";
    }

    // ── SIMPAN TRANSAKSI (atomic) ────────────────────────────────────
    public boolean simpan(Transaksi trx) {
        try {
            conn.setAutoCommit(false);  // mulai mode transaksi manual

            // 1. Insert header transaksi
            String sql1 = "INSERT INTO tb_transaksi " +
                "(transaction_id,cashier_id,total_amount,discount_total," +
                " grand_total,paid,change_amount,payment_method,status,notes) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sql1)) {
                ps.setString(1,  trx.getTransactionId());
                ps.setString(2,  trx.getCashierId());
                ps.setDouble(3,  trx.getTotalAmount());
                ps.setDouble(4,  trx.getDiscountTotal());
                ps.setDouble(5,  trx.getGrandTotal());
                ps.setDouble(6,  trx.getPaid());
                ps.setDouble(7,  trx.getChangeAmount());
                ps.setString(8,  trx.getPaymentMethod().name());
                ps.setString(9,  trx.getStatus().name());
                ps.setString(10, trx.getNotes());
                ps.executeUpdate();
            }

            // 2. Insert detail item
            String sql2 = "INSERT INTO tb_detail_transaksi " +
                "(transaction_id,product_id,product_name,unit_price,qty,discount_pct,subtotal) " +
                "VALUES (?,?,?,?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sql2)) {
                for (CartItem item : trx.getItems()) {
                    ps.setString(1, trx.getTransactionId());
                    ps.setString(2, item.getProductId());
                    ps.setString(3, item.getProductName());
                    ps.setDouble(4, item.getUnitPrice());
                    ps.setInt   (5, item.getQty());
                    ps.setDouble(6, item.getDiscountPct());
                    ps.setDouble(7, item.getSubtotal());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            // 3. Kurangi stok tiap produk yang terjual
            String sqlStock = "UPDATE tb_product SET stock = stock - ? WHERE product_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlStock)) {
                for (CartItem item : trx.getItems()) {
                    ps.setInt   (1, item.getQty());
                    ps.setString(2, item.getProductId());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            // 4. Catat otomatis ke Ledger sebagai pemasukan
            new LedgerDAO().simpan(Ledger.fromTransaksi(trx, trx.getCashierId()));

            conn.commit();              // semua langkah berhasil → simpan permanen
            conn.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            System.err.println("Error simpan transaksi: " + e.getMessage());
            try {
                conn.rollback();        // ada yang gagal → batalkan semua
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

    // ── BATAL TRANSAKSI ──────────────────────────────────────────────
    public boolean batal(String transactionId) {
        String sql = "UPDATE tb_transaksi SET status='CANCELLED' WHERE transaction_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, transactionId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error batal transaksi: " + e.getMessage());
            return false;
        }
    }
}