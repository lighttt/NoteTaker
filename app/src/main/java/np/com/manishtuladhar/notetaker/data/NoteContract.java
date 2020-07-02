package np.com.manishtuladhar.notetaker.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class NoteContract {

    // authority
    public static final String AUTHORITY = "np.com.manishtuladhar.notetaker";
    //base uri
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" +AUTHORITY);
    public static final String PATH_NOTES = "notes";


    //inner class
    public static final class NoteEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NOTES).build();

        //table name
        public static final String TABLE_NAME  = "notes";
        public static final String COLUMN_DESCRIPTION  = "description";
        public static final String COLUMN_PRIORITY  = "priority";
    }
}
