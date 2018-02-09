package com.example.sanj.brickbreakout;

import android.graphics.RectF;

import java.util.Random;

/**
 * Created by Sanjana Vijaykumar (sxv163930) on 11/30/2017.
 * Human Computer Interaction CS6326.001 - Assignment 6
 *
 * This is  BallObject class that defines the properties of the ballObject on the paddleObject.
 * Defines Structure of ballObject, properties and implements getter and setter methods.
 */

public class BallObject {
    RectF rectF;

    float xVelocity;
    float yVelocity;

    float ballWidth = 20;
    float ballHeight = 20;

    int screenWidth;
    int screenHeight;

    public BallObject(int screenX, int screenY){
        xVelocity = 250;
        yVelocity = -500;

        screenWidth = screenX;
        screenHeight = screenY;

        rectF = new RectF();
    }


    public RectF getRectF(){
        return rectF;
    }

    public void update(long fps){
        rectF.left += xVelocity/fps;
        rectF.right = rectF.left + ballWidth;
        rectF.top += yVelocity/fps;
        rectF.bottom = rectF.top - ballHeight;

    }

    /**
     *  Reverse the direction of ballObject along x-direction.
     */
    public void revXVelocity(){
        xVelocity = -xVelocity;
    }

    /**
     *  Reverse the direction of ballObject along y-direction.
     */
    public void revYVelocity(){
        yVelocity = -yVelocity;
    }

    /**
     *  Initially sets random velocity to the ballObject.
     */
    public void setRandomVelocity(){
        Random randomVel = new Random();
        int velocity = randomVel.nextInt();

        if(velocity == 0)
            revXVelocity();
    }

    /**
     * Reset the ballObject to its initial position on the screen (x, y).
     * @param x = x cordinate of the ballObject position.
     * @param y = y cordinate of the ballObject position.
     */
    public void reset(float x, float y){
        rectF.left = x; // 45 is half the paddleObject length. So taht ballObject is in the middle of the paddleObject.
        rectF.right = x+ ballWidth;
        rectF.top = y;
        rectF.bottom = y -ballHeight;
        //rectF.top = y -260;
        //rectF.bottom = y - 260 - ballHeight;
        //rectF.top = y -350;
        //rectF.bottom = y - 350 - ballHeight;
    }
}
