package model;

import java.sql.Time;

/**
 * CLASS: Cashier
 * extends User → Inheritance
 * Konsep OOP: Inheritance — Cashier mewarisi semua dari User,
 * lalu menambah properti dan method khusus kasir.
 */
public class Cashier extends User {

    private String  cashierId;
    private Time    shiftStart;
    private Time    shiftEnd;
    private double  totalSales;     // akumulasi penjualan shift ini
    private boolean shiftOpen;

    public Cashier() { super(); }

    public Cashier(String cashierId, String userId, String username,
                   String password, String fullName) {
        super(userId, username, password, fullName, Role.CASHIER);
        this.cashierId  = cashierId;
        this.totalSales = 0.0;
        this.shiftOpen  = false;
    }

    // ─── Method khusus Cashier ───────────────────────────────────────

    /** Mulai shift kerja */
    public void openShift() {
        this.shiftStart = new Time(System.currentTimeMillis());
        this.shiftOpen  = true;
        this.totalSales = 0.0;
        System.out.println("Shift dibuka: " + getFullName() + " @ " + shiftStart);
    }

    /** Tutup shift kerja */
    public void closeShift() {
        this.shiftEnd  = new Time(System.currentTimeMillis());
        this.shiftOpen = false;
        System.out.println("Shift ditutup. Total penjualan: Rp" +
            String.format("%,.0f", totalSales));
    }

    /**
     * Proses satu transaksi penjualan.
     * Menambah total penjualan kasir.
     * @param trx Transaksi yang akan diproses
     * @return true jika berhasil
     */
    public boolean processSale(Transaksi trx) {
        if (!shiftOpen) {
            System.err.println("Shift belum dibuka!");
            return false;
        }
        this.totalSales += trx.getGrandTotal();
        return true;
    }

    /** Cetak struk (ke console — bisa dikembangkan ke printer) */
    public void printReceipt(Transaksi trx) {
        System.out.println("=".repeat(40));
        System.out.println("         STRUK PEMBELIAN");
        System.out.println("=".repeat(40));
        System.out.println("No   : " + trx.getTransactionId());
        System.out.println("Kasir: " + getFullName());
        System.out.println("-".repeat(40));
        for (CartItem item : trx.getItems()) {
            System.out.printf("%-20s %3d x %,8.0f%n",
                item.getProductName(), item.getQty(), item.getUnitPrice());
            System.out.printf("  = Rp%,12.0f%n", item.getSubtotal());
        }
        System.out.println("-".repeat(40));
        System.out.printf("TOTAL        : Rp%,12.0f%n", trx.getGrandTotal());
        System.out.printf("BAYAR        : Rp%,12.0f%n", trx.getPaid());
        System.out.printf("KEMBALI      : Rp%,12.0f%n", trx.getChangeAmount());
        System.out.println("=".repeat(40));
        System.out.println("       Terima kasih!");
        System.out.println("=".repeat(40));
    }

    // ─── Getters & Setters ───────────────────────────────────────────
    public String  getCashierId()               { return cashierId; }
    public void    setCashierId(String id)      { this.cashierId = id; }

    public Time    getShiftStart()              { return shiftStart; }
    public void    setShiftStart(Time t)        { this.shiftStart = t; }

    public Time    getShiftEnd()                { return shiftEnd; }
    public void    setShiftEnd(Time t)          { this.shiftEnd = t; }

    public double  getTotalSales()              { return totalSales; }
    public void    setTotalSales(double s)      { this.totalSales = s; }

    public boolean isShiftOpen()                { return shiftOpen; }
}
