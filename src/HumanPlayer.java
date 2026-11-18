import java.util.Scanner;

public class HumanPlayer implements Player {
    private final Scanner scanner;

    public HumanPlayer(Scanner scanner) {
        this.scanner=scanner;
    }
    @Override
    public int[] getMove(Board board) {
        while(true){
            System.out.println("row (1-2-3): ");
            int row = scanner.nextInt() - 1;
            System.out.println("column (1-2-3): ");
            int col =scanner.nextInt() - 1;

            if(board.isValidMove(row,col)){
                return new int[]{row,col};
            }
            System.out.println("invalid move, try again.");
        }
    }
}
