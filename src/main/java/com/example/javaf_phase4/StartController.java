package com.example.javaf_phase4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.io.IOException;

public class StartController {
    @FXML
    private RadioButton addFestivalRunRadButton;

    @FXML
    private RadioButton addConcertRadButton;

    @FXML
    private RadioButton statisticsRadButton;

    public void continueAction(ActionEvent e) throws IOException {
        if(addConcertRadButton.isSelected()) {
            Parent root = FXMLLoader.load(getClass().getResource("addConcert-view.fxml"));
            Stage s = (Stage) ((Node) e.getSource()).getScene().getWindow();
            s.setTitle("Add Concert");
            s.setScene(new Scene(root));
            s.show();
        }
        if(addFestivalRunRadButton.isSelected()) {
            Parent root = FXMLLoader.load(getClass().getResource("addFestivalRun-view.fxml"));
            Stage s = (Stage) ((Node) e.getSource()).getScene().getWindow();
            s.setTitle("Add Festival Run");
            s.setScene(new Scene(root, 600, 153));
            s.show();
        }

        if (statisticsRadButton.isSelected()) {
            Parent root = FXMLLoader.load(getClass().getResource("statistics-view.fxml"));
            Stage s = (Stage) ((Node) e.getSource()).getScene().getWindow();
            s.setTitle("Statistics");
            s.setScene(new Scene(root, 600, 153));
            s.show();
        }

    }
}