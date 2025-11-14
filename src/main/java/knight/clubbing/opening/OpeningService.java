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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;
import java.util.logging.LogManager;

public class OpeningService {
    private final OpeningBookDao openingBookDao;

    public static final String jdbcUrl = "jdbc:postgresql://172.18.0.2:5432/knight_clubbing_db?ssl=false";

    protected static final String memoryUrl = "jdbc:sqlite:file:memdb1?mode=memory&cache=shared";

    public OpeningService() {
        this(jdbcUrl);
    }

    public OpeningService(String jdbcUrl) {
        silenceAll();
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

    private void verifyConnection() {
        try {
            int count = this.openingBookDao.countAll();
        } catch (Exception e) {
            throw new RuntimeException("Database connection established but verification failed: " + e.getMessage(), e);
        }
    }

    public static void silenceAll() {
        LogManager.getLogManager().reset();

        try {
            Class<?> bridge = Class.forName("org.slf4j.bridge.SLF4JBridgeHandler");
            Method remove = bridge.getMethod("removeHandlersForRootLogger");
            Method install = bridge.getMethod("install");
            remove.invoke(null);
            install.invoke(null);
        } catch (ClassNotFoundException cnf) {
            // SLF4J bridge not on classpath — ignore
        } catch (Throwable t) {
            // Any other reflection failure — ignore to avoid breaking startup
        }


        final PrintStream original = System.out;
        PrintStream filtered = new PrintStream(new ByteArrayOutputStream()) {
            @Override
            public void println(String x) {
                if (x == null) return;
                if (x.startsWith("Liquibase:") || x.startsWith("Running Changeset") || x.contains("Rows affected")) {
                    return;
                }
                original.println(x);
            }
        };
        System.setOut(filtered);
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
