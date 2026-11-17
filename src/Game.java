import java.util.Scanner;

public class Game {
    private Board board;
    private char currentPlayer;
    private final Scanner scanner = new Scanner(System.in);

    public void play(){
        board = new Board();
        currentPlayer = 'X';

        while (true){
            board.print();
            System.out.println("Player " + currentPlayer + "'s turn.");

            int row,col;
            while(true){
                System.out.println("row (1-3): ");
                row = scanner.nextInt() - 1;
                System.out.println("column (1-3): ");
                col = scanner.nextInt() - 1;

                if(board.isValidMove(row,col)){
                    break;
                }
                System.out.println("invalid move, try again");
            }
            board.placeMove(row,col,currentPlayer);

            if(board.checkWinner() != ' ') {
                board.print();
                System.out.println("player " + currentPlayer + " wins!");
                break;
            }
                if(board.isFull()){
                    board.print();
                    System.out.println("it's a draw!");
                    break;
                }
                currentPlayer = (currentPlayer == 'X') ? '0' : 'X';
        }
    }
}

