package knight.clubbing.opening;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.util.List;

public class OpeningService {
    private final OpeningBookDao openingBookDao;

    public static final String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=mysecretpassword";

    protected static final String memoryUrl = "jdbc:sqlite::memory:";

    private final Jdbi jdbi;
    private final Handle handle;

    public OpeningService(String jdbcUrl) {
        this.jdbi = Jdbi.create(jdbcUrl).installPlugin(new SqlObjectPlugin());
        this.handle = jdbi.open(); // <- keeps the connection open
        this.openingBookDao = handle.attach(OpeningBookDao.class);
        this.openingBookDao.createTable();
    }

    public void close() {
        this.handle.close(); // <- call this at the end of your test
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
}
