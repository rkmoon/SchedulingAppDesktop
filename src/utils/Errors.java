package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view_controller.ErrorWindowController;

import java.io.IOException;

/**
 * This class contains all the error messages and opens the error window. Instead of writing an error message for each
 * time an error occurs, more general error messages are contained here and called by other classes to generate the error
 * message.
 */
public class Errors {
    private static final String noSelection = "Nothing Selected";
    private static final String overlap = "Appointment Overlaps with Another";
    private static final String businessHours = "This Appointment Takes Place Outside of Business Hours";
    private static final String multipleDays = "Appointment Must Take Place During One Day";
    private static final String endBeforeStart = "Appointment Must Start Before it can End";
    private static final String fieldsIncomplete = "All Fields Must be Filled";
    private static final String customerHasAppointments = "All Customer Appointments Must be Deleted Before Customer can be Deleted";

    /**
     * opens the error menu and sends the error message to be displayed
     * @param error error message to be displayed
     * @throws IOException error opening the window
     */
    public static void openErrorMenu(String error) throws IOException {

        FXMLLoader loader = new FXMLLoader(Errors.class.getResource("../view_controller/ErrorWindow.fxml"));
        Parent root = loader.load();
        ErrorWindowController errorWindowController = loader.getController();
        errorWindowController.changeMessage(error);

        Stage stage = new Stage();

        Scene scene = new Scene(root);
        stage.setTitle("Error");
        stage.setScene(scene);
        stage.show();

    }

    public static String getNoSelection() {
        return noSelection;
    }

    public static String getOverlap() {
        return overlap;
    }

    public static String getBusinessHours() {
        return businessHours;
    }

    public static String getMultipleDays() {
        return multipleDays;
    }

    public static String getEndBeforeStart() {
        return endBeforeStart;
    }

    public static String getFieldsIncomplete() {
        return fieldsIncomplete;
    }

    public static String getCustomerHasAppointments() {
        return customerHasAppointments;
    }
}
