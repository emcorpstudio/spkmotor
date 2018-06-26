package emcorp.studio.spkmotor.Adapter;

/**
 * Created by ASUS on 27/11/2015.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import emcorp.studio.spkmotor.R;

public class PenyebabAdapter extends ArrayAdapter<String> {
    private final Activity context;
    List<String> listkdpenyebab = new ArrayList<String>();
    List<String> listnmpenyebab = new ArrayList<String>();
    List<String> listtket = new ArrayList<String>();
    List<String> listfoto = new ArrayList<String>();
    public PenyebabAdapter(Activity context,
                           List<String> listkdpenyebab, List<String> listnmpenyebab, List<String> listtket, List<String> listfoto) {
        super(context, R.layout.row_list, listkdpenyebab);

        this.context = context;
        this.listkdpenyebab = listkdpenyebab;
        this.listnmpenyebab = listnmpenyebab;
        this.listtket = listtket;
        this.listfoto = listfoto;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.row_list, null, true);
        TextView tvKode = (TextView) rowView.findViewById(R.id.tvKode);
        TextView tvNama = (TextView) rowView.findViewById(R.id.tvNama);
        tvKode.setText(listkdpenyebab.get(position));
        tvNama.setText(listnmpenyebab.get(position));
        return rowView;
    }


}