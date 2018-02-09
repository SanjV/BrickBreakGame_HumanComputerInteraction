package com.example.sanj.brickbreakout;

import android.graphics.RectF;

/**
 * Created by Sanjana Vijaykumar (sxv163930) on 11/30/2017.
 * Human Computer Interaction CS6326.001 - Assignment 6
 *
 * This is BrickObject Class.
 * Defines structure, properties of the brick object on the screen and
 * implements appropriate getter and setter methods.
 */

public class BrickObject {
    private RectF rectF;

    public boolean isVisible;

    public BrickObject(int row, int column, int width, int height){
        isVisible = true;
        int padding = 5;

        rectF = new RectF(column*width + padding, row*height + padding, column * width + width - padding, row*height + height - padding);
    }

    /**
     * returns the rectangle object to be drawn on the screen.
     * @return = rectangle object (RectF object)
     */
    public RectF getRectF(){
        return this.rectF;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public boolean getVisibility(){
        return isVisible;
    }
}
