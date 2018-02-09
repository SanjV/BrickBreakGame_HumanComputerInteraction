package com.example.sanj.brickbreakout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 *  Created by Sanjana Vijaykumar (sxv163930)  11/26/2017.
 *  Human Computer Interaction CS6326.001 - Assignment 6
 *
 *  BreakOutGameScreen.
 *  This is the where the entire game loop resides.
 *
 *  This activity sets BreakOutView as its layout.
 *  BreakOutView class extends SurfaceView class,
 *                             to get a hold on the surface for the Canvas to draw objects
 *                      implements Runnable interface,
 *                              to start the Gaming thread and loop it continuously.
 *                      implements SensorEventListener,
 *                              for implementing Sensor (Accelerometer).
 *
 *  The BreakOutView constructor, gets hold of the surface,
 *                                initializes paint object,
 *                                gets screen Dimensions,
 *                                initializes paddleObject and ballObject objects,
 *                                Constructs the bricks layout.
 *
 *  Since, it implements Runnable interface, it has to implement Thread methods, run, onPause and onResume
 *
 *  The thread run method, updates and draws the screen objects.
 *  Responds to Touch events(onTouchEvent) and Sensor events(onSensorChanged),
 *          Changes the paddleObject position according to the event.
 *
 *          Implemented paddleObject movement with touch and flip (Accelerometer sensor).
 *
 *  Implements Pause and resume game functionality with the Pause and Play icon in the Action Bar.
 *
 */
public class BreakOutGameScreen extends AppCompatActivity {

    public BreakOutView breakOutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_break_out);

        breakOutView = new BreakOutView(this);
        setContentView(breakOutView);



    }

    @Override
    /**
     *  Sets the Play and Pause icons on the Menu, according to the game state(pause or resumed state)
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.PauseButton){
            if(breakOutView.paused == true){
                breakOutView.paused = false;
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.pause));
            }
            else{
                breakOutView.paused = true;
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.play));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        BreakOutGameScreen.this.invalidateOptionsMenu();
        return true;
    }

    class BreakOutView extends SurfaceView implements Runnable,SensorEventListener{

        Thread gameThread = null;
        SurfaceHolder surfaceHolder;
        volatile boolean gameStatus;
        boolean paused = true;


        Canvas canvas;
        Paint paint;

        long fps;
        private long timeFrame;
        int screenX;
        int screenY;

        PaddleObject paddleObject;
        BallObject ballObject;
        BrickObject[] brickObjectWall = new BrickObject[100];
        int numberOfBricks = 0;
        int bricksBroke = 0;
        int score = 0;
        int lives = 3;

        float intialTouch = 0;

        private SensorManager sensorManager;
        private Sensor sensor;
        public float sensorX;
        public float sensorY;
        public float paddleLeft;
        public float paddleRight;

        public BreakOutView(Context context){
            super(context);

            surfaceHolder = getHolder();
            paint = new Paint();

            sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            screenX = size.x;
            screenY = size.y;

            paddleObject = new PaddleObject(screenX, screenY);
            ballObject = new BallObject(screenX, screenY);

            paddleLeft = paddleObject.getRectF().left;
            paddleRight = paddleObject.getRectF().right;

            createBricksLayout();
        }

        /**
         *  Creates a layout of bricks on the screen.
         *  Starting at a position of screenY/10, it creates 4 rows and 5 columns of bricks.
         *  This can be customized to any design.
         *  Also resets the position of ballObject, whenever the layout is created.
         */
        public void createBricksLayout(){
            //ballObject.reset(screenX/2 + 45, screenY - 140);
            ballObject.reset(screenX/2 + 90, screenY - 245);
            int brickWidth = screenX / 6;
            int brickHeight = screenY / 20;

            numberOfBricks = 0;
            for(int i =2; i< 6; i++){
                for(int j =0; j< 6; j++){
                    brickObjectWall[numberOfBricks] = new BrickObject(i, j, brickWidth, brickHeight);
                    numberOfBricks++;
                }
            }
        }
        @Override
        /**
         *  Game loop
         *      updates the surface objects based on the game state (paused or not).
         *      calculates fps, base on game start time and time taken to draw the entire layout.
         *      draws the objects on the surface through draw() method.
         */
        public void run() {
            while (gameStatus){
                long startFrameTime = System.currentTimeMillis();

                if(!paused){
                    update();
                }

                draw();

                timeFrame = System.currentTimeMillis() - startFrameTime;
                if(timeFrame >= 1)
                    fps = 1300/timeFrame;
            }
        }

        /**
         *  The Objects to be drawn on the Canvas.
         *  draws all the game objects,
         *                              PaddleObject, BallObject, Bricks, Top Wall,
         *                              Score, Lives.
         *  Draws teh layout of bricks based on their visibility.
         *  This is where we can change colors of different objects, using paint object.
         */
        private void draw() {
            if(surfaceHolder.getSurface().isValid()){
                canvas = surfaceHolder.lockCanvas();

                canvas.drawColor(Color.parseColor("#191d23"));

                paint.setColor(Color.parseColor("#FFFFFF"));
                paint.setTextSize(50);

                canvas.drawText("Lives:", screenX/2 - 250, screenY/27, paint);
                canvas.drawText(String.valueOf(lives), screenX/2 + 150 - 250, screenY/27, paint); // Number of lives
                canvas.drawText("Score:",screenX/2 + 100, screenY/27, paint);
                canvas.drawText(String.valueOf(score), screenX/2 + 250, screenY/27, paint); // Score
                canvas.drawLine(screenX/2 - 260, screenY/25, screenX/2 + 280, screenY/25, paint);
                //canvas.drawLine(0, screenY/10, screenX, screenY/10, paint);
                // This is the Top Wall, that restricts the movement of ballObject on the Top of the screen.
               // paint.setColor(Color.parseColor("#4682B4"));
               // canvas.drawRect(0, screenY/10, screenX, screenY/12, paint);


                canvas.drawRect(paddleObject.getRectF(), paint); //PaddleObject Object
                paint.setColor(Color.parseColor("#FFFFFF"));
                canvas.drawRect(ballObject.getRectF(), paint); // BallObject Object

                //Drawing Bricks Layout
                for(int i=0; i<numberOfBricks; i++){
                    int y = 0;
                    if(brickObjectWall[i].getVisibility()){
                        if( i%2 == 0)
                            paint.setColor(Color.parseColor("#40b4ff"));
                        else
                            paint.setColor(Color.parseColor("#0012d8"));
                        canvas.drawRect(brickObjectWall[i].getRectF(), paint);
                    }
                }

                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }

        /**
         *  Update the objects on the screen.
         *  Updates the position of the ballObject, paddleObject and visibility of the bricks.
         *
         *  This part of code also contains logic of "Detecting Collision".
         *
         *  Collision of Objects:
         *      Since we use all objects o be rectangles, in the screen, detecting collisions was easy,
         *      which include detecting intersection of two rectangle objects.
         *
         *      If other shapes are used, it has to be handled likewise.
         *
         *      If ballObject collides with the brick, the visibility of brick is changed to false,
         *          so that in he later part, when we draw the bricks layout, the collided brick doesn't appear.
         *          This will be brick disappearing on collision with ballObject.
         *          And the BallObject direction will be reversed along Y-direction.
         *
         *      If ballObject collides with the paddleObject or Top Wall, it's direction along Y-direction is reversed.
         *      If ballObject collides with the sides of the screen, it's direction along X-xis will be reversed.
         *
         *      Update function also checks for Number of lives remained and Number of bricks broken,
         *          if either of them are updated, it will procedd to Displaying your score i.e., ScoreDisplayActivity.
         *
         */
        private void update() {

            paddleObject.update(fps);
            ballObject.update(fps);

            RectF ballRect = ballObject.getRectF();
            if(ballRect.right >= screenX)
                ballObject.revXVelocity();

            if(ballRect.left <= 0)
                ballObject.revXVelocity();

            if(ballRect.top <= screenY/250)
                ballObject.revYVelocity();

            if((!paused && ballRect.bottom >= paddleObject.getRectF().top && ballRect.left >= paddleObject.getRectF().left && ballRect.right <= paddleObject.getRectF().right && ballRect.bottom < paddleObject.getRectF().bottom)
                || (ballRect.right == paddleObject.getRectF().left && ballRect.bottom < paddleObject.getRectF().bottom) || (ballRect.bottom == paddleObject.getRectF().right  && ballRect.bottom < paddleObject.getRectF().bottom)){
                ballObject.revYVelocity();
            }


            if(ballRect.bottom >= screenY || ballRect.bottom >= paddleObject.getRectF().bottom){
                ballObject.reset(paddleObject.getRectF().left + 90, screenY - 245);
                paused = true;
                ballObject.xVelocity = 250;
                ballObject.yVelocity = -500;
                lives--;
                if(lives < 0){
                    Intent intent = new Intent(getApplicationContext(), ScoreDisplayActivity.class);
                    intent.putExtra("Score", score);
                    intent.putExtra("Lives", lives);
                    BreakOutGameScreen.this.startActivity(intent);
                    finish();
                }
            }

            for(int i =0; i<numberOfBricks; i++){
                if(brickObjectWall[i].isVisible){
                    if(RectF.intersects(brickObjectWall[i].getRectF(), ballRect)){
                        brickObjectWall[i].setInvisible();
                        ballObject.revYVelocity();
                        bricksBroke++;
                        score += 20;
                    }
                }
            }

            if(bricksBroke == numberOfBricks){
                Intent intent = new Intent(getApplicationContext(), ScoreDisplayActivity.class);
                intent.putExtra("Score", score);
                intent.putExtra("Lives", lives);
                BreakOutGameScreen.this.startActivity(intent);
                finish();
            }

        }

        public void pause() throws InterruptedException {
            gameStatus = false;
            gameThread.join();
        }

        public void resume(){
            gameStatus = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        /**
         * Checks the X position of where touch happened,
         *  Changes the state of the paddleObject accordingly.
         * @param motionEvent
         * @return
         */
        public boolean onTouchEvent(MotionEvent motionEvent){
            switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_DOWN:
                    paused = false;
                    intialTouch = motionEvent.getX();
                    if(intialTouch > screenX/2)
                        paddleObject.setState(paddleObject.RIGHT);
                    else
                        paddleObject.setState(paddleObject.LEFT);
                    break;

                case MotionEvent.ACTION_MOVE:
                    float x = motionEvent.getX();
                    if(x-intialTouch > 0)
                        paddleObject.setState(paddleObject.RIGHT);
                    else if(x - intialTouch < 0)
                        paddleObject.setState(paddleObject.LEFT);
                    else{
                        if(x - paddleObject.getRectF().left <= 0)
                            paddleObject.setState(paddleObject.LEFT);

                        if(x - paddleObject.getRectF().right >= 0)
                            paddleObject.setState(paddleObject.RIGHT);
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    paddleObject.setState(paddleObject.STOPPED);
                    break;
            }
            return true;
        }

        @Override
        /**
         *  Detects the X-position and Y-position of the sensor, and sets the paddleObject movement state
         *  according to the x-value.
         *  if the tilt is towards eft, paddleMovement is set to Left,
         *  else if it is right, paddleObject movement i set to right.
         *  els,e paddleObject will state will be Static.
         */
        public void onSensorChanged(SensorEvent event) {
            Sensor accelerometer = event.sensor;
            if(accelerometer.getType() == Sensor.TYPE_ACCELEROMETER){
                sensorX = event.values[0];
                sensorY = event.values[1];

                paddleLeft += sensorX * 2;
                paddleRight += sensorY * 2;

                if(sensorX < 0) {
                    paddleObject.setState(paddleObject.RIGHT);
                }
                else if(sensorX > 0){
                    paddleObject.setState(paddleObject.LEFT);
                }
                else
                    paddleObject.setState(paddleObject.STOPPED);

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    protected void onResume(){
        super.onResume();
        breakOutView.resume();
    }

    protected void onPause(){
        super.onPause();
        try {
            breakOutView.pause();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
