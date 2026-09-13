package model;

/**
 * CLASS: CartItem
 * Representasi satu baris item dalam transaksi.
 * Seperti satu baris di struk belanja.
 */
public class CartItem {

    private String productId;
    private String productName;
    private double unitPrice;     // harga setelah diskon
    private int    qty;
    private double discountPct;
    private double subtotal;      // unitPrice * qty

    public CartItem() {}

    public CartItem(String productId, String productName,
                    double unitPrice, int qty, double discountPct) {
        this.productId   = productId;
        this.productName = productName;
        this.unitPrice   = unitPrice;
        this.qty         = qty;
        this.discountPct = discountPct;
        this.subtotal     = unitPrice * qty;
    }

    public void recalculate() {
        this.subtotal = unitPrice * qty;
    }

    // Getters & Setters
    public String getProductId()              { return productId; }
    public void   setProductId(String id)     { this.productId = id; }
    public String getProductName()            { return productName; }
    public void   setProductName(String n)    { this.productName = n; }
    public double getUnitPrice()              { return unitPrice; }
    public void   setUnitPrice(double p)      { this.unitPrice = p; recalculate(); }
    public int    getQty()                    { return qty; }
    public void   setQty(int q)               { this.qty = q; recalculate(); }
    public double getDiscountPct()            { return discountPct; }
    public void   setDiscountPct(double d)    { this.discountPct = d; }
    public double getSubtotal()               { return subtotal; }
}
