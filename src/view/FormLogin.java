package view;

import dao.UserDAO;
import model.Cashier;
import model.User;
import util.Koneksi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * FormLogin — titik masuk satu-satunya aplikasi POS.
 * Versi revisi: layout lebih rapi, pesan error pasti terlihat,
 * dan window berukuran proporsional (bukan window kecil terpotong).
 */
public class FormLogin extends JFrame {

    private JTextField     txtUsername;
    private JPasswordField txtPassword;
    private JButton        btnLogin;
    private JLabel         lblStatus;

    private static final Color WARNA_GELAP       = new Color(30, 41, 59);
    private static final Color WARNA_HIJAU       = new Color(34, 197, 94);
    private static final Color WARNA_HIJAU_HOVER = new Color(22, 163, 74);
    private static final Color WARNA_ABU         = new Color(148, 163, 184);

    public FormLogin() {
        initComponents();
        setTitle("Login — Kasir POS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 480);            // diperbesar supaya tidak ada elemen terpotong
        setMinimumSize(new Dimension(420, 480));
        setLocationRelativeTo(null);  // selalu muncul di tengah layar
        setVisible(true);
    }

    // ── INIT UI ──────────────────────────────────────────────────────
    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // ── Header ──────────────────────────────────────────────────
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(WARNA_GELAP);
        header.setBorder(new EmptyBorder(36, 30, 36, 30));

        JLabel lblIcon = new JLabel("🛒", SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblApp = new JLabel("KASIR POS", SwingConstants.CENTER);
        lblApp.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblApp.setForeground(Color.WHITE);
        lblApp.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblApp.setBorder(new EmptyBorder(8, 0, 4, 0));

        JLabel lblSub = new JLabel("Point of Sales System", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(WARNA_ABU);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(lblIcon);
        header.add(lblApp);
        header.add(lblSub);
        add(header, BorderLayout.NORTH);

        // ── Form Input (BoxLayout vertikal — lebih predictable daripada GridBag) ──
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(32, 36, 20, 36));

        Font fLabel = new Font("Segoe UI", Font.BOLD, 12);
        Font fField = new Font("Segoe UI", Font.PLAIN, 14);

        JLabel lblUsername = new JLabel("USERNAME");
        lblUsername.setFont(fLabel);
        lblUsername.setForeground(new Color(100, 116, 139));
        lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsername = new JTextField();
        txtUsername.setFont(fField);
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
            new EmptyBorder(6, 10, 6, 10)
        ));
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPassword = new JLabel("PASSWORD");
        lblPassword.setFont(fLabel);
        lblPassword.setForeground(new Color(100, 116, 139));
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblPassword.setBorder(new EmptyBorder(16, 0, 0, 0));

        txtPassword = new JPasswordField();
        txtPassword.setFont(fField);
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
            new EmptyBorder(6, 10, 6, 10)
        ));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── LABEL STATUS / ERROR ──────────────────────────────────────
        // PERBAIKAN UTAMA: diberi tinggi tetap (preferredSize) dan warna
        // mencolok, jadi PASTI terlihat dan tidak ketiban komponen lain.
        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(new Color(220, 38, 38));     // merah tegas
        lblStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStatus.setBorder(new EmptyBorder(12, 0, 4, 0));
        lblStatus.setPreferredSize(new Dimension(320, 20));   // beri ruang pasti
        lblStatus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(WARNA_HIJAU);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setOpaque(true);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.setBorder(new EmptyBorder(10, 0, 10, 0));
        // efek hover sederhana
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                btnLogin.setBackground(WARNA_HIJAU_HOVER);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                btnLogin.setBackground(WARNA_HIJAU);
            }
        });

        form.add(lblUsername);
        form.add(Box.createRigidArea(new Dimension(0, 6)));
        form.add(txtUsername);
        form.add(lblPassword);
        form.add(Box.createRigidArea(new Dimension(0, 6)));
        form.add(txtPassword);
        form.add(lblStatus);
        form.add(Box.createRigidArea(new Dimension(0, 8)));
        form.add(btnLogin);

        add(form, BorderLayout.CENTER);

        // ── Status Koneksi Database (footer) ───────────────────────
        boolean connected = Koneksi.isConnected();
        JLabel lblDb = new JLabel("  " + (connected
            ? "● Database terhubung"
            : "● Database tidak terhubung — cek XAMPP MySQL"));
        lblDb.setForeground(connected ? new Color(34, 197, 94) : new Color(220, 38, 38));
        lblDb.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDb.setBorder(new EmptyBorder(6, 10, 8, 10));
        add(lblDb, BorderLayout.SOUTH);

        // ── Events ──────────────────────────────────────────────────
        btnLogin.addActionListener(e -> doLogin());
        txtPassword.addActionListener(e -> doLogin());                  // Enter di password → login
        txtUsername.addActionListener(e -> txtPassword.requestFocus());  // Enter di username → pindah fokus
    }

    // ── PROSES LOGIN ─────────────────────────────────────────────────
    private void doLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            tampilkanError("Username dan password wajib diisi!");
            return;
        }

        UserDAO userDAO = new UserDAO();
        User.Role role  = userDAO.loginCheck(username, password);

        if (role == null) {
            tampilkanError("Username atau password salah!");
            txtPassword.setText("");
            txtPassword.requestFocus();
            return;
        }

        // Login berhasil → bersihkan pesan error sebelum pindah form
        lblStatus.setText(" ");

        if (role == User.Role.ADMIN) {
            dispose();
            new FormAdmin();
        } else {
            Cashier cashier = userDAO.getCashier(username);
            if (cashier == null) {
                tampilkanError("Data kasir tidak ditemukan di tabel tb_cashier!");
                return;
            }
            dispose();
            new FormKasir(cashier);
        }
    }

    /** Tampilkan pesan error dengan ikon, supaya jelas terlihat */
    private void tampilkanError(String pesan) {
        lblStatus.setText("⚠ " + pesan);
    }

    // ── MAIN — titik masuk aplikasi ─────────────────────────────────
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {
            // Jika Nimbus tidak tersedia, pakai tampilan default — tidak fatal
        }

        Koneksi.getConnection();   // pastikan koneksi siap sebelum GUI dibuka

        SwingUtilities.invokeLater(FormLogin::new);
    }
}