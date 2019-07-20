package com.example.vahel.PelDungeon.SQLiteStatement;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLiteWeapons {
    /**
     * Connect to the test.db database
     *
     * @return the Connection object
     */
    private Connection connect() throws ClassNotFoundException {
        // SQLite connection string
        String sDriverName = "org.sqlite.JDBC";
        Class.forName(sDriverName);
        String url = "jdbc:sqlite:C:/Users/Vahel/Items.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Connection to SQLite has been not established.");
        }
        return conn;
    }

    /**
     * Insert a new row into the warehouses table
     *
     * @param name
     * @param capacity
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void insert(String name, Integer base_strenght, Integer accuracy, Double delay) {
        String sql = "INSERT INTO weapons_melee(name, base_strenght, accuracy, delay) VALUES(?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, base_strenght);
            pstmt.setInt(3, accuracy);
            pstmt.setDouble(4, delay);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void main(String[] args) {

        SQLiteWeapons app = new SQLiteWeapons();
        // insert three new rows
        app.insert("XY", 3000, 2000, 2.3);
    }
}
