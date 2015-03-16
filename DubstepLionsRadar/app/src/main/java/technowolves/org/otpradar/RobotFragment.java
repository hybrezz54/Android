package technowolves.org.otpradar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RobotFragment extends Fragment {

    private static final String PREFS_KEY = "technowolves.org.otpradar.RobotFragment.PREFERENCE_FILE_KEY";
    private static final String IMAGE_KEY = "ROBOT_PICTURE";
    private static final String STYLE_KEY = "ROBOT_STYLE";
    private static final String DRIVE_KEY = "DRIVE_TRAIN";
    private static final String WHEELS_KEY = "WHEELS";
    private static final String RATE_KEY = "ROBOT_RATE";
    private static final String NOTE_KEY = "ROBOT_NOTES";

    private static final String ARG_POS_NUMBER = "ROBOT_FRAG_POS";
    private static final String ARG_EDITING = "ROBOT_FRAG_STATE";

    public static final int REQUEST_IMG_CAPTURE = 3;

    private static final String[] HEADER = new String[] {"Team #", "Team Name", "Robot Style", "Drive Train", "Wheel Type", "Robot Rating", "Robot Notes"};
    private static final String[] ROBOT_STYLE = new String[] {"------", "Insane Tote Stacker/Lifter", "Recycle container carrier", "Tote hauler/pusher",
            "Tote Stacker/Lifter + Container Carry", "Tote Hauler/Pusher + Container Carry", "Tote Lifter + Tote pusher + Container Carry"};
    private static final String[] DRIVE_TRAIN = new String[] {"------", "Tank", "Mecanum", "Swerve", "Slide", "Holonomic"};
    private static final String[] WHEELS = new String[] {"------", "Mecanum", "Omni", "Tread", "Other"};

    private boolean isEditing;
    private int mPosition;
    private String mPhotoPath;

    private SharedPreferences mPrefs;
    private Activity mActivity;

    private ImageView mRobotImg;
    private Button mPicButton;
    private Spinner mRobotStyle;
    private Spinner mDriveTrain;
    private Spinner mWheels;
    private RatingBar mRate;
    private EditText mNotes;

    public static RobotFragment newInstance(int position, boolean editing) {
        RobotFragment fragment = new RobotFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POS_NUMBER, position);
        args.putBoolean(ARG_EDITING, editing);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        Bundle args = this.getArguments();
        mPosition = args.getInt(ARG_POS_NUMBER);
        isEditing = args.getBoolean(ARG_EDITING);

        mActivity = getActivity();
        updatePrefs();

        ActionBar bar = ((ActionBarActivity) mActivity).getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setTitle("Robot Details");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_robot_data, container, false);
        mRobotImg = (ImageView) rootView.findViewById(R.id.robotPic);
        mPicButton = (Button) rootView.findViewById(R.id.picButton);
        mRobotStyle = (Spinner) rootView.findViewById(R.id.robotStyle);
        mDriveTrain = (Spinner) rootView.findViewById(R.id.driveTrain);
        mWheels = (Spinner) rootView.findViewById(R.id.wheels);
        mRate = (RatingBar) rootView.findViewById(R.id.robotRate);
        mNotes = (EditText) rootView.findViewById(R.id.robotNotes);

        mPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (intent.resolveActivity(mActivity.getPackageManager()) != null) {

                    File photo = null;
                    try {
                        photo = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (photo != null) {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                        startActivityForResult(intent, REQUEST_IMG_CAPTURE);
                    }

                }
            }
        });

        initSpinner();
        loadValues();
        setPic();

        if (!isEditing)
            viewsEnabled(false);

        return rootView;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveValues();
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (isEditing)
            getActivity().getMenuInflater().inflate(R.menu.save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentManager fm = getFragmentManager();

        switch (id) {
            case android.R.id.home:
                fm.popBackStack();
                break;
            case R.id.action_save:
                saveValues();
                fm.popBackStack();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMG_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap img = (Bitmap) extras.get("data");
            mRobotImg.setImageBitmap(img);
        }
    }

    public void export(final String[] teams, final Activity activity) {

        mActivity = activity;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_DARK);
        builder.setTitle("Share via Bluetooth");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage("Do you really wish to send all robot data via bluetooth?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CsvWriter writer = new CsvWriter(activity, "robot_data", HEADER,
                        getStringsFromFields(teams, HEADER.length));
                writer.writeFile();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/*");
                intent.setPackage("com.android.bluetooth");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(writer.getFile()));
                activity.startActivity(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }

    public Uri getFileAfterWrite(final String[] teams, final Activity activity) {
        mActivity = activity;

        CsvWriter writer = new CsvWriter(activity, "robot_data", HEADER,
                getStringsFromFields(teams, HEADER.length));
        writer.writeFile();

        return Uri.fromFile(writer.getFile());
    }

    public Uri getImageFile() {
        File file = new File(mPhotoPath);
        return Uri.fromFile(file);
    }

    public void remove(int position) {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_KEY + position, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

    public void updatePosition(int position) {
        mPosition = position;
        updatePrefs();
        loadPhotoPath();
        //loadValues();
    }

    public void updateEditing(boolean editing) {
        this.isEditing = editing;
        viewsEnabled(editing);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = mPosition + "_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private String[] getStringsFromFields(String[] front, int length) {
        String[] values = new String[(front.length / 2) * length];
        int counter = 0;
        int index = 0;

        for (int i = 0; i < (front.length / 2); i++) {
            updatePosition(i);
            values[counter] = front[index];
            counter++;
            index++;
            values[counter] = front[index];
            counter++;
            index++;
            values[counter] = processStyles(mPrefs.getInt(STYLE_KEY, 0));
            counter++;
            values[counter] = processDrives(mPrefs.getInt(DRIVE_KEY, 0));
            counter++;
            values[counter] = processWheels(mPrefs.getInt(WHEELS_KEY, 0));
            counter++;
            values[counter] = String.valueOf(mPrefs.getFloat(RATE_KEY, 0f));
            counter++;
            values[counter] = processNotes(mPrefs.getString(NOTE_KEY, ""));
            counter++;
        }

        return values;
    }

    private void loadValues() {
        loadPhotoPath();

        int style = mPrefs.getInt(STYLE_KEY, 0);
        int drive = mPrefs.getInt(DRIVE_KEY, 0);
        int wheel = mPrefs.getInt(WHEELS_KEY, 0);
        float rate = mPrefs.getFloat(RATE_KEY, 0f);
        String notes = mPrefs.getString(NOTE_KEY, "");

        mRobotStyle.setSelection(style);
        mDriveTrain.setSelection(drive);
        mWheels.setSelection(wheel);
        mRate.setRating(rate);
        mNotes.setText(notes);
    }

    private void saveValues() {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(IMAGE_KEY, mPhotoPath);
        editor.putInt(STYLE_KEY, mRobotStyle.getSelectedItemPosition());
        editor.putInt(DRIVE_KEY, mDriveTrain.getSelectedItemPosition());
        editor.putInt(WHEELS_KEY, mWheels.getSelectedItemPosition());
        editor.putFloat(RATE_KEY, mRate.getRating());
        editor.putString(NOTE_KEY, mNotes.getText().toString());
        editor.commit();
    }

    private void loadPhotoPath() {
        mPhotoPath = mPrefs.getString(IMAGE_KEY, "");
    }

    private void viewsEnabled(boolean isEditing) {
        mRobotStyle.setEnabled(isEditing);
        mDriveTrain.setEnabled(isEditing);
        mWheels.setEnabled(isEditing);
        mRate.setEnabled(isEditing);
        mNotes.setEnabled(isEditing);
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = mRobotImg.getWidth();
        int targetH = mRobotImg.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath, bmOptions);
        mRobotImg.setImageBitmap(bitmap);
    }

    private void initSpinner() {
        ArrayAdapter<String> styles = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, ROBOT_STYLE);
        ArrayAdapter<String> drives = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, DRIVE_TRAIN);
        ArrayAdapter<String> wheels = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, WHEELS);

        mRobotStyle.setAdapter(styles);
        mDriveTrain.setAdapter(drives);
        mWheels.setAdapter(wheels);
    }

    private void updatePrefs() {
        if (mActivity != null)
            mPrefs = mActivity.getSharedPreferences(PREFS_KEY + mPosition, Context.MODE_PRIVATE);
    }

    private String processStyles(int index) {
        return ROBOT_STYLE[index];
    }

    private String processDrives(int index) {
        return DRIVE_TRAIN[index];
    }

    private String processWheels(int index) {
        return WHEELS[index];
    }

    private String processNotes(String text) {
        return text.replace("\n", "   ").replace(",", "");
    }

}
