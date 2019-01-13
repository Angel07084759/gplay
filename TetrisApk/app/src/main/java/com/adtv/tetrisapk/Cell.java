package com.adtv.tetrisapk;

import android.content.Context;
import android.graphics.Color;

/**
 * Cell is a button that represents a Tetris Cell.
 * This subclass derives from AppCompatButton and
 * contains variables int color and int id that help
 * to Get And set the color and id for each Tetris Cell.
 */
public class Cell extends android.support.v7.widget.AppCompatButton
{

    /**
     * Default int id for any new Cell.
     */
    public static final int DEFAULT_ID = 0;
    /**
     * Default Color color for any new Cell.
     */
    public static final int DEFAULT_COLOR = Color.LTGRAY;

    private static int idGenerator;//Generates unique IDs by incrementing by one.

    private int color = DEFAULT_COLOR;//Int color for this cell.
    private int id = 0;//ID for this Cell.

    /**
     * Creates a Cell in the given context.
     *
     * @param context The Context through which it can access the current theme, resources, etc.
     */
    public Cell(Context context)
    {
        super(context);
    }

    /**
     * Generates a unique ID.
     *
     * @return a generated unique ID.
     */
    public static int generateId()
    {
        return ++idGenerator;
    }

    /**
     * Checks if this cell is empty.
     *
     * @return true if this cell ID is equivalent to the DEFAULT_ID; false otherwise.
     */
    public boolean isEmpty()
    {
        return id == DEFAULT_ID;
    }

    /**
     * Sets the ID of this Cell.
     *
     * @param id The ID for this Cell.
     */
    @Override
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Accessor for the ID of this Cell.
     *
     * @return the ID of this Cell.
     */
    @Override
    public int getId()
    {
        return id;
    }

    /**
     * Sets the color for this Cell.
     *
     * @param color the color for this Cell.
     */
    public void setColor(int color)
    {
//        setText(id +"");//Debuggin
        setBackgroundColor(this.color = color);
    }

    /**
     * Accessor for the color of this Cell.
     *
     * @return the color of this Cell.
     */
    public int getColor()
    {
        return color;
    }
}