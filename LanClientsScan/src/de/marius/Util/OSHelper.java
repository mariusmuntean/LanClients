package de.marius.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A helper to determine the current OS.
 * <p>
 * Created by Marius on 05.02.2017.
 */
public class OSHelper {

    public enum OS {
        Windows,
        Linux,
        macOS,
        Unix,
        Unknown
    }

    private static String osName;
    private static String cliName;

    static {
        osName = System.getProperty("os.name");
        cliName = isWindows() ? "C:\\WINDOWS\\system32\\cmd.exe" : "/bin/bash";
    }

    public static boolean isWindows() {
        return osName.trim().toLowerCase().contains("win");
    }

    public static boolean isUnix() {
        return !isWindows();
    }

    public static boolean isMac() {
        return osName != null && (osName.toLowerCase().trim().contains("mac") || osName.trim().toLowerCase().contains("darwin"));
    }

    public static OS getCurrentOS() {
        if (isWindows())
            return OS.Windows;

        if (isLinux())
            return OS.Linux;

        if (isMac())
            return OS.macOS;

        if (isUnix())
            return OS.Unix;

        return OS.Unknown;
    }

    public static boolean isLinux() {
        return osName != null && (osName.toLowerCase().trim().contains("nux"));
    }

    public static String executeCommandAndGetOutput(String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        StringBuilder commandOutput = new StringBuilder();
        while ((line = input.readLine()) != null) {
            commandOutput.append(line);
            commandOutput.append(System.getProperty("line.separator"));
        }
        return commandOutput.toString();
    }

    public static String getMacForIp(String hostIp) {
        String arpCommand = getOsSpecificArpCommand(hostIp);
        String arpOutput = null;
        try {
            arpOutput = executeCommandAndGetOutput(arpCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (arpOutput == null) {
            return "";
        }

        String extractedMac = extractMacAddr(arpOutput);
        System.out.println("Extracted MAC: " + extractedMac);
        if (!extractedMac.trim().isEmpty()) {
            return extractedMac;
        }

        return "";
    }

    private static String getOsSpecificArpCommand(String hostIp) {
        String arpCommand = "";
        switch (getCurrentOS()) {
            case Windows:
                arpCommand = "arp -a " + hostIp;
                break;
            case Linux:
                arpCommand = "/usr/sbin/arp -a " + hostIp;
                break;
            case macOS:
                arpCommand = "arp " + hostIp;
                break;
            case Unix:
                throw new UnsupportedOperationException("arp command isn't yet supported for Unix");
//                break;
            case Unknown:
                throw new UnsupportedOperationException("arp command isn't supported for your OS");
//                break;
        }

        return arpCommand;
    }

    private static String extractMacAddr(String str) {
        String arr[] = str.split(" ");
        for (String string : arr) {
            if (string.trim().length() == 17) {
                return string.trim().toUpperCase();
            }
        }
        return "<none>";
    }
}
