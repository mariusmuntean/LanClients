package de.marius.LanClientsViewer.services;

import de.marius.LanClientsCore.helper.LocationsHelper;
import de.marius.LanClientsViewer.domain.LanClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by marius on 05/03/2017.
 */
public class WhitelistService {

    private final Path whitelistFilePath;

    private Map<String, LanClient> whitelistedClients;

    Logger LOG = Logger.getLogger(WhitelistService.class.getName());

    public WhitelistService() throws IOException {
        whitelistFilePath = LocationsHelper.getWhitelistFilePath();

        if (!Files.exists(whitelistFilePath.getParent())) {
            Files.createDirectories(whitelistFilePath.getParent());
        }

        Files.createFile(whitelistFilePath);
    }


    public List<LanClient> getWhitelistedClients() throws IOException {

        if (whitelistedClients == null || whitelistedClients.entrySet().size() == 0) {
            reloadClients();
        }

        return whitelistedClients.values().stream().collect(Collectors.toList());
    }

    public boolean isClientWhitelisted(LanClient lanClient) {
        return whitelistedClients.containsKey(lanClient.getMacAddress());
    }

    public void whitelistClient(LanClient lanClient) throws IOException {
        if (isClientWhitelisted(lanClient)) {
            return;
        }

        whitelistedClients.put(lanClient.getMacAddress(), lanClient);

        persistWhitelist();
    }

    public void removeClientFromWhitelist(LanClient lanClient) throws IOException {
        if (!isClientWhitelisted(lanClient)) {
            return;
        }

        whitelistedClients.remove(lanClient.getMacAddress());

        persistWhitelist();
    }

    private void persistWhitelist() throws IOException {
        Files.write(whitelistFilePath,
                whitelistedClients.values().stream().map(lc -> lc.getMacAddress() + " " + lc.getName()).collect(Collectors.toList()),
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    private void reloadClients() throws IOException {
        whitelistedClients.clear();
        try (Stream<String> linesStream = Files.lines(whitelistFilePath)) {
            linesStream.forEach(s -> {
                try {
                    String[] whitelistedClientData = s.split(" ");
                    whitelistedClients.put(whitelistedClientData[0], new LanClient(whitelistedClientData[0], "", whitelistedClientData[1]));
                } catch (Exception ex) {
                    LOG.warning("Exception while parsing whitelist entry: " + s + " EXCEPTION: " + ex);
                }
            });
        }
    }
}
