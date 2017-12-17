package dsa.upc.edu.talesofeetacclient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class MainGameActivity extends Activity {
    /** Called when the activity is first created. */
    private static final String TAG = MainGameActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // set our MainGamePanel as the View
        setContentView(new MainGamePanel(this));
        Log.d(TAG, "View added");
    }

    //Only for log the the activity's lifecycle purposes
    @Override
    protected void onDestroy() {
        Log.d(TAG, "Destroying...");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Stopping...");
        super.onStop();
    }
}
