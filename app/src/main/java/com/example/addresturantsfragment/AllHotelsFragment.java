package com.example.addresturantsfragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllHotelsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllHotelsFragment extends Fragment {
    private ArrayList<Hotel> list, filteredList;
    private RecyclerView recyclerView;
    private FirebaseServices fbs;
    private ArrayList<Hotel> rests;
    private RecyclerView rvRests;
    private HotelAdapter adapter;
    private FloatingActionButton btn;
    private Button hbtn;
    private HotelAdapter myAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllHotelsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllRestaurantsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllHotelsFragment newInstance(String param1, String param2) {
        AllHotelsFragment fragment = new AllHotelsFragment();
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
        return inflater.inflate(R.layout.fragment_all_restaurants, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView =getView().findViewById(R.id.rvRestaurantsRestFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        filteredList = new ArrayList<>();
        myAdapter = new HotelAdapter(getActivity(), list);
        recyclerView.setAdapter(myAdapter);
        hbtn=getView().findViewById(R.id.hbtn2);
        btn=getView().findViewById(R.id.floatingbtn);
        fbs = FirebaseServices.getInstance();
        rests = new ArrayList<>();
        rvRests = getView().findViewById(R.id.rvRestaurantsRestFragment);
        rvRests.setHasFixedSize(true);
        rvRests.setLayoutManager(new LinearLayoutManager(getActivity()));
        fbs.getFire().collection("hotels").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot dataSnapshot: queryDocumentSnapshots.getDocuments()){
                    Hotel rest = dataSnapshot.toObject(Hotel.class);

                    rests.add(rest);
                }

                adapter = new HotelAdapter(getContext(), rests);
                rvRests.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "No data available", Toast.LENGTH_SHORT).show();
                Log.e("AllHotelsFragment", e.getMessage());

            }

        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout,new AddHotelFragment());
                ft.commit();
                }

        });

        hbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout,new LoginFragment());
                ft.commit();
            }

        })
        ;}

    private void applyFilter(String query) {
        // TODO: add onBackspace - old and new query
        if (query.trim().isEmpty())
        {
            myAdapter = new HotelAdapter(getContext(), list);
            recyclerView.setAdapter(myAdapter);
            //myAdapter.notifyDataSetChanged();
            return;
        }
        filteredList.clear();
        for(Hotel car : list)
        {
            if (car.getPhone().toLowerCase().contains(query.toLowerCase()) ||
                    car.getAddress().toLowerCase().contains(query.toLowerCase()) ||
                    car.getDescription().toLowerCase().contains(query.toLowerCase()) ||
                    car.getName().toLowerCase().contains(query.toLowerCase()) ||
                    car.getPhoto().toLowerCase().contains(query.toLowerCase()))


                {
                        filteredList.add(car);
            }


        if (filteredList.size() == 0)
        {
            showNoDataDialogue();
            return;
        }
        myAdapter = new HotelAdapter(getContext(), filteredList);
        recyclerView.setAdapter(myAdapter);

        myAdapter.

                /*
        myAdapter.setOnItemClickListener(new myAdapter.OnItemClickListener())
        {
            @Override

            public void onItemClick(int position) {
                // Handle item click here
                String selectedItem = filteredList.get(position).getName();
                Toast.makeText(getActivity(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();
                Bundle args = new Bundle();
                args.putParcelable("car", filteredList.get(position)); // or use Parcelable for better performance
               DetailedFragment cd = new DetailedFragment();
                cd.setArguments(args);
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout,cd);
                ft.commit();
            }
        }; */
    }



    }
    private void showNoDataDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("No Results");
        builder.setMessage("Try again!");
        builder.show();
    }
}
