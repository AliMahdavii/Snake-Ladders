package Snake_Ladders;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

public class Snake_Ladder extends Application {

    private static final int tileSize=60;
    private static final int height=10;
    private static final int width=10;

    private GridPane board;
    private Random random=new Random();

    private Map<Integer,Integer> snake_ladders=new HashMap<>();
    private List<Player> players=new ArrayList<>();
    private int currentPlayerIndex=0;

    private Image[] diceImages=new Image[6];
    private ImageView diceView;

    @Override
    public void start(Stage stage) throws Exception {
        board=new GridPane();
        board.setAlignment(Pos.CENTER);

        Pane gameLayer=new Pane();

        gameLayer.getChildren().add(board);

        int number=1;
        boolean left_right=true;

        for (int row=height-1; row>=0; row--){
            for (int col=0; col<width; col++){
                Rectangle tile=new Rectangle(tileSize,tileSize);

                tile.setFill((row+col)%2==0?Color.BEIGE:Color.LIGHTGRAY);
                tile.setStroke(Color.BLACK);

                Text text=new Text(String.valueOf(number));
                text.setFill(Color.BLACK);

                StackPane cell=new StackPane(tile,text);

                if (left_right) {
                    board.add(cell,col,row);
                } else {
                    board.add(cell,width-col-1,row);
                }
                number++;
            }
            left_right=!left_right;
        }

        setupSnake_ladders();
        drawSnake_ladders(gameLayer,snake_ladders);

        createPlayers(gameLayer);

        diceImages[0]=new Image(getClass().getResource("diceImages/1.PNG").toExternalForm());
        diceImages[1]=new Image(getClass().getResource("diceImages/2.PNG").toExternalForm());
        diceImages[2]=new Image(getClass().getResource("diceImages/3.PNG").toExternalForm());
        diceImages[3]=new Image(getClass().getResource("diceImages/4.PNG").toExternalForm());
        diceImages[4]=new Image(getClass().getResource("diceImages/5.PNG").toExternalForm());
        diceImages[5]=new Image(getClass().getResource("diceImages/6.PNG").toExternalForm());

        diceView=new ImageView(diceImages[0]);
        diceView.setFitHeight(60);
        diceView.setFitWidth(60);

        Button rollDice=new Button();
        rollDice.setGraphic(diceView);
        rollDice.setOnAction(actionEvent -> {
            rollDice.setGraphic(diceView);
            rollDiceAndMove();
        });

        VBox root=new VBox(20,gameLayer,rollDice);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(tileSize*width,tileSize*height+250);

        Scene scene=new Scene(root);
        stage.setTitle("SNAKE_LADDER");
        stage.setScene(scene);
        stage.show();
    }

    private void setupSnake_ladders(){
        snake_ladders.put(4,14);
        snake_ladders.put(20,38);
        snake_ladders.put(9,31);
        snake_ladders.put(28,84);
        snake_ladders.put(40,59);
        snake_ladders.put(51,67);
        snake_ladders.put(63,81);
        snake_ladders.put(71,91);

        snake_ladders.put(17,7);
        snake_ladders.put(54,34);
        snake_ladders.put(62,19);
        snake_ladders.put(64,60);
        snake_ladders.put(87,24);
        snake_ladders.put(93,73);
        snake_ladders.put(95,75);
        snake_ladders.put(99,78);
    }

    private void rollDiceAndMove() {
        Player current=players.get(currentPlayerIndex);

        int diceValue=random.nextInt(6)+1;
        System.out.println("Tas: "+diceValue);

        diceView.setImage(diceImages[diceValue-1]);

        int newPos=current.getPosition()+diceValue;
        if (newPos>width*height) newPos=width*height;

        if (snake_ladders.containsKey(newPos)){
            int jumpPos=snake_ladders.get(newPos);
            System.out.println("Move from "+newPos+" to "+jumpPos);
            newPos=jumpPos;
        }

        current.setPosition(newPos);
        movePlayer(current);

        if (newPos==width*height) {
            System.out.println("You WIN!!!!  player: "+current.getName());
        } else {
            nextTurn();
        }
    }

    private void drawSnake_ladders(Pane board, Map<Integer,Integer> snake_ladders){
        for (Map.Entry<Integer,Integer> entry:snake_ladders.entrySet()){
            int start=entry.getKey();
            int end=entry.getValue();

            double[] startCoords =getTileCoordinates(start);
            double[] endCoords=getTileCoordinates(end);

            Line line=new Line(startCoords[0],startCoords[1],endCoords[0],endCoords[1]);

            if (start>end){
                line.setStroke(Color.RED);
            } else {
                line.setStroke(Color.GREEN);
            }
            line.setStrokeWidth(3);

            board.getChildren().add(line);
        }
    }

    private double[] getTileCoordinates(int tileNumber){
        int row=(tileNumber-1)/width;
        int col=(tileNumber-1)%width;

        int actualRow=row;

        if (actualRow%2==1){
            col=width-1-col;
        }

        double x=col*tileSize+tileSize/2.0;
        double y=(height-1-actualRow)*tileSize+tileSize/2.0;

        return new  double[]{x,y};
    }

    private void createPlayers(Pane board){
        Color[] colors={Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};

        for (int i=0; i<colors.length; i++){
            double startX=tileSize/2.0 + (i*20)-30;
            double startY=tileSize*9 + tileSize/2.0;

            String[] colorNames={"RED","BLUE","GREEN","YELLOW"};

            Player player=new Player("Snake_Ladders.Player "+colorNames[i],colors[i], startX, startY);
            players.add(player);
            board.getChildren().add(player.getPiece());
            player.setPosition(1);
            movePlayer(player);
        }
    }

    private void movePlayer(Player player){
        int pos =player.getPosition();

        if (pos<1) pos=1;
        if (pos>width*height) pos=width*height;

        double[] coords=getTileCoordinates(pos);
        double x=coords[0];
        double y=coords[1];

        int playerIndex=players.indexOf(player);
        double offsetX=(playerIndex % 2)*15-7;
        double offsetY=(playerIndex/2.0)*15-7;

        player.getPiece().setTranslateX(x+offsetX);
        player.getPiece().setTranslateY(y+offsetY);

    }

    private void nextTurn(){
        currentPlayerIndex=(currentPlayerIndex+1)%players.size();
        Player current=players.get(currentPlayerIndex);
        System.out.println("Nobat: "+current.getName());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
