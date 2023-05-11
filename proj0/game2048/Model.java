package game2048;


import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author liang
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        this.board.setViewingPerspective(side);
        boolean changed;
        changed = false;

        // Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.
        // for every column from right to left.
        for (int col = 3; col >= 0; col = col -1) {
            // for every row in a single col top-down.
            for (int row =3; row >= 0; row = row - 1) {
                // move the tile if it exists.
                if (this.board.tile(col, row) != null) {
                    if (moveToDestination(col, row)) {
                        changed = true;
                    }
                }

            }
        }
        this.board.setViewingPerspective(Side.NORTH);
        checkGameOver();
        if (changed) {
            setChanged();
        }

        return changed;
    }

    /** given a Tile in THIS.BOARD.TILE(col, row), move that tile to destination,
     * update THIS.SCORE.*/
    public boolean moveToDestination(int col, int row) {
        int desRow = row;
        boolean moved = false;
        boolean Merged = false;
        for (int j = row + 1; j <= 3; j = j + 1) {
            if (this.board.tile(col, j) == null) {
                desRow = desRow + 1;
            } else {
                if (this.board.tile(col, j).value() == this.board.tile(col, row).value()) {
                    desRow = desRow + 1;

                }
                // once there is a tile, there's no need to loop.
                break;
            }
        }
        if (desRow > row){
            moved = true;
        }
        if (moved) {
            Merged = this.board.move(col, desRow, this.board.tile(col, row));
        }
        if (Merged){
            this.score = this.score + this.board.tile(col, desRow).value();
        }
        return moved;
    }




    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        int max = b.size();
        for (int i = 0; i < max; i = i + 1) {
            for (int j = 0; j < max; j = j + 1) {
                if (b.tile(i, j) == null) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        int maxroll = b.size();
        for (int i = 0; i < maxroll; i = i + 1) {
            for (int j = 0; j < maxroll; j = j + 1) {
                if (b.tile(i, j) != null && b.tile(i, j).value() == MAX_PIECE) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // 1.have empty spaces;
        // 2.there is at least one tile's adjacent that has same value as itself
        // on board.

        //use exist function.
        if (emptySpaceExists(b)) {
            return true;
        }
        //iterate through all tiles.
        int max = b.size();
        for (int i = 0; i < max; i = i + 1) {
            for (int j = 0; j < max; j = j + 1) {
                //for every tile on board, iterate through it's adjacent tiles.
                for (Tile x : Adjacent(b.tile(i,j), b)) {
                    //every adjacent function returns either 4 adjacent tiles or
                    //the tile itself. Get rid of tile it self, and compare.
                    if (x != b.tile(i, j)) {
                        if (x.value() == b.tile(i, j).value()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /** Returns all adjacent tiles of a tile on board as a array.
     * if the tile is on the edge, the result contains the tile itself. */
    private static Tile[] Adjacent(Tile tile, Board b) {
        int x = tile.col();
        int y = tile.row();
        //all cord of adjacent tiles
        int upx = x;
        int upy = y + 1;
        int downx = x;
        int downy = y - 1;
        int leftx = x - 1;
        int lefty = y;
        int rightx = x + 1;
        int righty = y;
        //if index out of board, just select the tile in the middle(self).
        if (upy >= b.size()) {
            upy = y;
        }
        if (downy < 0) {
            downy = y;
        }
        if (leftx < 0) {
            leftx = x;
        }
        if (rightx >= b.size()) {
            rightx = x;
        }
        Tile[] allAdjacent = new Tile[]{b.tile(upx,upy), b.tile(downx, downy),
                b.tile(leftx, lefty), b.tile(rightx,righty)};
        return allAdjacent;
    }

    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
