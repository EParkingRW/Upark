package com.example.upark.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.upark.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View view;

    @SuppressLint("InflateParams")
    public InfoWindowAdapter(FragmentActivity aContext) {

        LayoutInflater inflater = (LayoutInflater) aContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.parking_marker, null);
    }

    @Override
    public View getInfoContents(@NonNull Marker marker) {

        if (marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
            marker.showInfoWindow();
        }else {
            marker.showInfoWindow();
        }
        return view;
    }

    @Override
    public View getInfoWindow(@NonNull final Marker marker) {

//        final String title = marker.getTitle();
//        final TextView titleUi = ((TextView) view.findViewById(R.id.title));
//        if (title != null) {
//            titleUi.setText(title);
//        } else {
//            titleUi.setText("");
//            titleUi.setVisibility(View.GONE);
//        }
//
//        final String snippet = marker.getSnippet();
//        final TextView snippetUi = ((TextView) view
//                .findViewById(R.id.snippet));
//
//        if (snippet != null) {
//
//            String[] SnippetArray = snippet.split(SEPARATOR);
//
//
//            snippetUi.setText(SnippetArray[0]);
//        } else {
//            snippetUi.setText("");
//        }

        return view;
    }
}