package de.marius.LanClientsCore;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by marius on 29/03/2017.
 */
@DatabaseTable(tableName = "accounts")
public class Account {
    @DatabaseField(id = true)
    private String name;

    @DatabaseField(canBeNull = false)
    private String password;

    public Account() {
        // no-arg constructor for orm
    }

    public Account(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "[name: " + name + ", password: " + password + "]";
    }
}
