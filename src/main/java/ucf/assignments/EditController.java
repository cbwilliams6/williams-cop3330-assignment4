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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class EditController {
    @FXML private Label todo_editListLabel;
    @FXML private TextField todo_edit;

    // just sets a label to the name of the list being edited, cause its way easier to get the list name this way
    public void setList(String listName) {
        todo_editListLabel.setText(listName);
    }

    public void editList() {
        // and this is why it was easier to just set a label as the list name
        String todoName = todo_edit.getText();
        String listName = todo_editListLabel.getText();
        String directory = ".\\src\\main\\java\\ucf\\assignments\\Lists\\";
        // setting a couple files with the old and new list names
        File oldFile = new File(directory + listName + ".txt");
        File newFile = new File(directory + todoName + ".txt");

        // checking that it won't rename to a file that already exists
        if (newFile.exists())
            System.out.print("Filename already exists");
        else
            // renames the old file to whatever the new name is
            oldFile.renameTo(newFile);
    }

    public void createListView(ActionEvent event) throws IOException {
        // this is all just the same stuff as the ones found in Controller, nothing new
        Parent editLoader = FXMLLoader.load(getClass().getResource("app.fxml"));
        Scene editScene = new Scene(editLoader);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(editScene);
        window.show();
    }
}
