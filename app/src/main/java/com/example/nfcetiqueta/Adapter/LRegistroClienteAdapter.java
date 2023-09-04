package com.example.nfcetiqueta.Adapter;

import android.content.Context;
import android.graphics.Color;
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
    final OnItemClickListener listener;
    private int selectedItem;

    ArrayList<LClienteAfiliados> listaOriginal;

    public void updateData(List<LClienteAfiliados> newData) {
        lClienteAfiliadosList.clear();
        lClienteAfiliadosList.addAll(newData);
        notifyDataSetChanged();
    }


    public interface  OnItemClickListener{

        void onItemClick(LClienteAfiliados item);

    }

    public LRegistroClienteAdapter(List<LClienteAfiliados> lClienteAfiliadosList, Context context, OnItemClickListener listener){

        this.lClienteAfiliadosList = lClienteAfiliadosList;
        this.context    = context;
        this.listener   = listener;
        selectedItem    = -1;

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

        LClienteAfiliados lClienteAfiliados = lClienteAfiliadosList.get(position);

        holder.textNFC.setText(lClienteAfiliadosList.get(position).getRfid());
        holder.textArticuloID.setText(lClienteAfiliadosList.get(position).getArticuloID());
        holder.textClienteID.setText(lClienteAfiliadosList.get(position).getClienteID());
        holder.textTipoCliente.setText(String.valueOf(lClienteAfiliadosList.get(position).getTipoCliente()));
        holder.textClienteRZ.setText(lClienteAfiliadosList.get(position).getClienteRZ());
        holder.textMontoDescuento.setText(String.valueOf(String.format("%.2f",lClienteAfiliadosList.get(position).getMontoDescuento())));

        holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));

        if (selectedItem == position) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#0dcaf0"));
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousItem = selectedItem;
                selectedItem = position;
                notifyItemChanged(previousItem);
                notifyItemChanged(position);

                listener.onItemClick(lClienteAfiliados);
            }
        });
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
        private TextView textClienteRZ;
        private TextView textMontoDescuento;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            cardView            = itemView.findViewById(R.id.cardlistaclienteafiliados);
            textNFC             = itemView.findViewById(R.id.textNFC);
            textArticuloID      = itemView.findViewById(R.id.textArticuloID);
            textClienteID       = itemView.findViewById(R.id.textClienteID);
            textTipoCliente     = itemView.findViewById(R.id.textTipoCliente);
            textClienteRZ       = itemView.findViewById(R.id.textClienteRZ);
            textMontoDescuento  = itemView.findViewById(R.id.textMontoDescuento);
        }
    }
}
