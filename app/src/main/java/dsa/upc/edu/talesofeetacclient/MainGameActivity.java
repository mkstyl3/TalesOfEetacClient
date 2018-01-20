package dsa.upc.edu.talesofeetacclient;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import dsa.upc.edu.talesofeetacclient.Model.Main.User;

public class MainGameActivity extends Activity {
    // gameView will be the view of the game
    // It will also hold the logic of the game
    // and respond to screen touches as well
    GameView gameView;
    User user;
    MediaPlayer mySong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize gameView and set it as the view
        Intent intent = getIntent();
        user = intent.getParcelableExtra("profile");
        gameView = new GameView(this,user);
        setContentView(gameView);

        mySong=MediaPlayer.create(MainGameActivity.this,R.raw.plateau);

    }
    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();
        // Tell the gameView resume method to execute
        gameView.resume();

        mySong.setLooping(true);
        mySong.start();
    }
    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();
        // Tell the gameView pause method to execute
        gameView.pause();
    }
}
