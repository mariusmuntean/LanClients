package de.marius.LanClientsCore;

import de.marius.LanClientsCore.helper.LocationsHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.UUID;

/**
 * Created for fooling around
 */
public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        Main main = new Main();
        main.ormLite();
//        main.nativeSQLite();
    }

    private void ormLite() throws IOException, SQLException {
        AccountRepository accountRepository = new AccountRepository();

        for (int i = 0; i < 10; i++) {

            String name = "Jim Smith" + UUID.randomUUID().toString();
            Account account = new Account(name, UUID.randomUUID().toString());

            accountRepository.store(account);
        }


        accountRepository.getAll().forEach(account -> {
            System.out.println("Account: " + account);
        });


        accountRepository.close();
    }

    private void nativeSQLite() throws IOException {
        Connection connection = null;
        try {
            // create a database connection
            Files.createDirectories(LocationsHelper.getLanClientsDbPath().getParent());
            String connectionString = "jdbc:sqlite:" + LocationsHelper.getLanClientsDbPath().toAbsolutePath().toString();
            connection = DriverManager.getConnection(connectionString);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists person");
            statement.executeUpdate("create table person (id integer, name string)");
            statement.executeUpdate("insert into person values(1, 'leo')");
            statement.executeUpdate("insert into person values(2, 'yui')");
            ResultSet rs = statement.executeQuery("select * from person");
            while (rs.next()) {
                // read the result set
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
    }
}
