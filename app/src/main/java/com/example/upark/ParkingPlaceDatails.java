package com.example.upark;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.core.util.Consumer;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.upark.helpers.ImageLoadTask;
import com.example.upark.models.Garage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParkingPlaceDatails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParkingPlaceDatails extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "garage";

    // TODO: Rename and change types of parameters
    private Garage garage;

    // from view variables

    private ImageView garage_image;
    private TextView garage_name;
    private TextView garage_address;
    private TextView garage_distance;
    private TextView working_time;
    private TextView garage_description;
    private TextView price_per_hour;

    public ParkingPlaceDatails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Parking_place_datails.
     */
    // TODO: Rename and change types and number of parameters
    public static ParkingPlaceDatails newInstance(Garage garage) {
        ParkingPlaceDatails fragment = new ParkingPlaceDatails();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM, garage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            garage = (Garage) getArguments().getSerializable(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.parking_lot_details, container, false);
        garage_image = view.findViewById(R.id.garage_image_view);
        garage_name = view.findViewById(R.id.garage_name);
        garage_address = view.findViewById(R.id.garage_address);
        garage_distance = view.findViewById(R.id.distance);
        working_time = view.findViewById(R.id.working_range);
        garage_description = view.findViewById(R.id.garage_description);
        price_per_hour = view.findViewById(R.id.price_per_hour);

        setFieldValues();
        return view;
    }
    public void setFieldValues(){
        try {
            garage_name.setText(garage.getName());
            garage_address.setText(garage.getAddress());
            garage_distance.setText(R.string._kg);
            working_time.setText(garage.getWorkingTime());
            garage_description.setText(garage.getDescription());
            price_per_hour.setText(garage.getHourFeesDisplay());



            Consumer<Bitmap> onImageRead = (image) -> garage_image.setImageBitmap(image);
            new ImageLoadTask(garage.getImageURL(), onImageRead).execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}