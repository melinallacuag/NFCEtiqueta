package com.example.nfcetiqueta.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.nfcetiqueta.R;
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoRango;

import java.util.ArrayList;

public class TipoRangoAdapter extends ArrayAdapter<TipoRango> {

    private Context context;
    private ArrayList<TipoRango> tipoRangos;
    public Resources res;
    TipoRango currRowVal = null;
    LayoutInflater inflater;

    public TipoRangoAdapter(@NonNull Context context, int textViewResourceId, ArrayList<TipoRango> tipoRangos, Resources resLocal) {
        super(context, textViewResourceId, tipoRangos);
        this.context = context;
        this.tipoRangos    = tipoRangos;
        this.res     = resLocal;
        inflater      = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
    @Override
    public int getCount() {
        return tipoRangos.size();
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = inflater.inflate(R.layout.itemrango, parent, false);
        currRowVal = null;
        currRowVal = (TipoRango) tipoRangos.get(position);
        TextView label = (TextView) row.findViewById(R.id.spinnerItemRango);

        label.setText(currRowVal.getId());

        return row;
    }

}
