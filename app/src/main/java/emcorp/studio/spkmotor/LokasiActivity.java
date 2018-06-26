package emcorp.studio.spkmotor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import emcorp.studio.spkmotor.Library.Constant;
import emcorp.studio.spkmotor.Library.GPSTracker;

public class LokasiActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    GPSTracker gps;
    private ProgressDialog progressDialog;
    List<String> listrecid = new ArrayList<String>();
    List<String> listnama = new ArrayList<String>();
    List<String> listalamat = new ArrayList<String>();
    List<String> listlongitude = new ArrayList<String>();
    List<String> listlatitude = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lokasi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Lokasi Bengkel");
        gps = new GPSTracker(LokasiActivity.this);
        try {
            MapsInitializer.initialize(LokasiActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LoadProcess();
    }

    public void LoadProcess(){
        progressDialog = new ProgressDialog(LokasiActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constant.ROOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("CETAK",response);
//                        Toast.makeText(getContext(),response,Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        listrecid.clear();
                        listnama.clear();
                        listalamat.clear();
                        listlongitude.clear();
                        listlatitude.clear();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(response.indexOf("null")>0){
                                Toast.makeText(LokasiActivity.this,"Tidak ada lokasi", Toast.LENGTH_SHORT).show();
                            }else{
                                JSONArray jsonArray = obj.getJSONArray("hasil");
                                if(jsonArray.length()==0){
                                    Toast.makeText(LokasiActivity.this,"Tidak ada lokasi", Toast.LENGTH_SHORT).show();
                                }else{
                                    for (int i=0; i<jsonArray.length(); i++) {
                                        JSONObject isiArray = jsonArray.getJSONObject(i);
                                        String recid = isiArray.getString("recid");
                                        String nama = isiArray.getString("nama");
                                        String alamat = isiArray.getString("alamat");
                                        String longitude = isiArray.getString("longitude");
                                        String latitude = isiArray.getString("latitude");
                                        listrecid.add(recid);
                                        listnama.add(nama);
                                        listalamat.add(alamat);
                                        listlongitude.add(longitude);
                                        listlatitude.add(latitude);
                                    }
                                    MapsProcess();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                LokasiActivity.this,
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("function", Constant.FUNCTION_LISTLOKASI);
                params.put("key", Constant.KEY);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LokasiActivity.this);
        requestQueue.add(stringRequest);
//        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        MapsProcess();
    }

    public void MapsProcess(){
        mMap.clear();
        Marker markerMap;
        for(int i=0;i<listlongitude.size();i++){
            double latitude = Double.valueOf(listlatitude.get(i));
            double longitude = Double.valueOf(listlongitude.get(i));
            Log.d("LONGLAT", String.valueOf(latitude)+" , "+ String.valueOf(longitude));
            MarkerOptions marker = new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
//                    .title(listnama.get(i)+" | "+listalamat.get(i))
                    .title(listnama.get(i))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mMap.addMarker(marker);
        }

        //Lokasi User
        final double latitude = gps.getLatitude();
        final double longitude = gps.getLongitude();
        MarkerOptions marker = new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Lokasi Anda")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
//        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_peta_small));
        mMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(12).build();
        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                if(!marker.getTitle().equals("Lokasi Anda")){
//                    findIdLoc(String.valueOf(marker.getPosition().latitude), String.valueOf(marker.getPosition().longitude));
//                    String origin = String.valueOf(latitude) + "," + String.valueOf(longitude);
//                    String destination = String.valueOf(marker.getPosition().latitude) + "," + String.valueOf(marker.getPosition().longitude);
//                    LoadRoute(origin,destination);
//                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(LokasiActivity.this,MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent i = new Intent(LokasiActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
