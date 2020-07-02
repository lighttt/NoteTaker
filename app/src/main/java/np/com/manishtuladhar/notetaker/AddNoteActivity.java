package np.com.manishtuladhar.notetaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import np.com.manishtuladhar.notetaker.data.NoteContract;

public class AddNoteActivity extends AppCompatActivity {

    private int mPriority;
    EditText etNoteDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        mPriority = 1;
        ((RadioButton) findViewById(R.id.radButton1)).setChecked(true);
        etNoteDescription = findViewById(R.id.etNoteDescription);
    }

    /**
     * When priority button is clicked and a new priority is set
     */
    public void onPrioritySelected(View view) {
        if (((RadioButton) findViewById(R.id.radButton1)).isChecked()) {
            mPriority = 1;
        } else if (((RadioButton) findViewById(R.id.radButton2)).isChecked()) {
            mPriority = 2;
        } else if (((RadioButton) findViewById(R.id.radButton3)).isChecked()) {
            mPriority = 3;
        }

    }

    /**
     *  Adding new note to the database
     *
     */
    public void onClickAddNote(View view) {
        String note = etNoteDescription.getText().toString();
        if (note.length() == 0) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        // put data to database :  note and priority
        contentValues.put(NoteContract.NoteEntry.COLUMN_DESCRIPTION, note);
        contentValues.put(NoteContract.NoteEntry.COLUMN_PRIORITY, mPriority);
        //insert the data using content resolver
        Uri uri = getContentResolver().insert(NoteContract.NoteEntry.CONTENT_URI, contentValues);
        if (uri != null)
        {
            Toast.makeText(this, "Inserted value" + uri.toString(), Toast.LENGTH_SHORT).show();
        }
        //after data is added close activity
        finish();
    }
}