import jdk.swing.interop.SwingInterOpUtils;

import java.util.Scanner;

public class Game {
    private Board board;
    private char currentPlayer;
    private Player playerX;
    private Player playerO;
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        while(true){
        System.out.println("1)two players");
        System.out.println("2)play vs computer");
        System.out.println("choose mode: ");
        int mode =scanner.nextInt();

        playerX = new HumanPlayer(scanner);
        if(mode == 2){
            playerO = new RandomAI();
        }else{
            playerO = new HumanPlayer(scanner);
        }

            play();

            System.out.print("Play again ? (yes/no): ");
            String answer = scanner.next();

            if(!answer.equalsIgnoreCase("yes")){
                System.out.println("thanks for playing:)");
                break;
            }
        }
    }

    public void play() {
        board = new Board();
        currentPlayer = 'X';

        while (true){
            board.print();
            System.out.println("Player " + currentPlayer + "'s turn.");


            Player current = (currentPlayer == 'X') ? playerX : playerO;
            int[] move = current.getMove(board);
            int row =move[0] , col = move[1];
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
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }
    }
}

