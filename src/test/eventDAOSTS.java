import com.example.groupupcab302.Event;
import com.example.groupupcab302.EventDAO;
import com.example.groupupcab302.GroupUpUser;
import com.example.groupupcab302.CustomSQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class EventDAOSTS {
    private EventDAO eventDAO;
    private GroupUpUser user;

    private Event event;


    @BeforeEach
    void setUp() throws SQLException {
        eventDAO = new EventDAO();
        user = new GroupUpUser(1, "username", "firstName", "lastName", "email@example.com", "123456789", "25", "password");
    }

    // Due to the nature of the tests, if delete is run work it will fail testUpdateEvent
    // This is because delete removes a row, then test update event creates another event with the key of the event that was just deleted
    // Upon insertion into DB, DB auto increments key by +1
    // This means the event objects ID doesnt align with its entry in the db which is eventID + 1
    // Run individually for testing purposes!
    @Test
    void testUpdateEvent() throws SQLException {
        // Insert an event first to have something to update
        event = new Event(user, "Test Event", "2024-05-01", "12:00 PM", "Location", "Genre", 100, "Description", "image.jpg");
        eventDAO.insert(event);

        eventDAO.update(event, "genre", "Clubbing");
        System.out.println(event.getEventID());
        Event queriedEvent = eventDAO.getEventById(event.getEventID());
        assertEquals("Clubbing", queriedEvent.getGenre());

    }

    @Test
    void testDeleteEvent() throws SQLException {
        // Insert an event first to ensure there's something to delete
        // Call the constuctor for the event where "listofattendees" isnt required.
        // This is because when an event is created users cant pre-signup to an event so it must always be null

        // Note that when a record is deleted and another is readded, autoincrement does not set the key to the key of the deleted row
        // i.e if eventID 30 is removed then the next insertion takes 31
        // Delete is functional however its not expected for events to be deleted in this manner i.e after you insert you then constantly delete
        Event eventToDelete = new Event(user, "Test Event", "2024-05-01", "12:00 PM", "Location", "Genre", 100, "Description", "image.jpg");
        eventDAO.insert(eventToDelete);
        assertDoesNotThrow(() -> {
            eventDAO.delete(eventToDelete.getEventID());
        });

        Event event = eventDAO.getEventById(eventToDelete.getEventID());
        if (event != null){
            fail("Event with the corresponding eventID was returned from the database instead of being deleted");
        }
    }

    //Some methods are missing/not tested because they were temporarily used to devise a solution but will be replaced appropriately!
}