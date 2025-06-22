package knight.clubbing.opening;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface OpeningBookDao {
    @SqlUpdate("""
        CREATE TABLE IF NOT EXISTS opening_book (
            zobrist_key BIGINT NOT NULL,
            move TEXT NOT NULL,
            weight INTEGER DEFAULT 0,
            depth INTEGER DEFAULT 1,
            wdl TEXT,
            PRIMARY KEY (zobrist_key, move)
        );
    """)
    void createTable();

    @SqlUpdate("""
            INSERT INTO opening_book (zobrist_key, move, weight, depth, wdl)
            VALUES (:zobristKey, :move, :weight, :depth, :wdl)
            ON CONFLICT (zobrist_key, move) DO UPDATE SET
                weight = excluded.weight,
                depth = excluded.depth,
                wdl = excluded.wdl
            """)
    @GetGeneratedKeys
    int upsert(@BindBean OpeningBookEntry entry);

    @SqlQuery("SELECT * FROM opening_book")
    @RegisterBeanMapper(OpeningBookEntry.class)
    List<OpeningBookEntry> list();

    @SqlQuery("""
    SELECT * FROM opening_book
    WHERE zobrist_key = :zobristKey
    ORDER BY weight DESC
    LIMIT 1
""")
    @RegisterBeanMapper(OpeningBookEntry.class)
    OpeningBookEntry selectBestByZobristKey(@Bind("zobristKey") long zobristKey);


    @SqlUpdate("""
        DELETE FROM opening_book WHERE zobrist_key = :zobristKey AND move = :move
    """)
    void delete(@Bind("zobristKey") long zobristKey, @Bind("move") String move);

    @SqlUpdate("DELETE FROM opening_book")
    void deleteAll();
}
