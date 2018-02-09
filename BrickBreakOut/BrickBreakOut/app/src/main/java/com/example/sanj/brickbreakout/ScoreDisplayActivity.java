package com.example.sanj.brickbreakout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Sanjana Vijaykumar (sxv163930) on 12/2/2017.
 * Human Computer Interaction CS6326.001 - Assignment 6
 *
 * Enter Score Activity
 * This Activity comes after your Game is Over.
 *
 * Displays your score, appreciates you and allows you to submit your score.
 *
 * if you lose all the lives and game completes, it displays Good Try!!!
 * if you complete breaking all the bricks, it displays You are Awesome!!!
 *
 * Nevertheless, it allows you to enter your name, if you want to submit to score.
 * If you enter your name and press submit, your name will be added to the list of scores and
 *                                          the Leader Board(list of scores) will be displayed.
 * If you do not enter the name, the score will be listed as Anonymous if you press submit.
 *
 * Alteratively, if the user do no wish to submit, he can either proceed to Main menu or Leader Board.
 *
 */
public class ScoreDisplayActivity extends AppCompatActivity {
    private EditText nameOfGamer;
    private Button submitDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_score);

        Intent intent = getIntent();
        final int score = intent.getIntExtra("Score", 0);
        int lives = intent.getIntExtra("Lives", -1);

        TextView performance = (TextView)findViewById(R.id.PerformanceText);
        if(lives < 0){
            performance.setText("");
            performance.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }

        else
            performance.setText("You are Awesome !!!");

        TextView scoreText = (TextView)findViewById(R.id.scoreText);
        scoreText.setText("Your Score:");

        TextView finalScore = (TextView)findViewById(R.id.finalScore);
        finalScore.setText(String.valueOf(score));

        Button mainMenu = (Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreDisplayActivity.this, MainActivity.class);
                ScoreDisplayActivity.this.startActivity(intent);
                finish();
            }
        });

        nameOfGamer = (EditText)findViewById(R.id.nameEdit);

        submitDetails = (Button)findViewById(R.id.submit);

        submitDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreDisplayActivity.this, HighScoreActivity.class);
                if(nameOfGamer.getText().toString().trim().equals(""))
                    intent.putExtra("Name", "Anonymous");
                else
                    intent.putExtra("Name",nameOfGamer.getText().toString());

                intent.putExtra("Score", score);
                intent.putExtra("Status", 1);
                ScoreDisplayActivity.this.startActivity(intent);
                finish();
            }
        });

        Button hihghscores = (Button)findViewById(R.id.highscoreActivity);
        hihghscores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ScoreDisplayActivity.this, HighScoreActivity.class);
                ScoreDisplayActivity.this.startActivity(intent1);
            }
        });

    }
}
