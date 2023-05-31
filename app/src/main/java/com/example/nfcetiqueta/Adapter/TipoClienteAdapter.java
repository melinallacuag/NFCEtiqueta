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
import com.example.nfcetiqueta.WebApiSVEN.Models.TipoCliente;

import java.util.ArrayList;

public class TipoClienteAdapter extends ArrayAdapter<TipoCliente> {

    private Context context;
    private ArrayList<TipoCliente> tipoClientes;
    public Resources res;
    TipoCliente currRowVal = null;
    LayoutInflater inflater;

    public TipoClienteAdapter(@NonNull Context context, int textViewResourceId, ArrayList<TipoCliente> tipoClientes, Resources resLocal) {
        super(context, textViewResourceId, tipoClientes);
        this.context       = context;
        this.tipoClientes  = tipoClientes;
        this.res           = resLocal;
        inflater           = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
        return tipoClientes.size();
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = inflater.inflate(R.layout.itemcliente, parent, false);
        currRowVal = null;
        currRowVal = (TipoCliente) tipoClientes.get(position);
        TextView label = (TextView) row.findViewById(R.id.spinnerItemCliente);

        label.setText(currRowVal.getId());

        return row;
    }


}
