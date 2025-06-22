package knight.clubbing.opening;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpeningServiceTest {

    private OpeningService openingService;

    @BeforeEach
    void setUp() {
        this.openingService = new OpeningService(OpeningService.memoryUrl);
        this.openingService.deleteAll();
        assertNotNull(openingService);
        assertTrue(openingService.getAll().isEmpty());
    }

    @Test
    void getAll() {
        openingService.insert(new OpeningBookEntry(1L, "d2d4", 13, 5, "33-33-33"));
        openingService.insert(new OpeningBookEntry(1L, "e2e4", 11, 5, "33-33-33"));
        assertEquals(2, openingService.getAll().size());
    }

    @Test
    void AddAndGet() {
        OpeningBookEntry entry = new OpeningBookEntry(1L, "e2e4", 13, 5, "33-33-33");
        openingService.insert(entry);

        assertEquals(1, openingService.getAll().size());
        assertEquals(entry, openingService.getAll().getFirst());
        assertEquals(entry, openingService.getBest(1L));
    }

    @Test
    void getBest() {
        String expected = "d2d4";
        openingService.insert(new OpeningBookEntry(1L, expected, 13, 5, "33-33-33"));
        openingService.insert(new OpeningBookEntry(1L, "e2e4", 11, 5, "33-33-33"));
        assertEquals(2, openingService.getAll().size());
        assertEquals(expected, openingService.getBest(1L).getMove());
    }

    @Test
    void updateShouldReplaceExistingEntry() {
        OpeningBookEntry original = new OpeningBookEntry(1L, "e2e4", 10, 3, "00-00-00");
        OpeningBookEntry updated = new OpeningBookEntry(1L, "e2e4", 25, 7, "11-11-11");

        openingService.insert(original);
        openingService.update(updated);

        OpeningBookEntry result = openingService.getBest(1L);
        assertEquals(updated, result);
        assertEquals(1, openingService.getAll().size());
    }

    @Test
    void removeShouldDeleteEntry() {
        OpeningBookEntry entry1 = new OpeningBookEntry(1L, "e2e4", 10, 3, "00-00-00");
        OpeningBookEntry entry2 = new OpeningBookEntry(1L, "d2d4", 12, 4, "11-11-11");

        openingService.insert(entry1);
        openingService.insert(entry2);
        openingService.remove(1L, "e2e4");

        assertEquals(1, openingService.getAll().size());
        assertEquals("d2d4", openingService.getAll().getFirst().getMove());
    }

    @Test
    void getBestReturnsNullWhenKeyNotFound() {
        assertNull(openingService.getBest(9999L));
    }

    @Test
    void closeShouldNotThrow() {
        assertDoesNotThrow(() -> openingService.close());
    }
}