package np.com.manishtuladhar.notetaker;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import np.com.manishtuladhar.notetaker.data.NoteContract;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    //variables
    private Context mContext;
    private Cursor mCursor;


    public NoteAdapter(Context context, Cursor cursor)
    {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.note_layout,parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        // index of columns
        int idIndex = mCursor.getColumnIndex(NoteContract.NoteEntry._ID);
        int descriptionIndex = mCursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_DESCRIPTION);
        int priorityIndex = mCursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_PRIORITY);

        mCursor.moveToPosition(position);

        //assigning values
        //tag
        final int id = mCursor.getInt(idIndex);
        holder.itemView.setTag(id);
        //description
        String description = mCursor.getString(descriptionIndex);
        holder.noteDescriptionTV.setText(description);
        //priority
        int priority = mCursor.getInt(priorityIndex);
        String priorityString = "" + priority;
        holder.priorityTV.setText(priorityString);
        //background color of priority
        GradientDrawable priorityCircle = (GradientDrawable) holder.priorityTV.getBackground();
        // set background
        int priorityColor = getPriorityColor(priority);
        priorityCircle.setColor(priorityColor);
    }

    /**
     * Helps to get the color for priority
     * @param priority : we get from database
     *
     */
    private  int getPriorityColor(int priority){
        int priorityColor = 0;
        switch (priority)
        {
            case 1: priorityColor = ContextCompat.getColor(mContext,R.color.materialRed);
            break;
            case 2: priorityColor = ContextCompat.getColor(mContext,R.color.materialOrange);
            break;
            case 3: priorityColor = ContextCompat.getColor(mContext,R.color.materialYellow);
            break;
            default:break;
        }
        return priorityColor;
    }

    /**
     * When data changes and re-query occurs
     * @param c
     * @return
     */
    public Cursor swapCursor(Cursor c){
        // new cursor and old cursor is same
        if(mCursor == c)
        {
            // do nth just return null
            return null;
        }

        Cursor temp = mCursor;
        this.mCursor = c; // new cursor

        // valid cursor
        if(c!=null)
        {
            this.notifyDataSetChanged();
        }
        return temp;
    }


    @Override
    public int getItemCount() {
        if(mCursor == null)
        {
            return 0;
        }
        return mCursor.getCount();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView noteDescriptionTV;
        TextView priorityTV;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteDescriptionTV = itemView.findViewById(R.id.taskDescription);
            priorityTV = itemView.findViewById(R.id.priorityTextView);
        }
    }
}
