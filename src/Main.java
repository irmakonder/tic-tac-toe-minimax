class Main {
    public static void main(String[] args){
        Board board = new Board();
        board.placeMove(0, 0, 'X');
        board.placeMove(1, 1, 'X');
        board.placeMove(2, 2, 'X');
        board.print();
        System.out.println("Winner: " + board.checkWinner());
        System.out.println("Full: " + board.isFull());

    }
}
