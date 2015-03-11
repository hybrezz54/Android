package technowolves.org.dubsteplionsradar;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CsvWriter {

    private static final String SEPERATOR = ",";

    private Context mContext;
    private String[] header;
    private String[] values;
    private String mPath = "";

    public CsvWriter(Context context, String[] header, String[] values) {
        mContext = context;
        this.header = header;
        this.values = values;
    }

    public void writeFile() {
        if (isExternalStorageWritable()) {
            try {
                mPath = getStorageDir(mContext, "data")
                        .getPath() + File.separator + getCurrentTime() + ".csv";
                FileOutputStream fOut = new FileOutputStream(mPath);
                fOut.write(csvString().getBytes());
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFilePath() {
        return mPath;
    }

    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private File getStorageDir(Context context, String fileDir) {
        // Get the directory for the user's public pictures directory.
        File file = new File(context.getExternalFilesDir(null),
                fileDir);

        if (!file.mkdirs())
            Log.e("CsvWriter", "Directory not created");

        return file;
    }

    private String csvString() {
        String csv = "";

        for (int i = 0; i < header.length; i++) {
            csv += header[i];
            if (i < (header.length - 1))
                csv += SEPERATOR;
        }

        for (int i = 0; i < values.length; i++) {
            csv += values[i];
            if (i < (header.length - 1))
                csv += SEPERATOR;
            else
                csv += "\n";
        }

        Log.v("CsvWriter", csv);
        return csv;
    }

    private String getCurrentTime() {
        //Calendar calendar = Calendar.getInstance();
        return String.valueOf(System.currentTimeMillis());
    }

}
