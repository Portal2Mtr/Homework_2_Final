import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

// This class handles each node on the tree structure along with their XY coordinates and shape.
public class mssNode {

    // General node vars.
    int nodeNum;
    int nodeX;
    int nodeY;
    Circle nodeCircle;
    Color nodeColor;
    Text nodeText;
    Line nodeLine;
    String name;

    // Constructor for partNode without given initial XY position.
    mssNode(int nodeNum,int nodeX,int nodeY, Group nodeGroup){

        this.name = name;
        this.nodeX = 200;
        this.nodeY = 50;
        this.nodeNum = nodeNum;
        this.nodeText = new Text();
        nodeText.setText("" + this.nodeNum);
        nodeText.setX(this.nodeX - 5);
        nodeText.setY(this.nodeY + 5);
        initCircle(nodeGroup);

    }

    // Initialize node circle shape and color.
    public void initCircle(Group nodeGroup){

        this.nodeCircle = new Circle(nodeX,nodeY,10);
        this.nodeColor = Color.GREEN; // Standard color for standard node.

        // Make root node a special color
        if(this.name == "root"){
            this.nodeColor = Color.BLUE;
        }

        nodeCircle.setFill(nodeColor);

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

}