 class Board {
    private final char[][] grid = new char[3][3];

    public Board() {
        for(int i =0;i < 3;i++){
            for(int  j=0;j<3;j++){
                grid[i][j]= ' ';
        }
    }
  }
     public void print() {
         for (int i = 0; i < 3; i++) {
             for (int j = 0; j < 3; j++) {
                 System.out.print(" " + grid[i][j] + " ");
                 if (j < 2) {
                     System.out.print("|");
                 }
             }
             System.out.println();

             if (i < 2) {
                 System.out.println("---+---+---");
             }
         }
     }
     public void placeMove(int row,int col,char symbol) {
        grid[row][col]=symbol;
     }
     public boolean isValidMove(int row,int col) {
        if(row < 0 || row > 2){
            return false;
        }
        if(col < 0 || col > 2){
            return false;
        }
        if(grid[row][col] != ' '){
            return false;
        }
        return true;
     }

     public boolean isFull() {
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(grid[i][j] == ' '){
                    return false;
                }
            }
        }
        return true;
     }
     public char checkWinner() {
         for (int j = 0; j < 3; j++) {
             if (grid[0][j] != ' ' && grid[0][j] == grid[1][j] && grid[1][j] == grid[2][j]) {
                 return grid[0][j];
             }
         }
         if (grid[0][0] != ' ' && grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2]) {
             return grid[0][0];
         }
         if (grid[0][2] != ' ' && grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0]) {
             return grid[0][2];
         }
         return ' ';
     }

  }


