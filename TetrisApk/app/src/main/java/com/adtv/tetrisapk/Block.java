package com.adtv.tetrisapk;

/**
 * Block represents a Tetris block by grouping a set of Cells.
 * Block is NOT a View but the engine of the a block which is
 * used to give the user the illusion of a moving block.
 */
public class Block
{
    /**
     * Constant Default number of cell per block.
     */
    public static final int CELLS = 4;
    /**
     * Rotation map based on a 4 x 4 array matrix.
     */
    public static final int[][][] ROTATE = {
            {{0, 3}, {1, 3}, {2, 3}, {3, 3}},
            {{0, 2}, {1, 2}, {2, 2}, {3, 2}},
            {{0, 1}, {1, 1}, {2, 1}, {3, 1}},
            {{0, 0}, {1, 0}, {2, 0}, {3, 0}}};

    private int[][] block;//current Block map.
    private Cell[][] board;//2D Cell Array to display this block.

    private int blockID;//This Block ID
    private int blockColor;//This Block color

    /**
     * Block constructor that define a board; a block map; and block color.
     * Throws IndexOutOfBoundsException if a block is defined with more than CELLS number of cells.
     *
     * @param board a 2D Cell array to be used to display this block.
     * @param block a 2D int array that represents a block of CELLS number of cells.
     * @param color the color that represents this block.
     */
    public Block(Cell[][] board, int[][] block, int color)
    {
        this.board = board;
        this.block = block;
        this.blockColor = color;
        this.blockID = Cell.generateId();
    }

    /**
     * Sets a board to display this block.
     * @param board the board to display this block.
     */
    public void setBoard(Cell[][] board)///////////////////////////////////////////////////////////
    {
        this.board = board;
    }

    /**
     * Paints the given block map in the board given in the constructor.
     *
     * @param block the block map to be painted.
     * @param id    the id that identifies the group of cell that represents this block.
     * @param color the color for each cell that represents this block.
     */
    void paintBlock(int[][] block, int id, int color)
    {
        for (int i = 0; i < CELLS; i++)
        {
            Cell cell = board[block[i][0]][block[i][1]];
            if (cell.getId() == Cell.DEFAULT_ID || cell.getId() == blockID)
            {
                board[block[i][0]][block[i][1]].setId(id);
                board[block[i][0]][block[i][1]].setColor(color);
            }
        }
    }

    /**
     * Accessor for the color of this block.
     *
     * @return the color int value of this block.
     */
    public int getBlockColor()
    {
        return blockColor;
    }


    /**
     * Finds the current row of a current block in the board given in the constructor.
     *
     * @return the current row of a current block in the board given in the constructor.
     */

    public int getRow()
    {
        int row = block[0][0];
        for (int i = 1; i < CELLS; i++)
        {
            row = block[i][0] < row ? block[i][0] : row;
        }
        return row;
    }

    /**
     * Finds the current column of a current block in the board given in the constructor.
     *
     * @return the current column of a current block in the board given in the constructor.
     */
    public int getColumn()
    {
        int col = block[0][1];
        for (int i = 1; i < CELLS; i++)
        {
            col = block[i][1] < col ? block[i][1] : col;
        }
        return col;
    }

    /**
     * Trace the map of the current block.
     *
     * @return a copy of the current block map in a 2D array: new int[CELLS][2]
     */
    public int[][] getBlock()
    {
        int currRow = getRow();//block current row
        int currCol = getColumn();//block current column
        int[][] temp = new int[CELLS][2];//copy of this block

        for (int i = 0; i < CELLS; i++)
        {
            temp[i][0] = block[i][0] - currRow;
            temp[i][1] = block[i][1] - currCol;
        }
        return temp;
    }

    //if (!(cellRow >= 0 && cellCol >= 0 && cellRow < board.length && cellCol < board[0].length && (board[cellRow][cellCol].isEmpty() || board[cellRow][cellCol].getId() == blockID)))

    /**
     * Checks if the given block map with the given offset row and column
     * lay inside of the given board and in an empty spot.
     *
     * @param src     the block map to be validate.
     * @param moveRow the row offset to be tested.
     * @param moveCol the column offset to be tested.
     * @return a new block map if the given values are valid; null otherwise.
     */
    public int[][] validateBlock(int[][] src, int moveRow, int moveCol, boolean force)
    {
        int[][] copy = new int[CELLS][2];
        for (int i = 0; i < CELLS; i++)
        {
            int newRow = copy[i][0] = src[i][0] + moveRow;
            int newCol = copy[i][1] = src[i][1] + moveCol;

            boolean isValid = newRow >= 0 && newCol >= 0;
            isValid = isValid && newRow < board.length && newCol < board[0].length;

            if (force && isValid)
            {

            }
            else if (!(isValid && (board[newRow][newCol].isEmpty() || board[newRow][newCol].getId() == blockID)))
            {
                return null;
            }
        }
        return copy;
    }

    /**
     * Moves a tetris block at the given row and column.
     *
     * @param row the row to move the block.
     * @param col the column to move the block.
     * @return true if given values result in valid move; false otherwise.
     */
    public boolean move(int row, int col, boolean force)
    {
        int[][] helper;
        if ((helper = validateBlock(block, row, col, force)) != null)
        {
            paintBlock(block, Cell.DEFAULT_ID, Cell.DEFAULT_COLOR);
            paintBlock(helper, blockID, blockColor);
            block = helper;
            return true;
        }
        return false;
    }

    /**
     * Rotates this block clockwise.
     *
     * @return true if rotation lays in a valid move; false otherwise.
     */
    public boolean rotate()
    {
        int currRow = getRow();//current block row
        int currCol = getColumn();//current block column
        int nextRow = board.length;//after rotation block row
        int nextCol = board[0].length;//after rotation block column
        int[][] rotate = new int[CELLS][2];//rotation helper
        int[][] backup = validateBlock(block, 0, 0, false);//backing up in case rotation fails

        if (backup == null)//cannot rotate because of the current position
        {
            return false;
        }

        //unpainting current block temporarily
        paintBlock(block, Cell.DEFAULT_ID, Cell.DEFAULT_COLOR);

        //calculating rotation using the ROTATE array map
        for (int i = 0; i < CELLS; i++)
        {
            rotate[i][0] = ROTATE[(block[i][0]) - currRow][(block[i][1] - currCol)][0];
            rotate[i][1] = ROTATE[(block[i][0]) - currRow][(block[i][1] - currCol)][1];
            nextRow = rotate[i][0] < nextRow ? rotate[i][0] : nextRow;
            nextCol = rotate[i][1] < nextCol ? rotate[i][1] : nextCol;
        }

        //calculating current block position after rotating
        for (int i = 0; i < CELLS; i++)
        {
            rotate[i][0] += (currRow - nextRow);
            rotate[i][1] += (currCol - nextCol);
        }

        //fixing rotate out of bounds errors in row and column with three cells max error
        for (int i = 0; (block = validateBlock(rotate, -i, 0, false)) == null && i < 3; i++) ;//empty loop
        rotate = block != null ? block : rotate;//updating rotated block
        for (int i = 0; (block = validateBlock(rotate, 0, -i, false)) == null && i < 3; i++) ;//empty loop

        if (block == null)//failed to rotate
        {
            block = backup;
            return !move(0, 0, false);//false since rotation failed
        }
        return move(0, 0, false);//true if rotation is valid ; false otherwise.
    }
}