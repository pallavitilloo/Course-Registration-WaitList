package info.tilloop1.courseregistration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import info.tilloop1.courseregistration.R;
import info.tilloop1.courseregistration.database.model.Student;
import androidx.recyclerview.widget.RecyclerView;

/************************************************************************************************
 * Class WaitingListAdapter to render the RecyclerView with defined layout and data set
 * Created by: Pallavi Tilloo
 * Date: 04/22/2021
 ************************************************************************************************/
public class WaitingListAdapter extends RecyclerView.Adapter<WaitingListAdapter.MyViewHolder> {

    // Private members of class
    private Context context;
    private List<Student> student;

    // Nested class
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView priority;

        // Constructor of MyViewHolder
        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            priority = view.findViewById(R.id.priority);
        }
    }

    // Constructor of WaitingListAdapter
    public WaitingListAdapter(Context context, List<Student> student) {
        this.context = context;
        this.student = student;
    }

    /*******************************************************************************************
     * Overridden method for onCreateViewHolder
     * @param parent
     * @param viewType
     * @return
     *****************************************************************************************/
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    /*****************************************************************************************
     * Overridden method for onBindViewHolder
     * @param holder
     * @param position
     *****************************************************************************************/
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Student item = student.get(position);
        holder.name.setText(item.getName());
        holder.priority.setText(item.getPriority());
    }

    /******************************************************************************************
     * Return the count of items in the Waiting list
     * @return
     */
    @Override
    public int getItemCount() {
        return student.size();
    }
}