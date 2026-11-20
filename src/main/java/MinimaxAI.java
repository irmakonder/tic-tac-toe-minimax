public class MinimaxAI implements Player {
    private final char me;
    private final char opponent;

    public MinimaxAI(char me){
        this.me = me;
        this.opponent = (me == 'X') ? 'O' : 'X';
    }

    @Override
    public int[] getMove(Board board) {
        int bestScore = Integer.MIN_VALUE;
        int [] bestMove = null;

        for(int i =0;i < 3;i++){
            for(int j =0;j < 3;j++){
                if(board.isValidMove(i,j)) {
                    board.placeMove(i,j,me);
                    int score =minimax(board,false);
                    board.undoMove(i,j);

                    if(score > bestScore) {
                        bestScore = score;
                        bestMove = new int []{i,j};
                    }
                }
            }
        }
        System.out.println("computer plays: " + (bestMove[0] + 1) + " " + (bestMove[1] + 1));
        return bestMove;
    }

    public int minimax(Board board,  boolean isMyTurn) {
        char winner = board.checkWinner();
        if(winner == me) return 10;
        if(winner == opponent) return -10;
        if(board.isFull()) return 0;

        if(isMyTurn){
            int best = Integer.MIN_VALUE;
            for(int i =0;i < 3;i++){
                for(int j =0;j < 3;j++){
                    if(board.isValidMove(i,j)) {
                        board.placeMove(i,j,me);
                        best = Math.max(best, minimax(board, false));
                        board.undoMove(i,j);
                    }
                }
            }
            return best;
        }else {
            int best = Integer.MAX_VALUE;
            for(int i =0; i < 3;i++) {
                for(int j =0;j < 3;j++){
                    if(board.isValidMove(i,j)) {
                        board.placeMove(i,j,opponent);
                        best = Math.min(best, minimax(board, true));
                        board.undoMove(i,j);
                    }
                }
            }
            return best;
        }
    }
}
