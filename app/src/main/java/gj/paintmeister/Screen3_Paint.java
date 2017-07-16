package gj.paintmeister;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.content.SharedPreferences;

/**
 * App: Paint Meister (Screen 3 Paint)
 * This screen provides users opportunity to paint a new picture and save it, or load a saved
 * picture, or clean the current picture if they want to start over
 * Author: Gajjan Jasani
 * version: 11/19/2016
 */

public class Screen3_Paint extends Activity {

    /** Field declaring the CustomView class */
    CustomView touchArea;
    /** Field for holding brush width of a stroke */
    private float strokeSize = 0.0f;

    //===========================================================
    /**
     * Called when the view is created
     * @param savedInstanceState saved Instance
     */
    //===========================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen3__paint);
        touchArea = (CustomView) this.findViewById(R.id.view1);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            // extracting the string from the extras
            String fileName = extras.getString("File Name");
            if(fileName != null){ // if a file name is present then that means this activity is
                                  // called from the saved list activity
                touchArea.load(fileName); // loading the user selected file
            }
        }

        SeekBar seekBar; // seekBar is our slider for handling brush width

        SharedPreferences settings =   getSharedPreferences("MyPreferences",Context.MODE_PRIVATE );
        final SharedPreferences.Editor editor = settings.edit();

        seekBar = (SeekBar) findViewById(R.id.slider);
        strokeSize = settings.getFloat("brushWidth", 0.0f); // getting brush size from memory
        seekBar.setProgress((int)strokeSize);
//        Log.i("Stroke Size", ""+strokeSize);
        touchArea.setBrushWidth(strokeSize); // Setting the brush size the first time from memory

        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                strokeSize = (float) progressValue;
                editor.putFloat("brushWidth",strokeSize); //saving the changed brush size to memory
                editor.apply();

                touchArea.setBrushWidth(strokeSize); // Setting brush size at runtime
//                Log.i("Stroke Size changed to", ""+strokeSize);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }//==========================================================

    /**
     * Generate the options menu.
     * @param menu menu
     * @return true when menu is loaded, false otherwise
     */
    //==============================================================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }//=============================================================================================

    /**
     * Respond to options selections.
     * @param item selected item
     * @return true whenever a menu item is touched
     */
    //==============================================================================================
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i;
        switch (item.getItemId()) {
            case R.id.action_opt1:
                i = new Intent(this, Screen4_ColorList.class);
                this.startActivity(i);
                return true;
            case R.id.action_opt2:
                i = new Intent(this, Screen5_SavedFilesList.class);
                this.startActivity(i);
                return true;
            case R.id.action_opt3:
                savePicture();
                return true;
            case R.id.action_opt4:
                recreate();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }//end onOptions Selected======================================================================

    /**
     * This method prompts user to enter a valid file name and calls the save() of the
     * CustomView class and passes the user provided file name to it
     */
    private void savePicture(){
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        // Found this great article explaining why "null" is acceptable here
        // https://possiblemobile.com/2013/05/layout-inflation-as-intended/
        View promptsView = li.inflate(R.layout.file_name_prompt, null);
        final EditText userInput = (EditText) promptsView
                                        .findViewById(R.id.editTextDialogUserInput);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and give it to save() method of CustomView class
                                String fileName = userInput.getText().toString()+".txt";
                                fileName = fileName.trim();
                                //touchArea.save(fileName.replaceAll("^\\s+", "")+".txt");
                                touchArea.save(fileName);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
