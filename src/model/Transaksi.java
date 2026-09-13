package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CLASS: Transaksi
 * Mewakili satu transaksi penjualan di kasir.
 * Berisi daftar CartItem dan kalkulasi total.
 */
public class Transaksi {

    public enum PaymentMethod { CASH, DEBIT, KREDIT, QRIS }
    public enum Status { PENDING, DONE, CANCELLED }

    private String         transactionId;
    private String         cashierId;
    private List<CartItem> items;
    private double         totalAmount;     // sebelum diskon
    private double         discountTotal;
    private double         grandTotal;      // yang harus dibayar
    private double         paid;
    private double         changeAmount;
    private PaymentMethod  paymentMethod;
    private Status         status;
    private Date           createdAt;
    private String         notes;

    public Transaksi() {
        this.items         = new ArrayList<>();
        this.status         = Status.PENDING;
        this.createdAt      = new Date();
        this.paymentMethod  = PaymentMethod.CASH;
    }

    public Transaksi(String transactionId, String cashierId) {
        this();
        this.transactionId = transactionId;
        this.cashierId     = cashierId;
    }

    // ─── Operasi keranjang ───────────────────────────────────────────

    public void addItem(CartItem item) {
        // Jika produk sudah ada → update qty
        for (CartItem existing : items) {
            if (existing.getProductId().equals(item.getProductId())) {
                existing.setQty(existing.getQty() + item.getQty());
                calculateTotal();
                return;
            }
        }
        items.add(item);
        calculateTotal();
    }

    public void removeItem(String productId) {
        items.removeIf(i -> i.getProductId().equals(productId));
        calculateTotal();
    }

    public void clearItems() {
        items.clear();
        totalAmount = discountTotal = grandTotal = 0;
    }

    // ─── Kalkulasi ───────────────────────────────────────────────────

    public double calculateTotal() {
        totalAmount   = items.stream().mapToDouble(CartItem::getSubtotal).sum();
        discountTotal = 0; // diskon sudah masuk di unitPrice masing-masing item
        grandTotal    = totalAmount - discountTotal;
        return grandTotal;
    }

    public double calculateChange() {
        changeAmount = paid - grandTotal;
        return changeAmount;
    }

    public void setPaid(double paid) {
        this.paid = paid;
        calculateChange();
    }

    public void selesaikan(PaymentMethod method) {
        this.paymentMethod = method;
        this.status         = Status.DONE;
        calculateTotal();
        calculateChange();
    }

    // ─── Getters & Setters ───────────────────────────────────────────
    public String         getTransactionId()                 { return transactionId; }
    public void           setTransactionId(String id)        { this.transactionId = id; }
    public String         getCashierId()                     { return cashierId; }
    public void           setCashierId(String id)            { this.cashierId = id; }
    public List<CartItem> getItems()                          { return items; }
    public double         getTotalAmount()                   { return totalAmount; }
    public double         getDiscountTotal()                 { return discountTotal; }
    public double         getGrandTotal()                    { return grandTotal; }
    public double         getPaid()                          { return paid; }
    public double         getChangeAmount()                  { return changeAmount; }
    public PaymentMethod  getPaymentMethod()                 { return paymentMethod; }
    public void           setPaymentMethod(PaymentMethod m)  { this.paymentMethod = m; }
    public Status         getStatus()                        { return status; }
    public void           setStatus(Status s)                { this.status = s; }
    public Date           getCreatedAt()                     { return createdAt; }
    public String         getNotes()                          { return notes; }
    public void           setNotes(String n)                 { this.notes = n; }
}