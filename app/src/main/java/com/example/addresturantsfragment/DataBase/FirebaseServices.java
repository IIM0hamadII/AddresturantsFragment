package com.example.addresturantsfragment.DataBase;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.addresturantsfragment.Utilites.UserCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class FirebaseServices {
    private  static FirebaseServices instance;
    private FirebaseAuth  auth;
    private FirebaseFirestore fire;
    private FirebaseStorage storage;
    private Uri selectedImageURL;
    private User currentUser;
    private Hotel selectedHotel;
    private boolean userChangeFlag;


    public  FirebaseServices ()
    {
        auth=FirebaseAuth.getInstance();
        fire=FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();
        getCurrentObjectUser(new UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                // Access the currentUser here
                if (user != null) {
                    setCurrentUser(user);
                }
            }
        });

        userChangeFlag = false;
        selectedImageURL = null;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseFirestore getFire() {
        return fire;
    }
    public static FirebaseServices reloadInstance(){
        instance=new FirebaseServices();
        return instance;
    }

    public static void setInstance(FirebaseServices instance) {
        FirebaseServices.instance = instance;
    }

    public void setAuth(FirebaseAuth auth) {
        this.auth = auth;
    }

    public void setFire(FirebaseFirestore fire) {
        this.fire = fire;
    }

    public void setStorage(FirebaseStorage storage) {
        this.storage = storage;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }

    public  static FirebaseServices getInstance(){
        if (instance==null){
            instance=new FirebaseServices();

        }
        return instance;
    }


    public Hotel getSelectedHotel() {
        return selectedHotel;
    }

    public void setSelectedHotel(Hotel selectedHotel) {
        this.selectedHotel = selectedHotel;
    }

    public Uri getSelectedImageURL() {
        return selectedImageURL;
    }

    public void setSelectedImageURL(Uri selectedImageURL) {
        this.selectedImageURL = selectedImageURL;
    }
    public boolean isUserChangeFlag() {
        return userChangeFlag;
    }

    public void setUserChangeFlag(boolean userChangeFlag) {
        this.userChangeFlag = userChangeFlag;
    }

    public void getCurrentObjectUser(UserCallback callback) {        ArrayList<User> usersInternal = new ArrayList<>();
        fire.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dataSnapshot: queryDocumentSnapshots.getDocuments()){
                    User user = dataSnapshot.toObject(User.class);
                    if (auth.getCurrentUser() != null && auth.getCurrentUser().getEmail().equals(user.getEmail())) {
                        usersInternal.add(user);

                    }
                }
                if (usersInternal.size() > 0)
                    currentUser = usersInternal.get(0);

                callback.onUserLoaded(currentUser);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public User getCurrentUser()
    {
        return this.currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public boolean updateUser(User user)
    {
        final boolean[] flag = {false};
        // Reference to the collection
        String collectionName = "users";
        String firstNameFieldName = "firstName";
        String firstNameValue = user.getFirstName();
        String lastNameFieldName = "lastName";
        String lastNameValue = user.getLastName();
        String email = "email";
        String emailvalue = user.getEmail();
        String password = "password";
        String passwordvalue = user.getPassword();
        String phoneFieldName = "phone";
        String phoneValue = user.getPhone();
        String photoFieldName = "photo";
        String photoValue = user.getPhoto();
        String favoritesFieldName = "favorites";
        ArrayList<String> favoritesValue = user.getFavorites();


        // Create a query for documents based on a specific field
        Query query = fire.collection(collectionName).
                whereEqualTo(email, emailvalue);

        // Execute the query
        query.get()
                .addOnSuccessListener((QuerySnapshot querySnapshot) -> {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        // Get a reference to the document
                        DocumentReference documentRef = document.getReference();

                        // Update specific fields of the document
                        documentRef.update(
                                        firstNameFieldName, firstNameValue,
                                        lastNameFieldName, lastNameValue,
                                        phoneFieldName, phoneValue,
                                        photoFieldName, photoValue,
                                        email, emailvalue,
                                        password,passwordvalue,
                                        favoritesFieldName, favoritesValue

                                )
                                .addOnSuccessListener(aVoid -> {

                                    flag[0] = true;
                                })
                                .addOnFailureListener(e -> {
                                    System.err.println("Error updating document: " + e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error getting documents: " + e);
                });

        return flag[0];
    }


}
