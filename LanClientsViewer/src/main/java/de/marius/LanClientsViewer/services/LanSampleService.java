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

    public List<LanSample> getAll() {
        Path lanSamplesPath = getReachabilityDirPath();
        if (!Files.exists(lanSamplesPath)) {
            return new ArrayList<>();
        }

        try {
            return Files.list(lanSamplesPath).filter(path -> path.toAbsolutePath().toString().endsWith(".txt")).map(LanSample::new).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private Path getReachabilityDirPath() {
        return Paths.get(System.getProperty("user.home")).resolve("reachability");
    }
}
