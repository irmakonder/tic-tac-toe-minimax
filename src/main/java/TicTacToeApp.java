import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TicTacToeApp extends Application {

    @Override
    public void start(Stage stage) {
        Label title = new Label("Tic-Tac-Toe");
        title.getStyleClass().add("title");

        StackPane root = new StackPane(title);
        root.getStyleClass().add("background");

        Scene scene = new Scene(root, 420, 560);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setTitle("Tic-Tac-Toe");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}