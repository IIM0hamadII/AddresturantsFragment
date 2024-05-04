package com.example.addresturantsfragment.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.addresturantsfragment.Fragments.MapsFragment;
import com.example.addresturantsfragment.R;

public class ChooseLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        // Load the MapsFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.map5, new MapsFragment())
                    .commit();
        }
    }
}