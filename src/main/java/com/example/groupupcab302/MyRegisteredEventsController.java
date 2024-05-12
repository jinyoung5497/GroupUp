package com.example.groupupcab302;

import com.example.groupupcab302.Constants.EventTypes;
import com.example.groupupcab302.DAO.EventDAO;
import com.example.groupupcab302.Interfaces.IDisplayDynamicHeader;
import com.example.groupupcab302.Interfaces.IDisplayingEvent;
import com.example.groupupcab302.Objects.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MyRegisteredEventsController extends ParentViewController implements IDisplayingEvent, IDisplayDynamicHeader {

    @FXML
    private GridPane eventGrid;
    @FXML
    private Label HeaderText;

    private UserInformationController userInformationController = new UserInformationController();
    private EventDAO eventDAO = new EventDAO();
    private List<Event> eventList;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void initialize(){
        //Users only viewing events without intention to edit, ensure cards when clicked have one behaviour
        userInformationController.setDoesUserWantToEditTheirEvents(false);
        setHeader();
        showEventsFromDB();
    }

    public void setHeader(){
        if (userInformationController.getUserEventPreferences().equals(EventTypes.OPEN_FOR_REGISTRATION.getEventType())){
            HeaderText.setText("Your Registered Events");
        }

        else if (userInformationController.getUserEventPreferences().equals(EventTypes.CLOSED.getEventType())){
            HeaderText.setText("Your Cancelled Registered Events");
        }

        else {
            HeaderText.setText("Your Completed Registered Events");
        }

    }

    // Modify the code to only show events which the user signed up to
    public void showEventsFromDB() {
        try {
            eventList = eventDAO.getAllEvents(userInformationController.getLoggedInUserInformation(), userInformationController.getUserEventPreferences());
            int columns = 0;
            int rows = 1;
            for (int counter = 0; counter<eventList.size() ; counter++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                // Use the fxml loader object to load the event card fxml doc to retrieve the structure
                fxmlLoader.setLocation(getClass().getResource("event-cards.fxml"));

                // Create a vbox to hold the structure returned by the event card fxml file upon load
                VBox eventCardPlacementBox = fxmlLoader.load();

                // Retrieve the controller associated with the event card fxml
                EventCardController eventCardController = fxmlLoader.getController();
                // Set the content of the card by supplying the data of an event retrieved from the database
                eventCardController.setData(eventList.get(counter));

                // Check that the number of event cards created does not exceed 3 per row
                if (columns == 3){
                    // Reset the number to 0 to start placing cards at the start of the row
                    columns = 0;
                    // Increment the rows to move down 1 before starting to place the next event cards
                    ++rows;
                }
                // Add the event card to the actual grid pane for display
                eventGrid.add(eventCardPlacementBox, columns++, rows);
                // Create a margin between all event cards of 10 pixels for all sides
                GridPane.setMargin(eventCardPlacementBox, new Insets(10));
                continue;

            }

        } catch (SQLException sqlException) {
            System.out.println("There was an issue when trying to get all events which you are signed up for!" + sqlException);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
