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
import com.example.nfcetiqueta.WebApiSVEN.Models.LCompany;

import java.util.ArrayList;

public class LCompanyAdapter extends ArrayAdapter<LCompany> {

    private Context context;
    private ArrayList<LCompany> lCompanies;
    public Resources res;
    LCompany currRowVal = null;
    LayoutInflater inflater;

    public LCompanyAdapter(@NonNull Context context, int textViewResourceId, ArrayList<LCompany> lCompanies, Resources resLocal) {
        super(context, textViewResourceId, lCompanies);
        this.context     = context;
        this.lCompanies  = lCompanies;
        this.res         = resLocal;
        inflater         = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
        return lCompanies.size();
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = inflater.inflate(R.layout.itemcompany, parent, false);
        currRowVal = null;
        currRowVal = (LCompany) lCompanies.get(position);
        TextView label = (TextView) row.findViewById(R.id.spinnerItemCompany);

        label.setText(currRowVal.getNames());

        return row;
    }

}
