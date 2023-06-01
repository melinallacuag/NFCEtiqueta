package com.example.nfcetiqueta.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nfcetiqueta.R;
import com.example.nfcetiqueta.WebApiSVEN.Models.LClienteAfiliados;
import com.example.nfcetiqueta.WebApiSVEN.Models.LProductos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LRegistroClienteAdapter extends RecyclerView.Adapter<LRegistroClienteAdapter.ViewHolder>{

    public List<LClienteAfiliados> lClienteAfiliadosList;
    private Context context;

    ArrayList<LClienteAfiliados> listaOriginal;


    public LRegistroClienteAdapter(List<LClienteAfiliados> lClienteAfiliadosList, Context context){
        this.lClienteAfiliadosList = lClienteAfiliadosList;
        this.context    = context;

        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(lClienteAfiliadosList);


    }
    @NonNull
    @Override
    public LRegistroClienteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlistaclienteafiliados,parent,false);
        return new LRegistroClienteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LRegistroClienteAdapter.ViewHolder holder, int position) {
        holder.textNFC.setText(lClienteAfiliadosList.get(position).getRfid());
        holder.textArticuloID.setText(lClienteAfiliadosList.get(position).getArticuloID());
        holder.textClienteID.setText(lClienteAfiliadosList.get(position).getClienteID());
        holder.textTipoCliente.setText(String.valueOf(lClienteAfiliadosList.get(position).getTipoCliente()));
        holder.textTipoRango.setText(lClienteAfiliadosList.get(position).getTipoCliente());
        holder.textRango1.setText(String.valueOf(String.format("%.3f",lClienteAfiliadosList.get(position).getRango1())));
        holder.textRango2.setText(String.valueOf(String.format("%.3f",lClienteAfiliadosList.get(position).getRango2())));
        holder.textClienteRZ.setText(lClienteAfiliadosList.get(position).getClienteRZ());
        holder.textNroPlaca.setText(String.valueOf(lClienteAfiliadosList.get(position).getNroPlaca()));
        holder.textTipoDescuento.setText(lClienteAfiliadosList.get(position).getTipoDescuento());
        holder.textMontoDescuento.setText(String.valueOf(String.format("%.2f",lClienteAfiliadosList.get(position).getMontoDescuento())));
    }

    @Override
    public int getItemCount() {
        return lClienteAfiliadosList.size();
    }

    public void filtrado(final String txtBuscar) {
        lClienteAfiliadosList.clear();

        if (txtBuscar.isEmpty()) {
            lClienteAfiliadosList.addAll(listaOriginal);
        } else {
            String txtBuscarLowerCase = txtBuscar.toLowerCase();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<LClienteAfiliados> filteredList = listaOriginal.stream()
                        .filter(i -> i.getClienteRZ().toLowerCase().contains(txtBuscarLowerCase))
                        .collect(Collectors.toList());
                lClienteAfiliadosList.addAll(filteredList);
            } else {
                for (LClienteAfiliados comprobante : listaOriginal) {
                    if (comprobante.getClienteRZ().toLowerCase().contains(txtBuscarLowerCase)) {
                        lClienteAfiliadosList.add(comprobante);
                    }
                }
            }
        }

        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        private TextView textNFC;
        private TextView textArticuloID;
        private TextView textClienteID;
        private TextView textTipoCliente;
        private TextView textTipoRango;
        private TextView textRango1;
        private TextView textRango2;
        private TextView textClienteRZ;
        private TextView textNroPlaca;
        private TextView textTipoDescuento;
        private TextView textMontoDescuento;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            cardView            = itemView.findViewById(R.id.cardlistaclienteafiliados);
            textNFC             = itemView.findViewById(R.id.textNFC);
            textArticuloID      = itemView.findViewById(R.id.textArticuloID);
            textClienteID       = itemView.findViewById(R.id.textClienteID);
            textTipoCliente     = itemView.findViewById(R.id.textTipoCliente);
            textTipoRango       = itemView.findViewById(R.id.textTipoRango);
            textRango1          = itemView.findViewById(R.id.textRango1);
            textRango2          = itemView.findViewById(R.id.textRango2);
            textClienteRZ       = itemView.findViewById(R.id.textClienteRZ);
            textNroPlaca        = itemView.findViewById(R.id.textNroPlaca);
            textTipoDescuento   = itemView.findViewById(R.id.textTipoDescuento);
            textMontoDescuento  = itemView.findViewById(R.id.textMontoDescuento);
        }
    }
}
