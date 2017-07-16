package gj.paintmeister;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * App: Paint Meister (Custom Adapter for Color List)
 * Author: Gajjan Jasani
 * version: 11/21/2016
 */
class CustomListAdapter extends ArrayAdapter<String>{

    /** Array to hold collors */
    private String[] colors;
    /** Array to hold hex values of all the colors */
    private int[] hexVals;
    /** Array to hold all the map markers */
    private Drawable[] images;
    /** field for holding Context of the color list activity */
    private Context ctx;

    /**
     * Construvtor of the custom list adapter class
     * @param context context of calling activity
     * @param hexValues hex velus provided by calling activity
     * @param colorNames color names provided by the calling activity
     * @param images map markers proviced by the calling activity
     */
    CustomListAdapter(Context context, int[] hexValues,
                             String[] colorNames, Drawable[] images) {
        super(context, R.layout.list_item_a, colorNames);
        this.colors = colorNames;
        this.hexVals = hexValues;
        this.images = images;
        ctx = context;
    }

    /**
     * Method to set up the custom components of the custom adapter for the list view
     * @param position position of the custom view
     * @param listItemView view of the custom view
     * @param parent parent of the custom views (calling list view)
     * @return the custom built view for the list view
     */
    @Override
    public View getView(int position, View listItemView, ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);

        if(position %2 == 0){
            listItemView = inflater.inflate(R.layout.list_item_a, parent, false);
        } else {
            listItemView = inflater.inflate(R.layout.list_item_b, parent, false);
        }

        TextView color_text = (TextView) listItemView.findViewById(R.id.color_text);
        TextView hex_text = (TextView) listItemView.findViewById(R.id.hex_text);
        ImageView icon = (ImageView) listItemView.findViewById(R.id.imageView);
        color_text.setText(colors[position]);
        String hex = "#"+String.format("%06X",hexVals[position]);
        hex_text.setText(hex);
        icon.setImageDrawable(images[position]);
        return listItemView;
    }




}