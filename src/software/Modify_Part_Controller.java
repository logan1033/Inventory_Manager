package software;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;


/**
 * Modify Part form Controller
 * @see Part
 * @author Kyle Hogue
 */
public class Modify_Part_Controller{

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
     * ID of Part to bo Modified.
     */
    private int partID;

    /**
     *
     * @param part fills form with selected part contents based on type.
     */

    public void initInHouseData(InHouse part){
        partID = part.getId();
        idField.setText(String.valueOf(part.getId()));
        partNameField.setText(part.getName());
        partPriceField.setText(String.valueOf(part.getPrice()));
        partInvField.setText(String.valueOf(part.getStock()));
        partMinField.setText(String.valueOf(part.getMin()));
        partMaxField.setText(String.valueOf(part.getMax()));
        partMachineOrCompanyField.setText((String.valueOf(part.getMachineID())));
    }

    /**
     *
     * @param part fills form with selected part contents based on type.
     */
    public void initOutsourcedData(Outsourced part){
        outsourcedRadio.setSelected(true);
        partMachineOrCompany.setText("Company Name");
        partID = part.getId();
        idField.setText(String.valueOf(part.getId()));
        partNameField.setText(part.getName());
        partPriceField.setText(String.valueOf(part.getPrice()));
        partInvField.setText(String.valueOf(part.getStock()));
        partMinField.setText(String.valueOf(part.getMin()));
        partMaxField.setText(String.valueOf(part.getMax()));
        partMachineOrCompanyField.setText((String.valueOf(part.getCompanyName())));
    }

    /**
     * Closes form and returns to main form.
     */
    public void handleCancelButton() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Saves Modified Product to Inventory.
     */
    public void handleSaveButton() {
        try {
            String enteredPartName = partNameField.getText();
            double enteredPrice = Double.parseDouble(partPriceField.getText());
            int enteredStock = Integer.parseInt(partInvField.getText());
            int enteredMin = Integer.parseInt(partMinField.getText());
            int enteredMax = Integer.parseInt(partMaxField.getText());

            boolean validEntries = false;
            while (validEntries == false) {
                // Check for correct range
                if ((Integer.parseInt(partMinField.getText())) > (Integer.parseInt(partMaxField.getText()))) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Min must be less than Max!");
                    partMinField.clear();
                    partMaxField.clear();
                    alert.showAndWait();
                    break;
                } else if ((Integer.parseInt(partInvField.getText())) > (Integer.parseInt(partMaxField.getText())) || (Integer.parseInt(partInvField.getText())) < (Integer.parseInt(partMinField.getText()))) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Current Inventory must be more than Min and less than Max!");
                    partInvField.clear();
                    alert.showAndWait();
                    break;
                } else {
                    validEntries = true;
                }
            }

            if (inHouseRadio.isSelected() && validEntries == true) {
                int enteredMachineID = Integer.parseInt(partMachineOrCompanyField.getText());
                Inventory.updatePart(partID, new InHouse(partID, enteredPartName, enteredPrice, enteredStock, enteredMin, enteredMax, enteredMachineID));
                Stage stage = (Stage) cancelButton.getScene().getWindow();
                stage.close();
            } else if (outsourcedRadio.isSelected() && validEntries == true) {
                String enteredCompanyName = partMachineOrCompanyField.getText();

                Inventory.updatePart(partID, new Outsourced(partID, enteredPartName, enteredPrice, enteredStock, enteredMin, enteredMax, enteredCompanyName));
                Stage stage = (Stage) cancelButton.getScene().getWindow();
                stage.close();
            }
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Data Must Be Of Correct Type! \n\nAll Boxes Must Be Filled!");
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();

            alert.showAndWait();
        }
    }

    /**
     * Changes TextField Label on Radio Selection.
     */
    public void handleOutsourcedRadio() {
        partMachineOrCompany.setText("Company Name");
    }

    public void handleInHouseRadio() {
        partMachineOrCompany.setText("Machine ID");
    }
}
