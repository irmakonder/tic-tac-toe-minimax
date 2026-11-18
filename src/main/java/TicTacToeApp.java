import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;


public class TicTacToeApp extends Application {
    private Stage stage;
    private String selectedMode;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Tic-Tac-Toe");
        stage.setScene(createStartScene());
        stage.setResizable(false);
        stage.show();
    }
    private Scene createStartScene() {
        Label title = new Label("Tic-Tac-Toe");
        title.getStyleClass().add("title");

        Label subtitle = new Label("can you beat the computer?");
        subtitle.getStyleClass().add("subtitle");

        ToggleGroup modes = new ToggleGroup();
        ToggleButton twoPlayers = createModeButton("2 players", modes);
        ToggleButton easy = createModeButton("Easy", modes);
        ToggleButton hard = createModeButton("Hard", modes);
        hard.setSelected(true);

        VBox modeBox = new VBox(10,twoPlayers,easy,hard);
        modeBox.setAlignment(Pos.CENTER);

        Button newGame = new Button("New Game");
        newGame.getStyleClass().add("new-game-button");
        newGame.setOnAction(e -> {
            ToggleButton selected = (ToggleButton) modes.getSelectedToggle();
            if (selected == null) {
                return;
            }
            selectedMode = selected.getText();
            System.out.println("Starting game: " + selectedMode);
        });
        VBox content = new VBox(8, createBow(1.6), title, subtitle, modeBox, newGame);
        content.setAlignment(Pos.CENTER);
        VBox.setMargin(modeBox, new Insets(24, 0, 16, 0));

        StackPane root = new StackPane(content);
        root.getStyleClass().add("background");
        addCornerBows(root);

        Scene scene = new Scene(root, 420, 560);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return scene;
    }
    private ToggleButton createModeButton(String text, ToggleGroup group) {
        ToggleButton button = new ToggleButton(text);
        button.setToggleGroup(group);
        button.getStyleClass().add("mode-button");
        return button;
    }

    private Group createBow(double size) {
        double w = 40 * size;
        double h = 24 * size;

        Polygon left = new Polygon(0, 0, 0, h, w / 2, h / 2);
        Polygon right = new Polygon(w, 0, w, h, w / 2, h / 2);
        left.setFill(Color.web("#ED93B1"));
        right.setFill(Color.web("#ED93B1"));

        Circle knot = new Circle(w / 2, h / 2, 4.5 * size, Color.web("#D4537E"));

        return new Group(left, right, knot);
    }

    private void addCornerBows(StackPane root) {
        Pos[] corners = {Pos.TOP_LEFT, Pos.TOP_RIGHT, Pos.BOTTOM_LEFT, Pos.BOTTOM_RIGHT};
        for (Pos corner : corners) {
            Group bow = createBow(0.8);
            StackPane.setAlignment(bow, corner);
            StackPane.setMargin(bow, new Insets(18));
            root.getChildren().add(bow);
        }
    }




































}