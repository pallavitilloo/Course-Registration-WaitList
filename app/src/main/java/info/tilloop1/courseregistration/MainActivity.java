package info.tilloop1.courseregistration;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import info.tilloop1.courseregistration.database.DatabaseHelper;
import info.tilloop1.courseregistration.database.model.Student;
import info.tilloop1.courseregistration.utils.MyDividerItemDecoration;
import info.tilloop1.courseregistration.utils.RecyclerTouchListener;

public class MainActivity extends AppCompatActivity {
    private WaitingListAdapter mAdapter;
    private List<Student> student = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noNamesView;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get views from layout
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        noNamesView = findViewById(R.id.empty_items_view);

        // Create Database helper
        db = new DatabaseHelper(this);

        // Add all entries to the list from the database
        student.addAll(db.getAllEntries());

        // This is to show PLUS sign for user
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createItemDialog(null, -1);
            }
        });

        mAdapter = new WaitingListAdapter(this, student);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyItems();

        /*****************************************************************************************
         * On long press on RecyclerView item, open alert dialog with options to choose
         * Edit and Delete
         */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }

    /********************************************************************************************
     * Inserts new entry for student and priority into Database
     * @param name Name of the student
     */
    private void createItem(String name) {

        Student x = new Student();

        // updating note text
        x.setName(name);
        x.setPriority("");

        // Inserting student details in db and getting newly inserted id
        long id = db.insertEntry(x);

        // get the newly inserted entry from db
        Student n = db.getEntry(id);

        if (n != null) {
            // adding new entry to array list at 0 position
            student.add(0, n);
            // refreshing the list
            mAdapter.notifyDataSetChanged();
            toggleEmptyItems();
        }
    }

    /**********************************************************************************************
     * Code to set priority for student
     * @param name Name of the student
     * @param priority Priority of the student
     * @param position Position of data item in the list view
     */
    private void setPriority(String name, String priority, int position){
        Student x = student.get(position);

        x.setPriority(priority);

        // updating data in db
        db.updateEntry(x);

        // refreshing the list
        student.set(position, x);
        mAdapter.notifyItemChanged(position);

        toggleEmptyItems();
    }

    /********************************************************************************************
     * Updating item in db and in the list by its position
     * @param name: Name of the student
     * @param priority: Priority of the student
     * @param position: Position of the item in the list
     */
    private void updateItem(String name, String priority, int position) {
        Student x = student.get(position);
        // updating data
        x.setName(name);
        x.setPriority(priority);

        // updating data in db
        db.updateEntry(x);

        // refreshing the list
        student.set(position, x);
        mAdapter.notifyItemChanged(position);

        toggleEmptyItems();
    }

    /*********************************************************************************************
     * Deletes student from Database and removing the item from the list by its position
     * @param position: Position of the entry to be deleted
     */
    private void deleteItem(int position) {

        // deleting the entry from db
        db.deleteEntry(student.get(position));

        // removing the note from the list
        student.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyItems();
    }

    /*********************************************************************************************
     * Opens dialog with Edit - Delete options
     * @param position
     */
    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Set Priority", "Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    setPriorityDialog(student.get(position), position);
                } else if (which == 1){
                    editItemDialog(student.get(position), position);
                }
                else{
                    deleteItem(position);
                }
            }
        });
        builder.show();
    }

    /*********************************************************************************************
     * Shows alert dialog to create new item     *
     * @param item: The item to be created/updated
     * @param position: Position of the item in the list
     */
    private void createItemDialog(final Student item, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());

        // For create new item
        View view = layoutInflaterAndroid.inflate(R.layout.new_item_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputName = view.findViewById(R.id.name);

        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(getString(R.string.lbl_new_item_title));

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton( "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputName.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter name!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // create new entry
                createItem(inputName.getText().toString());
            }
        });
    }

    /********************************************************************************************
     * Sets priority for an item by showing dialog for setting priority
     * @param item Item whose priority is to be set
     * @param position Position of the item in the list view
     */
    private void setPriorityDialog(final Student item, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());

        // For create new item
        View view = layoutInflaterAndroid.inflate(R.layout.set_priority_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final TextView inputName = view.findViewById(R.id.name);
        final EditText inputPriority = view.findViewById(R.id.priority);

        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(getString(R.string.lbl_set_priority_title));

        if (item != null) {
            inputName.setText(item.getName());
            inputPriority.setText(item.getPriority());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(inputPriority.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter priority!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating the details
                if (item != null) {
                    // update entry by it's id
                    updateItem(inputName.getText().toString(), inputPriority.getText().toString(), position);
                }
            }
        });
    }

    /*********************************************************************************************
     * Show Edit Item dialog box
     * @param item
     * @param position
     */
    private void editItemDialog(final Student item, final int position){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());

        //Update item - both name and priority will be editable
        View view = layoutInflaterAndroid.inflate(R.layout.edit_item_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputName = view.findViewById(R.id.name);
        final EditText inputPriority = view.findViewById(R.id.priority);

        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(getString(R.string.lbl_edit_item_title));

        if (item != null) {
            inputName.setText(item.getName());
            if(!String.valueOf(item.getPriority()).isEmpty())
                inputPriority.setText(item.getPriority());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("update" , new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputName.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter name!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }
                if (TextUtils.isEmpty(inputPriority.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter priority!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating the details
                if (item != null) {
                    // update note by it's id
                    updateItem(inputName.getText().toString(), inputPriority.getText().toString(), position);
                }
            }
        });
    }

    /*********************************************************************************************
     * Toggling list and empty notes view
     */
    private void toggleEmptyItems() {

        // Check if notesList.size() > 0

        if (db.getNameCount() > 0) {
            noNamesView.setVisibility(View.GONE);
        } else {
            noNamesView.setVisibility(View.VISIBLE);
        }
    }
}