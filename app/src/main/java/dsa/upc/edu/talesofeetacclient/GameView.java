package dsa.upc.edu.talesofeetacclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import dsa.upc.edu.talesofeetacclient.Model.Cell.Cell;
import dsa.upc.edu.talesofeetacclient.Model.Cell.UserCell;
import dsa.upc.edu.talesofeetacclient.Model.Main.Location;
import dsa.upc.edu.talesofeetacclient.Model.Main.Map;
import dsa.upc.edu.talesofeetacclient.Model.Main.User;

/**
 * Created by mike on 22/11/17.
 */

public class GameView extends SurfaceView implements Runnable {

    // This is our thread
    Thread gameThread = null;

    private int screenHeight = 0;
    private int screenWidth = 0;
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
    float bobXPosition = 36;
    float bobYPosition = 36;

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

    private static final int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };

    private int direction;

    private static final int START_STAGE = 1;
    private static final int START_LEVEL = 1;

    private static final int CONTROLS_UP = 3;
    private static final int CONTROLS_DOWN = 0;
    private static final int CONTROLS_LEFT = 1;
    private static final int CONTROLS_RIGHT = 2;
    private static final int CONTROLS_A = 4;
    private static final int CONTROLS_B = 5;

    private Rect controlsUpRect;
    private Rect controlsDownRect;
    private Rect controlsLeftRect;
    private Rect controlsRightRect;
    private Rect controlsARect;
    private Rect controlsBRect;

    User user;
    List<Cell> cells;
    List<Map> maps;

    // When the we initialize (call new()) on gameView
    // This special constructor method runs
    public GameView(Context context, User u) {
        // The next line of code asks the
        // SurfaceView class to set up our object.
        // How kind.
        super(context);
        user = new User();
        user = u;

        // Initialize ourHolder and paint objects
        ourHolder = getHolder();
        paint = new Paint();
        initializeObjects();

        // Load Bob from his .png file
        maps.add(createMap(1));



        bitmapBob = BitmapFactory.decodeResource(this.getResources(), R.drawable.link128x128);
        frameHeight = bitmapBob.getHeight() /4;
        frameWidth = bitmapBob.getWidth() /4;
        //Void rectangle with .png's file size
        frameToDrawX.set(0,
                0,
                frameWidth,
                frameHeight);
        // Scale the bitmap to the correct size <--- Maybe we need it later


    }

    private void initializeObjects() {
        maps = new ArrayList<>();
    }

    public Map getMap (int id) {
        return this.maps.get(id - 1);
    }

    public void setMap (Map map) {
        this.maps.set(map.getId() - 1, map);
    }

    public void setCell(int mapId, Cell cell) {
        this.getMap(mapId).setCell(cell);
    }

    public Cell getCell(int mapId, Location l) {
        return getMap(mapId).getCell(l);
    }
    public Cell getCellByCoords(int mapId, int x, int y) {
        return getMap(mapId).getCellByCoords(x,y);
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
            screenWidth = canvas.getWidth();
            screenHeight = canvas.getHeight();

            drawUserInterface(screenWidth, screenHeight);
            drawMap(1, canvas);
            //DrawCell();
            //drawUserCell(user);
            whereToDrawX.set((int)bobXPosition,
                    bobYPosition,
                    (int)bobXPosition + frameWidth,
                    bobYPosition + frameHeight);
            getCurrentFrame(direction);
            canvas.drawBitmap(bitmapBob,
                    frameToDrawX,
                    whereToDrawX, paint);

            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawMap(int mapId, Canvas canvas) {
        for (int i=0; i<144; i++) {
            canvas.drawBitmap(getMap(mapId).getCellArray()[i].getBitmap(),null, getMap(mapId).getCellArray()[i].getRect(), null );
        }
    }

    private void drawUserInterface(int screenWidth, int screenHeight) {
        Paint playScreenPaint = new Paint();
        playScreenPaint.setColor(Color.argb(255,  26, 128, 182));

        Paint screenBorderPaint = new Paint();
        screenBorderPaint.setColor(Color.argb(255,96,96,96));

        // Draw the background color
        canvas.drawColor(Color.argb(255,96,96,96));
        canvas.drawRect(0,0,screenWidth,1044,playScreenPaint);
        canvas.drawRect(0,0,35,1044,screenBorderPaint);
        canvas.drawRect(0,0,1044,35,screenBorderPaint);
        canvas.drawRect(1044,0, 1080, 1044,screenBorderPaint);

        // Choose the brush color for drawing
        paint.setColor(Color.argb(255,  249, 129, 0));

        // Make the text a bit bigger
        paint.setTextSize(45);

        // Display the current fps on the screen
        canvas.drawText("FPS:" + fps, screenWidth/3 +100, 1130, paint);

        DrawControls();

    }

    private void DrawCell() {
        Rect rect = new Rect(screenWidth/2,36,screenWidth/2+84,36+84);
        Bitmap bitmapCell = BitmapFactory.decodeResource(this.getResources(), R.drawable.dngn_closed_door84x84);
        canvas.drawBitmap(bitmapCell, null, rect, null);
    }

    private void DrawControls() {
        //Up Control
        controlsUpRect = new Rect(160, screenHeight /2 + 200+200, 160+120, screenHeight /2 +200+120+200);
        Bitmap bitmapControlUp = BitmapFactory.decodeResource(this.getResources(), R.drawable.ctrl_up_arrow);
        canvas.drawBitmap(bitmapControlUp,null, controlsUpRect,null);
        //Left Control
        controlsLeftRect = new Rect(40, screenHeight /2 + 280+200, 40+120, screenHeight /2 +280+120+200);
        Bitmap bitmapControlLeft = BitmapFactory.decodeResource(this.getResources(), R.drawable.ctrl_left_arrow);
        canvas.drawBitmap(bitmapControlLeft,null, controlsLeftRect,null);
        //Right Control
        controlsRightRect = new Rect(280, screenHeight /2 + 280+200, 280+120, screenHeight /2 +280+120+200);
        Bitmap bitmapControlRight = BitmapFactory.decodeResource(this.getResources(), R.drawable.ctrl_right_arrow);
        canvas.drawBitmap(bitmapControlRight,null, controlsRightRect,null);
        //Down Control
        controlsDownRect = new Rect(160, screenHeight /2 + 360+200, 160+120, screenHeight /2 +360+120+200);
        Bitmap bitmapControlDown = BitmapFactory.decodeResource(this.getResources(), R.drawable.ctrl_down_arrow);
        canvas.drawBitmap(bitmapControlDown,null, controlsDownRect,null);
        //A Control
        controlsARect = new Rect(720, screenHeight /2 + 360+200, 720+130, screenHeight /2 +360+130+200);
        Bitmap bitmapControlA = BitmapFactory.decodeResource(this.getResources(), R.drawable.controls_a);
        canvas.drawBitmap(bitmapControlA,null, controlsARect,null);
        //B Control
        controlsBRect = new Rect(800, screenHeight /2 + 200+200, 800+120, screenHeight /2 +200+120+200);
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
            case MotionEvent.ACTION_DOWN: {

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
            }

                // Player has removed finger from screen
            case MotionEvent.ACTION_UP: {
                // Set isMoving so Bob does not move
                isMoving = false;

                break;
            }
        }


        return true;
    }

    public Map createMap (int mapId) {
        //logger.info("loadMap: Loading map...");
        try {
            StringBuilder s = new StringBuilder();
            s.append("map").append(mapId).append(".txt");
            ObjectMapper mapper = new ObjectMapper();
            BufferedReader br = new BufferedReader(new InputStreamReader(this.getResources().openRawResource(R.raw.map1)), 8192);
            Cell cells[] = mapper.readValue(br, Cell[].class);
            for (Cell cell : cells) {
                if (cell.getClass().getSimpleName().equals("Door")) {
                    cell.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.dngn_closed_door84x84));
                }
                else if (cell.getClass().getSimpleName().equals("Wall")) {
                    cell.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stone_brick4_84x84));
                }
                else if (cell.getClass().getSimpleName().equals("Tree")) {
                    cell.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.tree1_red84x84));
                }
                else if (cell.getClass().getSimpleName().equals("Field")) {
                    cell.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.dirt_s84x84));
                }

            }
            Map map = new Map(mapId, cells);
            //logger.info("loadMap: map loaded.");

            return map;
        }
        catch (IOException ex)
        {
            //logger.fatal("File not found");

            return null;
        }
    }


}