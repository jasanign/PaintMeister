package gj.paintmeister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * App: Paint Meister (Screen 2 Main Menu)
 * This screen provides users opportunity to choose if they want to start a new painting
 * or load previously painted/saved images
 * Author: Gajjan Jasani
 * version: 11/17/2016
 */
public class Screen2_MainMenu extends Activity implements View.OnClickListener{

    /** Buttons to handle user input */
    Button btn1;
    Button btn2;

    /**
     * Method: onCreate
     * This method sets up the screen 2 on the device
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2__main_menu);
    }

    /**
     * Method: onStart
     * makes all the buttons clickable to get the user input
     */
    @Override
    protected void onStart(){
        super.onStart();
        // Connecting the xml screen 2's button elements with code
        btn1 = (Button) findViewById(R.id.scr2_btn1);
        btn2 = (Button) findViewById(R.id.scr2_btn2);
        //btn3 = (Button) findViewById(R.id.scr2_btn3);
        //making those buttons to listen to user click (touch)
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    /**
     * Method: onClick
     * This method loads screen associated to the button clicked by the user
     * @param v - the clickable view (button) element that has been clicked/touched
     */
    @Override
    public void onClick(View v) {

        // casting the clicked view element to a button because we
        // know it is a button
        Button b = (Button) v;

        if(b.getId() == R.id.scr2_btn1){
            Intent selected_screen = new Intent(this,Screen3_Paint.class);
            this.startActivity(selected_screen); //starting the user selected activity
        }else if(b.getId() == R.id.scr2_btn2){
            Intent selected_screen = new Intent(this, Screen5_SavedFilesList.class);
            this.startActivity(selected_screen); //starting the user selected activity
        }
    }
}
