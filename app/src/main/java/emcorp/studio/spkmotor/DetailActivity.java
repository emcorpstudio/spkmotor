package emcorp.studio.spkmotor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import emcorp.studio.spkmotor.Library.Constant;

public class DetailActivity extends AppCompatActivity {
    ImageView imgFoto;
    TextView tvKerusakan,tvKeterangan;
    Button btnHome, btnUlangi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        btnHome = (Button)findViewById(R.id.btnHome);
        btnUlangi = (Button)findViewById(R.id.btnUlangi);
        tvKerusakan = (TextView)findViewById(R.id.tvKerusakan);
        tvKeterangan = (TextView)findViewById(R.id.tvKeterangan);
        imgFoto = (ImageView) findViewById(R.id.imgFoto);
        setTitle("Hasil");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tvKeterangan.setText(extras.getString("KET"));
            tvKerusakan.setText(extras.getString("NAMA"));
            if(!extras.getString("FOTO").equals("")){
                Picasso.with(DetailActivity.this)
                        .load(Constant.PICT_URL+extras.getString("FOTO"))
                        .error(R.drawable.ic_logo)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .into(imgFoto);
            }
        }

        btnUlangi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailActivity.this,DiagnosaActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
