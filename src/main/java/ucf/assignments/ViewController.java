/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Christian Williams
 */

package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ViewController {
    @FXML private Label todo_viewListLabel;
    @FXML private TableView<Item> todo_tableView;
    @FXML private TableColumn<Item, String> todo_tableDescription;
    @FXML private TableColumn<Item, LocalDate> todo_tableDate;
    @FXML private TableColumn<Item, String> todo_tableCompletion;
    @FXML private TextField descriptionField;
    @FXML private DatePicker dateField;
    @FXML private ChoiceBox completionField;

    public void setList(String listName) {
        todo_viewListLabel.setText(listName);
    }

    public void fillTable() throws FileNotFoundException {
        String directory = ".\\src\\main\\java\\ucf\\assignments\\Lists\\" + todo_viewListLabel.getText() + ".txt";
        Scanner itemScanner = new Scanner(new File(directory));

        while (itemScanner.hasNextLine()) {
            String[] data = itemScanner.nextLine().split(", ");
            LocalDate dueDate = LocalDate.parse(data[1]);
            Item addedItem = new Item(data[0], dueDate, data[2]);
            todo_tableView.getItems().add(addedItem);
        }
    }

    public void editDescription() {
    }

    public void newItem() throws IOException {
        if (descriptionField.getText().isEmpty() || dateField.getValue() == null || completionField.getValue() == null) {
            System.out.println("Didn't add item because something was left empty");
        }
        else {
            List <List<String>> itemList = new ArrayList<>();
            Item newItem = new Item(descriptionField.getText(), dateField.getValue(), (String) completionField.getSelectionModel().getSelectedItem());

            todo_tableView.getItems().add(newItem);

            itemWriter(itemList);
        }
    }

    public void removeItem() throws IOException {
        List <List<String>> itemList = new ArrayList<>();
        Item itemSelected = todo_tableView.getSelectionModel().getSelectedItem();
        int itemIndex = todo_tableView.getSelectionModel().getSelectedIndex();

        todo_tableView.getItems().remove(itemSelected);

        if (itemIndex >= 0) {
            itemWriter(itemList);
        }
    }

    public void showAll() throws FileNotFoundException {
        todo_tableView.getItems().clear();
        fillTable();
    }

    public void showIncomplete() throws FileNotFoundException {
        showAll();

        for (int i = todo_tableView.getItems().size() - 1; i >= 0; i--) {
            Item item = todo_tableView.getItems().get(i);
            if (item.getCompletion().equals("true")) {
                todo_tableView.getItems().remove(item);
            }
        }
    }

    public void showComplete() throws FileNotFoundException {
        showAll();

        for (int i = todo_tableView.getItems().size() - 1; i >= 0; i--) {
            Item item = todo_tableView.getItems().get(i);
            if (item.getCompletion().equals("false")) {
                todo_tableView.getItems().remove(item);
            }
        }
    }

    public void itemWriter(List <List<String>> itemList) throws IOException {
        FileWriter writer = new FileWriter(".\\src\\main\\java\\ucf\\assignments\\Lists\\" + todo_viewListLabel.getText() + ".txt");

        for (int i = 0; i < todo_tableView.getItems().size(); i++) {
            Item item = todo_tableView.getItems().get(i);
            itemList.add(new ArrayList<>());
            itemList.get(i).add(item.getDescription());
            itemList.get(i).add(", " + item.getDueDate());
            itemList.get(i).add(", " + item.getCompletion());
        }

        for (int j = 0; j < itemList.size(); j++) {
            for (int k = 0; k < itemList.get(j).size(); k++) {
                writer.write(itemList.get(j).get(k));
            }
            if (j == itemList.size() - 1)
                continue;
            else
                writer.write("\n");
        }

        writer.close();
    }

    public void createListView(ActionEvent event) throws IOException {
        Parent editLoader = FXMLLoader.load(getClass().getResource("app.fxml"));
        Scene editScene = new Scene(editLoader);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(editScene);
        window.show();
    }

    @FXML
    public void initialize() {
        todo_tableDescription.setCellValueFactory(new PropertyValueFactory<Item, String>("description"));
        todo_tableCompletion.setCellValueFactory(new PropertyValueFactory<Item, String>("completion"));
        todo_tableDate.setCellValueFactory(new PropertyValueFactory<Item, LocalDate>("dueDate"));

        ObservableList<String> completionChoices = FXCollections.observableArrayList("false", "true");
        completionField.setItems(completionChoices);
    }
}