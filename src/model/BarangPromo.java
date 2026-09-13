package model;

import java.util.Date;

/**
 * CLASS: BarangPromo
 * extends Barang → Multi-level Inheritance
 * Produk dengan diskon terbatas waktu.
 * Konsep OOP: Polymorphism — override calculateDiscount()
 */
public class BarangPromo extends Barang {

    private int    promoId;
    private double discountPct;   // persen diskon, misal 15 = 15%
    private Date   promoStart;
    private Date   promoEnd;

    public BarangPromo() { super(); }

    public BarangPromo(String productId, String barcode, String name,
                       int categoryId, double price, double buyPrice,
                       int stock, String unit,
                       double discountPct, Date promoStart, Date promoEnd) {
        super(productId, barcode, name, categoryId, price, buyPrice, stock, unit);
        this.discountPct = discountPct;
        this.promoStart  = promoStart;
        this.promoEnd    = promoEnd;
    }

    // ─── Override: hitung diskon berbeda! ───────────────────────────
    /**
     * Jika promo aktif → kembalikan nilai diskon dalam Rupiah
     * Jika tidak aktif → 0 (tidak ada diskon)
     * Konsep OOP: Polymorphism
     */
    @Override
    public double calculateDiscount() {
        if (isPromoActive()) {
            return getPrice() * (discountPct / 100.0);
        }
        return 0.0;
    }

    /** Cek apakah promo sedang aktif hari ini */
    public boolean isPromoActive() {
        if (promoStart == null || promoEnd == null) return false;
        Date now = new Date();
        return !now.before(promoStart) && !now.after(promoEnd);
    }

    /** Label untuk ditampilkan di UI */
    public String getPromoLabel() {
        if (isPromoActive()) {
            return String.format("PROMO %.0f%%", discountPct);
        }
        return "";
    }

    // ─── Getters & Setters ───────────────────────────────────────────
    public int    getPromoId()                   { return promoId; }
    public void   setPromoId(int id)             { this.promoId = id; }

    public double getDiscountPct()               { return discountPct; }
    public void   setDiscountPct(double pct)     { this.discountPct = pct; }

    public Date   getPromoStart()                { return promoStart; }
    public void   setPromoStart(Date d)          { this.promoStart = d; }

    public Date   getPromoEnd()                  { return promoEnd; }
    public void   setPromoEnd(Date d)            { this.promoEnd = d; }
}