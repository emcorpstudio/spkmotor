package emcorp.studio.spkmotor.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import emcorp.studio.spkmotor.Adapter.ListPenyebabAdapter;
import emcorp.studio.spkmotor.Library.Constant;
import emcorp.studio.spkmotor.R;

import static android.app.Activity.RESULT_OK;


public class PenyebabFragment extends Fragment {
    ListView list;
    private ProgressDialog progressDialog;
    List<String> listrecid = new ArrayList<String>();
    List<String> listidpenyebab = new ArrayList<String>();
    List<String> listkdpenyebab = new ArrayList<String>();
    List<String> listnmpenyebab = new ArrayList<String>();
    List<String> listfoto = new ArrayList<String>();
    List<String> listtket = new ArrayList<String>();
    Dialog dialogAdd;
    ImageView imgFoto;
    String value_report_img = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_penyebab, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setHasOptionsMenu(true);
        list = (ListView)view.findViewById(R.id.listView);
        LoadProcess();
        return view;
    }

    public void dialogAdd(final int pos){
        dialogAdd = new Dialog(getContext());
        dialogAdd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAdd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
//        dialogAdd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogAdd.getWindow().setBackgroundDrawableResource(android.R.color.white);
        dialogAdd.setCancelable(false);
        dialogAdd.setContentView(R.layout.dialog_penyebab);

        final EditText edtKode = (EditText) dialogAdd.findViewById(R.id.edtKode);
        final EditText edtPenyebab = (EditText) dialogAdd.findViewById(R.id.edtPenyebab);
        final EditText edtKeterangan = (EditText) dialogAdd.findViewById(R.id.edtKeterangan);
        imgFoto = (ImageView) dialogAdd.findViewById(R.id.imgFoto);
        final TextView tvTitle = (TextView)dialogAdd.findViewById(R.id.tvTitle);

        Button btnSimpan = (Button) dialogAdd.findViewById(R.id.btnSimpan);
        Button btnCancel = (Button) dialogAdd.findViewById(R.id.btnCancel);
        final Button btnDelete = (Button) dialogAdd.findViewById(R.id.btnDelete);
        Button btnClose = (Button) dialogAdd.findViewById(R.id.btnClose);

        if(pos>=0){
            //Delete or Update
            btnDelete.setText("HAPUS");
            edtKode.setText(listkdpenyebab.get(pos).replace("P",""));
            edtPenyebab.setText(listnmpenyebab.get(pos));
            edtKeterangan.setText(listtket.get(pos));
            Picasso.with(getContext())
                    .load(Constant.PICT_URL+listfoto.get(pos))
                    .placeholder(R.drawable.ic_noimage)
                    .into(imgFoto);
            tvTitle.setText("Ubah Data Penyebab");
            btnClose.setVisibility(View.VISIBLE);
            edtKode.setEnabled(false);
        }else{
            //Add
            edtKode.setEnabled(true);
            btnDelete.setText("BATAL");
            btnClose.setVisibility(View.INVISIBLE);
            tvTitle.setText("Simpan Data Penyebab");
        }

        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClick();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtKode.getText().toString().equals("")){
                    if(!edtPenyebab.getText().toString().equals("")){
                        if(!edtKeterangan.getText().toString().equals("")){
                            if(checkKode(edtKode.getText().toString())){
                                Toast.makeText(getContext(),"Kode sudah digunakan",Toast.LENGTH_SHORT).show();
                            }else{
                                if(pos>=0){
                                    AddData("P"+edtKode.getText().toString(),"IP"+edtKode.getText().toString(),"P"+edtKode.getText().toString(),edtPenyebab.getText().toString(),edtKeterangan.getText().toString(),value_report_img,"");
                                }else{
                                    //Add Process
                                    if(value_report_img.equals("")){
                                        Toast.makeText(getContext(),"Belum pilih gambar",Toast.LENGTH_SHORT).show();
                                        imgFoto.requestFocus();
                                    }else{
                                        AddData("","IP"+edtKode.getText().toString(),"P"+edtKode.getText().toString(),edtPenyebab.getText().toString(),edtKeterangan.getText().toString(),value_report_img,"");
                                    }
                                }
                            }
                        }else{
                            Toast.makeText(getContext(),"Keterangan belum diisi",Toast.LENGTH_SHORT).show();
                            edtKeterangan.requestFocus();
                        }
                    }else{
                        Toast.makeText(getContext(),"Penyebab belum diisi",Toast.LENGTH_SHORT).show();
                        edtPenyebab.requestFocus();
                    }
                }else{
                    Toast.makeText(getContext(),"Kode belum diisi",Toast.LENGTH_SHORT).show();
                    edtKode.requestFocus();
                }
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAdd.dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos>=0){
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    AddData("P"+edtKode.getText().toString(),"IP"+edtKode.getText().toString(),"P"+edtKode.getText().toString(),edtPenyebab.getText().toString(),edtKeterangan.getText().toString(),value_report_img,"1");
                                    dialog.dismiss();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Apakah data ingin dihapus?").setPositiveButton("Ya", dialogClickListener)
                            .setNegativeButton("Tidak", dialogClickListener).show();
                }else{
                    dialogAdd.dismiss();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAdd.dismiss();
            }
        });
        dialogAdd.show();
    }

    public void imageClick(){
        final CharSequence[] items = {"Pilih dari Gallery", "Hapus", "Batal"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Upload Gambar Penyebab");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Pilih dari Gallery")) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, 1);
                } else if (items[item].equals("Hapus")) {
                    imgFoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_noimage));
                    value_report_img = "";
                }else if (items[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null){
            try {
                Uri selectedImage = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                imgFoto.setImageURI(selectedImage);
                value_report_img = getStringImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void AddData(final String recid, final String idpenyebab, final String kdpenyebab, final String nmpenyebab, final String tket, final String foto, final String delete){
        progressDialog = new ProgressDialog(getContext());
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
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject userDetails = obj.getJSONObject("hasil");
                            String message = userDetails.getString("message");
                            String success = userDetails.getString("success");
                            if(success.equals("1")){
                                //Succes
                                dialogAdd.dismiss();
                                LoadProcess();
                                value_report_img = "";
                            }
                            Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
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
                                getContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("function", Constant.FUNCTION_PENYEBAB);
                params.put("key", Constant.KEY);
                params.put("recid", recid);
                params.put("idpenyebab", idpenyebab);
                params.put("kdpenyebab", kdpenyebab);
                params.put("nmpenyebab", nmpenyebab);
                params.put("tket", tket);
                params.put("foto", foto);
                params.put("delete", delete);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public boolean checkKode(String kode){
        boolean exist = false;
        for(int i=0;i<listkdpenyebab.size()-1;i++){
            if(listkdpenyebab.get(i).equals("P"+kode)){
                exist = true;
                break;
            }
            Log.d("CETAKNIH",listkdpenyebab.get(i)+" - P"+kode);
        }
        return exist;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                dialogAdd(-1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void LoadProcess(){
        progressDialog = new ProgressDialog(getContext());
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
                        listrecid.clear();
                        listidpenyebab.clear();
                        listkdpenyebab.clear();
                        listnmpenyebab.clear();
                        listfoto.clear();
                        listtket.clear();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(response.indexOf("null")>0){
                                Toast.makeText(getContext(),"Tidak ada data", Toast.LENGTH_SHORT).show();
                                list.setVisibility(View.GONE);
                            }else{
                                JSONArray jsonArray = obj.getJSONArray("hasil");
                                if(jsonArray.length()==0){
                                    Toast.makeText(getContext(),"Tidak ada data", Toast.LENGTH_SHORT).show();
                                    list.setVisibility(View.GONE);
                                }else{
                                    list.setVisibility(View.VISIBLE);
                                    for (int i=0; i<jsonArray.length(); i++) {
                                        JSONObject isiArray = jsonArray.getJSONObject(i);
                                        String recid = isiArray.getString("recid");
                                        String idpenyebab = isiArray.getString("idpenyebab");
                                        String kdpenyebab = isiArray.getString("kdpenyebab");
                                        String nmpenyebab = isiArray.getString("nmpenyebab");
                                        String foto = isiArray.getString("foto");
                                        String tket = isiArray.getString("tket");
                                        listrecid.add(recid);
                                        listidpenyebab.add(idpenyebab);
                                        listkdpenyebab.add(kdpenyebab);
                                        listnmpenyebab.add(nmpenyebab);
                                        listfoto.add(foto);
                                        listtket.add(tket);
                                        getAllDataKerusakan();
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
                                getContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("function", Constant.FUNCTION_LISTPENYEBAB);
                params.put("key", Constant.KEY);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void getAllDataKerusakan(){
        list.setAdapter(null);
        ListPenyebabAdapter adapter = new ListPenyebabAdapter(getActivity(),listrecid,listidpenyebab,listkdpenyebab,listnmpenyebab,listfoto,listtket);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dialogAdd(position);
                return true;
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

    }


}
