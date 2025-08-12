import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Player {
    private String name;
    private Color color;
    private int position;
    private Circle piece;
    private double startX;
    private double startY;

    public Player(String name, Color color, double startX, double startY) {
        this.name = name;
        this.color = color;
        this.position = 1;

        piece=new Circle(15,color);
        piece.setStroke(Color.BLACK);
        piece.setTranslateX(startX);
        piece.setTranslateY(startY);
    }

    public String getName() {
        return name;
    }

    public Circle getPiece() {
        return piece;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
