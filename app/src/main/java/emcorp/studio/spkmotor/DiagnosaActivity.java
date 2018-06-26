package emcorp.studio.spkmotor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import emcorp.studio.spkmotor.Adapter.KerusakanAdapter;
import emcorp.studio.spkmotor.Adapter.PenyebabAdapter;
import emcorp.studio.spkmotor.Library.Constant;

public class DiagnosaActivity extends AppCompatActivity {
    ListView list;
    private ProgressDialog progressDialog;
    List<String> listkdkerusakan = new ArrayList<String>();
    List<String> listnmkerusakan = new ArrayList<String>();
    List<String> listkdpenyebab = new ArrayList<String>();
    List<String> listnmpenyebab = new ArrayList<String>();
    List<String> listtket = new ArrayList<String>();
    List<String> listfoto = new ArrayList<String>();
    boolean kodeCek = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        list = (ListView)findViewById(R.id.listView);
        setTitle("Diagnosa");
        LoadProcess("");
    }

    public void LoadProcess(final String kode){
        progressDialog = new ProgressDialog(DiagnosaActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constant.ROOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("CETAK",response);
                        progressDialog.dismiss();
                        listkdkerusakan.clear();
                        listnmkerusakan.clear();
                        listkdpenyebab.clear();
                        listnmpenyebab.clear();
                        listtket.clear();
                        listfoto.clear();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(response.indexOf("null")>0){
                                Toast.makeText(DiagnosaActivity.this,"Tidak ada data", Toast.LENGTH_SHORT).show();
                                list.setVisibility(View.GONE);
                            }else{
                                JSONArray jsonArray = obj.getJSONArray("hasil");
                                if(jsonArray.length()==0){
                                    Toast.makeText(DiagnosaActivity.this,"Tidak ada data", Toast.LENGTH_SHORT).show();
                                    list.setVisibility(View.GONE);
                                }else{
                                    list.setVisibility(View.VISIBLE);
                                    for (int i=0; i<jsonArray.length(); i++) {
                                        JSONObject isiArray = jsonArray.getJSONObject(i);
                                        if(kode.equals("")){
                                            String kdkerusakan = isiArray.getString("kdkerusakan");
                                            String nmkerusakan = isiArray.getString("nmkerusakan");
                                            String kdpenyebab = isiArray.getString("kdpenyebab");
                                            listkdkerusakan.add(kdkerusakan);
                                            listnmkerusakan.add(nmkerusakan);
                                            listkdpenyebab.add(kdpenyebab);
                                            getAllDataKerusakan();
                                            kodeCek = true;
                                            setTitle("Diagnosa");
                                        }else{
                                            String tket = isiArray.getString("tket");
                                            String nmpenyebab = isiArray.getString("nmpenyebab");
                                            String kdpenyebab = isiArray.getString("kdpenyebab");
                                            String foto = isiArray.getString("foto");
                                            listtket.add(tket);
                                            listnmpenyebab.add(nmpenyebab);
                                            listkdpenyebab.add(kdpenyebab);
                                            listfoto.add(foto);
                                            getAllDataKerusakan();
                                            kodeCek = false;
                                            setTitle("Diagnosa 2");
                                        }
                                    }
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
                                DiagnosaActivity.this,
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("function", Constant.FUNCTION_LISTMASALAHH);
                params.put("key", Constant.KEY);
                params.put("kode", kode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DiagnosaActivity.this);
        requestQueue.add(stringRequest);
//        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void getAllDataKerusakan(){
        list.setAdapter(null);
        KerusakanAdapter adapterKerusakan;
        PenyebabAdapter adapterPenyebab;
        if(kodeCek){
            adapterKerusakan = new KerusakanAdapter(DiagnosaActivity.this, listkdkerusakan,listnmkerusakan,listkdpenyebab);
            list.setAdapter(adapterKerusakan);
        }else{
            adapterPenyebab = new PenyebabAdapter(DiagnosaActivity.this,listkdpenyebab,listnmpenyebab,listtket,listfoto);
            list.setAdapter(adapterPenyebab);
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(kodeCek){
                    LoadProcess(listkdpenyebab.get(position));
                }else{
                    Intent i = new Intent(DiagnosaActivity.this,DetailActivity.class);
                    i.putExtra("FOTO",listfoto.get(position));
                    i.putExtra("NAMA",listnmpenyebab.get(position));
                    i.putExtra("KET",listtket.get(position));
                    startActivity(i);
                    finish();
                }
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                dialogAdd(position);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(DiagnosaActivity.this,MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            Intent i = new Intent(DiagnosaActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
