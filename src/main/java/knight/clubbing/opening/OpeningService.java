package knight.clubbing.opening;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class OpeningService {
    private final OpeningBookDao openingBookDao;

    public static final String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=mysecretpassword";

    protected static final String memoryUrl = "jdbc:sqlite::memory:";

    public OpeningService() {
        this(jdbcUrl);
    }

    public OpeningService(String jdbcUrl) {
        try {
            Connection conn = DriverManager.getConnection(jdbcUrl);

            // Run migrations
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(conn));
            Liquibase liquibase = new Liquibase(
                    "db/changelog/openingbook-changelog.xml",
                    new ClassLoaderResourceAccessor(),
                    database
            );
            liquibase.update(new Contexts());

            Jdbi jdbi = Jdbi.create(conn).installPlugin(new SqlObjectPlugin());
            this.openingBookDao = jdbi.onDemand(OpeningBookDao.class);
        } catch (Exception e) {
            throw new RuntimeException("Database setup failed", e);
        }
    }

    public void update(OpeningBookEntry entry) {
        this.insert(entry);
    }

    public void insert(OpeningBookEntry entry) {
        this.openingBookDao.upsert(entry);
    }

    public void remove(long zobristKey, String move) {
        this.openingBookDao.delete(zobristKey, move);
    }

    public List<OpeningBookEntry> getAll() {
        return this.openingBookDao.list();
    }

    public OpeningBookEntry getBest(long zobristKey) {
        return this.openingBookDao.selectBestByZobristKey(zobristKey);
    }

    public void deleteAll() {
        this.openingBookDao.deleteAll();
    }

    public boolean exists(long zobristKey) {
        return this.openingBookDao.existsByZobristKey(zobristKey);
    }

    public int count() {
        return this.openingBookDao.countAll();
    }

    public int countByZobristKey(long zobristKey) {
        return this.openingBookDao.countByZobristKey(zobristKey);
    }
}
