package com.example.addresturantsfragment.Activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.addresturantsfragment.DataBase.FirebaseServices;
import com.example.addresturantsfragment.DataBase.Hotel;
import com.example.addresturantsfragment.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class map extends AppCompatActivity {

        private GoogleMap mMap;
    private SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    Hotel hotel;
    private LocationManager locationManager;
    FirebaseServices fbs;
    Context context;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        init();
    }
    private void init() {
        fbs = FirebaseServices.getInstance();
        hotel= fbs.getSelectedHotel();
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        client = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(map.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(map.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }
    }
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Handle case where permission is not granted
            ActivityCompat.requestPermissions(map.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location !=null){
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                            MarkerOptions options = new MarkerOptions().position(latLng).title("You are here");
                            googleMap.addMarker(options);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                            // Add marker for hotel location
                            String address = hotel.getAddress();
                            LatLng hotelLatLng = getLocationFromAddress22(address);

                            if (hotelLatLng != null) {
                                MarkerOptions hotelOptions = new MarkerOptions().position(hotelLatLng).title("Hotel location");
                                googleMap.addMarker(hotelOptions);
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hotelLatLng, 10));
                            } else {
                                Toast.makeText(map.this, "Failed to get hotel location", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    private LatLng getCoordinatesFromAddress(String address) {

            // Split the address into latitude and longitude components
            String[] arr = address.split(",");
            System.out.println(arr[0] + "," + arr[1]);

            // Ensure there are exactly two components
            if (arr.length != 2) {
                Log.e("AddressFormat", "Invalid address format: " + address);
                return null;
            }

            // Parse latitude and longitude components into double values

            double lat = Double.parseDouble(arr[0].trim());
            double lng = Double.parseDouble(arr[1].trim());

            System.out.println(lat + "," + lng);

            // Check if latitude and longitude are within valid ranges
            if (Math.abs(lat) > 90 || Math.abs(lng) > 180) {
                Log.e("Coordinates", "Invalid coordinates: Latitude=" + lat + ", Longitude=" + lng);
                return null;
            }

            // Create a LatLng object using the parsed latitude and longitude
            return new LatLng(lat, lng);

    }


    public LatLng getLocationFromAddress22(String strAddress) {

        Geocoder coder = new Geocoder(this);
       Address address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = (Address) coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address;
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }


}
