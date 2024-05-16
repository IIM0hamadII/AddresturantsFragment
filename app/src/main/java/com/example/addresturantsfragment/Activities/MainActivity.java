package com.example.addresturantsfragment.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.addresturantsfragment.Fragments.AddHotelFragment;
import com.example.addresturantsfragment.Fragments.AllHotelsFragment;
import com.example.addresturantsfragment.DataBase.FirebaseServices;
import com.example.addresturantsfragment.Fragments.ListFragmentType;
import com.example.addresturantsfragment.Fragments.LoginFragment;
import com.example.addresturantsfragment.Fragments.ProfileFragment;
import com.example.addresturantsfragment.R;
import com.example.addresturantsfragment.DataBase.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private FirebaseServices fbs;
    private BottomNavigationView bottomNavigationView;
    private ListFragmentType listType;
    private Stack<Fragment> fragmentStack = new Stack<>();
    private FrameLayout fragmentContainer;
    private User userData, user1;
    private ImageView imj;
    Intent btn;
    private static final String TAG = "MainActivity";
    private static final int FRAGMENT_CONTAINER_ID = R.id.frameLayout;


    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {

        getCacheDir().delete();

// Clear the app's data.
        getDatabasePath("app_database.db").delete();

// Clear the app's shared preferences.
        getSharedPreferences("app_preferences", Context.MODE_PRIVATE).edit().clear().apply();

          imj=findViewById(R.id.imageView4);
        fbs = FirebaseServices.getInstance();
        imj.setVisibility(View.VISIBLE);
        listType = ListFragmentType.Regular;
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        FirebaseUser user = fbs.getAuth().getCurrentUser();
        if (user != null) {
            // User is signed in.
            gotoHotelList();

        } else {
            // User is signed out.
            gotoLoginFragment();
            bottomNavigationView.setVisibility(View.GONE);
        }



        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);

                // Get the menu from the navigation view.
                Menu menu = navigationView.getMenu();

                // Get the admin navigation item.
                MenuItem adminItem = menu.findItem(R.id.action_add);

                // Check if the user is an admin.
                if (!isAdmin()) {
                    // Hide the admin navigation item.
                    adminItem.setVisible(false);
                }
                else {
                    adminItem.setVisible(true);
                }
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.action_home) {


                    selectedFragment = new AllHotelsFragment();
                }
                 else if ( item.getItemId() == R.id.action_add  ) {
                     selectedFragment = new AddHotelFragment();

                     }
                 else if (item.getItemId() == R.id.action_profile ) {
                    selectedFragment = new ProfileFragment();
                   }

                else if (item.getItemId() == R.id.action_signout) {

                    signout();
                    bottomNavigationView.setVisibility(View.GONE);
                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, selectedFragment).commit();

                }
                return true;
            }
        });
        fragmentContainer = findViewById(R.id.frameLayout);
        userData = getUserData();

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (fbs.getAuth().getCurrentUser() == null) {
            gotoLoginFragment();
            bottomNavigationView.setVisibility(View.GONE);

            pushFragment(new LoginFragment());
        } else {
            bottomNavigationView.setVisibility(View.VISIBLE);
            // fbs.getCurrentObjectUser();
            gotoHotelList();
            pushFragment(new AllHotelsFragment());
        }
    }

    public User getUserDataObject() {
        return this.userData;
    }
    private boolean isAdmin() {
        // Replace this with your own logic for checking if the user is an admin.
        // For example, you could check a list of known admin email addresses.
        String userEmail = fbs.getAuth().getCurrentUser().getEmail();
        return userEmail.equals("hamoudy1221h@gmail.com");
    }
    private void signout() {
        // Sign out the user from Firebase Authentication.
        fbs.getAuth().signOut();

        // Log a message to indicate that the user has signed out.
        Log.d(TAG, "User signed out.");

        // Clear the back stack and navigate to the login fragment.
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .replace(FRAGMENT_CONTAINER_ID, new LoginFragment())
                .commit();

        // Hide the bottom navigation view.
        bottomNavigationView.setVisibility(View.GONE);
    }


    private void gotoLoginFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, new LoginFragment());
        ft.commit();
        bottomNavigationView.setVisibility(View.GONE);
    }

    public void gotoHotelList() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, new AllHotelsFragment());
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (fragmentStack.size() > 1) {
            fragmentStack.pop(); // Remove the current fragment from the stack
            Fragment previousFragment = fragmentStack.peek(); // Get the previous fragment

            // Replace the current fragment with the previous one
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, previousFragment)
                    .commit();
        } else {
            super.onBackPressed(); // If there's only one fragment left, exit the app
        }
    }

    // Method to add a new fragment to the stack
    public void pushFragment(Fragment fragment) {
        fragmentStack.push(fragment);
        /*
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit(); */
    }

    public User getUserData() {
        final User[] currentUser = {null};
        try {
            fbs.getFire().collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    User user = document.toObject(User.class);
                                    if (fbs.getAuth().getCurrentUser() != null ) {
                                        //if (fbs.getAuth().getCurrentUser().getEmail().equals(user.getUsername())) {
                                        currentUser[0] = document.toObject(User.class);
                                        fbs.setCurrentUser(currentUser[0]);
                                    }
                                }
                            } else {
                                Log.e("AllRestActivity: readData()", "Error getting documents.", task.getException());
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "error reading!" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return currentUser[0];


    }
}