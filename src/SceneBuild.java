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
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.input.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

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
    // Setup global vars...

    int requestingMHNum;
    int numMSS = 10;
    int numMH = 3;
    int maxMHNum = numMSS * numMH - 1;
    mssNode logicalMSSs[] = new mssNode[numMSS];
    token globalToken;

    public void p1solution(){
        int globalPosX = 900;
        int globalPosY = 600;

        Stage p1stage = new Stage();

        // Create user input gridpane with customization buttons.
        Label inLabel = new Label("User Inputs");
        Button grantButton = new Button("Grant Request");
        Label selectLabel = new Label("MH Request #:");
        TextField MHField = new TextField("0");
        Button requestButton = new Button("Make Request");

        GridPane settingsGrid = new GridPane();

        settingsGrid.add(inLabel,0,0);
        settingsGrid.add(grantButton,0,1);
        settingsGrid.add(selectLabel,0,2);
        settingsGrid.add(MHField,0,3);
        settingsGrid.add(requestButton,0,4);

        settingsGrid.setAlignment(Pos.TOP_LEFT);

        Group p1Group = new Group();

        // Setup requesting global vars...
        requestingMHNum = 0;

        // Setup MSSs and MHs for each
        int mssY = 250, mssInitX = (globalPosX/numMSS) - 10, mhShift = 30;
        int mhNum = 0;

        for(int i = 0; i < logicalMSSs.length; i++){

            logicalMSSs[i] = new mssNode(i,mssInitX * (i + 1),mssY,p1Group);
            logicalMSSs[i].initQueue(p1Group);

            for(int j = 0; j < numMH; j++){

                mhNode workingMH = new mhNode(mhNum, mssInitX * (i + 1),mssY - (j + 1) * mhShift, p1Group);
                logicalMSSs[i].addMH(workingMH);
                workingMH.setMhMSS(logicalMSSs[i]);
                mhNum++;
            }

        }

        // Setup 15 'randomized' initial requests from MHs...
        int numInitReq = 15;
        initRandRequest(numInitReq);

        // Initialize token...
        globalToken = new token(logicalMSSs.length);
        logicalMSSs[globalToken.MSSLoc].setHasToken(true);

        grantButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Grant the new request for the hosts.

                while(true) {

                    // Check if next request's MH is in MSS domain...
                    if (logicalMSSs[globalToken.MSSLoc].canGrantRequest(globalToken)) {
                        // MH is here! grant the MH request and delete it from all other MSSs...
                        mhRequestLog deleteRequest = logicalMSSs[globalToken.MSSLoc].grantRequest(globalToken);
                        globalDeleteRequest(deleteRequest);

                        //Move token to next MSS...
                        moveToken();
                        break;

                    }else{
                        // MH is not here... move on to the next MSS...
                        moveToken();

                        // Add visual delay to show token moving
                        try {
                            Thread.sleep(1000);
                        } catch(InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }

                    }

                }

                updateMSSQueues();
            }
        });

        // Update caller node position on user update.
        MHField.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ENTER)) {
                    // Get new caller position.
                    requestingMHNum = Integer.parseInt(MHField.getText());

                }
            }
        });

        requestButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Create a new MH request...

                mhNode requestingMH = findMHByNum(requestingMHNum);
                requestingMH.mhMSS.addMHRequest(requestingMH);
                globalMSSUpdate(requestingMH.mhMSS);
                updateMSSQueues();
            }
        });

        // Update and display scene.
        p1Group.getChildren().add(settingsGrid);
        Scene mys = new Scene(p1Group, globalPosX , globalPosY);
        p1stage.setScene(mys);
        p1stage.setTitle("Problem #1 Solution");
        p1stage.show();

    }

    // Returns the MH object given a MH number...
    mhNode findMHByNum(int requestingMHNum){

        for(int i = 0; i < logicalMSSs.length; i++){

            for (int j = 0; j < logicalMSSs[i].mhList.size(); j++){

                if(logicalMSSs[i].mhList.get(j).nodeNum == requestingMHNum){

                    return logicalMSSs[i].mhList.get(j);

                }

            }

        }

        return null;

    }

    // Initializes a given number of requests in all MSS queues...
    void initRandRequest(int numRequests){

        Random r = new Random();

        for(int i = 0; i < numRequests; i++){

            int nextMHNum = r.nextInt(maxMHNum + 1);

            mhNode nextMH = findMHByNum(nextMHNum);

            nextMH.mhMSS.addMHRequest(nextMH);
            globalMSSUpdate(nextMH.mhMSS);

        }

        updateMSSQueues();

    }

    // Visually updates all MSS queues...
    void updateMSSQueues(){

        for (int i = 0; i < logicalMSSs.length; i++)
            logicalMSSs[i].updateQueue();

    }

    // Finds the global priority for a new request and sends it to all MSSs
    void globalMSSUpdate(mssNode recievingMSS){

        mhRequestLog newRequest = recievingMSS.mssQueue.peek();

        Integer globalPrior[] = new Integer[logicalMSSs.length];

        // Have each MSS find their max temporary request number for the new request...
        for(int i = 0; i < logicalMSSs.length; i++){
            globalPrior[i] = logicalMSSs[i].addAndFindMax(newRequest);
        }

        // Find the max of the new temporary requests...
        int newPrior = Collections.max(Arrays.asList(globalPrior));

        // Send the new global max to each of the MSSs, have them resort their queues...
        for(int i = 0; i < logicalMSSs.length; i++){
            logicalMSSs[i].updateGlobalRequest(newRequest,newPrior);
            logicalMSSs[i].resortQueue();
        }

    }

    // Moves the token to the next MSS in the token ring...
    void moveToken(){
        logicalMSSs[globalToken.MSSLoc].setHasToken(false);
        globalToken.incMSSLoc();
        logicalMSSs[globalToken.MSSLoc].setHasToken(true);
    }

    // Deletes the given request from each of the MSSs...
    void globalDeleteRequest(mhRequestLog deleteRequest){

        for(int i = 0; i < logicalMSSs.length; i++){
            logicalMSSs[i].deleteRequest(deleteRequest);
        }

    }


    // END SECONDARY FUNCTIONS

}
