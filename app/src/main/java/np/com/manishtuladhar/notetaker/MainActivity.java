package np.com.manishtuladhar.notetaker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import np.com.manishtuladhar.notetaker.data.NoteContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivity";

    RecyclerView mRecyclerView;
    NoteAdapter mAdapter;

    //loader
    private static final int NOTE_LOADER_ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //rv
        mRecyclerView = findViewById(R.id.recyclerViewTasks);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NoteAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        //swipe function
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int id = (int) viewHolder.itemView.getTag();

                //uri to delete
                String stringID = Integer.toString(id);
                Uri uri = NoteContract.NoteEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringID).build();

                //delete operation content provider
                getContentResolver().delete(uri,null,null);

                //restart and get new data
                getSupportLoaderManager().restartLoader(NOTE_LOADER_ID,null,MainActivity.this);
            }
        }).attachToRecyclerView(mRecyclerView);

        //floating to add new task
        FloatingActionButton fabButton = findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNoteIntent = new Intent(MainActivity.this,AddNoteActivity.class);
                startActivity(addNoteIntent);
            }
        });

        getSupportLoaderManager().initLoader(NOTE_LOADER_ID,null,this);
    }

    // =========================== ACTIVITY LIFECYCLE ======================

    @Override
    protected void onResume() {
        super.onResume();
        //restart or re accessed new data
        getSupportLoaderManager().restartLoader(NOTE_LOADER_ID,null,this);
    }

    // =========================== LOADERS ======================
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            //data hold
            Cursor mNoteData = null;

            @Override
            protected void onStartLoading() {
                if(mNoteData !=null)
                {
                    //cache old data direct passed
                    deliverResult(mNoteData);
                }
                else{
                    // new data loads
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public Cursor loadInBackground() {
                try{
                    return getContentResolver().query(NoteContract.NoteEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            NoteContract.NoteEntry.COLUMN_PRIORITY);
                }
                catch (Exception e)
                {
                    Log.e(TAG, "loadInBackground: failed to load data" );
                    return null;
                }

            }

            @Override
            public void deliverResult(@Nullable Cursor data) {
                mNoteData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // swap cursor displays new data
        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}