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
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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

    // same kinda thing as edit window, sets a label to the list name for ez access
    public void setList(String listName) {
        todo_viewListLabel.setText(listName);
    }

    public void fillTable() throws FileNotFoundException {
        // setting up a scanner so we can read through the .txt file
        String directory = ".\\src\\main\\java\\ucf\\assignments\\Lists\\" + todo_viewListLabel.getText() + ".txt";
        Scanner itemScanner = new Scanner(new File(directory));

        // looping through until theres no items left
        while (itemScanner.hasNextLine()) {
            // setting up an array with a split of ", " since that's just how I formatted the data
            String[] data = itemScanner.nextLine().split(", ");
            // changing the data for the date from a string to a LocalDate type
            LocalDate dueDate = LocalDate.parse(data[1]);
            // creating an item with the extracted data and adding it to the table
            Item addedItem = new Item(data[0], dueDate, data[2]);
            todo_tableView.getItems().add(addedItem);
        }
    }

    public void newItem() throws IOException {
        // checking if any of the 3 entry parts are empty
        if (descriptionField.getText().isEmpty() || dateField.getValue() == null || completionField.getValue() == null) {
            System.out.println("Didn't add item because something was left empty");
        }
        else {
            /* itemList should've just been put in the itemWriter method, but it's a bit late for that
               ... I don't wanna touch it, cause it works though it's inefficient */
            List <List<String>> itemList = new ArrayList<>();
            // creating a new item with all the entered data
            Item newItem = new Item(descriptionField.getText(), dateField.getValue(), (String) completionField.getSelectionModel().getSelectedItem());

            // then adding that new item to the table
            todo_tableView.getItems().add(newItem);

            // and calling on the method that writes it to the .txt file
            itemWriter(itemList);
        }
    }

    public void removeItem() throws IOException {
        List <List<String>> itemList = new ArrayList<>();
        // getting the name and index of the item selected
        Item itemSelected = todo_tableView.getSelectionModel().getSelectedItem();
        int itemIndex = todo_tableView.getSelectionModel().getSelectedIndex();

        // removing the item from the table based on whatever its name is
        todo_tableView.getItems().remove(itemSelected);

        // checking if an item is actually selected before removing it from the .txt file
        if (itemIndex >= 0) {
            itemWriter(itemList);
        }
    }

    public void showAll() throws FileNotFoundException {
        // clears the table before filling it all back in again, so nothing dupes
        todo_tableView.getItems().clear();
        fillTable();
    }

    public void showIncomplete() throws FileNotFoundException {
        // refreshing the table, cause when going from incomplete -> complete or vice versa it would dupe items
        showAll();

        /* looping through the entire table, but from bottom item up
           when I tried a regular forward loop, it would crash since the indexes would change and it'd try to find something that didn't exist anymore */
        for (int i = todo_tableView.getItems().size() - 1; i >= 0; i--) {
            // getting the item being looped through
            Item item = todo_tableView.getItems().get(i);
            // then checking if the completion status is true or false
            if (item.getCompletion().equals("true")) {
                // if true, then we remove it since we only want to see incomplete ones
                todo_tableView.getItems().remove(item);
            }
        }
    }

    public void showComplete() throws FileNotFoundException {
        // everything here is the same as showing incomplete, but obviously checking if an item is incomplete instead, so we can remove it
        showAll();

        for (int i = todo_tableView.getItems().size() - 1; i >= 0; i--) {
            Item item = todo_tableView.getItems().get(i);
            if (item.getCompletion().equals("false")) {
                todo_tableView.getItems().remove(item);
            }
        }
    }

    public void itemWriter(List <List<String>> itemList) throws IOException {
        // this is the function where the list of lists matters
        // setting up a filewriter for the current list
        FileWriter writer = new FileWriter(".\\src\\main\\java\\ucf\\assignments\\Lists\\" + todo_viewListLabel.getText() + ".txt");

        // just looping through the whole table
        for (int i = 0; i < todo_tableView.getItems().size(); i++) {
            // creating an item for each time we loop through
            Item item = todo_tableView.getItems().get(i);
            /* adding an arraylist to the overall list, so we'll have a list with a bunch of arraylists by the end
               each arraylist will then have all the parameters for a single item / row */
            itemList.add(new ArrayList<>());
            // adding each parameter to the arraylist and separating by ", "
            itemList.get(i).add(item.getDescription());
            itemList.get(i).add(", " + item.getDueDate());
            itemList.get(i).add(", " + item.getCompletion());
        }

        // looping through the entire list, so we can loop through every arraylist
        for (int j = 0; j < itemList.size(); j++) {
            // then looping through a single arraylist to write its data
            for (int k = 0; k < itemList.get(j).size(); k++) {
                // writing every parameter to the .txt file, with separations
                writer.write(itemList.get(j).get(k));
            }
            // checking if we're on the 2nd to last arraylist
            if (j == itemList.size() - 1)
                continue;
            else
                // printing a linebreak if not, since we don't want an extra linebreak at the very end
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

    public void editDescription(TableColumn.CellEditEvent editedDescription) throws IOException {
        // this method checks if a description cell is being edited, so we can change it in the table and file
        List <List<String>> itemList = new ArrayList<>();
        Item itemSelected = todo_tableView.getSelectionModel().getSelectedItem();
        int itemIndex = todo_tableView.getSelectionModel().getSelectedIndex();

        String directory = ".\\src\\main\\java\\ucf\\assignments\\Lists\\" + todo_viewListLabel.getText() + ".txt";
        Scanner itemScanner = new Scanner(new File(directory));

        // looping through a currently selected .txt file line by line
        while (itemScanner.hasNextLine()) {
            // creating a new item, same as the old one except with a new entered description
            Item newItem = new Item(editedDescription.getNewValue().toString(), itemSelected.getDueDate(), itemSelected.getCompletion());
            // simple array with the usual split
            String[] data = itemScanner.nextLine().split(", ");
            // making sure that the text in the table is the same as the text in the array, so we work on the right item
            if (data[0].equals(itemSelected.getDescription())) {
                // remove the item with the old, unedited description
                todo_tableView.getItems().remove(itemSelected);
                // add in a new item with the new entered description, making sure to place it at the same spot as the old item
                todo_tableView.getItems().add(itemIndex, newItem);
                // update the whole table
                itemWriter(itemList);
            }
        }
    }

    public void editDate(TableColumn.CellEditEvent editedDate) throws IOException {
        // placeholder cause this part is so confusing
    }

    public void editCompletion(TableColumn.CellEditEvent editedCompletion) throws IOException {
        // this is almost all just the same as editDescription method
        List <List<String>> itemList = new ArrayList<>();
        Item itemSelected = todo_tableView.getSelectionModel().getSelectedItem();
        int itemIndex = todo_tableView.getSelectionModel().getSelectedIndex();

        String directory = ".\\src\\main\\java\\ucf\\assignments\\Lists\\" + todo_viewListLabel.getText() + ".txt";
        Scanner itemScanner = new Scanner(new File(directory));

        while (itemScanner.hasNextLine()) {
            // this parts the only difference, just gets the new value for completion instead of description of course
            Item newItem = new Item(itemSelected.getDescription(), itemSelected.getDueDate(), editedCompletion.getNewValue().toString());
            String[] data = itemScanner.nextLine().split(", ");
            if (data[0].equals(itemSelected.getDescription())) {
                todo_tableView.getItems().remove(itemSelected);
                todo_tableView.getItems().add(itemIndex, newItem);
                itemWriter(itemList);
            }
        }
    }

    @FXML
    public void initialize() {
        todo_tableDescription.setCellValueFactory(new PropertyValueFactory<Item, String>("description"));
        todo_tableCompletion.setCellValueFactory(new PropertyValueFactory<Item, String>("completion"));
        todo_tableDate.setCellValueFactory(new PropertyValueFactory<Item, LocalDate>("dueDate"));

        // setting the choicebox options to be either false or true
        ObservableList<String> completionChoices = FXCollections.observableArrayList("false", "true");
        completionField.setItems(completionChoices);

        // setting the table to be editable
        todo_tableView.setEditable(true);
        // when we double click on a cell, either render a textfield or a choicebox inside it, depending on what's being edited
        todo_tableDescription.setCellFactory(TextFieldTableCell.forTableColumn());
        todo_tableCompletion.setCellFactory(ChoiceBoxTableCell.forTableColumn("false", "true"));
    }
}