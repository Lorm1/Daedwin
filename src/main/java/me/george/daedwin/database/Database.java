package me.george.daedwin.database;

import me.george.daedwin.Daedwin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    static Connection connection;
    static Database instance;

    String host, database, username, password;
    int port;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public static boolean isConnected() {
        return (connection == null ? false : true);
    }

    public static Connection getConnection() {
        return connection;
    }

    public void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host+ ":" + this.port + "/" + this.database, this.username, this.password);
        }
    }

    public void connect() {
        host = "localhost";
        port = 3306;
        database = "daedwin";
        username = "root";
        password = "";

        BukkitRunnable r = new BukkitRunnable() {
            public void run() {
                try {
                    openConnection();
                    Statement statement = connection.createStatement();

                    System.out.println("[Database] Connected to Database: " + database.toUpperCase());
                } catch (SQLException exception) {
                    exception.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };

        r.runTaskAsynchronously(Daedwin.getInstance());
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
                Bukkit.getConsoleSender().sendMessage("[Database] Disconnecting Database: " + database.toUpperCase());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
