package com.adtv.tetrisapk;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Random;

/**
 * Tetris is the engine of the game. This class allows you to
 * add, move, and rotate the current block in a given layout
 * that represents the Tetris board.
 */
public class Tetris// implements Runnable
{
    /**
     * Number of rows for the Tetris Board.
     */
    public static final int ROWS = 20;
    /**
     * Number of columns for the Tetris Board.
     */
    public static final int COLUMNS = ROWS / 2;

    //Definitions of each Tetris block.
    private static final int[][] BLOCK_I = {{0, 0}, {0, 1}, {0, 2}, {0, 3}};
    private static final int[][] BLOCK_J = {{0, 1}, {1, 1}, {2, 1}, {2, 0}};
    private static final int[][] BLOCK_L = {{0, 0}, {1, 0}, {2, 0}, {2, 1}};
    private static final int[][] BLOCK_O = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
    private static final int[][] BLOCK_S = {{0, 1}, {0, 2}, {1, 0}, {1, 1}};
    private static final int[][] BLOCK_T = {{0, 0}, {0, 1}, {0, 2}, {1, 1}};
    private static final int[][] BLOCK_Z = {{0, 0}, {0, 1}, {1, 1}, {1, 2}};

    Block currentBlock;
    Block nextBlock;
    private Cell[][] boardCells;
    private Cell[][] nextBlockCells;
    private Random random = new Random();
    private int rowsCleared;

    /**
     * Creates the game environment in the given Layouts.
     *
     * @param tetris_board the main board of the game.
     * @param next_block   the board to display the next block.
     */
    public Tetris(TableLayout tetris_board, TableLayout next_block)
    {
        boardCells = new Cell[ROWS][COLUMNS];
        nextBlockCells = new Cell[Block.CELLS][Block.CELLS];
        initCells(tetris_board, boardCells);
        initCells(next_block, nextBlockCells);
        currentBlock = generateBlock();
        currentBlock.setBoard(boardCells);
        nextBlock = generateBlock();
    }

    /**
     * Generates Tetris Blocks.
     *
     * @return a random tetris Block.
     */
    private Block generateBlock()
    {
        Block[] blocks = {
                new Block(nextBlockCells, BLOCK_I, Color.BLACK),
                new Block(nextBlockCells, BLOCK_J, Color.BLACK),
                new Block(nextBlockCells, BLOCK_L, Color.BLACK),
                new Block(nextBlockCells, BLOCK_O, Color.BLACK),
                new Block(nextBlockCells, BLOCK_S, Color.BLACK),
                new Block(nextBlockCells, BLOCK_T, Color.BLACK),
                new Block(nextBlockCells, BLOCK_Z, Color.BLACK)};
        return blocks[random.nextInt(blocks.length)];
    }

    /**
     * Accessor for rowsCleared.
     *
     * @return the number of row that has been cleared.
     */
    public int getRowsCleared()
    {
        return rowsCleared;
    }

    /**
     * Initializes a table layout with the given Cells.
     *
     * @param table the table layout to be initialized.
     * @param cells the cells to initialize the layout.
     */
    private void initCells(TableLayout table, Cell[][] cells)
    {
        for (int row = 0; row < cells.length; row++)
        {
            TableRow tableRow = new TableRow(table.getContext());

            TableLayout.LayoutParams rowParam = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f);
            tableRow.setLayoutParams(rowParam);

            table.addView(tableRow);

            for (int col = 0; col < cells[0].length; col++)
            {
                cells[row][col] = new Cell(table.getContext());//?
                cells[row][col].setBackgroundColor(Cell.DEFAULT_COLOR);//?
                cells[row][col].setPadding(0, 0, 0, 0);

                TableRow.LayoutParams buttonsParam = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                buttonsParam.setMargins(2, 2, 2, 2);
                cells[row][col].setLayoutParams(buttonsParam);
                tableRow.addView(cells[row][col]);
            }
        }
    }

    /**
     * Moves a tetris block at the given row and column.
     *
     * @param row the row value to move the block.
     * @param col the column value to move the block.
     * @return true if given values result in valid move; false otherwise.
     */
/*    public boolean moveBlock(int row, int col)
    {
        return currentBlock.move(row, col);
    }*/

    /**
     * Rotates this block clockwise.
     *
     * @return true if rotation lays in a valid move; false otherwise.
     */
    /*public boolean rotate()
    {
        return currentBlock.rotate();
    }*/

    /**
     * Spawns a new Tetris block at the given row and column.
     *
     * @param row the row to be used to spawn the block.
     * @param col the column to be used to spawn the block.
     * @return true if the block spawns lays in a valid spot; false otherwise.
     */
    public boolean spawn(int row, int col)
    {
        rowsCleared += clearRow();
        currentBlock = nextBlock;
        currentBlock.setBoard(boardCells);/////////////////////////////////////////
        nextBlock = generateBlock();
        paintNextBlock(0,0, random.nextInt(3));
        return currentBlock.move(row, col, false);
    }


    /**
     * Resets the Tetris board by clearing all of the current used cells in the board
     */
    public void reset()/////////////////////////////////////////////////////////////////////////////
    {
        for (int i = 0; i < nextBlockCells.length; i++)
        {
            for (int j = 0; j < nextBlockCells[0].length; j++)
            {
                nextBlockCells[i][j].setId(Cell.DEFAULT_ID);
                nextBlockCells[i][j].setColor(Cell.DEFAULT_COLOR);
            }
        }
        for (int i = 0; i < boardCells.length; i++)
        {
            for (int j = 0; j < boardCells[0].length; j++)
            {
                boardCells[i][j].setId(Cell.DEFAULT_ID);
                boardCells[i][j].setColor(Cell.DEFAULT_COLOR);
            }
        }
        rowsCleared = 0;
    }
    public void reset(Main.GameHandler game, Button[] btns, GameAudioLoop audio, GameTimer timer, boolean runIt)
    {
        new ResetGame(game, btns, audio, timer, runIt);
    }

    private class ResetGame extends Handler implements Runnable
    {
        int index;

        Handler game;
        Button[] btns;
        GameAudioLoop audio;
        GameTimer timer;
        boolean runIt;
        public ResetGame(Main.GameHandler game, Button[] btns, GameAudioLoop audio, GameTimer timer, boolean runIt)
        {
            this.game = game;
            this.btns = btns;
            this.audio = audio;
            this.timer = timer;
            this.runIt = runIt;

            timer.reset();
            for (Button b: btns) { b.setEnabled(false);}
            for (int i = 0; i < nextBlockCells.length; i++)
            {
                for (int j = 0; j < nextBlockCells[0].length; j++)
                {
                    nextBlockCells[i][j].setId(Cell.DEFAULT_ID);
                    nextBlockCells[i][j].setColor(Cell.DEFAULT_COLOR);
                }
            }
            post(this);
        }

        @Override
        public void run()
        {
            int r = (index / boardCells[0].length);
            int c = (index % boardCells[0].length);
            boardCells[r][c].setId(Cell.DEFAULT_ID);
            boardCells[r][c].setColor(Cell.DEFAULT_COLOR);
            index++;
            post(this);
            if (index >= (boardCells.length * boardCells[0].length))
            {
                removeCallbacks(this);
                for (Button b: btns) { b.setEnabled(true);}
                if (runIt)
                {
                    game.post((Runnable) game);
                    audio.play();
                    timer.start();
                }
            }
        }
    }


    /**
     * Paints the next block in the layout given in the constructor.
     */
    public void paintNextBlock(int row, int col, int rotate)
    {

        for (int i = 0; i < nextBlockCells.length; i++)
        {
            for (int j = 0; j < nextBlockCells[0].length; j++)
            {
                nextBlockCells[i][j].setId(Cell.DEFAULT_ID);
                nextBlockCells[i][j].setColor(Cell.DEFAULT_COLOR);
            }
        }
        for (int i = 0; i < rotate ; i++)
        {
            nextBlock.rotate();
        }
        nextBlock.move(row,col, false);
    }

    /**
     * Checks if the given row index is full.
     *
     * @param row the row to be checked.
     * @return true if all of the cell are empty not empty; false otherwise.
     */
    public boolean isRowFull(int row)
    {
        for (int i = 0; i < boardCells[0].length; i++)
        {
            if (boardCells[row][i].isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Deletes the given row and pulls down the rest of the rows.
     *
     * @param row the row to be deleted.
     */
    public void clearRow(int row)
    {
        if (row == 0)
        {
            for (int i = 0; i < boardCells[0].length; i++)
            {
                boardCells[row][i].setId(Cell.DEFAULT_ID);
                boardCells[row][i].setColor(Cell.DEFAULT_COLOR);
            }
        }
        else if (row > 0 && row < boardCells.length)
        {
            for (int r = row; r > 0; r--)
            {
                for (int c = 0; c < boardCells[0].length; c++)
                {
                    boardCells[r][c].setId(boardCells[r - 1][c].getId());
                    boardCells[r][c].setColor(boardCells[r - 1][c].getColor());
                }
            }
        }

    }

    /**
     * Checks and deletes any row that is full.
     *
     * @return the number of deleted rows.
     */
    private int clearRow()
    {
        int score = 0;
        for (int i = boardCells.length - 1; i >= 0; i--)
        {
            if (isRowFull(i))
            {
                score++;
                clearRow(i++);
            }
        }
        return score;
    }
}