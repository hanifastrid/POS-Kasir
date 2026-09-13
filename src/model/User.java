package model;

/**
 * CLASS: User (Parent)
 * Parent class untuk semua pengguna sistem POS.
 * Konsep OOP: Encapsulation, Inheritance base
 */
public class User {

    public enum Role { ADMIN, CASHIER }

    private String  userId;
    private String  username;
    private String  password;
    private String  fullName;
    private Role    role;
    private boolean isActive;

    public User() {}

    public User(String userId, String username, String password,
                String fullName, Role role) {
        this.userId   = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role     = role;
        this.isActive = true;
    }

    /** Login sederhana — cocokkan password */
    public boolean login(String inputUser, String inputPass) {
        return this.username.equals(inputUser) &&
               this.password.equals(inputPass) &&
               this.isActive;
    }

    public void logout() {
        System.out.println(fullName + " telah logout.");
    }

    /** Cek izin berdasarkan role */
    public boolean hasPermission(String action) {
        if (role == Role.ADMIN) return true;   // Admin bisa semua
        // Cashier hanya bisa: transaksi, lihat produk
        return action.equals("TRANSAKSI") || action.equals("LIHAT_PRODUK");
    }

    // Getters & Setters
    public String  getUserId()                  { return userId; }
    public void    setUserId(String id)         { this.userId = id; }
    public String  getUsername()                { return username; }
    public void    setUsername(String u)        { this.username = u; }
    public String  getPassword()                { return password; }
    public void    setPassword(String p)        { this.password = p; }
    public String  getFullName()                { return fullName; }
    public void    setFullName(String n)        { this.fullName = n; }
    public Role    getRole()                    { return role; }
    public void    setRole(Role r)              { this.role = r; }
    public boolean isActive()                   { return isActive; }
    public void    setActive(boolean a)         { this.isActive = a; }

    @Override
    public String toString() { return fullName + " (" + role + ")"; }
}