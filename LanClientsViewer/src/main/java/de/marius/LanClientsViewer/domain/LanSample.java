package de.marius.LanClientsViewer.domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by Marius on 05.02.2017.
 */
public class LanSample {
    private Path filePath;
    private Date sampleDate;
    private String ip;
    private String mac;

    private HashMap<String, String> clients;

    public LanSample(Path path) {
        filePath = path;

        extractSampleData();
    }

    private void extractSampleData() {
        String fileContent = null;
        try {
            fileContent = new String(Files.readAllBytes(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] fileContentLines = fileContent.split(System.getProperty("line.separator"));
        if (fileContentLines.length == 0) {
            return;
        }

        try {
            String fileNameNoExt = filePath.getFileName().toString();
            if (fileNameNoExt.indexOf(".") > 0)
                fileNameNoExt = fileNameNoExt.substring(0, fileNameNoExt.lastIndexOf("."));
            sampleDate = new Date(Long.parseLong(fileNameNoExt));
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 2; i < fileContentLines.length; i++) {
            String[] currentLineData = fileContentLines[i].split(" ");
            ip = currentLineData[0];
            mac = currentLineData[0];

            clients = new HashMap<>();
            clients.put(ip, mac);
        }
    }

    public Path getFilePath() {
        return filePath;
    }

    public Date getSampleDate() {
        return sampleDate;
    }

    public String getIp() {
        return ip;
    }

    public String getMac() {
        return mac;
    }

    public HashMap<String, String> getClients() {
        return clients;
    }

    @Override
    public String toString() {
        return sampleDate.toString() + " (" + clients.entrySet().size() + ") clients";
    }
}
