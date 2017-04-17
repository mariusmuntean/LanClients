package de.marius.LanClientsCore.repos;

import de.marius.LanClientsCore.domain.LanClient;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Repository for {@link LanClient}
 */
public class LanClientRepository extends BaseRepository<LanClient>{

    public LanClientRepository() throws IOException, SQLException {
        super();
    }

    @Override
    public Class getRepositoryClass() {
        return LanClient.class;
    }
}
