package edu.orangecoastcollege.cs273.caffeinefinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class CaffeineListActivity extends AppCompatActivity implements OnMapReadyCallback{

    private DBHelper db;
    private List<Location> allLocationsList;
    private ListView locationsListView;
    private LocationListAdapter locationListAdapter;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caffeine_list);

        deleteDatabase(DBHelper.DATABASE_NAME);
        db = new DBHelper(this);
        db.importLocationsFromCSV("locations.csv");

        allLocationsList = db.getAllCaffeineLocations();
        locationsListView = (ListView) findViewById(R.id.locationsListView);
        locationListAdapter = new LocationListAdapter(this, R.layout.location_list_item, allLocationsList);
        locationsListView.setAdapter(locationListAdapter);

        // Hook up SupportMapFragment to this activity

        SupportMapFragment caffeineMapFragment =
                (SupportMapFragment) getSupportFragmentManager().
                        findFragmentById(R.id.caffeineMapFragment);

        caffeineMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Use latitude and longitude for each location to create a Marker on the GoogleMap
        mMap = googleMap;

        // Loop through each Location and add them on the map
        for (Location l : allLocationsList) {
            LatLng coordinate = new LatLng(l.getLatitude(), l.getLongitude());

            // Add a Marker at this coordinate

            mMap.addMarker(new MarkerOptions().position(coordinate).title(l.getName()));

            // Update how mMap looks

            CameraPosition cP = new CameraPosition(coordinate, 14.0f, 0.0f, 0.0f);
            CameraUpdate cU = CameraUpdateFactory.newCameraPosition(cP);

            mMap.moveCamera(cU);
        }
    }
}
