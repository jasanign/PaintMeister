package gj.paintmeister;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * App: Paint Meister (Saved Files List)
 * Author: Gajjan Jasani
 * version: 11/21/2016
 */
public class Screen5_SavedFilesList extends ListActivity {

    /** ArrayList for holding file names */
    ArrayList<String> listItems = new ArrayList<>(5);

    /**
     * Called when the view is created
     * @param savedInstanceState saved Instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen5__saved_files_list);
        Log.i("Screen 5 test1", "we are at least loading this screen successfully");
        File dataDirectory = Environment.getDataDirectory(); // getting hold of data directory
        // Moving on to the paint meister specific directory
        File fileDir = new File(dataDirectory, "data/gj.paintmeister/files");
        // getting the list of everything in paint meister's file directory
        File[] allFiles = fileDir.listFiles();
        for(int i = 0; i < allFiles.length; i++){
            String fileName = allFiles[i].getName();
            int fileNameLength = fileName.length();
            //ignoring file/s if it is not a picture saved by the user
            if( fileNameLength > 3 && fileName.substring(fileNameLength-4).equals(".txt")){
                listItems.add(fileName);
            }
        }
        Log.i("screen 5 test2", "reached the right directory and check the files list properly ");
        // if the there is no file saved by user, making the empty sign textview visible
        if(listItems.isEmpty()){
            Log.i("Screen 5 test3a", "list items is null");
            TextView emptyText = (TextView)findViewById(R.id.empty);
            emptyText.setVisibility(View.VISIBLE);
        } else { // else putting all files in list view using the custom adapter
            Log.i("Screen 5 test3b", "list items is not null");
            String temp = "";
            for (String any : listItems){
                temp = temp+any+", ";
            }
            Log.i("List Items", temp);
            setListAdapter(new ArrayAdapter<>(this, R.layout.list_item_file, listItems));
            ListView lv = getListView();
            Collections.sort(listItems); // Sorting the files list for user convenience
            lv.setTextFilterEnabled(true);
        }
    }

    /**
     * list item click listeener method
     * @param l list view
     * @param selectedView selected custom view of the list view
     * @param position position of the selected custom view
     * @param id id
     */
    @Override
    public void onListItemClick(ListView l, View selectedView, int position, long id) {

        Intent i = new Intent(this, Screen3_Paint.class);
        // passing the name to the paint class so it can load the selected file
        i.putExtra("File Name", listItems.get(position));
        startActivity(i);
        finish();
    }


}
