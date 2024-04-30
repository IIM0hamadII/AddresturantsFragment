package com.example.addresturantsfragment.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.addresturantsfragment.Activities.MainActivity;
import com.example.addresturantsfragment.DataBase.FirebaseServices;
import com.example.addresturantsfragment.DataBase.User;
import com.example.addresturantsfragment.R;
import com.example.addresturantsfragment.Utilites.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateProfile2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateProfile2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText etFirstName, etLastName, etPhone,etPassword,etEmail;
    private static final int GALLERY_REQUEST_CODE = 134;
    private Button btnUpdate;
    private ImageView ivUser,ivUser2;
    private FirebaseServices fbs;
    private Utils utils;
    private String imageStr;
    private boolean flagAlreadyFilled = false;

    public UpdateProfile2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateProfile2.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateProfile2 newInstance(String param1, String param2) {
        UpdateProfile2 fragment = new UpdateProfile2();
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
        return inflater.inflate(R.layout.fragment_update_profile2, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs = FirebaseServices.getInstance();
        etFirstName=getView().findViewById(R.id.etFirstnameUserDetailsEdit);
        etLastName=getView().findViewById(R.id.etLastnameUserDetailsEdit);
        etPhone=getView().findViewById(R.id.etPhoneUserDetailsEdit);
        etPassword=getView().findViewById(R.id.etPassword);
        etEmail=getView().findViewById(R.id.etEmail);
        ivUser2=getView().findViewById(R.id.ivUserUserDetailsEdit);
        btnUpdate = getView().findViewById(R.id.btnUpdateUserDetailsEdit);

        utils = Utils.getInstance();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Data validation
                String firstname = etFirstName.getText().toString();
                String lastname = etLastName.getText().toString();
                String phone = etPhone.getText().toString();
                String password= etPassword.getText().toString();
                String email= etEmail.getText().toString();

                if (firstname.trim().isEmpty() || lastname.trim().isEmpty() ||email.trim().isEmpty()|| password.trim().isEmpty()||
                        phone.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "some fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                User current = fbs.getCurrentUser();
                if (current != null)
                {
                    if (!current.getFirstName().equals(firstname)  ||
                            !current.getLastName().equals(lastname)    ||
                            !current.getPhone().equals(phone)          ||
                            !current.getPhoto().equals(fbs.getSelectedImageURL().toString())
                            || !current.getPassword().equals(password) || !current.getEmail().equals(email))

                    {
                        User user;
                        if (fbs.getSelectedImageURL() != null){
                            user = new User(firstname, lastname, phone,  fbs.getSelectedImageURL().toString(),password,email);

                            fbs.updateUser(user);
                            utils.showMessageDialog(getActivity(), "Data updated succesfully!");
                            fbs.reloadInstance();
                            fbs = FirebaseServices.reloadInstance();
                        }

                        else
                            user = new User(firstname, lastname,  phone, "", password,email);
                        fbs.updateUser(user);
                        utils.showMessageDialog(getActivity(), "Data updated succesfully ");
                        fbs.reloadInstance();
                        fbs = FirebaseServices.reloadInstance();

                    }
                    else
                    {
                        utils.showMessageDialog(getActivity(), "No changes!");

                    }
                }
            }
        });
        ivUser2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        fillUserData();
        flagAlreadyFilled = true;
    }

    private void fillUserData() {
        if (flagAlreadyFilled)
            return;
        User current = fbs.getCurrentUser();
        if (current != null)
        {
            etFirstName.setText(current.getFirstName());
            etLastName.setText(current.getLastName());
            etEmail.setText(current.getEmail());
            etPassword.setText(current.getPassword());

            etPhone.setText(current.getPhone());

        }

    }
    private void setNavigationBarVisible() {
        ((MainActivity)getActivity()).getBottomNavigationView().setVisibility(View.VISIBLE);
    }

    public void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                btnUpdate.setEnabled(false);
                // Get the image's URI
                final android.net.Uri imageUri = data.getData();

                // Load the image into the ImageView using an asynchronous task or a library like Glide or Picasso
                // For example, using Glide:
                Glide.with(this).load(imageUri).into(ivUser2);
                uploadImage(imageUri);
            }
        }
    }

    public void uploadImage(Uri selectedImageUri) {
        if (selectedImageUri != null) {
            imageStr = "images/" + UUID.randomUUID() + ".jpg"; //+ selectedImageUri.getLastPathSegment();
            StorageReference imageRef = fbs.getStorage().getReference().child("images/" + selectedImageUri.getLastPathSegment());

            UploadTask uploadTask = imageRef.putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //selectedImageUri = uri;
                            fbs.setSelectedImageURL(uri);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                    Toast.makeText(getActivity(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    btnUpdate.setEnabled(true);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                    btnUpdate.setEnabled(true);
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please choose an image first", Toast.LENGTH_SHORT).show();
        }
    }

}