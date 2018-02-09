package com.example.sanj.brickbreakout;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Sanjana Vijaykumar (sxv163930) on 11/30/2017.
 * Human Computer Interaction CS6326.001 - Assignment 6
 *
 * HighScoreActivity
 * Displays a List of Scores (Leader Board).
 *
 * Load the list of scores from highscoreslistdata file.
 *
 * highscoreslistdata - stores the lst of scores, submitted by the users.
 * If the user does not specify their name, it will be displayed as Anonymous.
 *
 * Displays list by implementing custom array adapter of high score objects.
 */

public class HighScoreActivity extends AppCompatActivity{

    public List<HighScoreObject> scoresFromFile;
    HashSet<HighScoreObject> scoreSet;
    List<HighScoreObject> scoreList;

    public static final String fileName = "highscoreslistdata";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);


        File file = getFilesDir();
        String path = file.getAbsolutePath();

        scoresFromFile = fileRead(this.getApplicationContext(), file);
        Intent intent = getIntent();
        int status = intent.getIntExtra("Status", 0);
        String userName = intent.getStringExtra("Name");
        int score = intent.getIntExtra("Score", 0);

        scoreSet = new HashSet<HighScoreObject>(scoresFromFile);
        scoreList = new ArrayList<HighScoreObject>(scoreSet);

        if(status == 1){
            HighScoreObject highScoreObject = new HighScoreObject(userName, String.valueOf(score));
            int flag = 0;
            for(int i=0; i<scoresFromFile.size(); i++){
                if(highScoreObject.getName() == scoresFromFile.get(i).getName() && highScoreObject.getScore() == scoresFromFile.get(i).getScore())
                    flag = 1;
            }
            if(flag == 0){
                scoresFromFile.add(highScoreObject);
                scoreSet = new HashSet<HighScoreObject>(scoresFromFile);
                scoreList = new ArrayList<HighScoreObject>(scoreSet);
                writeContactsToFile(fileName, scoreList);

            }
        }

        /*
        List<HighScoreObject> highScores = new ArrayList<HighScoreObject>();
        highScores.add(new HighScoreObject("Ram", "500"));
        highScores.add(new HighScoreObject("Sanj", "400"));
        highScores.add(new HighScoreObject("Sindhu", "800"));
        */
        if(scoresFromFile.size() > 0){
            Collections.sort(scoreList, new Comparator<HighScoreObject>() {
                @Override
                public int compare(HighScoreObject o1, HighScoreObject o2) {
                    return o2.getScore().compareTo(o1.getScore());
                }
            });
        }
        final ListView listview = (ListView) findViewById(R.id.list);
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1 , scoreList);
        listview.setAdapter(adapter);

    }

    /**
     * Method to read from the file and return a list of scores.
     * @param context = Current Application Context
     * @param file = file object.
     * @return = list f HughScore objects (name, score of the user).
     */
    public static List<HighScoreObject> fileRead(Context context, File file){
        List<String> records = null;
        records = readLines(context, file);
        List<HighScoreObject> highScoreObjects = new ArrayList<HighScoreObject>();
        for(String line:records){
         if(!line.equals("")){
             HighScoreObject highScoreObject = StringToObject(line);
             if(highScoreObject.toString() !="" && highScoreObject.getScore()!= null && highScoreObject.getName()!=null)
                 highScoreObjects.add(highScoreObject);
         }
        }
        return highScoreObjects;
    }

    /**
     * Helper function to convert a string to HighScoreObject object.
     * Used to retrieve scores (HighScoreObject objects) form the text file.
     * @param highscore = HighScoreObject object that contains name, score.
     * @return = Hgh score object.
     */
    public static HighScoreObject StringToObject(String highscore){
        HighScoreObject highScoreObject = new HighScoreObject();
        if(highscore!=""){
            String[] record = highscore.split(" ");
            if(record.length == 2 && record[0]!=null && record[1]!=null){
                highScoreObject.setName(record[0]);
                highScoreObject.setScore(record[1]);
            }
        }
        return highScoreObject;
    }

    /**
     * Reads from the text file and returns  a list of individual lines of the text file.
     * Each line represent string value of high score object.
     * @param context = Application context
     * @param file = file object
     * @return = list of strings that contains name of the user + score as a string
     */
    public static List<String> readLines(Context context, File file) {
        List<String> lines = new ArrayList<String>();
        String line;

        InputStream is = null;
        try {
            is = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            while((line = br.readLine())!=null){
                lines.add(line);
            }
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * writes the list of high scores to the specified file
     * @param filename = name of the file to be written (highscoreslistdata)
     * @param scores = array list of scores.
     */
    public  void writeContactsToFile(String filename, List<HighScoreObject> scores){
        FileOutputStream fos =null;
        try{
            fos =openFileOutput(filename, MODE_ENABLE_WRITE_AHEAD_LOGGING);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            for(int i=0; i<scores.size(); i++){
                if(scores.get(i).toString()!= "" && scores.get(i).getScore()!=null && scores.get(i).getName()!=null)
                    osw.write(scores.get(i).toString()+"\n");
            }
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stable Array Adapter
     * This is a custom adapter that stacks holds score view objects.
     * To hold individual High Score objects (each single row element).
     */

    private class StableArrayAdapter extends ArrayAdapter<HighScoreObject> {

        HashMap<HighScoreObject, Integer> mIdMap = new HashMap<HighScoreObject, Integer>();
        //List<HighScoreObject> highScores = new ArrayList<HighScoreObject>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<HighScoreObject> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }

        }

        @Override
        public long getItemId(int position) {
            HighScoreObject item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}
