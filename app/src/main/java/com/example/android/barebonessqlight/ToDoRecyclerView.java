package com.example.android.barebonessqlight;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.examlpe.android.barebonessqlight.data.ToDoContract;

/**
 * Created by peterpomlett on 04/12/2017.
 */

public class ToDoRecyclerView extends RecyclerView.Adapter<ToDoRecyclerView.ToDoViewHolder> {

    private final CheckBockListener itemClickListener;
    private Cursor mCursor;
    private Context mContext;

   public interface CheckBockListener{
         void checkBoxChanged(Long id, int done) ;
    }

    public ToDoRecyclerView(CheckBockListener itemClickListener, Context context, Cursor cursor) {
        this.itemClickListener = itemClickListener;
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public ToDoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, parent, false);
        return new ToDoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ToDoViewHolder holder, final int position) {
        if (!mCursor.moveToPosition(position))
            return;
        // Update the view holder with the information needed to display
        final String title = mCursor.getString(mCursor.getColumnIndex(ToDoContract.ToDoEntry.COLUMN_TITLE));

        int checkMark = mCursor.getInt(mCursor.getColumnIndex(ToDoContract.ToDoEntry.COLUMN_CHECK_MARK));
        // COMPLETED (6) Retrieve the id from the cursor and
        final long id = mCursor.getLong(mCursor.getColumnIndex(ToDoContract.ToDoEntry._ID));
        holder.done.setTag(id);
        final int newValue;
        if(checkMark > 0){
             newValue =0;
           holder.done.setChecked(true );
        }
else{ newValue = 1;
            holder.done.setChecked(false );
        }
        holder.done.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(final View view) {
                                               System.out.println("row at" + position + view.getTag());
                                               long itenId = (long) view.getTag();
                                               itemClickListener.checkBoxChanged( itenId, newValue);
                                             }});
        holder.itemView.setTag(id);
        holder.toDoTextView.setText(title);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }}

    public class ToDoViewHolder extends RecyclerView.ViewHolder {
        TextView toDoTextView;
         CheckBox done;
        public ToDoViewHolder(View itemView) {
            super(itemView);
            toDoTextView = (TextView) itemView.findViewById(R.id.title_textView);
             done = (CheckBox)itemView.findViewById(R.id.checkBox_done);
        }
    }
}