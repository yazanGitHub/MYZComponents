/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myzcomponents;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class TableViewWithAddButtonExample extends Application {
  public static void main(String[] args) { launch(args); }
  @Override public void start(final Stage stage) {
    stage.setTitle("People");
//    stage.getIcons().add(new Image("http://icons.iconarchive.com/icons/icons-land/vista-people/72/Historical-Viking-Female-icon.png"));  // icon license: Linkware (Backlink to http://www.icons-land.com required)

    // create a table.
    final TableView<Person> table = new TableView<>(
      FXCollections.observableArrayList(
        new Person("Jacob", "Smith"),
        new Person("Isabella", "Johnson"),
        new Person("Ethan", "Williams"),
        new Person("Emma", "Jones"),
        new Person("Michael", "Brown")
      )
    );

    // define the table columns.
    TableColumn<Person, String> firstNameCol = new TableColumn<>("First Name");
    firstNameCol.setCellValueFactory(new PropertyValueFactory("firstName"));
    TableColumn<Person, String> lastNameCol = new TableColumn<>("Last Name");
    lastNameCol.setCellValueFactory(new PropertyValueFactory("lastName"));
    TableColumn<Person, Boolean> actionCol = new TableColumn<>("Action");
    actionCol.setSortable(false);

    // define a simple boolean cell value for the action column so that the column will only be shown for non-empty rows.
    actionCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Person, Boolean>, ObservableValue<Boolean>>() {
      @Override public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Person, Boolean> features) {
        return new SimpleBooleanProperty(features.getValue() != null);
      }
    });

    // create a cell value factory with an add button for each row in the table.
    actionCol.setCellFactory(new Callback<TableColumn<Person, Boolean>, TableCell<Person, Boolean>>() {
      @Override public TableCell<Person, Boolean> call(TableColumn<Person, Boolean> personBooleanTableColumn) {
        return new AddPersonCell(stage, table);
      }
    });

    table.getColumns().setAll(firstNameCol, lastNameCol, actionCol);
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    stage.setScene(new Scene(table));
    stage.show();
  }

  /** A table cell containing a button for adding a new person. */
  private class AddPersonCell extends TableCell<Person, Boolean> {
    // a button for adding a new person.
    final Button addButton       = new Button("Add");
    // pads and centers the add button in the cell.
    final StackPane paddedButton = new StackPane();
    // records the y pos of the last button press so that the add person dialog can be shown next to the cell.
    final DoubleProperty buttonY = new SimpleDoubleProperty();

    /**
     * AddPersonCell constructor
     * @param stage the stage in which the table is placed.
     * @param table the table to which a new person can be added.
     */
    AddPersonCell(final Stage stage, final TableView table) {
      paddedButton.setPadding(new Insets(3));
      paddedButton.getChildren().add(addButton);
      addButton.setOnMousePressed(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          buttonY.set(mouseEvent.getScreenY());
        }
      });
      addButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent actionEvent) {
          showAddPersonDialog(stage, table, buttonY.get());
          table.getSelectionModel().select(getTableRow().getIndex());
        }
      });
    }

    /** places an add button in the row only if the row is not empty. */
    @Override protected void updateItem(Boolean item, boolean empty) {
      super.updateItem(item, empty);
      if (!empty) {
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setGraphic(paddedButton);
      }
    }
  }

  /**
   * shows a dialog which displays a UI for adding a person to a table.
   * @param parent a parent stage to which this dialog will be modal and placed next to.
   * @param table the table to which a person is to be added.
   * @param y the y position of the top left corner of the dialog.
   */
  private void showAddPersonDialog(Stage parent, final TableView<Person> table, double y)
  {
    // initialize the dialog.
    final Stage dialog = new Stage();
    dialog.setTitle("New Person");
    dialog.initOwner(parent);
    dialog.initModality(Modality.WINDOW_MODAL);
    dialog.initStyle(StageStyle.UTILITY);
    dialog.setX(parent.getX() + parent.getWidth());
    dialog.setY(y);

    // create a grid for the data entry.
    GridPane grid = new GridPane();
    final TextField firstNameField = new TextField();
    final TextField lastNameField = new TextField();
    grid.addRow(0, new Label("First Name"), firstNameField);
    grid.addRow(1, new Label("Last Name"), lastNameField);
    grid.setHgap(10);
    grid.setVgap(10);
    GridPane.setHgrow(firstNameField, Priority.ALWAYS);
    GridPane.setHgrow(lastNameField, Priority.ALWAYS);

    // create action buttons for the dialog.
    Button ok = new Button("OK");
    ok.setDefaultButton(true);
    Button cancel = new Button("Cancel");
    cancel.setCancelButton(true);

    // only enable the ok button when there has been some text entered.
    ok.disableProperty().bind(firstNameField.textProperty().isEqualTo("").or(lastNameField.textProperty().isEqualTo("")));

    // add action handlers for the dialog buttons.
    ok.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override public void handle(ActionEvent actionEvent)
      {
        int nextIndex = table.getSelectionModel().getSelectedIndex() + 1;
        table.getItems().add(nextIndex, new Person(firstNameField.getText(), lastNameField.getText()));
        table.getSelectionModel().select(nextIndex);
        dialog.close();
      }
    });
    cancel.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent actionEvent) {
        dialog.close();
      }
    });

    // layout the dialog.
    HBox buttons = HBoxBuilder.create().spacing(10).children(ok, cancel).alignment(Pos.CENTER_RIGHT).build();
    VBox layout = new VBox(10);
    layout.getChildren().addAll(grid, buttons);
    layout.setPadding(new Insets(5));
    dialog.setScene(new Scene(layout));
    dialog.show();
  }

  public class Person
  {
  private StringProperty firstName;
  private StringProperty lastName;

  public Person(String firstName, String lastName) {
    setFirstName(firstName);
    setLastName(lastName);
  }

  public final void setFirstName(String value) { firstNameProperty().set(value); }
  public final void setLastName(String value) { lastNameProperty().set(value); }
  public String getFirstName() { return firstNameProperty().get(); }
  public String getLastName() { return lastNameProperty().get(); }

  public StringProperty firstNameProperty() {
    if (firstName == null) firstName = new SimpleStringProperty(this, "firstName");
    return firstName;
  }
  public StringProperty lastNameProperty() {
    if (lastName == null) lastName = new SimpleStringProperty(this, "lastName");
    return lastName;
  }
}

}