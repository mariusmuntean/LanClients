package de.marius.LanClientsCore.helper;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Central place to get file and directory paths in LanClientsX
 */
public class LocationsHelper {

    public static final String LAN_CLIENTS_HOME_DIR = "LanClients";
    public static final String LAN_CLIENTS_LAN_SAMPLES_DIR = "LanSamples";
    public static final String LAN_CLIENTS_CONFIG_DIR = "Config";
    public static final String LAN_CLIENTS_WHITELIST = "whilelist.txt";
    public static final String LAN_CLIENTS_BLACKLIST = "blacklist.txt";

    public static Path getUserHomeDirectory() {
        return Paths.get(System.getProperty("user.home"));
    }

    public static Path getLanClientsHomeDir() {
        return getUserHomeDirectory().resolve(LAN_CLIENTS_HOME_DIR);
    }

    public static Path getLanClientsConfigDirectory() {
        return getLanClientsHomeDir().resolve(LAN_CLIENTS_CONFIG_DIR);
    }

    public static Path getLanSamplesDirectory() {
        return getLanClientsHomeDir().resolve(LAN_CLIENTS_LAN_SAMPLES_DIR);
    }

    public static Path getWhitelistDirectory() {
        return getLanClientsHomeDir().resolve(LAN_CLIENTS_WHITELIST);
    }

    public static Path getBlacklistDirectory() {
        return getLanClientsHomeDir().resolve(LAN_CLIENTS_BLACKLIST);
    }
}
