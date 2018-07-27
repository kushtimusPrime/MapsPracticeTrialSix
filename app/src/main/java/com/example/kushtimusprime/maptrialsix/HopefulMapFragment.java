package com.example.kushtimusprime.maptrialsix;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HopefulMapFragment extends Fragment implements OnMapReadyCallback{
    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    private EditText addressEditText;
    private Button addressButton;
    private ImageButton zoomInButton;
    private ImageButton zoomOutButton;

    public HopefulMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView=inflater.inflate(R.layout.fragment_hopeful_map, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView=(MapView)mView.findViewById(R.id.mapFragmentView);
        addressEditText=mView.findViewById(R.id.addressEditText);
        addressButton=mView.findViewById(R.id.addressButton);
        zoomInButton=mView.findViewById(R.id.zoomInButton);
        zoomOutButton=mView.findViewById(R.id.zoomOutButton);
        if(mMapView!=null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap=googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        float cityLevel=12.0f;
        LatLng columbus=new LatLng(39.9612,-82.9988);
        //info window tester stuff
        LatLng melsHouse = new LatLng(40.057613, -83.082275);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(melsHouse)
                .title("Mel's House")
                .snippet("This is where I live")
                .icon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_BLUE));
        InfoWindowData info = new InfoWindowData();
        info.setImage("snowqualmie"); //idk if it can find this image? this is just the example
        info.setDateOfEvent("I am here every day"); //hotel and food were the defaults it gave but we can change
        info.setTickets("No tickets available");
        info.setTransport("Reach the site by bus, car and train.");
        CustomInfoWindow customInfoWindow = new CustomInfoWindow(this.getActivity());
        mGoogleMap.setInfoWindowAdapter(customInfoWindow);
        Marker m = mGoogleMap.addMarker(markerOptions);
        m.setTag(info);
        m.showInfoWindow();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(columbus,cityLevel));
        addressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(addressEditText.getText().toString())) {
                    String address=addressEditText.getText().toString();
                    ArrayList<Double> points=getLocationFromAddress(address);
                    try {
                        LatLng marker = new LatLng(points.get(0), points.get(1));
                        googleMap.addMarker(new MarkerOptions().position(marker).title("Marker is placed"));
                        float zoomLevel = 16.0f; //This goes up to 21
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, zoomLevel));
                        // mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
                    } catch (NullPointerException e) {
                        Toast.makeText(getContext(),"Please type a REAL address",Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getContext(),"Please type an address",Toast.LENGTH_LONG).show();
                }
            }
        });
        zoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.moveCamera(CameraUpdateFactory.zoomIn());
            }
        });
        zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.moveCamera(CameraUpdateFactory.zoomOut());
            }
        });
    }
    public ArrayList getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        ArrayList<Double> p1 = new ArrayList<>();

        try {
            try {
                address = coder.getFromLocationName(strAddress,5);
            } catch (Exception e) {
                address=new ArrayList<>();
            }
            if (address.size()==0) {
                return null;
            }
            Address location=address.get(0);
            double latitude=location.getLatitude();
            double longitude=location.getLongitude();
            p1.clear();
            p1.add(latitude);
            p1.add(longitude);

            return p1;
        } catch (Exception e) {
            Toast.makeText(getContext(),"error: "+e.getMessage(),Toast.LENGTH_LONG).show();

        }
        return p1;
    }
}
