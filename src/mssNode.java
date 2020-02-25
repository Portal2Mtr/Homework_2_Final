import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.ArrayList;

// This class handles each node on the tree structure along with their XY coordinates and shape.
public class mssNode {

    int nodeNum;
    int nodeX;
    int nodeY;
    Circle nodeCircle;
    Color nodeColor;
    Text nodeText;
    ArrayList<mhNode> mhList;

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

}