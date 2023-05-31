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
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoDescuento;

import java.util.ArrayList;

public class TipoDescuentoAdapter extends ArrayAdapter<TipoDescuento> {

    private Context context;
    private ArrayList<TipoDescuento> tipoDescuentos;
    public Resources res;
    TipoDescuento currRowVal = null;
    LayoutInflater inflater;

    public TipoDescuentoAdapter(@NonNull Context context, int textViewResourceId, ArrayList<TipoDescuento> tipoDescuentos, Resources resLocal) {
        super(context, textViewResourceId, tipoDescuentos);
        this.context         = context;
        this.tipoDescuentos  = tipoDescuentos;
        this.res             = resLocal;
        inflater             = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
        return tipoDescuentos.size();
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = inflater.inflate(R.layout.itemdescuento, parent, false);
        currRowVal = null;
        currRowVal = (TipoDescuento) tipoDescuentos.get(position);
        TextView label = (TextView) row.findViewById(R.id.spinnerItemDescuento);

        label.setText(currRowVal.getId());

        return row;
    }

}
