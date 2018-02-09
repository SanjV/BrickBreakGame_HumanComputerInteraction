package com.example.sanj.brickbreakout;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Sanjana Vijaykumar (sxv163930) on 11/28/2017.
 * Human Computer Interaction CS6326.001 - Assignment 6
 *
 * Main Activity.
 * Displays Main Menu.
 * Contains New Game, to start a new game,
 *          High Score, to view the Leader Board,
 *          Help, to view the help menu or instructions,
 *          Quit, to exit the Application.
 *
 * On Quit, it displays a dialogue box that asks you, whetehr you really want to quit the game.
 *          Exits the game on OK
 *          Remains the same on CANCEL
 *
 * Launches appropriate activities, by communicating through intents, on button click.
 * Entry screen to all other activities in the Application.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button quitButton = (Button) findViewById(R.id.quit);
        quitButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Quit Game");
                alertDialog.setMessage("Do you really want to Close the Game?");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });

        Button newGameButton = (Button)findViewById(R.id.newgame);
        newGameButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BreakOutGameScreen.class);
                MainActivity.this.startActivity(intent);
            }
        });

        Button highscoresButton = (Button)findViewById(R.id.highscores);
        highscoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HighScoreActivity.class);
                intent.putExtra("Status", 0);
                MainActivity.this.startActivity(intent);
            }
        });

        Button helpButton = (Button)findViewById(R.id.help);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HelpScreen.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }
}
