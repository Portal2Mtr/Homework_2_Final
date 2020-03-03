import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

import java.util.*;

// This class handles each MSS node with their XY coordinates and shape.
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
    boolean hasToken;

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

    // Adds a MH to the list of MHs for the MSS...
    public void addMH(mhNode workingNode){

        mhList.add(workingNode);

    }

    // Adds a request to the MSS queue... (Note: does not update global priority)
    public void addMHRequest(mhNode requestNode){

        mhRequestLog newRequest = new mhRequestLog(requestNode.sendRequest());

        mssQueue.add(newRequest);

    }

    // Visually initializes the Queue for the MSS
    public void initQueue(Group p1Group){

        p1Group.getChildren().add(queueDesc);
        queueDesc.setLayoutX(this.nodeX - 30);
        queueDesc.setLayoutY(this.nodeY + 10);

        p1Group.getChildren().add(queueLabel);
        queueLabel.setLayoutX(this.nodeX - 30);
        queueLabel.setLayoutY(this.nodeY + 30);

    }

    // Visually updates the queue for the MSS...
    public void updateQueue(){

        queueString = "";

        if(mssQueue.isEmpty()) {
            queueString = "EMPTY!";
        }else {

            Object[] workingList  = mssQueue.toArray();


            for (int i = 0; i < mssQueue.size(); i++){

                mhRequestLog workingRequest = (mhRequestLog) workingList[i];


                queueString += "| " + workingRequest.mhRequest[0];
                queueString += "  " + workingRequest.mhRequest[1];

                if(workingRequest.isDeliv){
                    queueString += "      T";
                }else{
                    queueString += "      F";
                }

                queueString += "    " + workingRequest.priorityNum + "|\n";

            }

        }

        queueLabel.setText(queueString);

    }

    // Updates the node color if the MSS has the token or not...
    public void setHasToken(boolean hasToken) {
        this.hasToken = hasToken;

        if(hasToken){
            this.nodeColor = Color.GOLD;
        }else{
            this.nodeColor = Color.RED;
        }

        nodeCircle.setFill(this.nodeColor);

    }

    public boolean canGrantRequest(token globalToken){

        mhRequestLog workingReq = mssQueue.peek();
        int workingMHNum = workingReq.mhRequest[0];
        int workingMHIdx;
        boolean mhIsHere = false;

        // Check if the MH for the current request exists in this MSS's domain
        for(int i = 0; i < mhList.size(); i++){

            if(workingMHNum == mhList.get(i).nodeNum){

                mhIsHere = true;
                workingMHIdx = i;
                break;

            }

        }

        if(!mhIsHere){
            return false;
        }

        // MH is here, check if request can be granted yet...

        // Check if the MH loop count is less than the token's loop count
        if(workingReq.mhRequest[1] > globalToken.loopCount){
            return false;
        }

        // The MH request is less than token's loop count, check the global priority...

        if(workingReq.priorityNum > globalToken.priorityCount){
            return false;
        }

        // Check if deliverable...

        if(!workingReq.isDeliv){
            return false;
        }

        // Request matches all criteria, request can be granted!
        return true;

    }

    public int addAndFindMax(mhRequestLog newRequest){

        int tempMaxPrior = 0;
        int oldMax = 0;

        Object[] workingList  = mssQueue.toArray();

        for(int i = 0; i < mssQueue.size(); i++){

            mhRequestLog workingRequest = (mhRequestLog) workingList[i];

            if(workingRequest.priorityNum >= oldMax)
                oldMax = workingRequest.priorityNum;

        }

        // Set priority number to one greater than the max of the current queue.
        tempMaxPrior = oldMax + 1;
        newRequest.priorityNum = tempMaxPrior;

        // Add request to queue with temporary priority number.
        mssQueue.add(newRequest);

        return tempMaxPrior;

    }

    public void updateGlobalRequest(int newPrior, mhRequestLog newRequest){

        Object[] workingList  = mssQueue.toArray();
        mhRequestLog workingRequest;

        for(int i = 0; i < mssQueue.size(); i++){

            workingRequest = (mhRequestLog) workingList[i];

            if(workingRequest.equals(newRequest)){

                Object[] updateList  = mssQueue.toArray();
                // Parse requests from queue into array...
                mhRequestLog[] updateRequests = new mhRequestLog[mssQueue.size()];
                for(int j = 0; j < mssQueue.size(); j++){
                    updateRequests[j] = (mhRequestLog) workingList[j];
                }

                updateRequests[i].priorityNum = newPrior;
                updateRequests[i].isDeliv = true;

                // Empty mssQueue
                while(!mssQueue.isEmpty())
                    mssQueue.poll();

                // Add back sorted array of requests

                for(int j = 0; j < updateRequests.length; j++){
                    mssQueue.add(updateRequests[j]);
                }

            }

        }

    }

    public void resortQueue(){

        Object[] workingList  = mssQueue.toArray();

        // Parse requests from queue into array...
        mhRequestLog[] workingRequests = new mhRequestLog[mssQueue.size()];
        for(int i = 0; i < mssQueue.size(); i++){
            workingRequests[i] = (mhRequestLog) workingList[i];
        }

        // Sort Requests based on priority number in ascending order...
        Arrays.sort(workingRequests);

        // Empty mssQueue
        while(!mssQueue.isEmpty())
            mssQueue.poll();

        // Add back sorted array of requests

        for(int i = 0; i < workingRequests.length; i++){
            mssQueue.add(workingRequests[i]);
        }


    }

    public mhRequestLog grantRequest(token globalToken){

        mhRequestLog deleteRequest = mssQueue.poll();
        int workingMHNum = deleteRequest.mhRequest[0];

        int workingMHIdx = 0;

        // Get the working MH idx...
        for(int i = 0; i < mhList.size(); i++){

            if(workingMHNum == mhList.get(i).nodeNum){

                workingMHIdx = i;
                break;

            }

        }

        mhNode workingMH = mhList.get(workingMHIdx);

        // Signify request granting by changing node color...
        workingMH.nodeColor = Color.BLUE;
        workingMH.nodeCircle.setFill(workingMH.nodeColor);

        // Return the polled request for deletion...
        return deleteRequest;

    }

    public void deleteRequest(mhRequestLog deleteRequest){

        mssQueue.remove(deleteRequest);

    }

}