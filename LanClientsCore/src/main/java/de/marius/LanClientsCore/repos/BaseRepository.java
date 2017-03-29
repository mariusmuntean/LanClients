package de.marius.LanClientsCore.repos;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import de.marius.LanClientsCore.domain.LanClient;
import de.marius.LanClientsCore.helper.LocationsHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by marius on 29/03/2017.
 */
public abstract class BaseRepository<T> {

    private final Dao<T, String> tDao;
    private final ConnectionSource connectionSource;

    public BaseRepository() throws IOException, SQLException {

        // Make sure the path to the databse file exists
        Files.createDirectories(LocationsHelper.getLanClientsDbPath().getParent());
        String connectionString = "jdbc:sqlite:" + LocationsHelper.getLanClientsDbPath().toAbsolutePath().toString();

        // Create a connection source to our database
        connectionSource = new JdbcConnectionSource(connectionString);

        // Instantiate the DAO to handle T with String id
        Class repositoryClass = getRepositoryClass();
        tDao = DaoManager.createDao(connectionSource, getRepositoryClass());

        // Create the table for T if necessary
        TableUtils.createTableIfNotExists(connectionSource, getRepositoryClass());
    }

    public void close() throws IOException {
        connectionSource.close();
    }

    public void store(T storeMe) throws SQLException {
        tDao.create(storeMe);
    }

    public T get(String id) throws SQLException {
        return tDao.queryForId(id);
    }

    public List<T> getAll() throws SQLException {
        return tDao.queryForAll();
    }

    public abstract Class<T> getRepositoryClass();

    public T storeIfNotExists(T storeMe) throws SQLException {
        return tDao.createIfNotExists(storeMe);
    }
}
