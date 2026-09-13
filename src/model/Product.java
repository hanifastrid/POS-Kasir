package model;

/**
 * PARENT CLASS: Product
 * Konsep OOP: Abstraction + Encapsulation
 * Semua jenis barang mewarisi class ini.
 */
public abstract class Product {

    // ─── Properties (private = Encapsulation) ───────────────────────
    private String  productId;
    private String  barcode;
    private String  name;
    private String  description;
    private String  categoryName;
    private int     categoryId;
    private double  price;
    private int     stock;
    private String  unit;
    private boolean isActive;

    // ─── Konstruktor ─────────────────────────────────────────────────
    public Product() {}

    public Product(String productId, String barcode, String name,
                   int categoryId, double price, int stock, String unit) {
        this.productId  = productId;
        this.barcode    = barcode;
        this.name       = name;
        this.categoryId = categoryId;
        this.price      = price;
        this.stock      = stock;
        this.unit       = unit;
        this.isActive   = true;
    }

    // ─── Abstract Method (wajib di-override anak) ────────────────────
    /**
     * Hitung diskon produk ini.
     * Setiap subclass mengimplementasikan cara diskon yang berbeda.
     * Konsep OOP: Polymorphism
     */
    public abstract double calculateDiscount();

    // ─── Concrete Methods (bisa dipakai langsung) ────────────────────

    /** Harga setelah diskon */
    public double getEffectivePrice() {
        return price - calculateDiscount();
    }

    /** Cek apakah produk tersedia */
    public boolean isAvailable() {
        return isActive && stock > 0;
    }

    /** Info ringkas produk */
    public String getInfo() {
        return String.format("[%s] %s | Rp%,.0f | Stok: %d %s",
            productId, name, getEffectivePrice(), stock, unit);
    }

    /** Kurangi stok saat terjual */
    public void reduceStock(int qty) {
        if (qty > stock) throw new IllegalArgumentException("Stok tidak cukup!");
        this.stock -= qty;
    }

    /** Tambah stok saat restok */
    public void addStock(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Qty harus positif!");
        this.stock += qty;
    }

    // ─── Getters & Setters ───────────────────────────────────────────
    public String  getProductId()                   { return productId; }
    public void    setProductId(String productId)   { this.productId = productId; }

    public String  getBarcode()                     { return barcode; }
    public void    setBarcode(String barcode)       { this.barcode = barcode; }

    public String  getName()                        { return name; }
    public void    setName(String name)             { this.name = name; }

    public String  getDescription()                 { return description; }
    public void    setDescription(String desc)      { this.description = desc; }

    public int     getCategoryId()                  { return categoryId; }
    public void    setCategoryId(int categoryId)    { this.categoryId = categoryId; }

    public String  getCategoryName()                { return categoryName; }
    public void    setCategoryName(String n)        { this.categoryName = n; }

    public double  getPrice()                       { return price; }
    public void    setPrice(double price)           { this.price = price; }

    public int     getStock()                       { return stock; }
    public void    setStock(int stock)              { this.stock = stock; }

    public String  getUnit()                        { return unit; }
    public void    setUnit(String unit)             { this.unit = unit; }

    public boolean isActive()                       { return isActive; }
    public void    setActive(boolean active)        { this.isActive = active; }

    @Override
    public String toString() { return name + " (Rp" + String.format("%,.0f", getEffectivePrice()) + ")"; }
}