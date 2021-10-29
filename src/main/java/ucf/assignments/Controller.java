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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Controller {
    @FXML private TextField todo_create;
    @FXML private ListView todo_listView;

    public void loadLists() {
        String directory = ".\\src\\main\\java\\ucf\\assignments\\Lists\\";
        File dir = new File(directory);
        File[] fileList = dir.listFiles();

        todo_listView.getItems().clear();

        for (int i = 0; i < fileList.length; i++) {
            String fileName = fileList[i].getName();
            fileName = fileName.replace(".txt", "");
            todo_listView.getItems().addAll(fileName);
        }
    }

    public void createList() throws IOException {
        String todoName = todo_create.getText();
        String directory = ".\\src\\main\\java\\ucf\\assignments\\Lists\\" + todoName + ".txt";
        File tempFile = new File(directory);

        if (tempFile.exists())
            System.out.print("Didn't create duplicate file");
        else {
            todo_listView.getItems().addAll(todoName);
            FileWriter outputFile = new FileWriter(directory);
            outputFile.close();
        }
    }

    public void removeList() {
        int listIndex = todo_listView.getSelectionModel().getSelectedIndex();
        String listName = (String) todo_listView.getSelectionModel().getSelectedItem();

        if (listIndex >= 0) {
            todo_listView.getItems().remove(listIndex);
            File removedList = new File(".\\src\\main\\java\\ucf\\assignments\\Lists\\" + listName + ".txt");
            removedList.delete();
        }
    }

    public void editListView(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("appEdit.fxml"));

        int listIndex = todo_listView.getSelectionModel().getSelectedIndex();
        String listName = (String) todo_listView.getSelectionModel().getSelectedItem();

        Parent editLoader = loader.load();
        Scene editScene = new Scene(editLoader);

        EditController editController = loader.getController();
        editController.setList(listName);

        if (listIndex >= 0) {
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(editScene);
            window.show();
        }
    }

    public void viewListView(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("appView.fxml"));

        int listIndex = todo_listView.getSelectionModel().getSelectedIndex();
        String listName = (String) todo_listView.getSelectionModel().getSelectedItem();

        Parent viewLoader = loader.load();
        Scene viewScene = new Scene(viewLoader);

        ViewController viewController = loader.getController();
        viewController.setList(listName);

        viewController.fillTable();

        if (listIndex >= 0) {
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(viewScene);
            window.show();
        }
    }
}