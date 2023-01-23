class GameOfLife {
    final static int nrows = 30, ncols = 100;
    static boolean[][] gameBoard = new boolean[nrows][ncols];
    public static final String CSI = "\033[";

    // Note: 1, 1 is the top left corner of the screen
    private static void internalMoveCursor(int row, int col) {
        System.out.print(CSI + row + ";" + col + "H");
    }

    /**
     * Hides the terminal text cursor
     */
    public static void hideCursor() {
        System.out.print(CSI + "?25l");
    }

    /**
     * Shows the terminal text cursor
     */
    public static void showCursor() {
        System.out.print(CSI + "?25h");
    }

    /**
     * Uses an xterm standardized escape code to enable the alternate buffer. This
     * means that the terminal
     * will switch to a black screen for us to render upon and once we exit, the
     * user's original screen will be preserved.
     * To exit this, call disableAlternateScreen()
     * 
     * @see public void disableAlternateScreen()
     */
    public static void initAlternateScreen() {
        System.out.print(CSI + "?1049h");
        internalMoveCursor(1, 1);
    }

    /**
     * Take the terminal out of the alternate screen buffer.
     * This will restore the user's previous screen.
     * 
     * @see public void initAlternateScreen()
     */
    public static void disableAlternateScreen() {
        System.out.print(CSI + "?1049l");
    }

    /**
     * Clears the screen
     */
    public static void clear() {
        internalMoveCursor(1, 1);
        System.out.print(CSI + "2J");
    }

    public static void drawBoard() {
        clear();
        for(int i = 0; i < gameBoard.length; i++) {
            for(int j = 0; j < gameBoard[i].length; j++) {
                System.out.print(gameBoard[i][j] ? "#" : " ");
            }
            System.out.println();
        }
    }

    
    public static void main(String[] args) {
        for(int i = 0; i < gameBoard.length; i++) {
            for(int j = 0; j < gameBoard[i].length; j++) {
                gameBoard[i][j] = false;
            }
        }

        gameBoard[5][5] = true;
        gameBoard[6][6] = true;
        gameBoard[4][5] = true;
        gameBoard[6][5] = true;
        gameBoard[9][5] = true;

        initAlternateScreen();
        while(true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            boolean[][] nextBoard = new boolean[nrows][ncols];
            for(int i = 0; i < nextBoard.length; i++) {
                System.arraycopy(gameBoard[i], 0, nextBoard[i], 0, nextBoard[i].length);
            }
            

            // run algorithm
            for(int r = 0; r < gameBoard.length; r++) {
                for(int c = 0; c < gameBoard[r].length; c++) {
                        int numNeighbors = 0;
                        int startRow = r <= 0 ? r : r - 1;
                        int endRow = r >= nrows - 1 ? r : r + 1;
                        int startCol = c <= 0 ? c : c - 1;
                        int endCol = c >= ncols - 1 ? c : c + 1;

                        for(int i = startRow; i <= endRow; i++) {
                            for(int j = startCol; j <= endCol; j++) {
                                if(i == r && j == c)
                                    continue;
                                
                                if(gameBoard[i][j]) {
                                    numNeighbors++;
                                }
                            }
                        }

                        if(gameBoard[r][c]) {
                            if(numNeighbors <= 0) {
                                nextBoard[r][c] = false;
                            } else if(numNeighbors == 2 || numNeighbors == 3) {
                                nextBoard[r][c] = true;
                            } else if(numNeighbors > 3) {
                                nextBoard[r][c] = false;
                            }
                        } else if(numNeighbors == 3) {
                            nextBoard[r][c] = true;
                        }
                }
            }
            
            gameBoard = nextBoard;

            drawBoard();
        }
        //disableAlternateScreen();

    }
}