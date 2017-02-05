package de.marius.LanClientsViewer.services;

import de.marius.LanClientsViewer.domain.LanSample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Marius on 05.02.2017.
 */
public class LanSampleService {

    private static final String LAN_SAMPLES_DIR;

    static {
        LAN_SAMPLES_DIR = System.getProperty("os.name").toLowerCase().contains("win") ? "C:\\Users\\Mariu\\reachability" : "/home/pi/reachability";
    }

    public List<LanSample> getAll() {
        Path lanSamplesPath = Paths.get(LAN_SAMPLES_DIR);
        if (!Files.exists(lanSamplesPath)) {
            return new ArrayList<>();
        }

        try {
            return Files.list(lanSamplesPath).map(LanSample::new).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
