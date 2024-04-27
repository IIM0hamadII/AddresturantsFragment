package com.example.addresturantsfragment.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.addresturantsfragment.DataBase.FirebaseServices;
import com.example.addresturantsfragment.DataBase.Hotel;
import com.example.addresturantsfragment.DataBase.User;
import com.example.addresturantsfragment.R;
import com.example.addresturantsfragment.Utilites.HotelAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MapAddressFragment extends Fragment implements LocationListener {

    private LocationManager location;
    private FirebaseServices fbs;
    private ArrayList<Hotel> hotel;
    private User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        fbs = FirebaseServices.getInstance();
        hotel = new ArrayList<Hotel>();
        getHotels();
       user= fbs.getCurrentUser();


        View view= inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        //TODO: get current barbers list for map marker

        // Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // When map is loaded
                //googleMap.set

                addHotelMarkerLocations(googleMap);
                moveToCurrentLocation(googleMap, new LatLng(user.getLat(), user.getLng()));
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        // When clicked on map
                        // Initialize marker options
                        MarkerOptions markerOptions=new MarkerOptions();
                        // Set position of marker
                        markerOptions.position(latLng);
                        // Set title of marker
                        markerOptions.title(latLng.latitude+" : "+latLng.longitude);
                        // Remove all marker
                        googleMap.clear();
                        // Animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                        // Add marker on map
                        googleMap.addMarker(markerOptions);

                    }
                });
            }

            private void moveToCurrentLocation(GoogleMap googleMap, LatLng currentLocation)
            {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
                // Zoom in, animating the camera.
                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
            }

            private void addHotelMarkerLocations(GoogleMap googleMap) {
                for (Hotel b :hotel)
                {
                    LatLng barberLocation = new LatLng(b.getLat(), b.getLng());
                    googleMap.addMarker(new MarkerOptions()
                            .position(barberLocation)
                            .title(b.getName()));
                }
            }


        });
        // Return view
        return view;
    }


    public void getHotels()
    {

        try {
            hotel.clear();
            fbs.getFire().collection("hotels")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    hotel.add(document.toObject(Hotel.class));
                                }

                            } else {
                                Log.e("AllRestActivity: readData()", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
        catch (Exception e)
        {
            Log.e("getCompaniesMap(): ", e.getMessage());
        }


    }
    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}