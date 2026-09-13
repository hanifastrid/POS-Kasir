package dao;

import model.Ledger;
import util.Koneksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DAO untuk Ledger (pembukuan keuangan).
 */
public class LedgerDAO {

    private Connection conn;

    public LedgerDAO() {
        this.conn = Koneksi.getConnection();
    }

    public boolean simpan(Ledger ledger) {
        String sql = "INSERT INTO tb_ledger (type,amount,description,reference_id,recorded_by) " +
                     "VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ledger.getType().name());
            ps.setDouble(2, ledger.getAmount());
            ps.setString(3, ledger.getDescription());
            ps.setString(4, ledger.getReferenceId());
            ps.setString(5, ledger.getRecordedBy());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error simpan ledger: " + e.getMessage());
            return false;
        }
    }

    /** Total pemasukan hari ini */
    public double getTodayIncome() {
        String sql = "SELECT COALESCE(SUM(amount),0) AS total FROM tb_ledger " +
                     "WHERE type='INCOME' AND DATE(recorded_at)=CURDATE()";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /** Total pengeluaran hari ini */
    public double getTodayExpense() {
        String sql = "SELECT COALESCE(SUM(amount),0) AS total FROM tb_ledger " +
                     "WHERE type='EXPENSE' AND DATE(recorded_at)=CURDATE()";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /** Saldo bersih hari ini (income - expense) */
    public double getBalance() {
        return getTodayIncome() - getTodayExpense();
    }
}