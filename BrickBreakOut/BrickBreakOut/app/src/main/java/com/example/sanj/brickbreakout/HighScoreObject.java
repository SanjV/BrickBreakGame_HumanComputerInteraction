package com.example.sanj.brickbreakout;

/**
 * Created by Sanjana Vijaykumar (sxv163930) on 12/3/2017.
 *
 * High Score class.
 * Defines the Structure, properties of high score object.
 * Helps to display and sort scores of the users in the Leader board.
 * Implements getter and setter methods.
 */

public class HighScoreObject {
    String name;
    String score;

    public HighScoreObject(String name, String score){
        this.name = name;
        this.score = score;
    }
    public HighScoreObject(){}

    public String getName(){
        return name;
    }

    public String getScore(){
        return score;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setScore(String score){
        this.score = score;
    }

    public String toString(){ return name +" "+ score;}
}
