package dsa.upc.edu.talesofeetacclient.View.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import dsa.upc.edu.talesofeetacclient.Controller.ApiAdapter;
import dsa.upc.edu.talesofeetacclient.Model.Cell.Cell;
import dsa.upc.edu.talesofeetacclient.Model.Cell.ChestCell;
import dsa.upc.edu.talesofeetacclient.Model.Cell.Door;
import dsa.upc.edu.talesofeetacclient.Model.Cell.NPC;
import dsa.upc.edu.talesofeetacclient.Model.Cell.UserCell;
import dsa.upc.edu.talesofeetacclient.Model.Main.Relation.ChestItem;
import dsa.upc.edu.talesofeetacclient.Model.Main.Item;
import dsa.upc.edu.talesofeetacclient.Model.Main.Location;
import dsa.upc.edu.talesofeetacclient.Model.Main.Map;
import dsa.upc.edu.talesofeetacclient.Model.Main.Relation.UserItem;
import dsa.upc.edu.talesofeetacclient.Model.Main.User;
import dsa.upc.edu.talesofeetacclient.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mike on 22/11/17.
 */

public class GameView extends SurfaceView implements Runnable {

    //Logging TAG
    private static final String TAG = "GameView";
    // This is our thread
    private Thread gameThread = null;

    private int screenHeight = 0;
    private int screenWidth = 0;
    // This is new. We need a SurfaceHolder
    // When we use Paint and Canvas in a thread
    // We will see it in action in the draw method soon.
    private SurfaceHolder ourHolder;

    // A boolean which we will set and unset
    // when the game is running- or not.
    private volatile boolean playing;

    // A Canvas and a Paint object
    private Canvas canvas;
    private Paint paint;

    // This variable tracks the game frame rate
    private long fps;

    // This is used to help calculate the fps
    private long timeThisFrame;

    // Declare an object of type Bitmap
    private Bitmap bitmapBob;

    // Bob starts off not moving
    private boolean isMoving = false;
    private boolean actionA = false;
    private boolean actionB = false;
    private boolean textMode = false;

    // He can walk at 150 pixels per second
    private float walkSpeedPerSecond = 150;

    // He starts 10 pixels from the left
    private float bobXPosition = 120;
    private float bobYPosition = 120;


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
    private Rect whereToDrawX = new Rect();

    private static final int[] DIRECTION_TO_ANIMATION_MAP = {3, 1, 0, 2};

    private int direction;

    private Rect controlsUpRect;
    private Rect controlsDownRect;
    private Rect controlsLeftRect;
    private Rect controlsRightRect;
    private Rect controlsARect;
    private Rect controlsBRect;

    private User user;
    private List<Map> maps;
    private Cell userCell;
    private int currentMapId = 1;
    private Location nextCellLoc;
    int collisionResult;
    private List<Item> items;
    private List<Item> chest0;
    private List<Item> chest1;
    private String text;
    private boolean gotTheKey = false;
    private boolean gotTheInvocation = false;


    // When the we initialize (call new()) on gameView
    // This special constructor method runs
    public GameView(Context context, User u) {
        // The next line of code asks the
        // SurfaceView class to set up our object.
        // How kind.
        super(context);
        user = new User();
        user = u;
        user.setLocation(new Location(1, 1)); //Bob initial location
        userCell = new UserCell(user);

        // Initialize ourHolder and paint objects
        ourHolder = getHolder();
        paint = new Paint();
        initializeObjects();


        for (Cell cell : getMap(1).getCellArray()) {
            if (cell.getType().equals("ChestCell")) {
                if (((ChestCell) cell).getChest().getId() == 0) {
                    getChestItemList(0);
                }
                if (((ChestCell) cell).getChest().getId() == 1) {
                    getChestItemList(1);
                }
            }
        }

        userCell.setBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.link128x128));
        frameHeight = userCell.getBitmap().getHeight() / 4;
        frameWidth = userCell.getBitmap().getWidth() / 4;
        frameToDrawX.set(0,
                0,
                frameWidth,
                frameHeight);
    }

    private void initializeObjects() {
        maps = new ArrayList<>();
        nextCellLoc = new Location();
        items = new ArrayList<>();
        chest0 = new ArrayList();
        chest1 = new ArrayList();
        setMap(createMap(1));
        setMap(createMap(2));
        setMap(createMap(3));
        Log.v(TAG, "initializeObjects() done");
    }

    public Map getMap(int id) {
        return this.maps.get(id - 1);
    }

    public void setMap(Map map) {
        this.maps.add(map.getId() - 1, map);
    }

    public void setCell(int mapId, Cell cell) {
        this.getMap(mapId).setCell(cell);
    }

    public Cell getCell(int mapId, Location l) {
        return getMap(mapId).getCell(l);
    }

    public Cell getCellByCoords(int mapId, int x, int y) {
        return getMap(mapId).getCellByCoords(x, y);
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
        if (isMoving) {
            nextCellLoc = new Location();
            switch (direction) {
                case 3: { //TOP
                    nextCellLoc.setCoords(user.getLocation().getX() - 1, user.getLocation().getY());
                    collisionResult = isCollisionDetected(userCell.getRect(), getCell(currentMapId, nextCellLoc).getRect());
                    Cell nextCell = getCell(currentMapId, nextCellLoc);
                    switch (collisionResult) {
                        case 0: { //0=No collision,
                            bobYPosition = bobYPosition - (walkSpeedPerSecond / fps);
                            user.setLocation(locateUser(whereToDrawX.centerX(), whereToDrawX.centerY()));
                            break;
                        }
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3:
                            text = ((NPC) nextCell).getDialogue();
                            if (actionA) {
                                text = ((NPC) nextCell).getDialogue();
                            }
                            else if (actionB) {
                                if (!gotTheInvocation) {
                                    for (Item i: user.getItems()) {
                                        if(i.getName().equals("dark hole")) {
                                            currentMapId = 3;
                                            gotTheKey = true;
                                            textMode = false;
                                        }
                                    }
                                }
                            }
                            textMode = true;
                            actionA = false;
                            break;
                        case 4:
                            if (actionA) {
                                switch (((ChestCell) nextCell).getChest().getId()) {
                                    case 0 : {
                                        if (chest0.isEmpty()) {
                                            text = "The chest is empty";
                                            break;
                                        }
                                        else {
                                            user.getItems().addAll(chest0);
                                            UserItem userItem = new UserItem(user.getId(),5);
                                            setUserItem(userItem);
                                            chest0.clear();
                                            //getDeleteChestItems(0);
                                            text = "You've got a Dark Hole Invocation!";
                                            break;
                                        }
                                    }
                                    case 1 : {
                                        if (chest1.isEmpty()) {
                                            text = "The chest is empty";
                                            break;
                                        }
                                        else {
                                            user.getItems().addAll(chest1);
                                            UserItem userItem = new UserItem(user.getId(),10);
                                            setUserItem(userItem);
                                            chest1.clear();
                                            //getDeleteChestItems(1);
                                            text = "You've got the Master-Key!";
                                            break;
                                        }
                                    }
                                }

                                textMode = true;
                                actionA = false;
                            }
                            break;

                    }
                    break;
                }
                case 0: { //BOTT
                    nextCellLoc.setCoords(user.getLocation().getX() + 1, user.getLocation().getY());
                    int collisionResult = isCollisionDetected(userCell.getRect(), getCell(currentMapId, nextCellLoc).getRect());
                    Cell nextCell = getCell(currentMapId, nextCellLoc);
                    switch (collisionResult) {
                        case 0: {
                            bobYPosition = bobYPosition + (walkSpeedPerSecond / fps);
                            user.setLocation(locateUser(whereToDrawX.centerX(), whereToDrawX.centerY()));
                            break;
                        }
                        case 1: //Do nothing
                            break;
                        case 2:
                            if (actionA) {
                                for (Item i: user.getItems()) {
                                    if (i.getName().equals("key")) {
                                        currentMapId = ((Door) nextCell).getNextMap();
                                        gotTheKey = true;
                                    }
                                }

                                if (gotTheKey) {
                                    text =  "Unlocked";
                                    textMode = true;
                                }
                                else {
                                    text =  "Locked";
                                    textMode = true;
                                }

                            }
                            actionA = false;
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                    }
                    break;
                }
                case 1: {
                    nextCellLoc.setCoords(user.getLocation().getX(), user.getLocation().getY() - 1);
                    int collisionResult = isCollisionDetected(userCell.getRect(), getCell(currentMapId, nextCellLoc).getRect());
                    Cell nextCell = getCell(currentMapId, nextCellLoc);
                    switch (collisionResult) {
                        case 0: {
                            bobXPosition = bobXPosition - (walkSpeedPerSecond / fps);
                            user.setLocation(locateUser(whereToDrawX.centerX(), whereToDrawX.centerY()));
                            break;
                        }
                        case 1: //Do nothing
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                    }
                    break;
                }
                case 2: {
                    nextCellLoc.setCoords(user.getLocation().getX(), user.getLocation().getY() + 1);
                    int collisionResult = isCollisionDetected(userCell.getRect(), getCell(currentMapId, nextCellLoc).getRect());
                    Cell nextCell = getCell(currentMapId, nextCellLoc);switch (collisionResult) {
                        case 0: {
                            bobXPosition = bobXPosition + (walkSpeedPerSecond / fps);
                            user.setLocation(locateUser(whereToDrawX.centerX(), whereToDrawX.centerY()));
                            break;
                        }
                        case 1: //Do nothing
                            break;
                        case 2:
                            setMap(createMap(2));
                            currentMapId = 2;
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                    }
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
            drawMap(currentMapId, canvas);

            whereToDrawX.set((int) bobXPosition,
                    (int) bobYPosition,
                    (int) bobXPosition + frameWidth,
                    (int) bobYPosition + frameHeight);
            userCell.setRect(whereToDrawX);
            getCurrentFrame(direction);
            canvas.drawBitmap(userCell.getBitmap(),
                    frameToDrawX,
                    whereToDrawX, paint);
            DrawControls();
            if (textMode) drawDialogue(text);
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawMap(int mapId, Canvas canvas) {
        for (int i = 0; i < 144; i++) {
            canvas.drawBitmap(getMap(mapId).getCellArray()[i].getBitmap(), null, getMap(mapId).getCellArray()[i].getRect(), null);
        }
    }


    private void drawUserInterface(int screenWidth, int screenHeight) {
        Paint playScreenPaint = new Paint();
        playScreenPaint.setColor(Color.argb(255, 51, 153, 51));

        Paint screenBorderPaint = new Paint();
        screenBorderPaint.setColor(Color.argb(255, 96, 96, 96));

        // Draw the background color
        canvas.drawColor(Color.argb(255, 96, 96, 96));
        canvas.drawRect(0, 0, screenWidth, 1044, playScreenPaint);
        canvas.drawRect(0, 0, 35, 1044, screenBorderPaint);
        canvas.drawRect(0, 0, 1044, 35, screenBorderPaint);
        canvas.drawRect(1044, 0, 1080, 1044, screenBorderPaint);

        // Choose the brush color for drawing
        paint.setColor(Color.argb(255, 50, 34, 50));

        // Make the text a bit bigger
        paint.setTextSize(45);

        // Display the current fps on the screen
        canvas.drawText("FPS:" + fps, screenWidth / 3 + 100, 1500, paint);
        canvas.drawText("CellLoc:" + user.getLocation().getX() + "," + user.getLocation().getY(), screenWidth / 3 + 50, 1550, paint);
    }

    private void drawDialogue (String text) {
        Log.v(TAG, "drawDialogue starting...");
        Paint dialoguePaint = new Paint();
        dialoguePaint.setColor(Color.argb(255, 0, 0, 0));
        dialoguePaint.setTextSize(60);
        dialoguePaint.setTextAlign(Paint.Align.CENTER);
        Rect rect = new Rect (36, 1080, 1044, 1248);
        int width = rect.width();
        int numOfChars = dialoguePaint.breakText(text,true,width,null);
        int start = (text.length()-numOfChars)/2;
        canvas.drawText(text,start,start+numOfChars,rect.exactCenterX(),rect.exactCenterY(),dialoguePaint);
        Log.v(TAG, "drawDialogue done.");
    }

    private void DrawControls() {
        //Up Control
        controlsUpRect = new Rect(160, screenHeight / 2 + 200 + 200, 160 + 120, screenHeight / 2 + 200 + 120 + 200);
        Bitmap bitmapControlUp = BitmapFactory.decodeResource(this.getResources(), R.drawable.ctrl_up_arrow);
        canvas.drawBitmap(bitmapControlUp, null, controlsUpRect, null);
        //Left Control
        controlsLeftRect = new Rect(40, screenHeight / 2 + 280 + 200, 40 + 120, screenHeight / 2 + 280 + 120 + 200);
        Bitmap bitmapControlLeft = BitmapFactory.decodeResource(this.getResources(), R.drawable.ctrl_left_arrow);
        canvas.drawBitmap(bitmapControlLeft, null, controlsLeftRect, null);
        //Right Control
        controlsRightRect = new Rect(280, screenHeight / 2 + 280 + 200, 280 + 120, screenHeight / 2 + 280 + 120 + 200);
        Bitmap bitmapControlRight = BitmapFactory.decodeResource(this.getResources(), R.drawable.ctrl_right_arrow);
        canvas.drawBitmap(bitmapControlRight, null, controlsRightRect, null);
        //Down Control
        controlsDownRect = new Rect(160, screenHeight / 2 + 360 + 200, 160 + 120, screenHeight / 2 + 360 + 120 + 200);
        Bitmap bitmapControlDown = BitmapFactory.decodeResource(this.getResources(), R.drawable.ctrl_down_arrow);
        canvas.drawBitmap(bitmapControlDown, null, controlsDownRect, null);
        //A Control
        controlsARect = new Rect(720, screenHeight / 2 + 360 + 200, 720 + 130, screenHeight / 2 + 360 + 130 + 200);
        Bitmap bitmapControlA = BitmapFactory.decodeResource(this.getResources(), R.drawable.controls_a);
        canvas.drawBitmap(bitmapControlA, null, controlsARect, null);
        //B Control
        controlsBRect = new Rect(800, screenHeight / 2 + 200 + 200, 800 + 120, screenHeight / 2 + 200 + 120 + 200);
        Bitmap bitmapControlB = BitmapFactory.decodeResource(this.getResources(), R.drawable.controls_b);
        canvas.drawBitmap(bitmapControlB, null, controlsBRect, null);
    }

    public int isCollisionDetected(Rect rect1, Rect rect2) {

        final int[] COLLISION_WITH = {0, 1, 2, 3, 4}; //0=NoCollision
        if (Rect.intersects(rect1, rect2)) {
            Rect collisionBounds = getCollisionBounds(rect1, rect2);
            for (int i = collisionBounds.left; i < collisionBounds.right; i++) {
                for (int j = collisionBounds.top; j < collisionBounds.bottom; j++) {
                    switch (getCell(currentMapId, nextCellLoc).getType()) {
                        case "Wall":
                        case "Tree": {
                            Log.v(TAG, "isCollisionDetected: collision detected with a Tree");
                            return COLLISION_WITH[1];
                        }
                        case "Door": {
                            Log.v(TAG, "isCollisionDetected: collision detected with a Door");
                            return COLLISION_WITH[2];
                        }
                        case "NPC": {
                            Log.v(TAG, "isCollisionDetected: collision detected with a NPC");
                            return COLLISION_WITH[3];
                        }
                        case "ChestCell": {
                            Log.v(TAG, "isCollisionDetected: collision detected with a Chest");
                            return COLLISION_WITH[4];
                        }
                        case "Water": {
                            Log.v(TAG, "isCollisionDetected: collision detected with water");
                            return COLLISION_WITH[4];
                        }
                        case "Lava": {
                            Log.v(TAG, "isCollisionDetected: collision detected with lava");
                            return COLLISION_WITH[4];
                        }

                    }

                }
            }
        }

        return COLLISION_WITH[0];
    }


    private static Rect getCollisionBounds(Rect rect1, Rect rect2) {
        int left = (int) Math.max(rect1.left, rect2.left);
        int top = (int) Math.max(rect1.top, rect2.top);
        int right = (int) Math.min(rect1.right, rect2.right);
        int bottom = (int) Math.min(rect1.bottom, rect2.bottom);
        return new Rect(left, top, right, bottom);
    }


    public void getCurrentFrame(int direction) {

        long time = System.currentTimeMillis();
        if (isMoving) {// Only animate if bob is moving
            if (time > lastFrameChangeTime + frameLengthInMilliseconds) {
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
            case 3: {
                frameToDrawX.offsetTo(currentFrame * frameWidth, 3 * frameHeight);

                Rect controlsUpRectP = new Rect(160, screenHeight / 2 + 200 + 200, 160 + 120, screenHeight / 2 + 200 + 120 + 200);
                Bitmap bmpupp = BitmapFactory.decodeResource(getResources(), R.drawable.ctrl_up_arrow_pressed);
                canvas.drawBitmap(bmpupp, null, controlsUpRectP, null);

                break;
            }
            case 0: {
                frameToDrawX.offsetTo(currentFrame * frameWidth, 0 * frameHeight);

                Rect controlsDownRectP = new Rect(160, screenHeight / 2 + 360 + 200, 160 + 120, screenHeight / 2 + 360 + 120 + 200);
                Bitmap bmpdownp = BitmapFactory.decodeResource(getResources(), R.drawable.ctrl_down_arrow_pressed);
                canvas.drawBitmap(bmpdownp, null, controlsDownRectP, null);

                break;
            }
            case 2: {
                frameToDrawX.offsetTo(currentFrame * frameWidth, 2 * frameHeight);

                Rect controlsRightRectP = new Rect(280, screenHeight / 2 + 280 + 200, 280 + 120, screenHeight / 2 + 280 + 120 + 200);
                Bitmap bmprightp = BitmapFactory.decodeResource(getResources(), R.drawable.ctrl_right_arrow_pressed);
                canvas.drawBitmap(bmprightp, null, controlsRightRectP, null);

                break;
            }
            case 1: {
                frameToDrawX.offsetTo(currentFrame * frameWidth, 1 * frameHeight);

                Rect controlsLeftRectP = new Rect(40, screenHeight / 2 + 280 + 200, 40 + 120, screenHeight / 2 + 280 + 120 + 200);
                Bitmap bmpleftp = BitmapFactory.decodeResource(getResources(), R.drawable.ctrl_left_arrow_pressed);
                canvas.drawBitmap(bmpleftp, null, controlsLeftRectP, null);

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

                if (controlsUpRect.contains(x, y)) {
                    // Set isMoving so Bob is moved in the update method
                    textMode = false;
                    isMoving = true;
                    direction = 3;

                    break;
                } else if (controlsDownRect.contains(x, y)) {
                    // Set isMoving so Bob is moved in the update method
                    textMode = false;
                    isMoving = true;
                    direction = 0;
                    break;
                } else if (controlsLeftRect.contains(x, y)) {
                    // Set isMoving so Bob is moved in the update method
                    textMode = false;
                    isMoving = true;
                    direction = 1;
                    break;
                } else if (controlsRightRect.contains(x, y)) {
                    // Set isMoving so Bob is moved in the update method
                    textMode = false;
                    isMoving = true;
                    direction = 2;
                    break;
                } else if (controlsARect.contains(x, y)) {
                    // Set isMoving so Bob is moved in the update method
                    textMode = false;
                    actionA = true;
                    break;
                } else if (controlsBRect.contains(x, y)) {
                    // Set isMoving so Bob is moved in the update method
                    textMode = false;
                    actionB = true;
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

    public Map createMap(int mapId) {
        //logger.info("loadMap: Loading map...");
        Log.v(TAG, "createMap: map with id: "+mapId+" is beeing created...");
        BufferedReader br = null;
        try {
            StringBuilder s = new StringBuilder();
            s.append("map").append(mapId).append(".txt");
            ObjectMapper mapper = new ObjectMapper();
            if(mapId == 1){
                InputStreamReader isr1 = new InputStreamReader(this.getResources().openRawResource(R.raw.map1));
                br = new BufferedReader(isr1,8192);
            }
            else if (mapId == 2) {
                InputStreamReader isr2 = new InputStreamReader(this.getResources().openRawResource(R.raw.map2));
                br = new BufferedReader(isr2,8192);
                }
            else if (mapId == 3) {
                InputStreamReader isr2 = new InputStreamReader(this.getResources().openRawResource(R.raw.map3));
                br = new BufferedReader(isr2,8192);
            }
            Cell cells[] = mapper.readValue(br, Cell[].class);
            for (Cell cell : cells) {
                if (cell.getClass().getSimpleName().equals("Door")) {
                    cell.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.dngn_closed_door84x84));
                } else if (cell.getClass().getSimpleName().equals("Wall")) {
                    cell.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stone_brick4_84x84));
                } else if (cell.getClass().getSimpleName().equals("Tree")) {
                    cell.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.tree1_red84x84));
                } else if (cell.getClass().getSimpleName().equals("Field")) {
                    cell.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.dirt_s84x84));
                } else if (cell.getClass().getSimpleName().equals("ChestCell")) {
                    cell.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.chest2_closed84x84));
                } else if (cell.getClass().getSimpleName().equals("NPC")) {
                    cell.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.angel_mace84x84));
                } else if (cell.getClass().getSimpleName().equals("Hole")) {
                    cell.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.dngn_trap_magical84x84));
                } else if (cell.getClass().getSimpleName().equals("Water")) {
                    cell.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.water));
                } else if (cell.getClass().getSimpleName().equals("Lava")) {
                    cell.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.lava0));
                } else if (cell.getClass().getSimpleName().equals("Floor")){
                    cell.setBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.floor));
                }
            }
            Map map = new Map(mapId, cells);
            Log.v(TAG, "createMap: map with id: "+mapId+" has been created.");

            return map;
        } catch (IOException ex) {
            Log.e(TAG, "createMap: map with id: "+mapId+" has produced Errors");

            return null;
        }
    }

    public void getDeleteChestItems(int chestId) {
        Log.v(TAG, "getDeleteChestItems: Deleting all items from chest with id: "+chestId+"...");
        Call<Boolean> call = ApiAdapter.getApiService("http://192.168.0.107:8080/talesofeetac/db/")
                .deleteChestItemsService(chestId);
        call.enqueue(new GetDeleteChestItemsCallback(chestId));
    }

    private class GetDeleteChestItemsCallback implements Callback<Boolean> {
        int chestId;

        public GetDeleteChestItemsCallback(int chestId) {
            this.chestId = chestId;
        }

        @Override
        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
            response.body();
            Log.v(TAG, "getDeleteChestItems: All items from chest with id: "+chestId+" has been deleted");
        }

        @Override
        public void onFailure(Call<Boolean> call, Throwable t) {
            Log.e(TAG, "getDeleteChestItems: we've got a failure deleting the items from chest with id: "+chestId);
        }
    }

    public void getChestItemList(int chestId) {
        Log.v(TAG, "getChestItemList: Retreiving all items from chest with id: "+chestId+"...");
        Call<List<Item>> call = ApiAdapter.getApiService("http://192.168.0.107:8080/talesofeetac/db/").getChestItemsService(chestId);
        call.enqueue(new GetChestItemListCallback(chestId));
    }

    private class GetChestItemListCallback implements Callback<List<Item>> {

        int chestId;

        private GetChestItemListCallback (int chestId) {
            this.chestId = chestId;
        }
        @Override
        public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {

            switch (chestId) {
                case 0 :
                {
                    chest0 = response.body();
                    break;
                }
                case 1 :
                {
                    chest1 = response.body();
                    break;
                }
            }
            Log.v(TAG, "getChestItemList: All items from chest with id: "+chestId+"...");
        }

        @Override
        public void onFailure(Call<List<Item>> call, Throwable t) {
            Log.e(TAG, "getChestItemList: we've got a failure retreiving the items from chest with id: "+chestId);
        }
    }

    public void setUserItem(UserItem userItem) {
        Log.v(TAG, "setUserItem: Adding userItem with id: "+userItem.getId()+" to the db...");
        Call<Boolean> call = ApiAdapter.getApiService("http://192.168.0.107:8080/talesofeetac/db/").setUserItemService(userItem);
        call.enqueue(new setUserItemCallback());
    }

    private class setUserItemCallback implements Callback<Boolean> {
        @Override
        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
            Boolean bool = response.body();
        }

        @Override
        public void onFailure(Call<Boolean> call, Throwable t) {

        }
    }





    private Location locateUser(int x, int y) {
        int xNex =-1;
        int yNex = -1;
        if(x < 121) {
            yNex = 0;
            if (y < 121) xNex = 0;
            else if (y>120 && y<205) xNex = 1;
            else if (y>204 && y<289) xNex = 2;
            else if (y>288 && y<373) xNex = 3;
            else if (y>372 && y<457) xNex = 4;
            else if (y>456 && y<541) xNex = 5;
            else if (y>540 && y<647) xNex = 6;
            else if (y>624 && y<709) xNex = 7;
            else if (y>708 && y<793) xNex = 8;
            else if (y>792 && y<877) xNex = 9;
            else if (y>876 && y<961) xNex = 10;
            else if (y>960 && y<1045) xNex = 11;
        }

        if(x>120 && x<205) {
            yNex = 1;
            if (y < 121) xNex = 0;
            else if (y>120 && y<205) xNex = 1;
            else if (y>204 && y<289) xNex = 2;
            else if (y>288 && y<373) xNex = 3;
            else if (y>372 && y<457) xNex = 4;
            else if (y>456 && y<541) xNex = 5;
            else if (y>540 && y<647) xNex = 6;
            else if (y>624 && y<709) xNex = 7;
            else if (y>708 && y<793) xNex = 8;
            else if (y>792 && y<877) xNex = 9;
            else if (y>876 && y<961) xNex = 10;
            else if (y>960 && y<1045) xNex = 11;
        }

        if(x>204 && x<289) {
            yNex = 2;
            if (y < 121) xNex = 0;
            else if (y>120 && y<205) xNex = 1;
            else if (y>204 && y<289) xNex = 2;
            else if (y>288 && y<373) xNex = 3;
            else if (y>372 && y<457) xNex = 4;
            else if (y>456 && y<541) xNex = 5;
            else if (y>540 && y<647) xNex = 6;
            else if (y>624 && y<709) xNex = 7;
            else if (y>708 && y<793) xNex = 8;
            else if (y>792 && y<877) xNex = 9;
            else if (y>876 && y<961) xNex = 10;
            else if (y>960 && y<1045) xNex = 11;
        }

        if(x>288 && x<373) {
            yNex = 3;
            if (y < 121) xNex = 0;
            else if (y>120 && y<205) xNex = 1;
            else if (y>204 && y<289) xNex = 2;
            else if (y>288 && y<373) xNex = 3;
            else if (y>372 && y<457) xNex = 4;
            else if (y>456 && y<541) xNex = 5;
            else if (y>540 && y<647) xNex = 6;
            else if (y>624 && y<709) xNex = 7;
            else if (y>708 && y<793) xNex = 8;
            else if (y>792 && y<877) xNex = 9;
            else if (y>876 && y<961) xNex = 10;
            else if (y>960 && y<1045) xNex = 11;
        }

        if(x>372 && x<457) {
            yNex = 4;
            if (y < 121) xNex = 0;
            else if (y>120 && y<205) xNex = 1;
            else if (y>204 && y<289) xNex = 2;
            else if (y>288 && y<373) xNex = 3;
            else if (y>372 && y<457) xNex = 4;
            else if (y>456 && y<541) xNex = 5;
            else if (y>540 && y<647) xNex = 6;
            else if (y>624 && y<709) xNex = 7;
            else if (y>708 && y<793) xNex = 8;
            else if (y>792 && y<877) xNex = 9;
            else if (y>876 && y<961) xNex = 10;
            else if (y>960 && y<1045) xNex = 11;
        }

        if(x>456 && x<541) {
            yNex = 5;
            if (y < 121) xNex = 0;
            else if (y>120 && y<205) xNex = 1;
            else if (y>204 && y<289) xNex = 2;
            else if (y>288 && y<373) xNex = 3;
            else if (y>372 && y<457) xNex = 4;
            else if (y>456 && y<541) xNex = 5;
            else if (y>540 && y<647) xNex = 6;
            else if (y>624 && y<709) xNex = 7;
            else if (y>708 && y<793) xNex = 8;
            else if (y>792 && y<877) xNex = 9;
            else if (y>876 && y<961) xNex = 10;
            else if (y>960 && y<1045) xNex = 11;
        }

        if(x>540 && x<625) {
            yNex =6;
            if (y < 121) xNex = 0;
            else if (y>120 && y<205) xNex = 1;
            else if (y>204 && y<289) xNex = 2;
            else if (y>288 && y<373) xNex = 3;
            else if (y>372 && y<457) xNex = 4;
            else if (y>456 && y<541) xNex = 5;
            else if (y>540 && y<647) xNex = 6;
            else if (y>624 && y<709) xNex = 7;
            else if (y>708 && y<793) xNex = 8;
            else if (y>792 && y<877) xNex = 9;
            else if (y>876 && y<961) xNex = 10;
            else if (y>960 && y<1045) xNex = 11;
        }

        if(x>624 && x<709) {
            yNex =7;
            if (y < 121) xNex = 0;
            else if (y>120 && y<205) xNex = 1;
            else if (y>204 && y<289) xNex = 2;
            else if (y>288 && y<373) xNex = 3;
            else if (y>372 && y<457) xNex = 4;
            else if (y>456 && y<541) xNex = 5;
            else if (y>540 && y<647) xNex = 6;
            else if (y>624 && y<709) xNex = 7;
            else if (y>708 && y<793) xNex = 8;
            else if (y>792 && y<877) xNex = 9;
            else if (y>876 && y<961) xNex = 10;
            else if (y>960 && y<1045) xNex = 11;
        }

        if(x>708 && x<793) {
            yNex = 8;
            if (y < 121) xNex = 0;
            else if (y>120 && y<205) xNex = 1;
            else if (y>204 && y<289) xNex = 2;
            else if (y>288 && y<373) xNex = 3;
            else if (y>372 && y<457) xNex = 4;
            else if (y>456 && y<541) xNex = 5;
            else if (y>540 && y<647) xNex = 6;
            else if (y>624 && y<709) xNex = 7;
            else if (y>708 && y<793) xNex = 8;
            else if (y>792 && y<877) xNex = 9;
            else if (y>876 && y<961) xNex = 10;
            else if (y>960 && y<1045) xNex = 11;
        }

        if(x>792 && x<877) {
            yNex = 9;
            if (y < 121) xNex = 0;
            else if (y>120 && y<205) xNex = 1;
            else if (y>204 && y<289) xNex = 2;
            else if (y>288 && y<373) xNex = 3;
            else if (y>372 && y<457) xNex = 4;
            else if (y>456 && y<541) xNex = 5;
            else if (y>540 && y<647) xNex = 6;
            else if (y>624 && y<709) xNex = 7;
            else if (y>708 && y<793) xNex = 8;
            else if (y>792 && y<877) xNex = 9;
            else if (y>876 && y<961) xNex = 10;
            else if (y>960 && y<1045) xNex = 11;
        }

        if(x>876 && x<961) {
            yNex = 10;
            if (y < 121) xNex = 0;
            else if (y>120 && y<205) xNex = 1;
            else if (y>204 && y<289) xNex = 2;
            else if (y>288 && y<373) xNex = 3;
            else if (y>372 && y<457) xNex = 4;
            else if (y>456 && y<541) xNex = 5;
            else if (y>540 && y<647) xNex = 6;
            else if (y>624 && y<709) xNex = 7;
            else if (y>708 && y<793) xNex = 8;
            else if (y>792 && y<877) xNex = 9;
            else if (y>876 && y<961) xNex = 10;
            else if (y>960 && y<1045) xNex = 11;
        }

        if(x>960 && x<1045) {
            yNex = 11;
            if (y < 121) xNex = 0;
            else if (y>120 && y<205) xNex = 1;
            else if (y>204 && y<289) xNex = 2;
            else if (y>288 && y<373) xNex = 3;
            else if (y>372 && y<457) xNex = 4;
            else if (y>456 && y<541) xNex = 5;
            else if (y>540 && y<647) xNex = 6;
            else if (y>624 && y<709) xNex = 7;
            else if (y>708 && y<793) xNex = 8;
            else if (y>792 && y<877) xNex = 9;
            else if (y>876 && y<961) xNex = 10;
            else if (y>960 && y<1045) xNex = 11;
        }

        return new Location(xNex,yNex);
    }

}