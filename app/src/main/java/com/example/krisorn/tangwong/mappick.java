package com.example.krisorn.tangwong;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class mappick extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    public FirebaseAuth mAuth;
    Button buttonNormal;
    Button buttonSatellite;
    Button buttonHybrid;
    SupportMapFragment mapFragment;
    private DatabaseReference mapdatabase;
    private String getKey;
    //getcurrent location;
    public  String tvLongi;
    public  String tvLati;
    public  String nameUser;
    public double longx ;
    public double latix ;
    public  double dLatitude ;
    public double dLongitude ;
    public boolean readmap  = true ;
    public boolean checkfuntion,getlocal = false;
    TextView tvLatitude;
    TextView tvLongitude;
    LocationManager locationManager;
    Location userlocation ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mappick);
        CheckPermission();
        buttonNormal = (Button) findViewById(R.id.buttonNormal);
        buttonSatellite = (Button) findViewById(R.id.buttonSatellite);
        buttonHybrid = (Button) findViewById(R.id.buttonHybrid);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        buttonNormal = (Button) findViewById(R.id.buttonNormal);
        buttonSatellite = (Button) findViewById(R.id.buttonSatellite);
        buttonHybrid = (Button) findViewById(R.id.buttonHybrid);


    }
    public void CheckPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void  click(View view){
        switch (view.getId()) {
            case R.id.buttonSatellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.buttonHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            default:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
        }

        mapFragment.getMapAsync(this);

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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



    }

    @Override
    public void onLocationChanged(final Location location) {

        // Getting reference to TextView tv_longitude
        tvLongitude = (TextView) findViewById(R.id.tv_longitude);
        // Getting reference to TextView tv_latitude
        tvLatitude = (TextView) findViewById(R.id.tv_latitude);

        if(location!=null)
        {


            mAuth=FirebaseAuth.getInstance();
            final FirebaseUser user = mAuth.getCurrentUser();
            mapdatabase = FirebaseDatabase.getInstance().getReference();
            mapdatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    getKey = dataSnapshot.child("user").child(user.getUid()).child("livenow").getValue(String.class);
                    long getcount = dataSnapshot.child("room").child(getKey).child("Map").getChildrenCount();
                    for(int i = 0 ; i < getcount ; i++) {
                        tvLongi = dataSnapshot.child("room").child(getKey).child("Map").child(String.valueOf(i)).child("location").child("long").getValue(String.class);
                        tvLati = dataSnapshot.child("room").child(getKey).child("Map").child(String.valueOf(i)).child("location").child("lat").getValue(String.class);
                        dLongitude = Double.valueOf(tvLongi);
                        dLatitude = Double.valueOf(tvLati);
                        Log.d("taxx", "be");
                        Log.d("taxx", String.valueOf(dLongitude));
                        Log.d("taxx", String.valueOf(dLatitude));
                        mMap.addMarker(new MarkerOptions().position(new LatLng(dLatitude, dLongitude))
                                .title("My Location").icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude), 15));
                    }
                    Log.d("taxx","af");
                    Log.d("taxx",String.valueOf(dLongitude));
                    Log.d("taxx",String.valueOf(dLatitude));

                    Log.d("taxx","out");
                    Log.d("taxx",String.valueOf(dLongitude));
                    Log.d("taxx",String.valueOf(dLatitude));

                    // Setting Current Longitude
                    tvLongitude.setText("Longitude:" + dLongitude);
                    // Setting Current Latitude
                    tvLatitude.setText("Latitude:" + dLatitude);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




        }
        else
        {
            Toast.makeText(this, "Unable to fetch the current location", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider!" + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(mappick.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();


    }
}
