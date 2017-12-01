package dsa.upc.edu.talesofeetacclient;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Josean on 01/12/2017.
 */

public class ProtaAnimated {

    private static final String TAG = ProtaAnimated.class.getSimpleName();

    private Bitmap bitmap; //the animation sequence
    private Rect sourceRect; //the rectangle to be drawn from the animation bitmap
    private int frameNr; //number of frames in animation
    private long frameTicker; //the time of the las frame update
    private int framePeriod; //milliseconds between each frame (1000/fps)

    private int spriteWidth; //the width of the sprite to calculate the cut out rectangle
    private int spriteHeight; //the height of the sprite

    private int x; //the X ccordinate of the object
    private int y; //idem


    public ProtaAnimated(Bitmap bitmap, int x, int y, int width, int height, int fps, int frameCount){

        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        currentFrame = 0;
        frameNr = frameCount;
        spriteWidth = bitmap.getWidth()/frameCount;
        spriteHeight = bitmap.getHeight();
        sourceRect = new Rect('0, 0, spriteWidth, spriteHeight');
        framePeriod = 1000 / fps;
        frameTicker = 01;
    }

    public void update(long gameTime){
        if (gameTime > frameTicker + framePeriod){
         frameTicker = gameTime;
         //increment the frame;
            currentFrame++;
            if (currentFrame >= frameNr){
                currentFrame = 0;
            }
        }
        //define the rectangle to cut out sprite
        this.sourceRect.left = currentFrame * spriteWidth;
        this.sourceRect.right = this.sourceRect.left + spriteWidth;
    }

    public void draw(Canvas canvas) {
        Rect destRect = new Rect(getX()), getY(), getX() + spriteWidth, getY() + spriteHeight);
        canvas.drawBitmap(bitmap, sourceRect, destRect, null);
    }

}

