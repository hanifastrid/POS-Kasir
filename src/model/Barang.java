package model;

import java.util.Date;

/**
 * CLASS: Barang
 * extends Product → Inheritance
 * Produk fisik dengan info beli, berat, kadaluarsa, supplier
 */
public class Barang extends Product {

    // Properti tambahan Barang
    private double buyPrice;      // harga beli dari supplier
    private double weight;        // berat dalam gram
    private Date   expiryDate;    // tanggal kadaluarsa
    private String supplierId;

    public Barang() { super(); }

    public Barang(String productId, String barcode, String name,
                  int categoryId, double price, double buyPrice,
                  int stock, String unit) {
        super(productId, barcode, name, categoryId, price, stock, unit);
        this.buyPrice = buyPrice;
    }

    // ─── Override abstract method ────────────────────────────────────
    /**
     * Barang reguler = tidak ada diskon (0)
     * Konsep OOP: Polymorphism — implementasi berbeda dari BarangPromo
     */
    @Override
    public double calculateDiscount() {
        return 0.0;   // tidak ada diskon default
    }

    // ─── Method tambahan Barang ──────────────────────────────────────

    /** Keuntungan per item */
    public double getProfit() {
        return getEffectivePrice() - buyPrice;
    }

    /** Margin keuntungan dalam persen */
    public double getProfitMarginPct() {
        if (buyPrice == 0) return 0;
        return (getProfit() / buyPrice) * 100;
    }

    /** Cek apakah sudah kadaluarsa */
    public boolean isExpired() {
        if (expiryDate == null) return false;
        return new Date().after(expiryDate);
    }

    // ─── Getters & Setters ───────────────────────────────────────────
    public double getBuyPrice()               { return buyPrice; }
    public void   setBuyPrice(double p)       { this.buyPrice = p; }

    public double getWeight()                 { return weight; }
    public void   setWeight(double w)         { this.weight = w; }

    public Date   getExpiryDate()             { return expiryDate; }
    public void   setExpiryDate(Date d)       { this.expiryDate = d; }

    public String getSupplierId()             { return supplierId; }
    public void   setSupplierId(String id)    { this.supplierId = id; }
}