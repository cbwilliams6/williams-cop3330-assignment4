/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Christian Williams
 */

package ucf.assignments;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;
import java.time.LocalDate;

public class Item {
    private SimpleStringProperty description;
    private LocalDate dueDate;
    private SimpleStringProperty completion;

    public Item(String description, LocalDate dueDate, String completion) {
        this.description = new SimpleStringProperty(description);
        this.dueDate = dueDate;
        this.completion = new SimpleStringProperty(completion);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description = new SimpleStringProperty(description);
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getCompletion() {
        return completion.get();
    }

    public void setCompletion(String completion) {
        this.completion = new SimpleStringProperty(completion);
    }
}
