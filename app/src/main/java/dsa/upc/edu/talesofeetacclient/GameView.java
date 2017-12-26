package dsa.upc.edu.talesofeetacclient;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import dsa.upc.edu.talesofeetacclient.View.GameImage;

/**
 * Created by mike on 22/11/17.
 */

public class GameView extends SurfaceView implements Runnable {

    // This is our thread
    Thread gameThread = null;
    private float mScreenDensity;
    private Context mGameContext;
    private int mScreenXMax = 0;
    private int mScreenYMax = 0;

    // This is new. We need a SurfaceHolder
    // When we use Paint and Canvas in a thread
    // We will see it in action in the draw method soon.
    SurfaceHolder ourHolder;

    // A boolean which we will set and unset
    // when the game is running- or not.
    volatile boolean playing;

    // A Canvas and a Paint object
    Canvas canvas;
    Paint paint;

    // This variable tracks the game frame rate
    long fps;

    // This is used to help calculate the fps
    private long timeThisFrame;

    // Declare an object of type Bitmap
    Bitmap bitmapBob;

    // Bob starts off not moving
    boolean isMoving = false;

    // He can walk at 150 pixels per second
    float walkSpeedPerSecond = 150;

    // He starts 10 pixels from the left
    float bobXPosition = 10;
    float bobYPosition = 10;

    // These next two values can be anything you like
    // As long as the ratio doesn't distort the sprite too much
    private int frameWidth;
    private int frameHeight;

    // How many frames are there on the sprite sheet?
    private int frameCount = 4;

    // Start at the first frame - where else?
    private int currentFrame = 0;

    // What time was it when we last changed frames
    private long lastFrameChangeTime = 0;

    // How long should each frame last
    private int frameLengthInMilliseconds = 100;

    private Rect frameToDrawX = new Rect();

    // A rect that defines an area of the screen
    // on which to draw
    RectF whereToDrawX = new RectF();
    RectF whereToDrawY = new RectF();

    private static final int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };

    private int direction;
    private static final int CONTROLS_PADDING = 10;

    private static final int START_STAGE = 1;
    private static final int START_LEVEL = 1;

    private static final int DIRECTION_UP = 1;
    private static final int DIRECTION_DOWN = 2;
    private static final int DIRECTION_LEFT = 3;
    private static final int DIRECTION_RIGHT = 4;
    private static final int DIRECTION_A = 5;
    private static final int DIRECTION_B = 6;

    private GameImage ctrlUpArrowImage = null;
    private GameImage ctrlDownArrowImage = null;
    private GameImage ctrlLeftArrowImage = null;
    private GameImage ctrlRightArrowImage = null;

    Resources res;

    private Rect controlsUpRect;
    private Rect controlsDownRect;
    private Rect controlsLeftRect;
    private Rect controlsRightRect;
    private Rect controlsARect;
    private Rect controlsBRect;



    // When the we initialize (call new()) on gameView
    // This special constructor method runs
    public GameView(Context context) {
        // The next line of code asks the
        // SurfaceView class to set up our object.
        // How kind.
        super(context);
        this.mGameContext = context;

        // Initialize ourHolder and paint objects
        ourHolder = getHolder();
        paint = new Paint();

        res = context.getResources();


        // Load Bob from his .png file
        bitmapBob = BitmapFactory.decodeResource(this.getResources(), R.drawable.link128x128);
        frameHeight = bitmapBob.getHeight() / 4;
        frameWidth = bitmapBob.getWidth() /4;
        frameToDrawX.set(0,
                0,
                frameWidth,
                frameHeight);
        whereToDrawX.set(bobXPosition, 0,
                bobXPosition + frameWidth,
                frameHeight);

        whereToDrawY.set(bobXPosition, 0,
                bobYPosition + frameWidth,
                frameHeight);
        // Scale the bitmap to the correct size
        // We need to do this because Android automatically
        // scales bitmaps based on screen density
        //bitmapBob = Bitmap.createScaledBitmap(bitmapBob, frameWidth * frameCount, frameHeight, false);

        // Set our boolean to true - game on!
        //playing = true;

    }

    @Override
    public void run() {
        while (playing) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();
            synchronized (ourHolder) {
                // Update the frame
                update();

                // Draw the frame
                draw();

                // Calculate the fps this frame
                // We can then use the result to
                // time animations and more.
                timeThisFrame = System.currentTimeMillis() - startFrameTime;
                if (timeThisFrame > 0) {
                    fps = 1000 / timeThisFrame;
                }
            }
        }

    }

    // Everything that needs to be updated goes in here
    // In later projects we will have dozens (arrays) of objects.
    // We will also do other things like collision detection.
    public void update() {

        // If bob is moving (the player is touching the screen)
        // then move him to the right based on his target speed and the current fps.
        if(isMoving){
            switch (direction) {
                case 3 : {
                    bobYPosition = bobYPosition - (walkSpeedPerSecond / fps);
                    break;
                }

                case 0 : {
                    bobYPosition = bobYPosition + (walkSpeedPerSecond / fps);
                    break;
                }

                case 1 : {
                    bobXPosition = bobXPosition - (walkSpeedPerSecond / fps);
                    break;
                }

                case 2 : {
                    bobXPosition = bobXPosition + (walkSpeedPerSecond / fps);
                    break;
                }
            }
        }

    }

    // Draw the newly updated scene
    public void draw() {

        // Make sure our drawing surface is valid or we crash
        if (ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas();
            int playScreenHeight = canvas.getHeight()/2;
            int playScreenWidth = canvas.getWidth();
            mScreenXMax = canvas.getWidth();
            mScreenYMax = canvas.getHeight();

            Paint paint2 = new Paint();
            paint2.setColor(Color.argb(255,  26, 128, 182));
            // Draw the background color
            canvas.drawColor(Color.argb(255,96,96,96));
            canvas.drawRect(0,0,playScreenWidth,playScreenHeight,paint2);
            // Draw the background color


            // Choose the brush color for drawing
            paint.setColor(Color.argb(255,  249, 129, 0));

            // Make the text a bit bigger
            paint.setTextSize(45);

            // Display the current fps on the screen
            canvas.drawText("FPS:" + fps, 20, 40, paint);

            // Draw bob at bobXPosition, 200 pixels

            //canvas.drawBitmap(bitmapBob, bobXPosition, 200, paint);

            whereToDrawX.set((int)bobXPosition,
                    bobYPosition,
                    (int)bobXPosition + frameWidth,
                    bobYPosition+frameHeight);

            getCurrentFrame(direction);

            canvas.drawBitmap(bitmapBob,
                    frameToDrawX,
                    whereToDrawX, paint);

            DrawControls();

            // Draw bob at bobXPosition, 200 pixels
            //canvas.drawBitmap(bitmapBob, bobXPosition, 200, paint);

            // Draw everything to the screen
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void DrawControls() {
        //Up Control
        controlsUpRect = new Rect(160, mScreenYMax/2 + 200, 160+120,mScreenYMax/2 +200+120);
        Bitmap bitmapControlUp = BitmapFactory.decodeResource(this.getResources(), R.drawable.ctrl_up_arrow);
        canvas.drawBitmap(bitmapControlUp,null, controlsUpRect,null);
        //Left Control
        controlsLeftRect = new Rect(40, mScreenYMax/2 + 280, 40+120,mScreenYMax/2 +280+120);
        Bitmap bitmapControlLeft = BitmapFactory.decodeResource(this.getResources(), R.drawable.ctrl_left_arrow);
        canvas.drawBitmap(bitmapControlLeft,null, controlsLeftRect,null);
        //Right Control
        controlsRightRect = new Rect(280, mScreenYMax/2 + 280, 280+120,mScreenYMax/2 +280+120);
        Bitmap bitmapControlRight = BitmapFactory.decodeResource(this.getResources(), R.drawable.ctrl_right_arrow);
        canvas.drawBitmap(bitmapControlRight,null, controlsRightRect,null);
        //Down Control
        controlsDownRect = new Rect(160, mScreenYMax/2 + 360, 160+120,mScreenYMax/2 +360+120);
        Bitmap bitmapControlDown = BitmapFactory.decodeResource(this.getResources(), R.drawable.ctrl_down_arrow);
        canvas.drawBitmap(bitmapControlDown,null, controlsDownRect,null);
        //A Control
        controlsARect = new Rect(720, mScreenYMax/2 + 360, 720+130,mScreenYMax/2 +360+130);
        Bitmap bitmapControlA = BitmapFactory.decodeResource(this.getResources(), R.drawable.controls_a);
        canvas.drawBitmap(bitmapControlA,null, controlsARect,null);
        //B Control
        controlsBRect = new Rect(800, mScreenYMax/2 + 200, 800+120,mScreenYMax/2 +200+120);
        Bitmap bitmapControlB = BitmapFactory.decodeResource(this.getResources(), R.drawable.controls_b);
        canvas.drawBitmap(bitmapControlB,null, controlsBRect,null);
    }


    public void getCurrentFrame(int direction){

        long time  = System.currentTimeMillis();
        if(isMoving) {// Only animate if bob is moving
            if ( time > lastFrameChangeTime + frameLengthInMilliseconds) {
                lastFrameChangeTime = time;
                currentFrame++;
                if (currentFrame >= frameCount) {

                    currentFrame = 0;
                }
            }
        }
        //update the left and right values of the source of
        //the next frame on the spritesheet
        frameToDrawX.left = currentFrame * frameWidth;
        switch (direction) {
            case 3 : {
                frameToDrawX.offsetTo(currentFrame * frameWidth, 3*frameHeight);
                break;
            }
            case 0 : {
                frameToDrawX.offsetTo(currentFrame * frameWidth, 0*frameHeight);
                break;
            }
            case 2 : {
                frameToDrawX.offsetTo(currentFrame * frameWidth, 2*frameHeight);
                break;
            }
            case 1 : {
                frameToDrawX.offsetTo(currentFrame * frameWidth, 1*frameHeight);
                break;
            }

        }
        frameToDrawX.right = frameToDrawX.left + frameWidth;

    }

    // If SimpleGameEngine Activity is paused/stopped
    // shutdown our thread.
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    // If SimpleGameEngine Activity is started then
    // start our thread.
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    // The SurfaceView class implements onTouchListener
    // So we can override this method and detect screen touches.
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:
                final int x = (int) motionEvent.getX();
                final int y = (int) motionEvent.getY();

                if (controlsUpRect.contains(x,y)) {
                    // Set isMoving so Bob is moved in the update method
                    isMoving = true;
                    direction = 3;
                    break;
                }

                else if (controlsDownRect.contains(x,y)) {
                    // Set isMoving so Bob is moved in the update method
                    isMoving = true;
                    direction = 0;
                    break;
                }

                else if (controlsLeftRect.contains(x,y)) {
                    // Set isMoving so Bob is moved in the update method
                    isMoving = true;
                    direction = 1;
                    break;
                }

                else if (controlsRightRect.contains(x,y)) {
                    // Set isMoving so Bob is moved in the update method
                    isMoving = true;
                    direction = 2;
                    break;
                }

                else if (controlsARect.contains(x,y)) {
                    // Set isMoving so Bob is moved in the update method
                    isMoving = false;
                    //actionA = yes;
                    break;
                }

                else if (controlsBRect.contains(x,y)) {
                    // Set isMoving so Bob is moved in the update method
                    isMoving = false;
                    //actionB = yes;
                    break;
                }

                //private static final int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };


            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:

                // Set isMoving so Bob does not move
                isMoving = false;

                break;
        }
        return true;
    }


    /*
    private static final int DIRECTION_UP = 1;
    private static final int DIRECTION_DOWN = 2;
    private static final int DIRECTION_LEFT = 3;
    private static final int DIRECTION_RIGHT = 4;
    private static final int DIRECTION_A = 5;
    private static final int DIRECTION_B = 6;
    */


}