import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.animation.PathTransition;
import javafx.util.Duration;



public class TicTacToeApp extends Application {

    private Stage stage;
    private String selectedMode;

    private Board board;
    private char currentPlayer;
    private boolean gameOver;
    private final Button[][] cells = new Button[3][3];
    private Label statusLabel;
    private Player computer;
    private PauseTransition computerDelay;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("Tic-Tac-Toe");
        stage.setResizable(false);
        stage.setScene(createStartScene());
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

        VBox modeBox = new VBox(10, twoPlayers, easy, hard);
        modeBox.setAlignment(Pos.CENTER);

        Button newGame = new Button("New game");
        newGame.getStyleClass().add("new-game-button");
        newGame.setOnAction(e -> {
            ToggleButton selected = (ToggleButton) modes.getSelectedToggle();
            if (selected == null) {
                return;
            }
            selectedMode = selected.getText();
            startGame();
        });

        VBox content = new VBox(8, createBow(1.6), title, subtitle, modeBox, newGame);
        content.setAlignment(Pos.CENTER);
        VBox.setMargin(modeBox, new Insets(24, 0, 16, 0));

        return createPinkScene(content);
    }

    private void startGame() {
        board = new Board();
        currentPlayer = 'X';
        gameOver = false;

        if(selectedMode.equals("easy")) {
            computer =  new RandomAI();
        }else if(selectedMode.equals("hard")) {
            computer = new MinimaxAI('O');
        }else {
            computer = null;
        }
        stage.setScene(createGameScene());
    }

    private Scene createGameScene() {
        Button menuButton = new Button("← Menu");
        menuButton.getStyleClass().add("menu-button");
        menuButton.setOnAction(e -> {
            if (computerDelay != null) {
                computerDelay.stop();
            }
            stage.setScene(createStartScene());
        });

        Label modeLabel = new Label(selectedMode + " mode");
        modeLabel.getStyleClass().add("mode-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topBar = new HBox(menuButton, spacer, modeLabel);
        topBar.setAlignment(Pos.CENTER_LEFT);

        statusLabel = new Label();
        statusLabel.getStyleClass().add("status");
        updateStatus();

        GridPane grid = new GridPane();
        grid.getStyleClass().add("board");
        grid.setAlignment(Pos.CENTER);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int row = i;
                final int col = j;
                Button cell = new Button();
                cell.getStyleClass().add("cell");
                cell.setOnAction(e -> handleClick(row, col));
                cells[i][j] = cell;
                grid.add(cell, j, i);
            }
        }

        Button playAgain = new Button("Play again");
        playAgain.getStyleClass().add("new-game-button");
        playAgain.setOnAction(e -> resetGame());

        VBox content = new VBox(16, topBar, statusLabel, grid, playAgain);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(40, 30, 30, 30));

        return createPinkScene(content);
    }

    private void handleClick(int row, int col) {
      boolean computerTurn = (computer != null && currentPlayer == 'O');
      if(gameOver || computerTurn || !board.isValidMove(row,col)) {
          return;
      }

      makeMove(row,col);

      if(!gameOver && computer != null) {
          computerMove();
      }
    }

    private void makeMove(int row, int col) {
        board.placeMove(row, col, currentPlayer);
        cells[row][col].setText(String.valueOf(currentPlayer));
        cells[row][col].getStyleClass().add(currentPlayer == 'X' ? "x-mark" : "o-mark");

        if (board.checkWinner() != ' ') {
            statusLabel.setText(winnerMessage());
            gameOver = true;
            return;
        }
        if (board.isFull()) {
            statusLabel.setText("It's a draw!");
            gameOver = true;
            return;
        }

        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        updateStatus();
    }

    private void computerMove() {
        statusLabel.setText("computer is thinking...");
        computerDelay = new PauseTransition(Duration.millis(500));
        computerDelay.setOnFinished(e -> {
            int[] move = computer.getMove(board);
            makeMove(move[0],move[1]);
        });
        computerDelay.play();
    }


    private void resetGame() {
        if(computerDelay != null) {
            computerDelay.stop();
        }
        board = new Board();
        currentPlayer = 'X';
        gameOver = false;
        for (Button[] rowCells : cells) {
            for (Button cell : rowCells) {
                cell.setText("");
                cell.getStyleClass().removeAll("x-mark", "o-mark");
            }
        }
        updateStatus();
    }

    private void updateStatus() {
        if(computer == null) {
            statusLabel.setText(currentPlayer + "'s turn");
        }else {
            statusLabel.setText("your turn ♡ ");
        }
    }
    private String winnerMessage() {
        if(computer == null) {
            return currentPlayer + " wins ! ";
        }
        return (currentPlayer == 'X') ? "you win ! " : "computer wins ! ";
    }

    private Scene createPinkScene(VBox content) {
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