package com.example.ahmed.zgo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Map extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private String username, email, image;
    private TextView name, mail, dist;
    private ImageView img;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    Bundle bundle;
    private EditText from, to;
    private Button find;

    GoogleApiClient client;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        startService(new Intent(this, Background.class));

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        find = (Button) findViewById(R.id.find);
        from = (EditText) findViewById(R.id.from);
        to = (EditText) findViewById(R.id.to);
        dist = (TextView) findViewById(R.id.dist);

        navHeader = navigationView.getHeaderView(0);
        bundle = getIntent().getExtras();

        username = bundle.getString("username");
        email = bundle.getString("email");
        image = bundle.getString("photo");
        name = (TextView) navHeader.findViewById(R.id.profile_username);
        mail = (TextView) navHeader.findViewById(R.id.profile_email);
        img = (ImageView) navHeader.findViewById(R.id.profile_image);

        name.setText(username);
        mail.setText(email);

        Bitmap selectedImage = BitmapFactory.decodeFile(image);
        Drawable drawable = new BitmapDrawable(selectedImage);
        img.setImageDrawable(drawable);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if (googleServices()) {
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
            mapFragment.getMapAsync(this);

        }

        geoLocate();

    }

    public boolean googleServices() {
        GoogleApiAvailability ga = GoogleApiAvailability.getInstance();
        int isAvailable = ga.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS)
            return true;
        else if (ga.isUserResolvableError(isAvailable)) {
            Dialog dialog = ga.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else
            Toast.makeText(this, "can not to play services", Toast.LENGTH_SHORT).show();
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(Map.this, History.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
    }


    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        map.moveCamera(update);
    }

    public void geoLocate() {
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = from.getText().toString();
                String destination = to.getText().toString();

                Geocoder geocoder = new Geocoder(Map.this);
                try {
                    List<android.location.Address> listfrom = geocoder.getFromLocationName(location, 1);
                    List<android.location.Address> listto = geocoder.getFromLocationName(destination, 1);
                    android.location.Address addressfrom = listfrom.get(0);
                    android.location.Address addressto = listto.get(0);
                    String localityfrom = addressfrom.getLocality();
                    String localityto = addressto.getLocality();
                    //Toast.makeText(Map.this, locality, Toast.LENGTH_SHORT).show();

                    double latf = addressfrom.getLatitude();
                    double lngf = addressfrom.getLongitude();
                    double latt = addressto.getLatitude();
                    double lngt = addressto.getLongitude();

                    goToLocationZoom(latf, lngf, 15);
                    setMarker(localityfrom, latf, lngf);
                    goToLocationZoom(latt, lngt, 15);
                    setMarker(localityto, latt, lngt);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    Marker marker1, marker2;
    Polyline line;

    private void setMarker(String locality, double lat, double lng) {
        MarkerOptions options ;

        if(null == marker1)
        {
            options  = new MarkerOptions().title(locality)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .position(new LatLng(lat, lng));
            marker1 = map.addMarker(options);

        }
        else if(null == marker2) {
            options  = new MarkerOptions().title(locality)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .position(new LatLng(lat, lng));

            marker2 = map.addMarker(options);
            drawLine();
        }
        else {
            removeEverything();
            options  = new MarkerOptions().title(locality)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .position(new LatLng(lat, lng));
            marker1 = map.addMarker(options);
        }
    }

    private void drawLine() {
        PolylineOptions options = new PolylineOptions().add(marker1.getPosition())
                .add(marker2.getPosition()).color(Color.BLUE).width(10);

        line = map.addPolyline(options);
        int distance = (int) SphericalUtil.computeDistanceBetween(marker1.getPosition(), marker2.getPosition());
        dist.setText( "Distance : "+ distance/1000 +"  Kilometers");
        //Toast.makeText(this, "Distance : "+ distance/1000 +"  Kilometers", Toast.LENGTH_LONG).show();
    }

    private void removeEverything() {
        marker1.remove();
        marker1=null;
        marker2.remove();
        marker2=null;
    }


    LocationRequest request;

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        request = LocationRequest.create();
        request.setPriority(request.PRIORITY_HIGH_ACCURACY);
        request.setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (null == location)
            Toast.makeText(this, "can't get current location", Toast.LENGTH_SHORT).show();
        else
        {
            LatLng lng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(lng, 15);
            map.animateCamera(update);
        }

    }
}
