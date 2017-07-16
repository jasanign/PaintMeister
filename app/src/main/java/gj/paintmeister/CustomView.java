package gj.paintmeister;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * A custom view to capture onClickEvents and draw circles
 * where clicked.
 * @author Gajjan Jasani
 * @version 11/28/2016
 *
 */
public class CustomView  extends View {

    /** Field for holding context of the calling activity */
    private Context mContext;
    /** Field for holding current path of the view */
    private Path mPath;
    /** Field for holding Paint view of canvas */
    protected static Paint mPaint;
    /** Field for holding instance of path wrapper class */
    private Stroke tempStroke;
    /** ArrayList for holding all the wrapped paths */
    private ArrayList<Stroke> strokes = new ArrayList<>(25);
    /** Shared preferences for saving and editing preferences in memory */
    SharedPreferences settings;
    /** Field for holding a string form of stroke to load in picture */
    static String[] currentStrokeStr;

    /**
     * Constructor of the CustomView class
     * Called when the custom view is initialized
     * @param context context
     */
    //======================================================================= START - CONSTRUCTOR
    public CustomView(Context context, AttributeSet attrs) {
        super(context,attrs);

        mContext = context;
        settings = context.getSharedPreferences("MyPreferences",Context.MODE_PRIVATE );
//        color = settings.getInt("color", 0xFF5900ab);
//        String hex = "#"+color;
//        Log.i("current hex color", hex);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);

    }//========================================================================== END - CONSTRUCTOR

    /**
     * Method to set the size of the brush at runtime
     * @param size
     *///================================================================ START - SET BRUSH WIDTH()
    protected void setBrushWidth(float size){
        mPaint.setStrokeWidth(size);
    }
    //===================================================================== END - SET BRUSH WIDTH()

    /**
     * Draw the canvas items
     */
    //=========================================================================== START - ON DRAW()
    @Override
    protected void onDraw(Canvas canvas) {
        if(strokes != null){
            for (Stroke any : strokes){

                mPaint.setColor(any.color);
                mPaint.setStrokeWidth(any.bWidth);
                canvas.drawPath(any.path, mPaint);
            }
        }
    }//============================================================================ END - ON DRAW()

    /**
     * When the view is touched.
     *///================================================================= START - ON EVENT TOUCH()
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                performTouchEvent(event);
        }
        return true;
    }//===================================================================== END - ON EVENT TOUCH()

    /**
     * Perform each touch event.
     *///============================================================ START - PERFORM TOUCH EVENT()
    public boolean performTouchEvent(MotionEvent event) {

//        Log.v("TouchDemo","Touch");

        //Define drawable attributes.
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath = new Path();
                tempStroke = new Stroke();
                tempStroke.points.add(x);
                tempStroke.points.add(y);
                mPath.moveTo(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                tempStroke.points.add(x);
                tempStroke.points.add(y);
                mPath.lineTo(x,y);
                break;
            case MotionEvent.ACTION_UP:
                tempStroke.points.add(x);
                tempStroke.points.add(y);
                mPath.lineTo(x,y);
                tempStroke.path = mPath;
                // getting color from the memory
                tempStroke.color = settings.getInt("color", 0xFF5900ab);
                // getting brush size from the memory
                tempStroke.bWidth = settings.getFloat("brushWidth", 0.0f);
                strokes.add(tempStroke);
                break;
        }//end switch

        invalidate();

        return true;

    }//================================================================ END - PERFORM TOUCH EVENT()

    /**
     * Saves the text from the text view to the internal file storage of
     * the android app using  file output stream
     * @param fileName name of the new file
     */
    //============================================================================== START - SAVE()
    protected void save(String fileName){

        if(fileName.equals(".txt")){
            Toast.makeText(mContext, "Please enter a file name", Toast.LENGTH_LONG).show();
            return;
        }
        //Create a file if its not already on disk
        File file = new File(mContext.getFilesDir(), fileName);
        //get the picture in string form
        String string = picToString();

        FileOutputStream outputStream;//declare FOS

        try{  //to do this
            outputStream = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
            Toast.makeText(mContext, "Saved", Toast.LENGTH_LONG).show();
        }
        catch(FileNotFoundException fnfe){
            Toast.makeText(mContext, "Error saving file", Toast.LENGTH_LONG).show();
            fnfe.printStackTrace();
        }
        catch(IOException ioe){
            Toast.makeText(mContext, "Error saving file", Toast.LENGTH_LONG).show();
            ioe.printStackTrace();
        }
        catch (Exception e) {//else if failed trying do this
            Toast.makeText(mContext, "Error saving file", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }//=============================================================================== END - SAVE()

    /**
     * Helper method for save
     * This method converts the drawn picture to a string
     */
    //===================================================================== START - PIC TO STRING()
    private String picToString(){

        String pic = "";
        StringBuilder sb = new StringBuilder(pic);
        for(Stroke tempStroke : strokes){
            sb.append(tempStroke.toString());
            sb.append("\n");
        }
        pic = sb.toString();
        Log.i("All the strokes: \n", pic);
        return pic;
    }//====================================================================== END - PIC TO STRING()

    /**
     * Loads a picture from the file saved on internal memory
     * @param fileName name of the file located in internal memory
     */
    //============================================================================== START - LOAD()
    void load(String fileName){

        //Create a file if its not already on disk
        File file = new File(mContext.getFilesDir(), fileName);
        //Read text from file
        StringBuilder text = new StringBuilder();

        //Needs lots of try and catch blocks because so much can go wron
        try{

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }//end while
            br.close();//Close the buffer
        }//end try
        catch (FileNotFoundException e){//If file not found on disk here.
            Toast.makeText(mContext, "There was no data to load", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        catch (IOException e){//If io Exception here
            Toast.makeText(mContext, "Error loading file", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }//end catch

        //Set the data from the file content and convert it to a String
        String data = new String(text);
        //Safety first
        if (data.length() > 0) {
            //put the loaded data into the text view
            reDrawPicture(data);
            Log.v(fileName+" content\n", data);
            //Toast.makeText(mContext, data, Toast.LENGTH_LONG).show();
            Toast.makeText(mContext, "Picture Loaded: "+fileName, Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(mContext, "There is no data to display", Toast.LENGTH_LONG).show();

    }//=============================================================================== END - LOAD()

    /**
     * Helper method for load
     * Once the file is read from the memory this method takes over and draws the
     * picture on paint screen
     * @param data picture instructions
     */
    private void reDrawPicture(String data){

        //strokes.clear();

        String[] picData = data.split("\n");
        //mPath = new Path();

        for (String thisStrokeStr:picData ){
            tempStroke = new Stroke();
            currentStrokeStr = thisStrokeStr.split(",");

            if(currentStrokeStr.length < 4 ){
                continue;
            }
            move();
            for (int i = 4; i < currentStrokeStr.length - 3; i=i+2) {
                line(i);
            }

            float x = Float.valueOf(currentStrokeStr[currentStrokeStr.length - 2]);
            float y = Float.valueOf(currentStrokeStr[currentStrokeStr.length - 1]);
            mPath.lineTo(x,y);
            tempStroke.color = Integer.parseInt(currentStrokeStr[0].substring(3), 16)+0xff000000;
            tempStroke.bWidth = Float.valueOf(currentStrokeStr[1]);
            tempStroke.path = mPath;
            strokes.add(tempStroke);
            Log.i("invalidate 3", "Yes!!! Finally, it is being called :)");
            this.invalidate();
        }//================================================================ END - RE DRAW PICTURE()

    }

    /**
     * Helper method for reDrawPicture()
     * @return true when called
     */
    private boolean move(){

        mPath = new Path();
        float x1 = Float.valueOf(currentStrokeStr[2]);
        float y1 = Float.valueOf(currentStrokeStr[3]);
        mPath.moveTo(x1, y1);
        Log.i("invalidate 1", "No big deal");
        this.invalidate();
        return true;
    }

    /**
     * Helper Helper method for reDrawPicture()
     * @param i index for the current point in path
     * @return true when called
     */
    private boolean line(int i){

        float x = Float.valueOf(currentStrokeStr[i]);
        float y = Float.valueOf(currentStrokeStr[i+1]);
        mPath.lineTo(x,y);
        Log.i("invalidate 2", "Good Job!!!");
        this.invalidate();
        return true;
    }

    /**
     * Inner class: Stroke
     * This is the wrapper class for each path being drawn on the paint screen by the user
     * @author Gajjan Jasani
     * @version 11/20/2016
     */
    //######################################################################## START - STROKE CLASS
    private class Stroke{

        int color = 0;
        float bWidth = 0.0f;
        Path path = new Path();
        ArrayList<Float> points = new ArrayList<>(50);

        @Override
        public String toString(){
            String hex = "#"+String.format("%06X",color);
            String temp = hex +","+ bWidth;
            StringBuilder sb = new StringBuilder(temp);
            for(Float any:points){
                sb.append(",");
                sb.append(any);
            }
            Log.i("Stroke", sb.toString());
            return sb.toString();
        }

    }
    //########################################################################## END - STROKE CLASS


//++++++++++++++ Attempt` to handle orientation changes +++++++++++++++++++++++++++++++++

//    @Override
//    public Parcelable onSaveInstanceState(){
//
//        Log.i("Method started", "On Saved Instance - GJ");
//        System.out.println("save instance");
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(EXTRA_STATE, super.onSaveInstanceState());
//        bundle.putParcelableArrayList(EXTRA_EVENT_LIST, eventList);
//
//        return bundle;
//    }
//
//    @Override
//    public void onRestoreInstanceState(Parcelable state)
//    {
//        Screen3_Paint.colorCounter = 0;
//        if (state instanceof Bundle){
//
//            Log.i("Method started", "On Restore Instance");
//            Bundle bundle = (Bundle) state;
//            super.onRestoreInstanceState(bundle.getParcelable(EXTRA_STATE));
//            ArrayList<MotionEvent> eventList2 = bundle.getParcelableArrayList(EXTRA_EVENT_LIST);
//            if (eventList2 == null) {
//                eventList2 = new ArrayList<MotionEvent>(250);
//            }
//            for (MotionEvent event : eventList2) {
//                performTouchEvent(event);
//            }
//            return;
//        }
//        super.onRestoreInstanceState(state);
//    }

}//#######################################################
