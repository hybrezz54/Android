package technowolves.org.otpradar.fragment;

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

import technowolves.org.otpradar.R;
import technowolves.org.otpradar.util.CsvWriter;


public class RobotFragment extends Fragment {

    private static final String PREFS_KEY = "technowolves.org.otpradar.fragment.RobotFragment.PREFERENCE_FILE_KEY";
    private static final String IMAGE_KEY = "ROBOT_PICTURE";
    private static final String STYLE_KEY = "ROBOT_STYLE";
    private static final String DRIVE_KEY = "DRIVE_TRAIN";
    private static final String WHEELS_KEY = "WHEELS";
    private static final String RATE_KEY = "ROBOT_RATE";
    private static final String STRENGTH_KEY = "ROBOT_STRENGTH";
    private static final String WEAKNESS_KEY = "ROBOT_WEAKNESS";
    private static final String WEIGHT_KEY = "ROBOT_WEIGHT";
    private static final String HEIGHT_KEY = "ROBOT_HEIGHT";
    private static final String SPEED_KEY = "ROBOT_SPEED";
    private static final String TURRET_KEY = "ROBOT_TURRET";
    private static final String STRAFE_KEY = "ROBOT_STRAFE";
    private static final String AUTO1_KEY = "AUTO_STYLE_ONE";
    private static final String AUTO2_KEY = "AUTO_STYLE_TWO";
    private static final String AUTO3_KEY = "AUTO_STYLE_THREE";
    private static final String START_KEY = "AUTO_START";
    private static final String CAPACITY_KEY = "TOTE_CAPACITY";

    private static final String ARG_POS_NUMBER = "ROBOT_FRAG_POS";
    private static final String ARG_EDITING = "ROBOT_FRAG_STATE";

    public static final int REQUEST_IMG_CAPTURE = 3;

    private static final String[] HEADER = new String[] {"Team #", "Team Name", "Robot Style", "Drive Train", "Wheel Type", "Robot Rating", "Strengths", "Weaknesses",
            "Robot Weight", "Robot Height", "Robot Speed", "Robot Turret", "Robot Strafing", "Auto: move totes?", "Auto: move containers?", "Auto: acquire containers?",
            "Preferred Starting Location", "Tote Carry Capacity"};
    private static final String[] ROBOT_STYLE = new String[] {"------", "Insane Tote Stacker/Lifter", "Recycle container carrier", "Tote hauler/pusher",
            "Tote Stacker/Lifter + Container Carry", "Tote Hauler/Pusher + Container Carry", "Tote Lifter + Tote pusher + Container Carry"};
    private static final String[] DRIVE_TRAIN = new String[] {"------", "Tank", "Mecanum", "Swerve", "Slide", "Holonomic"};
    private static final String[] WHEELS = new String[] {"------", "Mecanum", "Omni", "Tread", "Other"};
    /*private static final String[] AUTO_STYLE = new String[] {"------", "Do Nothing", "Move to Auto zone", "Lift tote", "Lift recycle container","Lift tote & move",
            "Lift container & move", "Push tote", "Push container", "Stack totes", "Spin w/ tote", "Spin w/ container", "Move to Landfill", "Get tote from Landfill",
            "Get container from Landfill", "Aim for coopertition totes"};*/
    private static final String[] FRCSCOUT_HEADER = new String[] {"Robot Height", "Robot Weight", "Can move totes?", "Can move containers?", "Can acquire containers?",
            "Preferred starting location", "Tote Stack Capacity", "Robot has turret?", "Robot has strafing?", "Robot speed", "Robot Strengths", "Robot Weaknesses"};
    public static final String[] SIMPLE = new String[] {"------", "No", "Yes"};
    public static final String[] AUTO_START = new String[] {"------", "Side staging zone w/ no platform", "Middle staging zone", "Side staging zone w/ platform",
            "Auto zone"};

    private boolean isEditing;
    private int mPosition;
    private String mPhotoPath = "";

    private SharedPreferences mPrefs;
    private Activity mActivity;

    private ImageView mRobotImg;
    private Button mPicButton;
    private Spinner mRobotStyle;
    private Spinner mDriveTrain;
    private Spinner mWheels;
    private RatingBar mRate;
    private EditText mStrength;
    private EditText mWeakness;
    private EditText mWeight;
    private EditText mHeight;
    private EditText mSpeed;
    private Spinner mTurret;
    private Spinner mStrafe;
    private Spinner mAuto1;
    private Spinner mAuto2;
    private Spinner mAuto3;
    private Spinner mAutoStart;
    private EditText mCapacity;

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
        mStrength = (EditText) rootView.findViewById(R.id.robotStrength);
        mWeakness = (EditText) rootView.findViewById(R.id.robotWeakness);
        mWeight = (EditText) rootView.findViewById(R.id.edtWeight);
        mHeight = (EditText) rootView.findViewById(R.id.edtHeight);
        mSpeed = (EditText) rootView.findViewById(R.id.edtSpeed);
        mTurret = (Spinner) rootView.findViewById(R.id.turret);
        mStrafe = (Spinner) rootView.findViewById(R.id.strafing);
        mAuto1 = (Spinner) rootView.findViewById(R.id.autoStyle1);
        mAuto2 = (Spinner) rootView.findViewById(R.id.autoStyle2);
        mAuto3 = (Spinner) rootView.findViewById(R.id.autoStyle3);
        mAutoStart = (Spinner) rootView.findViewById(R.id.preferredStart);
        mCapacity = (EditText) rootView.findViewById(R.id.edtToteCapacity);

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!mPhotoPath.equals(""))
            setPic();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMG_CAPTURE && resultCode == Activity.RESULT_OK) {
            /*Bundle extras = data.getExtras();
            Bitmap img = (Bitmap) extras.get("data");
            mRobotImg.setImageBitmap(img);*/
            setPic();
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

    public Uri getImageFile(final int index) {
        //mActivity = activity;
        updatePosition(index);
        loadPhotoPath();
        File file = new File(mPhotoPath);
        return Uri.fromFile(file);
    }

    public String[] getFrcScoutExportArray(int index, Activity activity) {
        mActivity = activity;
        updatePosition(index);

        String[] values = new String[FRCSCOUT_HEADER.length];
        values[0] = mPrefs.getString(HEIGHT_KEY, "");
        values[1] = mPrefs.getString(WEIGHT_KEY, "");
        values[2] = processSimple(mPrefs.getInt(AUTO1_KEY, 0));
        values[3] = processSimple(mPrefs.getInt(AUTO2_KEY, 0));
        values[4] = processSimple(mPrefs.getInt(AUTO3_KEY, 0));
        values[5] = processStart(mPrefs.getInt(START_KEY, 0));
        values[6] = mPrefs.getString(CAPACITY_KEY, "");
        values[7] = processSimple(mPrefs.getInt(TURRET_KEY, 0));
        values[8] = processSimple(mPrefs.getInt(STRAFE_KEY, 0));
        values[9] = processNotes(mPrefs.getString(SPEED_KEY, ""));
        values[10] = processNotes(mPrefs.getString(STRENGTH_KEY, ""));
        values[11] = processNotes(mPrefs.getString(WEAKNESS_KEY, ""));

        return values;
    }

    public void remove(int position, Activity activity) {
        updatePosition(position);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.clear();
        editor.commit();
    }

    private void updatePosition(int position) {
        mPosition = position;
        updatePrefs();
        //loadPhotoPath();
        //loadValues();
    }

    /*private void updateEditing(boolean editing) {
        this.isEditing = editing;
        viewsEnabled(editing);
    }*/

    private File createImageFile() throws IOException {
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String number = ((PrimaryListFragment) getFragmentManager().findFragmentByTag("PrimaryListFragment3"))
                .getTeamNumber(mPosition);
        String imageFileName = number + "_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        //mPhotoPath = "file:" + image.getAbsolutePath();
        mPhotoPath = image.getPath();
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
            values[counter] = processNotes(mPrefs.getString(STRENGTH_KEY, ""));
            counter++;
            values[counter] = processNotes(mPrefs.getString(WEAKNESS_KEY, ""));
            counter++;
            values[counter] = mPrefs.getString(WEIGHT_KEY, "");
            counter++;
            values[counter] = mPrefs.getString(HEIGHT_KEY, "");
            counter++;
            values[counter] = mPrefs.getString(SPEED_KEY, "");
            counter++;
            values[counter] = processSimple(mPrefs.getInt(TURRET_KEY, 0));
            counter++;
            values[counter] = processSimple(mPrefs.getInt(STRAFE_KEY, 0));
            counter++;
            values[counter] = processSimple(mPrefs.getInt(AUTO1_KEY, 0));
            counter++;
            values[counter] = processSimple(mPrefs.getInt(AUTO2_KEY, 0));
            counter++;
            values[counter] = processSimple(mPrefs.getInt(AUTO3_KEY, 0));
            counter++;
            values[counter] = processStart(mPrefs.getInt(START_KEY, 0));
            counter++;
            values[counter] = mPrefs.getString(CAPACITY_KEY, "");
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
        String strength = mPrefs.getString(STRENGTH_KEY, "");
        String weakness = mPrefs.getString(WEAKNESS_KEY, "");
        String weight = mPrefs.getString(WEIGHT_KEY, "");
        String height = mPrefs.getString(HEIGHT_KEY, "");
        String speed = mPrefs.getString(SPEED_KEY, "");
        int turret = mPrefs.getInt(TURRET_KEY, 0);
        int strafe = mPrefs.getInt(STRAFE_KEY, 0);
        int auto1 = mPrefs.getInt(AUTO1_KEY, 0);
        int auto2 = mPrefs.getInt(AUTO2_KEY, 0);
        int auto3 = mPrefs.getInt(AUTO3_KEY, 0);
        int start = mPrefs.getInt(START_KEY, 0);
        String capacity = mPrefs.getString(CAPACITY_KEY, "");

        mRobotStyle.setSelection(style);
        mDriveTrain.setSelection(drive);
        mWheels.setSelection(wheel);
        mRate.setRating(rate);
        mStrength.setText(strength);
        mWeakness.setText(weakness);
        mWeight.setText(weight);
        mHeight.setText(height);
        mSpeed.setText(speed);
        mTurret.setSelection(turret);
        mStrafe.setSelection(strafe);
        mAuto1.setSelection(auto1);
        mAuto2.setSelection(auto2);
        mAuto3.setSelection(auto3);
        mAutoStart.setSelection(start);
        mCapacity.setText(capacity);
    }

    private void saveValues() {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(IMAGE_KEY, mPhotoPath);
        editor.putInt(STYLE_KEY, mRobotStyle.getSelectedItemPosition());
        editor.putInt(DRIVE_KEY, mDriveTrain.getSelectedItemPosition());
        editor.putInt(WHEELS_KEY, mWheels.getSelectedItemPosition());
        editor.putFloat(RATE_KEY, mRate.getRating());
        editor.putString(STRENGTH_KEY, mStrength.getText().toString());
        editor.putString(WEAKNESS_KEY, mWeakness.getText().toString());
        editor.putString(WEIGHT_KEY, mWeight.getText().toString());
        editor.putString(HEIGHT_KEY, mHeight.getText().toString());
        editor.putString(SPEED_KEY, mSpeed.getText().toString());
        editor.putInt(TURRET_KEY, mTurret.getSelectedItemPosition());
        editor.putInt(STRAFE_KEY, mStrafe.getSelectedItemPosition());
        editor.putInt(AUTO1_KEY, mAuto1.getSelectedItemPosition());
        editor.putInt(AUTO2_KEY, mAuto2.getSelectedItemPosition());
        editor.putInt(AUTO3_KEY, mAuto3.getSelectedItemPosition());
        editor.putInt(START_KEY, mAutoStart.getSelectedItemPosition());
        editor.putString(CAPACITY_KEY, mCapacity.getText().toString());
        editor.commit();
    }

    private void loadPhotoPath() {
        mPhotoPath = mPrefs.getString(IMAGE_KEY, "");
    }

    private void viewsEnabled(boolean isEditing) {
        mPicButton.setEnabled(isEditing);
        mRobotStyle.setEnabled(isEditing);
        mDriveTrain.setEnabled(isEditing);
        mWheels.setEnabled(isEditing);
        mRate.setEnabled(isEditing);
        mStrength.setEnabled(isEditing);
        mWeakness.setEnabled(isEditing);
        mWeight.setEnabled(isEditing);
        mHeight.setEnabled(isEditing);
        mSpeed.setEnabled(isEditing);
        mTurret.setEnabled(isEditing);
        mStrafe.setEnabled(isEditing);
        mAuto1.setEnabled(isEditing);
        mAuto2.setEnabled(isEditing);
        mAuto3.setEnabled(isEditing);
        mAutoStart.setEnabled(isEditing);
        mCapacity.setEnabled(isEditing);
    }

    private void setPic() {
        /*// Get the dimensions of the View
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

        Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath, bmOptions);*/

        Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath);
        mRobotImg.setImageBitmap(bitmap);
    }

    private void initSpinner() {
        ArrayAdapter<String> styles = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_dropdown_item, ROBOT_STYLE);
        ArrayAdapter<String> drives = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_dropdown_item, DRIVE_TRAIN);
        ArrayAdapter<String> wheels = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_dropdown_item, WHEELS);
        ArrayAdapter<String> simple = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_dropdown_item, SIMPLE);
        ArrayAdapter<String> start = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_dropdown_item, AUTO_START);

        mRobotStyle.setAdapter(styles);
        mDriveTrain.setAdapter(drives);
        mWheels.setAdapter(wheels);
        mTurret.setAdapter(simple);
        mStrafe.setAdapter(simple);
        mAuto1.setAdapter(simple);
        mAuto2.setAdapter(simple);
        mAuto3.setAdapter(simple);
        mAutoStart.setAdapter(start);
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

    private String processSimple(int index) {
        return SIMPLE[index];
    }

    private String processStart(int index) { return AUTO_START[index]; }

    private String processNotes(String text) {
        return text.replace("\n", "   ").replace(",", "");
    }

    /*private String processAutoStyle(int index) {
        return AUTO_STYLE[index];
    }*/

}
