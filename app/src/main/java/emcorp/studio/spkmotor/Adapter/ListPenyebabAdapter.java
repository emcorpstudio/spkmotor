package emcorp.studio.spkmotor.Adapter;

/**
 * Created by ASUS on 27/11/2015.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import emcorp.studio.spkmotor.Library.Constant;
import emcorp.studio.spkmotor.R;

public class ListPenyebabAdapter extends ArrayAdapter<String> {
    private final Activity context;
    List<String> listrecid = new ArrayList<String>();
    List<String> listidpenyebab = new ArrayList<String>();
    List<String> listkdpenyebab = new ArrayList<String>();
    List<String> listnmpenyebab = new ArrayList<String>();
    List<String> listfoto = new ArrayList<String>();
    List<String> listtket = new ArrayList<String>();
    public ListPenyebabAdapter(Activity context,
                               List<String> listrecid, List<String> listidpenyebab, List<String> listkdpenyebab, List<String> listnmpenyebab, List<String> listfoto, List<String> listtket) {
        super(context, R.layout.penyebab_list, listrecid);

        this.context = context;
        this.listrecid = listrecid;
        this.listidpenyebab = listidpenyebab;
        this.listkdpenyebab = listkdpenyebab;
        this.listnmpenyebab = listnmpenyebab;
        this.listfoto = listfoto;
        this.listtket = listtket;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.penyebab_list, null, true);
        TextView tvIdPenyebab = (TextView) rowView.findViewById(R.id.tvIdPenyebab);
        TextView tvKodePenyebab = (TextView) rowView.findViewById(R.id.tvKodePenyebab);
        TextView tvNamaPenyebab = (TextView) rowView.findViewById(R.id.tvNamaPenyebab);
        TextView tvKeterangan = (TextView) rowView.findViewById(R.id.tvKeterangan);
        ImageView image = (ImageView) rowView.findViewById(R.id.image);
        tvIdPenyebab.setText(listidpenyebab.get(position));
        tvKodePenyebab.setText(listkdpenyebab.get(position));
        tvNamaPenyebab.setText(listnmpenyebab.get(position));
        tvKeterangan.setText(listtket.get(position));
        Picasso.with(getContext())
                .load(Constant.PICT_URL+listfoto.get(position))
                .placeholder(R.drawable.ic_logo)
                .into(image);
        return rowView;
    }


}