package com.example.android.barebonessqlight;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.examlpe.android.barebonessqlight.data.ToDoContract;
import com.examlpe.android.barebonessqlight.data.ToDoDbHelper;

public class MainActivity extends AppCompatActivity implements ToDoRecyclerView.CheckBockListener {

    private SQLiteDatabase mDb;
    ToDoRecyclerView viewAdapter;
    RecyclerView toDoRecycleView;
    public String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toDoRecycleView = findViewById(R.id.ma_rv_toDo);
        toDoRecycleView.setLayoutManager(new LinearLayoutManager(this));
        toDoRecycleView.setHasFixedSize(true);
        ToDoDbHelper dbHelper = new ToDoDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        Cursor cursor = getToDos();
        viewAdapter = new ToDoRecyclerView(this, this, cursor);
        toDoRecycleView.setAdapter(viewAdapter);
        //swipe to delete callback
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //get the id off of the tag
                long id = (long) viewHolder.itemView.getTag();
                //call the remove method
                removeGuest(id);
                //Update UI
                viewAdapter.swapCursor(getToDos());
            }

        }).attachToRecyclerView(toDoRecycleView);
    }

    //Menu override methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_new_todo) {
            Intent startNewToDoActivity = new Intent(this, NewToDoActivity.class);
            startActivityForResult(startNewToDoActivity, 2);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Cursor getToDos() {
        return mDb.query(
                ToDoContract.ToDoEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ToDoContract.ToDoEntry._ID
        );
    }

    //Called when dismissing the NewToDo Activity to pass in the to do title
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check that it is the SecondActivity with an OK result
        if (resultCode == RESULT_OK) {
            // get String data from Intent
            String returnString = data.getStringExtra("keyName");
            System.out.println(returnString);
            if (returnString.length() == 0) {
                return;
            }
            // Add new To do to the data base
            addNewToDo(returnString);
            // Update UI
            viewAdapter.swapCursor(getToDos());
        }
    }

    //Data base update methods
    private void addNewToDo(String title) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ToDoContract.ToDoEntry.COLUMN_TITLE, title);
        contentValues.put(ToDoContract.ToDoEntry.COLUMN_CHECK_MARK, 0);
        mDb.insert(ToDoContract.ToDoEntry.TABLE_NAME, null, contentValues);
    }

    private void removeGuest(long id) {
        mDb.delete(ToDoContract.ToDoEntry.TABLE_NAME, ToDoContract.ToDoEntry._ID + "=" + id, null);
    }

    // CheckBoxListener interface method
    @Override
    public void checkBoxChanged(Long id, int done) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ToDoContract.ToDoEntry.COLUMN_CHECK_MARK, done);
        mDb.update(ToDoContract.ToDoEntry.TABLE_NAME, contentValues, ToDoContract.ToDoEntry._ID + "=" + id, null);
        viewAdapter.swapCursor(getToDos());
    }

}
