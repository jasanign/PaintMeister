package gj.paintmeister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * App: Paint Meister (Splash Screen)
 * Author: Gajjan Jasani
 * version: 11/17/2016
 */
public class Screen1_Splash extends Activity {

    /** Handler to apply delay */
    Handler handler;

    /**
     * Method: onCreate
     * This method displays the splash screen when the app is opened
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen1__splash);
    }

    /**
     * Method: onStart
     * This method loads Screen2 after 3 seconds of splash screen
     */
    protected void onStart(){
        super.onStart();

        /** Constant to define 5 seconds */
        final int DELAY = 5000;
        handler = new Handler();
        handler.postDelayed(runner, DELAY);
    }

    /**
     * Thread to make the delay possible before loading next screen
     */
    private final  Runnable runner = new Runnable() {
        @Override
        public void run() {
            nextScreen();
        }
    };

    /**
     * Helper method to load next screen
     */
    private void nextScreen(){

        Intent i = new Intent(this, gj.paintmeister.Screen2_MainMenu.class);
        this.startActivity(i);
    }
}
