package software;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Add Part form Controller
 * @see Part
 * @author Kyle Hogue
 */
public class Add_Part_Controller{

    @FXML
    public RadioButton inHouseRadio;
    @FXML
    public ToggleGroup PartSource;
    @FXML
    public RadioButton outsourcedRadio;
    @FXML
    public TextField partInvField;
    @FXML
    public TextField partNameField;
    @FXML
    public TextField partPriceField;
    @FXML
    public TextField partMaxField;
    @FXML
    public TextField partMinField;
    @FXML
    public Label partMachineOrCompany;
    @FXML
    public TextField partMachineOrCompanyField;
    @FXML
    public Button saveButton;
    @FXML
    public Button cancelButton;
    @FXML
    public Label titleLabel;
    @FXML
    public TextField idField;

    /**
     * Closes form and returns to main form.
     */
    public void handleCancelButton() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Saves New Part to Inventory with input error checking.
     * @see Part
     * @see Inventory
     */
    public void handleSaveButton() {
        try {
            int newid = Inventory.getAllParts().size() + 1;
            String enteredPartName = partNameField.getText();
            double enteredPrice = Double.parseDouble(partPriceField.getText());
            int enteredStock = Integer.parseInt(partInvField.getText());
            int enteredMin = Integer.parseInt(partMinField.getText());
            int enteredMax = Integer.parseInt(partMaxField.getText());
            if (inHouseRadio.isSelected()) {
                int enteredMachineID = Integer.parseInt(partMachineOrCompanyField.getText());
                //Check for correct range
                if (enteredMin > enteredMax) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Min must be less than Max!");
                    partMinField.clear();
                    partMaxField.clear();
                    alert.showAndWait();
                } else if (enteredStock > enteredMax || enteredStock < enteredMin) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Current Inventory must be more than Min and less than Max!");
                    partInvField.clear();
                    alert.showAndWait();
                } else {
                    Inventory.addPart(new InHouse(newid, enteredPartName, enteredPrice, enteredStock, enteredMin, enteredMax, enteredMachineID));
                    Stage stage = (Stage) cancelButton.getScene().getWindow();
                    stage.close();
                }
            } else if (outsourcedRadio.isSelected()) {
                String enteredCompanyName = partMachineOrCompanyField.getText();
                //Check for correct range
                if (enteredMin > enteredMax) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Min must be less than Max!");
                    partMinField.clear();
                    partMaxField.clear();
                    alert.showAndWait();
                } else if (enteredStock > enteredMax || enteredStock < enteredMin) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Current Inventory must be more than Min and less than Max!");
                    partInvField.clear();
                    alert.showAndWait();
                } else {
                    Inventory.addPart(new Outsourced(newid, enteredPartName, enteredPrice, enteredStock, enteredMin, enteredMax, enteredCompanyName));
                    Stage stage = (Stage) cancelButton.getScene().getWindow();
                    stage.close();
                }
            }
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Data Must Be Of Correct Type! \n\nAll Boxes Must Be Filled!");
            partNameField.clear();
            partInvField.clear();
            partPriceField.clear();
            partMinField.clear();
            partMaxField.clear();
            partMachineOrCompanyField.clear();

            alert.showAndWait();
        }
    }

    /**
     * Changes Label with Radio Selection to Company Name.
     */
    public void handleOutsourcedRadio() {
        partMachineOrCompany.setText("Company Name");
    }

    /**
     * Changes Label with Radio Selection to Machine ID.
     */
    public void handleInHouseRadio() {
        partMachineOrCompany.setText("Machine ID");
    }
}
