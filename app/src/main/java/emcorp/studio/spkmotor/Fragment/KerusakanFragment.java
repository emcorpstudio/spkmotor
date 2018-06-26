package emcorp.studio.spkmotor.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import android.widget.CheckBox;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import emcorp.studio.spkmotor.Adapter.ListKerusakanAdapter;
import emcorp.studio.spkmotor.Library.Constant;
import emcorp.studio.spkmotor.Library.ListViewItemCheckboxBaseAdapter;
import emcorp.studio.spkmotor.Library.ListViewItemDTO;
import emcorp.studio.spkmotor.R;


public class KerusakanFragment extends Fragment {
    ListView list;
    private ProgressDialog progressDialog;
    List<String> listrecid = new ArrayList<String>();
    List<String> listidkerusakan = new ArrayList<String>();
    List<String> listkdkerusakan = new ArrayList<String>();
    List<String> listnmkerusakan = new ArrayList<String>();
    List<String> listkdpenyebab = new ArrayList<String>();
    Dialog dialogAdd;
    ListView listViewWithCheckbox;
    List<ListViewItemDTO> initItemList;
    List<String> listrecidsource = new ArrayList<String>();
    List<String> listidpenyebabsource = new ArrayList<String>();
    List<String> listkdpenyebabsource = new ArrayList<String>();
    List<String> listnmpenyebabsource = new ArrayList<String>();
    List<String> listfotosource = new ArrayList<String>();
    List<String> listtketsource = new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kerusakan, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setHasOptionsMenu(true);
        list = (ListView)view.findViewById(R.id.listView);
        LoadProcess();
        return view;
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

    private List<ListViewItemDTO> getInitViewItemDtoList(int pos)
    {
        List<ListViewItemDTO> ret = new ArrayList<ListViewItemDTO>();

        int length = listrecidsource.size();
        String source = "";
        if(pos>=0)source = listkdpenyebab.get(pos);
        for(int i=0;i<length;i++)
        {
            String itemText = listkdpenyebabsource.get(i)+" - "+listnmpenyebabsource.get(i);

            ListViewItemDTO dto = new ListViewItemDTO();
            if(pos>=0){
                if(source.indexOf(listkdpenyebabsource.get(i))>=0){
                    dto.setChecked(true);
                }else{
                    dto.setChecked(false);
                }
//                Log.d("CEKNIH",String.valueOf(i)+" - "+listkdpenyebabsource.get(i)+" in "+source+" - "+String.valueOf(source.indexOf(listkdpenyebabsource.get(i))>0));
            }else{
                dto.setChecked(false);
            }

            dto.setItemText(itemText);
            ret.add(dto);
        }

        return ret;
    }

    public void dialogAdd(final int pos){
        dialogAdd = new Dialog(getContext());
        dialogAdd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAdd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
//        dialogAdd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogAdd.getWindow().setBackgroundDrawableResource(android.R.color.white);
        dialogAdd.setCancelable(false);
        dialogAdd.setContentView(R.layout.dialog_kerusakan);

        final EditText edtKode = (EditText) dialogAdd.findViewById(R.id.edtKode);
        final EditText edtKerusakan = (EditText) dialogAdd.findViewById(R.id.edtKerusakan);
        final TextView tvTitle = (TextView)dialogAdd.findViewById(R.id.tvTitle);

        Button btnSimpan = (Button) dialogAdd.findViewById(R.id.btnSimpan);
        Button btnCancel = (Button) dialogAdd.findViewById(R.id.btnCancel);
        final Button btnDelete = (Button) dialogAdd.findViewById(R.id.btnDelete);
        Button btnClose = (Button) dialogAdd.findViewById(R.id.btnClose);
        listViewWithCheckbox = (ListView) dialogAdd.findViewById(R.id.listView);

        initItemList = getInitViewItemDtoList(pos);
        ListViewItemCheckboxBaseAdapter listViewDataAdapter = new ListViewItemCheckboxBaseAdapter(getContext(), initItemList);
        listViewDataAdapter.notifyDataSetChanged();

        // Set data adapter to list view.
        listViewWithCheckbox.setAdapter(listViewDataAdapter);

        listViewWithCheckbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
                // Get user selected item.
                Object itemObject = adapterView.getAdapter().getItem(itemIndex);

                // Translate the selected item to DTO object.
                ListViewItemDTO itemDto = (ListViewItemDTO)itemObject;

                // Get the checkbox.
                CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.list_view_item_checkbox);

                // Reverse the checkbox and clicked item check state.
                if(itemDto.isChecked())
                {
                    itemCheckbox.setChecked(false);
                    itemDto.setChecked(false);
                }else
                {
                    itemCheckbox.setChecked(true);
                    itemDto.setChecked(true);
                }
            }
        });

        if(pos>=0){
            //Delete or Update
            btnDelete.setText("HAPUS");
            edtKode.setText(listkdkerusakan.get(pos).replace("K",""));
            edtKerusakan.setText(listnmkerusakan.get(pos));
            edtKode.setEnabled(false);
            tvTitle.setText("Ubah Data Kerusakan");
            btnClose.setVisibility(View.VISIBLE);
        }else{
            //Add
            btnDelete.setText("BATAL");
            tvTitle.setText("Simpan Data Kerusakan");
            btnClose.setVisibility(View.INVISIBLE);
            edtKode.setEnabled(true);
        }
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtKode.getText().toString().equals("")){
                    if(!edtKerusakan.getText().toString().equals("")){
                        if(checkPenyebab().equals("")){
                            Toast.makeText(getContext(),"Penyebab kerusakan belum dipilih",Toast.LENGTH_SHORT).show();
                            listViewWithCheckbox.requestFocus();
                        }else{
                            if(pos>=0){
                                AddData("K"+edtKode.getText().toString(),"IK"+edtKode.getText().toString(),"K"+edtKode.getText().toString(),edtKerusakan.getText().toString(),checkPenyebab(),"");
                            }else{
                                //Add Process
                                if(checkKode(edtKode.getText().toString())){
                                    Toast.makeText(getContext(),"Kode sudah digunakan",Toast.LENGTH_SHORT).show();
                                }else{
                                    AddData("","IK"+edtKode.getText().toString(),"K"+edtKode.getText().toString(),edtKerusakan.getText().toString(),checkPenyebab(),"");
                                }
                            }
                        }

                    }else{
                        Toast.makeText(getContext(),"Kerusakan belum diisi",Toast.LENGTH_SHORT).show();
                        edtKerusakan.requestFocus();
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
                                    AddData("K"+edtKode.getText().toString(),"IK"+edtKode.getText().toString(),"K"+edtKode.getText().toString(),edtKerusakan.getText().toString(),checkPenyebab(),"1");
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

    public void AddData(final String recid, final String idkerusakan, final String kdkerusakan, final String nmkerusakan, final String kdpenyebab, final String delete){
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
                params.put("function", Constant.FUNCTION_KERUSAKAN);
                params.put("key", Constant.KEY);
                params.put("recid", recid);
                params.put("idkerusakan", idkerusakan);
                params.put("kdkerusakan", kdkerusakan);
                params.put("nmkerusakan", nmkerusakan);
                params.put("kdpenyebab", kdpenyebab);
                params.put("delete", delete);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public String checkPenyebab(){
        String kode = "";
        for(int i=0;i<initItemList.size();i++){
            if(initItemList.get(i).isChecked()){
                kode = kode + listkdpenyebabsource.get(i)+",";
            }
        }
        return kode;
    }

    public boolean checkKode(String kode){
        boolean exist = false;
        for(int i=0;i<listkdkerusakan.size()-1;i++){
            if(listkdkerusakan.get(i).equals("K"+kode)){
                exist = true;
                break;
            }
        }
        return exist;
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
                        listidkerusakan.clear();
                        listkdkerusakan.clear();
                        listnmkerusakan.clear();
                        listkdpenyebab.clear();
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
                                        String recid  = isiArray.getString("recid");
                                        String idkerusakan  = isiArray.getString("idkerusakan");
                                        String kdkerusakan  = isiArray.getString("kdkerusakan");
                                        String nmkerusakan  = isiArray.getString("nmkerusakan");
                                        String kdpenyebab  = isiArray.getString("kdpenyebab");
                                        listrecid.add(recid);
                                        listidkerusakan.add(idkerusakan);
                                        listkdkerusakan.add(kdkerusakan);
                                        listnmkerusakan.add(nmkerusakan);
                                        listkdpenyebab.add(kdpenyebab);
                                    }
                                    getAllDataKerusakan();
                                    LoadPenyebab();
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
                params.put("function", Constant.FUNCTION_LISTKERUSAKAN);
                params.put("key", Constant.KEY);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void LoadPenyebab(){
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
                        listrecidsource.clear();
                        listidpenyebabsource.clear();
                        listkdpenyebabsource.clear();
                        listnmpenyebabsource.clear();
                        listfotosource.clear();
                        listtketsource.clear();
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
                                        listrecidsource.add(recid);
                                        listidpenyebabsource.add(idpenyebab);
                                        listkdpenyebabsource.add(kdpenyebab);
                                        listnmpenyebabsource.add(nmpenyebab);
                                        listfotosource.add(foto);
                                        listtketsource.add(tket);
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
        ListKerusakanAdapter adapter = new ListKerusakanAdapter(getActivity(),listrecid,listidkerusakan,listkdkerusakan,listnmkerusakan,listkdpenyebab);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(),"Click - "+String.valueOf(position),Toast.LENGTH_SHORT).show();
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
