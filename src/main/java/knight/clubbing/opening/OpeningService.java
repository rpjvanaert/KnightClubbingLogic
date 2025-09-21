package knight.clubbing.opening;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class OpeningService {
    private final OpeningBookDao openingBookDao;

    public static final String jdbcUrl = "jdbc:postgresql://127.0.0.1:5432/knight_clubbing_db?ssl=false";

    protected static final String memoryUrl = "jdbc:sqlite:file:memdb1?mode=memory&cache=shared";

    public OpeningService() {
        this(jdbcUrl);
    }

    public OpeningService(String jdbcUrl) {
        try {
            Properties props = new Properties();
            props.setProperty("user", "kce");
            props.setProperty("password", "");

            Connection migrationConn = null;
            if (jdbcUrl.startsWith("jdbc:sqlite:")) {
                migrationConn = DriverManager.getConnection(jdbcUrl, props);
                Database database = DatabaseFactory.getInstance()
                        .findCorrectDatabaseImplementation(new JdbcConnection(migrationConn));
                Liquibase liquibase = new Liquibase(
                        "db/changelog/openingbook-changelog.xml",
                        new ClassLoaderResourceAccessor(),
                        database
                );
                liquibase.update(new Contexts());

                Jdbi jdbi = Jdbi.create(migrationConn).installPlugin(new SqlObjectPlugin());
                this.openingBookDao = jdbi.onDemand(OpeningBookDao.class);
            } else {
                try (Connection conn = DriverManager.getConnection(jdbcUrl, props)) {
                    Database database = DatabaseFactory.getInstance()
                            .findCorrectDatabaseImplementation(new JdbcConnection(conn));
                    Liquibase liquibase = new Liquibase(
                            "db/changelog/openingbook-changelog.xml",
                            new ClassLoaderResourceAccessor(),
                            database
                    );
                    liquibase.update(new Contexts());
                }
                Jdbi jdbi;
                if (jdbcUrl.startsWith("jdbc:postgresql:")) {
                    PGSimpleDataSource ds = new PGSimpleDataSource();
                    ds.setUrl(jdbcUrl);
                    ds.setUser("kce");
                    ds.setPassword("");
                    jdbi = Jdbi.create(ds).installPlugin(new SqlObjectPlugin());
                } else {
                    throw new IllegalArgumentException("Unsupported JDBC URL: " + jdbcUrl);
                }
                this.openingBookDao = jdbi.onDemand(OpeningBookDao.class);
            }

            verifyConnection();
        } catch (Exception e) {
            throw new RuntimeException("Database setup failed", e);
        }
    }

    private void printConnection(Connection conn) {
        try {
            System.out.println(conn.getMetaData().getURL());
            System.out.println("Connected to " + conn.getMetaData().getDatabaseProductName() + " " + conn.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + conn.getMetaData().getDriverName() + " " + conn.getMetaData().getDriverVersion());
            System.out.println("User: " + conn.getMetaData().getUserName());
            System.out.println("Auto Commit: " + conn.getAutoCommit());
        } catch (SQLException e) {
            System.err.println("Failed to get DB metadata: " + e.getMessage());
        }
    }

    private void verifyConnection() {
        try {
            // Try a simple query to ensure the DB is accessible and the table exists
            int count = this.openingBookDao.countAll();
            // Optionally, check for a minimum expected count or other invariants
        } catch (Exception e) {
            throw new RuntimeException("Database connection established but verification failed: " + e.getMessage(), e);
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
