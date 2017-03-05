package de.marius.LanClientsViewer.domain;

/**
 * Created by marius on 05/03/2017.
 */
public class LanClient {

    private String macAddress;
    private String ipAddress;
    private String name;

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
}
