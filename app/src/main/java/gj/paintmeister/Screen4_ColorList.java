package gj.paintmeister;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * App: Paint Meister (Color List)
 * Author: Gajjan Jasani
 * version: 11/19/2016
 */
public class Screen4_ColorList extends Activity implements OnItemClickListener {

    /** Shared preferences for saving and editing preferences in memory */
    SharedPreferences settings;
    /** Array for holding colors */
    String[] colors = {"Black", "Grey", "Pepermint", "Green", "Lime", "Yellow", "Orange",
            "Brown", "Red", "Pink", "Purple", "Blue", "Cyan"};
    /** Array for holding colors' hex values */
    int[] hexVals = {0x000000, 0x777777, 0x88DDAA, 0x22AA22, 0xAAFF55, 0xFFEE00,
            0xFF8800, 0x776622, 0xAA2200, 0xFF55DD, 0xAA55DD, 0x0000AA, 0x00BBDD};
    /** Array for holding map markers */
    Drawable[] images = new Drawable[colors.length];
    /**The array adapter for the list view**/
    ArrayAdapter<String> adapter;

    /**
     * Called when the view is created
     * @param savedInstanceState saved Instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen4__color_list);

        ListView lv; // The list view which is displayed
        settings = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE );

        //Set the list view adapters for this view.
        lv = (ListView) findViewById(R.id.mylist);
        //this class will be the on item click listener for the list.
        lv.setOnItemClickListener(this);
        setMarkers();
        //Instantiate the custome addapter and pass to it the lyout yo br displayed
        adapter = new CustomListAdapter(this, hexVals, colors, images);
        lv.setAdapter(adapter);
    }

    /**
     * An interface method of the OnClickListener. Provides a class with the ability to listen to
     * item clicks of things like List views.
     * @param list The ListView Adapter that backs this list.
     * @param view The row's view within the list that was selected.
     * @param position The position of the selected row witin the list.
     * @param id The id of the row's layout.
     */
    //===========================================================================================
    @Override
    public void onItemClick(AdapterView<?> list, View view, int position, long id) {

        final SharedPreferences.Editor editor = settings.edit();
        int color = hexVals[position] + 0xFF000000;
        CustomView.mPaint.setColor(color);
        editor.putInt("color", color);
        editor.apply();
        finish();

    }//end if====================================================================================

    /**
     * Helper method for setting the markers from the resources folder
     */
    private void setMarkers(){

        int[] markers = {R.drawable.map_marker_blk, R.drawable.map_marker_gry,
                R.drawable.map_marker_pmt, R.drawable.map_marker_green, R.drawable.map_marker_lme,
                R.drawable.map_marker_yellow,R.drawable.map_marker_org, R.drawable.map_marker_brn,
                R.drawable.map_marker_red, R.drawable.map_marker_mgt, R.drawable.map_marker_purple,
                R.drawable.map_marker_blue,R.drawable.map_marker_cyn};

        for (int i = 0; i < images.length; i++){
            images[i] = this.getResources().getDrawable(markers[i]);
        }

    }
}
