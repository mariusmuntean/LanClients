package de.marius.LanClientsCore.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by marius on 05/03/2017.
 */
@DatabaseTable(tableName = "lanclients")
public class LanClient {

    @DatabaseField(id = true)
    private String macAddress;

    @DatabaseField(canBeNull = true)
    private String ipAddress;

    @DatabaseField(canBeNull = true)
    private String name;

    public LanClient(){
        // no-arg constructor for ORMLite
    }

    public LanClient(String macAddress, String ipAddress, String name) {
        this.macAddress = macAddress;
        this.ipAddress = ipAddress;
        this.name = name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
