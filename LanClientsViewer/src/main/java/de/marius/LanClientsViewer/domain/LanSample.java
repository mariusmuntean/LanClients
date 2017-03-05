package de.marius.LanClientsViewer.domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * A single sampling of the network
 */
public class LanSample {
    private Path filePath;
    private Date sampleDate;

    private HashMap<String, String> clients;

    public LanSample(Path path) {
        filePath = path;

        extractSampleDate();
    }

    private void extractSampledClients() {

        // Make the traversal of the file a bit more efficient by reading and parsing line by line
        Stream<String> linesStream = null;
        try {
            linesStream = Files.lines(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (linesStream == null) {
            markSampleEmpty();
            return;
        }

        clients = new HashMap<>();
        linesStream.skip(2).forEach(line -> {
            String[] currentLineData = line.split(" ");

            clients.put(currentLineData[0], currentLineData[1]);
        });
    }

    private void markSampleEmpty() {
        clients = new HashMap<>();
    }

    private void extractSampleDate() {
        try {
            String fileNameNoExt = filePath.getFileName().toString();
            if (fileNameNoExt.indexOf(".") > 0)
                fileNameNoExt = fileNameNoExt.substring(0, fileNameNoExt.lastIndexOf("."));
            sampleDate = new Date(Long.parseLong(fileNameNoExt));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Path getFilePath() {
        return filePath;
    }

    public Date getSampleDate() {
        return sampleDate;
    }

    public HashMap<String, String> getClients() {
        if(clients == null){
            extractSampledClients();
        }
        return clients;
    }

    @Override
    public String toString() {
        return sampleDate.toString();
    }
}
