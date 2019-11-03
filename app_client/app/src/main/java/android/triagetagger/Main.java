package android.triagetagger;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getPermission();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id./*add fab here*/);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Button button = findViewById(R.id./*add button name here*/);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(Main.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(Main.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null) {
//                            int num = 0;
//                            try {
//                                updatePatientLatitude(num, location.getLatitude());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                            try {
//                                updatePatientLongitude(num, location.getLongitude());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                        }
                    }
                });
            }
        });
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

    // old getLocation()
    // public void getLocation() {
    // Task<Location> locationResult =
    // mFusedLocationProviderClient.getLastLocation();
    // locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>()
    // {
    // @Override
    // public void onComplete(@NonNull Task<Location> task) {
    // if(task.isSuccessful()) {
    // mLastKnownLocation = task.getResult();
    // }
    // }
    // });
    // }

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
}
