/**
 * Created by Miner on 1/30/2020.
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.input.*;

import java.util.ArrayList;
import java.util.Queue;

public class SceneBuild extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize labels
        Label problemlabel = new Label();
        problemlabel.setText("Select Problem:");
        // Initialize buttons and button events
        Button p1button = new Button();
        p1button.setText("MSS Queue Scheme");

        p1button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                p1solution();
            }
        });

        // Setup gridpane with children for main scene
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(400,200);
        gridPane.setPadding(new Insets(10,10,10,10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(problemlabel,0,0);
        gridPane.add(p1button,0,1);

        // Setup scene and window parameters
        Scene s = new Scene(gridPane);
        primaryStage.setTitle("Homework #1");
        primaryStage.setScene(s);
        primaryStage.show();
    }

    // BEGIN SECONDARY FUNCTIONS

    // Problem 1: Create partition scheme.
    public void p1solution(){
        int globalPosX = 900;
        int globalPosY = 250;

        Stage p1stage = new Stage();

        // Create user input gridpane with customization buttons.
        Label inLabel = new Label("User Inputs");
        Label requestLabel = new Label("Grant Request");
        Button grantButton = new Button("Grant");

        GridPane settingsgrid = new GridPane();

        settingsgrid.add(inLabel,0,0);
        settingsgrid.add(requestLabel,0,1);
        settingsgrid.add(grantButton,0,2);
        settingsgrid.setAlignment(Pos.TOP_LEFT);

        Group p1Group = new Group();

        // Setup MSSs and MHs for each

        // TODO: SETUP INITIAL MSS AND MHS (10XMSS,3XMH/MSS)

        // Update caller node position on user update.
        grantButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ENTER)) {
                    // Grant the new request for the hosts.

                    //TODO: GRANT MH REQUEST AFTER UPDATING GLOBAL PRIORITY


                }
            }
        });


        // Update and display scene.
        p1Group.getChildren().add(settingsgrid);
        Scene mys = new Scene(p1Group, globalPosX , globalPosY);
        p1stage.setScene(mys);
        p1stage.setTitle("Problem #1 Solution");
        p1stage.show();

    }



    // END SECONDARY FUNCTIONS

}
