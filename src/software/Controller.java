package software;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *  Main form Controller Class
 *
 *  FUTURE FEATURE COMMENT!
 *
 *      Adding an extra TableView to the main page to show associated parts when selecting a specific product.
 *      As the program currently is, the only way to see associated parts of a specific product is to select, then
 *      modify which introduces the potential for a miss-click or bad keystroke to unintentionally modify data.
 *
 * @author Kyle Hogue
 */
public class Controller {

    @FXML
    public Button exitButton;
    @FXML
    public Button deleteProductButton;
    @FXML
    public Button modifyProductButton;
    @FXML
    public Button productAddButton;
    @FXML
    public TableView<Product> productTableview;
    @FXML
    public TextField productSearchField;
    @FXML
    public Button deletePartButton;
    @FXML
    public Button modifyPartButton;
    @FXML
    public Button addPartButton;
    @FXML
    public TableView<Part> partTableview;
    @FXML
    public TextField partSearchField;

    /**
     * Initializes basic test Inventory, TableView Columns, Search Fields, and Populates TableViews.
     * @see Inventory
     */
    public void initialize() {
        //Populate Inventory With Generic Sample Data
        Inventory.addPart(new InHouse(1, "Brakes", 12.99, 10, 1, 20, 570242));
        Inventory.addPart(new Outsourced(2, "Tire", 14.99, 15, 1, 20, "Goodyear"));
        Inventory.addPart(new InHouse(3, "Seat", 15.99, 10, 1, 20, 570242));
        Inventory.addPart(new InHouse(4, "Rim", 56.99, 15, 1, 20, 570242));

        Inventory.addProduct(new Product(1, "Giant Bike", 299.99, 15, 1, 20));
        Inventory.addProduct(new Product(2, "Tricycle", 99.99, 5, 1, 20));
        Inventory.addProduct(new Product(3, "Unicycle", 99.99, 20, 1, 20));

        //Create partTableview columns
        TableColumn<Part, Integer> id = new TableColumn<>("Part ID");
        TableColumn<Part, String> name = new TableColumn<>("Part Name");
        TableColumn<Part, Double> price = new TableColumn<>("Price");
        TableColumn<Part, Integer> stock = new TableColumn<>("Inv");
        TableColumn<Part, Integer> min = new TableColumn<>("Min");
        TableColumn<Part, Integer> max = new TableColumn<>("Max");
        partTableview.getColumns().addAll(id, name, price, stock, min, max);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        min.setCellValueFactory(new PropertyValueFactory<>("min"));
        max.setCellValueFactory(new PropertyValueFactory<>("max"));

        //Create productTableview columns
        TableColumn<Product, Integer> pid = new TableColumn<>("Product ID");
        TableColumn<Product, String> pname = new TableColumn<>("Product Name");
        TableColumn<Product, Double> pprice = new TableColumn<>("Price");
        TableColumn<Product, Integer> pstock = new TableColumn<>("Inv");
        TableColumn<Product, Integer> pmin = new TableColumn<>("Min");
        TableColumn<Product, Integer> pmax = new TableColumn<>("Max");
        productTableview.getColumns().addAll(pid, pname, pprice, pstock, pmin, pmax);

        pid.setCellValueFactory(new PropertyValueFactory<>("id"));
        pname.setCellValueFactory(new PropertyValueFactory<>("name"));
        pprice.setCellValueFactory(new PropertyValueFactory<>("price"));
        pstock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        pmin.setCellValueFactory(new PropertyValueFactory<>("min"));
        pmax.setCellValueFactory(new PropertyValueFactory<>("max"));

        //default sort
        partTableview.getSortOrder().add(id);
        productTableview.getSortOrder().add(pid);

        //Populate Tableviews
        partTableview.setItems(Inventory.getAllParts());
        productTableview.setItems(Inventory.getAllProducts());

        //Search field for partTableView
        FilteredList<Part> filteredPartList = new FilteredList<>(Inventory.getAllParts(), b -> true);

        partSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredPartList.setPredicate(part -> {
                //empty search field
                if (newValue == null || newValue.isEmpty()) { return true; }

                String lowerCaseFilter = newValue.toLowerCase();

                //name field search
                if (part.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                //id field search
                } else if (String.valueOf(part.getId()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else return false;
                //An error message here is suggested in the rubric but this looks and feels cleaner.
            });
        });

        SortedList<Part> sortedPartList = new SortedList<>(filteredPartList);

        sortedPartList.comparatorProperty().bind(partTableview.comparatorProperty());

        partTableview.setItems(sortedPartList);

        //Search field for productTableView
        FilteredList<Product> filteredProductList = new FilteredList<>(Inventory.getAllProducts(), b -> true);

        productSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredProductList.setPredicate(product -> {
                //empty search field
                if (newValue == null || newValue.isEmpty()) { return true; }

                String lowerCaseFilter = newValue.toLowerCase();

                //name field search
                if (product.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                    //id field search
                } else if (String.valueOf(product.getId()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else return false;
                //An error message here is suggested in the rubric but this looks and feels cleaner.
            });
        });

        SortedList<Product> sortedProductList = new SortedList<>(filteredProductList);

        sortedProductList.comparatorProperty().bind(productTableview.comparatorProperty());

        productTableview.setItems(sortedProductList);
    }

    /**
     * Opens Add Part form on button click.
     */
    public void handleAddPartButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Add_Part_form.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Part");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {e.printStackTrace();}
    }

    /**
     * Opens Modify Part form on button click, passes selected Product.
     * @see Product
     */
    public void handleModifyPartButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Modify_Part_form.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();

            Modify_Part_Controller controller = fxmlLoader.getController();
            if (partTableview.getSelectionModel().getSelectedItem().getClass().getName() == "software.InHouse") {
                controller.initInHouseData((InHouse)partTableview.getSelectionModel().getSelectedItem());
            } else {
                controller.initOutsourcedData((Outsourced)partTableview.getSelectionModel().getSelectedItem());
            }
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modify Part");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!!");
            alert.setHeaderText(null);
            alert.setContentText("Must Select A Part To Modify!");

            alert.showAndWait();
        }
    }

    /**
     * Removes Selected Part from Inventory on button click.
     */
    public void handleDeletePartButton() {
        int selectedIndex = partTableview.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            try {
                if (Inventory.deletePart(partTableview.getItems().get(selectedIndex))) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Success!!");
                    alert.setHeaderText(null);
                    alert.setContentText("Part Deleted!!");

                    alert.showAndWait();
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!!");
                alert.setHeaderText(null);
                alert.setContentText("Something Went Wrong! Try Again.");

                alert.showAndWait();

            }
        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Must Select an Item to Delete.");

            alert.showAndWait();
        }
    }

    /**
     * Opens Add Product form on button click.
     */
    public void handleProductAddButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Add_Product_form.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Product");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {e.printStackTrace();}
    }

    /**
     * Opens Modify Product form on button click, passes selected Product from TableView.
     */
    public void handleModifyProductButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Modify_Product_form.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();

            Modify_Product_Controller controller = fxmlLoader.getController();
            controller.initData(productTableview.getSelectionModel().getSelectedItem());

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modify Product");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!!");
            alert.setHeaderText(null);
            alert.setContentText("Must Select A Part To Modify!");

            alert.showAndWait();
        }
    }

    /**
     * Removes Selected Product from Inventory on button click, checks for Associated Parts.
     */
    public void handleDeleteProductButton() {
        int selectedIndex = productTableview.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            try {
                if (productTableview.getItems().get(selectedIndex).getAssociatedParts().isEmpty()) {
                    if (Inventory.deleteProduct(productTableview.getItems().get(selectedIndex))) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Success!!");
                        alert.setHeaderText(null);
                        alert.setContentText("Product Deleted!!");

                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!!");
                    alert.setHeaderText(null);
                    alert.setContentText("Product Has Associated Parts!!");

                    alert.showAndWait();
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!!");
                alert.setHeaderText(null);
                alert.setContentText("Something Went Wrong! Try Again.");

                alert.showAndWait();
            }
        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Must Select an Item to Delete.");

            alert.showAndWait();
        }
    }

    /**
     * Closes App.
     */
    public void handleExitButton() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

}
