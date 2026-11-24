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
import javafx.animation.ScaleTransition;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.List;



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
    private int xWins;
    private int oWins;
    private int draws;
    private Label xScoreLabel;
    private Label oScoreLabel;
    private Label drawScoreLabel;
    private final Leaderboard leaderboard = new Leaderboard();
    private String playerName = "";
    private int sessionPoints;
    private boolean sessionActive;
    private final Set<String> modesPlayed = new LinkedHashSet<>();
    private Label pointsLabel;


    @Override
    public void start(Stage stage) {
        this.stage = stage;
        xWins = 0;
        oWins = 0;
        draws = 0;
        stage.setTitle("Tic-Tac-Toe");
        stage.setResizable(false);
        stage.setScene(createStartScene());
        stage.show();
        stage.setOnCloseRequest(e -> saveSession());
    }

    private Scene createStartScene() {
        Label title = new Label("Tic-Tac-Toe");
        title.getStyleClass().add("title");

        Label subtitle = new Label("can you beat the computer?");
        subtitle.getStyleClass().add("subtitle");

        TextField nameField = new TextField(playerName);
        nameField.setPromptText("your name (for high scores)");
        nameField.getStyleClass().add("name-field");
        nameField.setMaxWidth(220);

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
            playerName = cleanName(nameField.getText());
            selectedMode = selected.getText();
            startGame();
        });

        Button highScores = new Button("HIGHSCORES");
        highScores.getStyleClass().add("secondary-button");
        highScores.setOnAction(e -> {stage.setScene(createHighScoresScene());});

        VBox content = new VBox(8, createBow(1.6), title , subtitle , nameField , modeBox , highScores , newGame);
        content.setAlignment(Pos.CENTER);
        VBox.setMargin(nameField, new Insets(16, 0, 0, 0));
        VBox.setMargin(modeBox, new Insets(12, 0, 16, 0));

        return createPinkScene(content);
    }

    private void startGame() {
        sessionPoints = 0;
        sessionActive = true;
        modesPlayed.clear();
        modesPlayed.add(selectedMode);

        xWins = 0;
        oWins = 0;
        draws = 0;

        board = new Board();
        currentPlayer = 'X';
        gameOver = false;

        if (selectedMode.equals("Easy")) {
            computer = new RandomAI();
        } else if (selectedMode.equals("Hard")) {
            computer = new MinimaxAI('O');
        } else {
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
            saveSession();
            stage.setScene(createStartScene());
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar;
        if (computer == null) {
            Label modeLabel = new Label("2 players");
            modeLabel.getStyleClass().add("mode-label");
            topBar = new HBox(menuButton, spacer, modeLabel);
        } else {
            Button modeSwitch = new Button(selectedMode + " ⇄");
            modeSwitch.getStyleClass().addAll("mode-label", "mode-switch");
            modeSwitch.setOnAction(e -> switchDifficulty(modeSwitch));
            topBar = new HBox(menuButton, spacer, modeSwitch);
        }
        topBar.setAlignment(Pos.CENTER_LEFT);

        xScoreLabel = new Label();
        oScoreLabel = new Label();
        drawScoreLabel = new Label();

        String xName = (computer == null) ? "X" : "you";
        String oName = (computer == null) ? "O" : "computer";

        HBox scoreRow = new HBox(10,
                createScoreCard(xName, xScoreLabel),
                createScoreCard("Draws", drawScoreLabel),
                createScoreCard(oName, oScoreLabel));
        scoreRow.setAlignment(Pos.CENTER);
        updateScoreLabels();

        pointsLabel = new Label();
        pointsLabel.getStyleClass().add("points-label");
        pointsLabel.setVisible(!playerName.isEmpty());
        pointsLabel.setManaged(!playerName.isEmpty());
        updatePointsLabel();

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

        VBox content = new VBox(16, topBar, scoreRow, pointsLabel, statusLabel, grid, playAgain);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(40, 30, 30, 30));

        return createPinkScene(content);
    }

    private void handleClick(int row, int col) {
        boolean computerTurn = (computer != null && currentPlayer == 'O');
        if (gameOver || computerTurn || !board.isValidMove(row, col)) {
            return;
        }

        makeMove(row, col);

        if (!gameOver && computer != null) {
            computerMove();
        }
    }

    private void makeMove(int row, int col) {
        board.placeMove(row, col, currentPlayer);
        cells[row][col].setText(String.valueOf(currentPlayer));
        cells[row][col].getStyleClass().add(currentPlayer == 'X' ? "x-mark" : "o-mark");
        playPopAnimation(cells[row][col]);

        if (board.checkWinner() != ' ') {
            highlightWinningLine();
            if (currentPlayer == 'X') {
                xWins++;
                sessionPoints += selectedMode.equalsIgnoreCase("Hard") ? 5 : 1;
            } else {
                oWins++;
            }
            updateScoreLabels();
            statusLabel.setText(winnerMessage());
            gameOver = true;
            return;
        }
        if (board.isFull()) {
            draws++;
            if (selectedMode.equalsIgnoreCase("Hard")) {
                sessionPoints += 3;
            }
            updateScoreLabels();
            updatePointsLabel();
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
            makeMove(move[0], move[1]);
        });
        computerDelay.play();
    }


    private void resetGame() {
        if (computerDelay != null) {
            computerDelay.stop();
        }
        board = new Board();
        currentPlayer = 'X';
        gameOver = false;
        for (Button[] rowCells : cells) {
            for (Button cell : rowCells) {
                cell.setText("");
                cell.getStyleClass().removeAll("x-mark", "o-mark", "winning-cell");
            }
        }
        updateStatus();
    }

    private void updateStatus() {
        if (computer == null) {
            statusLabel.setText(currentPlayer + "'s turn");
        } else {
            statusLabel.setText("your turn ♡ ");
        }
    }

    private String winnerMessage() {
        if (computer == null) {
            return currentPlayer + " wins ! ♡";
        }
        return (currentPlayer == 'X') ? "you win ! ♡" : "computer wins ! ";
    }

    private Scene createPinkScene(VBox content) {
        StackPane root = new StackPane(content);
        root.getStyleClass().add("background");
        addCornerBows(root);

        Scene scene = new Scene(root, 420, 640);
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

    private VBox createScoreCard(String title, Label valueLabel) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("score-title");
        valueLabel.getStyleClass().add("score-value");

        VBox card = new VBox(2, titleLabel, valueLabel);
        card.getStyleClass().add("score-card");
        card.setAlignment(Pos.CENTER);
        return card;
    }

    private void updateScoreLabels() {
        xScoreLabel.setText(String.valueOf(xWins));
        oScoreLabel.setText(String.valueOf(oWins));
        drawScoreLabel.setText(String.valueOf(draws));
    }

    private void highlightWinningLine() {
        int[][] line = board.getWinningLine();
        if (line == null) {
            return;
        }
        for (int[] pos : line) {
            cells[pos[0]][pos[1]].getStyleClass().add("winning-cell");
        }
    }

    private void playPopAnimation(Button cell) {
        ScaleTransition pop = new ScaleTransition(Duration.millis(150), cell);
        pop.setFromX(0.8);
        pop.setFromX(0.8);
        pop.setToX(1);
        pop.setToY(1);
        pop.play();

    }

    private void switchDifficulty(Button modeSwitch) {
        if (selectedMode.equalsIgnoreCase("Easy")) {
            selectedMode = "Hard";
            computer = new MinimaxAI('O');
        } else {
            selectedMode = "Easy";
            computer = new RandomAI();
        }
        modesPlayed.add(selectedMode);
        modeSwitch.setText(selectedMode + "  ⇄ ");
        resetGame();

    }

    private String cleanName(String raw) {
        String name = raw.trim().replace(";", "").replace("\n", "");
        if (name.length() > 12) {
            name = name.substring(0, 12);
        }
        return name;
    }

    private void updatePointsLabel() {
        pointsLabel.setText("♡ " + playerName + " · " + sessionPoints + " points");

    }

    private void saveSession() {
        if (!sessionActive) {
            return;
        }
        sessionActive = false;
        if (playerName.isEmpty()) {
            return;
        }
        String mode = String.join(" + ", modesPlayed);
        leaderboard.add(new ScoreEntry(playerName, sessionPoints, mode));
        System.out.println("saved: " + leaderboard.getEntries());
    }

    private Scene createHighScoresScene() {
        Label title = new Label("HIGHSCORES");
        title.getStyleClass().add("title");

        VBox list = new VBox(6);
        list.setAlignment(Pos.TOP_CENTER);

        List<ScoreEntry> entries = leaderboard.getEntries();
            if (entries.isEmpty()) {
                Label empty = new Label("No scores yet. Enter your name and play!");
                empty.getStyleClass().add("subtitle");
                list.getChildren().add(empty);
            } else {
                for (int i = 0; i < entries.size(); i++) {
                    list.getChildren().add(createScoreRow(i + 1, entries.get(i)));
                }
            }

            Button back = new Button("← Menu");
            back.getStyleClass().add("menu-button");
            back.setOnAction(e -> stage.setScene(createStartScene()));

            VBox content = new VBox(12, createBow(1.2), title, list, back);
            content.setAlignment(Pos.TOP_CENTER);
            content.setPadding(new Insets(40, 30, 30, 30));

            return createPinkScene(content);
        }

        private HBox createScoreRow(int rank, ScoreEntry entry) {
            Label rankLabel = new Label(rank + ".");
            rankLabel.getStyleClass().add("row-rank");
            rankLabel.setMinWidth(26);

            Label nameLabel = new Label(entry.name());
            nameLabel.getStyleClass().add("row-name");

            Label pointsLabel = new Label(entry.points() + " pts");
            pointsLabel.getStyleClass().add("row-points");

            Label modeLabel = new Label(entry.mode());
            modeLabel.getStyleClass().add("row-mode");
            modeLabel.setMinWidth(90);
            modeLabel.setAlignment(Pos.CENTER_RIGHT);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox row = new HBox(10, rankLabel, nameLabel, spacer, pointsLabel, modeLabel);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("score-row");
            if (rank <= 3) {
                row.getStyleClass().add("top-three");
            }
            return row;
    }
}


