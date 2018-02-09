package com.example.sanj.brickbreakout;

import android.graphics.RectF;

/**
 * Created by Sanjana Vijaykumar (sxv163930) on 11/22/2017.
 * Human Computer Interaction CS6326.001 - Assignment 6
 *
 * This is PaddleObject class. (PaddleObject object on the screen).
 * Defines, Structure of paddleObject, properties of paddleObject object and
 * implements appropriate getter and setter methods.
 */

public class PaddleObject {
    private RectF rectF;

    private float length;
    private float height;

    private int screenWidth;
    private int screenHeight;

    private float x;
    private float y;

    private float paddleSpeed;

    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    private int paddleState = STOPPED;

    public PaddleObject(int screenX, int screenY){
        length = 200;
        height = 20;

        screenWidth = screenX;
        screenHeight = screenY;

        x = screenX/2;
        //y = screenY - 20;
        //y = screenY - 240;
        //y = screenY - 260;
        y = screenY - 245;
        rectF = new RectF(x,y, x+length, y+height);
        paddleSpeed = 350;
    }

    public RectF getRectF(){
        return rectF;
    }

    public float getX(){
        return this.x;
    }

    public void setX(float x){
        this.x = x;
    }

    public void setState(int state){
        paddleState = state;
    }

    /**
     * updates the paddleObject position on the screen based on the movement and fps.
     * Checks for padlle state (left or right) and moves the paddleObject.
     * @param fps = frames per second, determines the speed of the paddleObject on the screen.
     */
    public void update(long fps){
        if(this.paddleState == LEFT)
            x = x - paddleSpeed / fps;
        if (this.paddleState == RIGHT)
            x = x + paddleSpeed / fps;

        if(x <= 0)
            x = 0;

        if(x + length  >= screenWidth)
            x = screenWidth - length;

        rectF.left = x;
        rectF.right = x + length;

    }

}
