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

public class ListKerusakanAdapter extends ArrayAdapter<String> {
    private final Activity context;
    List<String> listrecid = new ArrayList<String>();
    List<String> listidkerusakan = new ArrayList<String>();
    List<String> listkdkerusakan = new ArrayList<String>();
    List<String> listnmkerusakan = new ArrayList<String>();
    List<String> listkdpenyebab = new ArrayList<String>();
    public ListKerusakanAdapter(Activity context,
                                List<String> listrecid, List<String> listidkerusakan, List<String> listkdkerusakan, List<String> listnmkerusakan, List<String> listkdpenyebab) {
        super(context, R.layout.kerusakan_list, listrecid);

        this.context = context;
        this.listrecid = listrecid;
        this.listidkerusakan = listidkerusakan;
        this.listkdkerusakan = listkdkerusakan;
        this.listnmkerusakan = listnmkerusakan;
        this.listkdpenyebab = listkdpenyebab;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.kerusakan_list, null, true);
        TextView tvIdKerusakan = (TextView) rowView.findViewById(R.id.tvIdKerusakan);
        TextView tvKodeKerusakan = (TextView) rowView.findViewById(R.id.tvKodeKerusakan);
        TextView tvNamaKerusakan = (TextView) rowView.findViewById(R.id.tvNamaKerusakan);
        TextView tvKodePenyebab = (TextView) rowView.findViewById(R.id.tvKodePenyebab);
        tvIdKerusakan.setText(listidkerusakan.get(position));
        tvKodeKerusakan.setText(listkdkerusakan.get(position));
        tvNamaKerusakan.setText(listnmkerusakan.get(position));
        tvKodePenyebab.setText(listkdpenyebab.get(position));
        return rowView;
    }


}