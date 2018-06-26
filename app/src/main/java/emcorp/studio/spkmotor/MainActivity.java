package emcorp.studio.spkmotor;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import emcorp.studio.spkmotor.Library.ButtonClick;
import emcorp.studio.spkmotor.Library.Constant;

public class MainActivity extends AppCompatActivity {
    ImageButton btnDiagnosa, btnLokasi, btnList;
    boolean doubleBackToExitPressedOnce = false;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    Dialog dialogAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDiagnosa = (ImageButton)findViewById(R.id.btnDiagnosa);
        btnLokasi = (ImageButton)findViewById(R.id.btnLokasi);
        btnList = (ImageButton)findViewById(R.id.btnList);

        setTitle("Menu Utama");

        btnDiagnosa.setOnTouchListener(new ButtonClick());
        btnLokasi.setOnTouchListener(new ButtonClick());
        btnList.setOnTouchListener(new ButtonClick());

        checkLocationPermission();

        btnDiagnosa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,DiagnosaActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,LokasiActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAdd(-1);
            }
        });
    }

    public void dialogAdd(final int pos){
        dialogAdd = new Dialog(MainActivity.this);
        dialogAdd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAdd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialogAdd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogAdd.getWindow().setBackgroundDrawableResource(android.R.color.white);
        dialogAdd.setCancelable(false);
        dialogAdd.setContentView(R.layout.dialog_password);

        final EditText edtKode = (EditText) dialogAdd.findViewById(R.id.edtKode);
        final TextView tvTitle = (TextView)dialogAdd.findViewById(R.id.tvTitle);
        edtKode.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());

        Button btnLogin = (Button) dialogAdd.findViewById(R.id.btnLogin);
        Button btnClose = (Button) dialogAdd.findViewById(R.id.btnClose);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtKode.getText().toString().equals("")){
                    if(edtKode.getText().toString().equals(Constant.PASSWORD)){
                        Intent i = new Intent(MainActivity.this,ListActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"Password salah",Toast.LENGTH_SHORT).show();
                        edtKode.requestFocus();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Password belum diisi",Toast.LENGTH_SHORT).show();
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
        dialogAdd.show();
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new android.app.AlertDialog.Builder(this)
                        .setTitle("Permission")
                        .setMessage("Dibutuhkan permission ke lokasi anda")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Klik dua kali untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
