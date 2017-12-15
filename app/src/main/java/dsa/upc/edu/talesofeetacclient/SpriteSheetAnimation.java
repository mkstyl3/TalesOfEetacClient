package dsa.upc.edu.talesofeetacclient;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Josean on 15/12/2017.
 */

/* This class is from Toni Oller example of sprite animation */


public class SpriteSheetAnimation extends Activity{

    //Our object will hold the view and
    // the sprite sheet animation logic
    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize gameView and set it as the view
        gameView = new GameView(this);
        setContentView(gameView);

    }

    /* Here it is our implementation of GameView
    It is not an inner class
    Note it how the final closing curly brace
    is inside SpriteSheetAnimation

    Notice we implement runnable so we have
    a thread and can override the run method.
     */

    class GameView extends SurfaceView implements Runnable {

        //This is our thread
        Thread gameThread = null;

        //we need a surface when
        // we use Paint and Canvas in a thread
        SurfaceHolder ourHolder;

        //A boolean which we will set and unset
        //when the game is running or not.
        volatile boolean playing;

        //A canvas and a Paint object
        Canvas canvas;
        Paint paint;

        //This variable tracks the game frame rate
        long fps;

        //This is used to help calculate the fps
        private long timeThisFrame;

        //Declare and object of type Bitmap
        Bitmap bitmapRha;

        //Set starts off not moving
        boolean isMoving = false;

        //He can walk at 150 pixels per second
        float walkSpeedPerSecond = 250;

        //He starts 10 pixels from the left
        float rhaXPosition = 10;

        //New for the sprite sheet animation

        //These next two values can be anything you like
        //As long as the ratio doesn't distort the sprite too much
        //ATM, I'll let then equal to the example
        private int frameWidth = 100;
        private int frameHeight = 50;

        //How many frames are there on the sprite sheet?
        private int frameCount = 5;

        //Start at the first frame - where elseT
        private int currentFrame = 0;

        //What time was it when we last changed frames
        private long lastFrameChangeTime = 0;

        //How long should each frame last
        private int frameLengthInMilliseconds = 100;

        //A rectangle to define an area of the
        // sprite sheet that represents 1 frame
        private Rect frameToDraw = new Rect(
                0,
                0,
                frameWidth,
                frameHeight);

        //A rect that defines an area of the screen
        //on which to draw
        RectF whereToDraw = new RectF(
                rhaXPosition, 0,
                rhaXPosition + frameWidth,
                frameHeight);

        //When we initialize (call new()) on gameView
        //This special constructor method runs
        public GameView(Context context){

            //The next of code asks the
            //SurfaceView class to set up our object.
            //How kind.
            super(context);

            //initialize ourHodler and paint objects
            ourHolder = getHolder();
            paint = new Paint();

            //Load Rhas from his .png file
            bitmapRha = BitmapFactory.decodeResource(this.getResources(), R.drawable.rha_300x104);

            //Scale the bitmap to the correct size
            //we need to do this because android automatically
            //scales bitmaps based on screen density
            bitmapRha = Bitmap.createScaledBitmap(bitmapRha,
                    frameWidth * frameCount,
                    frameHeight,
                    false);

            //Set our boolean to true - game on!
            //playing = true;


        }

        @Override
        public void run() {
            while (playing) {

                //capture the current time in milliseconds
                //in startFrameTime
                long startFrameTIme = System.currentTimeMillis();

                //Update the frame
                update();

                //Draw the frame
                draw();

                //Calculate the fps this frame
                //we can then use the result to
                //time animations and more
                timeThisFrame = System.currentTimeMillis() - startFrameTIme;
                if (timeThisFrame >= 1) {
                    fps = 1000 / timeThisFrame;
                }
            }
        }

        //everything that needs to be updated goes in here
        //in later projects we will have dosens (arrays of objects
        //We will also do other ghings like collision detection
        public void udpate(){

            //If Rha is moving (the player is touching the screen)
            //then move him to the right based on his target speed and
            //the current fps.
            if(isMoving){
                rhaXPosition = rhaXPosition + (walkSpeedPerSecond / fps);
            }
        }















    }
}








