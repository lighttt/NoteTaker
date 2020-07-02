package np.com.manishtuladhar.notetaker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NoteContentProvider extends ContentProvider {

    // ========================== URI ==================================

    //uri : directory for a single item and notes
    public static final  int NOTES = 100;
    public static final  int NOTE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    /**
     * Helps to find the associated Uri and checks if it exists or not
     *
     */
    public static UriMatcher buildUriMatcher()
    {
        //initialize
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //adding two uri
        uriMatcher.addURI(NoteContract.AUTHORITY,NoteContract.PATH_NOTES,NOTES);
        uriMatcher.addURI(NoteContract.AUTHORITY,NoteContract.PATH_NOTES + "/#",NOTE_WITH_ID);
        return  uriMatcher;
    }



    // ========================== CONTENT PROVIDER ==================================

    // database helper
    private NoteDBHelper noteDBHelper;

    @Override
    public boolean onCreate() {
        noteDBHelper = new NoteDBHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        //database access
        final SQLiteDatabase db = noteDBHelper.getWritableDatabase();
        //match ansuar hamile curosr ra data pathauchua
        int match = sUriMatcher.match(uri);
        //uri return garne
        Uri returnUri;
        switch (match)
        {
            case NOTES:
                long id = db.insert(NoteContract.NoteEntry.TABLE_NAME,null,contentValues);
                if(id>0){
                    returnUri = ContentUris.withAppendedId(NoteContract.NoteEntry.CONTENT_URI,id);
                }
                else{
                    throw new SQLException("Failed to insert new row into "+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        //notifying content resolver about new data insertion
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
