package model;

import java.util.Date;

/**
 * CLASS: Ledger
 * Mencatat setiap pemasukan dan pengeluaran kas.
 * Setiap transaksi selesai → otomatis tambah entri INCOME ke Ledger.
 */
public class Ledger {

    public enum EntryType { INCOME, EXPENSE }

    private int       ledgerId;
    private EntryType type;
    private double    amount;
    private String    description;
    private String    referenceId;    // transaction_id jika dari penjualan
    private String    recordedBy;     // user_id
    private Date       recordedAt;

    public Ledger() {}

    public Ledger(EntryType type, double amount, String description,
                  String referenceId, String recordedBy) {
        this.type        = type;
        this.amount       = amount;
        this.description  = description;
        this.referenceId  = referenceId;
        this.recordedBy   = recordedBy;
        this.recordedAt   = new Date();
    }

    /** Buat entri otomatis dari transaksi yang sudah selesai */
    public static Ledger fromTransaksi(Transaksi trx, String recordedBy) {
        return new Ledger(
            EntryType.INCOME,
            trx.getGrandTotal(),
            "Penjualan - " + trx.getPaymentMethod(),
            trx.getTransactionId(),
            recordedBy
        );
    }

    // Getters & Setters
    public int       getLedgerId()              { return ledgerId; }
    public void      setLedgerId(int id)        { this.ledgerId = id; }
    public EntryType getType()                  { return type; }
    public void      setType(EntryType t)       { this.type = t; }
    public double    getAmount()                { return amount; }
    public void      setAmount(double a)        { this.amount = a; }
    public String    getDescription()           { return description; }
    public void      setDescription(String d)   { this.description = d; }
    public String    getReferenceId()           { return referenceId; }
    public void      setReferenceId(String r)   { this.referenceId = r; }
    public String    getRecordedBy()             { return recordedBy; }
    public void      setRecordedBy(String u)    { this.recordedBy = u; }
    public Date       getRecordedAt()            { return recordedAt; }
}