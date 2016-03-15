package org.technowolves.otpradar.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.technowolves.otpradar.ActivityInteractionListener;
import org.technowolves.otpradar.FragmentInteractionListener;
import org.technowolves.otpradar.R;
import org.technowolves.otpradar.SharedMap;
import org.technowolves.otpradar.model.Robot2016;
import org.technowolves.otpradar.model.Team;
import org.technowolves.otpradar.util.IoUtils;
import org.technowolves.otpradar.view.activity.SettingsActivity;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RobotFragInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RobotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RobotFragment extends Fragment implements ActivityInteractionListener {

    public static final String[] STYLE_2015 = new String[] {"---ROBOT STYLE---", "TOTE STACKER/LIFTER",
            "CONTAINER CARRIER", "TOTE HAULER/PUSHER", "TOTE STACK + CONTAINER CARRY", "TOTE PUSH + CONTAINER CARRY",
            "STACK + PUSH + CARRY"};
    public static final String[] STYLE_2016 = new String[] {"---ROBOT STYLE---", "HIGH SHOOTER",
            "LOW SHOOTER", "LOW & HIGH SHOOTER"};

    public static final String[] DRIVE_TRAIN = new String[] {"---DRIVE TRAIN---", "TANK", "MECANUM", "SWERVE",
            "SLIDE", "HOLONOMIC", "OTHER"};
    public static final String[] WHEEL = new String[] {"---WHEELS---", "MECANUM", "OMNI", "TREAD", "OTHER"};

    private FragmentInteractionListener mFragListener;
    private RobotFragInteractionListener mListener;
    private Robot2016 mRobot;

    private View view;
    private TextView robotNumber;
    private EditText robotName;
    private EditText weight;
    private EditText height;
    private Spinner robotStyle;
    private Spinner driveTrain;
    private Spinner wheels;
    private CheckBox shootBall;
    private CheckBox visionTrack;
    private CheckBox portcullisAuto;
    private CheckBox drawbridgeAuto;
    private CheckBox chevalDeFriseAuto;
    private CheckBox moatAuto;
    private CheckBox rampartsAuto;
    private CheckBox sallyportAuto;
    private CheckBox rockWallAuto;
    private CheckBox roughTerrainAuto;
    private CheckBox lowBarAuto;
    private CheckBox portcullis;
    private CheckBox drawbridge;
    private CheckBox chevalDeFrise;
    private CheckBox moat;
    private CheckBox ramparts;
    private CheckBox sallyport;
    private CheckBox rockWall;
    private CheckBox roughTerrain;
    private CheckBox lowBar;

    public RobotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RobotFragment.
     */
    public static RobotFragment newInstance() {
        RobotFragment fragment = new RobotFragment();
        /*Bundle args = new Bundle();
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_robot_2016, container, false);

        robotNumber = (TextView) view.findViewById(R.id.robotNumber);
        robotName = (EditText) view.findViewById(R.id.robotName2016);
        weight = (EditText) view.findViewById(R.id.robotWeight2016);
        height = (EditText) view.findViewById(R.id.robotHeight2016);
        robotStyle = (Spinner) view.findViewById(R.id.robotStyle2016);
        driveTrain = (Spinner) view.findViewById(R.id.driveTrain2016);
        wheels = (Spinner) view.findViewById(R.id.wheels2016);
        shootBall = (CheckBox) view.findViewById(R.id.shootBall);
        visionTrack = (CheckBox) view.findViewById(R.id.visionTrack);
        portcullisAuto = (CheckBox) view.findViewById(R.id.portcullisAuto);
        drawbridgeAuto = (CheckBox) view.findViewById(R.id.drawbridgeAuto);
        chevalDeFriseAuto = (CheckBox) view.findViewById(R.id.chevalDeFriseAuto);
        moatAuto = (CheckBox) view.findViewById(R.id.moatAuto);
        rampartsAuto = (CheckBox) view.findViewById(R.id.rampartsAuto);
        sallyportAuto = (CheckBox) view.findViewById(R.id.sallyportAuto);
        rockWallAuto = (CheckBox) view.findViewById(R.id.rockWallAuto);
        roughTerrainAuto = (CheckBox) view.findViewById(R.id.roughTerrainAuto);
        lowBarAuto = (CheckBox) view.findViewById(R.id.lowbarAuto);
        portcullis = (CheckBox) view.findViewById(R.id.portcullis);
        drawbridge = (CheckBox) view.findViewById(R.id.drawbridge);
        chevalDeFrise = (CheckBox) view.findViewById(R.id.chevalDeFrise);
        moat = (CheckBox) view.findViewById(R.id.moat);
        ramparts = (CheckBox) view.findViewById(R.id.ramparts);
        sallyport = (CheckBox) view.findViewById(R.id.sallyport);
        rockWall = (CheckBox) view.findViewById(R.id.rockWall);
        roughTerrain = (CheckBox) view.findViewById(R.id.roughTerrain);
        lowBar = (CheckBox) view.findViewById(R.id.lowbar);

        ArrayAdapter<String> style = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, STYLE_2016);
        robotStyle.setAdapter(style);

        ArrayAdapter<String> drive = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, DRIVE_TRAIN);
        driveTrain.setAdapter(drive);

        ArrayAdapter<String> wheel = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, WHEEL);
        wheels.setAdapter(wheel);

        mRobot = null;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                setupData();
            }
        }, 500);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RobotFragInteractionListener &&
                context instanceof FragmentInteractionListener) {
            mFragListener = (FragmentInteractionListener) context;
            mListener = (RobotFragInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RobotFragInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onButtonPressed() {
        if (mRobot != null) {
            mRobot.setName(robotName.getText().toString());
            mRobot.setWeight(weight.getText().toString());
            mRobot.setHeight(height.getText().toString());
            mRobot.setStyle(robotStyle.getSelectedItemPosition());
            mRobot.setDriveTrain(driveTrain.getSelectedItemPosition());
            mRobot.setWheels(wheels.getSelectedItemPosition());
            mRobot.setShootBall(shootBall.isChecked());
            mRobot.setVisionTrack(visionTrack.isChecked());
            mRobot.setPortcullisAuto(portcullisAuto.isChecked());
            mRobot.setDrawbridgeAuto(drawbridgeAuto.isChecked());
            mRobot.setChevalDeFriseAuto(chevalDeFriseAuto.isChecked());
            mRobot.setMoatAuto(moatAuto.isChecked());
            mRobot.setRampartsAuto(rampartsAuto.isChecked());
            mRobot.setSallyportAuto(sallyportAuto.isChecked());
            mRobot.setRockWallAuto(rockWallAuto.isChecked());
            mRobot.setRoughTerrainAuto(roughTerrainAuto.isChecked());
            mRobot.setLowBarAuto(lowBarAuto.isChecked());
            mRobot.setPortcullis(portcullis.isChecked());
            mRobot.setDrawbridge(drawbridge.isChecked());
            mRobot.setChevalDeFrise(chevalDeFrise.isChecked());
            mRobot.setMoat(moat.isChecked());
            mRobot.setRamparts(ramparts.isChecked());
            mRobot.setSallyport(sallyport.isChecked());
            mRobot.setRockWall(rockWall.isChecked());
            mRobot.setRoughTerrain(roughTerrain.isChecked());
            mRobot.setLowBar(lowBar.isChecked());

            if (IoUtils.isExternalStorageAvailable() || IoUtils.isExternalStorageReadOnly()) {
                String json = new Gson().toJson(mRobot);
                IoUtils.writeBytestoFile(getContext(), SharedMap.USER_DATA_DIR,
                        getFileName(mRobot.getNumber()), json.getBytes());
            }
        }
    }

    public void setupData() {
        Team team = mFragListener.getSelectedTeam();

        if (team != null) {
            String number = team.getNumber();
            mRobot = new Robot2016(number);
            boolean fileExists = IoUtils.isFileExisting(getContext(),
                    SharedMap.USER_DATA_DIR, getFileName(number));

            if (fileExists) {
                Gson gson = new Gson();
                String json = IoUtils.readStringFromFile(getContext(),
                        SharedMap.USER_DATA_DIR, getFileName(number));
                mRobot = gson.fromJson(json, Robot2016.class);
            }

            if (number.equals(""))
                mRobot = null;
        }

        setupViews();
    }

    private void setupViews() {
        if (mRobot != null) {
            robotNumber.setText(mRobot.getNumber());
            robotName.setText(mRobot.getName());
            weight.setText(mRobot.getWeight());
            height.setText(mRobot.getHeight());
            robotStyle.setSelection(mRobot.getStyle());
            driveTrain.setSelection(mRobot.getDriveTrain());
            wheels.setSelection(mRobot.getWheels());
            shootBall.setChecked(mRobot.isShootBall());
            visionTrack.setChecked(mRobot.isVisionTrack());
            portcullisAuto.setChecked(mRobot.isPortcullisAuto());
            drawbridgeAuto.setChecked(mRobot.isDrawbridgeAuto());
            chevalDeFriseAuto.setChecked(mRobot.isChevalDeFriseAuto());
            moatAuto.setChecked(mRobot.isMoatAuto());
            rampartsAuto.setChecked(mRobot.isRampartsAuto());
            sallyportAuto.setChecked(mRobot.isSallyportAuto());
            rockWallAuto.setChecked(mRobot.isRockWallAuto());
            roughTerrainAuto.setChecked(mRobot.isRoughTerrainAuto());
            lowBarAuto.setChecked(mRobot.isLowBarAuto());
            portcullis.setChecked(mRobot.isPortcullis());
            drawbridge.setChecked(mRobot.isDrawbridge());
            chevalDeFrise.setChecked(mRobot.isChevalDeFrise());
            moat.setChecked(mRobot.isMoat());
            ramparts.setChecked(mRobot.isRamparts());
            sallyport.setChecked(mRobot.isSallyport());
            rockWall.setChecked(mRobot.isRockWall());
            roughTerrain.setChecked(mRobot.isRoughTerrain());
            lowBar.setChecked(mRobot.isLowBar());
        } else {
            robotName.setText("");
            weight.setText("");
            height.setText("");
            robotStyle.setSelection(0);
            driveTrain.setSelection(0);
            wheels.setSelection(0);
            shootBall.setChecked(false);
            visionTrack.setChecked(false);
            portcullisAuto.setChecked(false);
            drawbridgeAuto.setChecked(false);
            chevalDeFriseAuto.setChecked(false);
            moatAuto.setChecked(false);
            rampartsAuto.setChecked(false);
            sallyportAuto.setChecked(false);
            rockWallAuto.setChecked(false);
            roughTerrainAuto.setChecked(false);
            lowBarAuto.setChecked(false);
            portcullis.setChecked(false);
            drawbridge.setChecked(false);
            chevalDeFrise.setChecked(false);
            moat.setChecked(false);
            ramparts.setChecked(false);
            sallyport.setChecked(false);
            rockWall.setChecked(false);
            roughTerrain.setChecked(false);
            lowBar.setChecked(false);
        }
    }

    private String getFileName(String number) {
        SharedPreferences manager = PreferenceManager.getDefaultSharedPreferences(getContext());

        String filename = manager.getString(SettingsActivity.EVENT_KEY, "2016ncral");
        filename += "_robot_" + number + ".json";

        return filename;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface RobotFragInteractionListener {
    }
}
