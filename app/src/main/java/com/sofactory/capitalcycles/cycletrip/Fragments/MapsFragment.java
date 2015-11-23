
package com.sofactory.capitalcycles.cycletrip.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sofactory.capitalcycles.cycletrip.Activities.LoginActivity;
import com.sofactory.capitalcycles.cycletrip.Activities.MainActivity;
import com.sofactory.capitalcycles.cycletrip.DTOs.PosicionDTO;
import com.sofactory.capitalcycles.cycletrip.DTOs.RutaDTO;
import com.sofactory.capitalcycles.cycletrip.R;
import com.sofactory.capitalcycles.cycletrip.Tasks.RouteRegisterTask;
import com.sofactory.capitalcycles.cycletrip.Utils.Connections.HttpConnection;
import com.sofactory.capitalcycles.cycletrip.Utils.Maps.PathJSONParser;
import com.sofactory.capitalcycles.cycletrip.Utils.Preferences.UserPreferences;

import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class MapsFragment extends Fragment {

    private MapView mMapView;
    private GoogleMap googleMap;
    private GPSTracker gps;
    private double latitude;
    private double longitude;
    private SharedPreferences preferences;
    private Marker markerInitRoute;
    private Marker markerEndRoute;
    private LatLng latLngStartPoint;
    private LatLng latLngEndPoint;
    private Polyline polyline;
    private boolean track = false;
    private String userName;
    private String userLastName;
    private List<PosicionDTO> posicionDTOList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        posicionDTOList = new LinkedList<PosicionDTO>();
        preferences=getActivity().getApplicationContext().getSharedPreferences(LoginActivity.USER_PREFERENCES,Context.MODE_PRIVATE);
        View v = inflater.inflate(R.layout.fragment_maps, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately


        Button mButtonCalculateRoute = (Button) v.findViewById(R.id.buttonRoute);
        mButtonCalculateRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markerEndRoute != null) {
                    if (polyline != null) {
                        polyline.remove();
                    }
                    String url = getMapsApiDirectionsUrl();
                    ReadTask downloadTask = new ReadTask();
                    downloadTask.execute(url);
                } else {
                    Toast toast = Toast.makeText(getActivity(), R.string.warning_route_attempt, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        Button mButtonStartTracking = (Button) v.findViewById(R.id.buttonTrack);
        mButtonStartTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markerEndRoute != null && polyline!=null) {
                    track=true;
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLngStartPoint));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(22));


                } else {
                    Toast toast = Toast.makeText(getActivity(), R.string.warning_start_track, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        Button mButtonStopTracking = (Button) v.findViewById(R.id.buttonEndTrack);
        mButtonStopTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(track){
                        Long code = Long.parseLong(preferences.getString(UserPreferences.USER_CODE, ""));
                        RutaDTO rutaDTO = new RutaDTO();
                        rutaDTO.setId(code);
                        rutaDTO.setCodigoUsuario(code + "");
                        rutaDTO.setPosiciones(posicionDTOList);
                        RouteRegisterTask mRouteRegisterTask = new RouteRegisterTask(rutaDTO,getActivity());
                        mRouteRegisterTask.execute((Void) null);

                        PosicionDTO posicionInicial = posicionDTOList.get(0);
                        PosicionDTO posicionFinal = posicionDTOList.get(posicionDTOList.size()-1);
                        Long diferenciaMilisegundos = posicionFinal.getTiempo().getTime() - posicionInicial.getTiempo().getTime();
                        Long dias = diferenciaMilisegundos/(1000*60*60*24);
                        Long diferenciaSinDias = diferenciaMilisegundos - dias*(1000*60*60*24);
                        Long horas = diferenciaSinDias/(1000*60*60);
                        Long diferenciaSinHoras = diferenciaSinDias - horas*(1000*60*60);
                        Long minutos = diferenciaSinHoras/(1000*60);
                        Long diferenciaSinMinutos = diferenciaSinHoras - minutos*(1000*60);
                        Long segundos = diferenciaSinMinutos/(1000);

                        Geocoder geocoder;
                        List<Address> addresses = new ArrayList<Address>();
                        List<Address> addressesFinal = new ArrayList<Address>();
                        geocoder = new Geocoder(Locale.getDefault());

                        try {
                            addresses = geocoder.getFromLocation(posicionInicial.getLatitud(), posicionInicial.getLongitud(), 1);
                            addressesFinal = geocoder.getFromLocation(posicionFinal.getLatitud(), posicionFinal.getLongitud(), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String direccionInicial = addresses.get(0).getAddressLine(0);
                        String city = addresses.get(0).getLocality();
                        String direccionFinal = addressesFinal.get(0).getAddressLine(0);
                        String cityFinal = addressesFinal.get(0).getLocality();

                        float distancia = 0;
                        for(int i = 1; i < posicionDTOList.size(); i++)
                        {
                            PosicionDTO punto1 = posicionDTOList.get(i - 1);
                            Location loc1 = new Location("");
                            loc1.setLatitude(punto1.getLatitud());
                            loc1.setLongitude(punto1.getLongitud());

                            PosicionDTO punto2 = posicionDTOList.get(i);
                            Location loc2 = new Location("");
                            loc2.setLatitude(punto2.getLatitud());
                            loc2.setLongitude(punto2.getLongitud());

                            distancia += loc1.distanceTo(loc2);
                        }

                        String mensaje = "Dirección de inicio: " + direccionInicial + ", " + city + ",\n"
                                + "Dirección final: " + direccionFinal + ", " + cityFinal + ",\n"
                                + "Distancia recorrida: " + String.format("%.2f", distancia) + " metros.\n";
                        String duracionRecorrido = "Duración del recorrido: " + dias + " días, " + horas + " horas, " + minutos + " minutos, " + segundos + " segundos.";
                        mensaje += duracionRecorrido;
                        // Use the Builder class for convenient dialog construction
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(mensaje)
                                .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                        // Create the AlertDialog object
                        AlertDialog dialogResumenRecorrido = builder.create();
                        dialogResumenRecorrido.show();

                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                } else {
                    Toast toast = Toast.makeText(getActivity(), R.string.warning_stop_track, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        // latitude and longitude


        gps = new GPSTracker(getActivity());
        // check if GPS enabled
        if(gps.canGetLocation()){

             latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            latLngStartPoint = new LatLng(latitude,
                    longitude);

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        // create initial marker
        userName = preferences.getString(UserPreferences.USER_NAME,"");
        userLastName = preferences.getString(UserPreferences.USER_LASTNAME,"");
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title(userName.concat(" " + userLastName));

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory
                .fromResource(R.mipmap.ic_biker));

        // adding marker
        markerInitRoute = googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(16).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


        // Setting a click event handler for the map
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                if(markerEndRoute!=null){
                    markerEndRoute.remove();
                }
                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_finish_flag));

                // Animating to the touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                markerEndRoute = googleMap.addMarker(markerOptions);
                latLngEndPoint = new LatLng(latLng.latitude,latLng.longitude);

            }
        });

        // Perform any camera updates here
        return v;
    }





    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private String getMapsApiDirectionsUrl() {
        String originDest = "origin="
                + latLngStartPoint.latitude + "," + latLngStartPoint.longitude
                + "&destination="+ latLngEndPoint.latitude + ","
                + latLngEndPoint.longitude ;

        String key = "key="+getActivity().getString(R.string.google_maps_server_key);
        String params = originDest + "&" + key;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        return url;
    }


    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(10);
                polyLineOptions.color(Color.BLUE);
                polyLineOptions.geodesic(true);
            }

            polyline = googleMap.addPolyline(polyLineOptions);

        }
    }


    public class GPSTracker extends Service implements LocationListener {

        private final Context mContext;

        // flag for GPS status
        boolean isGPSEnabled = false;

        // flag for network status
        boolean isNetworkEnabled = false;

        // flag for GPS status
        boolean canGetLocation = false;

        Location location; // location
        double latitude; // latitude
        double longitude; // longitude

        // The minimum distance to change Updates in meters
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5; // 10 meters

        // The minimum time between updates in milliseconds
        private static final long MIN_TIME_BW_UPDATES = 1000 * 10; // 10 seconds

        // Declaring a Location Manager
        protected LocationManager locationManager;

        public GPSTracker(Context context) {
            this.mContext = context;
            getLocation();
        }

        public Location getLocation() {
            try {
                locationManager = (LocationManager) mContext
                        .getSystemService(LOCATION_SERVICE);

                // getting GPS status
                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                } else {
                    this.canGetLocation = true;
                    // First get location from Network Provider
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return location;
        }

        /**
         * Stop using GPS listener
         * Calling this function will stop using GPS in your app
         * */
        public void stopUsingGPS(){
            if(locationManager != null){
                locationManager.removeUpdates(GPSTracker.this);
            }
        }

        /**
         * Function to get latitude
         * */
        public double getLatitude(){
            if(location != null){
                latitude = location.getLatitude();
            }

            // return latitude
            return latitude;
        }

        /**
         * Function to get longitude
         * */
        public double getLongitude(){
            if(location != null){
                longitude = location.getLongitude();
            }

            // return longitude
            return longitude;
        }

        /**
         * Function to check GPS/wifi enabled
         * @return boolean
         * */
        public boolean canGetLocation() {
            return this.canGetLocation;
        }

        /**
         * Function to show settings alert dialog
         * On pressing Settings button will lauch Settings Options
         * */
        public void showSettingsAlert(){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

            // Setting Dialog Title
            alertDialog.setTitle("GPS is settings");

            // Setting Dialog Message
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }

        @Override
        public void onLocationChanged(Location location) {
            if (track) {
                markerInitRoute.remove();
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(latitude, longitude)).title(userName.concat(" " + userLastName));

                // Changing marker icon
                marker.icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_biker));
                markerInitRoute = googleMap.addMarker(marker);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(22));
                PosicionDTO posicionDTO = new PosicionDTO();
                posicionDTO.setLatitud(latitude);
                posicionDTO.setLongitud(longitude);
                posicionDTO.setTiempo(new Date());
                posicionDTOList.add(posicionDTO);

            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }





    }

}

