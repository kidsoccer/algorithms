import static javafx.application.Platform.exit;

public class MiniMax {

    public static void main(String... s) {
        playGame();
    }

    public static void playGame() {
        String[][] gameBoard = new String[][]{
                {"_", "_", "_"},
                {"_", "_", "_"},
                {"_", "_", "_"}
        };
        TickTackToe game = new TickTackToe(gameBoard);

        int gameSte = game.evaludate();
        while (gameSte != -10 && gameSte != 10) {

            //human first
            Move move = game.humanMove();
            game.gameBorad[move.x][move.y] = TickTackToe.HUMMAN_PLAYER;
            gameSte = game.evaludate();
            game.printCurrentBoard();

            //computer
            move = game.aiMove();
            game.gameBorad[move.x][move.y] = TickTackToe.AI_PLAYER;
            gameSte = game.evaludate();
            game.printCurrentBoard();

        }

        if (gameSte == -10) {
            System.out.println(TickTackToe.AI_PLAYER + " won");
            exit();
        } else if (gameSte == 10) {
            System.out.println(TickTackToe.HUMMAN_PLAYER + " won");
            exit();
        }

    }


    static class TickTackToe {
        static final String HUMMAN_PLAYER = "x"; //min player
        static final String AI_PLAYER = "o"; //max player

        static final String UN_TOUCHED = "_"; //max player

        String[][] gameBorad;

        public TickTackToe(String[][] gameBorad) {
            this.gameBorad = gameBorad;
        }


        private void printCurrentBoard(){
            System.out.println("Current board");
            for (int i = 0; i < gameBorad.length; i++){
                for (int j = 0; j < gameBorad.length; j++){
                    System.out.print("|" + gameBorad[i][j] + "|");
                }

            }
        }

        /**
         * check xem game dang o trang thai nao
         *
         * @return
         */
        int evaludate() {

            //rows
            for (int row = 0; row < gameBorad.length; row++) {
                if (gameBorad[row][0].equals(gameBorad[row][1]) && gameBorad[row][1].equals(gameBorad[row][2])) {
                    if (HUMMAN_PLAYER.equals(gameBorad[row][0])) {
                        //human won
                        return +10;
                    } else if(AI_PLAYER.equals(gameBorad[row][0])) {
                        return -10;
                    }
                }
            }
            //cols
            for (int col = 0; col < gameBorad.length; col++) {
                if (gameBorad[0][col].equals(gameBorad[1][0]) && gameBorad[1][col].equals(gameBorad[2][col])) {
                    if (HUMMAN_PLAYER.equals(gameBorad[0][col])) {
                        //human won
                        return +10;
                    }
                    else if(AI_PLAYER.equals(gameBorad[0][col])) {
                        return -10;
                    }
                }
            }

            //diagonal
            if (gameBorad[0][0].equals(gameBorad[1][1]) && gameBorad[1][1].equals(gameBorad[2][2])) {
                if (HUMMAN_PLAYER.equals(gameBorad[0][0])) {
                    //human won
                    return +10;
                }
                else if(AI_PLAYER.equals(gameBorad[0][0])) {
                    return -10;
                }
            }


            //oposite diagonal
            if (gameBorad[2][0].equals(gameBorad[1][1]) && gameBorad[1][1].equals(gameBorad[0][2])) {
                if (HUMMAN_PLAYER.equals(gameBorad[2][0])) {
                    //human won
                    return +10;
                }
                else if(AI_PLAYER.equals(gameBorad[2][0])) {
                    return -10;
                }
            }


            //game draw
            return 0;
        }


        /**
         * // This function returns true if there are moves
         * // remaining on the board. It returns false if
         * // there are no moves left to play.
         *
         * @return
         */
        boolean hasNextMoves() {

            for (int i = 0; i < gameBorad.length; i++)
                for (int j = 0; j < gameBorad[0].length; j++)
                    if (gameBorad[i][j].equals(UN_TOUCHED))
                        return true;
            return false;

        }


        /**
         * // This is the minimax function. It considers all
         * // the possible ways the game can go and returns
         * // the value of the board
         *
         * @param depth
         * @param player
         * @return
         */
        int minimax(String player) {

            int bestMaxInit = Integer.MIN_VALUE;
            int bestMinInit = Integer.MAX_VALUE;

            //player co the la min player hoac max player
            int currentGameScore = evaludate();

            if (currentGameScore == 10 || currentGameScore == -10) {
                return currentGameScore;
            }

            //If there are no more moves and no winner then
            // it is a tie
            if (!hasNextMoves()) {
                return 0;
            }

            for (int i = 0; i < gameBorad.length; i++) {
                for (int j = 0; j < gameBorad[0].length; j++) {
                    if (gameBorad[i][j].equals(UN_TOUCHED)) {

                        //marked
                        gameBorad[i][j] = player;

                        if (player.equals(HUMMAN_PLAYER)) {
                            //maximizers - humman
                            bestMaxInit = Math.max(bestMaxInit, minimax(player));

                        } else {
                            //AI - computer
                            bestMinInit = Math.min(bestMinInit, minimax(player));

                        }

                        //reset
                        gameBorad[i][j] = UN_TOUCHED;
                    }


                }
            }

            if (player.equals(HUMMAN_PLAYER)) {
                return bestMaxInit;
            }

            return bestMaxInit;
        }


        public Move humanMove() {
            Move move = new Move(-1, -1);
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < gameBorad.length; i++) {
                for (int j = 0; j < gameBorad[0].length; j++) {
                    if (gameBorad[i][j].equals(UN_TOUCHED)) {
                        //marked as x
                        gameBorad[i][j] = HUMMAN_PLAYER;
                        int score = minimax(HUMMAN_PLAYER);
                        if (score > bestScore) {
                            move = new Move(i, j);
                            bestScore = score;
                        }

                        //reset
                        gameBorad[i][j] = UN_TOUCHED;
                    }
                }
            }
            System.out.println("The best optimal move for human is : " + move.toString());
            return move;

        }


        public Move aiMove() {
            Move move = new Move(-1, -1);
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < gameBorad.length; i++) {
                for (int j = 0; j < gameBorad[0].length; j++) {
                    if (gameBorad[i][j].equals(UN_TOUCHED)) {
                        //marked as x
                        gameBorad[i][j] = AI_PLAYER;
                        int score = minimax(AI_PLAYER);
                        if (score < bestScore) {
                            move = new Move(i, j);
                            bestScore = score;
                        }

                        //reset
                        gameBorad[i][j] = AI_PLAYER;
                    }
                }
            }
            System.out.println("The best optimal move for ai is : " + move.toString());
            return move;

        }
    }


    static class Move {
        int x;
        int y;

        public Move(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Move{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
