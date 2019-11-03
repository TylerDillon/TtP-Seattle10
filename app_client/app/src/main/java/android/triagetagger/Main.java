package android.triagetagger;

import java.io.*;
import java.sql.*;
import android.os.*;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import android.view.*;
import android.widget.TextView;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.net.URL;

import android.location.Location;
import android.widget.Button;

import androidx.annotation.NonNull;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

//THE ONLY SOURCES OF ERRORS ARE BECAUSE I CAN'T MAKE THE BUTTONS
public class Main extends AppCompatActivity {
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private Location mLastKnownLocation;

    String triage_color = "GREEN";
    int gps_string_1 = 0;
    int gps_string_2 = 0;
    int green_count = 0;
    int yellow_count = 0;
    int red_count = 0;
    int stripe_count = 0;
    private Connection conn;
    private static final String COUNT_SELECT_COLOR = "SELECT COUNT(*) FROM Bracelet WHERE triage_color = ?;";
    private PreparedStatement countSelectColorStatement;


    public void newStripeTag(View view) {
        stripe_count++;
        triage_color = "STRIPE";
    }


    public void newRedTag(View view) {
        red_count++;
        triage_color = "RED";
    }

    public void newGreenTag(View view) {
        green_count++;
        triage_color = "GREEN";
    }

    public void newYellowTag(View view) {
        yellow_count++;
        triage_color = "YELLOW";
    }

    public void getLocationTag(View view) {
        if(ActivityCompat.checkSelfPermission(Main.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(Main.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    int num = 0;
                    try {
                        updatePatientLatitude(num, location.getLatitude());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        updatePatientLongitude(num, location.getLongitude());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getPermission();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    public void createPatient(int num, String triage_color, double latitude, double longitude) throws IOException {
        URL url = new URL(
                "https://seattle10demo.glitch.me/add_patient?patient_id="+num+"&triage_color="+triage_color+"&gps_longitude="+longitude+"&gps_longitude="+latitude);
    }

    public void createPatient(int num, String triage_color, String firstName, double latitude, double longitude) throws IOException {
        URL url = new URL(
                "https://seattle10demo.glitch.me/add_patient?patient_id="+num+"&triage_color="+triage_color+"&first_name="+firstName+"&gps_longitude="+longitude+"&gps_longitude="+latitude);
    }

    public void createPatient(int num, String triage_color, String firstName, String lastName, double latitude, double longitude) throws IOException {
        URL url = new URL(
                "https://seattle10demo.glitch.me/add_patient?patient_id="+num+"&triage_color="+triage_color+"&first_name="+firstName+"&last_name="+lastName+"&gps_longitude="+longitude+"&gps_longitude="+latitude);
    }

    public void updatePatientLatitude(int patientNum, double latitude) throws IOException {
        URL url = new URL(
                "https://seattle10demo.glitch.me/set_gps_latitude?patient_id=" + patientNum + "&value=" + latitude);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    }

    public void updatePatientLongitude(int patientNum, double longitude) throws IOException {
        URL url = new URL(
                "https://seattle10demo.glitch.me/set_gps_latitude?patient_id=" + patientNum + "&value=" + longitude);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    }

    public void updateTriageColor(int patientNum, String color) throws IOException {
        URL url = new URL(
                "https://seattle10demo.glitch.me/set_triage_color?patient_id=" + patientNum + "&value=" + color);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    }

    public void setFirstName(int patientNum, String firstName) throws IOException {
        URL url = new URL(
                "https://seattle10demo.glitch.me/set_first_name?patient_id=" + patientNum + "&value=" + firstName);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    }

    public void setLastName(int patientNum, String lastName) throws IOException {
        URL url = new URL(
                "https://seattle10demo.glitch.me/set_last_name?patient_id=" + patientNum + "&value=" + lastName);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    }

    private void getPermission() {
        ActivityCompat.requestPermissions(this, new String[] { ACCESS_FINE_LOCATION }, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void prepareStatements() throws SQLException {
        countSelectColorStatement = conn.prepareStatement(COUNT_SELECT_COLOR);
    }
    public String CountSelectColor(String color) {
        try {
            countSelectColorStatement.setNString(1, color);
            ResultSet result = countSelectColorStatement.executeQuery();
            return " triage color : " + color + "\n";
        } catch(SQLException e){
            e.printStackTrace();
            return "Count select number ERROR";
        }
    }

    public void tag_stripe(){
    }

    public void displayForGps1(){
        TextView gpsView = (TextView) findViewById(R.id.gps_string_1);
        gpsView.setText(String.valueOf(gps_string_1));
    }
    public void displayForGps2(){
        TextView gpsView = (TextView) findViewById(R.id.gps_string_2);
        gpsView.setText(String.valueOf(gps_string_2));
    }
    public void displayGreen(){
        TextView greenView = (TextView) findViewById(R.id.green_count);
        greenView.setText(String.valueOf(green_count));
    }
    public void displayYellow(){
        TextView yellowView = (TextView) findViewById(R.id.yellow_count);
        yellowView.setText(String.valueOf(yellow_count));
    }
    public void displayRed(){
        TextView redView = (TextView) findViewById(R.id.red_count);
        redView.setText(String.valueOf(red_count));
    }
    public void displayStripe(){
        TextView stripeView = (TextView) findViewById(R.id.stripe_count);
        stripeView.setText(String.valueOf(stripe_count));
    }

}