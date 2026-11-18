import java.util.Random;

public class RandomAI implements Player {
    private final Random random = new Random();

    @Override
    public int[] getMove(Board board){
        while(true){
            int row = random.nextInt(3);
            int col = random.nextInt(3);
            if(board.isValidMove(row,col)){
                System.out.println("computer plays: " + (row + 1) + " " + (col + 1));
                return new int[]{row,col};
            }
        }
    }
}
