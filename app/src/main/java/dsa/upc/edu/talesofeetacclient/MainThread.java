package dsa.upc.edu.talesofeetacclient;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by mike on 22/11/17.
 */

public class MainThread extends Thread {
    private static final String TAG = MainThread.class.getSimpleName();
    // flag to hold game state
    private boolean running;
    private SurfaceHolder surfaceHolder;
    private MainGamePanel gamePanel;

    public MainThread(SurfaceHolder surfaceHolder, MainGamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @SuppressLint("WrongCall")
    @Override
    public void run() {
        Canvas canvas;
        Log.d(TAG, "Starting game loop");
        while(running) {
            canvas = null;
            // try locking the canvas for exclusive pixel editing on the surface
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    // update game state
                    // draws the canvas on the panel
                    this.gamePanel.onDraw(canvas);
                }
            }
            finally {
                // in case of an exception the surface is not left in
                // an inconsistent state
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
