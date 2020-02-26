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
        Label problemLabel = new Label();
        problemLabel.setText("Select Problem:");
        // Initialize buttons and button events
        Button p1button = new Button();
        p1button.setText("MSS Queue Scheme");

        p1button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                p1solution();
            }
        });

        // Setup gridPane with children for main scene
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(400,200);
        gridPane.setPadding(new Insets(10,10,10,10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(problemLabel,0,0);
        gridPane.add(p1button,0,1);

        // Setup scene and window parameters
        Scene s = new Scene(gridPane);
        primaryStage.setTitle("Homework #2");
        primaryStage.setScene(s);
        primaryStage.show();
    }

    // BEGIN SECONDARY FUNCTIONS

    // Problem 1: Create partition scheme.
    public void p1solution(){
        int globalPosX = 900;
        int globalPosY = 500;

        Stage p1stage = new Stage();

        // Create user input gridpane with customization buttons.
        Label inLabel = new Label("User Inputs");
        Label requestLabel = new Label("Grant Request");
        Button grantButton = new Button("Grant");

        GridPane settingsGrid = new GridPane();

        settingsGrid.add(inLabel,0,0);
        settingsGrid.add(requestLabel,0,1);
        settingsGrid.add(grantButton,0,2);
        settingsGrid.setAlignment(Pos.TOP_LEFT);

        Group p1Group = new Group();

        // Setup MSSs and MHs for each


        int numMSS = 10, numMH = 3,mssY = 150, mssInitX = (globalPosX/numMSS) - 10, mhShift = 30;

        mssNode logicalMSSs[] = new mssNode[numMSS];
        int mhNum = 0;

        for(int i = 0; i < logicalMSSs.length; i++){

            logicalMSSs[i] = new mssNode(i,mssInitX * (i + 1),mssY,p1Group);
            logicalMSSs[i].initQueue(p1Group);

            for(int j = 0; j < numMH; j++){

                mhNode workingMH = new mhNode(mhNum, mssInitX * (i + 1), mssY - (j + 1) * mhShift, p1Group);
                logicalMSSs[i].addMH(workingMH);
                workingMH.setMhMSS(logicalMSSs[i]);
                mhNum++;
            }

        }

        // TODO: SETUP RANDOMIZED INITIAL REQUESTS?


        grantButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Grant the new request for the hosts.

                //TODO: GRANT MH REQUEST AFTER UPDATING GLOBAL PRIORITY
                //TODO: ADD TOKEN CLASS?

                // testing requests, can remove with token ring logic implementation
                logicalMSSs[0].addMHRequest(logicalMSSs[0].mhList.get(0));
                logicalMSSs[0].addMHRequest(logicalMSSs[0].mhList.get(1));
                logicalMSSs[0].addMHRequest(logicalMSSs[0].mhList.get(2));


                for (int i = 0; i < logicalMSSs.length; i++)
                    logicalMSSs[i].updateQueue();
            }
        });

        // Update and display scene.
        p1Group.getChildren().add(settingsGrid);
        Scene mys = new Scene(p1Group, globalPosX , globalPosY);
        p1stage.setScene(mys);
        p1stage.setTitle("Problem #1 Solution");
        p1stage.show();

    }

    // END SECONDARY FUNCTIONS

}
