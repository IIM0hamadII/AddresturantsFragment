package com.example.addresturantsfragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    private ImageView Profile;
    private TextView tvName,tvEmail;
    private AppCompatButton btnFav,btnCall,btnSet,btnBack;
    private  FirebaseServices fbs;
    private String imageStr;
    private static final int REQUEST_CALL_PERMISSION = 2;
    private boolean flagAlreadyFilled = false;
    Fragment selectedFragment = null;
    @Override
    public void onStart() {
        super.onStart();


        Profile=getView().findViewById(R.id.Profile);
        tvName=getView().findViewById(R.id.tvName);
        tvEmail=getView().findViewById(R.id.tvEmail);
        btnFav=getView().findViewById(R.id.btnfav1);
        btnCall=getView().findViewById(R.id.BtnCall);
        btnSet=getView().findViewById(R.id.btsSet);
        btnBack=getView().findViewById(R.id.Mainp);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = "";

                    phoneNum = "tel:" + "0506494372";
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNum));
                    startActivity(intent);


            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               gotoHotelList();

            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUpdateProfileFragment();
            }
        });

        fillUserData();
        flagAlreadyFilled = true;
    }

    private void gotoUpdateProfileFragment() {
        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout,new UpdateProfileFragment());
        ft.commit();
    }
    public void gotoHotelList()
    {
        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout,new AllHotelsFragment());
        ft.commit();
        setNavigationBarVisible();
    }
    private void setNavigationBarVisible() {
        ((MainActivity)getActivity()).getBottomNavigationView().setVisibility(View.VISIBLE);
    }
    private void fillUserData() {
        if (flagAlreadyFilled)
            return;
        User current = fbs.getCurrentUser();
        if (current != null)
        {
            tvName.setText(current.getFirstName());
            tvEmail.setText(current.getEmail());
            if (current.getPhoto() != null && !current.getPhoto().isEmpty()) {
                Picasso.get().load(current.getPhoto()).into(Profile);
                fbs.setSelectedImageURL(Uri.parse(current.getPhoto()));
            }
        }

    }
    private void startCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" +"0506494372" ));

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        }


//        Intent callIntent = new Intent(Intent.ACTION_CALL);
//        callIntent.setData(Uri.parse(myCar.getPhone()));
//
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
//            startActivity(callIntent);
//        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCall();
            }
        }

    }

}