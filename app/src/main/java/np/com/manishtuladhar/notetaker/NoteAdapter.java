package np.com.manishtuladhar.notetaker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    //variables
    private Context mContext;
    private Cursor mCursor;


    public  NoteAdapter(Context context, Cursor cursor)
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
