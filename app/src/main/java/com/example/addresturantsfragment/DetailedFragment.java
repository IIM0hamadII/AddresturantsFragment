package com.example.addresturantsfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedFragment extends Fragment {


    TextView tvName, tvPhone,tvdescreption,tvadress;
    ImageView ivCar;
    String Name,Phone,Photo;

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

        Bundle bundle =this.getArguments();


        Name=bundle.getString("Car");
        Phone=bundle.getString("Phone");

        Photo=bundle.getString("Photo");



        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        ivCar=getView().findViewById(R.id.DetailedCar);
        tvName=getView().findViewById(R.id.DetailedMan);
        tvdescreption=getView().findViewById(R.id.tvdescreption);
         tvadress=getView().findViewById(R.id.tvadress);



        tvName.setText(Name);
        tvPhone.setText("Contact: " + Phone);




        if ( Photo == null || Photo.isEmpty())
        {
            Toast.makeText(getActivity(), "no photo uploaded", Toast.LENGTH_LONG).show();

        }
        else {
            Picasso.get().load(Photo).into(ivCar);

        }

    }
}