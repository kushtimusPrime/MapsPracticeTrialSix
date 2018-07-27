package com.example.kushtimusprime.maptrialsix;

import android.content.Context;
import android.graphics.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Context mContext=this;
    private EditText addressEditText;
    private Button addressButton;
    private ImageButton zoomInButton;
    private ImageButton zoomOutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        addressEditText=findViewById(R.id.addressEditText);
        addressButton=findViewById(R.id.addressButton);
        zoomInButton=findViewById(R.id.zoomInButton);
        zoomOutButton=findViewById(R.id.zoomOutButton);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
        CustomInfoWindow customInfoWindow = new CustomInfoWindow(this);
        mMap.setInfoWindowAdapter(customInfoWindow);
        Marker m = mMap.addMarker(markerOptions);
        m.setTag(info);
        m.showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(columbus,cityLevel));
        addressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(addressEditText.getText().toString())) {
                    String address=addressEditText.getText().toString();
                    ArrayList<Double> points=getLocationFromAddress(address);
                    try {
                        LatLng marker = new LatLng(points.get(0), points.get(1));
                        mMap.addMarker(new MarkerOptions().position(marker).title("Marker is placed"));
                        float zoomLevel = 16.0f; //This goes up to 21
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, zoomLevel));
                        // mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
                    } catch (NullPointerException e) {
                        Toast.makeText(mContext,"Please type a REAL address",Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(mContext,"Please type an address",Toast.LENGTH_LONG).show();
                }
            }
        });
        zoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.moveCamera(CameraUpdateFactory.zoomIn());
            }
        });
        zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.moveCamera(CameraUpdateFactory.zoomOut());
            }
        });
        // Add a marker in Sydney and move the camera
        // ArrayList<Double> myHouse=getLocationFromAddress("3930 Tarrington Lane");
        /*LatLng sydney = new LatLng(myHouse.get(0), myHouse.get(1));
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker at my house"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }
    public ArrayList getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this);
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
            Toast.makeText(mContext,"error: "+e.getMessage(),Toast.LENGTH_LONG).show();

        }
        return p1;
    }
}
