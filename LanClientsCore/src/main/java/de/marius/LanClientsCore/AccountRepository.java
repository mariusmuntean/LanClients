package de.marius.LanClientsCore;

import de.marius.LanClientsCore.repos.BaseRepository;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by marius on 29/03/2017.
 */
public class AccountRepository extends BaseRepository<Account> {


    public AccountRepository() throws IOException, SQLException {
    }

    public Class<Account> getRepositoryClass() {
        return Account.class;
    }




}
