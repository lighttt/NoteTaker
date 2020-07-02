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
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
        mAdapter = new NoteAdapter(this,null);
        mRecyclerView.setAdapter(mAdapter);

        //swipe function
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

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
    protected void onRestart() {
        super.onRestart();
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
                return null;
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

    }
}