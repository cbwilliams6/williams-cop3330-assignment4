/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Christian Williams
 */

package ucf.assignments;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Controller {
    @FXML private TextField todo_create;
    @FXML private ListView todo_listView;

    public void populateLists() {
        String directory = ".\\src\\main\\java\\ucf\\assignments\\Lists\\";
        File dir = new File(directory);
        // getting an array of the files in the lists folder
        File[] fileList = dir.listFiles();

        // clearing all list names from the listview, so we can repopulate the entire thing
        todo_listView.getItems().clear();

        // looping through the array of files, so we can add them to the listview 1 at a time
        for (int i = 0; i < fileList.length; i++) {
            // get the name of the file currently being looped through
            String fileName = fileList[i].getName();
            // remove the file extension and add it to the listview
            fileName = fileName.replace(".txt", "");
            todo_listView.getItems().addAll(fileName);
        }
    }

    public void createList() throws IOException {
        // getting the text entered in the textfield
        String todoName = todo_create.getText();
        String directory = ".\\src\\main\\java\\ucf\\assignments\\Lists\\" + todoName + ".txt";
        File tempFile = new File(directory);

        // checking if a file with the entered name already exists in the lists folder
        if (tempFile.exists())
            System.out.print("Didn't create duplicate file");
        else {
            // if it won't dupe, then add the entered name to the listview
            todo_listView.getItems().addAll(todoName);
            // create a new .txt file with the entered name in the lists folder
            FileWriter outputFile = new FileWriter(directory);
            outputFile.close();
        }
    }

    public void removeList() {
        // getting both the index number and name of the selected item in the listview
        int listIndex = todo_listView.getSelectionModel().getSelectedIndex();
        String listName = (String) todo_listView.getSelectionModel().getSelectedItem();

        // checking if the user actually selected an item
        if (listIndex >= 0) {
            // removing the selected item from the listview
            todo_listView.getItems().remove(listIndex);
            // also removing the file associated with it
            File removedList = new File(".\\src\\main\\java\\ucf\\assignments\\Lists\\" + listName + ".txt");
            removedList.delete();
        }
    }

    public void saveList() throws IOException {
        // prompting the user to choose a directory to save the file
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);
        // getting the name of the selected file, since this option saves just 1
        String listName = (String) todo_listView.getSelectionModel().getSelectedItem();

        // checking that the user selected a file and a directory properly
        if (selectedDirectory != null && listName != null) {
            // creating a File variable for the file that already exists in the lists folder
            File oldFile = new File(".\\src\\main\\java\\ucf\\assignments\\Lists\\" + listName + ".txt");
            // another File variable for the location where the new file will be copied and saved to
            File newFile = new File(selectedDirectory.getAbsolutePath() + "\\" + listName + ".txt");

            // checking if a file with the same name already exists in the new directory
            if (newFile.exists()) {
                System.out.println("Didn't create duplicate file");
            }
            else {
                // copying the file from the lists folder to wherever the user wants it to be saved to
                Files.copy(oldFile.toPath(), newFile.toPath());
            }

            // clearing and repopulating the listview again
            populateLists();
        }
        else {
            System.out.println("Something went wrong");
        }
    }

    public void saveAll() throws IOException {
        // same as before, prompting the user to choose a directory to save everything in
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);

        // only need to check for valid directory this time, since the user doesn't select a specific file
        if (selectedDirectory != null) {
            // loops through the entire listview
            for (int i = 0; i < todo_listView.getItems().size(); i++) {
                // setting File variables based on the listview item currently being looped through
                File oldFile = new File(".\\src\\main\\java\\ucf\\assignments\\Lists\\" + todo_listView.getItems().get(i).toString() + ".txt");
                File newFile = new File(selectedDirectory.getAbsolutePath() + "\\" + todo_listView.getItems().get(i).toString() + ".txt");

                if (newFile.exists()) {
                    System.out.println("Didn't create duplicate file");
                }
                else {
                    // just copying every file 1 at a time from the lists folder to the new directory
                    Files.copy(oldFile.toPath(), newFile.toPath());
                }
            }

            // populating the listview again cause it's gotta be updated
            populateLists();
        }
        else {
            System.out.println("Something went wrong");
        }
    }

    public void loadLists() throws IOException {
        // this time the user is prompted to choose one or more files to copy into the lists folder
        FileChooser fileChooser = new FileChooser();
        // the file selection window will only show .txt files, which is what every list is saved as
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT Files", "*.txt"));
        // allows selecting multiple files at once
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

        // checking that the user actually selected at least 1 file
        if (selectedFiles != null) {
            // looping through however many files were selected
            for (int i = 0; i < selectedFiles.size(); i++) {
                // setting the directory to be in the lists folder, with whatever name the current file being looped through has
                String directory = ".\\src\\main\\java\\ucf\\assignments\\Lists\\" + selectedFiles.get(i).getName();

                // setting File variable to whatever directory the file being loaded is in right now
                File oldFile = new File(selectedFiles.get(i).getAbsolutePath());
                // another File variable that just points to the lists folder
                File newFile = new File(directory);

                // checking that the file being loaded isn't already in the lists folder
                if (newFile.exists())
                    System.out.println("Didn't create duplicate file");
                else
                    Files.copy(oldFile.toPath(), newFile.toPath());
            }

            populateLists();
        }
        else {
            System.out.println("Something went wrong");
        }
    }

    public void editListView(ActionEvent event) throws IOException {
        // loading the edit window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("appEdit.fxml"));

        // getting the index and name of the item being selected
        int listIndex = todo_listView.getSelectionModel().getSelectedIndex();
        String listName = (String) todo_listView.getSelectionModel().getSelectedItem();

        // couple variables to load the new scene
        Parent editLoader = loader.load();
        Scene editScene = new Scene(editLoader);

        // setting a label in the edit window to the name of the list, so the list can easily be found in the other class
        EditController editController = loader.getController();
        editController.setList(listName);

        // checking if a list was selected, then showing the new scene if so
        if (listIndex >= 0) {
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(editScene);
            window.show();
        }
    }

    public void viewListView(ActionEvent event) throws IOException {
        // everything was just copy-pasted from editListView so not much to explain
        FXMLLoader loader = new FXMLLoader(getClass().getResource("appView.fxml"));

        int listIndex = todo_listView.getSelectionModel().getSelectedIndex();
        String listName = (String) todo_listView.getSelectionModel().getSelectedItem();

        Parent viewLoader = loader.load();
        Scene viewScene = new Scene(viewLoader);

        ViewController viewController = loader.getController();
        viewController.setList(listName);

        if (listIndex >= 0) {
            // setting up the to-do list item table before showing the scene
            viewController.fillTable();

            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(viewScene);
            window.show();
        }
    }

    // automatically populating the list with every file in the lists folder on startup
    @FXML
    public void initialize() {
        populateLists();
    }
}