package com.fitriasari.latvsga_aplikasimaps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap googleMap;
    double latitude;
    double longitude;
    Button koordinat, btn_user;
    FusedLocationProviderClient locationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    private Location currentLocation;
    private String provider;

    private LocationManager locationManager;


    private int LOCATION_PERMISSION = 100;
    private static final int UPDATE_INTERVAL = 5000; // 5 seconds


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // 1.inisialisasi button koordinat user dan posisi user
        koordinat = findViewById(R.id.koordinat);
        btn_user = findViewById(R.id.btn_user);


        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
//        assert mapFragment != null;
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }


        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("latvsga_aplikasimaps", "User tidak mengijinkan akses lokasi");
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        koordinat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                koordinatuser();
            }
        });

        btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posisiUser();
            }
        });
    }

    private void posisiUser() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                 float currentZoom = -1;
                //Create instance for current user lat and lng
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                // Add user position marker
                currentUserPositionMarker(latLng);

                //move map camera
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, currentZoom));


            }
        });
    }

    private void currentUserPositionMarker(LatLng latLng) {
        float currentZoom = -1;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
//        markerOptions.title(currentLocation);
//        locationProviderClient = googleMap.addMarker(markerOptions);

        //move map camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, currentZoom ));
    }


    private void koordinatuser() {
        FusedLocationProviderClient fusedLocation = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocation.getLastLocation().addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    Log.d("Saat ini saya berada", "Latitude : " + location.getLatitude() + "Longitude : "
                    + location.getLongitude());
                    Toast.makeText(MapsActivity.this, "Latitude : " +
                            location.getLatitude() + "Longitude : " + location.getLongitude(),Toast.LENGTH_LONG).show();

                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        locationManager.removeUpdates(MapsActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        LatLng sydney = new LatLng(-34, 151);
//        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL); // 3 seconds interval
        locationRequest.setFastestInterval(LOCATION_PERMISSION);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

//    public void onMapReady(GoogleMap googleMap) {
//
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }


}