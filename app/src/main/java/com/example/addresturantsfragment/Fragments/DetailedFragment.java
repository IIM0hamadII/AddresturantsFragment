package com.example.addresturantsfragment.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.addresturantsfragment.Activities.MainActivity;
import com.example.addresturantsfragment.Activities.map;
import com.example.addresturantsfragment.DataBase.FirebaseServices;
import com.example.addresturantsfragment.DataBase.Hotel;
import com.example.addresturantsfragment.DataBase.MyLocationListener;
import com.example.addresturantsfragment.R;
import com.google.android.gms.maps.MapView;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedFragment extends Fragment {

    private static final int PERMISSION_SEND_SMS = 1;
    private static final int REQUEST_CALL_PERMISSION = 2;
    FirebaseServices fbs;
    private TextView tvName, tvPhone, tvdescreption, tvadress;
    private ImageView ivHotel;
    private MapView map;
    private LocationManager locationManager;
    private static final int GALLERY_REQUEST_CODE = 134;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private Button btnWhatsapp, btnCall;
    private Hotel myHotel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailedFragment newInstance(String param1, String param2) {
        DetailedFragment fragment = new DetailedFragment();
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
        View view = inflater.inflate(R.layout.fragment_detailed, container, false);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        fbs = FirebaseServices.getInstance();
        tvName = getView().findViewById(R.id.DetailedMan);
        tvdescreption = getView().findViewById(R.id.tvdescreption);
        tvadress = getView().findViewById(R.id.tvadress);
        tvPhone=getView().findViewById(R.id.DetailedPhone);
        ivHotel=getView().findViewById(R.id.DetailedCar);
        map=getView().findViewById(R.id.map);
        if(fbs.getSelectedHotel()!=null) myHotel = fbs.getSelectedHotel();

        if (myHotel != null) {

            tvName.setText(myHotel.getName());
            tvPhone.setText(myHotel.getPhone());
            tvdescreption.setText(myHotel.getDescription());
            tvadress.setText(myHotel.getAddress());
            tvPhone.setText(myHotel.getPhone());

            if (myHotel.getPhoto() == null || myHotel.getPhoto().isEmpty()) {
                Picasso.get().load(R.drawable.ic_fav).into(ivHotel);
            } else {
                Picasso.get().load(myHotel.getPhoto()).into(ivHotel);
            }
        }

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),com.example.addresturantsfragment.Activities.map.class);
                startActivity(intent);
            }
        });

        btnCall = getView().findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = "";
                if(myHotel.getPhone()!=null) {

                    phoneNum = "tel:" + myHotel.getPhone();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNum));
                    startActivity(intent);

                }
            }
        });
        btnWhatsapp=getView().findViewById(R.id.btnWhatsApp);
        btnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWhatsAppMessage(v);

            }
        });


        requestLocationPermission();
    }
    private boolean checkAddressFormat(String address) {
        try {
            String[] arr = address.split(",");
            if (Arrays.stream(arr).count() != 2)
                return false;
            double lat = Double.parseDouble(arr[0]);
            double lng = Double.parseDouble(arr[1]);
            return  true;
        } catch (NumberFormatException e) {
            Log.e("DetailedFragment: checkAddressFormat: ", "String is not parseable to double.");
            return false;
        }
    }

    private void checkAndSendSMS() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SEND_SMS}, PERMISSION_SEND_SMS);
        } else {
            sendSMS();
        }
    }

    private void sendSMS() {
        String phoneNumber = myHotel.getPhone();
        String message = "I am Interested in your  "+myHotel.getName()+"  hotel: " + myHotel.getPhone();

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getActivity(), "SMS sent.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "SMS sending failed.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMS();
            } else {
                Toast.makeText(requireContext(), "Permission denied. Cannot send SMS.", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCall();
            }
        }
    }
    // TODO : check Phone number is not correct;
    public void sendWhatsAppMessage(View view) {

        String smsNumber = "+972"+myHotel.getPhone();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        //  Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, " I am Interested in your  " +myHotel.getName()+ "  hotel:  "  + myHotel.getPhone());
        sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
        sendIntent.setPackage("com.whatsapp");

        startActivity(sendIntent);
//        String phoneNumber ="+972"+ myCar.getPhone(); // Replace with the recipient's phone number
//        String message = "Hello, this is a WhatsApp message!"; // Replace with your message
//        String phoneNumber2=  phoneNumber;
//        boolean isWhatsAppInstalled  =isAppInstalled("com.whatsapp");
//
//        if(isWhatsAppInstalled ){
//            Intent intent=new Intent(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+phoneNumber+"&text="+message));
//            startActivity(intent);
//        }
//        else {
//            Toast.makeText(getActivity(), "whatsapp is not installed", Toast.LENGTH_SHORT).show();
//         }

//
//
//        // Create an intent with the ACTION_SENDTO action and the WhatsApp URI
//        Intent intent = new Intent(Intent.ACTION_SENDTO);
//        intent.setData(Uri.parse("smsto:" + phoneNumber));
//        intent.putExtra("sms_body", message);
//
//        // Verify if WhatsApp is installed
//        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//            startActivity(intent);
//        } else {
//            // WhatsApp is not installed
//            // You can handle this case as per your app's requirement
//        }
    }
    //  888 whatsapp setting
    private boolean isAppInstalled(String s) {
        PackageManager packageManager= getActivity().getPackageManager();
        boolean is_installed;
        try{
            packageManager.getPackageInfo(s,PackageManager.GET_ACTIVITIES);
            is_installed=true;
        } catch (PackageManager.NameNotFoundException e) {
            is_installed=false;
            throw new RuntimeException(e);
        }
        return  is_installed;
    }

    private void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            startCall();
        }
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
//        } else {
//            startCall();
//        }
    }

    private void startCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + myHotel.getPhone()));

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
    public void gotoHotelList()
    {
        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout,new AllHotelsFragment());
        ft.commit();

    }
    private void requestLocationPermission() {
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            startLocationUpdates();
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "Location permission is required for this app",
                    LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION
            );
        }
    }

    private void startLocationUpdates() {
        locationManager = (LocationManager) ((MainActivity)getActivity()).getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
           MyLocationListener locationListener = new MyLocationListener(getActivity());
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }
}
