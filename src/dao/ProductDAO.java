package dao;

import model.Barang;
import model.BarangPromo;
import model.Product;
import util.Koneksi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO untuk Produk/Barang — CRUD lengkap.
 * Menangani Barang reguler dan BarangPromo lewat satu tabel
 * (pola "Single Table Inheritance" — dibedakan kolom product_type).
 */
public class ProductDAO {

    private Connection conn;

    public ProductDAO() {
        this.conn = Koneksi.getConnection();
    }

    // ── CREATE ──────────────────────────────────────────────────────
    public boolean simpan(Barang b) {
        String sql = "INSERT INTO tb_product " +
            "(product_id,barcode,name,description,category_id,price,buy_price," +
            " stock,unit,weight,expiry_date,supplier_id,product_type) " +
            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,  b.getProductId());
            ps.setString(2,  b.getBarcode());
            ps.setString(3,  b.getName());
            ps.setString(4,  b.getDescription());
            ps.setInt   (5,  b.getCategoryId());
            ps.setDouble(6,  b.getPrice());
            ps.setDouble(7,  b.getBuyPrice());
            ps.setInt   (8,  b.getStock());
            ps.setString(9,  b.getUnit());
            ps.setDouble(10, b.getWeight());
            if (b.getExpiryDate() != null)
                ps.setDate(11, new java.sql.Date(b.getExpiryDate().getTime()));
            else
                ps.setNull(11, Types.DATE);
            ps.setString(12, b.getSupplierId());
            ps.setString(13, (b instanceof BarangPromo) ? "PROMO" : "REGULAR");

            boolean ok = ps.executeUpdate() > 0;
            if (ok && b instanceof BarangPromo) {
                simpanPromo((BarangPromo) b);
            }
            return ok;
        } catch (SQLException e) {
            System.err.println("Error simpan product: " + e.getMessage());
            return false;
        }
    }

    private void simpanPromo(BarangPromo bp) throws SQLException {
        String sql = "INSERT INTO tb_promo (product_id,discount_pct,promo_start,promo_end) " +
                     "VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bp.getProductId());
            ps.setDouble(2, bp.getDiscountPct());
            ps.setDate  (3, new java.sql.Date(bp.getPromoStart().getTime()));
            ps.setDate  (4, new java.sql.Date(bp.getPromoEnd().getTime()));
            ps.executeUpdate();
        }
    }

    // ── READ ALL ─────────────────────────────────────────────────────
    public List<Product> getAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM v_product_full WHERE is_active = 1 ORDER BY name";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("Error getAll: " + e.getMessage());
        }
        return list;
    }

    // ── READ BY ID ───────────────────────────────────────────────────
    public Product getById(String productId) {
        String sql = "SELECT * FROM v_product_full WHERE product_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getById: " + e.getMessage());
        }
        return null;
    }

    // ── READ BY BARCODE ──────────────────────────────────────────────
    public Product getByBarcode(String barcode) {
        String sql = "SELECT * FROM v_product_full WHERE barcode = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, barcode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getByBarcode: " + e.getMessage());
        }
        return null;
    }

    // ── SEARCH ───────────────────────────────────────────────────────
    public List<Product> cari(String keyword) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM v_product_full " +
                     "WHERE is_active=1 AND (name LIKE ? OR barcode LIKE ? OR product_id LIKE ?) " +
                     "ORDER BY name";
        String kw = "%" + keyword + "%";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kw); ps.setString(2, kw); ps.setString(3, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error cari: " + e.getMessage());
        }
        return list;
    }

    // ── UPDATE ───────────────────────────────────────────────────────
    public boolean update(Barang b) {
        String sql = "UPDATE tb_product SET name=?,description=?,category_id=?," +
                     "price=?,buy_price=?,stock=?,unit=?,is_active=? WHERE product_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getName());
            ps.setString(2, b.getDescription());
            ps.setInt   (3, b.getCategoryId());
            ps.setDouble(4, b.getPrice());
            ps.setDouble(5, b.getBuyPrice());
            ps.setInt   (6, b.getStock());
            ps.setString(7, b.getUnit());
            ps.setBoolean(8, b.isActive());
            ps.setString(9, b.getProductId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error update: " + e.getMessage());
            return false;
        }
    }

    // ── UPDATE STOK ──────────────────────────────────────────────────
    public boolean updateStock(String productId, int newStock) {
        String sql = "UPDATE tb_product SET stock=? WHERE product_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt   (1, newStock);
            ps.setString(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updateStock: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE (soft delete) ─────────────────────────────────────────
    public boolean hapus(String productId) {
        String sql = "UPDATE tb_product SET is_active=0 WHERE product_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error hapus: " + e.getMessage());
            return false;
        }
    }

    // ── MAP ROW → OBJECT ─────────────────────────────────────────────
    private Product mapRow(ResultSet rs) throws SQLException {
        String type = rs.getString("product_type");
        if ("PROMO".equals(type) && rs.getDate("promo_start") != null) {
            BarangPromo bp = new BarangPromo();
            fillBarang(bp, rs);
            bp.setDiscountPct(rs.getDouble("discount_pct"));
            bp.setPromoStart (rs.getDate("promo_start"));
            bp.setPromoEnd   (rs.getDate("promo_end"));
            return bp;
        } else {
            Barang b = new Barang();
            fillBarang(b, rs);
            return b;
        }
    }

    private void fillBarang(Barang b, ResultSet rs) throws SQLException {
        b.setProductId   (rs.getString("product_id"));
        b.setBarcode     (rs.getString("barcode"));
        b.setName        (rs.getString("name"));
        b.setDescription (rs.getString("description"));
        b.setCategoryId  (rs.getInt("category_id"));
        b.setCategoryName(rs.getString("category_name"));
        b.setPrice       (rs.getDouble("price"));
        b.setBuyPrice    (rs.getDouble("buy_price"));
        b.setStock       (rs.getInt("stock"));
        b.setUnit        (rs.getString("unit"));
        b.setActive      (rs.getBoolean("is_active"));
        b.setExpiryDate  (rs.getDate("expiry_date"));
        if (rs.getString("supplier_id") != null)
            b.setSupplierId(rs.getString("supplier_id"));
    }
}