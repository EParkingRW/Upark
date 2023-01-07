package com.example.upark.helpers.backend;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.util.Arrays;
import java.util.function.Consumer;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.upark.constants.Const;
import com.example.upark.constants.Endpoints;
import com.example.upark.models.Garage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GarageBackend {
    private static final String TAG = GarageBackend.class.getSimpleName();
    public static void getGarages(Context context, Consumer<List<Garage>> onGaragesReady){
        Log.d(TAG, "getData");

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Const.BACKEND_URL+Endpoints.garages, null,
                response -> {
                    final List<Garage> garages = new ArrayList<>();
                    try {
                        Log.d(TAG, "RESPONSE :"+ response.toString());
                        if(response.getInt("status") != 200){
                            throw new Resources.NotFoundException("Garage not found");
                        }
                        JSONObject data = response.getJSONObject("data");
                        JSONArray garagesJson = data.getJSONArray("rows");
                        for (int i = 0; i < garagesJson.length(); i++) {
                            try {
                                JSONObject eachGarage = garagesJson.getJSONObject(i);
                                garages.add(getGarageFromJson(eachGarage));
                            }catch (Exception e){
                                e.printStackTrace();
                                Log.e(TAG,"Garage conversion fail: "+ e.getMessage());
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, "Error: " + e.getMessage());
                    }

                    onGaragesReady.accept(garages);
                }, error -> Log.d(TAG,"fetch-ERROR:"+ error.toString()));
//        {
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> params = new HashMap<>();
//                params.put("x-rapidapi-host", "edamam-food-and-grocery-database.p.rapidapi.com");
//                params.put("x-rapidapi-key", "6b1d2a7879msh375f16e28cd9056p1d8f4bjsn415a8c9e4a27");
//
//                return params;
//            }
//        };
        requestQueue.add(request);
    }
    public static Garage getGarageFromJson(JSONObject garageJson) throws Exception{
        Garage garage = new Garage(garageJson.getString("id"));
        garage.setAddress(garageJson.getString("address"));
        garage.setName(garageJson.getString("name"));
        garage.setLatitude(garageJson.getDouble("latitude"));
        garage.setLongitude(garageJson.getDouble("longitude"));
        garage.setImageURL(garageJson.getString("imageUrl"));
        garage.setUserId(garageJson.getString("userId"));
        garage.setHourFees(garageJson.getInt("hourlyFee"));
        try {
            String open = garageJson.getString("openingTime");
            String close = garageJson.getString("closingTime");
            garage.setOpeningTime(open.substring(0,open.length() - 3));
            garage.setClosingTime(close.substring(0,open.length() - 3));
        }catch (Exception e){
            Log.d(TAG, "Error :"+ e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        garage.setDescription(garageJson.getString("description"));
        garage.setSlots(garageJson.getInt("slots"));
        garage.setTakenSlots(garageJson.getInt("takenSlots"));
//        garage.setCreatedAt(createdAt)
//        garage.setUpdatedAt(updatedAt)
        return garage;
    }
}
