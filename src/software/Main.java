package software;

/**
 *
 * @author Kyle Hogue
 *
 *
 *  RUNTIME ERROR DESCRIPTION NEAR CODE IN Modify_Product_Controller.java Line 125
 *
 *  (Copied from above location for reading ease)
 *
 *  RUNTIME ERROR!  The below code copies the temporary list to the product specific one.
 *              *   The original problem had associated parts added during modifications still being saved even after modification was cancelled.
 *              *   A temporary associated parts list was added to allow display of intended modifications before making any changes final.
 *              *   This assocParts List acts as a buffer, copied via the forEach loop below when saved but discarded (see handleProductCancelButton) if modifications is cancelled.
 *
 *  FUTURE FEATURE COMMENT!
 *
 *      Adding an extra TableView to the main page to show associated parts when selecting a specific product.
 *      As the program currently is, the only way to see associated parts of a specific product is to select ->
 *      modify which introduces the potential for a miss-click or bad keystroke to unintentionally modify data.
 *
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Loads Application.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Main_form.fxml"));
        primaryStage.setTitle("Inventory Management Software");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
