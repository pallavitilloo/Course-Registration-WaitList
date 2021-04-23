package info.tilloop1.courseregistration.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**************************************************************************************************
 * The class RecycleTouchListener adds touch event to RecyclerView row.
 * Created By: Pallavi Tilloo
 * Date: 04/22/2021
 *************************************************************************************************/
public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

    private ClickListener clicklistener;
    private GestureDetector gestureDetector;

    /*********************************************************************************************
     * Constructor for the class
     *********************************************************************************************/
    public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener) {

        this.clicklistener = clicklistener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clicklistener != null) {
                    clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                }
            }
        });
    }

    /*********************************************************************************************
     * Method overridden from OnItemTouchListener
     * @param rv
     * @param e
     * @return
     *********************************************************************************************/
    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
            clicklistener.onClick(child, rv.getChildAdapterPosition(child));
        }

        return false;
    }

    /*********************************************************************************************
     * Override method onTouchEvent from RecyclerView class
     * @param rv
     * @param e
     *********************************************************************************************/
    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    /*********************************************************************************************
     * Override method from RecyclerView Class
     * @param disallowIntercept
     *********************************************************************************************/
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    /*********************************************************************************************
     * Interface for the ClickListener class
     */
    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }
}
