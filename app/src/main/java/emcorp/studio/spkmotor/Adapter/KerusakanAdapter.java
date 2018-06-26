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

public class KerusakanAdapter extends ArrayAdapter<String> {
    private final Activity context;
    List<String> listkdkerusakan = new ArrayList<String>();
    List<String> listnmkerusakan = new ArrayList<String>();
    List<String> listkdpenyebab = new ArrayList<String>();
    public KerusakanAdapter(Activity context,
                            List<String> listkdkerusakan, List<String> listnmkerusakan, List<String> listkdpenyebab) {
        super(context, R.layout.row_list, listkdkerusakan);

        this.context = context;
        this.listkdkerusakan = listkdkerusakan;
        this.listnmkerusakan = listnmkerusakan;
        this.listkdpenyebab = listkdpenyebab;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.row_list, null, true);
        TextView tvKode = (TextView) rowView.findViewById(R.id.tvKode);
        TextView tvNama = (TextView) rowView.findViewById(R.id.tvNama);
        tvKode.setText(listkdkerusakan.get(position));
        tvNama.setText(listnmkerusakan.get(position));
        return rowView;
    }


}