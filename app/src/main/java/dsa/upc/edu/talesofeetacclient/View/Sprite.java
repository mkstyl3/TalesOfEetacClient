package dsa.upc.edu.talesofeetacclient.View;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import dsa.upc.edu.talesofeetacclient.GameView;

/**
 * Created by mike on 24/12/17.
 */

public class Sprite {
    private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 4;
    private GameView gameView2;
    private Bitmap bmp;
    private int currentFrame = 0;
    private int width;
    private int height;
    private int x = 0;
    private int y = 0;
    // direction = 0 up, 1 left, 2 down, 3 right,
    // animation = 3 back, 1 left, 0 front, 2 right
    private static final int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };

    // animation = 3 back, 1 left, 0 front, 2 right
    
    public Sprite(GameView gameView, Bitmap bmp){
        this.gameView2=gameView;
        this.bmp=bmp;
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
    }

    public void onDraw(Canvas canvas) {
        int srcX = currentFrame * width;
        int srcY = 2 * height;
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        Rect dst = new Rect(x, y, x + width, y + height);
    }

    private int getAnimationRow(int direction) {
        return DIRECTION_TO_ANIMATION_MAP[direction];
    }


}
