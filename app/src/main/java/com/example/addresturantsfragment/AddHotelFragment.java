package com.example.addresturantsfragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddHotelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddHotelFragment extends Fragment {

    private FirebaseServices fbs;
    private EditText etName, etDescription, etAddress, etPhone;
    private Button btnAdd;
    private Utils utils;
    private static final int GALLERY_REQUEST_CODE = 123;
    private ImageView img ;
    private String imageStr;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddHotelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddRestaurantFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddHotelFragment newInstance(String param1, String param2) {
        AddHotelFragment fragment = new AddHotelFragment();
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
        return inflater.inflate(R.layout.fragment_add_restaurant, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        connectComponents();
    }

    private void connectComponents() {
        fbs = FirebaseServices.getInstance();
        etName = getView().findViewById(R.id.etNameAddRestaurantFragment);
        etDescription = getView().findViewById(R.id.etDescAddRestaurantFragment);
        etAddress = getView().findViewById(R.id.etAddressAddRestaurantFragment);
        etPhone = getView().findViewById(R.id.etPhoneAddRestaurantFragment);
        btnAdd = getView().findViewById(R.id.btnAddAddRestaurantFragment);
        utils = Utils.getInstance();
        img = getView().findViewById(R.id.ivupload);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get data from screen
                addToFirebase();
        }});
    }

    private void addToFirebase() {

        String name = etName.getText().toString();
        String description = etDescription.getText().toString();
        String address = etAddress.getText().toString();
        String phone = etPhone.getText().toString();


        // data validation
        if (name.trim().isEmpty() || description.trim().isEmpty() ||
                address.trim().isEmpty() || phone.trim().isEmpty())
        {
            Toast.makeText(getActivity(), "Some fields are empty!", Toast.LENGTH_LONG).show();
            return;
        }

        // add data to
        //
        // firestore

        Hotel hotel;
        if (fbs.getSelectedImageURL() == null)
        {
            hotel = new Hotel(name, description, address, phone, "");
        }
        else
        {
            hotel = new Hotel(name, description, address, phone, fbs.getSelectedImageURL().toString());

        }
        fbs.getFire().collection("hotels").add(hotel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getActivity(), "Successfully added your hotel!", Toast.LENGTH_SHORT).show();
                gotoHotelList();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Failed to add your hotel: ", e.getMessage());
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && data != null) {
            Uri selectedImageUri = data.getData();
            img.setImageURI(selectedImageUri);
            utils.uploadImage(getActivity(), selectedImageUri);
        }
    }
    public void toBigImg(View view) {
    }
    public void gotoHotelList()
    {
        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout,new AllHotelsFragment());
        ft.commit();

    }
}
