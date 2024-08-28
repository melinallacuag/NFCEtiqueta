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
import com.example.nfcetiqueta.WebApiSVEN.Models.LClientePuntos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LRegistroPuntosAdapter extends RecyclerView.Adapter<LRegistroPuntosAdapter.ViewHolder>{

    public List<LClientePuntos> lClientePuntosList;
    private Context context;
    final LRegistroPuntosAdapter.OnItemClickListener listener;
    private int selectedItem;

    ArrayList<LClientePuntos> listaOriginal;

    public void updateData(List<LClientePuntos> newData) {
        lClientePuntosList.clear();
        lClientePuntosList.addAll(newData);
        notifyDataSetChanged();
    }

    public interface  OnItemClickListener{

        void onItemClick(LClientePuntos item);

    }

    public LRegistroPuntosAdapter(List<LClientePuntos> lClientePuntosList, Context context, LRegistroPuntosAdapter.OnItemClickListener listener){

        this.lClientePuntosList = lClientePuntosList;
        this.context    = context;
        this.listener   = listener;
        selectedItem    = -1;

        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(lClientePuntosList);


    }

    @NonNull
    @Override
    public LRegistroPuntosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlistaclientepuntos,parent,false);
        return new LRegistroPuntosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LRegistroPuntosAdapter.ViewHolder holder, int position) {
        LClientePuntos lClientePuntos = lClientePuntosList.get(position);

        holder.textNFC.setText(lClientePuntosList.get(position).getRfid());
        holder.textArticuloID.setText(lClientePuntosList.get(position).getArticuloID());
        holder.textClienteID.setText(lClientePuntosList.get(position).getClienteID());
        holder.textClienteRZ.setText(lClientePuntosList.get(position).getClienteRZ());
        holder.textDisponible.setText(String.valueOf(lClientePuntosList.get(position).getDisponibles()));
        boolean status = lClientePuntosList.get(position).getStatus();
        holder.textEstado.setText(status ? "Activo" : "Inactivo");
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

                listener.onItemClick(lClientePuntos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lClientePuntosList.size();
    }

    public void filtrado(final String txtBuscar) {
        lClientePuntosList.clear();

        if (txtBuscar.isEmpty()) {
            lClientePuntosList.addAll(listaOriginal);
        } else {
            String txtBuscarLowerCase = txtBuscar.toLowerCase();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<LClientePuntos> filteredList = listaOriginal.stream()
                        .filter(i -> i.getClienteRZ().toLowerCase().contains(txtBuscarLowerCase))
                        .collect(Collectors.toList());
                lClientePuntosList.addAll(filteredList);
            } else {
                for (LClientePuntos comprobante : listaOriginal) {
                    if (comprobante.getClienteRZ().toLowerCase().contains(txtBuscarLowerCase)) {
                        lClientePuntosList.add(comprobante);
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
        private TextView textClienteRZ;
        private TextView textDisponible;
        private TextView textEstado;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            cardView            = itemView.findViewById(R.id.cardlistaclientepuntos);
            textNFC             = itemView.findViewById(R.id.textNFC);
            textArticuloID      = itemView.findViewById(R.id.textArticuloID);
            textClienteID       = itemView.findViewById(R.id.textClienteID);
            textClienteRZ       = itemView.findViewById(R.id.textClienteRZ);
            textDisponible      = itemView.findViewById(R.id.textDisponible);
            textEstado          = itemView.findViewById(R.id.textEstado);
        }
    }
}
