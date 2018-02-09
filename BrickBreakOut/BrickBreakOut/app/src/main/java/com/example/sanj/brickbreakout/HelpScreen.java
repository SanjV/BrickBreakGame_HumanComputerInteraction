package com.example.sanj.brickbreakout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
/**
 * Created by Sanjana Vijaykumar (sxv163930) on 12/4/2017.
 * Human Computer Interaction CS6326.001 - Assignment 6
 *
 * Displays the set of instructions for the Game play.
 * Loads the layout that contains an image of the Game rules.
 */

public class HelpScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }
}
