import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

import java.util.*;

// This class handles each node on the tree structure along with their XY coordinates and shape.
public class mssNode {

    int nodeNum;
    int nodeX;
    int nodeY;
    Circle nodeCircle;
    Color nodeColor;
    Text nodeText;
    ArrayList<mhNode> mhList;
    Queue<mhRequestLog> mssQueue;
    Label queueDesc;
    Label queueLabel;
    String queueString;


    // TODO: ADD QUEUE FOR REQUESTS AND CHILD MHS

    // Constructor for partNode without given initial XY position.
    mssNode(int nodeNum,int nodeX,int nodeY, Group nodeGroup){

        // Setup graphical vars
        this.nodeX = nodeX;
        this.nodeY = nodeY;
        this.nodeNum = nodeNum;
        this.nodeText = new Text();
        nodeText.setText("" + this.nodeNum);
        nodeText.setX(this.nodeX - 5);
        nodeText.setY(this.nodeY + 5);
        initCircle(nodeGroup);

        // Setup Logic vars
        mhList = new ArrayList<>();
        mssQueue = new LinkedList<>();
        queueString = "EMPTY!";
        queueLabel = new Label(queueString);
        queueDesc = new Label("|H H_N D? P_N|");
    }
    // Initialize node circle shape and color.
    public void initCircle(Group nodeGroup){

        nodeCircle = new Circle(nodeX,nodeY,10);
        nodeColor = Color.RED; // Standard color for standard node.
        nodeCircle.setFill(nodeColor);
        nodeGroup.getChildren().add(nodeCircle);
        nodeGroup.getChildren().add(nodeText);

    }

    // Set XY coordinates for a node and update circle position.
    public void setNodeXY(int nodeX, int nodeY){
        this.nodeX = nodeX;
        this.nodeY = nodeY;

        nodeCircle.setCenterX(this.nodeX);
        nodeCircle.setCenterY(this.nodeY);

        nodeText.setX(this.nodeX - 5);
        nodeText.setY(this.nodeY + 5);

    }

    public void addMH(mhNode workingNode){

        mhList.add(workingNode);

    }

    // TODO: ADD FUNCTION TO INITIALIZE REQUESTS NUMBERS FROM ALL MHS (RANDOMLY?)

    public void addMHRequest(mhNode requestNode){

        mhRequestLog newRequest = new mhRequestLog(requestNode.sendRequest());

        //TODO: UPDATE PRIORITY NUM BASED ON MAX FROM ALL MSS

        mssQueue.add(newRequest);

    }

    public void initQueue(Group p1Group){

        p1Group.getChildren().add(queueDesc);
        queueDesc.setLayoutX(this.nodeX - 30);
        queueDesc.setLayoutY(this.nodeY + 10);

        p1Group.getChildren().add(queueLabel);
        queueLabel.setLayoutX(this.nodeX - 30);
        queueLabel.setLayoutY(this.nodeY + 30);

    }

    public void updateQueue(){

        queueString = "";

        if(mssQueue.isEmpty()) {
            queueString = "EMPTY!";
        }else {

            Object[] workingList  = mssQueue.toArray();


            for (int i = 0; i < mssQueue.size(); i++){

                mhRequestLog workingRequest = (mhRequestLog) workingList[i];


                queueString += " " + workingRequest.mhRequest[0];
                queueString += "  " + workingRequest.mhRequest[1];

                if(workingRequest.isDeliv){
                    queueString += "      T";
                }else{
                    queueString += "      F";
                }

                queueString += "    " + workingRequest.priorityNum + "\n";

            }

        }

        queueLabel.setText(queueString);

    }



}