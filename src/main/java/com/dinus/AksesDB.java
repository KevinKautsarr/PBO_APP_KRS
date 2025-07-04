package com.dinus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class AksesDB {

    // ========================= MAHASISWA =========================
    public static ObservableList<Mhs> getDataMhs() {
        ObservableList<Mhs> listMhs = FXCollections.observableArrayList();
        try (Connection c = KoneksiDB.getConn();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM mhs");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Mhs m = new Mhs(rs.getString("nim"), rs.getString("nama"), rs.getString("alamat"));
                listMhs.add(m);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AksesDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listMhs;
    }

    public static void addDataMhs(String nim, String nama, String alamat) {
        try (Connection c = KoneksiDB.getConn();
             PreparedStatement st = c.prepareStatement("INSERT INTO mhs(nim,nama,alamat) VALUES (?,?,?)")) {
            st.setString(1, nim);
            st.setString(2, nama);
            st.setString(3, alamat);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateDataMhs(String nim, String nama, String nim_lama, String alamat) {
        try (Connection c = KoneksiDB.getConn();
             PreparedStatement st = c.prepareStatement("UPDATE mhs SET nim=?,nama=?,alamat=? WHERE nim=?")) {
            st.setString(1, nim);
            st.setString(2, nama);
            st.setString(3, alamat);
            st.setString(4, nim_lama);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void delDataMhs(String nim) {
        try (Connection c = KoneksiDB.getConn();
             PreparedStatement st = c.prepareStatement("DELETE FROM mhs WHERE nim=?")) {
            st.setString(1, nim);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ========================= USER LOGIN =========================
    public static void signUpUser(ActionEvent event, String userName, String password) {
        try (Connection conn = KoneksiDB.getConn();
             PreparedStatement check = conn.prepareStatement("SELECT * FROM user WHERE username=?")) {
            check.setString(1, userName);
            ResultSet rs = check.executeQuery();

            if (rs.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Username sudah digunakan.");
                alert.show();
            } else {
                try (PreparedStatement insert = conn.prepareStatement("INSERT INTO user (username,password) VALUES (?,?)")) {
                    insert.setString(1, userName);
                    insert.setString(2, password);
                    insert.executeUpdate();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("User berhasil dibuat.");
                    alert.show();
                    DBUtil.changeScene(event, "fMenu.fxml", "SignUp", userName);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loginUser(ActionEvent event, String userName, String password) {
        try (Connection conn = KoneksiDB.getConn();
             PreparedStatement st = conn.prepareStatement("SELECT * FROM user WHERE username=?")) {
            st.setString(1, userName);
            ResultSet rs = st.executeQuery();

            if (!rs.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("User tidak ditemukan");
                alert.show();
            } else {
                while (rs.next()) {
                    String pw = rs.getString("password");
                    if (pw.equals(password)) {
                        DBUtil.changeScene(event, "fMenu.fxml", "Login Sistem KRS", userName);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Password salah");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ========================= MATAKULIAH =========================
    public static ObservableList<Matakuliah> getDataMatakuliah() {
        ObservableList<Matakuliah> list = FXCollections.observableArrayList();
        try (Connection c = KoneksiDB.getConn();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM matakuliah");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Matakuliah(rs.getString("kode_mk"), rs.getString("nama_mk"), rs.getInt("sks")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AksesDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static void addDataMatakuliah(String kode, String nama, int sks) {
        try (Connection c = KoneksiDB.getConn();
             PreparedStatement st = c.prepareStatement("INSERT INTO matakuliah (kode_mk, nama_mk, sks) VALUES (?, ?, ?)")) {
            st.setString(1, kode);
            st.setString(2, nama);
            st.setInt(3, sks);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateDataMatakuliah(String kode, String nama, int sks) {
        try (Connection c = KoneksiDB.getConn();
             PreparedStatement st = c.prepareStatement("UPDATE matakuliah SET nama_mk = ?, sks = ? WHERE kode_mk = ?")) {
            st.setString(1, nama);
            st.setInt(2, sks);
            st.setString(3, kode);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void delDataMatakuliah(String kode) {
        try (Connection c = KoneksiDB.getConn();
             PreparedStatement st = c.prepareStatement("DELETE FROM matakuliah WHERE kode_mk = ?")) {
            st.setString(1, kode);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ========================= DOSEN =========================
    public static ObservableList<Dosen> getDataDosen() {
        ObservableList<Dosen> list = FXCollections.observableArrayList();
        try (Connection c = KoneksiDB.getConn();
             PreparedStatement ps = c.prepareStatement("SELECT kode_dsn, nama_dsn FROM dosen");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Dosen(rs.getString("kode_dsn"), rs.getString("nama_dsn")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AksesDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static void addDataDosen(String kode, String nama) {
        try (Connection c = KoneksiDB.getConn();
             PreparedStatement st = c.prepareStatement("INSERT INTO dosen (kode_dsn, nama_dsn) VALUES (?, ?)")) {
            st.setString(1, kode);
            st.setString(2, nama);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateDataDosen(String kodeBaru, String nama, String kodeLama) {
        try (Connection c = KoneksiDB.getConn();
             PreparedStatement st = c.prepareStatement("UPDATE dosen SET kode_dsn = ?, nama_dsn = ? WHERE kode_dsn = ?")) {
            st.setString(1, kodeBaru);
            st.setString(2, nama);
            st.setString(3, kodeLama);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void delDataDosen(String kode) {
        try (Connection c = KoneksiDB.getConn();
             PreparedStatement st = c.prepareStatement("DELETE FROM dosen WHERE kode_dsn = ?")) {
            st.setString(1, kode);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ========================= JADWAL =========================
    public static ObservableList<Jadwal> getDataJadwal() {
        ObservableList<Jadwal> list = FXCollections.observableArrayList();
        String sql = "SELECT j.kode_mk, j.kelas, m.nama_mk, j.hari, j.jam, j.ruang, j.kode_dosen " +
                     "FROM jadwal j JOIN matakuliah m ON j.kode_mk = m.kode_mk";
        try (Connection c = KoneksiDB.getConn();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Jadwal(
                    rs.getString("kode_mk"),
                    rs.getString("nama_mk"),
                    rs.getString("kelas"),
                    rs.getString("hari"),
                    rs.getString("jam"),
                    rs.getString("ruang"),
                    rs.getString("kode_dosen")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AksesDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static void addDataJadwal(String kodeMk, String kelas, String hari, String jam, String ruang, String kodeDosen) {
        String sql = "INSERT INTO jadwal(kode_mk, kelas, hari, jam, ruang, kode_dosen) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = KoneksiDB.getConn();
             PreparedStatement st = c.prepareStatement(sql)) {
            st.setString(1, kodeMk);
            st.setString(2, kelas);
            st.setString(3, hari);
            st.setString(4, jam);
            st.setString(5, ruang);
            st.setString(6, kodeDosen);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateDataJadwal(String kodeMk, String kelas, String hari, String jam, String ruang, String kodeDosen) {
        String sql = "UPDATE jadwal SET hari=?, jam=?, ruang=?, kode_dosen=? WHERE kode_mk=? AND kelas=?";
        try (Connection c = KoneksiDB.getConn();
             PreparedStatement st = c.prepareStatement(sql)) {
            st.setString(1, hari);
            st.setString(2, jam);
            st.setString(3, ruang);
            st.setString(4, kodeDosen);
            st.setString(5, kodeMk);
            st.setString(6, kelas);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void delDataJadwal(String kodeMk, String kelas) {
        String sql = "DELETE FROM jadwal WHERE kode_mk=? AND kelas=?";
        try (Connection c = KoneksiDB.getConn();
             PreparedStatement st = c.prepareStatement(sql)) {
            st.setString(1, kodeMk);
            st.setString(2, kelas);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
