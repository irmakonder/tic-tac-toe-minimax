class Main {
    public static void main(String[] args){
        Board board = new Board();

        board.placeMove(0,0,'X');
        board.placeMove(1,1,'O');
        board.print();

        System.out.println(board.isValidMove(0,0));
        System.out.println(board.isValidMove(2,2));
        System.out.println(board.isValidMove(3,1));
    }
}
