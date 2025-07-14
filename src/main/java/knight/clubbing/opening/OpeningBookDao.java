package knight.clubbing.opening;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface OpeningBookDao {

    @SqlUpdate("""
        INSERT INTO opening_book (zobrist_key, move, score, depth)
        VALUES (:zobristKey, :move, :score, :depth)
        ON CONFLICT (zobrist_key, move) DO UPDATE SET
            score = excluded.score,
            depth = excluded.depth
    """)
    int upsert(@BindBean OpeningBookEntry entry);

    @SqlQuery("SELECT * FROM opening_book")
    @RegisterBeanMapper(OpeningBookEntry.class)
    List<OpeningBookEntry> list();

    @SqlQuery("""
        SELECT * FROM opening_book
        WHERE zobrist_key = :zobristKey
        ORDER BY score DESC
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

    @SqlQuery("""
    SELECT EXISTS (
        SELECT 1 FROM opening_book WHERE zobrist_key = :zobristKey
    )
""")
    boolean existsByZobristKey(@Bind("zobristKey") long zobristKey);

}
