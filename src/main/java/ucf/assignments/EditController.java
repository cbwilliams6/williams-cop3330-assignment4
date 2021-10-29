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

    public void setList(String listName) {
        todo_editListLabel.setText(listName);
    }

    public void editList() {
        String todoName = todo_edit.getText();
        String listName = todo_editListLabel.getText();
        String directory = ".\\src\\main\\java\\ucf\\assignments\\Lists\\";
        File oldFile = new File(directory + listName + ".txt");
        File newFile = new File(directory + todoName + ".txt");

        if (newFile.exists())
            System.out.print("Filename already exists");
        else
            oldFile.renameTo(newFile);
    }

    public void createListView(ActionEvent event) throws IOException {
        Parent editLoader = FXMLLoader.load(getClass().getResource("app.fxml"));
        Scene editScene = new Scene(editLoader);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(editScene);
        window.show();
    }
}
